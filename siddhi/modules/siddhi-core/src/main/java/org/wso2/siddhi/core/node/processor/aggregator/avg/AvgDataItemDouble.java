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

package org.wso2.siddhi.core.node.processor.aggregator.avg;

import org.wso2.siddhi.core.node.processor.aggregator.DataItem;
import org.wso2.siddhi.core.node.processor.aggregator.GeneralDataItem;

public class AvgDataItemDouble implements GeneralDataItem<Double> {
   private Double data = 0.0;
   private int count = 0;

    @Override
    public Double add(Double data) {
        return this.data = ( this.data * count + data) / ++count;
    }

    @Override
    public Double remove(Double data) {
        if (count == 1) {
            --count;
            return this.data = 0.0;
        } else {
            return this.data = (this.data * count - data) / --count;
        }
    }

    @Override
    public  Double getValue() {
        return data;
    }

    @Override
    public void reset() {
      data = 0.0;
      count = 0;
    }

    @Override
    public DataItem getNewInstance() {
        return new AvgDataItemDouble();
    }
}
