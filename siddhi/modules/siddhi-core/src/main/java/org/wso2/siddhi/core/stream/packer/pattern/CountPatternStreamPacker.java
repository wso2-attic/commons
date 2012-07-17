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
package org.wso2.siddhi.core.stream.packer.pattern;

import org.wso2.siddhi.core.event.ComplexEvent;
import org.wso2.siddhi.core.event.ListEvent;
import org.wso2.siddhi.core.event.StateEvent;
import org.wso2.siddhi.core.statemachine.pattern.CountPatternState;
import org.wso2.siddhi.core.statemachine.pattern.LogicPatternState;
import org.wso2.siddhi.core.stream.recevier.pattern.CountPatternSingleStreamReceiver;

public class CountPatternStreamPacker extends PatternStreamPacker {

    public CountPatternStreamPacker(CountPatternState state) {
        super(state);
    }

    public void process(ComplexEvent complexEvent) {
        //System.out.println("cp state=" + state.getStateNumber() + " event=" + complexEvent);
        ((CountPatternSingleStreamReceiver) streamReceiver).setPassed(true);
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
            if (activeEvents < ((CountPatternState) state).getMin()) {
                //System.out.println("->" + state.getStateNumber());
                ((CountPatternSingleStreamReceiver) streamReceiver).addOnlyToNextEvents((StateEvent) complexEvent);
            } else if (activeEvents == ((CountPatternState) state).getMin() ) {
                if (nextState != null) {
                    //System.out.println("->" + nextState.getStateNumber());
                    if (nextState instanceof LogicPatternState) {
                        //System.out.println("->" + ((LogicPatternState) nextState).getPartnerState().getStateNumber());
                        ((LogicPatternState) nextState).getPartnerState().getPatternSingleStreamReceiver().addToNextEvents((StateEvent) complexEvent);
                    }
                    nextState.getPatternSingleStreamReceiver().addToNextEvents((StateEvent) complexEvent);
                    ((CountPatternSingleStreamReceiver) streamReceiver).addOnlyToNextEvents((StateEvent) complexEvent);
                }
                if (nextEveryState != null) {
                    //System.out.println("->" + nextEveryState.getStateNumber());
                    StateEvent newStateEvent = ((StateEvent) complexEvent).cloneEvent(nextEveryState.getStateNumber());
                    newStateEvent.setEventState(nextEveryState.getStateNumber() - 1);
                    if (nextEveryState instanceof LogicPatternState) {
                        //System.out.println("->" + ((LogicPatternState) nextEveryState).getPartnerState().getStateNumber());
                        ((LogicPatternState) nextEveryState).getPartnerState().getPatternSingleStreamReceiver().addToNextEvents(newStateEvent);
                    }
                    nextEveryState.getPatternSingleStreamReceiver().addToNextEvents(newStateEvent);
                }
            } else if (activeEvents >= ((CountPatternState) state).getMin() && activeEvents <= ((CountPatternState) state).getMax()) {
                ((CountPatternSingleStreamReceiver) streamReceiver).addOnlyToNextEvents((StateEvent) complexEvent);
                //System.out.println("->" + state.getStateNumber());
//                if (state.getNextStateListSize() > 0) {
//                    for (State nextState : state.getNextStates()) {
//                        StateEvent complexEvent = ((StateEvent) complexEvent).cloneEvent();
//                        if (nextState instanceof LogicState) {
//                            ((LogicState) nextState).getPartnerState().getPatternSingleStreamReceiver().addToNextEvents(complexEvent);
//                        }
//                        nextState.getPatternSingleStreamReceiver().addToNextEvents(complexEvent);
//                    }
//                }

            }
        }
    }
}
