package org.wso2.siddhi.test.samples.complex.reset;

import junit.framework.Assert;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.wso2.siddhi.api.QueryFactory;
import org.wso2.siddhi.api.eventstream.InputEventStream;
import org.wso2.siddhi.api.eventstream.query.Query;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.event.EventImpl;
import org.wso2.siddhi.core.exception.ProcessorInitializationException;
import org.wso2.siddhi.core.exception.SiddhiException;
import org.wso2.siddhi.core.node.CallbackHandler;
import org.wso2.siddhi.core.node.InputHandler;

import static org.wso2.siddhi.api.condition.where.ConditionOperator.EQUAL;
import static org.wso2.siddhi.api.condition.where.ConditionOperator.GREATERTHAN;

public class ResetSequenceTestCase {

    private static final Logger log = Logger.getLogger(ResetSequenceTestCase.class);
    private volatile int i = 0;
    private volatile boolean eventCaptured = false;

    public static void main(String[] args)
            throws ProcessorInitializationException, InterruptedException, SiddhiException {
        // Test code
        new ResetSequenceTestCase().testCase();
    }

    @Before
    public void info() {
        log.debug("-----Query processed: Resets Sequence Processors-----");


    }

    @Test
    public void testCase() throws SiddhiException, ProcessorInitializationException,
                                  InterruptedException, InterruptedException {
        //Instantiate SiddhiManager
        SiddhiManager siddhiManager = new SiddhiManager();

        QueryFactory qf = siddhiManager.getQueryFactory();

        InputEventStream cseEventStream = new InputEventStream(
                "CSEStream",
                new String[]{"symbol", "price"},
                new Class[]{String.class, Integer.class}
        );

        InputEventStream infoStock = new InputEventStream(
                "infoStock",
                new String[]{"action"},
                new Class[]{String.class}
        );
        siddhiManager.addInputEventStream(cseEventStream);
        siddhiManager.addInputEventStream(infoStock);

        Query query = qf.createQuery(
                "StockQuote",
                qf.output("price=$0.price", "price=$0.first.price", "price=$0.last.price", "action=$1.action"),
                qf.inputStreams(qf.from(cseEventStream), qf.from(infoStock)),
                qf.sequence(
                        qf.star(
                                qf.condition("CSEStream.price", GREATERTHAN, "70")
                        ),
                        qf.condition("infoStock.action", EQUAL, "sell")
                )
        );

        siddhiManager.addQuery(query);

        siddhiManager.addCallback(new CallbackHandler("StockQuote") {
            public void callBack(Event event) {
                log.debug("       Event captured  " + event + " ");
                if (null == event.getNthAttribute(0) && null == event.getNthAttribute(2) && i == 0) {
                    eventCaptured = true;
                } else if (700 == (Integer) event.getNthAttribute(0) && 700 == (Integer) event.getNthAttribute(2) && i == 1) {
                    eventCaptured = true;
                } else if (1000 == (Integer) event.getNthAttribute(0) && 1000 == (Integer) event.getNthAttribute(2) && i == 2) {
                    eventCaptured = true;
                } else {
                    Assert.fail();
                }
                i++;
            }
        }
        );

        siddhiManager.update();


        InputHandler inputHandlerCseStream = siddhiManager.getInputHandler("CSEStream");
        InputHandler inputHandlerInfoStock = siddhiManager.getInputHandler("infoStock");
        //pumping the Events
        inputHandlerCseStream.sendEvent(new EventImpl("infoStock", new Object[]{"buy"}));
        inputHandlerCseStream.sendEvent(new EventImpl("infoStock", new Object[]{"sell"}));
        inputHandlerCseStream.sendEvent(new EventImpl("CSEStream", new Object[]{"IBM", 550}));
        inputHandlerCseStream.sendEvent(new EventImpl("CSEStream", new Object[]{"IBM", 600}));
        siddhiManager.reset(5);
        Thread.sleep(1000);
        inputHandlerCseStream.sendEvent(new EventImpl("CSEStream", new Object[]{"IBM", 700}));
        inputHandlerCseStream.sendEvent(new EventImpl("infoStock", new Object[]{"sell"}));
        inputHandlerCseStream.sendEvent(new EventImpl("CSEStream", new Object[]{"IBM", 570}));
        inputHandlerCseStream.sendEvent(new EventImpl("infoStock", new Object[]{"buy"}));
        inputHandlerCseStream.sendEvent(new EventImpl("CSEStream", new Object[]{"IBM", 30}));
        inputHandlerCseStream.sendEvent(new EventImpl("CSEStream", new Object[]{"IBM", 1000}));
        inputHandlerCseStream.sendEvent(new EventImpl("infoStock", new Object[]{"sell"}));
        Thread.sleep(2000);
        Assert.assertTrue("Events have not been captured", eventCaptured);
        Assert.assertTrue(i == 3);
        siddhiManager.shutDownTask();
    }


}
