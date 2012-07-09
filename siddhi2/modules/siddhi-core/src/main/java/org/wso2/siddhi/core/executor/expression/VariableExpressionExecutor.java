/*
*  Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/
package org.wso2.siddhi.core.executor.expression;

import org.wso2.siddhi.core.event.AtomicEvent;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.event.ListEvent;
import org.wso2.siddhi.core.event.in.StateEvent;
import org.wso2.siddhi.core.event.StreamEvent;
import org.wso2.siddhi.query.api.query.QueryEventStream;
import org.wso2.siddhi.query.api.definition.Attribute;
import org.wso2.siddhi.query.api.definition.StreamDefinition;
import org.wso2.siddhi.query.api.utils.SiddhiConstants;

import java.util.List;

public class VariableExpressionExecutor implements ExpressionExecutor {
    Attribute.Type type;
    int streamPosition = -1;
    int attributePosition = -1;
    int innerStreamPosition = -1;  //Simple Event (Default)


    public VariableExpressionExecutor(String streamIdOfVariable, String attributeName, int position,
                                      List<QueryEventStream> queryEventStreamList,
                                      String currentStreamReference) {
        String streamReference;
        if (streamIdOfVariable != null) {
            streamReference = streamIdOfVariable;
        } else {
            streamReference = currentStreamReference;
        }
        StreamDefinition streamDefinition = null;
        int queryEventStreamListSize = queryEventStreamList.size();
//        if (queryEventStreamListSize == 1) {
//            streamDefinition = queryEventStreamList.get(0).getStreamDefinition();
//        } else {
        for (int i = 0; i < queryEventStreamListSize; i++) {
            QueryEventStream queryEventStream = queryEventStreamList.get(i);
            if (queryEventStream.getReferenceStreamId().equals(streamReference)) {
                streamDefinition = queryEventStream.getStreamDefinition();
                streamPosition = i;
                if (position > -1) { //for known positions
                    innerStreamPosition = position;
                } else if (position == SiddhiConstants.PREV) {
                    innerStreamPosition = SiddhiConstants.PREV;
                } else if (queryEventStream.isCounterStream()) { //to get the last event
                    innerStreamPosition = SiddhiConstants.LAST;
                }
            }

//            }
        }
        if (streamDefinition == null) {
            streamDefinition = queryEventStreamList.get(0).getStreamDefinition();
        }
        type = streamDefinition.getAttributeType(attributeName);
        attributePosition = streamDefinition.getAttributePosition(attributeName);

    }

    @Override
    public Object execute(AtomicEvent event) {
        try {
            if (event instanceof Event) {
                return ((Event) event).getData()[attributePosition];
            } else if (innerStreamPosition == SiddhiConstants.PREV) {
                StreamEvent streamEvent = ((StateEvent) event).getStreamEvent(streamPosition);
                if (streamEvent instanceof ListEvent) {
                    int prevLast = ((ListEvent) streamEvent).getActiveEvents() - 2;
                    if (prevLast > 0) {
                        streamEvent = ((ListEvent) streamEvent).getEvent(prevLast);
                        if (streamEvent != null) {
                            return ((Event) streamEvent).getData(attributePosition);
                        }
                    }
                }
                if (streamPosition - 1 >= 0) {
                    streamEvent = ((StateEvent) event).getStreamEvent(streamPosition - 1);
                    if (streamEvent instanceof ListEvent) {
                        int prevLast = ((ListEvent) streamEvent).getActiveEvents() - 1;
                        if (prevLast > 0) {
                            streamEvent = ((ListEvent) streamEvent).getEvent(prevLast);
                            if (streamEvent != null) {
                                return ((Event) streamEvent).getData(attributePosition);
                            }
                        }
                    } else if (streamEvent instanceof Event) {
                        return ((Event) streamEvent).getData(attributePosition);
                    }
                }
                if (streamPosition - 2 >= 0) {
                    streamEvent = ((StateEvent) event).getStreamEvent(streamPosition - 2);
                    if (streamEvent instanceof ListEvent) {
                        int prevLast = ((ListEvent) streamEvent).getActiveEvents() - 1;
                        if (prevLast > 0) {
                            streamEvent = ((ListEvent) streamEvent).getEvent(prevLast);
                            if (streamEvent != null) {
                                return ((Event) streamEvent).getData(attributePosition);
                            }
                        }
                    } else if (streamEvent instanceof Event) {
                        return ((Event) streamEvent).getData(attributePosition);
                    }
                }
                return null;
//
            } else { //State Event
                StreamEvent streamEvent = ((StateEvent) event).getStreamEvent(streamPosition);
                if (streamEvent == null) {
                    return null;
                } else if (streamEvent instanceof Event) { //Single Event
                    return ((Event) streamEvent).getData(attributePosition);
                } else if (innerStreamPosition == SiddhiConstants.LAST) { //Event List Unknown Size (get the last event)
                    streamEvent = ((ListEvent) streamEvent).getEvent(((ListEvent) streamEvent).getActiveEvents() - 1);
                    if (streamEvent == null) {
                        return null;
                    } else {
                        return ((Event) streamEvent).getData(attributePosition);
                    }
                } else {    //Event List Known Size
                    streamEvent = ((ListEvent) streamEvent).getEvent(innerStreamPosition);
                    if (streamEvent == null) {
                        return null;
                    } else {
                        return ((Event) streamEvent).getData(attributePosition);
                    }
                }
            }
        } catch (NullPointerException e) {
            return null;
        }
    }

    public Attribute.Type getType() {
        return type;
    }


}
