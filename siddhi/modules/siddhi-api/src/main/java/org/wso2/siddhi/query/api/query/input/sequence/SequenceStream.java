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
package org.wso2.siddhi.query.api.query.input.sequence;

import org.wso2.siddhi.query.api.expression.constant.Constant;
import org.wso2.siddhi.query.api.query.QueryEventStream;
import org.wso2.siddhi.query.api.query.input.SingleStream;
import org.wso2.siddhi.query.api.query.input.Stream;
import org.wso2.siddhi.query.api.definition.StreamDefinition;
import org.wso2.siddhi.query.api.query.input.sequence.element.RegexElement;
import org.wso2.siddhi.query.api.query.input.sequence.element.NextElement;
import org.wso2.siddhi.query.api.query.input.sequence.element.OrElement;
import org.wso2.siddhi.query.api.query.input.sequence.element.SequenceElement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class SequenceStream implements Stream, SequenceElement {
    private SequenceElement sequenceElement;
    private List<String> streamIdList;
    private Constant within;

    public SequenceStream(SequenceElement sequenceElement, Constant within) {
        this.sequenceElement = sequenceElement;
        this.streamIdList = new ArrayList<String>(collectStreamIds(sequenceElement, new HashSet<String>()));
        this.within=within;
    }

    public SequenceElement getSequenceElement() {
        return sequenceElement;
    }

    @Override
    public List<String> getStreamIds() {
        return streamIdList;
    }

    @Override
    public List<QueryEventStream> constructQueryEventStreamList(
            Map<String, StreamDefinition> streamDefinitionMap,
            List<QueryEventStream> queryEventStreams) {
        return constructEventStreamList(getSequenceElement(), streamDefinitionMap, queryEventStreams);
    }
    private HashSet<String> collectStreamIds(SequenceElement sequenceElement,
                                             HashSet<String> streamIds) {
        if (sequenceElement instanceof SequenceStream) {
            streamIds.addAll(((SequenceStream) sequenceElement).getStreamIds());
        } else if (sequenceElement instanceof SingleStream) {
            streamIds.addAll(((SingleStream) sequenceElement).getStreamIds());
        } else if (sequenceElement instanceof OrElement) {
            collectStreamIds(((OrElement) sequenceElement).getSingleStream1(), streamIds);
            collectStreamIds(((OrElement) sequenceElement).getSingleStream2(), streamIds);
        } else if (sequenceElement instanceof RegexElement) {
            collectStreamIds(((RegexElement) sequenceElement).getSingleStream(), streamIds);
        } else if (sequenceElement instanceof NextElement) {
            collectStreamIds(((NextElement) sequenceElement).getSequenceElement(), streamIds);
            collectStreamIds(((NextElement) sequenceElement).getNextSequenceElement(), streamIds);
        }
        return streamIds;
    }

    public List<QueryEventStream> constructEventStreamList(SequenceElement sequenceElement,
                                                           Map<String, StreamDefinition> streamDefinitionMap,
                                                           List<QueryEventStream> queryEventStreams) {


        if (sequenceElement instanceof SingleStream) {
            ((SingleStream) sequenceElement).constructQueryEventStreamList(streamDefinitionMap, queryEventStreams);
        } else if (sequenceElement instanceof OrElement) {
            constructEventStreamList(((OrElement) sequenceElement).getSingleStream1(), streamDefinitionMap, queryEventStreams);
            constructEventStreamList(((OrElement) sequenceElement).getSingleStream2(), streamDefinitionMap, queryEventStreams);
        } else if (sequenceElement instanceof RegexElement) {
            constructEventStreamList(((RegexElement) sequenceElement).getSingleStream(), streamDefinitionMap, queryEventStreams);
        } else if (sequenceElement instanceof NextElement) {
            constructEventStreamList(((NextElement) sequenceElement).getSequenceElement(), streamDefinitionMap, queryEventStreams);
            constructEventStreamList(((NextElement) sequenceElement).getNextSequenceElement(), streamDefinitionMap, queryEventStreams);
        } else if (sequenceElement instanceof SequenceStream) {
            ((SequenceStream) sequenceElement).constructQueryEventStreamList(streamDefinitionMap, queryEventStreams);
        }

        return queryEventStreams;
    }

    public Constant getWithin() {
        return within;
    }
}
