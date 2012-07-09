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
package org.wso2.siddhi.core.stream.handler.window;

import org.wso2.siddhi.core.event.StreamEvent;
import org.wso2.siddhi.core.util.SchedulerQueue;
import org.wso2.siddhi.core.stream.StreamElement;
import org.wso2.siddhi.core.stream.StreamProcessor;
import org.wso2.siddhi.core.stream.handler.StreamHandler;
import org.wso2.siddhi.query.api.query.QueryEventStream;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public abstract class WindowHandler implements StreamHandler{
    private List<QueryEventStream> queryEventStreamList;
    private StreamProcessor nextPreStreamFlowProcessor;
    private ScheduledExecutorService eventRemoverScheduler = Executors.newScheduledThreadPool(1);
    private SchedulerQueue<StreamEvent> window = new SchedulerQueue<StreamEvent>();
    private StreamElement prevStreamElement;

    @Override
    public void setNext(StreamProcessor nextPreStreamFlowProcessor) {
        this.nextPreStreamFlowProcessor = nextPreStreamFlowProcessor;
    }


    protected List<QueryEventStream> getQueryEventStreamList() {
        return queryEventStreamList;
    }

//    protected StreamHandler getNextStreamHandler() {
//        return nextStreamHandler;
//    }


    protected StreamProcessor getNextPreStreamFlowProcessor() {
        return nextPreStreamFlowProcessor;
    }

    protected ScheduledExecutorService getEventRemoverScheduler() {
        return eventRemoverScheduler;
    }

    public SchedulerQueue<StreamEvent> getWindow() {
        return window;
    }

    @Override
    public void setPrevious(StreamElement streamElement) {
        this.prevStreamElement = streamElement;
    }

    @Override
    public String getStreamId() {
        return prevStreamElement.getStreamId();
    }

    public abstract void setParameters(Object[] parameters);
}
