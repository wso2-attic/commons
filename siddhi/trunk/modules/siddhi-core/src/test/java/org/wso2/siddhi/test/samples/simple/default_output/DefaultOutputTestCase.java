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

package org.wso2.siddhi.test.samples.simple.default_output;

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
import org.wso2.siddhi.core.event.EventImpl;
import org.wso2.siddhi.core.exception.SiddhiException;
import org.wso2.siddhi.core.node.CallbackHandler;
import org.wso2.siddhi.core.node.InputHandler;

import static org.wso2.siddhi.api.condition.where.ConditionOperator.EQUAL;

public class DefaultOutputTestCase {

    private static final Logger log = Logger.getLogger(DefaultOutputTestCase.class);
    private volatile int i;
    private volatile boolean eventCaptured;

    @Before
    public void info() {
        log.debug("-----Query processed: Testing default output generation-----");
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
                new Class[]{String.class, Integer.class}
        );

        InputHandler inputHandler = siddhiManager.addInputEventStream(cseEventStream);

        Query query = qf.createQuery(
                "StockQuote",
                qf.output("symbol=CSEStream.symbol", "avgPrice=avg(CSEStream.price)", "count=count(CSEStream.symbol)", "typeNo=(int)23"),
                qf.from(cseEventStream),
                qf.condition("CSEStream.symbol", EQUAL, "IBM")
        );
        siddhiManager.addQuery(query);

        siddhiManager.addCallback(assignCallback());

        siddhiManager.update();

        sendEvent(inputHandler);
        assertTest();
        siddhiManager.shutDownTask();
    }

    @Test
    public void testQuery() throws SiddhiException, InterruptedException, SiddhiPraserException {
        //Instantiate SiddhiManager
        SiddhiManager siddhiManager = new SiddhiManager();

        siddhiManager.addQueries("CSEStream:= symbol[string], price [int]; \n" +
                                 "" +
                                 "StockQuote:= select symbol, avgPrice=avg(price), symbolCount=count(symbol), typeNo=(int)23 " +
                                 "from CSEStream[win.time=2000] " +
                                 "where symbol=='IBM');");

        siddhiManager.addCallback(assignCallback());

        siddhiManager.update();

        sendEvent(siddhiManager.getInputHandler("CSEStream"));
        assertTest();
        siddhiManager.shutDownTask();
    }

    private CallbackHandler assignCallback() {
        return new CallbackHandler("StockQuote") {
            public void callBack(Event event) {
                log.debug("       Event captured  " + event + " ");
                if ((Integer) event.getNthAttribute(1) == 1000 & i == 0) {
                    eventCaptured = true;
                }
                i++;
            }
        };
    }

    private void sendEvent(InputHandler inputHandler) {
        inputHandler.sendEvent(new EventImpl("CSEStream", new Object[]{"IBM", 1000}));
    }

    private void assertTest() throws InterruptedException {
        Thread.sleep(1000);
        Assert.assertTrue(eventCaptured);
        Assert.assertTrue(i == 1);
    }

}
