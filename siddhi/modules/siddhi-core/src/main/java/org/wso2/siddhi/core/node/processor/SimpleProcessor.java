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

import org.apache.log4j.Logger;
import org.wso2.siddhi.api.eventstream.query.Query;
import org.wso2.siddhi.api.eventstream.query.SimpleQuery;
import org.wso2.siddhi.api.eventstream.query.inputstream.QueryInputStream;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.eventstream.handler.InputStreamHandler;
import org.wso2.siddhi.core.exception.InvalidQueryInputStreamException;
import org.wso2.siddhi.core.exception.ProcessorInitializationException;
import org.wso2.siddhi.core.exception.SiddhiException;
import org.wso2.siddhi.core.node.processor.executor.Executor;
import org.wso2.siddhi.core.node.processor.utils.OutputGenerator;
import org.wso2.siddhi.core.parser.ConditionParser;
import org.wso2.siddhi.core.parser.QueryInputStreamParser;


/**
 * Query processor
 */
public class SimpleProcessor extends AbstractProcessor {

    private static final Logger log = Logger.getLogger(SimpleProcessor.class);

    private SimpleQuery query;
    private Executor executor = null;

    private OutputGenerator outputGenerator;

    public SimpleProcessor(SimpleQuery query)
            throws InvalidQueryInputStreamException, ProcessorInitializationException,
                   SiddhiException {
        this.query = query;
        assignQueryInputStream(query.getQueryInputStream());
        init();
    }

    @Override
    public String getStreamId() {
        return query.getStreamId();

    }

    public Query getQuery() {
        return query;
    }

    @Override
    public void assignQueryInputStream(QueryInputStream queryInputStream)
            throws InvalidQueryInputStreamException {
        InputStreamHandler inputStreamHandler = QueryInputStreamParser.parse(queryInputStream);
        inputEventStream.assignInputStreamHandler(inputStreamHandler);
    }

    /**
     * Initialize the processor.
     */
    public void init() throws ProcessorInitializationException, SiddhiException {
        try {
            outputGenerator = new OutputGenerator(query, getInputEventStream());
            if (query.hasCondition()) {
                ConditionParser conditionParser = new ConditionParser(query.getCondition(), query.getInputEventStreams());
                executor = conditionParser.getExecutor();
            }
        } catch (Exception ex) {
            throw new ProcessorInitializationException("Cannot initialize  Simple Processor query "+query.getStreamId(),ex);
        }

    }


    public void run() {
        while (true) {
            try {
                Event event = inputEventStream.takeEvent();
                String eventStreamId = event.getEventStreamId();
                if (eventStreamId.equals(SiddhiManager.POISON_PILL)) {
                    if ((Integer) event.getNthAttribute(0) == SiddhiManager.KILL_ALL) {
                        outputEventStream.put(event);
                        break;
                    } else if ((Integer) event.getNthAttribute(0) == SiddhiManager.KILL) {
                        break;
                    }
                }

                //If match found after executing the 'conditions' do the following
                if (executor == null || executor.execute(event)) {
                    Event outputEvent = outputGenerator.generateOutput(event);
                    if (outputEvent != null) {
                        outputEventStream.put(outputEvent);

                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        log.debug(this.getClass().getSimpleName() + " ended");

    }


}
