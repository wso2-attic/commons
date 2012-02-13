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

package org.wso2.siddhi.test.samples.statemachine.sequence;

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
import static org.wso2.siddhi.api.condition.where.ConditionOperator.GREATERTHAN;

public class Sequence8TestCase {

    private static final Logger log = Logger.getLogger(Sequence8TestCase.class);
    private volatile int i;
    private volatile boolean eventCaptured;

    @Before
    public void info() {
        log.debug("-----Query processed: Testing repleting sequence with star, staring with different stream-----");
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

        siddhiManager.addCallback(assignCallback());

        siddhiManager.update();

        InputHandler inputHandlerCseStream = siddhiManager.getInputHandler("CSEStream");
        InputHandler inputHandlerInfoStock = siddhiManager.getInputHandler("infoStock");

        sendEvents(inputHandlerCseStream, inputHandlerInfoStock);
        assertTest();
        siddhiManager.shutDownTask();
    }

    @Test
    public void testQuery() throws SiddhiException, InterruptedException, SiddhiPraserException {
        //Instantiate SiddhiManager
        SiddhiManager siddhiManager = new SiddhiManager();

        QueryFactory qf = SiddhiManager.getQueryFactory();
        siddhiManager.addQueries("CSEStream:= symbol[string], price [int]; \n" +
                                 "infoStock:= action[string],timeStamp[long]; \n" +
                                 "" +
                                 "StockQuote:=select priceA=$a1.price, priceA=$a1[0].price, priceB=$a1[last].price, action=$a2.action \n" +
                                 "from CSEStream, infoStock \n" +
                                 "sequence [a1=CSEStream.price > 70,\n" +
                                 "a2= infoStock.action == 'sell' ] \n" +
                                 "$a1* $a2 ;");

        siddhiManager.addCallback(assignCallback());

        siddhiManager.update();

        InputHandler inputHandlerCseStream = siddhiManager.getInputHandler("CSEStream");
        InputHandler inputHandlerInfoStock = siddhiManager.getInputHandler("infoStock");

        sendEvents(inputHandlerCseStream, inputHandlerInfoStock);
        assertTest();
        siddhiManager.shutDownTask();
    }

    private void assertTest() {
        Assert.assertTrue("Events have not been captured", eventCaptured);
        Assert.assertTrue(i == 3);
    }

    private void sendEvents(InputHandler inputHandlerCseStream, InputHandler inputHandlerInfoStock)
            throws InterruptedException {
        inputHandlerInfoStock.sendEvent(new EventImpl("infoStock", new Object[]{"buy"}));
        inputHandlerInfoStock.sendEvent(new EventImpl("infoStock", new Object[]{"sell"}));
        inputHandlerCseStream.sendEvent(new EventImpl("CSEStream", new Object[]{"IBM", 550}));
        inputHandlerCseStream.sendEvent(new EventImpl("CSEStream", new Object[]{"IBM", 600}));
        inputHandlerCseStream.sendEvent(new EventImpl("CSEStream", new Object[]{"IBM", 700}));
        inputHandlerInfoStock.sendEvent(new EventImpl("infoStock", new Object[]{"sell"}));
        inputHandlerCseStream.sendEvent(new EventImpl("CSEStream", new Object[]{"IBM", 570}));
        inputHandlerInfoStock.sendEvent(new EventImpl("infoStock", new Object[]{"buy"}));
        inputHandlerCseStream.sendEvent(new EventImpl("CSEStream", new Object[]{"IBM", 30}));
        inputHandlerCseStream.sendEvent(new EventImpl("CSEStream", new Object[]{"IBM", 1000}));
        inputHandlerInfoStock.sendEvent(new EventImpl("infoStock", new Object[]{"sell"}));
        Thread.sleep(2000);
    }

    private CallbackHandler assignCallback() {
        return new CallbackHandler("StockQuote") {
            public void callBack(Event event) {
                log.debug("       Event captured  " + event + " ");
                if (null == event.getNthAttribute(0) && null == event.getNthAttribute(2) && i == 0) {
                    eventCaptured = true;
                } else if (550 == (Integer) event.getNthAttribute(0) && 700 == (Integer) event.getNthAttribute(2) && i == 1) {
                    eventCaptured = true;
                } else if (1000 == (Integer) event.getNthAttribute(0) && 1000 == (Integer) event.getNthAttribute(2) && i == 2) {
                    eventCaptured = true;
                } else {
                    Assert.fail();
                }
                i++;
            }
        };
    }


}
