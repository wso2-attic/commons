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
package org.wso2.siddhi.core.statemachine.pattern;

import org.wso2.siddhi.core.query.stream.packer.pattern.PatternStreamPacker;
import org.wso2.siddhi.core.query.stream.recevier.pattern.PatternSingleStreamReceiver;
import org.wso2.siddhi.query.api.stream.SingleStream;

public class PatternState {

    private SingleStream singleStream;
    private int stateNumber;
    private PatternState nextState ;
    private PatternState nextEveryState ;

    private boolean first = false;
    private boolean last = false;

    private PatternSingleStreamReceiver patternSingleStreamReceiver;
    private PatternStreamPacker patternStreamPacker;

    public PatternState(int stateNumber, SingleStream singleStream) {
        this.stateNumber = stateNumber;
        this.singleStream = singleStream;
    }

    public SingleStream getSingleStream() {
        return singleStream;
    }

    public void setSingleStream(SingleStream singleStream) {
        this.singleStream = singleStream;
    }

    public int getStateNumber() {
        return stateNumber;
    }

    public void setStateNumber(int stateNumber) {
        this.stateNumber = stateNumber;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }


    public void setFirst(boolean first) {
        this.first = first;
    }

    public boolean isFirst() {
        return first;
    }

    public PatternState getNextState() {
        return nextState;
    }

    public void setNextState(PatternState nextState) {
        this.nextState = nextState;
    }

    public PatternState getNextEveryState() {
        return nextEveryState;
    }

    public void setNextEveryState(PatternState nextEveryState) {
        this.nextEveryState = nextEveryState;
    }

    public void setPatternSingleStreamReceiver(
            PatternSingleStreamReceiver patternSingleStreamReceiver) {
        this.patternSingleStreamReceiver = patternSingleStreamReceiver;
    }

    public PatternSingleStreamReceiver getPatternSingleStreamReceiver() {
        return patternSingleStreamReceiver;
    }

    public void setPatternStreamPacker(PatternStreamPacker patternStreamPacker) {
        this.patternStreamPacker = patternStreamPacker;
    }

    public PatternStreamPacker getPatternStreamPacker() {
        return patternStreamPacker;
    }
}
