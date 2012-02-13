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

package eventing.samples.filter;

import org.wso2.eventing.impl.FilteringSink;
import org.wso2.eventing.Event;
import org.wso2.eventing.EventSink;
import eventing.samples.filter.ColoredEvent;

public class ColorFilteringSink extends FilteringSink {
    private String color;

    public ColorFilteringSink(String color, EventSink destination) {
        super(destination);
        this.color = color;
    }

    public boolean match(Event event) {
        try {
            ColoredEvent ce = (ColoredEvent)event;
            return color.equalsIgnoreCase(ce.getColor());
        } catch (ClassCastException ce) {
            return false;
        }
    }
}
