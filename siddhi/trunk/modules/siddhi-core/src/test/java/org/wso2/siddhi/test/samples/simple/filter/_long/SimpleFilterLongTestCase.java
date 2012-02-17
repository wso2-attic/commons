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
package org.wso2.siddhi.test.samples.simple.filter._long;

import junit.framework.Assert;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.wso2.siddhi.api.QueryFactory;
import org.wso2.siddhi.api.eventstream.InputEventStream;
import org.wso2.siddhi.api.eventstream.query.Query;
import org.wso2.siddhi.api.exception.SiddhiPraserException;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.exception.EventStreamNotFoundException;
import org.wso2.siddhi.core.exception.InvalidQueryException;
import org.wso2.siddhi.core.exception.ProcessorInitializationException;
import org.wso2.siddhi.core.exception.SiddhiException;
import org.wso2.siddhi.core.node.CallbackHandler;
import org.wso2.siddhi.core.node.InputHandler;

import static org.wso2.siddhi.api.condition.where.ConditionOperator.GREATERTHAN;


public class SimpleFilterLongTestCase {

    private static final Logger log = Logger.getLogger(SimpleFilterLongTestCase.class);
    private volatile boolean eventCaptured;
    private volatile int i;

    @Before
    public void info() {
        log.debug("------------------------Query being processed...------------------------------------------------");
        eventCaptured = false;
        i = 0;
    }

    @Test
    public void testAPI() throws SiddhiException, ProcessorInitializationException,
                                  InvalidQueryException, EventStreamNotFoundException,
                                  InterruptedException {
        //Instantiate SiddhiManager
        SiddhiManager siddhiManager = new SiddhiManager();

        QueryFactory qf = SiddhiManager.getQueryFactory();

        InputEventStream cseEventStream = new InputEventStream(
                "CSEStream",
                new String[]{"symbol", "price"},
                new Class[]{String.class, Long.class}
        );

        siddhiManager.addInputEventStream(cseEventStream);

        Query query = qf.createQuery(
                "StockQuote",
                qf.output("price=CSEStream.price"),
                qf.from(cseEventStream),
                qf.condition("CSEStream.price", GREATERTHAN, "3000")
        );

        siddhiManager.addQuery(query);

        siddhiManager.addCallback(assignCallback());
        siddhiManager.update();

        InputHandler inputHandler = siddhiManager.getInputHandler("CSEStream");
        sendEvents(inputHandler);


        assertTest();
        siddhiManager.shutDownTask();
    }

    @Test
    public void testQuery() throws SiddhiException, ProcessorInitializationException,
                                   InvalidQueryException, EventStreamNotFoundException,
                                   InterruptedException, SiddhiPraserException {
        //Instantiate SiddhiManager
        SiddhiManager siddhiManager = new SiddhiManager();

        siddhiManager.addConfigurations("CSEStream:= symbol[string], price [long]; \n" +
                                        "" +
                                        "StockQuote:= select price " +
                                        "from CSEStream " +
                                        "where price > 3000;");

        siddhiManager.addCallback(assignCallback());
        siddhiManager.update();

        InputHandler inputHandler = siddhiManager.getInputHandler("CSEStream");
        sendEvents(inputHandler);


        assertTest();
        siddhiManager.shutDownTask();
    }

    private void sendEvents(InputHandler inputHandler) {
        SimpleStockQuoteLongEG eventGenerator = new SimpleStockQuoteLongEG(100);
        //printing the Events
        for (int i = 0; i < 5; i++) {
            Event event = eventGenerator.generateEvent();
            log.debug("Event received: " + event);
            inputHandler.sendEvent(event);
        }
    }

    private void assertTest() throws InterruptedException {
        Thread.sleep(1000);
        Assert.assertTrue(eventCaptured);
        Assert.assertTrue(i == 3);
    }

    private CallbackHandler assignCallback() {
        return new CallbackHandler("StockQuote") {
            public void callBack(Event event) {
                log.debug("       Event captured  " + event + " ");
                if ((Long) event.getNthAttribute(0) == 4000) {
                    eventCaptured = true;
                } else if ((Long) event.getNthAttribute(0) == 5000) {
                    eventCaptured = true;
                } else if ((Long) event.getNthAttribute(0) == 6000) {
                    eventCaptured = true;
                } else {
                    Assert.fail();
                }
                i++;
            }
        };
    }


}
