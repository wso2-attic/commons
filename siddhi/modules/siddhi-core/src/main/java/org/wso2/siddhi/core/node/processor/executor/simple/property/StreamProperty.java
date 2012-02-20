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
package org.wso2.siddhi.core.node.processor.executor.simple.property;

/**
 * this Object contains fields of Stream Property which will have a format of StreamId.AttributeName
 */
public class StreamProperty implements Property {

    /**
     * leftAttributePosition: This is _always_ a size 2 array. zeroth element says the event number,
     * and the first element says the attribute position in that event.
     * ex: new Event[] {event1, event2, event3 }. here, if leftAttributePosition[0]=2 then it refers
     * to event3.
     */
    private int[] attributePositions = null;

    public StreamProperty(int[] attributePositions) {
        this.attributePositions = attributePositions;
    }

    public int[] getAttributePositions() {
        return attributePositions;
    }

    public void setAttributePositions(int[] attributePositions) {
        this.attributePositions = attributePositions;
    }
}
