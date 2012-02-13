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
import org.wso2.siddhi.api.exception.InvalidEventStreamIdException;
import org.wso2.siddhi.api.exception.UnsupportedQueryFormatException;

import java.util.List;

public class JoinQuery extends Query {

    private Join jointStream = null;

    /**
     * @param streamId         id of the query (output id)
     * @param outputDefinition definition of the output
     * @param jointStream
     * @param condition        query condition
     */
    public JoinQuery(String streamId, OutputDefinition outputDefinition, Join jointStream, Condition condition) {
        super(streamId, outputDefinition, condition);
        this.jointStream = jointStream;
        setSchema(getAttributeNames(), getAttributeClasses());
    }

    protected Class[] getAttributeClasses() {
        List<String> propertyList = outputDefinition.getPropertyList();
        Class[] classArray = new Class[propertyList.size()];
        if (jointStream != null) {                    //for Join Query
            EventStream left = jointStream.getQueryLeftInputStream().getEventStream();
            EventStream right = jointStream.getQueryRightInputStream().getEventStream();
            for (int i = 0; i < propertyList.size(); i++) {
                String streamId = propertyList.get(i).split("=")[1].split("\\.")[0];
                String attribute = propertyList.get(i).split("=")[1].split("\\.")[1];

                if (left.getStreamId().equals(streamId)) {
                    classArray[i] = left.getTypeForName(attribute);
                } else if (right.getStreamId().equals(streamId)) {
                    classArray[i] = right.getTypeForName(attribute);
                } else {
                    throw new InvalidEventStreamIdException("Wrong stream ID");
                }

            }
            return classArray;
        } else {
            throw new UnsupportedQueryFormatException("Unsupported query type");
        }

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
