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

package org.wso2.siddhi.test.samples.complex;

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
import org.wso2.siddhi.core.eventstream.StreamReference;
import org.wso2.siddhi.core.exception.InvalidQueryException;
import org.wso2.siddhi.core.exception.ProcessorInitializationException;
import org.wso2.siddhi.core.exception.SiddhiException;
import org.wso2.siddhi.core.node.CallbackHandler;
import org.wso2.siddhi.core.node.InputHandler;

import static org.wso2.siddhi.api.condition.where.ConditionOperator.EQUAL;
import static org.wso2.siddhi.api.condition.where.ConditionOperator.GREATERTHAN;


public class MultipleDynamicQueryAllocationTestCase {

    private static final Logger log = Logger.getLogger(MultipleDynamicQueryAllocationTestCase.class);
    private volatile int i = 0;
    private volatile int j = 0;
    private volatile boolean eventCaptured = false;

    public static void main(String[] args)
            throws InvalidQueryException, ProcessorInitializationException, InterruptedException,
                   SiddhiException {
        // Test code
        MultipleDynamicQueryAllocationTestCase simSimpleFilterTestCase = new MultipleDynamicQueryAllocationTestCase();
        simSimpleFilterTestCase.testCase();
    }

    @Before
    public void info() {
        log.debug("-----Query processed: Running multiple queries dynamically-----");
    }

    @Test
    public void testCase() throws SiddhiException, ProcessorInitializationException,
                                  InvalidQueryException,
                                  InterruptedException {

        //Instantiate SiddhiManager
        SiddhiManager siddhiManager = new SiddhiManager();
        QueryFactory qf = SiddhiManager.getQueryFactory();

        InputEventStream cseEventStream = new InputEventStream(
                "CSEStream",
                new String[]{"symbol", "price"},
                new Class[]{String.class, Integer.class}
        );

        InputHandler inputHandler = siddhiManager.addInputEventStream(cseEventStream);
        Query query = qf.createQuery(
                "StockQuote",
                qf.output("price=CSEStream.price"),
                qf.from(cseEventStream),
                qf.condition("CSEStream.symbol", EQUAL, "WSO2")
        );
        StreamReference streamReference = siddhiManager.addQuery(query);

        Query query1 = qf.createQuery(
                "StockQuote",
                qf.output("price=CSEStream.price"),
                qf.from(cseEventStream),
                qf.condition("CSEStream.symbol", EQUAL, "IBM")
        );
        StreamReference streamReference1 = siddhiManager.addQuery(query1);

        Query query2 = qf.createQuery(
                "StockQuoteOutput",
                qf.output("price=StockQuote.price"),
                qf.from(query1),
                qf.condition("StockQuote.price", GREATERTHAN, "102")
        );
        StreamReference streamReference2 = siddhiManager.addQuery(query2);

        Query query3 = qf.createQuery(
                "StockQuoteOutput",
                qf.output("price=StockQuote.price"),
                qf.from(query1),
                qf.condition("StockQuote.price", GREATERTHAN, "102")

        );
        StreamReference streamReference3 = siddhiManager.addQuery(query3);

        siddhiManager.addCallback(addCallback1());
        siddhiManager.addCallback(addCallback2());
        siddhiManager.update();

        sendEvents(inputHandler);


        log.debug("1st set of Queries end");
        Thread.sleep(1000);
        siddhiManager.removeStream(streamReference);
        siddhiManager.removeStream(streamReference2);

        siddhiManager.update();

        Thread.sleep(1000);
        Query query4 = qf.createQuery(
                "StockQuoteOutput",
                qf.output("price=CSEStream.price"),
                qf.from(cseEventStream),
                qf.condition("CSEStream.symbol", EQUAL, "WSO2"));

        siddhiManager.addQuery(query4);
        siddhiManager.update();

        sendEvents(inputHandler);

        log.debug("2nd set of Queries end");
        assertEvents();
        siddhiManager.shutDownTask();
    }

    private void assertEvents() throws InterruptedException {
        Thread.sleep(1000);

        Assert.assertTrue(eventCaptured);
        Assert.assertTrue(i == 10);
        Assert.assertTrue(j == 8);
    }

    private CallbackHandler addCallback1() {
        return new CallbackHandler("StockQuoteOutput") {
            public void callBack(Event event) {
                log.debug("       Event captured  " + event + " ");
                if ((Integer) event.getNthAttribute(0) == 105) {
                    eventCaptured = true;
                } else if ((Integer) event.getNthAttribute(0) == 109) {
                    eventCaptured = true;
                } else if ((Integer) event.getNthAttribute(0) == 110) {
                    eventCaptured = true;
                } else if ((Integer) event.getNthAttribute(0) == 100) {
                    eventCaptured = true;
                } else {
                    Assert.fail();
                }
                i++;
            }
        };
    }

    private CallbackHandler addCallback2() {
        return new CallbackHandler("StockQuote") {
            public void callBack(Event event) {
                log.debug("       Event captured  " + event + " ");
                if ((Integer) event.getNthAttribute(0) == 102) {
                    eventCaptured = true;
                } else if ((Integer) event.getNthAttribute(0) == 100) {
                    eventCaptured = true;
                } else if ((Integer) event.getNthAttribute(0) == 105) {
                    eventCaptured = true;
                } else if ((Integer) event.getNthAttribute(0) == 109) {
                    eventCaptured = true;
                } else if ((Integer) event.getNthAttribute(0) == 110) {
                    eventCaptured = true;
                } else {
                    Assert.fail();
                }
                j++;
            }
        };
    }

    private void sendEvents(InputHandler inputHandler) {
        inputHandler.sendEvent(new EventImpl("CSEStream", new Object[]{"IBM", 102}));
        inputHandler.sendEvent(new EventImpl("CSEStream", new Object[]{"IBM", 105}));
        inputHandler.sendEvent(new EventImpl("CSEStream", new Object[]{"WSO2", 100}));
        inputHandler.sendEvent(new EventImpl("CSEStream", new Object[]{"IBM", 110}));
        inputHandler.sendEvent(new EventImpl("CSEStream", new Object[]{"WSO2", 109}));
    }


}
