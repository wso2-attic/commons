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
import org.wso2.siddhi.api.condition.sequence.SequenceCondition;
import org.wso2.siddhi.api.eventstream.EventStream;
import org.wso2.siddhi.api.eventstream.query.inputstream.QueryInputStream;
import org.wso2.siddhi.api.exception.UnsupportedQueryFormatException;
import org.wso2.siddhi.api.util.OutputDefinitionParserUtil;

import java.util.ArrayList;
import java.util.List;

public class SequenceQuery extends Query {

    private List<QueryInputStream> queryInputStreamList = null;
    List<EventStream> inputEventStreams= new ArrayList<EventStream>();
    /**
     * @param streamId            id of the query
     * @param outputDefinition    definition of the output of the query
     * @param eventStreamList     list of event streams
     * @param sequenceCondition condition of the query
     */
    public SequenceQuery(String streamId, OutputDefinition outputDefinition, List<QueryInputStream> eventStreamList, SequenceCondition sequenceCondition) {
        super(streamId, outputDefinition, sequenceCondition);
        this.queryInputStreamList = eventStreamList;
        for(QueryInputStream queryInputStream:queryInputStreamList ) {
            inputEventStreams.add(queryInputStream.getEventStream());
        }
        setSchema(getAttributeNames(), getAttributeClasses());
    }

    protected Class[] getAttributeClasses() {
        List<String> propertyList = outputDefinition.getPropertyList();
        Class[] classArray = new Class[propertyList.size()];
        if (queryInputStreamList != null) {   // for a pattern query
            for (int i = 0; i < propertyList.size(); i++) { //action=$0.action
                List<String> list = OutputDefinitionParserUtil.createStreamIdListFromConditions((SequenceCondition) getCondition());
                String temp = propertyList.get(i).trim().split("=")[1].split("\\.")[0].substring(1);
                int position = Integer.valueOf(temp);
                String streamName = list.get(position);
                for (QueryInputStream queryInputStream : queryInputStreamList) {
                    if (queryInputStream.getEventStream().getStreamId().equals(streamName)) {

                        try {
                            classArray[i] = queryInputStream.getEventStream().getTypeForName(propertyList.get(i).split("=")[1].split("\\.")[2]);
                        } catch (Exception e) {
                            classArray[i] = queryInputStream.getEventStream().getTypeForName(propertyList.get(i).split("=")[1].split("\\.")[1]);
                        }
                        break;
                        
                    }
                }
            }
            return classArray;
        } else {
            throw new UnsupportedQueryFormatException("Unsupported query type");
        }
    }

    /**
     * get the list of input event streams
     *
     * @return the list of input event streams
     */
    public List<QueryInputStream> getQueryInputStreamList() {
        return queryInputStreamList;
    }

    /**
     * get the list of input event streams
     *
     * @return the list of input event streams
     */
    public List<EventStream> getEventStreamList() {
        return inputEventStreams;
    }
}
