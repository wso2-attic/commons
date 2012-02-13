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

public class CountDataItemInteger implements CountDataItem<Integer> {

    private Integer count = 0;

    @Override
    public Integer add() {
        return ++count;
    }

    @Override
    public Integer add(Object data) {
        if (data != null) {
            return ++count;
        } else {
            return count;
        }
    }

    @Override
    public Integer remove() {
        return --count;
    }

    @Override
    public Integer remove(Object data) {
        if (data != null) {
            return --count;
        } else {
            return count;
        }
    }

    @Override
    public Integer getValue() {
        return count;
    }

    @Override
    public void reset() {
        count = 0;
    }

    @Override
    public DataItem getNewInstance() {
        return new CountDataItemInteger();
    }

}
