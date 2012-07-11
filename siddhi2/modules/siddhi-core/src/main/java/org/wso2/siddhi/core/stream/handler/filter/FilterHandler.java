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
package org.wso2.siddhi.core.stream.handler.filter;

import org.wso2.siddhi.core.event.AtomicEvent;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.event.ComplexEvent;
import org.wso2.siddhi.core.event.ListEvent;
import org.wso2.siddhi.core.event.StreamEvent;
import org.wso2.siddhi.core.executor.conditon.ConditionExecutor;
import org.wso2.siddhi.core.util.SchedulerQueue;
import org.wso2.siddhi.core.stream.StreamElement;
import org.wso2.siddhi.core.stream.StreamProcessor;
import org.wso2.siddhi.core.stream.handler.StreamHandler;
import org.wso2.siddhi.query.api.query.QueryEventStream;

import java.util.ArrayList;
import java.util.List;

public class FilterHandler implements StreamHandler {
    private ConditionExecutor conditionExecutor;
    private List<QueryEventStream> queryEventStreamList;
    private StreamProcessor nextPreStreamFlowProcessor;
    private StreamElement prevStreamElement;

    public FilterHandler(ConditionExecutor conditionExecutor,
                         List<QueryEventStream> queryEventStreamList) {
        this.conditionExecutor = conditionExecutor;
        this.queryEventStreamList = queryEventStreamList;
    }

    @Override
    public void process(ComplexEvent complexEvent) {
//        //System.out.println("FILTER "+complexEvent);
        if (complexEvent instanceof ListEvent) {
            List<Event> list = new ArrayList<Event>();
            for (Event event : ((ListEvent) complexEvent).getEvents()) {
                if (conditionExecutor.execute(event)) {
                    list.add(event);
                }
            }
            if (list.size() > 0) {
                ((ListEvent) complexEvent).setEvents(list.toArray(new Event[list.size()]));
                nextPreStreamFlowProcessor.process(complexEvent);
            }
        } else if (conditionExecutor.execute((AtomicEvent) complexEvent)) {
            nextPreStreamFlowProcessor.process(complexEvent);
        }
    }

    @Override
    public void setNext(StreamProcessor nextPreStreamFlowProcessor) {
        this.nextPreStreamFlowProcessor = nextPreStreamFlowProcessor;
    }

    @Override
    public SchedulerQueue<StreamEvent> getWindow() {
        return prevStreamElement.getWindow();
    }
}
