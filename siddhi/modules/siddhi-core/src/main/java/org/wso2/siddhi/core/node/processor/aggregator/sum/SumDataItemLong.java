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

package org.wso2.siddhi.core.node.processor.aggregator.sum;

import org.wso2.siddhi.core.node.processor.aggregator.DataItem;
import org.wso2.siddhi.core.node.processor.aggregator.GeneralDataItem;

public class SumDataItemLong implements GeneralDataItem<Long> {
   private Long total = 0l;

    @Override
    public Long add(Long data) {
        return total = total + data;
    }

    @Override
    public Long remove(Long data) {
        return total = total-data;
    }

    @Override
    public Long getValue() {
        return total;
    }

    @Override
    public void reset() {
      total = 0l;
    }
    @Override
    public DataItem getNewInstance() {
        return new SumDataItemLong();
    }

}
