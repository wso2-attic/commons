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
import org.wso2.siddhi.api.condition.*;
import org.wso2.siddhi.api.eventstream.AbstractEventStream;
import org.wso2.siddhi.api.eventstream.EventStream;
import org.wso2.siddhi.api.exception.InvalidEventStreamIdException;

import java.util.List;

public abstract class Query extends AbstractEventStream {

    protected OutputDefinition outputDefinition;
    protected Condition condition;
    protected Condition havingCondition;
    protected String[] groupBy;
    protected EventStream[] inputEventStreams=new EventStream[1];

    /**
     * @param streamId         id of the query (output id)
     * @param outputDefinition definition of the output
     * @param condition        query condition
     */
    public Query(String streamId, OutputDefinition outputDefinition, Condition condition ) {
        super(streamId);
        this.outputDefinition = outputDefinition;
        this.condition = condition;
    }

    protected String[] getAttributeNames() {
        List<String> propertyList = outputDefinition.getPropertyList();
        String[] names = new String[propertyList.size()];
        for (int i = 0; i < propertyList.size(); i++) {
            names[i] = propertyList.get(i).split("=")[0];
        }
        return names;
    }

    /**
     * get the output definition of the query
     *
     * @return output definition of the query
     */
    public OutputDefinition getOutputDefinition() {
        return outputDefinition;
    }

    /**
     * get the condition from the query
     *
     * @return the condition of the query
     */
    public Condition getCondition() {
        return condition;
    }

    /**
     * Get the having condition
     *
     * @return the having condition
     */
    public Condition getHavingCondition() {
        return havingCondition;
    }

    /**
     * set the having condition
     *
     * @param havingCondition
     */
    public void having(Condition havingCondition) {
        this.havingCondition = havingCondition;
    }

    public void groupBy(String... groupByArray) {
        this.groupBy = groupByArray;
    }

    public String[] getGroupBy() {
        return groupBy;
    }

    public boolean hasGroupBy() {
        if (null != groupBy && null != groupBy[0]) {
            return true;
        }
        return false;
    }

    public boolean hasHaving() {
        if (null != havingCondition) {
            return true;
        }
        return false;
    }

    public boolean hasCondition() {
        if (null != condition) {
            return true;
        }
        return false;
    }



    public EventStream[] getInputEventStreams() {
        return inputEventStreams;
    }

    /**
     * Inefficient method
     * @param streamId
     * @return
     */

    public EventStream getInputEventStream(String streamId) {
        for(EventStream eventStream:inputEventStreams){
            if(eventStream.getStreamId().equals(streamId)){
                return eventStream;
            }
        }
        throw new InvalidEventStreamIdException("There is no stream with "+streamId );
    }

    /**
     * Inefficient method
     * @param streamId
     * @return
     */
    public int getInputEventStreamPosition(String streamId) {
        for (int i = 0, inputEventStreamsLength = inputEventStreams.length; i < inputEventStreamsLength; i++) {
            EventStream eventStream = inputEventStreams[i];
            if (eventStream.getStreamId().equals(streamId)) {
                return i;
            }
        }
        throw new InvalidEventStreamIdException("There is no stream with "+streamId );
    }
}
