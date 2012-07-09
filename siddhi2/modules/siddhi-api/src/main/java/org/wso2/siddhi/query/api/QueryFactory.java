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
package org.wso2.siddhi.query.api;

import org.wso2.siddhi.query.api.condition.Condition;
import org.wso2.siddhi.query.api.definition.StreamDefinition;
import org.wso2.siddhi.query.api.query.Query;
import org.wso2.siddhi.query.api.query.projection.Projector;
import org.wso2.siddhi.query.api.stream.JoinStream;
import org.wso2.siddhi.query.api.stream.SingleStream;
import org.wso2.siddhi.query.api.stream.Stream;
import org.wso2.siddhi.query.api.stream.pattern.PatternStream;
import org.wso2.siddhi.query.api.stream.pattern.element.PatternElement;
import org.wso2.siddhi.query.api.stream.sequence.SequenceStream;
import org.wso2.siddhi.query.api.stream.sequence.element.SequenceElement;

public abstract class QueryFactory {

    public static Query createQuery() {
        return new Query();
    }

    public static StreamDefinition createStreamDefinition() {
        return new StreamDefinition();
    }

    public static SingleStream inputStream(String streamId) {
        return new SingleStream(streamId, streamId);
    }

    public static Projector outputProjector() {
        return new Projector();
    }

    public static Stream joinStream(SingleStream leftStream, JoinStream.Type type,
                                    SingleStream rightStream,
                                    Condition onCompare) {
        return new JoinStream(leftStream, type, rightStream, onCompare, JoinStream.EventTrigger.ALL);
    }

    public static Stream joinStream(SingleStream leftStream, JoinStream.Type type,
                                    SingleStream rightStream) {
        return new JoinStream(leftStream, type, rightStream, null, JoinStream.EventTrigger.ALL);
    }

    public static Stream joinStream(SingleStream leftStream, JoinStream.Type type,
                                    SingleStream rightStream,
                                    Condition onCompare, JoinStream.EventTrigger trigger) {
        return new JoinStream(leftStream, type, rightStream, onCompare, trigger);
    }

    public static SingleStream inputStream(String streamReferenceId, String streamId) {
        return new SingleStream(streamReferenceId, streamId);

    }

    public static PatternStream patternStream(PatternElement patternElement) {
        return new PatternStream(patternElement);
    }

    public static SequenceStream sequenceStream(SequenceElement sequenceElement) {
        return new SequenceStream(sequenceElement);
    }
}
