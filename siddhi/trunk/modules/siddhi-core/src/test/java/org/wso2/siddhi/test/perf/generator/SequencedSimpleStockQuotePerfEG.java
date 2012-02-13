package org.wso2.siddhi.test.perf.generator;

import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.event.generator.EventGenerator;

import java.util.Random;

public class SequencedSimpleStockQuotePerfEG {
    Random randomGenerator; // To generate random numbers
    String[] quoteNames;
    final EventGenerator stockQuoteEventGeneratorType;
    String streamName;

    public SequencedSimpleStockQuotePerfEG() {
        randomGenerator = new Random();
        quoteNames = new String[]{"IBM", "IBM"};
        streamName = "CSEStream";
        stockQuoteEventGeneratorType =
                EventGenerator.DefaultFactory.create(streamName,
                        new String[]{"symbol", "price"},
                        new Class[]{String.class, Integer.class}
                );
    }

    String getQuoteName() {
        return quoteNames[randomGenerator.nextInt(2)];  // Pick random quote name
        // return quoteNames[1];  // Pick random quote name
    }

    //    int count = 0;
//
//    int getQuoteValue() {
//        return randomGenerator.nextInt(10);   // Pick random quote value
////        count += 20;
////        return 100 + count;   // Pick random quote value
//    }
    int counter = 0;

    private int getQuoteValue() {

        if (counter == 10) {
            counter = 0;
        }
        return ++counter;
//          return randomGenerator.nextInt(10);   // Pick random quote value

    }

    public Event generateEvent() {
        return stockQuoteEventGeneratorType.createEvent(getQuoteName(), getQuoteValue());
    }

    public Event generateDefinedEvent(String symbol, int val) {
        return stockQuoteEventGeneratorType.createEvent(symbol, val);
    }

    public EventGenerator getEventStream() {

        return stockQuoteEventGeneratorType;
    }

}
