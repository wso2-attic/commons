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

import org.mvel2.MVEL;
import org.wso2.siddhi.api.condition.Condition;
import org.wso2.siddhi.api.condition.where.ConditionOperator;
import org.wso2.siddhi.api.condition.where.SimpleCondition;
import org.wso2.siddhi.core.exception.InvalidQueryException;
import org.wso2.siddhi.core.exception.MoreComplexEventException;
import org.wso2.siddhi.core.exception.PropertyFormatException;
import org.wso2.siddhi.core.exception.SiddhiException;
import org.wso2.siddhi.core.exception.UndefinedPropertyException;
import org.wso2.siddhi.core.node.processor.executor.Executor;
import org.wso2.siddhi.core.node.processor.executor.simple.AbstractSimpleExecutor;
import org.wso2.siddhi.core.node.processor.executor.simple.Contains;
import org.wso2.siddhi.core.node.processor.executor.simple.Equal;
import org.wso2.siddhi.core.node.processor.executor.simple.GreatThan;
import org.wso2.siddhi.core.node.processor.executor.simple.GreatThanEqual;
import org.wso2.siddhi.core.node.processor.executor.simple.LessThan;
import org.wso2.siddhi.core.node.processor.executor.simple.LessThanEqual;
import org.wso2.siddhi.core.node.processor.executor.simple.NotEqual;
import org.wso2.siddhi.core.node.processor.executor.simple.property.ComplexProperty;
import org.wso2.siddhi.core.node.processor.executor.simple.property.Property;
import org.wso2.siddhi.core.node.processor.executor.simple.property.StreamProperty;
import org.wso2.siddhi.core.node.processor.executor.simple.property.ValueProperty;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;

import static org.wso2.siddhi.core.parser.ConditionParserUtil.nextUniqueString;

/**
 * to evaluate simple condition and get the appropriate Executor
 */
public class SimpleConditionParser {

    private ConditionParserUtil parserUtil;
    private Condition condition;
    private  DateFormat formatter = DateFormat.getDateInstance(DateFormat.LONG);

    public SimpleConditionParser(Condition condition, ConditionParserUtil parserUtil) {
        this.condition = condition;
        this.parserUtil = parserUtil;
    }


    /**
     * to parse the SimpleCondition and get the appropriate Executor
     * this can have the operators ==,  >=, <=, >, < and !=
     *
     * @param simpleCondition the simpleCondition that need to be parsed
     * @param state           the state of the state machine
     * @return simple Executor
     * @throws org.wso2.siddhi.core.exception.InvalidQueryException
     *
     * @throws org.wso2.siddhi.core.exception.PropertyFormatException
     *
     * @throws org.wso2.siddhi.core.exception.SiddhiException
     *
     * @throws org.wso2.siddhi.core.exception.UndefinedPropertyException
     *
     */
    public Executor parse(SimpleCondition simpleCondition, int state)
            throws PropertyFormatException, UndefinedPropertyException, InvalidQueryException,
                   SiddhiException {

        //  ==,  >=, <=, >, < or !=
        ConditionOperator conditionOperator = simpleCondition.getOperator();

        //left and right properties of the Condition
        String leftStringProperty = simpleCondition.getLeftProperty().trim();
        String rightStringProperty = simpleCondition.getRightProperty().trim();

        //left and right properties
        Property leftProperty = null;
        Property rightProperty = null;

        //The class of both the Properties
        Class comparingClass = null;

        //The streams relevant to the execution
        Set<String> checkingStreamNames = new HashSet<String>();

        boolean isBothSide = true;

        try {
            comparingClass = getComparingClass(leftStringProperty);
            if (comparingClass == null) {
                comparingClass = getComparingClass(rightStringProperty);
            } else {
                Class otherSideComparingClass = getComparingClass(rightStringProperty);
                if (null != otherSideComparingClass && !(comparingClass == otherSideComparingClass)) {
                    throw new MoreComplexEventException();
                }
            }
            leftProperty = getProperty(leftStringProperty, comparingClass, state);
            rightProperty = getProperty(rightStringProperty, comparingClass, state);
        } catch (MoreComplexEventException e) {
            /**
             * here the properties will be in form
             * A+B=..
             * ..=A+B
             * A=B
             * where A,B are different type of attributes and = is the operator
             */
        }

        if (null == leftProperty) {
            leftProperty = getProperty(leftStringProperty, state);
        }
        if (null == rightProperty) {
            rightProperty = getProperty(rightStringProperty, state);
        }

        if (null == leftProperty || null == rightProperty) {
            throw new UndefinedPropertyException("leftProperty: " + leftProperty + " or rightProperty: " + rightProperty + " is undefined ");
        }
        if (leftProperty instanceof ValueProperty && rightProperty instanceof ValueProperty) {
            throw new InvalidQueryException(" both rightPropertyType and leftPropertyType sides have value properties, rightPropertyType value :" + ((ValueProperty) rightProperty).getValue() + " leftPropertyType value :" + ((ValueProperty) leftProperty).getValue());
        } else if (leftProperty instanceof ValueProperty) {
            isBothSide = false;
            checkingStreamNames.add(parserUtil.moderatedStreamId(rightStringProperty));
        } else if (rightProperty instanceof ValueProperty) {
            isBothSide = false;
            checkingStreamNames.add(parserUtil.moderatedStreamId(leftStringProperty));
        } else {
            checkingStreamNames.add(parserUtil.moderatedStreamId(leftStringProperty));
            checkingStreamNames.add(parserUtil.moderatedStreamId(rightStringProperty));

        }

        switch (conditionOperator) {
            case EQUAL:
                return populateExecutor(new Equal<Object>(checkingStreamNames, comparingClass), leftProperty, rightProperty, isBothSide);
            case NOT_EQUAL:
                return populateExecutor(new NotEqual<Object>(checkingStreamNames, comparingClass), leftProperty, rightProperty, isBothSide);
            case GREATERTHAN:
                return populateExecutor(new GreatThan<Object>(checkingStreamNames, comparingClass), leftProperty, rightProperty, isBothSide);
            case LESSTHAN:
                return populateExecutor(new LessThan<Object>(checkingStreamNames, comparingClass), leftProperty, rightProperty, isBothSide);
            case GREATERTHAN_EQUAL:
                return populateExecutor(new GreatThanEqual<Object>(checkingStreamNames, comparingClass), leftProperty, rightProperty, isBothSide);
            case LESSTHAN_EQUAL:
                return populateExecutor(new LessThanEqual<Object>(checkingStreamNames, comparingClass), leftProperty, rightProperty, isBothSide);
            case CONTAINS:
                return populateExecutor(new Contains<Object>(checkingStreamNames, comparingClass), leftProperty, rightProperty, isBothSide);

        }
        return null;
    }

    /**
     * populate the Executor according to the the left and right properties
     *
     * @param simpleExecutor Executor inherits AbstractSimpleExecutor
     * @param leftProperty   the left property of the condition
     * @param rightProperty  the right property of the condition
     * @param bothSide       is whether to evealuate both side or is one side contains a value
     * @return a simpleExecutor
     * @throws SiddhiException
     */
    private static Executor populateExecutor(AbstractSimpleExecutor simpleExecutor, Property leftProperty, Property rightProperty, boolean bothSide) throws SiddhiException {
        if (bothSide) {  // evaluate where both LHS and RHS has attributes
            simpleExecutor.setEvaluateBothSide(true);
            simpleExecutor.assignLeftProperty(leftProperty);
            simpleExecutor.assignRightProperty(rightProperty);
        } else {
            simpleExecutor.setEvaluateBothSide(false);
            if (leftProperty instanceof ValueProperty) {
                simpleExecutor.assignValue((ValueProperty) leftProperty);
                simpleExecutor.assignRightProperty(rightProperty);
            } else {
                simpleExecutor.assignValue((ValueProperty) rightProperty);
                simpleExecutor.assignLeftProperty(leftProperty);
            }

        }
        return simpleExecutor;
    }

    /**
     * will generate the property
     *
     * @param stringProperty left or right string property of the condition
     * @param state          the state of the state machine
     * @return the property or null
     * @throws org.wso2.siddhi.core.exception.PropertyFormatException
     */
    private Property getProperty(String stringProperty, int state) throws PropertyFormatException {
        Matcher matcher = parserUtil.propertyPatternWithinStream.matcher(stringProperty);

        if (matcher.find()) {
            ComplexProperty property = new ComplexProperty();
            matcher.reset();
            while (matcher.find()) {
                String matchingString = matcher.group();
                if (!property.getStreamInputs().contains(matchingString)) {
                    property.getExpressionInputs().add(nextUniqueString(property.getExpressionInputs()));
                    property.getStreamInputs().add(matchingString);
                    stringProperty = stringProperty.replaceAll(matchingString, property.getExpressionInputs().get(property.getExpressionInputs().size() - 1));
                }
            }
            property.setCompiledExpression(MVEL.compileExpression(stringProperty));
            property.setExpressionPropertyPositionMap(parserUtil.createExpressionPropertyPositionMap(property.getStreamInputs(), property.getExpressionInputs(), state));
            return property;
        } else {

            try {
                //for values like 23+34/2
                Class comparingClass = getComparingClass(stringProperty);
                if (null != comparingClass) {
                    return new ValueProperty(MVEL.eval(stringProperty, comparingClass));
                    // leftPropertyType = 1;
                } else {
                    return new ValueProperty(MVEL.eval(stringProperty, String.class));
                    //leftValue = MVEL.eval(leftStringProperty, String.class);
                    //leftPropertyType = 4;

                }
            } catch (MoreComplexEventException e) {

            }

        }
        return null;
    }

    /**
     * will generate simple property based on the comparingClass
     *
     * @param stringProperty left or right string property of the condition
     * @param comparingClass the common class of both left and right properties
     * @param state          the state of the state machine
     * @return
     */
    private Property getProperty(String stringProperty, Class comparingClass, int state) {
        try {
            return new StreamProperty(parserUtil.getPosition(stringProperty, state));
        } catch (PropertyFormatException e) {
            if (Date.class == comparingClass) {

                try {
                    return new ValueProperty(formatter.parse(stringProperty));
                } catch (ParseException ex) {
                    return null;
                }
            }
            try {
                return new ValueProperty(parserUtil.getValue(stringProperty, comparingClass));
            } catch (PropertyFormatException ex) {
                return null;
            }
        }
    }

    /**
     * Get the class of which the property is referring to
     *
     * @param property left or right property of the condition
     * @return the class of which the property is refer to
     * @throws MoreComplexEventException
     */
    private Class getComparingClass(String property) throws MoreComplexEventException {
        Class comparingClass = null;
        /**
         * check if its in "StreamId.Attribute" format
         */
        if (parserUtil.propertyPatternStream.matcher(property).find()) {
            comparingClass = parserUtil.getPropertyAttributeType(parserUtil.moderateProperty(property));
        } else if (isDate(property)) {

            comparingClass = Date.class;
            /**
             *    if A.price + A.time is given
             *    and  if A.price  and A.time is same type (Long) then comparingClass Long will be returned
             *    else MoreComplexEventException is thrown
             *    Note this not handle Integer + Long
             */
        } else if (!parserUtil.propertyPatternNum.matcher(property).find()) {
            Matcher matcher = parserUtil.propertyPatternWithinStream.matcher(property);
            while (matcher.find()) {
                if (null == comparingClass) {
                    comparingClass = parserUtil.getPropertyAttributeType(matcher.group());
                } else if (!(comparingClass == parserUtil.getPropertyAttributeType(matcher.group()))) {
                    throw new MoreComplexEventException(comparingClass.toString() + " is not equal to " + matcher.group());
                }
            }
        }
        return comparingClass;
    }

    private boolean isDate(String property) {
        try {
            formatter.parse(property);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

}
