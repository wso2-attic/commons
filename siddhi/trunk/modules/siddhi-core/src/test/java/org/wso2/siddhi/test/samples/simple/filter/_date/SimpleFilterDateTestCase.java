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
package org.wso2.siddhi.test.samples.simple.filter._date;

import junit.framework.Assert;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.wso2.siddhi.api.QueryFactory;
import org.wso2.siddhi.api.eventstream.InputEventStream;
import org.wso2.siddhi.api.eventstream.query.Query;
import org.wso2.siddhi.api.exception.SiddhiPraserException;
import org.wso2.siddhi.core.exception.EventStreamNotFoundException;
import org.wso2.siddhi.core.exception.SiddhiException;
import org.wso2.siddhi.core.node.CallbackHandler;
import org.wso2.siddhi.core.node.InputHandler;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.event.Event;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.wso2.siddhi.api.condition.where.ConditionOperator.GREATERTHAN;


public class SimpleFilterDateTestCase {

    private static final Logger log = Logger.getLogger(SimpleFilterDateTestCase.class);
    private volatile boolean eventCaptured;
    private volatile int i;

    @Before
    public void info() {
        log.debug("------------------------Query being processed...------------------------------------------------");
        i = 0;
        eventCaptured = false;

    }

    @Test
    public void testAPI()
            throws SiddhiException, InterruptedException, EventStreamNotFoundException,
                   ParseException {
        //Instantiate SiddhiManager
        SiddhiManager siddhiManager = new SiddhiManager();

        QueryFactory qf = SiddhiManager.getQueryFactory();

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


        Query query = qf.createQuery(
                "StockQuote",
                qf.output("price=CSEStream.price", "date=CSEStream.date"),
                qf.from(cseEventStream),
                qf.condition("CSEStream.date", GREATERTHAN, newFormatter.format(date))
        );

        siddhiManager.addQuery(query);

        siddhiManager.addCallback(assignCallback());
        siddhiManager.update();

        InputHandler inputHandler = siddhiManager.getInputHandler("CSEStream");
        sendEvents(inputHandler);

        assertTest();
        siddhiManager.shutDownTask();
    }
    @Test
    public void testQuery()
            throws SiddhiException, InterruptedException, EventStreamNotFoundException,
                   ParseException, SiddhiPraserException {
        //Instantiate SiddhiManager
        SiddhiManager siddhiManager = new SiddhiManager();

        DateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
        DateFormat newFormatter = DateFormat.getDateInstance(DateFormat.LONG);
        Date date = null;
        date = (Date) formatter.parse("15-June-07");
        siddhiManager.addConfigurations("CSEStream:= symbol[string], price [int],aDate[date]; \n" +
                                        "" +
                                        "StockQuote:= select price,aDate " +
                                        "from CSEStream[win.time=2000] " +
                                        "where aDate >'" + newFormatter.format(date) + "' ;");

        siddhiManager.addCallback(assignCallback());
        siddhiManager.update();

        InputHandler inputHandler = siddhiManager.getInputHandler("CSEStream");
        sendEvents(inputHandler);

        assertTest();
        siddhiManager.shutDownTask();
    }

    private void assertTest() throws InterruptedException {
        Thread.sleep(1000);

        Assert.assertTrue(eventCaptured);
        Assert.assertTrue(i == 1);
    }

    private void sendEvents(InputHandler inputHandler) {
        SimpleStockQuoteDateEG eventGenerator = new SimpleStockQuoteDateEG(100);
        //printing the Events
        for (int i = 0; i < 5; i++) {
            Event event = eventGenerator.generateEvent();
            log.debug("Event received: " + event);
            inputHandler.sendEvent(event);
        }
    }

    private CallbackHandler assignCallback() {
        return new CallbackHandler("StockQuote") {
            public void callBack(Event event) {
                log.debug("       Event captured  " + event + " ");
                if ((Long) event.getNthAttribute(0) == 6000) {
                    eventCaptured = true;
                } else {
                    Assert.fail();
                }
                i++;
            }
        };
    }


}
