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

import org.wso2.siddhi.core.event.AtomicEvent;
import org.wso2.siddhi.core.event.StateEvent;
import org.wso2.siddhi.core.statemachine.pattern.AndPatternState;

public class AndPatternStreamPacker extends PatternStreamPacker {

    int lowerState;
    int higherState;

    public AndPatternStreamPacker(AndPatternState state) {
        super(state);
        if (state.getStateNumber() < state.getPartnerState().getStateNumber()) {
            lowerState = state.getStateNumber();
            higherState = state.getPartnerState().getStateNumber();
        } else {
            higherState = state.getStateNumber();
            lowerState = state.getPartnerState().getStateNumber();
        }
    }

    protected void setEventState(StateEvent eventBundle) {
        if (eventBundle.getEventState() == lowerState) {
            eventBundle.setEventState(higherState);
        } else {
            eventBundle.setEventState(lowerState);
        }
    }

    protected void sendEvent(AtomicEvent atomicEvent) {
        if (((StateEvent) atomicEvent).getEventState() == higherState) {
            queryProjector.process(atomicEvent);
        }
    }

}
