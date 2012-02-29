package org.wso2.siddhi.test.demo;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.wso2.siddhi.api.QueryFactory;
import org.wso2.siddhi.api.eventstream.InputEventStream;
import org.wso2.siddhi.api.eventstream.query.Query;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.exception.SiddhiException;
import org.wso2.siddhi.core.node.CallbackHandler;
import org.wso2.siddhi.core.node.InputHandler;

import static org.wso2.siddhi.api.condition.where.ConditionOperator.EQUAL;
import static org.wso2.siddhi.api.condition.where.ConditionOperator.GREATERTHAN_EQUAL;
import static org.wso2.siddhi.api.condition.where.ConditionOperator.LESSTHAN;

public class PeekDemo {

    private static int NUMBER_OF_EVENTS = 1000;
    private static final Logger log = Logger.getLogger(PeekDemo.class);

    public static void main(String[] args) throws SiddhiException, InterruptedException {
        new PeekDemo().testCase();
    }

    @Before
    public void info() {
        log.debug("------------------------Query being processed...------------------------------------------------");

    }

    @Test
    public void testCase() throws SiddhiException, InterruptedException {
        //Instantiate SiddhiManager
        SiddhiManager siddhiManager = new SiddhiManager();

        QueryFactory qf = SiddhiManager.getQueryFactory();

        InputEventStream stockExchangeStream = new InputEventStream("StockExchangeStream",
                                                                    new String[]{"symbol", "time", "price", "volume"},
                                                                    new Class[]{String.class, Long.class, Double.class, Integer.class}
        );
        InputHandler inputHandler = siddhiManager.addInputEventStream(stockExchangeStream);

        //create the query
        Query IBMStockQuote = qf.createQuery(
                "IBMStockQuote",
                qf.output("*"),
                qf.from(stockExchangeStream),
                qf.condition("StockExchangeStream.symbol", EQUAL, "IBM")
        );
        siddhiManager.addQuery(IBMStockQuote);


        Query peekQuery = qf.createQuery(
                "StockQuotePeek",
                qf.output("priceA=$0.price", "priceB=$1.price", "priceC1=$2.first.price", "priceC2=$2.last.price", "priceD=$3.price"),
                qf.inputStreams(qf.from(IBMStockQuote)),
                qf.sequence(
                        qf.condition("IBMStockQuote.symbol", EQUAL, "IBM"),
                        qf.condition("IBMStockQuote.price", GREATERTHAN_EQUAL, "$0.price"),
                        qf.star(
                                qf.condition("IBMStockQuote.price", GREATERTHAN_EQUAL, "$2.prev.price")
                        ),
                        qf.condition("IBMStockQuote.price", LESSTHAN, "$2.last.price")
                )
        );
        siddhiManager.addQuery(peekQuery);

        siddhiManager.addCallback(new CallbackHandler("StockQuotePeek") {
            public void callBack(Event event) {
                System.out.println(" >>> Event captured: " + event);
            }
        }
        );

//        siddhiManager.addCallback(new CallbackHandler("IBMStockQuote") {
//            public void callBack(Event event) {
//                System.out.println(" >>> IBM Event captured: " + event);
//            }
//        }
//        );

        siddhiManager.init();


        PeekStockQuoteDemoEG eventGenerator = new PeekStockQuoteDemoEG();
        //printing the Events
        for (int i = 0; i < NUMBER_OF_EVENTS; i++) {
            Event event = eventGenerator.generateEvent();
          // System.out.println("Event send: " + event);
            inputHandler.sendEvent(event);
//                if (i == 200) {
//                    try {
//                        Thread.sleep(5000);
//                        System.out.println("*******************************");
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
        }
        Thread.sleep(20000);
     //   siddhiManager.shutDownTask();

    }


}
