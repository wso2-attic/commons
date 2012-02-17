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
package org.wso2.siddhi.test.samples.simple.filter._string;

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

import static org.wso2.siddhi.api.condition.where.ConditionOperator.EQUAL;


public class SimpleFilterTestCase {

    private static final Logger log = Logger.getLogger(SimpleFilterTestCase.class);
    private volatile boolean eventCaptured;
    private volatile int i;

    @Before
    public void info() {
        log.debug("------------------------Query being processed...------------------------------------------------");
        i = 0;
        eventCaptured = false;
    }

    @Test
    public void testAPI() throws EventStreamNotFoundException, InterruptedException,
                                 ProcessorInitializationException, InvalidQueryException,
                                 SiddhiException {
        //Instantiate SiddhiManager
        SiddhiManager siddhiManager = new SiddhiManager();

        QueryFactory qf = SiddhiManager.getQueryFactory();

        InputEventStream cseEventStream = new InputEventStream(
                "CSEStream",
                new String[]{"symbol", "price"},
                new Class[]{String.class, Float.class}
        );

        siddhiManager.addInputEventStream(cseEventStream);

        Query query = qf.createQuery(
                "StockQuote",
                qf.output("symbol=CSEStream.symbol"),
                qf.from(cseEventStream),
                qf.condition("CSEStream.symbol", EQUAL, "IBM")
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
    public void testQuery() throws EventStreamNotFoundException, InterruptedException,
                                   ProcessorInitializationException, InvalidQueryException,
                                   SiddhiException, SiddhiPraserException {
        //Instantiate SiddhiManager
        SiddhiManager siddhiManager = new SiddhiManager();

        siddhiManager.addConfigurations("CSEStream:= symbol[string], price [float]; \n" +
                                        "" +
                                        "StockQuote:= select symbol " +
                                        "from CSEStream " +
                                        "where symbol == 'IBM';");

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
        Assert.assertTrue(i == 2);
    }

    private void sendEvents(InputHandler inputHandler) {
        SimpleStockQuoteStringEG eventGenerator = new SimpleStockQuoteStringEG();
        //printing the Events
        for (int i = 0; i < 5; i++) {
            Event event = eventGenerator.generateEvent();
            log.debug("Event received: " + event);
            inputHandler.sendEvent(event);
        }
    }

    private CallbackHandler assignCallback() {
        return new CallbackHandler("StockQuote") {
            public void callBack(Event event) {
                log.debug("       Event captured  " + event + " ");
                if (((String) event.getNthAttribute(0)).contains("IBM")) {
                    eventCaptured = true;
                } else {
                    Assert.fail();
                }
                i++;
            }
        };
    }


}
