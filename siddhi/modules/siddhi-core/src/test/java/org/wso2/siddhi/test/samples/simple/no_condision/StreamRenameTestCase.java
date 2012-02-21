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

package org.wso2.siddhi.test.samples.simple.no_condision;

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
import org.wso2.siddhi.core.exception.EventStreamNotFoundException;
import org.wso2.siddhi.core.exception.InvalidQueryException;
import org.wso2.siddhi.core.exception.ProcessorInitializationException;
import org.wso2.siddhi.core.exception.SiddhiException;
import org.wso2.siddhi.core.node.CallbackHandler;
import org.wso2.siddhi.core.node.InputHandler;

public class StreamRenameTestCase {

    private static final Logger log = Logger.getLogger(StreamRenameTestCase.class);
    private volatile int i;
    private volatile boolean eventCaptured;

    @Before
    public void info() {
        log.debug("-----Query processed: Renaming the stream, and not using conditions-----");
        i = 0;
        eventCaptured = false;
    }

    @Test
    public void testAPI() throws SiddhiException, ProcessorInitializationException,
                                 InvalidQueryException, EventStreamNotFoundException,
                                 InterruptedException {
        //Instantiate SiddhiManager
        SiddhiManager siddhiManager = new SiddhiManager();

        QueryFactory qf = SiddhiManager.getQueryFactory();

        InputEventStream stockQuoteStream = new InputEventStream(
                "StockQuoteStream",
                new String[]{"timestamp", "symbol", "price", "volume"},
                new Class[]{Integer.class, String.class, Integer.class, Integer.class}
        );

        siddhiManager.addInputEventStream(stockQuoteStream);

        //stock+ a[ ], stock b
        Query query = qf.createQuery(
                "StockQuote",
                qf.output("symbol", "price"),
                qf.from(stockQuoteStream));


        siddhiManager.addQuery(query);

        siddhiManager.addCallback(assignCallback());
        siddhiManager.update();

        InputHandler inputHandlerCseStream = siddhiManager.getInputHandler("StockQuoteStream");
        sendEents(inputHandlerCseStream);

        assertTest();
        siddhiManager.shutDownTask();
    }

    @Test
    public void testQuery() throws Exception {
        //Instantiate SiddhiManager
        SiddhiManager siddhiManager = new SiddhiManager();

        siddhiManager.addConfigurations("StockQuoteStream:= timestamp[int], symbol [string], price [int], volume [int]; \n");

        siddhiManager.addQuery("StockQuote:=select symbol,price from StockQuoteStream ;");

        siddhiManager.addCallback(assignCallback());
        siddhiManager.update();

        InputHandler inputHandlerCseStream = siddhiManager.getInputHandler("StockQuoteStream");
        sendEents(inputHandlerCseStream);

        assertTest();
        siddhiManager.shutDownTask();
    }

    private void assertTest() throws InterruptedException {
        Thread.sleep(1000);

        Assert.assertTrue(eventCaptured);
        Assert.assertTrue(i == 2);
    }

    private void sendEents(InputHandler inputHandlerCseStream) {
        inputHandlerCseStream.sendEvent(new EventImpl("StockQuoteStream", new Object[]{1, "IBM", 400, 1000}));
        inputHandlerCseStream.sendEvent(new EventImpl("StockQuoteStream", new Object[]{4, "IBM", 100, 200}));
    }

    private CallbackHandler assignCallback() {
        return new CallbackHandler("StockQuote") {

            public void callBack(Event event) {
                log.debug("       Event captured  " + event + " ");
                if ((Integer) event.getNthAttribute(1) == 100) {
                    eventCaptured = true;
                    i++;
                } else if ((Integer) event.getNthAttribute(1) == 400) {
                    eventCaptured = true;
                    i++;
                } else {
                    Assert.fail();
                }

            }
        };
    }


}