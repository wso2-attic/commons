package org.wso2.siddhi.test.demo;

import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.event.generator.EventGenerator;

import java.util.Random;

public class MinAvgMaxStockQuoteDemoEG {

    Random randomGenerator; // To generate random numbers
    String[] quoteNames;
    final EventGenerator stockQuoteEventGeneratorType;

    public MinAvgMaxStockQuoteDemoEG() {
        randomGenerator = new Random();
        quoteNames = new String[]{"IBM", "ORCL", "GOOG"};
        stockQuoteEventGeneratorType =
                EventGenerator.DefaultFactory.create("StockExchangeStream",
                        new String[]{"symbol", "time", "price", "volume"},
                        new Class[]{String.class, Long.class, Double.class, Integer.class}
                );
    }

    String getQuoteName() {
        return quoteNames[randomGenerator.nextInt(3)];  // Pick random quote name
    }

    int getQuoteVolume() {
        return 100 * randomGenerator.nextInt(20) + 10;   // Pick random quote value
    }

    double price = 1.0;

    double getQuotePrice() {
//        return 100 + randomGenerator.nextInt(20);   // Pick random quote value
        if (price > 200) {
            price = 1.0;
        }
        price += randomGenerator.nextInt(20);
        return 100 + price;

    }

    public Event generateEvent() {
        return stockQuoteEventGeneratorType.createEvent(getQuoteName(), System.currentTimeMillis(), getQuotePrice(), getQuoteVolume() );
    }

    public EventGenerator getEventStream() {
        return stockQuoteEventGeneratorType;
    }
}
