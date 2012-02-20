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
package org.wso2.siddhi.test.samples.simple.filter._int;

import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.event.generator.EventGenerator;

public class SimpleStockQuoteEG {

    String[] quoteNames;
    final EventGenerator stockQuoteEventGeneratorType;
    String streamName;
    int breakPoint = 0;
    int nameCount = 0;

    public SimpleStockQuoteEG(int breakPoint) {
        quoteNames = new String[]{"WSO2", "IBM"};
        streamName = "CSEStream";
        stockQuoteEventGeneratorType =
                EventGenerator.DefaultFactory.create(streamName,
                        new String[]{"symbol", "price"},
                        new Class[]{String.class, Integer.class}
                );
        this.breakPoint = breakPoint;
    }

    String getQuoteName() {
        if (nameCount == 2) {
            nameCount = 0;
        }
        return quoteNames[nameCount++];
    }

    int count = 10;

    int getQuoteValue() {
        if (count == breakPoint) {
            count = 0;
        }
        return count++;
    }

    public Event generateEvent() {
        return stockQuoteEventGeneratorType.createEvent(getQuoteName(), getQuoteValue());
    }

    public EventGenerator getEventStream() {
        return stockQuoteEventGeneratorType;
    }


}
