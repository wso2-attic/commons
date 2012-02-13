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

package org.wso2.siddhi.api;

import java.util.List;

/**
 * 
 * OutputDefinition is used to specify the output definition of the query
 */
public class OutputDefinition {

    /**
     * Each element in the propertyList has the format A.foo where A is a Stream and foo is a property in the stream
     * Existence of those Streams and properties needs to be validated at org.wso2.siddhi.api.eventstream.query.Query
     */

    private List<String> propertyList;

    /**
     * @param propertyList list of output properties
     */
    public OutputDefinition(List<String> propertyList) {
        this.propertyList = propertyList;
    }

    /**
     * get the output properties list
     *
     * @return the list of output properties
     */
    public List<String> getPropertyList() {
        return propertyList;
    }
}
