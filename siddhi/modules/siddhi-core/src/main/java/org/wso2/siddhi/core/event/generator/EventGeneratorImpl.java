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

import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.event.EventImpl;

import java.io.Serializable;

public class EventGeneratorImpl implements EventGenerator, Serializable {

    final Class<?>[] types;
    final String[] names;
    final String streamId;
    final int size;

    public EventGeneratorImpl(String streamId, String[] names, Class<?>[] types) {
        if (streamId == null) {
            throw new NullPointerException(
                    "streamId cannot be null and it should have a unique value");
        }
        this.streamId = streamId;
        if (names == null && types == null) {
            this.types = new Class<?>[0];
            this.names = new String[0];
        } else if (names != null && types != null) {
            if (names.length != types.length) {
                throw new IllegalArgumentException(
                        "Number of names=" + names.length +
                                " are expected to be equal to their respective number of objects=" +
                                types.length + ", here they are not equal!");
            }
            this.types = types;
            this.names = names;
        } else {
            throw new NullPointerException(
                    "names or types cannot be null");
        }
        if (types != null) {
            this.size = types.length;
        } else {
            this.size=0;
        }
    }

    public int size() {
        return size;
    }

    public Class<?> getNthAttributeType(int i) {
        return types[i];
    }

    public String getNthAttributeName(int i) {
        return names[i];
    }

    public Event createEvent(Object... values) {
        validateValues(values);
        return new EventImpl(streamId, values);
    }

    @Override
    public Event createExpiredEvent(Object... values) {
        validateValues(values);
        return new EventImpl(streamId, values,false);
    }

    private void validateValues(Object[] values) {
        if (values == null || values.length != types.length) {
            throw new IllegalArgumentException(
                    "Expected " + types.length + " values, not " +
                            (values == null ? "(null)" : values.length) + " values");
        }

        int i = 0, typesLength = types.length;
        while (i < typesLength) {
            if (values[i] != null &&  types[i].getClass().isInstance(values[i])) {
                throw new IllegalArgumentException(
                        "Expected value #" + i + " ('" +
                                values[i] + "') of new Tuple to be " +
                                 types[i] + ", not " + values[i].getClass());
            }
            i++;
        }
    }



}

