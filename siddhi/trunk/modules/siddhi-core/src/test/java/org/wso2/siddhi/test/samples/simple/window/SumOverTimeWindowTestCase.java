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


package org.wso2.siddhi.test.samples.simple.window;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.wso2.siddhi.api.QueryFactory;
import org.wso2.siddhi.api.condition.where.ConditionOperator;
import org.wso2.siddhi.api.eventstream.InputEventStream;
import org.wso2.siddhi.api.eventstream.query.Query;
import org.wso2.siddhi.api.eventstream.query.inputstream.property.WindowType;
import org.wso2.siddhi.api.exception.SiddhiPraserException;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.event.EventImpl;
import org.wso2.siddhi.core.exception.InvalidQueryException;
import org.wso2.siddhi.core.exception.ProcessorInitializationException;
import org.wso2.siddhi.core.exception.SiddhiException;
import org.wso2.siddhi.core.node.CallbackHandler;
import org.wso2.siddhi.core.node.InputHandler;

public class SumOverTimeWindowTestCase {

    private static final Logger log = Logger.getLogger(SumOverTimeWindowTestCase.class);

    @Before
    public void info() {
        log.debug("-----Query processed: Testing sum over time window-----");
    }

    @Test
    public void testAPI() throws ProcessorInitializationException, InvalidQueryException,
                                 SiddhiException, InterruptedException {
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
                qf.output("symbol=CSEStream.symbol", "totalPrice=sum(CSEStream.price)"),
                qf.from(cseEventStream).setWindow(WindowType.TIME, 500),
                qf.condition("CSEStream.symbol", ConditionOperator.EQUAL, "IBM")
        );
        siddhiManager.addQuery(query);

        siddhiManager.addCallback(assignCallback());

        siddhiManager.update();

        sendEvents(inputHandler);
        siddhiManager.shutDownTask();

    }

    @Test
    public void testQuery() throws ProcessorInitializationException, InvalidQueryException,
                                   SiddhiException, InterruptedException, SiddhiPraserException {
        //Instantiate SiddhiManager
        SiddhiManager siddhiManager = new SiddhiManager();
        QueryFactory qf = SiddhiManager.getQueryFactory();

        siddhiManager.addQueries("CSEStream:= symbol[string], price [int]; \n" +
                                 "" +
                                 "StockQuote:= select symbol, totalPrice=sum(CSEStream.price) " +
                                 "from CSEStream  [win.time=500] " +
                                 "where symbol == 'IBM';");

        siddhiManager.addCallback(assignCallback());
        siddhiManager.update();

        sendEvents(siddhiManager.getInputHandler("CSEStream"));
        siddhiManager.shutDownTask();
    }

    private void sendEvents(InputHandler inputHandler) throws InterruptedException {
        Integer[] price = new Integer[]{10, 20, 30, 40, 50, 10, 20, 30, 40, 50};
        for (int i = 0; i < 10; i++) {
            Event event = new EventImpl("CSEStream", new Object[]{"IBM", price[i]});
            if (log.isInfoEnabled()) {
                log.debug("Sending Event: " + event);
            }
            inputHandler.sendEvent(event);
            if (i == 4) {
                Thread.sleep(1000);
            }
        }
    }

    private CallbackHandler assignCallback() {
        return new CallbackHandler("StockQuote") {
            int i = 0;

            public void callBack(Event event) {
                log.debug("       Event captured  " + event + " ");
                switch (i) {
                    case 0:
                        Assert.assertTrue(event.<Integer>getNthAttribute(1) == 10);
                        break;
                    case 1:
                        Assert.assertTrue(event.<Integer>getNthAttribute(1) == 30);
                        break;
                    case 2:
                        Assert.assertTrue(event.<Integer>getNthAttribute(1) == 60);
                        break;
                    case 3:
                        Assert.assertTrue(event.<Integer>getNthAttribute(1) == 100);
                        break;
                    case 4:
                        Assert.assertTrue(event.<Integer>getNthAttribute(1) == 150);
                        break;
                    case 5:
                        Assert.assertTrue(event.<Integer>getNthAttribute(1) == 140);
                        break;
                    case 6:
                        Assert.assertTrue(event.<Integer>getNthAttribute(1) == 120);
                        break;
                    case 7:
                        Assert.assertTrue(event.<Integer>getNthAttribute(1) == 90);
                        break;
                    case 8:
                        Assert.assertTrue(event.<Integer>getNthAttribute(1) == 50);
                        break;
                    case 9:
                        Assert.assertTrue(event.<Integer>getNthAttribute(1) == 0);
                        i = -1;
                        break;
                    default:
                        Assert.fail();
                }
                i++;
            }
        };
    }

}
