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
package org.wso2.siddhi.core.stream;

import org.wso2.siddhi.core.event.StreamEvent;
import org.wso2.siddhi.core.query.stream.recevier.StreamReceiver;
import org.wso2.siddhi.core.stream.output.Callback;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class StreamJunction {
    private List<StreamReceiver> streamReceivers = new CopyOnWriteArrayList<StreamReceiver>();
    private List<Callback> callBacks = new CopyOnWriteArrayList<Callback>();
    private String streamId;

    public StreamJunction(String streamId) {
        this.streamId = streamId;
    }

    public void send(StreamEvent currentEvents, StreamEvent expiredEvents, StreamEvent allEvents) {

        for (StreamReceiver receiver : streamReceivers) {
            receiver.receive(allEvents);
        }
        //todo need to remove
        for (Callback callback : callBacks) {
            callback.receive(currentEvents, expiredEvents);
        }
    }

    public synchronized void addEventFlow(StreamReceiver streamReceiver) {
        //in reverse order to execute the later states first to overcome to dependencies of count states
        if (streamReceiver instanceof Callback) {
            callBacks.add((Callback) streamReceiver);
        } else {
            streamReceivers.add(0, streamReceiver);
        }
    }

    public synchronized void removeEventFlow(StreamReceiver streamReceiver) {
        streamReceivers.remove(streamReceiver);
        callBacks.remove(streamReceiver);
    }

    public String getStreamId() {
        return streamId;
    }
}
