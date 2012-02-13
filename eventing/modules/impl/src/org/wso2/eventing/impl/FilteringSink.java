/*
 * Copyright 2005-2007 WSO2, Inc. (http://wso2.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.eventing.impl;

import org.wso2.eventing.EventSink;
import org.wso2.eventing.Event;
import org.wso2.eventing.EventFilter;
import org.wso2.eventing.exceptions.EventException;

/**
 * A FilteringSink is an abstract class which filters events based on a
 * match() method which subclasses must implement.  Similar to FilteredSink,
 * just an implementation difference to demonstrate both APIs.
 */
public abstract class FilteringSink implements EventSink, EventFilter {
    private EventSink destination;

    protected FilteringSink(EventSink destination) {
        this.destination = destination;
    }

    /**
     * Check to see if this event matches our filter, and if so pass it on!
     * @param event an Event
     * @throws Exception
     */
    public void onEvent(Event event) throws EventException {
        try {
            if (match(event)) {
                destination.onEvent(event);
            }
        } catch (Exception e) {
            // do nothing, event gets dropped
        }
    }
}
