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
package org.wso2.siddhi.api.eventstream.query;

import org.wso2.siddhi.api.OutputDefinition;
import org.wso2.siddhi.api.condition.Condition;
import org.wso2.siddhi.api.eventstream.EventStream;
import org.wso2.siddhi.api.eventstream.query.inputstream.QueryInputStream;
import org.wso2.siddhi.api.eventstream.query.utils.QueryHelper;

public class SimpleQuery extends Query {

    private QueryInputStream queryInputStream = null;

    /**
     * @param streamId         id of the query (output id)
     * @param outputDefinition definition of the output
     * @param queryInputStream input event stream of the query
     * @param condition        condition of the query
     */
    public SimpleQuery(String streamId, OutputDefinition outputDefinition,
                       QueryInputStream queryInputStream, Condition condition) {
        super(streamId, outputDefinition, condition);
        this.queryInputStream = queryInputStream;
        this.inputEventStreams=new EventStream[]{queryInputStream.getEventStream()};
        setSchema(getAttributeNames(), QueryHelper.generateAttributeClasses(outputDefinition.getPropertyList(), this));
    }

    /**
     * get the input event eventstream
     *
     * @return the input event eventstream
     */

    public QueryInputStream getQueryInputStream() {
        return queryInputStream;
    }


}
