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
import org.wso2.siddhi.api.condition.pattern.FollowedByCondition;
import org.wso2.siddhi.api.condition.sequence.SequenceCondition;
import org.wso2.siddhi.api.eventstream.EventStream;
import org.wso2.siddhi.api.util.OutputDefinitionParserUtil;
import org.wso2.siddhi.core.exception.PropertyFormatException;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConditionParserUtil {

    //Property Patterns
    public static final Pattern propertyPatternStream = Pattern.compile("^[a-zA-Z_\\$][a-zA-Z0-9_]*(\\.[a-zA-Z]+)?\\.[a-zA-Z_][a-zA-Z0-9_]*$");
    public final Pattern propertyPatternNum = Pattern.compile("^\\d+(\\.\\d+)?$");
    public final Pattern propertyPatternWithinStream = Pattern.compile("[a-zA-Z_\\$][a-zA-Z0-9_]*(\\.[a-zA-Z]+)?\\.[a-zA-Z_][a-zA-Z0-9_]*");


    private static final char[] symbols = new char[26];
    private static final Random random = new Random();
    private static final char[] buf = new char[7];

    EventStream[] eventStreams;
    List<String> streamIdListFromConditions = null;
    private Condition condition;

    public ConditionParserUtil(Condition condition, EventStream[]  eventStreams) {
        this.condition = condition;
        this.eventStreams = eventStreams;
    }

    static {
        for (int idx = 0; idx < 26; ++idx)
            symbols[idx] = (char) ('a' + idx);
    }

    /**
     * will generate a unique string
     *
     * @param expressionInputs the already available strings
     * @return the unique string
     */
    static public String nextUniqueString(List<String> expressionInputs) {
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = symbols[random.nextInt(symbols.length)];
        String newString = new String(buf);
        if (expressionInputs.contains(newString)) {
            return nextUniqueString(expressionInputs);
        } else {
            return newString;
        }
    }

    /**
     * get the StreamId position in the event array and the Attribute position in an event
     *
     * @param property the StreamId.AttributeName
     * @param state    the state of the state machine
     * @return StreamId position at 0th position and  Attribute position on 1st position in int[]
     * @throws org.wso2.siddhi.core.exception.PropertyFormatException
     *          will be thrown if the property is not
     *          in "StreamId.Attribute" formats
     */
    public int[] getPosition(String property, int state) throws PropertyFormatException {
        if (propertyPatternStream.matcher(property).find()) {
            String[] propertyValues = property.trim().split("\\.");

            if (propertyValues.length == 2) {

                return getPosition(propertyValues[0], null, propertyValues[1], state);
            } else if (propertyValues.length == 3) {
                return getPosition(propertyValues[0], propertyValues[1], propertyValues[2], state);
            } else {
                throw new PropertyFormatException(property + " is an invalid property format");
            }
        } else {
            throw new PropertyFormatException(property + " is not a valid single property format");
        }
    }

    /**
     * get the StreamId position in the event array and the Attribute position in an event
     *
     * @param streamId          the StreamId
     * @param selfEventPosition event position  among same event
     * @param attributeName     the Attribute name
     * @param state             the state of the state machine
     * @return StreamId position at 0th position and  Attribute position on 1st position in int[]
     */

    private int[] getPosition(String streamId, String selfEventPosition, String attributeName, int state) {
        if (isInteger(streamId)) {
            for (EventStream anEventStreamList : eventStreams) {
                if (anEventStreamList.getStreamId().equals(streamId)) {
                    return new int[]{Integer.parseInt(streamId), anEventStreamList.getAttributePositionForName(attributeName)};
                }
            }
            return new int[2];
        } else {
            if (state < 0) {// for simple and join
                for (int i = 0; i < eventStreams.length; i++) {
                    if (eventStreams[i].getStreamId().equals(streamId)) {
                        return new int[]{i,eventStreams[i].getAttributePositionForName(attributeName)};
                    }
                }
                return new int[2];
            } else {       // for pattern
                if (streamId.startsWith("$")) { // for $1.price like conditions
                    int stateId = Integer.parseInt(streamId.replace("$", ""));
                    for (EventStream anEventStreamList : eventStreams) {
                        if (anEventStreamList.getStreamId().equals(streamIdListFromConditions.get(stateId))) {
                            if (selfEventPosition == null) {
                                return new int[]{stateId, anEventStreamList.getAttributePositionForName(attributeName)};
                            } else {
                                if (selfEventPosition.equalsIgnoreCase("first")) {
                                    return new int[]{stateId, anEventStreamList.getAttributePositionForName(attributeName), 0};
                                } else if (selfEventPosition.equalsIgnoreCase("last")){
                                    return new int[]{stateId, anEventStreamList.getAttributePositionForName(attributeName), 1};
                                } else {
                                    return new int[]{stateId, anEventStreamList.getAttributePositionForName(attributeName), 2};
                                }                                                                              
                            }
                        }
                    }
                } else {
                    for (EventStream anEventStreamList : eventStreams) {
                        if (anEventStreamList.getStreamId().equals(streamId)) {
                            return new int[]{state, anEventStreamList.getAttributePositionForName(attributeName)};
                        }
                    }

                }
                return new int[2];
            }
        }
    }

    /**
     * checks whether the checkString is an integer
     *
     * @param checkString the string to be tested
     * @return whether its integer or now
     */
    private static boolean isInteger(String checkString) {
        Pattern intOnly = Pattern.compile("^\\d+$");
        Matcher makeMatch = intOnly.matcher(checkString);
        return makeMatch.find();
    }

    /**
     * to get the stream id from $ streams
     *
     * @param property can be either only StreamId or StreamId.Attribute
     * @return StreamId in its proper name in the format: StreamId or StreamId.Attribute
     */
    public String moderateProperty(String property) {
        String[] propertyValues = property.trim().split("\\.");
        propertyValues[0] = moderatedStreamId(propertyValues[0]);
        if (propertyValues.length == 1 && null == propertyValues[0]) {
            return null;
        } else if (propertyValues.length == 1) {
            return propertyValues[0];
        } else {
            try {
                return propertyValues[0] + "." + propertyValues[1] + "." + propertyValues[2];
            } catch (Exception e) {
                return propertyValues[0] + "." + propertyValues[1];
            }
        }
    }

    /**
     * Check whether the StreamId exist and convert the StreamIds with $ to normal
     *
     * @param property the StreamId or StreamId.Attribute
     * @return the moderated StreamId
     */
    public String moderatedStreamId(String property) {
        String streamId = property.trim().split("\\.")[0];
        if (streamId.startsWith("$")) { // for $1.price like conditions
            if (null == streamIdListFromConditions) {
                if (condition instanceof FollowedByCondition) {
                    streamIdListFromConditions = OutputDefinitionParserUtil.createStreamIdListFromConditions((FollowedByCondition) condition);
                } else {
                    streamIdListFromConditions = OutputDefinitionParserUtil.createStreamIdListFromConditions((SequenceCondition) condition);
                }
            }
            int state = Integer.parseInt(streamId.replace("$", ""));
            for (EventStream anEventStreamList : eventStreams) {
                if (anEventStreamList.getStreamId().equals(streamIdListFromConditions.get(state))) {
                    return anEventStreamList.getStreamId();
                }
            }
        } else {
            for (EventStream anEventStreamList : eventStreams) {
                if (anEventStreamList.getStreamId().equals(streamId)) {
                    return streamId;
                }
            }
        }
        return null;
    }

    /**
     * Check whether the StreamIds exist and convert the StreamIds with $ to normal
     *
     * @param property the List of StreamIds or StreamId.Attributes
     * @return the moderated List of StreamIds
     */
    public List<String> moderatedStreamIdList(List<String> property) {
        List moderatedList = new ArrayList();
        for (String aProperty : property) {
            moderatedList.add(moderatedStreamId(aProperty));
        }
        return moderatedList;
    }


    /**
     * get the class of the property's attribute
     *
     * @param property the property in the format StreamId.AttributeName
     * @return the attribute class
     */
    public Class getPropertyAttributeType(String property) {
        String[] propertyValues = property.trim().split("\\.");
        if (propertyValues.length == 2) {
            return getType(propertyValues[0], propertyValues[1]);
        } else {
            return getType(propertyValues[0], propertyValues[2]);
        }
    }


    /**
     * get the class of the property's attribute
     *
     * @param streamId          the StreamId of the property
     * @param propertyAttribute the AttributeName of the property
     * @return the attribute class
     */
    private Class getType(String streamId, String propertyAttribute) {
        for (EventStream anEventStreamList : eventStreams) {
            if (anEventStreamList.getStreamId().equals(streamId)) {
                return anEventStreamList.getTypeForName(propertyAttribute);
            }
        }
        return null;
    }

    /**
     * get the string or numerical value of the property
     *
     * @param property       the sting version of the value
     * @param comparingClass the class of the value
     * @return the value in the comparingClass
     * @throws org.wso2.siddhi.core.exception.PropertyFormatException
     */
    public static Object getValue(String property, Class comparingClass) throws PropertyFormatException {
        // String
        if (comparingClass == String.class) {
            return property;
        } else {
            // number e.g 23, 23.03 and 45.00
            try {
                return MVEL.eval(property, comparingClass);
            } catch (Exception e) {
                throw new PropertyFormatException(property + " is not a valid value");
            }
        }
    }

    /**
     * will generate the expression properties to attribute position map
     *
     * @param inputPropertyList      the input properties
     * @param expressionPropertyList the unique expression properties according to the inputProperties
     * @param state                  the state of the state machine
     * @return the  expression properties to attribute position map
     * @throws org.wso2.siddhi.core.exception.PropertyFormatException
     */
    public Map<String, int[]> createExpressionPropertyPositionMap(List<String> inputPropertyList, List<String> expressionPropertyList, int state)
            throws PropertyFormatException {
        Map<String, int[]> map = new HashMap<String, int[]>();
        for (int i = 0; i < inputPropertyList.size(); i++) {
            map.put(expressionPropertyList.get(i), getPosition(inputPropertyList.get(i), state));
        }
        return map;
    }

}
