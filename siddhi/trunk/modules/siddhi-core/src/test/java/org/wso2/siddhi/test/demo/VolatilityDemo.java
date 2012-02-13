package org.wso2.siddhi.test.demo;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.wso2.siddhi.api.QueryFactory;
import org.wso2.siddhi.api.eventstream.InputEventStream;
import org.wso2.siddhi.api.eventstream.query.Query;
import org.wso2.siddhi.api.eventstream.query.inputstream.property.WindowType;
import org.wso2.siddhi.core.exception.SiddhiException;
import org.wso2.siddhi.core.node.CallbackHandler;
import org.wso2.siddhi.core.node.InputHandler;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.exception.ProcessorInitializationException;
import org.wso2.siddhi.core.exception.InvalidQueryException;

import static org.wso2.siddhi.api.condition.where.ConditionOperator.*;

public class VolatilityDemo {

    private static int NUMBER_OF_EVENTS = 1000;
    private static final Logger log = Logger.getLogger(VolatilityDemo.class);

    public static void main(String[] args)
            throws InvalidQueryException, ProcessorInitializationException, SiddhiException {
        new VolatilityDemo().testCase();
    }

    @Before
    public void info() {
        log.debug("------------------------Query being processed...------------------------------------------------");

    }

    @Test
    public void testCase() throws SiddhiException, ProcessorInitializationException,
                                  InvalidQueryException {
        //Instantiate SiddhiManager
        SiddhiManager siddhiManager = new SiddhiManager();

        QueryFactory qf = siddhiManager.getQueryFactory();

        int windowTimeGOOGStockChange = 15;
        String volatility1 = "10";
        String volatility2 = "7";
        String volatility3 = "5";
        String volatility4 = "2";

        InputEventStream stockExchangeStream = new InputEventStream("stockExchangeStream",
                                                                                    new String[]{"symbol", "time", "price", "volume"},
                                                                                    new Class[]{String.class, Long.class, Double.class, Integer.class}
        );
        InputHandler inputHandler = siddhiManager.addInputEventStream(stockExchangeStream);

        //create the query

        Query GOOGStockQuote = qf.createQuery(
                "GOOGStockQuote",
                qf.output("*"),
                qf.from(stockExchangeStream),
                qf.condition("stockExchangeStream.symbol", EQUAL, "GOOG")
        );
        siddhiManager.addQuery(GOOGStockQuote);

        Query peakGOOGQuery = qf.createQuery(
                "GOOGStockChange",
                qf.output("symbol=$0.symbol", "priceA=$0.price", "priceB=$1.price", "priceC=$2.first.price", "priced=$2.last.price", "priceE=$3.price"),
                qf.inputStreams(qf.from(GOOGStockQuote)),
                qf.sequence(
                        qf.condition("GOOGStockQuote.symbol", EQUAL, "GOOG"),
                        qf.condition("GOOGStockQuote.price", GREATERTHAN_EQUAL, "$0.price"),
                        qf.star(
                                qf.condition("GOOGStockQuote.price", GREATERTHAN_EQUAL, "$2.prev.price")
                        ),
                        qf.condition("GOOGStockQuote.price", LESSTHAN, "$2.last.price")
                )
        );
        siddhiManager.addQuery(peakGOOGQuery);

        Query troughGOOGQuery = qf.createQuery(
                "GOOGStockChange",
                qf.output("symbol=$0.symbol", "priceA=$0.price", "priceB=$1.price", "priceC=$2.first.price", "priced=$2.last.price", "priceE=$3.price"),
                qf.inputStreams(qf.from(GOOGStockQuote)),
                qf.sequence(
                        qf.condition("GOOGStockQuote.symbol", EQUAL, "GOOG"),
                        qf.condition("GOOGStockQuote.price", LESSTHAN_EQUAL, "$0.price"),
                        qf.star(
                                qf.condition("GOOGStockQuote.price", LESSTHAN_EQUAL, "$2.prev.price")
                        ),
                        qf.condition("GOOGStockQuote.price", GREATERTHAN, "$2.last.price")
                )
        );
        siddhiManager.addQuery(troughGOOGQuery);

        Query countChangeGOOGQuery = qf.createQuery(
                "countChangeGOOG",
                qf.output("count=count(GOOGStockChange.priceA)"),
                qf.from(troughGOOGQuery).setWindow(WindowType.TIME, windowTimeGOOGStockChange),
                qf.condition("GOOGStockChange.symbol", EQUAL, "GOOG")

        );
        siddhiManager.addQuery(countChangeGOOGQuery);

        Query tooVolatileQuery = qf.createQuery(
                "volatilityGOOG",
                qf.output("volatility=(string)tooVolatile"),
                qf.from(countChangeGOOGQuery),
                qf.condition("countChangeGOOG.count", GREATERTHAN, "10")

        );
        siddhiManager.addQuery(tooVolatileQuery);

        Query volatileQuery = qf.createQuery(
                "volatilityGOOG",
                qf.output("volatility=(string)volatile"),
                qf.from(countChangeGOOGQuery),
                qf.and(qf.condition("countChangeGOOG.count", GREATERTHAN, volatility2), qf.condition("countChangeGOOG.count", LESSTHAN_EQUAL, volatility1))

        );
        siddhiManager.addQuery(volatileQuery);

        Query moderateQuery = qf.createQuery(
                "volatilityGOOG",
                qf.output("volatility=(string)moderate"),
                qf.from(countChangeGOOGQuery),
                qf.and(qf.condition("countChangeGOOG.count", GREATERTHAN, volatility3), qf.condition("countChangeGOOG.count", LESSTHAN_EQUAL, volatility2))

        );
        siddhiManager.addQuery(moderateQuery);

        Query steadyQuery = qf.createQuery(
                "volatilityGOOG",
                qf.output("volatility=(string)steady"),
                qf.from(countChangeGOOGQuery),
                qf.and(qf.condition("countChangeGOOG.count", GREATERTHAN, volatility4), qf.condition("countChangeGOOG.count", LESSTHAN_EQUAL, volatility3))


        );
        siddhiManager.addQuery(steadyQuery);

        Query tooSteadyQuery = qf.createQuery(
                "volatilityGOOG",
                qf.output("volatility=(string)tooSteady"),
                qf.from(countChangeGOOGQuery),
                qf.condition("countChangeGOOG.count", LESSTHAN_EQUAL, volatility4)

        );
        siddhiManager.addQuery(tooSteadyQuery);


//            Query troughGOOGQuery = qf.createQuery(
//                    "GOOGStockChange",
//                    qf.output("priceFirst=$0.first.price", "priceChange=$0.last.price", "priceLast=$1.price", "timeLast=$1.time"),
//                    qf.inputStreams(GOOGStockQuote),
//                    qf.sequence(
//                            qf.star(
//                                    qf.condition("GOOGStockQuote.price", LESSTHAN_EQUAL, "$0.prev.price")
//                            ),
//                            qf.condition("GOOGStockQuote.price", GREATERTHAN, "$0.last.price")
//                    )
//            );
//            siddhiManager.addQuery(troughGOOGQuery);


        siddhiManager.addCallback(new CallbackHandler("volatilityGOOG") {
            public void callBack(Event event) {
                System.out.println(" >>> Event captured: " + event);
            }
        }
        );
        siddhiManager.addCallback(new CallbackHandler("countChangeGOOG") {
            public void callBack(Event event) {
                System.out.println(" >>> Event captured: " + event);
            }
        }
        );

        siddhiManager.update();


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
        siddhiManager.shutDownTask();
    }


}
