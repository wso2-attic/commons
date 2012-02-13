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

package org.wso2.siddhi.test.samples.simple.clause;

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
import org.wso2.siddhi.core.event.generator.EventGenerator;
import org.wso2.siddhi.core.exception.SiddhiException;
import org.wso2.siddhi.core.node.CallbackHandler;
import org.wso2.siddhi.core.node.InputHandler;

import static org.wso2.siddhi.api.condition.where.ConditionOperator.EQUAL;

public class AvgOverTimeWindowWithHavingClauseTestCase {

    private static final Logger log = Logger.getLogger(AvgOverTimeWindowWithHavingClauseTestCase.class);

    @Before
    public void info() {
        log.debug("-----Query processed: Testing Time-window with having and avg-----");
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
                qf.output("symbol=CSEStream.symbol", "avgPrice=avg(CSEStream.price)"),
                qf.from(cseEventStream).setWindow(WindowType.TIME,500),
                qf.condition("CSEStream.symbol", EQUAL, "IBM")
        );
        query.having(qf.condition("StockQuote.avgPrice", ConditionOperator.GREATERTHAN, "100"));

        siddhiManager.addQuery(query);

        siddhiManager.addCallback(assignCallback());
        siddhiManager.update();

        InputHandler inputHandler = siddhiManager.getInputHandler("CSEStream");
        sendEvents(inputHandler);

        siddhiManager.shutDownTask();

    }@Test
    public void testQuery() throws SiddhiException, InterruptedException, SiddhiPraserException {
        //Instantiate SiddhiManager
        SiddhiManager siddhiManager = new SiddhiManager();

        QueryFactory qf = SiddhiManager.getQueryFactory();

//        InputEventStream cseEventStream = new InputEventStream(
//                "CSEStream",
//                new String[]{"symbol", "price"},
//                new Class[]{String.class, Integer.class}
//        );
//        siddhiManager.addInputEventStream(cseEventStream);
//
//        Query query = qf.createQuery(
//                "StockQuote",
//                qf.output("symbol=CSEStream.symbol", "avgPrice=avg(CSEStream.price)"),
//                qf.from(cseEventStream).setWindow(WindowType.TIME,500),
//                qf.condition("CSEStream.symbol", EQUAL, "IBM")
//        );
//        query.having(qf.condition("StockQuote.avgPrice", ConditionOperator.GREATERTHAN, "100"));

        siddhiManager.addQueries("CSEStream:= symbol[string], price [int]; \n" +
                                 "" +
                                 "StockQuote:= select symbol, avgPrice=avg(price) " +
                                 "from CSEStream[win.time=500] " +
                                 "where symbol=='IBM' " +
                                 "having avgPrice > 100 ;");

        siddhiManager.addCallback(assignCallback());
        siddhiManager.update();

        InputHandler inputHandler = siddhiManager.getInputHandler("CSEStream");
        sendEvents(inputHandler);

        siddhiManager.shutDownTask();

    }

    private CallbackHandler assignCallback() {
        return new CallbackHandler("StockQuote") {
            int i = 0;

            public void callBack(Event event) {
                log.debug("       Event captured  " + event + " ");
                switch (i) {
                    case 0:
                        Assert.assertEquals(event.getNthAttribute(1), 110);
                        i++;
                        break;
                    case 1:
                        Assert.assertEquals(event.getNthAttribute(1), 101);
                        i++;
                        break;
                    case 2:
                        Assert.assertEquals(event.getNthAttribute(1), 110);
                        i++;
                        break;
                }
            }
        };
    }

    private void sendEvents(InputHandler inputHandler) throws InterruptedException {
        //init eventGenerator
        String[] quoteNames = new String[]{"WSO2", "IBM"};
        EventGenerator eventGenerator =
                EventGenerator.DefaultFactory.create(inputHandler.getEventStream());

        //sending Events
        inputHandler.sendEvent(eventGenerator.createEvent(quoteNames[1], 110));
        inputHandler.sendEvent(eventGenerator.createEvent(quoteNames[1], 70));
        inputHandler.sendEvent(eventGenerator.createEvent(quoteNames[1], 125));
        inputHandler.sendEvent(eventGenerator.createEvent(quoteNames[1], 70));
        sleep(1000);
        inputHandler.sendEvent(eventGenerator.createEvent(quoteNames[1], 90));
        inputHandler.sendEvent(eventGenerator.createEvent(quoteNames[1], 130));
        inputHandler.sendEvent(eventGenerator.createEvent(quoteNames[1], 30));
        sleep(1000);
    }

    private void sleep(int ms) throws InterruptedException {
        Thread.sleep(ms);
    }
}

