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
import org.wso2.eventing.exceptions.EventException;

/**
 * A simple EventSink which just emits event messages to the console.
 */
public class ConsoleSink implements EventSink {
    private String name;

    public ConsoleSink(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void onEvent(Event event) throws EventException {
        System.out.println(name + " got event - '" + event.getMessage() + "'");
    }
}
