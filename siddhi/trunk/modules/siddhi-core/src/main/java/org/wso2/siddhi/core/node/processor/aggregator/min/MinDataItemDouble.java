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

package org.wso2.siddhi.core.node.processor.aggregator.min;

import org.wso2.siddhi.core.node.processor.aggregator.DataItem;
import org.wso2.siddhi.core.node.processor.aggregator.WindowedDataItem;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.eventstream.queue.EventQueue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Class for Double minimum value calculation.
 * This is used by the min() method used in the Query.
 * Separate classes contains for Integer, Long, Double, Float.
 */
public class MinDataItemDouble implements WindowedDataItem<Double> {

    private Double data;
    private List<Double> windowList;
    private int valuePosition;       // position of the attribute of the event
    private EventQueue window;

    public MinDataItemDouble(EventQueue window, int valuePosition) {
        this.window = window;
        this.valuePosition = valuePosition;
    }

    public void setWindow(EventQueue window) {
        this.window = window;
    }

    @Override
    public Double add(Double data) {
        if (null == this.data) {
            return this.data = data;
        } else {
            return this.data = (this.data - data > 0) ? data : this.data;
        }
    }

    private void refreshWindow() {
        windowList = new ArrayList<Double>();
        for (Event aWindow : window) {
            windowList.add(aWindow.<Double>getNthAttribute(valuePosition));
        }
    }


    @Override
    public Double remove(Double data) {
        if (data.equals(this.data)) {
            refreshWindow();
            try {
                this.data = Collections.min(windowList);
            } catch (NoSuchElementException e) {
                reset();
            }
        }
        return getValue();
    }

    @Override
    public Double getValue() {
        if (null == data) {
            return 0.0;
        }
        return data;
    }

    @Override
    public void reset() {
        data = null;
    }

    @Override
    public DataItem getNewInstance() {
        return new MinDataItemDouble(this.window, this.valuePosition);
    }
}
