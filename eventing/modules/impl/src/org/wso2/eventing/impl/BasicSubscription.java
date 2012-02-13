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

import org.wso2.eventing.Subscription;
import org.wso2.eventing.EventSink;
import org.wso2.eventing.Event;
import org.wso2.eventing.exceptions.EventException;

/**
 * Cheezy-simple Subscription implementation.
 */
public class BasicSubscription extends Subscription implements EventSink {
    private EventSink listener;

    public EventSink getListener() {
        return listener;
    }

    public void setListener(EventSink listener) {
        this.listener = listener;
    }

    public void onEvent(Event event) throws EventException {
        listener.onEvent(event);
    }
}
