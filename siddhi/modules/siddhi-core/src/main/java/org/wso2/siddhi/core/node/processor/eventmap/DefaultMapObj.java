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

package org.wso2.siddhi.core.node.processor.eventmap;

public class DefaultMapObj extends org.wso2.siddhi.core.node.processor.eventmap.MapObj {
    Object value = null;

    public DefaultMapObj(Object value) {
        super(-1, -1);
        this.value = value;
    }

    public Object getValue() {
        return value;
    }
}
