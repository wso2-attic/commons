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

package org.wso2.siddhi.core.eventstream;

import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.eventstream.handler.InputStreamHandler;
import org.wso2.siddhi.core.exception.InvalidQueryInputStreamException;
import org.wso2.siddhi.core.node.EventSource;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class OutputEventStream {
    List<InputStreamHandler> inputStreamHandlerList = new CopyOnWriteArrayList<InputStreamHandler>();
    private EventSource eventSource;

    public OutputEventStream(EventSource eventSource) {
        this.eventSource = eventSource;
    }

    public void assignStreamHandlers(InputStreamHandler inputStreamHandler) {
        inputStreamHandlerList.add(inputStreamHandler);
    }


    public void put(Event event) throws InterruptedException {
        for (InputStreamHandler inputStreamHandler : inputStreamHandlerList) {
            try {
                inputStreamHandler.put(event);
            } catch (InterruptedException e) {
                throw new InterruptedException("Interrupted at " + eventSource.getStreamId() +
                                               " tail when adding event:" + event + " to " +
                                               inputStreamHandler.getStreamId());
            }
        }
    }

    public void removeStreamHandler(InputStreamHandler inputStreamHandler)
            throws InvalidQueryInputStreamException {
        if( !inputStreamHandlerList.remove(inputStreamHandler)){
            throw new InvalidQueryInputStreamException(inputStreamHandler.toString() + " is not present in " +
                                                    eventSource.getStreamId() + " head ");
        }
//        for (; i < streamHandlerListSize; i++) {
//            if (inputStreamHandlerList.get(i).getStreamId().equals(streamId)) {
//                break;
//            }
//
//        }
//        if (i < streamHandlerListSize) {
//            inputStreamHandlerList.remove(i);
//        } else {
//            throw new InvalidEventStreamIdException(streamId + " is not present in " +
//                                                    eventSource.getStreamId() + " head ");
//
//        }
    }

    public void removeAllStreamHandlers()  {
        this.inputStreamHandlerList.clear();
    }
//
//    public void deAssignAQueue(EventQueue eventQueue);
//
//    /**
//     * For setting Standard  views such as UNIQUE, UNIQUEFIRST etc.
//     *
//     * @param standardViewType
//     * @param standardViewExpressionArray
//     */
//    public void setStandardView(EventStream.StandardView standardViewType,
//                                List<String> standardViewExpressionArray);
}
