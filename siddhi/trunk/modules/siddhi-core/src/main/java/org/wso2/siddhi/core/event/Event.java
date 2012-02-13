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

import java.io.Serializable;

public interface Event extends Serializable {

    /**
     * The stream id of the event
     *
     * @return streamId the event stream id
     */
    public String getEventStreamId();

    /**
     * @param i the ith attribute that the user need to use get
     * @return the Generic object that was requested
     */
    public <T> T getNthAttribute(int i);

    /**
     * @return The attributes/values of this event as an array
     */
    public Object[] getValues();

    /**
     * the number of attributes this event has
     *
     * @return size of the one event
     */
    public int size();

    /**
     * When a new event is put in to the queue for processing it's isNew flag is set to true.
     * When the event got expired it is set to false (when throwing out from the sliding window)
     * Particularly related to WindowEventQueueTail like TimeWindowEventQueueTailImpl
     *
     * @param isNew whether the event is new or not
     */
    public void setIsNew(boolean isNew);

    /**
     * @return Whether the event is new or not
     */
    public boolean isNew();

    public long getTimeStamp();

    public void setTimeStamp(long timeStamp);


}
