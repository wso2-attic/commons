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

package org.wso2.siddhi.api.condition.pattern;


import org.wso2.siddhi.api.condition.ExpirableCondition;
import org.wso2.siddhi.api.condition.Condition;

public class NonOccurrenceCondition extends ExpirableCondition implements Condition{

    private Condition nonOccurringCondition;
    private Condition followingCondition;
    private FollowedBy followedBy;

    public NonOccurrenceCondition(Condition nonOccurringCondition) {
        this.nonOccurringCondition = nonOccurringCondition;
        followedBy = new FollowedBy(this);
    }


    public Condition getNonOccurringCondition() {
        return nonOccurringCondition;
    }

    public Condition getFollowingCondition() {
        return followingCondition;
    }

    public Condition getOccurringCondition() {
        if(followingCondition instanceof NonOccurrenceCondition){
         return ((NonOccurrenceCondition) followingCondition).getOccurringCondition();
        } else {
        return followingCondition;
        }
    }

    public FollowedBy getFollowedBy() {
        return followedBy;
    }

    public class FollowedBy {
        private NonOccurrenceCondition nonOccurrenceCondition;

        public FollowedBy(NonOccurrenceCondition nonOccurrenceCondition) {
            this.nonOccurrenceCondition = nonOccurrenceCondition;
        }

        public NonOccurrenceCondition followedBy(Condition followedByCondition) {
            followingCondition = followedByCondition;
            return nonOccurrenceCondition;
        }
    }

}
