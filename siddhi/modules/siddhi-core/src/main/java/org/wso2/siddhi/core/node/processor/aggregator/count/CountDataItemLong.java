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

package org.wso2.siddhi.core.node.processor.aggregator.count;

import org.wso2.siddhi.core.node.processor.aggregator.CountDataItem;
import org.wso2.siddhi.core.node.processor.aggregator.DataItem;


public class CountDataItemLong implements CountDataItem<Long> {

    private Long count = 0l;

    @Override
    public Long add() {
        return ++count;
    }

    /**
     * For Count, user can send any DataItem, not just a Long. The DataItem sent does not involve in any calculation
     * but used such that if it's not null, then the count get increased. If the sent dataItem is null then
     * this just returns the current count.
     *
     * @param data
     * @return
     */
    @Override
    public Long add(Object data) {
        if (data != null) {
            return ++count;
        } else {
            return count;
        }
    }

    @Override
    public Long remove() {
        return --count;
    }

    @Override
    public Long remove(Object data) {
        if (data != null) {
            return --count;
        } else {
            return count;
        }
    }

    @Override
    public Long getValue() {
        return count;
    }

    @Override
    public void reset() {
        count = 0l;
    }

    @Override
    public DataItem getNewInstance() {
        return new CountDataItemLong();
    }

}
