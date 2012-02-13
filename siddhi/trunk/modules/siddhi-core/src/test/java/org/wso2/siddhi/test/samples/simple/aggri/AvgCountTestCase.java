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

package org.wso2.siddhi.test.samples.simple.aggri;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.wso2.siddhi.api.QueryFactory;
import org.wso2.siddhi.api.eventstream.InputEventStream;
import org.wso2.siddhi.api.eventstream.query.Query;
import org.wso2.siddhi.api.eventstream.query.inputstream.property.WindowType;
import org.wso2.siddhi.api.exception.SiddhiPraserException;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.event.EventImpl;
import org.wso2.siddhi.core.exception.SiddhiException;
import org.wso2.siddhi.core.node.CallbackHandler;
import org.wso2.siddhi.core.node.InputHandler;

import static org.wso2.siddhi.api.condition.where.ConditionOperator.EQUAL;

public class AvgCountTestCase {

    private static final Logger log = Logger.getLogger(AvgCountTestCase.class);

    @Before
    public void info() {
        log.debug("-----Query processed: Testing avg and count on time window-----");
    }

    @Test
    public void testAPI() throws SiddhiException, SiddhiPraserException {
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
                qf.output("symbol=CSEStream.symbol", "avgPrice=avg(CSEStream.price)", "noPrice=count(CSEStream.price)"),
                qf.from(cseEventStream).setWindow(WindowType.TIME, 500),
                qf.condition("CSEStream.symbol", EQUAL, "IBM")
        );
        siddhiManager.addQuery(query);

        siddhiManager.addCallback(assignCallback());
        siddhiManager.update();

        sendEvents(inputHandler);
        siddhiManager.shutDownTask();

    }
 @Test
    public void testQuery() throws SiddhiException, SiddhiPraserException {
        //Instantiate SiddhiManager
        SiddhiManager siddhiManager = new SiddhiManager();

        siddhiManager.addQueries("CSEStream:= symbol[string], price [int]; \n" +
                                 "" +
                                 "StockQuote:= select symbol, avgPrice=avg(price), noPrice=count(price) " +
                                 "from CSEStream[win.time=500] " +
                                 "where symbol=='IBM' ;");

        siddhiManager.addCallback(assignCallback());

        siddhiManager.update();
        InputHandler inputHandler = siddhiManager.getInputHandler("CSEStream");

        sendEvents(inputHandler);
        siddhiManager.shutDownTask();

    }

    private void sendEvents(InputHandler inputHandler) {
        Integer[] price = new Integer[]{10, 20, 30, 40, 50, 10, 20, 30, 40, 50};
//          avg values                      10  15  20  25  30  35  40  45  50  0

        for (Integer aPrice : price) {
            Event event = new EventImpl("CSEStream", new Object[]{"IBM", aPrice});
            // log.debug("Sending Event: " + event);
            inputHandler.sendEvent(event);
            if (aPrice == 50) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
                        Assert.assertTrue(event.<Long>getNthAttribute(2) == 1);
                        break;
                    case 1:
                        Assert.assertTrue(event.<Integer>getNthAttribute(1) == 15);
                        Assert.assertTrue(event.<Long>getNthAttribute(2) == 2);
                        break;
                    case 2:
                        Assert.assertTrue(event.<Integer>getNthAttribute(1) == 20);
                        Assert.assertTrue(event.<Long>getNthAttribute(2) == 3);
                        break;
                    case 3:
                        Assert.assertTrue(event.<Integer>getNthAttribute(1) == 25);
                        Assert.assertTrue(event.<Long>getNthAttribute(2) == 4);
                        break;
                    case 4:
                        Assert.assertTrue(event.<Integer>getNthAttribute(1) == 30);
                        Assert.assertTrue(event.<Long>getNthAttribute(2) == 5);
                        break;
                    case 5:
                        Assert.assertTrue(event.<Integer>getNthAttribute(1) == 35);
                        Assert.assertTrue(event.<Long>getNthAttribute(2) == 4);
                        break;
                    case 6:
                        Assert.assertTrue(event.<Integer>getNthAttribute(1) == 40);
                        Assert.assertTrue(event.<Long>getNthAttribute(2) == 3);
                        break;
                    case 7:
                        Assert.assertTrue(event.<Integer>getNthAttribute(1) == 45);
                        Assert.assertTrue(event.<Long>getNthAttribute(2) == 2);
                        break;
                    case 8:
                        Assert.assertTrue(event.<Integer>getNthAttribute(1) == 50);
                        Assert.assertTrue(event.<Long>getNthAttribute(2) == 1);
                        break;
                    case 9:
                        Assert.assertTrue(event.<Integer>getNthAttribute(1) == 0);
                        Assert.assertTrue(event.<Long>getNthAttribute(2) == 0);
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
