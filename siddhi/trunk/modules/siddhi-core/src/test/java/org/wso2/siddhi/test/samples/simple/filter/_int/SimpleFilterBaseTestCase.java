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
package org.wso2.siddhi.test.samples.simple.filter._int;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.wso2.siddhi.api.QueryFactory;
import org.wso2.siddhi.api.condition.where.SimpleCondition;
import org.wso2.siddhi.api.eventstream.InputEventStream;
import org.wso2.siddhi.api.eventstream.query.Query;
import org.wso2.siddhi.api.exception.SiddhiPraserException;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.exception.SiddhiException;
import org.wso2.siddhi.core.node.CallbackHandler;
import org.wso2.siddhi.core.node.InputHandler;


public abstract class SimpleFilterBaseTestCase {

    private final Logger log = Logger.getLogger(this.getClass());
    private volatile boolean eventCaptured;

    @Before
    public void info() {
        log.debug("------------------------Query being processed...------------------------------------------------");
        eventCaptured = false;
    }

    @Test
    public void testAPI() throws SiddhiException, InterruptedException {
        //Instantiate SiddhiManager
        SiddhiManager siddhiManager = new SiddhiManager();

        QueryFactory qf = SiddhiManager.getQueryFactory();

        InputEventStream cseEventStream = new InputEventStream(
                "CSEStream",
                new String[]{"symbol", "price"},
                new Class[]{String.class, Integer.class}
        );

        siddhiManager.addInputEventStream(cseEventStream);

        Query query = qf.createQuery(
                "StockQuote",
                qf.output("price=CSEStream.price"),
                qf.from(cseEventStream),
                createAPICondition(qf)
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
    public void testQuery() throws SiddhiException, InterruptedException, SiddhiPraserException {
        //Instantiate SiddhiManager
        SiddhiManager siddhiManager = new SiddhiManager();


        siddhiManager.addQueries("CSEStream:= symbol[string], price [int]; \n" +
                                 "" +
                                 "StockQuote:= select price " +
                                 "from CSEStream " +
                                 "where " + createQueryCondition() + ";");


        siddhiManager.addCallback(assignCallback());
        siddhiManager.update();

        InputHandler inputHandler = siddhiManager.getInputHandler("CSEStream");
        sendEvents(inputHandler);
        assertTest();
        siddhiManager.shutDownTask();
    }

    private void assertTest() throws InterruptedException {
        Thread.sleep(1000);
        Assert.assertTrue(eventCaptured);
    }

    private void sendEvents(InputHandler inputHandler) {
        int numberOfEvents = setNumberOfEvents();
        SimpleStockQuoteEG eventGenerator = new SimpleStockQuoteEG(20);
        //printing the Events
        for (int i = 0; i < numberOfEvents; i++) {
            Event event = eventGenerator.generateEvent();
            log.debug("Event received: " + event);
            inputHandler.sendEvent(event);
        }
    }

    private CallbackHandler assignCallback() {
        return new CallbackHandler("StockQuote") {
            public void callBack(Event event) {
                log.debug("       Event captured  " + event.getNthAttribute(0) + " ");
                assertEvent(event);
                eventCaptured = true;
            }
        };
    }

    protected int setNumberOfEvents() {
        return 20;
    }

    protected abstract void assertEvent(Event event);

    protected abstract SimpleCondition createAPICondition(QueryFactory qf);
    protected abstract String createQueryCondition();


}
