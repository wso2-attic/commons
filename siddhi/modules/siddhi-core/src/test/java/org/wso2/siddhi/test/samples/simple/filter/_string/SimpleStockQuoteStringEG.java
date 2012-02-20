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

package org.wso2.siddhi.test.samples.simple.filter._string;

import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.event.generator.EventGenerator;

import java.util.Random;

/**
* User: siddhi
* Date: Dec 7, 2010
* Time: 2:13:03 PM
*/
public class SimpleStockQuoteStringEG {

    Random randomGenerator; // To generate random numbers
    String[] quoteNames;
    final EventGenerator stockQuoteEventGeneratorType;
    String streamName;
    int i = 0;

    public SimpleStockQuoteStringEG() {
        randomGenerator = new Random();
        quoteNames = new String[]{"WSO2", "IBM"};
        streamName = "CSEStream";
        stockQuoteEventGeneratorType =
                EventGenerator.DefaultFactory.create(streamName,
                        new String[]{"symbol", "price"},
                        new Class[]{String.class, Integer.class}
                );
    }

    String getQuoteName() {
        if (i == 2) {
            i = 0;
        }
        return quoteNames[i++];
    }

    //int count = 0;

    int getQuoteValue() {
        return 70 + randomGenerator.nextInt(50);   // Pick random quote value
//        count += 20;
//        return 100 + count;   // Pick random quote value
    }

    public Event generateEvent() {
        return stockQuoteEventGeneratorType.createEvent(getQuoteName(), getQuoteValue());
    }

    public EventGenerator getEventStream() {

        return stockQuoteEventGeneratorType;
    }


}
