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
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.event.EventImpl;
import org.wso2.siddhi.core.eventstream.handler.query.AbstractQueryInputStreamHandler;

import java.util.Iterator;

public class SimpleQueryInputStreamHandler extends AbstractQueryInputStreamHandler {


    public SimpleQueryInputStreamHandler(QueryInputStream queryInputStream) {
        super(queryInputStream);
    }

    @Override
    protected void handleNone(Event event) throws InterruptedException {
        eventQueue.put(event);
    }

    @Override
    protected void handleUnique(Event event) throws InterruptedException {
        //this is just ot make it faster... but event with out this the process stops
        if (event.getEventStreamId().equals(SiddhiManager.POISON_PILL)) {
            if ((Integer) event.getNthAttribute(0) == SiddhiManager.KILL_ALL ||
                (Integer) event.getNthAttribute(0) == SiddhiManager.KILL) {
                eventQueue.put(event);
            }
            return;
        }
        Iterator<Event> windowEventQueueIterator = window.iterator();

        while (windowEventQueueIterator.hasNext()) {
            Event windowedEvent = windowEventQueueIterator.next();
            //Only one expression attribute for now. Todo extend to multiple stdViewValues
            //if the new event is NOT unique
            if (windowedEvent.getNthAttribute(stdViewValue).equals(event.getNthAttribute(stdViewValue))) {

                //send the event as an old event
                eventQueue.put(windowedEvent);
                windowEventQueueIterator.remove();
                break;
            }
        }

        putToWindow(event);
    }

    @Override
    protected void handleFirstUnique(Event event) throws InterruptedException {
        if (event.getEventStreamId().equals(SiddhiManager.POISON_PILL)) {
            if ((Integer) event.getNthAttribute(0) == SiddhiManager.KILL_ALL ||
                (Integer) event.getNthAttribute(0) == SiddhiManager.KILL) {
                eventQueue.put(event);
            }
            return;
        }
        boolean similarEventExist = false;

        for (Event windowedEvent : window) {
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

    @Override
    protected void assignTimeStamp(Event event) {
    }

    protected void putToWindow(Event event) throws InterruptedException {
        Event expiringEvent = new EventImpl(event.getEventStreamId(), event.getValues(), false);
        window.put(expiringEvent);
        eventQueue.put(event);
    }

}
