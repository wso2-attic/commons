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

import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.eventstream.InputEventStream;
import org.wso2.siddhi.core.eventstream.OutputEventStream;
import org.wso2.siddhi.core.thread.SiddhiThreadPool;

public abstract class AbstractProcessor  implements Processor {

    private int nodeId;
    protected OutputEventStream outputEventStream;
    protected InputEventStream inputEventStream;

    protected AbstractProcessor() {
        this.nodeId = SiddhiManager.getNextNodeId();
        this.outputEventStream = new OutputEventStream(this);
        this.inputEventStream=new InputEventStream(this);
    }

    public InputEventStream getInputEventStream() {
        return inputEventStream;
    }

    public OutputEventStream getOutputEventStream() {
        return outputEventStream;
    }

    public int getNodeId() {
        return nodeId;
    }

    @Override
    public  void startRunnable(SiddhiThreadPool siddhiThreadPool) {
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


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AbstractProcessor that = (AbstractProcessor) o;

        if (nodeId != that.nodeId) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return nodeId;
    }
}
