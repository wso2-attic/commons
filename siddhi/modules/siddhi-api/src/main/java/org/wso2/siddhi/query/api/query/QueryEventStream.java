/*
*  Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/
package org.wso2.siddhi.query.api.query;

import org.wso2.siddhi.query.api.definition.StreamDefinition;

public class QueryEventStream {

    private String streamId;
    private String referenceStreamId;
    private StreamDefinition streamDefinition;
    private boolean isCounterStream;

    public QueryEventStream(String streamId, String referenceStreamId,
                            StreamDefinition streamDefinition) {
        this.streamId = streamId;
        this.referenceStreamId = referenceStreamId;
        this.streamDefinition = streamDefinition;
    }

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    public String getReferenceStreamId() {
        return referenceStreamId;
    }

    public void setReferenceStreamId(String referenceStreamId) {
        this.referenceStreamId = referenceStreamId;
    }

    public StreamDefinition getStreamDefinition() {
        return streamDefinition;
    }

    public void setStreamDefinition(StreamDefinition streamDefinition) {
        this.streamDefinition = streamDefinition;
    }

    public boolean isCounterStream() {
        return isCounterStream;
    }

    public void setCounterStream(boolean counterStream) {
        isCounterStream = counterStream;
    }
}
