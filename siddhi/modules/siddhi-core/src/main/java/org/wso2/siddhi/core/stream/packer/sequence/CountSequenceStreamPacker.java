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
package org.wso2.siddhi.core.stream.packer.sequence;

import org.wso2.siddhi.core.event.ComplexEvent;
import org.wso2.siddhi.core.event.ListEvent;
import org.wso2.siddhi.core.event.StateEvent;
import org.wso2.siddhi.core.statemachine.sequence.CountSequenceState;
import org.wso2.siddhi.core.statemachine.sequence.OrSequenceState;
import org.wso2.siddhi.core.stream.recevier.sequence.CountSequenceSingleStreamReceiver;
import org.wso2.siddhi.query.api.utils.SiddhiConstants;

public class CountSequenceStreamPacker extends SequenceStreamPacker {

    public CountSequenceStreamPacker(CountSequenceState state) {
        super(state);
    }

    public void process(ComplexEvent complexEvent) {
        //System.out.println("cp state=" + state.getStateNumber() + " event=" + complexEvent);
        ((CountSequenceSingleStreamReceiver) streamReceiver).setPassed(true);
        if (((StateEvent) complexEvent).getEventState() <= (state.getStateNumber())) {
            setEventState((StateEvent) complexEvent);
            int activeEvents = 0;
            ListEvent listEvent = ((ListEvent) ((StateEvent) complexEvent).getStreamEvent(state.getStateNumber()));
            if (listEvent != null) {
                activeEvents = listEvent.getActiveEvents();
            }
//            //System.out.println(" active "+activeEvents);
//            int activeEvents = ((SingleEventList) ((StateEvent) complexEvent).getStreamEvent(state.getStateNumber())).getActiveEvents();
            if (state.isLast()) {
                sendEvent((StateEvent)complexEvent);
            }
            //passToStreamReceivers
            if (activeEvents < ((CountSequenceState) state).getMin()) {
                //System.out.println("->" + state.getStateNumber());
                ((CountSequenceSingleStreamReceiver) streamReceiver).addOnlyToNextEvents((StateEvent) complexEvent);
            } else if (activeEvents >= ((CountSequenceState) state).getMin() && activeEvents <= ((CountSequenceState) state).getMax()||activeEvents >= ((CountSequenceState) state).getMin() && ((CountSequenceState) state).getMax()== SiddhiConstants.UNLIMITED) {
                if (nextState != null) {
                    //System.out.println("->" + nextState.getStateNumber());
                    if (nextState instanceof OrSequenceState) {
                        //System.out.println("->" + ((OrSequenceState) nextState).getPartnerState().getStateNumber());
                        ((OrSequenceState) nextState).getPartnerState().getSequenceSingleStreamReceiver().addToNextEvents((StateEvent) complexEvent);
                    }
                    nextState.getSequenceSingleStreamReceiver().addToNextEvents((StateEvent) complexEvent);
                    ((CountSequenceSingleStreamReceiver) streamReceiver).addOnlyToNextEvents((StateEvent) complexEvent);
                }
//            } else if (activeEvents >= ((CountSequenceState) state).getMin() && activeEvents <= ((CountSequenceState) state).getMax()||activeEvents >= ((CountSequenceState) state).getMin() && ((CountSequenceState) state).getMax()==RegexElement.UNLIMITED) {
//                //System.out.println("->" + state.getStateNumber());
//                ((CountSequenceSingleStreamReceiver) streamReceiver).addOnlyToNextEvents((StateEvent) complexEvent);
////                if (state.getNextStateListSize() > 0) {
////                    for (State nextState : state.getNextStates()) {
////                        StateEvent complexEvent = ((StateEvent) complexEvent).cloneEvent();
////                        if (nextState instanceof LogicState) {
////                            ((LogicState) nextState).getPartnerState().getPatternSingleStreamReceiver().addToNextEvents(complexEvent);
////                        }
////                        nextState.getPatternSingleStreamReceiver().addToNextEvents(complexEvent);
////                    }
////                }

            }
        }
    }
}
