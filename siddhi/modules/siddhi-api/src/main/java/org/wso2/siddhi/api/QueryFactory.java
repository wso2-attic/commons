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

package org.wso2.siddhi.api;

import org.wso2.siddhi.api.condition.Condition;
import org.wso2.siddhi.api.condition.pattern.EveryCondition;
import org.wso2.siddhi.api.condition.pattern.FollowedByCondition;
import org.wso2.siddhi.api.condition.pattern.NonOccurrenceCondition;
import org.wso2.siddhi.api.condition.sequence.SequenceCondition;
import org.wso2.siddhi.api.condition.sequence.SequenceStarCondition;
import org.wso2.siddhi.api.condition.where.ConditionOperator;
import org.wso2.siddhi.api.condition.where.ExpressionCondition;
import org.wso2.siddhi.api.condition.where.SimpleCondition;
import org.wso2.siddhi.api.condition.where.WhereCondition;
import org.wso2.siddhi.api.condition.where.logical.AndCondition;
import org.wso2.siddhi.api.condition.where.logical.NotCondition;
import org.wso2.siddhi.api.condition.where.logical.OrCondition;
import org.wso2.siddhi.api.eventstream.EventStream;
import org.wso2.siddhi.api.eventstream.query.JoinQuery;
import org.wso2.siddhi.api.eventstream.query.PatternQuery;
import org.wso2.siddhi.api.eventstream.query.Query;
import org.wso2.siddhi.api.eventstream.query.SequenceQuery;
import org.wso2.siddhi.api.eventstream.query.SimpleQuery;
import org.wso2.siddhi.api.eventstream.query.inputstream.QueryInputStream;
import org.wso2.siddhi.api.eventstream.query.jointstream.InnerJoin;
import org.wso2.siddhi.api.eventstream.query.jointstream.Join;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * The factory for creating queries.
 */
public class QueryFactory {
    private static QueryFactory factory = null;

    private QueryFactory() {
    }

    /**
     * get the query factory instance
     *
     * @return the instance of query factory
     */
    public static QueryFactory getInstance() {
        if (null == factory) {
            factory = new QueryFactory();
        }
        return factory;
    }


    /**
     * create a simple query
     *
     * @param streamName       name of the query
     * @param outputDefinition output definition of the query
     * @param queryInputStream input event stream of the query
     * @param condition        query condition
     * @return a simple query
     */
    public Query createQuery(String streamName, OutputDefinition outputDefinition,
                             QueryInputStream queryInputStream, WhereCondition condition) {
        List<String> propertyList = outputDefinition.getPropertyList();
        if ("*".equals(propertyList.get(0))) { // If * (star) is given in the SELECT
            propertyList.remove(0);
            addAttrsToOutputDefinitionSimpleQuery(queryInputStream.getEventStream(), outputDefinition);
        } else {
            outputDefinition = convertToFullyQualifiedName(queryInputStream, propertyList);
        }

        return new SimpleQuery(streamName, outputDefinition, queryInputStream, condition);
    }

    /**
     * create a simple query
     *
     * @param streamName       name of the query
     * @param outputDefinition output definition of the query
     * @param queryInputStream input event stream of the query
     * @return a simple query
     */
    public Query createQuery(String streamName, OutputDefinition outputDefinition,
                             QueryInputStream queryInputStream) {
        return createQuery(streamName, outputDefinition, queryInputStream, null);
    }

    private OutputDefinition convertToFullyQualifiedName(QueryInputStream queryInputStream,
                                                         List<String> propertyList) {
        List<String> fullNamePropertyList = new ArrayList<String>();
        for (String attribute : propertyList) {
            if (attribute.contains("avg(") || attribute.contains("sum(") || attribute.contains("max(")
                || attribute.contains("min(") || attribute.contains("count(")) {   //to handle aggregations
                if (!attribute.contains(".")) {
                    String[] attributeFragments = attribute.split("\\(");
                    attribute = attributeFragments[0] + "(" + queryInputStream.getEventStream().getStreamId() + "." + attributeFragments[1];

                }
                if (!attribute.contains("=")) {
                    String[] attributeFragments1 = attribute.split("\\(");
                    String[] attributeFragments2 = attributeFragments1[1].split("\\.");
                    attribute = attributeFragments1[0] + "_" + attributeFragments2[1].replace(")", "");   // default
                }
            } else if (!attribute.contains("(")) {
                if (!attribute.contains(".")) {
                    String[] attributeFragments = attribute.split("=");
                    attribute = queryInputStream.getEventStream().getStreamId() + "." + attributeFragments[attributeFragments.length - 1];
                }
                if (!attribute.contains("=")) {
                    String[] attributeFragments = attribute.split("\\.");
                    attribute = attributeFragments[1] + "=" + attribute;
                }
            }
            fullNamePropertyList.add(attribute);
        }
        return new OutputDefinition(fullNamePropertyList);
    }

    /**
     * create a join query
     *
     * @param streamName       name of the query
     * @param outputDefinition output definition of the query
     * @param jointStream      input join stream
     * @param condition        query condition
     * @return a join query
     */
    public Query createQuery(String streamName, OutputDefinition outputDefinition,
                             Join jointStream, WhereCondition condition) {
        List<String> propertyList = outputDefinition.getPropertyList();
        if ("*".equals(propertyList.get(0))) { // If * (star) is given in the SELECT
            propertyList.remove(0);
            addAttrsToOutputDefinitionSJoinQuery(jointStream.getQueryLeftInputStream().getEventStream(), outputDefinition);
            addAttrsToOutputDefinitionSJoinQuery(jointStream.getQueryRightInputStream().getEventStream(), outputDefinition);
        }

        return new JoinQuery(streamName, outputDefinition, jointStream, condition);

    }

    private void addAttrsToOutputDefinitionSimpleQuery(EventStream eventStream,
                                                       OutputDefinition outputDefinition) {
        for (String attrName : eventStream.getNames()) {
            outputDefinition.getPropertyList().add(attrName + "=" + eventStream.getStreamId() + "." + attrName);
            //attrName=streamId.attrName
        }
    }

    private void addAttrsToOutputDefinitionSJoinQuery(EventStream eventStream,
                                                      OutputDefinition outputDefinition) {
        for (String attrName : eventStream.getNames()) {
            outputDefinition.getPropertyList().add(
                    eventStream.getStreamId() + "_" + attrName + "=" + eventStream.getStreamId() + "." + attrName);
            //streamId_attrName=streamId.attrName
        }
    }

    /**
     * create a pattern query
     *
     * @param streamName        name of the query
     * @param outputDefinition  output definition of the query
     * @param queryInputStreams input event streams of the query
     * @param condition         query condition
     * @return a pattern query
     */
    public Query createQuery(String streamName, OutputDefinition outputDefinition,
                             List<QueryInputStream> queryInputStreams,
                             FollowedByCondition condition) {
        return new PatternQuery(streamName, outputDefinition, queryInputStreams, condition);
    }


    /**
     * create a sequence query
     *
     * @param streamName        name of the query
     * @param outputDefinition  output definition of the query
     * @param queryInputStreams input event streams of the query
     * @param condition         query condition
     * @return a sequence query
     */
    public Query createQuery(String streamName, OutputDefinition outputDefinition,
                             List<QueryInputStream> queryInputStreams,
                             SequenceCondition condition) {
        return new SequenceQuery(streamName, outputDefinition, queryInputStreams, condition);
    }


    /**
     * create a AndCondition
     *
     * @param leftCondition  one of the conditions
     * @param rightCondition one of the conditions
     * @return a AndCondition
     */
    public AndCondition and(Condition leftCondition, Condition rightCondition) {
        return new AndCondition(leftCondition, rightCondition);
    }

    /**
     * create a OrCondition
     *
     * @param leftCondition  one of the conditions
     * @param rightCondition one of the conditions
     * @return a OrCondition
     */
    public OrCondition or(Condition leftCondition, Condition rightCondition) {
        return new OrCondition(leftCondition, rightCondition);
    }

    /**
     * create a NotCondition
     *
     * @param condition to be converted as a not condition
     * @return a NotCondition
     */
    public NotCondition not(Condition condition) {
        return new NotCondition(condition);
    }

    /**
     * Sending pattern of constraint in correct order.
     * The pattern: A => B => C => D means A followed by B followed by C followed by D
     *
     * @param condition1          first constraint (A)
     * @param condition2          second constraint (B)
     * @param followingConditions All other constraints (C,D,..)
     * @return a FollowedByCondition object which contains the pattern in the correct order
     */
    public FollowedByCondition pattern(Condition condition1,
                                       Condition condition2,
                                       Condition... followingConditions) {

        List<Condition> followedByConditionsList = new ArrayList<Condition>();
        followedByConditionsList.add(condition1);
        followedByConditionsList.add(condition2);
        followedByConditionsList.addAll(Arrays.asList(followingConditions));
        return new FollowedByCondition(followedByConditionsList);
    }


    /**
     * Create a sequence condition
     *
     * @param Condition1         first constraint (A)
     * @param Condition2         second constraint (B)
     * @param sequenceConditions All other constraints (C,D,..)
     * @return a SequenceCondition object which contains the sequence in the correct order
     */
    public SequenceCondition sequence(Condition Condition1,
                                      Condition Condition2,
                                      Condition... sequenceConditions) {
        List<Condition> sequenceConditionsList = new ArrayList<Condition>();
        sequenceConditionsList.add(Condition1);
        sequenceConditionsList.add(Condition2);
        sequenceConditionsList.addAll(Arrays.asList(sequenceConditions));
        return new SequenceCondition(sequenceConditionsList);
    }

    /**
     * create a EveryCondition from a series of conditions
     *
     * @param condition1     first condition
     * @param conditionArray series of conditions
     * @return an EveryCondition
     */
    public EveryCondition every(Condition condition1, Condition... conditionArray) {
        List<Condition> conditionList = new ArrayList<Condition>();
        conditionList.add(condition1);
        conditionList.addAll(Arrays.asList(conditionArray));
        return new EveryCondition(conditionList);
    }

    /**
     * create a SequenceStarCondition from a series of conditions
     *
     * @param condition condition
     * @return an SequenceStarCondition
     */
    public SequenceStarCondition star(Condition condition) {
        return new SequenceStarCondition(condition);
    }

    /**
     * create a simple condition
     *
     * @param leftProperty  left property of the condition
     * @param operator      condition operator
     * @param rightProperty right property of the condition
     * @return a simple condition
     */
    public SimpleCondition condition(String leftProperty, ConditionOperator operator,
                                     String rightProperty) {
        return new SimpleCondition(leftProperty, operator, rightProperty);
    }

    /**
     * create a complex condition
     *
     * @param condition Complex Expression Condition
     * @return a simple condition
     */
    public ExpressionCondition condition(String condition) {
        return new ExpressionCondition(condition);
    }

    /**
     * create a output definition
     *
     * @param property1     first property of the output definition
     * @param propertyArray series of properties of output definition
     * @return output definition
     */
    public OutputDefinition output(String property1, String... propertyArray) {
        List<String> propertyList = new ArrayList<String>();
        propertyList.add(property1);
        propertyList.addAll(Arrays.asList(propertyArray));
        return new OutputDefinition(propertyList);
    }

    /**
     * create list of input streams
     *
     * @param queryInputStream1     series of input streams
     * @param queryInputStreamArray first input eventstream
     * @return list of event streams
     */
    public List<QueryInputStream> inputStreams(QueryInputStream queryInputStream1,
                                               QueryInputStream... queryInputStreamArray) {

        List<QueryInputStream> queryInputStreamList = new ArrayList<QueryInputStream>();
        queryInputStreamList.add(queryInputStream1);
        queryInputStreamList.addAll(Arrays.asList(queryInputStreamArray));

        return queryInputStreamList;
    }

    /**
     * create a input eventstream
     *
     * @param eventStream
     * @return a input event eventstream                                                           r
     */
    public QueryInputStream from(EventStream eventStream) {
        return new QueryInputStream(eventStream);
    }

    /**
     * create a inner Join for two event streams
     *
     * @param queryRightInputStream one of the event streams to be joined
     * @param queryLeftInputStream  one of the event streams to be joined
     * @return a Inner Join
     */
    public Join innerJoin(QueryInputStream queryLeftInputStream,
                          QueryInputStream queryRightInputStream) {
        return new InnerJoin(queryLeftInputStream, queryRightInputStream);
    }

    /**
     * Defaults to inner join. see #innerJoin
     *
     * @param queryRightInputStream one of the event streams to be joined
     * @param queryLeftInputStream  one of the event streams to be joined
     * @return a Inner Join
     */
    public Join join(QueryInputStream queryLeftInputStream,
                     QueryInputStream queryRightInputStream) {
        return new InnerJoin(queryLeftInputStream, queryRightInputStream);
    }


    public List<String> groupBy(String groupByAttribute1, String... groupByAttributeArray) {
        List<String> eventStreamList = new ArrayList<String>();
        eventStreamList.add(groupByAttribute1);
        eventStreamList.addAll(Arrays.asList(groupByAttributeArray));
        return eventStreamList;
    }

    public NonOccurrenceCondition.FollowedBy nonOccurrence(Condition nonOccurringCondition) {
        return new NonOccurrenceCondition(nonOccurringCondition).getFollowedBy();
    }


    public Query createQuery(String streamName, OutputDefinition outputDefinition,
                             List<QueryInputStream> queryInputStreams,
                             Map<String, Condition> conditionDefMap,
                             FollowedByCondition condition) {

        return new PatternQuery(streamName, outputDefinition, queryInputStreams, condition);
    }


}
