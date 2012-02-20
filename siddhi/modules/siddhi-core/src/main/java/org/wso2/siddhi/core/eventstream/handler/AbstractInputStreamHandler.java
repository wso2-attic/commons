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

package org.wso2.siddhi.core.eventstream.handler;

import org.wso2.siddhi.core.eventstream.queue.EventQueue;

public abstract class AbstractInputStreamHandler implements InputStreamHandler {

    protected String streamId;
   // protected final QueryInputStream queryInputStream;
    protected EventQueue eventQueue;

    protected AbstractInputStreamHandler(String streamId                                         ) {
     //   this.queryInputStream = queryInputStream;
        this.streamId = streamId;
    }


    //    public AbstractQueryInputStreamHandler(EventStream eventStream) {
//        this.eventStream = eventStream;
//
//    }


    @Override
    public void assignEventQueue(EventQueue eventQueue) {
        this.eventQueue = eventQueue;
    }

    public String getStreamId() {
        return streamId;
    }

    //    @Override
//    public void assignStreamCallBack(StreamHandlerCallBack streamCallBack) {
//        this.streamCallBack = streamCallBack;
//    }
//

//
//    @Override
//    public int size() {
//        return windowEventQueue.size();
//    }



    /**
     * Puts an event to all the queues in outputEventQueueList
     * Sets the expriryTime for the event. It should be unset (-1) when removing the event as an OldEvent
     *
     * @param event the event to be put
     * @throws InterruptedException occurs if there were any error related to concurrency when inserting to a queue
     */



//    public void newOutput(Event event) throws InterruptedException {
//        event.setIsNew(true);
//        eventQueue.put(event);
//    }
//
//    public void expiredOutput(Event event) throws InterruptedException {
//        event.setIsNew(false);
//        eventQueue.put(event);
//    }
//
//    public void output(Event event) throws InterruptedException {
//        eventQueue.put(event);
//    }


    @Override
    public String toString() {
        return "AbstractInputStreamHandler{" +
               "streamId='" + streamId + '\'' +
               '}';
    }
}
