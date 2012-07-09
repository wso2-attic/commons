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
package org.wso2.siddhi.core.event;

import org.wso2.siddhi.query.api.utils.SiddhiConstants;

import java.util.Arrays;

public abstract class ListEvent implements StreamEvent {

    Event[] events;
    private int activeEvents = 0;
    private int currentMaxSize = 0;
    private boolean unlimited = false;

    public ListEvent(int maxEvents) {
        if (maxEvents != SiddhiConstants.UNLIMITED) {
            this.events = new Event[maxEvents];
            currentMaxSize = maxEvents;
        } else {
            this.events = new Event[10];
            currentMaxSize = 10;
            this.unlimited = true;
        }
    }

    protected ListEvent(Event[] events, int activeEvents, boolean unlimited) {
        this.events = events;
        this.activeEvents = activeEvents;
        this.unlimited = unlimited;
    }

    public ListEvent(Event[] events) {
        this.events = events;
        this.activeEvents = events.length;
        this.unlimited = false;
    }

    public Event[] getEvents() {
        return events;
    }

    public Event getEvent(int i) {
        return events[i];
    }

    @Override
    public String toString() {
        return "SingleEventList{" +
               "events=" + (events == null ? null : Arrays.asList(events)) +
               '}';
    }

    @Override
    public long getTimeStamp() {
        return events[activeEvents - 1].getTimeStamp();
    }

    public boolean addEvent(Event event) {
        if (currentMaxSize == activeEvents) {
            if (unlimited) {
                Event[] newEvents = new Event[activeEvents + 10];
                currentMaxSize+=10;
                System.arraycopy(events, 0, newEvents, 0, events.length);
                events = newEvents;
            } else {
                return false;
            }
        }
        this.events[activeEvents] = event;
        activeEvents++;
        return true;
    }

    public int getActiveEvents() {
        return activeEvents;
    }


    public void removeLast() {
        activeEvents--;
        events[activeEvents] = null;
    }

    public StreamEvent cloneEvent() {
        int length = events.length;
        Event[] newEvents = new Event[length];
        System.arraycopy(newEvents, 0, newEvents, 0, activeEvents);
        return createEventClone(newEvents,activeEvents,unlimited);


    }

    protected abstract ListEvent createEventClone(Event[] newEvents, int activeEvents,
                                                  boolean unlimited) ;

    public void setEvents(Event[] events) {
        this.events = events;
        this.activeEvents = events.length;
        this.unlimited = false;
    }
}