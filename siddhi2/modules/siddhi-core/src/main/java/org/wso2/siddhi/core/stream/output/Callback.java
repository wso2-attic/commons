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
package org.wso2.siddhi.core.stream.output;

import org.wso2.siddhi.core.config.SiddhiConfiguration;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.event.StreamEvent;
import org.wso2.siddhi.core.event.in.InStream;
import org.wso2.siddhi.core.event.remove.RemoveStream;
import org.wso2.siddhi.core.stream.recevier.RunnableStreamReceiver;
import org.wso2.siddhi.core.util.SchedulerQueue;

import java.util.Arrays;
import java.util.concurrent.ThreadPoolExecutor;

public abstract class Callback implements RunnableStreamReceiver {

    private SchedulerQueue<StreamEvent> inputQueue = new SchedulerQueue<StreamEvent>();
    private ThreadPoolExecutor threadPoolExecutor;
    private String streamId;
    private SiddhiConfiguration configuration;

    public void setThreadPoolExecutor(ThreadPoolExecutor threadPoolExecutor) {
        this.threadPoolExecutor = threadPoolExecutor;
    }

    public void setSiddhiConfiguration(SiddhiConfiguration configuration) {
        this.configuration=configuration;
    }

    public String toString(long timeStamp, Object[] newEventData, Object[] removeEventData,
                           Object[] faultEventData) {
        String value = "Events{" +
                       " @timeStamp=" + timeStamp +
                       ", newEventData=";
        if (newEventData == null) {
            value += null;
        } else {
            for (Object data : newEventData) {
                value += Arrays.asList((Object[]) data).toString();
            }
        }
        value += ", removeEventData=";
        if (removeEventData == null) {
            value += null;
        } else {
            for (Object data : removeEventData) {
                value += Arrays.asList((Object[]) data).toString();
            }
        }
        value += ", faultEventData=";
        if (faultEventData == null) {
            value += null;
        } else {
            for (Object data : faultEventData) {
                value += Arrays.asList((Object[]) data).toString();
            }
        }
        value += '}';
        return value;
    }

    public Object getData(Object[] eventData, int eventPosition, int dataPosition) {
        return ((Object[]) eventData[eventPosition])[dataPosition];
    }

    public void receive(StreamEvent event) throws InterruptedException {
        if (configuration.isSingleThreading()) {
            process(event);
        } else {
            if (!inputQueue.put(event)) {
                threadPoolExecutor.execute(this);
            }
        }
    }

    @Override
    public void run() {
        int eventCounter = 0;
        while (true) {
            StreamEvent event = inputQueue.poll();
            if (event == null) {
                break;
            } else if (configuration.getEventBatchSize() > 0 && eventCounter > configuration.getEventBatchSize()) {
                threadPoolExecutor.execute(this);
                break;
            }
            process(event);
        }
    }

    private void process(StreamEvent event) {
        if (event instanceof Event) {
            try {
                if (event instanceof InStream) {
                    receive(event.getTimeStamp(), new Object[]{((Event) event).getData()}, null, null);
                } else if (event instanceof RemoveStream) {
                    receive(event.getTimeStamp(), null, new Object[]{((Event) event).getData()}, null);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public abstract void receive(long timeStamp, Object[] newEventData,
                                 Object[] removeEventData, Object[] faultEventData);

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    public String getStreamId() {
        return streamId;
    }
}
