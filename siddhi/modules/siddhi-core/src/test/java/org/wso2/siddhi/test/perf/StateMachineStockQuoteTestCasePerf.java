package org.wso2.siddhi.test.perf;


import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.wso2.siddhi.api.QueryFactory;
import org.wso2.siddhi.api.condition.where.ConditionOperator;
import org.wso2.siddhi.api.eventstream.InputEventStream;
import org.wso2.siddhi.api.eventstream.query.Query;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.event.generator.EventGenerator;
import org.wso2.siddhi.core.exception.EventStreamNotFoundException;
import org.wso2.siddhi.core.exception.InvalidQueryException;
import org.wso2.siddhi.core.exception.ProcessorInitializationException;
import org.wso2.siddhi.core.exception.SiddhiException;
import org.wso2.siddhi.core.node.CallbackHandler;
import org.wso2.siddhi.core.node.InputHandler;

import java.util.Date;


public class StateMachineStockQuoteTestCasePerf {

    private static int NUMBER_OF_EVENTS = 1000;
    private static final Logger log = Logger.getLogger(StateMachineStockQuoteTestCasePerf.class);
    public static long initTime = 0;
    public static boolean finished = false;
    InputHandler inputHandlerPinChange;
    InputHandler inputHandlerFraudWarning;
    EventGenerator fraudWarningEventGenerator;
    EventGenerator pinChangeEventGenerator;
    static int counter = 0;
    private int[] accounts = new int[]{12, 14, 58, 89, 34, 89, 13, 57, 23, 3};

    public static void main(String[] args)
            throws InvalidQueryException, ProcessorInitializationException,
                   EventStreamNotFoundException, SiddhiException {
        // Test code
        StateMachineStockQuoteTestCasePerf simSimpleFilterTestCase = new StateMachineStockQuoteTestCasePerf();
        simSimpleFilterTestCase.testCase();
    }

    @Before
    public void info() {
        log.debug("------------------------Query being processed...------------------------------------------------");
    }

    @Test
    public void testCase() throws SiddhiException, ProcessorInitializationException,
                                  InvalidQueryException, EventStreamNotFoundException {
        //Instantiate SiddhiManager

        SiddhiManager siddhiManager = new SiddhiManager();
        QueryFactory qf = SiddhiManager.getQueryFactory();

        InputEventStream fraudWarningEventStream = new InputEventStream(
                "FraudWarningEventStream",
                new String[]{"action", "accountNumber", "timestamp"},
                new Class[]{String.class, Integer.class, Date.class}
        );
        InputEventStream pinChangeEventStream = new InputEventStream(
                "PinChangeEventStream",
                new String[]{"action", "accountNumber", "timestamp"},
                new Class[]{String.class, Integer.class, Date.class}
        );
        siddhiManager.addInputEventStream(pinChangeEventStream);
        siddhiManager.addInputEventStream(fraudWarningEventStream);
        //f=FraudWarningEvent -> p=PINChangeEvent(accountNumber = f.accountNumber
        Query query = qf.createQuery(
                "WarningStream",
                qf.output("action=$0.action", "fraudAccountNumber=$0.accountNumber", "pinAccountNumber=$1.accountNumber"),
                qf.inputStreams(qf.from(pinChangeEventStream), qf.from(fraudWarningEventStream)),
                qf.pattern(
                        qf.every(
                                qf.condition("FraudWarningEventStream.action", ConditionOperator.EQUAL, "FRAUD")
                        ),
                        qf.condition("PinChangeEventStream.accountNumber", ConditionOperator.EQUAL, "$0.accountNumber")
                )
        );


        siddhiManager.addQuery(query);

        siddhiManager.addCallback(new CallbackHandler("WarningStream") {
            public void callBack(Event event) {
//                    System.out.println("************ Event captured  " + event + " ");
//                    if (finished) {
//                        System.out.println(">>>>>>>>>>>>>Total Elapsed Time in milli seconds<<<<<<<<<<<<<");
                System.out.println(counter++ + "::" + (System.currentTimeMillis() - initTime));
//                    System.out.println(System.currentTimeMillis() - initTime);
//                    }
            }
        }
        );

        siddhiManager.update();

        inputHandlerPinChange = siddhiManager.getInputHandler("PinChangeEventStream");
        inputHandlerFraudWarning = siddhiManager.getInputHandler("FraudWarningEventStream");
        //pumping the Events
        initTime = System.currentTimeMillis();

        fraudWarningEventGenerator = EventGenerator.DefaultFactory.create(fraudWarningEventStream);
        pinChangeEventGenerator = EventGenerator.DefaultFactory.create(pinChangeEventStream);

        for (int i = 0; i < NUMBER_OF_EVENTS; i++) {
            generateEvents();
        }

        inputHandlerFraudWarning.sendEvent(fraudWarningEventGenerator.createEvent("FRAUD", 66, new Date(System.currentTimeMillis())));
        inputHandlerPinChange.sendEvent(pinChangeEventGenerator.createEvent("PIN", 66, new Date(System.currentTimeMillis())));
        inputHandlerFraudWarning.sendEvent(fraudWarningEventGenerator.createEvent("FRAUD", 66, new Date(System.currentTimeMillis())));
        inputHandlerPinChange.sendEvent(pinChangeEventGenerator.createEvent("PIN", 66, new Date(System.currentTimeMillis())));

        //    finished = true;
//        try {
//            Thread.sleep(500000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
        siddhiManager.shutDownTask();
    }

    public void generateEvents() {

        int accountNo1 = getValue1();
        int accountNo2 = getValue2();
        inputHandlerFraudWarning.sendEvent(fraudWarningEventGenerator.createEvent("FRAUD", accountNo1, new Date(System.currentTimeMillis())));
        inputHandlerPinChange.sendEvent(pinChangeEventGenerator.createEvent("PIN", accountNo2, new Date(System.currentTimeMillis())));
    }

    int counter1 = 0;

    private int getValue1() {
        if (counter1 >= 10) {
            counter1 = 0;
        }
        return accounts[counter1++];
    }

    int counter2 = 9;

    private int getValue2() {
        if (counter2 < 0) {
            counter2 = 9;
        }
        return accounts[counter2--];
    }

}
