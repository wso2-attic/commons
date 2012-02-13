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

import org.apache.log4j.Logger;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.eventstream.InputEventStream;
import org.wso2.siddhi.core.eventstream.handler.CallbackStreamHandler;
import org.wso2.siddhi.core.thread.SiddhiThreadPool;

public abstract class CallbackHandler implements EventSink, ExecutableNode {
    private static final Logger log = Logger.getLogger(CallbackHandler.class);

    private String streamId;
    private InputEventStream inputEventStream;
    private int nodeId;
    protected SiddhiManager siddhiManager;


    public CallbackHandler(String streamId) {
        this.nodeId = SiddhiManager.getNextNodeId();
        this.streamId = streamId;
        this.inputEventStream = new InputEventStream(this);
        this.inputEventStream.assignInputStreamHandler(new CallbackStreamHandler(streamId));
    }


    public InputEventStream getInputEventStream() {
        return inputEventStream;
    }

    @Override
    public String getStreamId() {
        return streamId;
    }

    public int getNodeId() {
        return nodeId;
    }

    @Override
    public void startRunnable(SiddhiThreadPool siddhiThreadPool) {
        this.inputEventStream.startRunnable(siddhiThreadPool);
        siddhiThreadPool.execute(this);
    }

    @Override
    public void stopRunnable() throws InterruptedException {
        inputEventStream.stopRunnable();
    }

    public void resetRunnable() throws InterruptedException {
        inputEventStream.resetRunnable();
    }

    abstract public void callBack(Event event);

    @Override
    public void run() {
        try {
            while (true) {
                Event event = inputEventStream.takeEvent();
                if (SiddhiManager.POISON_PILL.equals(event.getEventStreamId())) {
                    Integer pill = (Integer) event.getNthAttribute(0);
                    if (SiddhiManager.RESET_PROCESSORS == pill) {
                        continue;
                    } else if (SiddhiManager.KILL_ALL == pill || SiddhiManager.KILL == pill) {
                        break;
                    }
                }
                callBack(event);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.debug(this.getClass().getSimpleName() + " ended");
    }


    public void addSiddhiManager(SiddhiManager siddhiManager) {
        this.siddhiManager = siddhiManager;
    }
}
