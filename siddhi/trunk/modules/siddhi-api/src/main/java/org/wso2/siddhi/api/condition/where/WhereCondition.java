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

package org.wso2.siddhi.api.condition.where;

import org.wso2.siddhi.api.condition.Condition;

import java.util.Map;

/**
 * 
 * The WhereCondition class contains all the common functionality of the Condition.
 * WhereCondition class is to be extended by all the other Conditions.
 */
public interface WhereCondition extends Condition{

    public abstract WhereCondition getNewInstance(Map<String,String> referenceConversion);
}
