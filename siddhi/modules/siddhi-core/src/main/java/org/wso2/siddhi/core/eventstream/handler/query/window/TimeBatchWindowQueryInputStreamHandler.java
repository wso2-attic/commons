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

package org.wso2.siddhi.core.eventstream.handler.query.window;

import org.wso2.siddhi.api.eventstream.query.inputstream.QueryInputStream;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.event.EventImpl;
import org.wso2.siddhi.core.thread.SiddhiThreadPool;

import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TimeBatchWindowQueryInputStreamHandler extends AbstractTimeWindowQueryInputHandler {

    protected final BlockingQueue<Event> freshWindow = new LinkedBlockingQueue<Event>();

    public TimeBatchWindowQueryInputStreamHandler(QueryInputStream queryInputStream) {
        super(queryInputStream);
    }

    @Override
    public void startRunnable(SiddhiThreadPool siddhiThreadPool) {
        siddhiThreadPool.execute(new TimeWindowEventRemover());
    }


    protected void handleNone(Event event) throws InterruptedException {
        putToWindow(event);
    }

    protected void handleUnique(Event event) throws InterruptedException {
        Iterator<Event> windowEventQueueIterator = freshWindow.iterator();

        while (windowEventQueueIterator.hasNext()) {
            Event windowedEvent = windowEventQueueIterator.next();
            //Only one expression attribute for now. Todo extend to multiple stdViewValues
            //if the new event is NOT unique
            if (windowedEvent.getNthAttribute(stdViewValue).equals(event.getNthAttribute(stdViewValue))) {

                //only remove not sent to user
                windowEventQueueIterator.remove();
                break;
            }
        }
        putToWindow(event);      //either way, the new event should be added to the lists.
    }

    protected void handleFirstUnique(Event event) throws InterruptedException {
        boolean similarEventExist = false;
        for (Event windowedEvent : freshWindow) {
            //Only one expression attribute for now. Todo extends to multiple stdViewValues
            //if the new event is NOT unique
            if (windowedEvent.getNthAttribute(stdViewValue).equals(event.getNthAttribute(stdViewValue))) {
                similarEventExist = true;
                break;
            }
        }
        if (!similarEventExist) {
            putToWindow(event);
        }
    }


    /**
     * Puts an event to all the queues in outputEventQueueList
     * Sets the expriryTime for the event. It should be unset (-1) when removing the event as an OldEvent
     *
     * @param event the event to be put
     * @throws InterruptedException occurs if there were any error related to concurrency when inserting to a queue
     */
    protected void putToWindow(Event event) throws InterruptedException {
        freshWindow.put(event);
    }

//

    /**
     * Takes care of event removals from the eventqueue,
     * and sending the old events in it's own eventstream
     */
    class TimeWindowEventRemover implements Runnable {
        public void run() {
            while (true) {
                try {
                    Thread.sleep(toKeepValue);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    int expiredLength = window.size();
                    for (int i = 0; i < expiredLength; i++) {
                        Event event = window.take();
                        if (event.getEventStreamId().equals(SiddhiManager.POISON_PILL)) {
                            if ((Integer) event.getNthAttribute(0) == SiddhiManager.KILL_ALL ||
                                (Integer) event.getNthAttribute(0) == SiddhiManager.KILL) {
                                eventQueue.put(event);
                                return;
                            } else {//reset all
                                freshWindow.clear();
                                break;
                            }
                        } else {
                            eventQueue.put(event);
                        }
                    }
                    window.clear();
                    for (Event event : freshWindow) {
                        eventQueue.put(event);
                        window.add(new EventImpl(event.getEventStreamId(),
                                                 event.getValues(),
                                                 event.getTimeStamp(),
                                                 false));
                    }
                    freshWindow.clear();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    protected void assignTimeStamp(Event event) {
    }
}
