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
package org.wso2.siddhi.test.samples.simple._null;

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
import org.wso2.siddhi.core.event.generator.EventGenerator;
import org.wso2.siddhi.core.exception.SiddhiException;
import org.wso2.siddhi.core.node.CallbackHandler;
import org.wso2.siddhi.core.node.InputHandler;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.wso2.siddhi.api.condition.where.ConditionOperator.EQUAL;
import static org.wso2.siddhi.api.condition.where.ConditionOperator.GREATERTHAN;


public class SimpleFilterDateTestCase {

    private static final Logger log = Logger.getLogger(SimpleFilterDateTestCase.class);
    private volatile boolean eventArrived;

    @Before
    public void info() {
        log.debug("-----Query processed: Checking Null handling-----");
        eventArrived = false;
    }

    @Test
    public void testAPI() throws SiddhiException, ParseException {
        //Instantiate SiddhiManager
        SiddhiManager siddhiManager = new SiddhiManager();
        QueryFactory qf = siddhiManager.getQueryFactory();
        InputEventStream cseEventStream = new InputEventStream(
                "CSEStream",
                new String[]{"symbol", "price", "date"},
                new Class[]{String.class, Long.class, Date.class}
        );

        siddhiManager.addInputEventStream(cseEventStream);
        DateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
        DateFormat newFormatter = DateFormat.getDateInstance(DateFormat.LONG);
        Date date = null;
        date = (Date) formatter.parse("15-June-07");

        Query query1 = qf.createQuery(
                "StockQuoteDate",
                qf.output("price=CSEStream.price", "date=CSEStream.date"),
                qf.from(cseEventStream),
                qf.condition("CSEStream.date", GREATERTHAN, newFormatter.format(date))
        );

        Query query2 = qf.createQuery(
                "StockQuoteSymbol",
                qf.output("symbol=CSEStream.symbol", "date=CSEStream.date"),
                qf.from(cseEventStream),
                qf.condition("CSEStream.symbol", EQUAL, "IBM")
        );

        siddhiManager.addQuery(query1);
        siddhiManager.addQuery(query2);

        assignCallbacks(siddhiManager);
        siddhiManager.update();

        InputHandler inputHandler = siddhiManager.getInputHandler("CSEStream");
        sendEvents(inputHandler);

        siddhiManager.shutDownTask();
        Assert.assertTrue(eventArrived);
    }

    @Test
    public void testQuery() throws SiddhiException, ParseException, SiddhiPraserException {
        //Instantiate SiddhiManager

        DateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
        DateFormat newFormatter = DateFormat.getDateInstance(DateFormat.LONG);
        Date date = null;
        date = (Date) formatter.parse("15-June-07");

        SiddhiManager siddhiManager = new SiddhiManager();
        siddhiManager.addQueries("CSEStream:= symbol[string], price [long], eventDate[date];\n" +
                                 "" +
                                 "StockQuoteDate:=select price,eventDate " +
                                 "from CSEStream " +
                                 "where eventDate > '" + newFormatter.format(date) + "'; \n" +
                                 "" +
                                 "StockQuoteSymbol:= select symbol, eventDate " +
                                 "from CSEStream " +
                                 "where symbol=='IBM' ;");

        assignCallbacks(siddhiManager);
        siddhiManager.update();

        InputHandler inputHandler = siddhiManager.getInputHandler("CSEStream");
        sendEvents(inputHandler);

        siddhiManager.shutDownTask();
        Assert.assertTrue(eventArrived);
    }

    private void assignCallbacks(SiddhiManager siddhiManager) {
        siddhiManager.addCallback(new CallbackHandler("StockQuoteDate") {
            public void callBack(Event event) {
                log.debug("       Event captured  " + event);
                Assert.fail();
            }
        }
        );
        siddhiManager.addCallback(new CallbackHandler("StockQuoteSymbol") {
            public void callBack(Event event) {
                log.debug("       Event captured  " + event);
                eventArrived = true;
            }
        }
        );
    }

    private void sendEvents(InputHandler inputHandler) {
        EventGenerator eventGenerator =
                EventGenerator.DefaultFactory.create(inputHandler.getEventStream());
        String[] quoteNames = new String[]{"WSO2", "IBM"};
        //sending Events
        inputHandler.sendEvent(eventGenerator.createEvent(quoteNames[0], 100, null));
        inputHandler.sendEvent(eventGenerator.createEvent(quoteNames[1], 105, null));
        inputHandler.sendEvent(eventGenerator.createEvent(quoteNames[1], 50, null));
        inputHandler.sendEvent(eventGenerator.createEvent(quoteNames[0], 70, null));
    }


}
