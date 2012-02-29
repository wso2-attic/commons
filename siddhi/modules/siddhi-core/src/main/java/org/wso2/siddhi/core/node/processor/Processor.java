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

package org.wso2.siddhi.core.node.processor;

import org.wso2.siddhi.api.eventstream.query.Query;
import org.wso2.siddhi.api.eventstream.query.inputstream.QueryInputStream;
import org.wso2.siddhi.core.exception.InvalidAttributeCastException;
import org.wso2.siddhi.core.exception.InvalidQueryException;
import org.wso2.siddhi.core.exception.InvalidQueryInputStreamException;
import org.wso2.siddhi.core.exception.ProcessorInitializationException;
import org.wso2.siddhi.core.exception.PropertyFormatException;
import org.wso2.siddhi.core.exception.SiddhiException;
import org.wso2.siddhi.core.exception.UndefinedPropertyException;
import org.wso2.siddhi.core.node.EventSink;
import org.wso2.siddhi.core.node.EventSource;
import org.wso2.siddhi.core.node.ExecutableNode;

public interface Processor extends EventSink, EventSource, ExecutableNode {

    public Query getQuery();

    public void assignQueryInputStream(QueryInputStream queryInputStream)
            throws InvalidQueryInputStreamException;

    public void init()
            throws ProcessorInitializationException, SiddhiException, InvalidQueryException,
                   PropertyFormatException, UndefinedPropertyException,
                   InvalidAttributeCastException;
}

