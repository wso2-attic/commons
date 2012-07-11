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
package org.wso2.siddhi.core.stream.recevier;

import org.wso2.siddhi.core.config.SiddhiConfiguration;
import org.wso2.siddhi.core.event.StreamEvent;
import org.wso2.siddhi.core.util.SchedulerQueue;
import org.wso2.siddhi.core.stream.StreamElement;
import org.wso2.siddhi.core.stream.StreamProcessor;
import org.wso2.siddhi.query.api.stream.SingleStream;

import java.util.concurrent.ThreadPoolExecutor;

public class SingleStreamReceiver implements RunnableStreamReceiver,
                                             StreamElement {
    private SingleStream inputStream;
    private ThreadPoolExecutor threadPoolExecutor;
    private SchedulerQueue<StreamEvent> inputQueue = new SchedulerQueue<StreamEvent>();
    private StreamProcessor firstStreamProcessor;
    private SiddhiConfiguration configuration;

    public SingleStreamReceiver(SingleStream inputStream,
                                StreamProcessor firstStreamProcessor,
                                ThreadPoolExecutor threadPoolExecutor) {
        this.inputStream = inputStream;
        this.firstStreamProcessor = firstStreamProcessor;
        this.threadPoolExecutor = threadPoolExecutor;
    }

    @Override
    public void receive(StreamEvent streamEvent) throws InterruptedException {
//        //System.out.println(event);
        if (configuration.isSingleThreading()) {
            firstStreamProcessor.process(streamEvent);
        } else {
            if (!inputQueue.put(streamEvent)) {
                threadPoolExecutor.execute(this);
            }
        }
    }

    @Override
    public void setSiddhiConfiguration(SiddhiConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void run() {
        int eventCounter = 0;
        while (true) {
            StreamEvent streamEvent = inputQueue.poll();
            if (streamEvent == null) {
                break;
            } else if (configuration.getEventBatchSize() > 0 && eventCounter > configuration.getEventBatchSize()) {
                threadPoolExecutor.execute(this);
                break;
            }
            eventCounter++;
            try {
                firstStreamProcessor.process(streamEvent);
            } catch (Throwable e) {
                e.printStackTrace();
            }

        }
    }

    public String getStreamId() {
        return inputStream.getStreamId();
    }

    @Override
    public SchedulerQueue<StreamEvent> getWindow() {
        return null;
    }

}
