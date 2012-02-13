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

package org.wso2.siddhi.core.node.processor.executor;


import org.wso2.siddhi.core.event.Event;

import java.util.HashSet;
import java.util.Set;

public class NonOccurrenceExecutor implements Executor {

    private Executor nonOccurringExecutor;
    private Executor followingExecutor;
    private NonOccurrenceExecutor root = this;
    private boolean isConditionHolds = true;

    public NonOccurrenceExecutor() {
    }

    public NonOccurrenceExecutor(Executor nonOccurringExecutor, Executor followingExecutor, NonOccurrenceExecutor root) {
        this.nonOccurringExecutor = nonOccurringExecutor;
        this.followingExecutor = followingExecutor;
        this.root = root;
    }


    public void setNonOccurringExecutor(Executor nonOccurringExecutor) {
        this.nonOccurringExecutor = nonOccurringExecutor;
    }

    public void setFollowingExecutor(Executor followingExecutor) {
        this.followingExecutor = followingExecutor;
    }

    @Override
    public boolean execute(Event[] eventArray) {
        //if (!nonOccurringExecutor.execute(eventArray)) {
        return followingExecutor.execute(eventArray);
//        } else { //if negation happens
//            root.setConditionHolds(false);
//            return true;
//        }
    }


    @Override
    public boolean execute(Event event) {
//        if (!nonOccurringExecutor.execute(event)) {
        return followingExecutor.execute(event);
//        } else { //if negation happens
//            root.setConditionHolds(false);
//            return true;
//        }
    }


    @Override
    public boolean execute(Event[][] eventArray) {
//        if (!nonOccurringExecutor.execute(eventArray)) {
        return followingExecutor.execute(eventArray);
//        } else { //if negation happens
//            root.setConditionHolds(false);
//            return true;
//        }
    }
      //if non occurrence true
    public boolean executeNonOccurrence(Event event) {
//        if (!nonOccurringExecutor.execute(event)) {
        return !nonOccurringExecutor.execute(event) && (!(followingExecutor instanceof NonOccurrenceExecutor)||(((NonOccurrenceExecutor) followingExecutor).executeNonOccurrence(event)));
//        } else { //if negation happens
//            root.setConditionHolds(false);
//            return true;
//        }
    }

    public boolean executeNonOccurrence(Event[] eventArray) {
//        if (!nonOccurringExecutor.execute(event)) {
        if (nonOccurringExecutor.execute(eventArray)) {
            return true;
        } else if (followingExecutor instanceof NonOccurrenceExecutor) {
            return ((NonOccurrenceExecutor) followingExecutor).executeNonOccurrence(eventArray);
        } else {
            return false;
        }
//        } else { //if negation happens
//            root.setConditionHolds(false);
//            return true;
//        }
    }

    public boolean executeNonOccurrence(Event[][] eventArray) {
//        if (!nonOccurringExecutor.execute(event)) {
        if (nonOccurringExecutor.execute(eventArray)) {
            return true;
        } else if (followingExecutor instanceof NonOccurrenceExecutor) {
            return ((NonOccurrenceExecutor) followingExecutor).executeNonOccurrence(eventArray);
        } else {
            return false;
        }
//        } else { //if negation happens
//            root.setConditionHolds(false);
//            return true;
//        }
    }


    @Override
    public Set<String> getCheckingStreamNames() {
        Set<String> set = new HashSet<String>();
        set.addAll(nonOccurringExecutor.getCheckingStreamNames());
        set.addAll(followingExecutor.getCheckingStreamNames());
        return set;
    }

    public boolean isConditionHolds() {
        return isConditionHolds;
    }

    public void setConditionHolds(boolean conditionHolds) {
        isConditionHolds = conditionHolds;
    }

}
