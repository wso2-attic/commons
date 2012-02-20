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

public class AvgDataItemInteger implements GeneralDataItem<Integer> {
    private Float total = 0.0f;
    private int count = 0;

    @Override
    public Integer add(Integer data) {
        return ((Float) ((total += data) / ++count)).intValue();
    }

    @Override
    public Integer remove(Integer data) {
        if (count == 1) {
            --count;
            this.total = 0f;
            return 0;
        } else {
            return ((Float) ((total -= data) / --count)).intValue();
        }

    }

    @Override
    public Integer getValue() {
        return ((Float) (total / count)).intValue();
    }

    @Override
    public void reset() {
        total = 0f;
        count = 0;
    }

    @Override
    public DataItem getNewInstance() {
        return new AvgDataItemInteger();
    }
}
