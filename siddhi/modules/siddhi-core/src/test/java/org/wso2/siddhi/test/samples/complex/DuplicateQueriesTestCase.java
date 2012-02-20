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
import org.wso2.siddhi.core.eventstream.StreamReference;
import org.wso2.siddhi.core.exception.SiddhiException;
import org.wso2.siddhi.core.node.CallbackHandler;
import org.wso2.siddhi.core.node.InputHandler;

import static org.wso2.siddhi.api.condition.where.ConditionOperator.EQUAL;


public class DuplicateQueriesTestCase {

    private static final Logger log = Logger.getLogger(DuplicateQueriesTestCase.class);
    private volatile int i = 0;
    private volatile boolean eventCaptured = false;

    public static void main(String[] args) throws InterruptedException, SiddhiException {
        // Test code
        DuplicateQueriesTestCase testCase = new DuplicateQueriesTestCase();
        testCase.testCase();
    }

    @Before
    public void info() {
        log.debug("-----Query processed: Running duplicate queries-----");
    }

    @Test
    public void testCase() throws SiddhiException, InterruptedException {

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
                qf.output("price=CSEStream.price"),
                qf.from(cseEventStream),
                qf.condition("CSEStream.symbol", EQUAL, "IBM")
        );

        siddhiManager.addQuery(query);
        Query query1 = qf.createQuery(
                "StockQuote",
                qf.output("price=CSEStream.price"),
                qf.from(cseEventStream),
                qf.condition("CSEStream.symbol", EQUAL, "IBM")
        );

        StreamReference streamReference = siddhiManager.addQuery(query1);

        siddhiManager.addCallback(new CallbackHandler("StockQuote") {
            public void callBack(Event event) {
                log.debug("       Event captured  " + event + " ");
                if ((Integer) event.getNthAttribute(0) == 102) {
                    eventCaptured = true;
                } else if ((Integer) event.getNthAttribute(0) == 105) {
                    eventCaptured = true;
                } else if ((Integer) event.getNthAttribute(0) == 110) {
                    eventCaptured = true;
                } else {
                    Assert.fail();
                }
                i++;
            }
        }
        );
        siddhiManager.update();

        inputHandler.sendEvent(new EventImpl("CSEStream", new Object[]{"IBM", 102}));
        inputHandler.sendEvent(new EventImpl("CSEStream", new Object[]{"IBM", 105}));
        inputHandler.sendEvent(new EventImpl("CSEStream", new Object[]{"WSO2", 100}));
        inputHandler.sendEvent(new EventImpl("CSEStream", new Object[]{"IBM", 110}));
        inputHandler.sendEvent(new EventImpl("CSEStream", new Object[]{"WSO2", 109}));

        siddhiManager.removeStream(streamReference);

        Thread.sleep(500);

        Assert.assertTrue(eventCaptured);
        Assert.assertTrue(i == 6);

        siddhiManager.shutDownTask();
    }


}
