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

package org.wso2.siddhi.core.eventstream.handler.query;

import org.wso2.siddhi.api.eventstream.query.inputstream.QueryInputStream;
import org.wso2.siddhi.api.eventstream.query.inputstream.property.StandardView;
import org.wso2.siddhi.api.eventstream.query.inputstream.property.WindowType;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.eventstream.handler.AbstractInputStreamHandler;
import org.wso2.siddhi.core.eventstream.queue.EventQueue;
import org.wso2.siddhi.core.eventstream.queue.EventQueueImpl;

public abstract class AbstractQueryInputStreamHandler extends AbstractInputStreamHandler {

    protected final QueryInputStream queryInputStream;
    protected StandardView stdViewType = StandardView.NONE;
    protected WindowType windowType = WindowType.NONE;
    protected int stdViewValue = -1;
    protected final EventQueue window = new EventQueueImpl();

    protected AbstractQueryInputStreamHandler(QueryInputStream queryInputStream) {
        super(queryInputStream.getEventStream().getStreamId());
        this.queryInputStream = queryInputStream;
        this.stdViewType = queryInputStream.getStandardView();
        if (stdViewType != StandardView.NONE) {
            this.stdViewValue = queryInputStream.getEventStream().getAttributePositionForName(
                    queryInputStream.getStandardViewValue());
        }
        this.windowType = queryInputStream.getWindowType();
    }


    //    public AbstractQueryInputStreamHandler(EventStream eventStream) {
//        this.eventStream = eventStream;
//
//    }

    public WindowType getWindowType() {
        return windowType;
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

    @Override
    public void put(Event event) throws InterruptedException {
        assignTimeStamp(event);
        switch (stdViewType) {

            case NONE:
                handleNone(event);
                break;

            //Only keeps the newest version of the unique events
            case UNIQUE:
                handleUnique(event);
                break;

            //drop new duplicate events based on the standardViewExpression (stdViewExprList)
            case FIRST_UNIQUE:
                handleFirstUnique(event);
                break;

            default:
                putToWindow(event);
                break;

        }
    }

    protected abstract void assignTimeStamp(Event event);

    /**
     * Puts an event to all the queues in outputEventQueueList
     * Sets the expriryTime for the event. It should be unset (-1) when removing the event as an OldEvent
     *
     * @param event the event to be put
     * @throws InterruptedException occurs if there were any error related to concurrency when inserting to a queue
     */
    protected abstract void putToWindow(Event event) throws InterruptedException;

    protected abstract void handleNone(Event event) throws InterruptedException;

    protected abstract void handleUnique(Event event) throws InterruptedException;

    protected abstract void handleFirstUnique(Event event) throws InterruptedException;

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
        return "AbstractQueryInputStreamHandler{" +
               "streamId='" + streamId + '\'' +
               ", queryInputStream=" + queryInputStream.getEventStream() +
               '}';
    }
}
