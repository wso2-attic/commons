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
package org.wso2.siddhi.core.stream.packer;

import org.wso2.siddhi.core.event.ComplexEvent;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.event.ListEvent;
import org.wso2.siddhi.core.projector.QueryProjector;
import org.wso2.siddhi.core.stream.StreamProcessor;

public class SingleStreamPacker implements StreamPacker, StreamProcessor {
    protected QueryProjector queryProjector;

    @Override
    public void process(ComplexEvent complexEvent) {
        if (complexEvent instanceof Event) {
            queryProjector.process((Event)complexEvent);
        } else if (complexEvent instanceof ListEvent) {
            queryProjector.process((ListEvent)complexEvent);
        }
    }

//    @Override
//    public String getStreamId() {
//        return prevStreamElement.getStreamId();
//    }
//
//    @Override
//    public SchedulerQueue<StreamEvent> getWindow() {
//        return prevStreamElement.getWindow();
//    }

    @Override
    public void setNext(QueryProjector queryProjector) {
        this.queryProjector = queryProjector;
    }
}
