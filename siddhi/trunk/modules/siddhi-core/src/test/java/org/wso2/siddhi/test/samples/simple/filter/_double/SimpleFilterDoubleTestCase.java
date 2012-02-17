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
package org.wso2.siddhi.test.samples.simple.filter._double;

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
import org.wso2.siddhi.core.exception.SiddhiException;
import org.wso2.siddhi.core.node.CallbackHandler;
import org.wso2.siddhi.core.node.InputHandler;

import static org.wso2.siddhi.api.condition.where.ConditionOperator.GREATERTHAN;


public class SimpleFilterDoubleTestCase {

    private static final Logger log = Logger.getLogger(SimpleFilterDoubleTestCase.class);
    private volatile boolean eventCaptured;
    private volatile int i;

    @Before
    public void info() {
        log.debug("------------------------Query being processed...------------------------------------------------");
        i = 0;
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
                new Class[]{String.class, Double.class}
        );

        siddhiManager.addInputEventStream(cseEventStream);

        Query query = qf.createQuery(
                "StockQuote",
                qf.output("price=CSEStream.price"),
                qf.from(cseEventStream),
                qf.condition("CSEStream.price", GREATERTHAN, "4000.555")
        );

        siddhiManager.addQuery(query);

        siddhiManager.addCallback(assignCallback());
        siddhiManager.update();
        InputHandler inputHandler = siddhiManager.getInputHandler("CSEStream");
        sendEents(inputHandler);

        assertTest();
        siddhiManager.shutDownTask();
    }   @Test
    public void testQuery() throws SiddhiException, InterruptedException, SiddhiPraserException {
        //Instantiate SiddhiManager
        SiddhiManager siddhiManager = new SiddhiManager();

        siddhiManager.addConfigurations("CSEStream:= symbol[string], price [double]; \n" +
                                        "" +
                                        "StockQuote:= select price " +
                                        "from CSEStream " +
                                        "where price > 4000.555;");

        siddhiManager.addCallback(assignCallback());
        siddhiManager.update();
        InputHandler inputHandler = siddhiManager.getInputHandler("CSEStream");
        sendEents(inputHandler);

        assertTest();
        siddhiManager.shutDownTask();
    }

    private CallbackHandler assignCallback() {
        return new CallbackHandler("StockQuote") {
            public void callBack(Event event) {
                log.debug("       Event captured  " + event + " ");
                if ((Double) event.getNthAttribute(0) == 5000.555) {
                    eventCaptured = true;
                } else if ((Double) event.getNthAttribute(0) == 6000.555) {
                    eventCaptured = true;
                } else {
                    Assert.fail();
                }
                i++;
            }
        };
    }

    private void sendEents(InputHandler inputHandler) {
        SimpleStockQuoteDoubleEG eventGenerator = new SimpleStockQuoteDoubleEG(100);
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
        Assert.assertTrue(i == 2);
    }


}
