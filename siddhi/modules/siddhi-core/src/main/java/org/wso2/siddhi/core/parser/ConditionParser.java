/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.siddhi.core.parser;

import org.wso2.siddhi.api.condition.ExpirableCondition;
import org.wso2.siddhi.api.condition.Condition;
import org.wso2.siddhi.api.condition.pattern.EveryCondition;
import org.wso2.siddhi.api.condition.pattern.FollowedByCondition;
import org.wso2.siddhi.api.condition.pattern.NonOccurrenceCondition;
import org.wso2.siddhi.api.condition.sequence.SequenceCondition;
import org.wso2.siddhi.api.condition.sequence.SequenceStarCondition;
import org.wso2.siddhi.api.condition.where.ExpressionCondition;
import org.wso2.siddhi.api.condition.where.SimpleCondition;
import org.wso2.siddhi.api.condition.where.logical.AndCondition;
import org.wso2.siddhi.api.condition.where.logical.NotCondition;
import org.wso2.siddhi.api.condition.where.logical.OrCondition;
import org.wso2.siddhi.api.eventstream.EventStream;
import org.wso2.siddhi.core.exception.InvalidAttributeCastException;
import org.wso2.siddhi.core.exception.InvalidQueryException;
import org.wso2.siddhi.core.exception.PropertyFormatException;
import org.wso2.siddhi.core.exception.SiddhiException;
import org.wso2.siddhi.core.exception.UndefinedPropertyException;
import org.wso2.siddhi.core.node.processor.executor.AndExecutor;
import org.wso2.siddhi.core.node.processor.executor.Executor;
import org.wso2.siddhi.core.node.processor.executor.ExpressionExecutor;
import org.wso2.siddhi.core.node.processor.executor.FollowedByExecutor;
import org.wso2.siddhi.core.node.processor.executor.NonOccurrenceExecutor;
import org.wso2.siddhi.core.node.processor.executor.NotExecutor;
import org.wso2.siddhi.core.node.processor.executor.OrExecutor;
import org.wso2.siddhi.core.node.processor.executor.SequenceExecutor;

import java.util.ArrayList;
import java.util.List;

public class ConditionParser {

    //util
//    private static final Logger log = Logger.getLogger(ConditionParser.class);
    private SimpleConditionParser simpleConditionParser;
    private ConditionParserUtil parserUtil;

    private Condition condition;
    private EventStream[] eventStreams;
    private Executor executor = null;

    //used only for state machine
    private List<FollowedByExecutor> followedbyExecutorList = null;
    private List<SequenceExecutor> sequenceExecutorList = null;
    //State of the state machine, -1 if case is not for state machine
    int state = -1;

    //only used for negation in Pattern processing
    NonOccurrenceExecutor rootExecutor = null;

    /**
     * ConditionParser constructor
     *
     * @param condition       the condition that need to be parsed
     * @param eventStreams the List of streams available for the condition
     * @throws UndefinedPropertyException
     * @throws InvalidAttributeCastException
     * @throws InvalidQueryException
     * @throws org.wso2.siddhi.core.exception.PropertyFormatException
     *
     */
    public ConditionParser(Condition condition, EventStream[] eventStreams)
            throws UndefinedPropertyException,
                   InvalidAttributeCastException, InvalidQueryException, PropertyFormatException,
                   SiddhiException {
        this.condition = condition;
        this.eventStreams = eventStreams;
        generateExecutor();
    }

    private void generateExecutor()
            throws UndefinedPropertyException, InvalidAttributeCastException, InvalidQueryException,
                   PropertyFormatException, SiddhiException {
        parserUtil = new ConditionParserUtil(condition, eventStreams);
        if (condition instanceof FollowedByCondition) {
            followedbyExecutorList = generateFollowedByExecutorList((FollowedByCondition) condition);
        } else if (condition instanceof SequenceCondition) {
            //           parserList = OutputDefinitionParserUtil.findNameListOfAttribute((SequenceCondition) condition);
            sequenceExecutorList = generatePatternExecutorList((SequenceCondition) condition);
        } else {
            executor = parse(condition);
        }

    }

    public Executor getExecutor() {
        return executor;
    }

    public List<FollowedByExecutor> getFollowedbyExecutorList() {
        return followedbyExecutorList;
    }

    public List<SequenceExecutor> getSequenceExecutorList() {
        return sequenceExecutorList;
    }

    /**
     * And , NOT, Or, Followedby ,SimpleCondition
     *
     * @param condition Condition
     * @return Executor
     * @throws org.wso2.siddhi.core.exception.InvalidQueryException
     *
     * @throws org.wso2.siddhi.core.exception.PropertyFormatException
     *
     * @throws org.wso2.siddhi.core.exception.SiddhiException
     *
     * @throws org.wso2.siddhi.core.exception.InvalidAttributeCastException
     *
     * @throws org.wso2.siddhi.core.exception.UndefinedPropertyException
     *
     */
    private Executor parse(Condition condition)
            throws PropertyFormatException, SiddhiException, InvalidQueryException,
                   UndefinedPropertyException, InvalidAttributeCastException {

        Executor executor = null;

        if (condition instanceof AndCondition) {
            executor = evaluateAnd((AndCondition) condition);
        } else if (condition instanceof OrCondition) {
            executor = evaluateOr((OrCondition) condition);
        } else if (condition instanceof NotCondition) {
            executor = evaluateNot((NotCondition) condition);
        } else if (condition instanceof SimpleCondition) {
            executor = evaluateSimpleCondition((SimpleCondition) condition);
        } else if (condition instanceof ExpressionCondition) {
            executor = evaluateExpressionCondition((ExpressionCondition) condition);
        } else if (condition instanceof NonOccurrenceCondition) {
            executor = evaluateNonOccurrenceCondition((NonOccurrenceCondition) condition);
        }

        return executor;
    }

    /**
     * Generate and And Executor by evaluating the And condition
     *
     * @param andCondition And Condition
     * @return And Executor
     * @throws UndefinedPropertyException
     * @throws InvalidAttributeCastException
     * @throws InvalidQueryException
     * @throws SiddhiException
     * @throws org.wso2.siddhi.core.exception.PropertyFormatException
     *
     */
    private Executor evaluateAnd(AndCondition andCondition)
            throws InvalidQueryException, PropertyFormatException, UndefinedPropertyException,
                   InvalidAttributeCastException, SiddhiException {
        Condition leftCondition = andCondition.getLeftCondition();
        Condition rightCondition = andCondition.getRightCondition();
        return new AndExecutor(parse(leftCondition), parse(rightCondition));
    }

    /**
     * Generate and Or Executor by evaluating the Or condition
     *
     * @param orCondition Or Condition
     * @return Or Executor
     * @throws UndefinedPropertyException
     * @throws InvalidAttributeCastException
     * @throws InvalidQueryException
     * @throws SiddhiException
     * @throws org.wso2.siddhi.core.exception.PropertyFormatException
     *
     */
    private Executor evaluateOr(OrCondition orCondition)
            throws InvalidQueryException, PropertyFormatException, UndefinedPropertyException,
                   InvalidAttributeCastException, SiddhiException {
        Condition leftCondition = orCondition.getLeftCondition();
        Condition rightCondition = orCondition.getRightCondition();
        return new OrExecutor(parse(leftCondition), parse(rightCondition));
    }

    /**
     * Generate and Not Executor by evaluating the Not condition
     *
     * @param notCondition Not Condition
     * @return Noy Executor
     * @throws UndefinedPropertyException
     * @throws InvalidAttributeCastException
     * @throws InvalidQueryException
     * @throws SiddhiException
     * @throws org.wso2.siddhi.core.exception.PropertyFormatException
     *
     */
    private Executor evaluateNot(NotCondition notCondition)
            throws InvalidQueryException, PropertyFormatException, UndefinedPropertyException,
                   InvalidAttributeCastException, SiddhiException {
        return new NotExecutor(parse(notCondition));
    }

    /**
     * Generate and Simple Executor by evaluating the Simple condition, such as Equal, not Equal,
     * Greater than, Less than,Greater than equal, Less than equal,
     *
     * @param simpleCondition Simple Condition
     * @return Simple Executor
     * @throws UndefinedPropertyException
     * @throws InvalidAttributeCastException
     * @throws InvalidQueryException
     * @throws org.wso2.siddhi.core.exception.PropertyFormatException
     *
     * @throws SiddhiException
     */
    private Executor evaluateSimpleCondition(SimpleCondition simpleCondition)
            throws InvalidQueryException, PropertyFormatException, UndefinedPropertyException,
                   SiddhiException {
        if (null == simpleConditionParser) {
            simpleConditionParser = new SimpleConditionParser(condition, parserUtil);
        }
        return simpleConditionParser.parse(simpleCondition, state);
    }


    private Executor evaluateExpressionCondition(ExpressionCondition condition)
            throws SiddhiException, PropertyFormatException {
        List<String> inputPropertyList = condition.getStreamInputs();
        List<String> expressionPropertyList = condition.getExpressionInputs();
        return new ExpressionExecutor(condition.getExpression(), parserUtil.moderatedStreamIdList(inputPropertyList), parserUtil.createExpressionPropertyPositionMap(inputPropertyList, expressionPropertyList, state));
    }

    private Executor evaluateNonOccurrenceCondition(NonOccurrenceCondition condition)
            throws SiddhiException, PropertyFormatException, InvalidQueryException,
                   UndefinedPropertyException, InvalidAttributeCastException {
        Condition nonOccurringCondition = condition.getNonOccurringCondition();
        Condition followingCondition = condition.getFollowingCondition();
        Executor thisExecutor;
        if (null == rootExecutor) {
            rootExecutor = new NonOccurrenceExecutor();
            rootExecutor.setNonOccurringExecutor(parse(nonOccurringCondition));
            rootExecutor.setFollowingExecutor(parse(followingCondition));
            thisExecutor = rootExecutor;
            rootExecutor = null;
        } else {
            thisExecutor = new NonOccurrenceExecutor(parse(nonOccurringCondition), parse(followingCondition), rootExecutor);
        }

        return thisExecutor;
    }

    private List<FollowedByExecutor> generateFollowedByExecutorList(
            FollowedByCondition followedByCondition) throws
                                                     UndefinedPropertyException,
                                                     InvalidAttributeCastException,
                                                     InvalidQueryException, SiddhiException,
                                                     PropertyFormatException {
        List<FollowedByExecutor> executorList = new ArrayList<FollowedByExecutor>();
        List<Condition> conditionsList = followedByCondition.getFollowingConditions();
        state = 0;
        FollowedByExecutor previousFollowedByExecutor = null;

        for (Condition aCondition : conditionsList) {
            //Enabling the loop withing Every Executors
            if (aCondition instanceof EveryCondition) {
                List<Condition> everyConditionsList = ((EveryCondition) aCondition).getFollowingConditions();
                FollowedByExecutor firstFollowedByExecutor = null;
                for (int k = 0; k < everyConditionsList.size(); k++) {

                    Condition tmpCondition = everyConditionsList.get(k);
                    FollowedByExecutor fe = new FollowedByExecutor(state, parse(tmpCondition));

                    if (((ExpirableCondition) tmpCondition).isLifeTimeSet()) {
                        fe.setLifeTime(((ExpirableCondition) tmpCondition).getLifeTime());                  // Setting the lifetime
                    }

                    state++;
                    if (k == 0) {
                        firstFollowedByExecutor = fe;
                    }
                    if (k == everyConditionsList.size() - 1) {
                        fe.setNextEveryExecutor(firstFollowedByExecutor);
                    }
                    if (previousFollowedByExecutor != null) {
                        executorList.get(executorList.size() - 1).setNextExecutor(fe);
                    }
                    executorList.add(fe);
                    previousFollowedByExecutor = fe;
                }
            } else {
                FollowedByExecutor fe = new FollowedByExecutor(state, parse(aCondition));

                if (((ExpirableCondition) aCondition).isLifeTimeSet()) {
                    fe.setLifeTime(((ExpirableCondition) aCondition).getLifeTime());      //setting the life time
                }
                state++;
                if (previousFollowedByExecutor != null) {                                  // TODO check
                    executorList.get(executorList.size() - 1).setNextExecutor(fe);
                }
                executorList.add(fe);
                previousFollowedByExecutor = fe;
            }
        }
        for (FollowedByExecutor fe : executorList) {
            fe.initArrivedEventSize(state);
        }
        state = -1;
        return executorList;
    }

    private List<SequenceExecutor> generatePatternExecutorList(SequenceCondition sequenceCondition)
            throws UndefinedPropertyException, InvalidAttributeCastException, InvalidQueryException,
                   PropertyFormatException, SiddhiException {

        List<SequenceExecutor> executorList = new ArrayList<SequenceExecutor>();
        List<Condition> conditionsList = sequenceCondition.getPatternConditions();
        SequenceExecutor previousSequenceExecutor = null;
        state = 0;
        for (Condition aCondition : conditionsList) {
            org.wso2.siddhi.core.node.processor.executor.SequenceExecutor pe;
            if (aCondition instanceof SequenceStarCondition) {
                pe = new SequenceExecutor(state, parse(((SequenceStarCondition) aCondition).getCondition()));
                pe.setStarExecutor(true);
                if (((ExpirableCondition) ((SequenceStarCondition) aCondition).getCondition()).isLifeTimeSet()) {
                    pe.setLifeTime(((ExpirableCondition) ((SequenceStarCondition) aCondition).getCondition()).getLifeTime());      //setting the life time
                }
            } else {
                pe = new SequenceExecutor(state, parse(aCondition));
                pe.setStarExecutor(false);
                if (((ExpirableCondition) aCondition).isLifeTimeSet()) {
                    pe.setLifeTime(((ExpirableCondition) aCondition).getLifeTime());      //setting the life time
                }
            }
            state++;
            if (previousSequenceExecutor != null) {
                executorList.get(executorList.size() - 1).setNextExecutor(pe);
            }
            executorList.add(pe);
            previousSequenceExecutor = pe;
        }
        executorList.get(0).initArrivedEvents();

        // Enable FireEvent
        SequenceExecutor currentExecutor;
        SequenceExecutor nextExecutor = executorList.get(executorList.size() - 1);
        nextExecutor.enableFireEvent();

        for (int i = executorList.size() - 2; i >= 0; i--) {
            if (nextExecutor.isStarExecutor()) {
                currentExecutor = executorList.get(i);
                currentExecutor.enableFireEvent();
                nextExecutor = currentExecutor;
            } else {
                break;
            }
        }
        state = -1;
        return executorList;
    }
}
