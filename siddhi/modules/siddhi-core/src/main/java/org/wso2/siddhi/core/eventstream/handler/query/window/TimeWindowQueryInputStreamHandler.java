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

import org.apache.log4j.Logger;
import org.wso2.siddhi.api.eventstream.query.inputstream.QueryInputStream;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.event.EventImpl;
import org.wso2.siddhi.core.thread.SiddhiThreadPool;

import java.util.Iterator;

public class TimeWindowQueryInputStreamHandler extends AbstractTimeWindowQueryInputHandler {

    private static final Logger log = Logger.getLogger(TimeWindowQueryInputStreamHandler.class);

    public TimeWindowQueryInputStreamHandler(QueryInputStream queryInputStream) {
        super(queryInputStream);
    }

    @Override
    public void startRunnable(SiddhiThreadPool siddhiThreadPool) {
        siddhiThreadPool.execute(new TimeWindowEventRemover());
    }


    @Override
    protected void handleNone(Event event) throws InterruptedException {
        putToWindow(event);
    }

    @Override
    protected void handleUnique(Event event) throws InterruptedException {
        Iterator<Event> windowIterator = window.iterator();

        while (windowIterator.hasNext()) {
            Event windowedEvent = windowIterator.next();
            //Only one expression attribute for now. Todo extend to multiple stdViewValues
            //if the new event is NOT unique
            if (windowedEvent.getNthAttribute(stdViewValue).equals(event.getNthAttribute(stdViewValue))) {
                //send the event as an old event
                eventQueue.put(windowedEvent);
                windowIterator.remove();

                break;
            }
        }

        putToWindow(event);
    }

    @Override
    protected void handleFirstUnique(Event event) throws InterruptedException {

        boolean similarEventExist = false;

        for (Event windowedEvent : window) {
            //Only one expression attribute for now. Todo extends to multiple attrs in stdViewExprList
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

    protected void putToWindow(Event event) throws InterruptedException {
        Event expiringEvent = new EventImpl(event.getEventStreamId(), event.getValues(), event.getTimeStamp(), false);

        window.put(expiringEvent);
        if (event.getEventStreamId().equals(SiddhiManager.POISON_PILL)) {
            return;
        }
        eventQueue.put(event);
    }

    /**
     * Takes care of event removals from the eventqueue,
     * and sending the old events in it's own eventstream
     */
    class TimeWindowEventRemover implements Runnable {
        private long timeDiff = 0;

        public void run() {
            while (true) {
                try {
                    Event event = peekEvent();
                    if (event.getEventStreamId().equals(SiddhiManager.POISON_PILL)) {
                        if ((Integer) event.getNthAttribute(0) == SiddhiManager.KILL_ALL ||
                            (Integer) event.getNthAttribute(0) == SiddhiManager.KILL) {
                            event.setTimeStamp(-1);
                            eventQueue.put(event);
                        }
                        break;
                    }

                    timeDiff = event.getTimeStamp() + toKeepValue - System.currentTimeMillis();
                    //Sleep until the right time come for removing the event
                    //The 'right' time is calculated using the event arriving time and current time
                    if (timeDiff > 0) {
                        try {
                            Thread.sleep(timeDiff);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    event.setTimeStamp(-1);
                    window.take();
                    eventQueue.put(event);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.debug(this.getClass().getSimpleName() + " ended");
        }

        private Event peekEvent() {
            Event event;
            for ( event = window.peek(); event == null; event = window.peek()) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return event;
        }
    }

    @Override
    protected void assignTimeStamp(Event event) {
        event.setTimeStamp(System.currentTimeMillis());
    }
}
