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

package org.wso2.siddhi.core.node;

import org.wso2.siddhi.api.eventstream.EventStream;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.eventstream.OutputEventStream;

/**
 * The class that takes events for Siddhi to process
 */
public class InputHandler implements EventSource  {

    private int nodeId;
    private EventStream eventStream;
    private OutputEventStream outputOutputEventStream;

    public InputHandler(EventStream eventStream) {
        this.nodeId= SiddhiManager.getNextNodeId();
        this.eventStream=eventStream;
        this.outputOutputEventStream = new OutputEventStream(this);

    }

    public String getStreamId() {
        return eventStream.getStreamId();
    }

    public OutputEventStream getOutputEventStream() {
        return outputOutputEventStream;
    }

    public int getNodeId() {
        return nodeId;
    }

    public EventStream getEventStream() {
        return eventStream;
    }

    /**
     * Sending events to be processed by Siddhi
     * @param event the data to be tested
     * @return returns sent event back
     */
    public Event sendEvent(Event event) {
        try {
            outputOutputEventStream.put(event);      // The event input side. Events put to the tail of the queue.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return event;
    }
}
