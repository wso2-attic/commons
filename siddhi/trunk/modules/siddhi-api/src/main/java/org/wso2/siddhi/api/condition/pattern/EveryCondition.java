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

import org.wso2.siddhi.api.condition.Condition;

import java.util.List;

/**
 * 
 * EveryCondition class handles the "every" clause in seuence queries.
 */
public class EveryCondition extends FollowedByCondition {

    /**
     *  
     * @param followingConditions    List of following condtitions
     */
    public EveryCondition(List<Condition> followingConditions) {
        super(followingConditions);
    }

}
