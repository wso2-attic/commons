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
package org.wso2.siddhi.test.samples.simple.filter._date;

import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.event.generator.EventGenerator;

import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Random;

public class SimpleStockQuoteDateEG {


    Random randomGenerator; // To generate random numbers
    String[] quoteNames;
    final EventGenerator stockQuoteEventGeneratorType;
    String streamName;
    int breakPoint = 0;
    DateFormat formatter;
    Date date;
    int dateInt=11;

    public SimpleStockQuoteDateEG(int breakPoint) {
        formatter = new SimpleDateFormat("dd-MMM-yy");
        randomGenerator = new Random();
        quoteNames = new String[]{"WSO2", "IBM"};
        streamName = "CSEStream";
        stockQuoteEventGeneratorType =
                EventGenerator.DefaultFactory.create(streamName,
                        new String[]{"symbol", "price", "date"},
                        new Class[]{String.class, Long.class, Date.class}
                );
        this.breakPoint = breakPoint;
    }

    String getQuoteName() {
        return quoteNames[randomGenerator.nextInt(2)];  // Pick random quote name
        // return quoteNames[1];  // Pick random quote name
    }

    int count = 0;

    long getQuoteValue() {
        if (count == breakPoint) {
            count = 0;
        }
        count = count + 1000;
        return 1000 + count;   // Pick random quote value
    }


    Date getDate() {
        String str_date = ++dateInt + "-June-07";
        try {
            date = (Date) formatter.parse(str_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public Event generateEvent() {
        return stockQuoteEventGeneratorType.createEvent(getQuoteName(), getQuoteValue(), getDate());
    }

    public EventGenerator getEventStream() {
        return stockQuoteEventGeneratorType;
    }


}
