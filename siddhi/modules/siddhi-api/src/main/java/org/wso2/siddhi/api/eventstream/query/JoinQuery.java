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
import org.wso2.siddhi.api.eventstream.query.jointstream.Join;
import org.wso2.siddhi.api.eventstream.query.utils.QueryHelper;

public class JoinQuery extends Query {

    private Join jointStream = null;

    /**
     * @param streamId         id of the query (output id)
     * @param outputDefinition definition of the output
     * @param jointStream
     * @param condition        query condition
     */
    public JoinQuery(String streamId, OutputDefinition outputDefinition, Join jointStream,
                     Condition condition) {
        super(streamId, outputDefinition, condition);
        this.jointStream = jointStream;
        this.inputEventStreams=new EventStream[]{jointStream.getQueryLeftInputStream().getEventStream(),
                                               jointStream.getQueryRightInputStream().getEventStream()};
        setSchema(getAttributeNames(), QueryHelper.generateAttributeClasses(outputDefinition.getPropertyList(), this));
    }

    /**
     * get the joint stream
     *
     * @return the joint stream
     */
    public Join getJointStream() {
        return jointStream;
    }


}
