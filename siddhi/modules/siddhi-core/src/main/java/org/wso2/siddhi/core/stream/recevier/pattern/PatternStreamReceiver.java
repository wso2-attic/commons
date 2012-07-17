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
package org.wso2.siddhi.core.stream.recevier.pattern;

import org.wso2.siddhi.core.config.SiddhiContext;
import org.wso2.siddhi.core.event.StreamEvent;
import org.wso2.siddhi.core.stream.StreamElement;
import org.wso2.siddhi.core.stream.recevier.StreamReceiver;
import org.wso2.siddhi.core.util.SchedulerQueue;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

public class PatternStreamReceiver implements StreamElement, StreamReceiver, Runnable {

    //  private List<SingleStream> inputStreamList;
    private String streamId;
    private ThreadPoolExecutor threadPoolExecutor;
    private SchedulerQueue<StreamEvent> inputQueue = new SchedulerQueue<StreamEvent>();
    private List<PatternSingleStreamReceiver> patternSingleStreamReceiverList;
    private int patternSingleStreamReceiverListSize;
    private SiddhiContext context;

    public PatternStreamReceiver(String streamId,
                                 List<PatternSingleStreamReceiver> patternSingleStreamReceiverList,
                                 ThreadPoolExecutor threadPoolExecutor, SiddhiContext siddhiContext) {
        this.streamId = streamId;
        this.patternSingleStreamReceiverList = patternSingleStreamReceiverList;
        this.threadPoolExecutor = threadPoolExecutor;
        this.patternSingleStreamReceiverListSize = patternSingleStreamReceiverList.size();
        this.context=siddhiContext;
    }

    @Override
    public void receive(StreamEvent streamEvent) throws InterruptedException {
//        //System.out.println(event);
        if (context.isSingleThreading()) {
            precess(streamEvent);
        } else {
            if (inputQueue.put(streamEvent)) {
                threadPoolExecutor.execute(this);
            }
        }
    }

    @Override
    public void run() {
        int eventCounter = 0;
        while (true) {
            StreamEvent streamEvent = inputQueue.poll();
            if (streamEvent == null) {
                break;
            } else if (context.getEventBatchSize() > 0 && eventCounter > context.getEventBatchSize()) {
                threadPoolExecutor.execute(this);
                break;
            }
            eventCounter++;
            precess(streamEvent);

        }
    }

    private void precess(StreamEvent streamEvent) {
        try {
            //in reverse order to execute the later states first to overcome to dependencies of count states
            for (int i = patternSingleStreamReceiverListSize - 1; i >= 0; i--) {
                patternSingleStreamReceiverList.get(i).moveNextEventsToCurrentEvents();
            }
            for (int i = patternSingleStreamReceiverListSize - 1; i >= 0; i--) {
                patternSingleStreamReceiverList.get(i).receive(streamEvent);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public String getStreamId() {
        return streamId;
    }

    @Override
    public SchedulerQueue<StreamEvent> getWindow() {
        return null;
    }


}
