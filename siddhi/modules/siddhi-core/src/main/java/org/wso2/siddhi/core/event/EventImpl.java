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

package org.wso2.siddhi.core.event;

import java.util.Arrays;

/**
 * The implementation of the Events
 */
public class EventImpl implements Event {

    private final String eventStreamId;
    private final Object[] values;
    private long timeStamp = -1;
    private boolean isNew = true;

    /**
     * @param eventStreamId The id of the event stream
     * @param values        An event object. This is an object array, in which the first object
     *                      should define the event stream ID.
     */
    public EventImpl(String eventStreamId, Object[] values) {
        this.eventStreamId = eventStreamId;
        this.values = values;
    }

    public EventImpl(String eventStreamId, Object[] values, boolean isNew) {
        this.eventStreamId = eventStreamId;
        this.values = values;
        this.isNew = isNew;
    }

    public EventImpl(String eventStreamId, Object[] values, Long timeStamp) {
        this.eventStreamId = eventStreamId;
        this.values = values;
        this.timeStamp = timeStamp;
    }

    public EventImpl(String eventStreamId, Object[] values, Long timeStamp, boolean isNew) {
        this.eventStreamId = eventStreamId;
        this.values = values;
        this.timeStamp = timeStamp;
        this.isNew = isNew;
    }

    public String getEventStreamId() {
        return eventStreamId;
    }

    public int size() {
        return values.length;
    }

    @SuppressWarnings("unchecked")
    public <T> T getNthAttribute(int i) {
        return (T) values[i];
    }

    public Object[] getValues() {
        return values;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        StringBuilder returnValue = new StringBuilder(eventStreamId);
        returnValue.append(":");

        String comma = ",";
        for (int i = 0, valuesLength = values.length; i < valuesLength; i++) {
            Object aValue = values[i];
            returnValue.append(aValue);
            returnValue.append(comma);
        }
        return returnValue.append(isNew).toString();
//        return returnValue.append(comma).append(timeStamp).toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventImpl)) return false;

        EventImpl event = (EventImpl) o;

        if (!eventStreamId.equals(event.eventStreamId)) return false;
      //  if (!isNew == event.isNew) return false;
        final int size = size();
        for (int i = 0; i < size; i++) {
            final Object thisNthAttribute = getNthAttribute(i);
            final Object otherNthAttribute = event.getNthAttribute(i);
            if ((thisNthAttribute == null && otherNthAttribute != null) ||
                    (thisNthAttribute != null && !thisNthAttribute.equals(otherNthAttribute))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = eventStreamId.hashCode();
        result = 31 * result + Arrays.hashCode(values);
//        if (isNew) {
//            return result + 1;
//        } else {
            return result;
//        }
    }
}
