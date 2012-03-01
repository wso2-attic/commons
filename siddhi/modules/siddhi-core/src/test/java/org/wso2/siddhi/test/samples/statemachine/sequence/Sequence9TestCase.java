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

import static org.wso2.siddhi.api.condition.where.ConditionOperator.GREATERTHAN;
import static org.wso2.siddhi.api.condition.where.ConditionOperator.LESSTHAN;

public class Sequence9TestCase {

    private static final Logger log = Logger.getLogger(Sequence9TestCase.class);
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

        siddhiManager.addInputEventStream(cseEventStream);

        Query query = qf.createQuery(
                "StockQuote",
                qf.output("price=$0.symbol", "price=$0.price", "price=$2.last.price", "price=$3.price"),
                qf.inputStreams(qf.from(cseEventStream)),
                qf.sequence(
                        qf.condition("CSEStream.price", GREATERTHAN, "70"),
                        qf.condition("CSEStream.price", GREATERTHAN, "$0.price"),
                        qf.star(
                                qf.condition("CSEStream.price", GREATERTHAN, "$2.prev.price")
                        ),
                        qf.condition("CSEStream.price", LESSTHAN, "$2.last.price")
                )
        );
        query.groupBy("CSEStream.symbol");

        siddhiManager.addQuery(query);

        siddhiManager.addCallback(assignCallback());

        siddhiManager.update();

        InputHandler inputHandlerCseStream = siddhiManager.getInputHandler("CSEStream");

        sendEvents(inputHandlerCseStream);
        assertTest();
        siddhiManager.shutDownTask();
    }

    @Test
    public void testQuery() throws SiddhiException, InterruptedException, SiddhiPraserException {
        //Instantiate SiddhiManager
        SiddhiManager siddhiManager = new SiddhiManager();

        siddhiManager.addConfigurations("CSEStream:= symbol[string], price [int]; \n" +
                                        "" +
                                        "StockQuote:=select priceA=$a1.symbol, priceB=$a1.price, priceD=$a3[last].price, priceE=$a4.price " +
                                        "from CSEStream \n" +
                                        "sequence [ a1=CSEStream.price > 70, " +
                                        "           a2=CSEStream.price > $a1.price, " +
                                        "           a3=CSEStream.price > $this[prev].price, " +
                                        "           a4=CSEStream.price < $a3[last].price] " +
                                        "$a1 $a2 $a3* $a4 " +
                                        "group by CSEStream.symbol;");

        siddhiManager.addCallback(assignCallback());

        siddhiManager.update();

        InputHandler inputHandlerCseStream = siddhiManager.getInputHandler("CSEStream");

        sendEvents(inputHandlerCseStream);
        assertTest();
        siddhiManager.shutDownTask();
    }

    private void assertTest() {
        Assert.assertTrue("Events have not been captured", eventCaptured);
        Assert.assertTrue(i == 4);
    }

    private void sendEvents(InputHandler inputHandlerCseStream)
            throws InterruptedException {
        inputHandlerCseStream.sendEvent(new EventImpl("CSEStream", new Object[]{"IBM", 60}));
        inputHandlerCseStream.sendEvent(new EventImpl("CSEStream", new Object[]{"IBM", 75}));
        inputHandlerCseStream.sendEvent(new EventImpl("CSEStream", new Object[]{"IBM", 91}));
        inputHandlerCseStream.sendEvent(new EventImpl("CSEStream", new Object[]{"IBM", 104}));
        inputHandlerCseStream.sendEvent(new EventImpl("CSEStream", new Object[]{"IBM", 90}));

        inputHandlerCseStream.sendEvent(new EventImpl("CSEStream", new Object[]{"WSO2", 60}));
        inputHandlerCseStream.sendEvent(new EventImpl("CSEStream", new Object[]{"WSO2", 75}));

        inputHandlerCseStream.sendEvent(new EventImpl("CSEStream", new Object[]{"IBM", 80}));
        inputHandlerCseStream.sendEvent(new EventImpl("CSEStream", new Object[]{"IBM", 60}));
        inputHandlerCseStream.sendEvent(new EventImpl("CSEStream", new Object[]{"IBM", 75}));

        inputHandlerCseStream.sendEvent(new EventImpl("CSEStream", new Object[]{"WSO2", 91}));

        inputHandlerCseStream.sendEvent(new EventImpl("CSEStream", new Object[]{"IBM", 91}));
        inputHandlerCseStream.sendEvent(new EventImpl("CSEStream", new Object[]{"IBM", 104}));

        inputHandlerCseStream.sendEvent(new EventImpl("CSEStream", new Object[]{"WSO2", 104}));
        inputHandlerCseStream.sendEvent(new EventImpl("CSEStream", new Object[]{"WSO2", 90}));
        inputHandlerCseStream.sendEvent(new EventImpl("CSEStream", new Object[]{"WSO2", 80}));
        inputHandlerCseStream.sendEvent(new EventImpl("CSEStream", new Object[]{"WSO2", 60}));
        inputHandlerCseStream.sendEvent(new EventImpl("CSEStream", new Object[]{"WSO2", 75}));
        inputHandlerCseStream.sendEvent(new EventImpl("CSEStream", new Object[]{"WSO2", 91}));
        inputHandlerCseStream.sendEvent(new EventImpl("CSEStream", new Object[]{"WSO2", 104}));
        inputHandlerCseStream.sendEvent(new EventImpl("CSEStream", new Object[]{"WSO2", 80}));
        inputHandlerCseStream.sendEvent(new EventImpl("CSEStream", new Object[]{"WSO2", 90}));

        inputHandlerCseStream.sendEvent(new EventImpl("CSEStream", new Object[]{"IBM", 80}));
        inputHandlerCseStream.sendEvent(new EventImpl("CSEStream", new Object[]{"IBM", 90}));

        Thread.sleep(2000);
    }

    private CallbackHandler assignCallback() {
        return new CallbackHandler("StockQuote") {
            public void callBack(Event event) {
                log.debug("       Event captured  " + event + " ");
                if ("IBM".equals(event.getNthAttribute(0)) && 75 == (Integer) event.getNthAttribute(1) && 90 == (Integer) event.getNthAttribute(3) && i == 0) {
                    eventCaptured = true;
                } else if ("WSO2".equals(event.getNthAttribute(0)) && 75 == (Integer) event.getNthAttribute(1) && 90 == (Integer) event.getNthAttribute(3) && i == 1) {
                    eventCaptured = true;
                } else if ("WSO2".equals(event.getNthAttribute(0)) && 75 == (Integer) event.getNthAttribute(1) && 80 == (Integer) event.getNthAttribute(3) && i == 2) {
                    eventCaptured = true;
                } else if ("IBM".equals(event.getNthAttribute(0)) && 75 == (Integer) event.getNthAttribute(1) && 80 == (Integer) event.getNthAttribute(3) && i == 3) {
                    eventCaptured = true;
                } else {
                    Assert.fail();
                }
                i++;
            }
        };
    }


}
