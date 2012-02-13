package org.wso2.siddhi.test.perf;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.wso2.siddhi.api.QueryFactory;
import org.wso2.siddhi.api.condition.where.ConditionOperator;
import org.wso2.siddhi.api.eventstream.InputEventStream;
import org.wso2.siddhi.api.eventstream.query.Query;
import org.wso2.siddhi.core.exception.SiddhiException;
import org.wso2.siddhi.core.node.CallbackHandler;
import org.wso2.siddhi.core.node.InputHandler;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.exception.ProcessorInitializationException;
import org.wso2.siddhi.core.exception.InvalidQueryException;
import org.wso2.siddhi.core.exception.EventStreamNotFoundException;
import org.wso2.siddhi.test.perf.generator.SimpleStockQuoteRandomPerfEG;


public class SimpleFilterTestCaseIntPerf {

    private static int NUMBER_OF_EVENTS = 1000;
    private static final Logger log = Logger.getLogger(SimpleFilterTestCaseIntPerf.class);
    public long initTime = 0;
    long runningTime = 0;
    public static long totalVal = 0;
    public static int iteratorCounter = 1;
    public static double averageVal = 0.0;

    public static void main(String[] args)
            throws InvalidQueryException, ProcessorInitializationException,
                   EventStreamNotFoundException, InterruptedException, SiddhiException {
        new SimpleFilterTestCaseIntPerf().testCase();
    }


    @Before
    public void info() {
        log.debug("------------------------Query being processed...------------------------------------------------");

    }

    @Test
    public void testCase() throws SiddhiException, ProcessorInitializationException,
                                  InvalidQueryException, EventStreamNotFoundException,
                                  InterruptedException {
        //Instantiate SiddhiManager
        SiddhiManager siddhiManager = new SiddhiManager();
        QueryFactory qf = siddhiManager.getQueryFactory();

        InputEventStream cseEventStream = new InputEventStream(
                "CSEStream",
                new String[]{"symbol", "price"},
                new Class[]{String.class, Integer.class}
        );

        siddhiManager.addInputEventStream(cseEventStream);

        Query query = qf.createQuery(
                "StockQuote",
                qf.output("symbol=CSEStream.symbol", "prices=CSEStream.price"),
                qf.from(cseEventStream),
                qf.condition("CSEStream.price", ConditionOperator.GREATERTHAN, "6")
//                  mvel
//                    qf.condition("CSEStream.price > 6")

        );

        siddhiManager.addQuery(query);

        siddhiManager.addCallback(new CallbackHandler("StockQuote") {
            public void callBack(Event event) {
//                    log.info("       Event captured  " + event + " ");
                if ((Integer) event.getNthAttribute(1) == 999) {
                    runningTime = System.currentTimeMillis() - initTime;
                    totalVal = totalVal + runningTime;
                    averageVal = totalVal * 1.0 / iteratorCounter;
//                        calculatedTimes.add(runningTime);
                    System.out.println(averageVal);
                    iteratorCounter++;
//                    finished = true;
                }
            }
        }
        );
        siddhiManager.update();


        SimpleStockQuoteRandomPerfEG eventGenerator = new SimpleStockQuoteRandomPerfEG();
        InputHandler inputHandler = null;
        inputHandler = siddhiManager.getInputHandler("CSEStream");
        //printing the Events

        initTime = System.currentTimeMillis();

        for (int i = 0; i < NUMBER_OF_EVENTS; i++) {
            Event event = eventGenerator.generateEvent();
//            log.debug("Event sending....: " + event);
            inputHandler.sendEvent(event);
        }

//        inputHandler.sendEvent(eventGenerator.generateDefinedEvent("IBM",1000));
//        inputHandler.sendEvent(eventGenerator.generateDefinedEvent("IBM",1000));
//        inputHandler.sendEvent(eventGenerator.generateDefinedEvent("IBM",1000));
        inputHandler.sendEvent(eventGenerator.generateDefinedEvent("IBM", 1000));
        inputHandler.sendEvent(eventGenerator.generateDefinedEvent("IBM", 1000));
        inputHandler.sendEvent(eventGenerator.generateDefinedEvent("IBM", 1000));
        inputHandler.sendEvent(eventGenerator.generateDefinedEvent("IBM", 999));

        Thread.sleep(5000);

        siddhiManager.shutDownTask();
    }


}
