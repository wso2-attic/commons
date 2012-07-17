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
package org.wso2.siddhi.query.api.stream.pattern;

import org.wso2.siddhi.query.api.query.QueryEventStream;
import org.wso2.siddhi.query.api.stream.SingleStream;
import org.wso2.siddhi.query.api.stream.Stream;
import org.wso2.siddhi.query.api.definition.StreamDefinition;
import org.wso2.siddhi.query.api.stream.pattern.element.CountElement;
import org.wso2.siddhi.query.api.stream.pattern.element.FollowedByElement;
import org.wso2.siddhi.query.api.stream.pattern.element.LogicalElement;
import org.wso2.siddhi.query.api.stream.pattern.element.PatternElement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class PatternStream implements Stream, PatternElement {

    private PatternElement patternElement;
    private List<String> streamIdList;

    public PatternStream(PatternElement patternElement) {
        this.patternElement = patternElement;
        this.streamIdList = new ArrayList<String>(collectStreamIds(patternElement, new HashSet<String>()));
    }

    @Override
    public List<String> getStreamIds() {
        return streamIdList;
    }

    @Override
    public List<QueryEventStream> constructQueryEventStreamList(
            Map<String, StreamDefinition> streamDefinitionMap,
            List<QueryEventStream> queryEventStreams) {
        return constructEventStreamList(patternElement, streamDefinitionMap, queryEventStreams);
    }

    public PatternElement getPatternElement() {
        return patternElement;
    }

    private HashSet<String> collectStreamIds(PatternElement patternElement,
                                             HashSet<String> streamIds) {
        if (patternElement instanceof PatternStream) {
            streamIds.addAll(((PatternStream) patternElement).getStreamIds());
        } else if (patternElement instanceof SingleStream) {
            streamIds.addAll(((SingleStream) patternElement).getStreamIds());
        } else if (patternElement instanceof LogicalElement) {
            collectStreamIds(((LogicalElement) patternElement).getSingleStream1(), streamIds);
            collectStreamIds(((LogicalElement) patternElement).getSingleStream2(), streamIds);
        } else if (patternElement instanceof CountElement) {
            collectStreamIds(((CountElement) patternElement).getSingleStream(), streamIds);
        } else if (patternElement instanceof FollowedByElement) {
            collectStreamIds(((FollowedByElement) patternElement).getPatternElement(), streamIds);
            collectStreamIds(((FollowedByElement) patternElement).getFollowedByPatternElement(), streamIds);
        }
        return streamIds;
    }

    public List<QueryEventStream> constructEventStreamList(PatternElement patternElement,
                                                           Map<String, StreamDefinition> streamDefinitionMap,
                                                           List<QueryEventStream> queryEventStreams) {


        if (patternElement instanceof SingleStream) {
            ((SingleStream) patternElement).constructQueryEventStreamList(streamDefinitionMap, queryEventStreams);
        } else if (patternElement instanceof LogicalElement) {
            constructEventStreamList(((LogicalElement) patternElement).getSingleStream1(), streamDefinitionMap, queryEventStreams);
            constructEventStreamList(((LogicalElement) patternElement).getSingleStream2(), streamDefinitionMap, queryEventStreams);
        } else if (patternElement instanceof CountElement) {
            constructEventStreamList(((CountElement) patternElement).getSingleStream(), streamDefinitionMap, queryEventStreams);
        } else if (patternElement instanceof FollowedByElement) {
            constructEventStreamList(((FollowedByElement) patternElement).getPatternElement(), streamDefinitionMap, queryEventStreams);
            constructEventStreamList(((FollowedByElement) patternElement).getFollowedByPatternElement(), streamDefinitionMap, queryEventStreams);
        } else if (patternElement instanceof PatternStream) {
            ((PatternStream) patternElement).constructQueryEventStreamList(streamDefinitionMap, queryEventStreams);
        }

        return queryEventStreams;
    }
}
