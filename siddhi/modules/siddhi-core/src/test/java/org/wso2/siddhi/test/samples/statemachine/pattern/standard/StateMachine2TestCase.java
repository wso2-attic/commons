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
import static org.wso2.siddhi.api.condition.where.ConditionOperator.GREATERTHAN_EQUAL;


public class StateMachine2TestCase {

    private static final Logger log = Logger.getLogger(StateMachine2TestCase.class);
    private volatile boolean eventCaptured;
    private volatile int i;

    @Before
    public void info() {
        log.debug("-----Query processed: Testing State-machine with complex conditions-----");
        eventCaptured = false;
        i = 0;

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
                qf.output("action=$0.action", "priceA=$1.symbol", "priceA=$1.price", "priceA=$2.symbol", "priceA=$2.price"),
                qf.inputStreams(qf.from(cseEventStream), qf.from(infoStock)),
                qf.pattern(
                        qf.every(
                                qf.condition("infoStock.action", EQUAL, "buy")  //0
                        ),
                        qf.condition("CSEStream.price*2", GREATERTHAN, "75"), //1
                        qf.condition("CSEStream.price", GREATERTHAN_EQUAL, "$1.price")  //2

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
                                        "StockQuote:=select action=$a1.action,  symbolA=$b1.symbol, priceA=$b1.price, symbolB=$b2.symbol, priceB=$b2.price \n" +
                                        "from CSEStream, infoStock\n" +
                                        "pattern [a1=infoStock.action==\"buy\",\n" +
                                        "b1= CSEStream.price>70,\n" +
                                        "b2= CSEStream.price>= $b1.price  ]\n" +
                                        "every($a1) -> $b1 -> $b2 ;");

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
        Assert.assertTrue(i == 3);
    }

    private void sendEvents(InputHandler inputHandlerCseStream,
                            InputHandler inputHandlerInfoStock) {
        SimpleStockQuotePatternEG eventGeneratorCseStream = new SimpleStockQuotePatternEG();
        InfoStockPatternEG eventGeneratorInfoStackPattern = new InfoStockPatternEG();
        //pumping the Events;
        for (int i = 0; i < 20; i++) {
            //  try {
            if (i % 5 == 2) {
                Event event = eventGeneratorInfoStackPattern.generateEvent();
//                System.out.println(event);
                inputHandlerInfoStock.sendEvent(event);
            } else {
                Event event = eventGeneratorCseStream.generateEvent();
//                System.out.println(event);
                inputHandlerCseStream.sendEvent(event);

            }
        }
    }

    private CallbackHandler assignCallback() {
        return new CallbackHandler("StockQuote") {
            public void callBack(Event event) {
                log.debug("       Event captured  " + event);
                if ((Integer) event.getNthAttribute(2) == 103 && (Integer) event.getNthAttribute(4) == 103) {
                    eventCaptured = true;
                    i++;
                } else if ((Integer) event.getNthAttribute(2) == 76 && (Integer) event.getNthAttribute(4) == 77) {
                    eventCaptured = true;
                    i++;
                } else if ((Integer) event.getNthAttribute(2) == 77 && (Integer) event.getNthAttribute(4) == 103) {
                    eventCaptured = true;
                    i++;
                } else {
                    Assert.fail("An unaccepted event " + event + " occurred");
                }
            }
        };
    }


}
