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

package org.wso2.siddhi.api.condition.sequence;

import org.wso2.siddhi.api.condition.Condition;

import java.util.List;

/**
 *
 * SequenceCondition class is used to specify the conditions of the sequence query
 */

public class SequenceCondition implements Condition {

    private List<Condition> patternConditions;
    private boolean skipTillFirstMatch = true;

    public SequenceCondition dontSkipTillFirstMatch() {
       skipTillFirstMatch = false;
       return this;
    }

    public boolean isSkipTillFirstMatch(){
        return skipTillFirstMatch;
    }

    /**
     * @param conditions List of sequence conditions
     */
    public SequenceCondition(List<Condition> conditions) {
        this.patternConditions = conditions;
    }

    /**
     * get the list of sequence conditions
     *
     * @return list of sequence conditions
     */
    public List<Condition> getPatternConditions() {
        return patternConditions;
    }


}
