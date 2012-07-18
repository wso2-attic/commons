/*
*  Copyright (c) 2005-2012, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
package org.wso2.siddhi.core.query;

import org.wso2.siddhi.core.config.SiddhiContext;
import org.wso2.siddhi.core.query.projector.QueryProjector;
import org.wso2.siddhi.core.query.stream.recevier.StreamReceiver;
import org.wso2.siddhi.core.stream.StreamJunction;
import org.wso2.siddhi.core.util.parser.StreamParser;
import org.wso2.siddhi.query.api.definition.Attribute;
import org.wso2.siddhi.query.api.definition.StreamDefinition;
import org.wso2.siddhi.query.api.expression.Expression;
import org.wso2.siddhi.query.api.query.Query;
import org.wso2.siddhi.query.api.query.QueryEventStream;
import org.wso2.siddhi.query.api.query.projection.attribute.OutputAttribute;
import org.wso2.siddhi.query.api.query.projection.attribute.SimpleAttribute;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ThreadPoolExecutor;

public class QueryProcessor {
    private String queryId;
    private Query query;
    private QueryProjector queryProjector;
    private List<StreamReceiver> streamReceiverList;

    public QueryProcessor(Query query, ConcurrentMap<String, StreamDefinition> streamDefinitionMap,
                          ConcurrentMap<String, StreamJunction> streamJunctionMap,
                          ThreadPoolExecutor threadPoolExecutor, SiddhiContext siddhiContext) {
        this.queryId = query.getOutputStreamId() + "-" + UUID.randomUUID();
        this.query = query;
        List<QueryEventStream> queryEventStreamList = query.getInputStream().constructQueryEventStreamList(streamDefinitionMap, new ArrayList<QueryEventStream>());
        initQuery(queryEventStreamList);
        queryProjector = new QueryProjector(query.getOutputStreamId(), query.getProjector(), queryEventStreamList, streamJunctionMap, streamDefinitionMap);

        streamReceiverList = StreamParser.parseStream(query.getInputStream(), queryEventStreamList, queryProjector, threadPoolExecutor, siddhiContext);

        for (StreamReceiver streamReceiver : streamReceiverList) {
            streamJunctionMap.get(streamReceiver.getStreamId()).addEventFlow(streamReceiver);
        }
    }


    public String getQueryId() {
        return queryId;
    }

    public Query getQuery() {
        return query;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QueryProcessor)) {
            return false;
        }

        QueryProcessor that = (QueryProcessor) o;

        if (queryId != null ? !queryId.equals(that.queryId) : that.queryId != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return queryId != null ? queryId.hashCode() : 0;
    }

    public void setQueryProjector(QueryProjector queryProjector) {
        this.queryProjector = queryProjector;
    }

    public void setStreamReceiverList(List<StreamReceiver> streamReceiverList) {
        this.streamReceiverList = streamReceiverList;
    }

    private void initQuery(List<QueryEventStream> queryEventStreamList) {

        //   Map<String, StreamDefinition> queryStreamDefinitionMap = getQueryStreamDefinitionMap(query.getInputStream().getStreamIds());
        //populate projection for * case
        List<OutputAttribute> attributeList = query.getProjector().getProjectionList();
        if (attributeList.size() == 0) {
            for (QueryEventStream queryEventStream : queryEventStreamList) {
                for (Attribute attribute : queryEventStream.getStreamDefinition().getAttributeList()) {
                    attributeList.add(new SimpleAttribute(queryEventStream.getReferenceStreamId() + "_" + attribute.getName(), Expression.variable(queryEventStream.getReferenceStreamId(), attribute.getName())));
                }
            }
        }

    }

    public QueryProjector getQueryProjector() {
        return queryProjector;
    }

    public StreamDefinition getOutputStreamDefinition() {
        return queryProjector.getOutputStreamDefinition();
    }

    public List<StreamReceiver> getStreamReceiverList() {
        return streamReceiverList;
    }

    public void removeQuery(ConcurrentMap<String, StreamJunction> streamJunctionMap,
                            ConcurrentMap<String, StreamDefinition> streamDefinitionMap) {
        for (StreamReceiver receiver : streamReceiverList) {
            StreamJunction junction = streamJunctionMap.get(receiver.getStreamId());
            junction.removeEventFlow(receiver);
        }
        streamDefinitionMap.remove(query.getOutputStreamId());
    }
}
