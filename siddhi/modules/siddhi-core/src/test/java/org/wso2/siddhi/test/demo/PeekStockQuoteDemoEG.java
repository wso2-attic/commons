package org.wso2.siddhi.test.demo;

import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.event.generator.EventGenerator;

import java.util.Random;

public class PeekStockQuoteDemoEG {

    Random randomGenerator; // To generate random numbers
    String[] quoteNames;
    final EventGenerator stockQuoteEventGeneratorType;

    public PeekStockQuoteDemoEG() {
        randomGenerator = new Random();
        quoteNames = new String[]{"IBM", "ORCL", "GOOG"};
//        quoteNames = new String[]{"IBM"};
//        quoteNames = new String[]{"GOOG"};
        stockQuoteEventGeneratorType =
                EventGenerator.DefaultFactory.create("StockExchangeStream",
                        new String[]{"symbol", "time", "price", "volume"},
                        new Class[]{String.class, Long.class, Double.class, Integer.class}
                );
    }

    String getQuoteName() {
        return quoteNames[randomGenerator.nextInt(3)];  // Pick random quote name
//        return quoteNames[randomGenerator.nextInt(1)];  // Pick random quote name
    }

    int getQuoteVolume() {
        return 100 * randomGenerator.nextInt(20) + 10;   // Pick random quote value
    }

        double price = 1.0;
    boolean priceTrend = true;

    double getQuotePrice() {
        if (priceTrend) {
            price++;
        } else {
            price--;
        }
//        return 100 + randomGenerator.nextInt(20);   // Pick random quote value
        if (price == 20) {
            priceTrend = false;
        }
        if (price == 0) {
            priceTrend = true;
        }
        //price += randomGenerator.nextInt(20);
//        System.out.println(100 + price);
        return 100 + price;

    }
//    double[] a = {100, 120, 150, 130, 100};
//    int count = 0;
//
//    double getQuotePrice() {
//        if (count == 5) {
//            count = 0;
//        }
//        return a[count++];
//
//    }

    public Event generateEvent() {
        return stockQuoteEventGeneratorType.createEvent(getQuoteName(), System.currentTimeMillis(), getQuotePrice(), getQuoteVolume());
    }

    public EventGenerator getEventStream() {
        return stockQuoteEventGeneratorType;
    }
}
