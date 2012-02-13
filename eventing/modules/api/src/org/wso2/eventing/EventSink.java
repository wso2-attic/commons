package org.wso2.eventing;

import org.wso2.eventing.Event;
import org.wso2.eventing.exceptions.EventException;
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

/**
 * EventSink represents a place to push an event.  This typically has two uses - as either
 * a callback on a client (i.e. a listener), or as a publishing point on an event
 * generator (i.e. a server).
 */
public interface EventSink {
    /**
     * Hand an Event to this sink.  The particular implementation of the sink will
     * determine what if anything to do with it.
     * @param event an Event
     * @throws Exception a problem occurred
     */
    void onEvent(Event event) throws EventException;
}
