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

import java.util.Iterator;

public class LengthWindowQueryInputStreamHandler extends AbstractWindowQueryInputStreamHandler {


    public LengthWindowQueryInputStreamHandler(QueryInputStream queryInputStream) {
        super(queryInputStream);
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

    protected void putToWindow(Event event) throws InterruptedException {
        if (event.getEventStreamId().equals(SiddhiManager.POISON_PILL)) {
            if ((Integer) event.getNthAttribute(0) == SiddhiManager.KILL_ALL ||
                (Integer) event.getNthAttribute(0) == SiddhiManager.KILL) {
                eventQueue.put(event);
                return;
            }
        }
        if (window.size() == toKeepValue) {
            eventQueue.put(window.take());
        }

        Event expiringEvent = new EventImpl(event.getEventStreamId(),
                                            event.getValues(),
                                            event.getTimeStamp(),
                                            false);//1 for new event (in)
        window.put(expiringEvent);

        eventQueue.put(event);
    }

    @Override
    protected void assignTimeStamp(Event event) {
    }
}
