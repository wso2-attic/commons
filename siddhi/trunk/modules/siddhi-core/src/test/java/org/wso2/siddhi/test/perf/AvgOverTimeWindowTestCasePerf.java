package org.wso2.siddhi.test.perf;


import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.wso2.siddhi.api.QueryFactory;
import org.wso2.siddhi.api.condition.where.ConditionOperator;
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
import org.wso2.siddhi.core.exception.EventStreamNotFoundException;
import org.wso2.siddhi.test.perf.generator.SimpleStockQuoteRandomPerfEG;

public class AvgOverTimeWindowTestCasePerf {

    public static int NUMBER_OF_EVENTS = 1000;
    private static final Logger log = Logger.getLogger(AvgOverTimeWindowTestCasePerf.class);
    public static boolean finished = false;
    public static long initTime = 0;

    public static void main(String[] args)
            throws InvalidQueryException, ProcessorInitializationException,
                   EventStreamNotFoundException, SiddhiException {
        // Test code
        AvgOverTimeWindowTestCasePerf avgOverTimeWIndowTestCasePerf = new AvgOverTimeWindowTestCasePerf();
        avgOverTimeWIndowTestCasePerf.testCase();
    }

    @Before
    public void info() {
        ////log.debug("------------------------Query being processed...------------------------------------------------");


    }

    @Test
    public void testCase() throws SiddhiException, EventStreamNotFoundException,
                                  ProcessorInitializationException, InvalidQueryException {
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
                qf.output("symbol=CSEStream.symbol", "prices=avg(CSEStream.price)"),
                qf.from(cseEventStream).setWindow(WindowType.TIME,5),
                qf.condition("CSEStream.symbol", ConditionOperator.EQUAL, "IBM")
        );

        siddhiManager.addQuery(query);

        siddhiManager.addCallback(new CallbackHandler("StockQuote") {
            public void callBack(Event event) {
//                    log.info("       Event captured  "  event  " ");
//                  if(finished){
//                      System.out.println(">>>>>>>>>>>>>Total Elapsed Time in milli seconds<<<<<<<<<<<<<");
                System.out.println(System.currentTimeMillis() - initTime);
//                  }
            }
        }
        );

        siddhiManager.update();

        SimpleStockQuoteRandomPerfEG eventGenerator = new SimpleStockQuoteRandomPerfEG();
        InputHandler inputHandler = siddhiManager.getInputHandler("CSEStream");
        //printing the Events
        initTime = System.currentTimeMillis();

        for (int i = 0; i < NUMBER_OF_EVENTS; i++) {
        Event event = eventGenerator.generateEvent();
        ////log.info("Event received: "  event);
        inputHandler.sendEvent(event);
    }

        inputHandler.sendEvent(eventGenerator.generateDefinedEvent("IBM", 666));
        inputHandler.sendEvent(eventGenerator.generateDefinedEvent("IBM", 666));
        inputHandler.sendEvent(eventGenerator.generateDefinedEvent("IBM", 666));
        inputHandler.sendEvent(eventGenerator.generateDefinedEvent("IBM", 666));
        inputHandler.sendEvent(eventGenerator.generateDefinedEvent("IBM", 666));

        finished = true;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        siddhiManager.shutDownTask();
    }
}
