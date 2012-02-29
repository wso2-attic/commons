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
package org.wso2.siddhi.test.samples.statemachine.pattern.standard;

import org.apache.log4j.Logger;
import org.junit.Assert;
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
import org.wso2.siddhi.test.samples.statemachine.pattern.InfoStockPatternEG;
import org.wso2.siddhi.test.samples.statemachine.pattern.SimpleStockQuotePatternEG;

import static org.wso2.siddhi.api.condition.where.ConditionOperator.EQUAL;
import static org.wso2.siddhi.api.condition.where.ConditionOperator.GREATERTHAN;


public class StateMachine1TestCase {

    private static final Logger log = Logger.getLogger(StateMachine1TestCase.class);
    private volatile boolean eventCaptured;

    @Before
    public void info() {
        log.debug("-----Query processed: Testing State-machine with complex conditions-----");
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
                new String[]{"action", "timeStamp"},
                new Class[]{String.class, Long.class}
        );
        siddhiManager.addInputEventStream(cseEventStream);
        siddhiManager.addInputEventStream(infoStock);

        Query query = qf.createQuery(
                "StockQuote",
                qf.output("action=$0.action", "priceA=$1.price", "priceA=$2.price"),
                qf.inputStreams(qf.from(cseEventStream), qf.from(infoStock)),
                qf.pattern(
                        qf.condition("infoStock.action", EQUAL, "buy"),  //0
                        qf.every(
                                qf.condition("CSEStream.price*2", GREATERTHAN, "75"), //1
                                qf.condition("CSEStream.price", GREATERTHAN, "$1.price")  //2
                        )
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

        siddhiManager.addConfigurations("CSEStream:= symbol[string], price [int]; \n" +
                                        "infoStock:= action[string], timeStamp[long]; \n" +
                                        "" +
                                        "StockQuote:=select action=$a1.action, priceA=$b1.price, priceA=$b2.price\n" +
                                        "from CSEStream, infoStock\n" +
                                        "pattern [a1=infoStock.action==\"buy\",\n" +
                                        "b1= CSEStream.price>70,\n" +
                                        "b2= CSEStream.price> $b1.price  ]\n" +
                                        "$a1 -> every($b1 -> $b2) ;");

        siddhiManager.addCallback(assignCallback());
        siddhiManager.update();

        InputHandler inputHandlerCseStream = siddhiManager.getInputHandler("CSEStream");
        InputHandler inputHandlerInfoStock = siddhiManager.getInputHandler("infoStock");
        sendEvents(inputHandlerCseStream, inputHandlerInfoStock);
        assertTest();
        siddhiManager.shutDownTask();
    }

    private void assertTest() throws InterruptedException {
        Thread.sleep(3000);
        Assert.assertTrue(eventCaptured);
    }

    private void sendEvents(InputHandler inputHandlerCseStream,
                            InputHandler inputHandlerInfoStock) {
        SimpleStockQuotePatternEG eventGeneratorCseStream = new SimpleStockQuotePatternEG();
        InfoStockPatternEG eventGeneratorInfoStackPattern = new InfoStockPatternEG();
        //pumping the Events;
        for (int i = 0; i < 40; i++) {
            //  try {
            if (i % 2 == 0) {
                Event event = eventGeneratorCseStream.generateEvent();
                inputHandlerCseStream.sendEvent(event);
            } else {
                Event event = eventGeneratorInfoStackPattern.generateEvent();
                inputHandlerInfoStock.sendEvent(event);
            }
        }
    }

    private CallbackHandler assignCallback() {
        return new CallbackHandler("StockQuote") {
            public void callBack(Event event) {
                log.debug("       Event captured  " + event);
                if ((Integer) event.getNthAttribute(1) == 77) {
                    Assert.assertEquals(103, event.getNthAttribute(2));
                    eventCaptured = true;
                } else if ((Integer) event.getNthAttribute(1) == 76) {
                    Assert.assertEquals(77, event.getNthAttribute(2));
                    eventCaptured = true;
                } else {
                    Assert.assertEquals(103, event.getNthAttribute(2));
                    eventCaptured = true;
                }
            }
        };
    }


}
