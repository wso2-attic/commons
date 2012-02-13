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

package org.wso2.siddhi.core.event.generator;

import org.wso2.siddhi.api.eventstream.EventStream;
import org.wso2.siddhi.core.event.Event;

public interface EventGenerator {

    public int size();

    public Class<?> getNthAttributeType(int i);

    public String getNthAttributeName(int i);

    /**
     * Events are immutable objects.  Events should contain only immutable objects or
     * objects that won't be modified while part of a tuple.
     *
     * @param values
     * @return Event tuple with the given values
     * @throws IllegalArgumentException if the wrong # of arguments or incompatible tuple values are provided
     */
    public Event createEvent(Object... values);

    public Event createExpiredEvent(Object... values);

    public class DefaultFactory {
        public static EventGenerator create(final String streamName, final String[] names, final Class<?>[] types) {
            return new EventGeneratorImpl(streamName, names, types);
        }
        public static EventGenerator create(final EventStream eventStream) {
            return new EventGeneratorImpl(eventStream.getStreamId(), eventStream.getNames(),  eventStream.getTypes());
        }

    }

}
