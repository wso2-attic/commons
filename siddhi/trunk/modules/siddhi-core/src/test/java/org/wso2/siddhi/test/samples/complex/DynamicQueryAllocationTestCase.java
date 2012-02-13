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

package org.wso2.siddhi.test.samples.complex;

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
import org.wso2.siddhi.core.exception.InvalidQueryException;
import org.wso2.siddhi.core.exception.ProcessorInitializationException;
import org.wso2.siddhi.core.exception.SiddhiException;
import org.wso2.siddhi.core.node.CallbackHandler;
import org.wso2.siddhi.core.node.InputHandler;

import static org.wso2.siddhi.api.condition.where.ConditionOperator.EQUAL;


public class DynamicQueryAllocationTestCase {

    private static final Logger log = Logger.getLogger(DynamicQueryAllocationTestCase.class);
    private volatile int i = 0;
    private volatile boolean event1Captured = false;
    private volatile boolean event2Captured = false;

    public static void main(String[] args)
            throws InvalidQueryException, ProcessorInitializationException, InterruptedException,
                   SiddhiException {
        // Test code
        DynamicQueryAllocationTestCase simSimpleFilterTestCase = new DynamicQueryAllocationTestCase();
        simSimpleFilterTestCase.testCase();
    }

    @Before
    public void info() {
        log.debug("-----Query processed: Dynamically change one query to another-----");
    }

    @Test
    public void testCase() throws SiddhiException, ProcessorInitializationException,
                                  InvalidQueryException, InterruptedException {

        //Instantiate SiddhiManager
        SiddhiManager siddhiManager = new SiddhiManager();
        QueryFactory qf = siddhiManager.getQueryFactory();


        InputEventStream cseEventStream = new InputEventStream(
                "CSEStream",
                new String[]{"symbol", "price"},
                new Class[]{String.class, Integer.class}
        );

        InputHandler inputHandler = siddhiManager.addInputEventStream(cseEventStream);
        Query query = qf.createQuery(
                "StockQuote",
                qf.output("price=CSEStream.price"),
                qf.from(cseEventStream),
                qf.condition("CSEStream.symbol", EQUAL, "IBM")
        );

        siddhiManager.addQuery(query);

        siddhiManager.addCallback(new CallbackHandler("StockQuote") {
            public void callBack(Event event) {
                log.debug("       Event captured  " + event + " ");
                if (i < 3 && event.getNthAttribute(0) instanceof Integer) {
                    event1Captured = true;
                } else if (i >= 3 && event.getNthAttribute(0).equals("WSO2")) {
                    event2Captured = true;
                } else {
                    Assert.fail();
                }
                i++;
            }
        }
        );
        siddhiManager.update();

        log.debug("1st Query ends");

        inputHandler.sendEvent(new EventImpl("CSEStream", new Object[]{"IBM", 490}));
        inputHandler.sendEvent(new EventImpl("CSEStream", new Object[]{"IBM", 300}));
        inputHandler.sendEvent(new EventImpl("CSEStream", new Object[]{"IBM", 350}));
        siddhiManager.removeQuery(query);

        Query query1 = qf.createQuery(
                "StockQuote",
                qf.output("symbol=CSEStream.symbol", "price=CSEStream.price"),
                qf.from(cseEventStream),
                qf.condition("CSEStream.symbol", EQUAL, "WSO2"));

        siddhiManager.addQuery(query1);

        siddhiManager.update();

        log.debug("Init ends");
        inputHandler.sendEvent(new EventImpl("CSEStream", new Object[]{"WSO2", 490}));
        inputHandler.sendEvent(new EventImpl("CSEStream", new Object[]{"WSO2", 300}));
        inputHandler.sendEvent(new EventImpl("CSEStream", new Object[]{"WSO2", 350}));

        log.debug("all processes are over");

        Thread.sleep(1000);

        Assert.assertTrue(event1Captured);
        Assert.assertTrue(event2Captured);
        Assert.assertTrue(i == 6);

        siddhiManager.shutDownTask();
    }


}
