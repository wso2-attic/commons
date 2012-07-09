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
package org.wso2.siddhi.query.api.stream;

import org.wso2.siddhi.query.api.query.QueryEventStream;
import org.wso2.siddhi.query.api.condition.Condition;
import org.wso2.siddhi.query.api.definition.StreamDefinition;
import org.wso2.siddhi.query.api.stream.handler.Handler;
import org.wso2.siddhi.query.api.stream.pattern.element.PatternElement;
import org.wso2.siddhi.query.api.stream.sequence.element.SequenceElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SingleStream implements Stream, SequenceElement, PatternElement {

    protected String streamId;
    protected StreamDefinition streamDefinition;
    protected String streamReferenceId;
    protected List<Handler> handlerList = new ArrayList<Handler>();
    protected boolean isCounterStream = false;

    protected SingleStream(String streamId) {
        this(streamId, streamId);
    }

    public SingleStream(String streamReferenceId, String streamId) {
        this.streamId = streamId;
        this.streamReferenceId = streamReferenceId;
    }

    public SingleStream handler(Handler.Type type, String name, Object... parameters) {
        handlerList.add(new Handler(name, type, parameters));
        return this;
    }

    public SingleStream addHandler(Handler handler) {
        handlerList.add(handler);
        return this;
    }

    public String getStreamId() {
        return streamId;
    }

    public String getStreamReferenceId() {
        return streamReferenceId;
    }

    public SingleStream setStreamReferenceId(String streamReferenceId) {
        this.streamReferenceId = streamReferenceId;
        return this;
    }

    public List<Handler> getHandlerList() {
        return handlerList;
    }

    public SingleStream handler(Condition filterCondition) {
        handlerList.add(new Handler(null, Handler.Type.FILTER, new Object[]{filterCondition}));
        return this;
    }

    @Override
    public List<String> getStreamIds() {
        List<String> list = new ArrayList<String>();
        list.add(streamId);
        return list;
    }

    public void setCounterStream(boolean counterStream) {
        isCounterStream = counterStream;
    }

    @Override
    public List<QueryEventStream> constructQueryEventStreamList(
            Map<String, StreamDefinition> streamDefinitionMap,
            List<QueryEventStream> queryEventStreams) {
        streamDefinition = streamDefinitionMap.get(streamId);
        QueryEventStream queryEventStream = new QueryEventStream(streamId, streamReferenceId, streamDefinition);
        queryEventStream.setCounterStream(isCounterStream);
        queryEventStreams.add(queryEventStream);
        return queryEventStreams;
    }


}
