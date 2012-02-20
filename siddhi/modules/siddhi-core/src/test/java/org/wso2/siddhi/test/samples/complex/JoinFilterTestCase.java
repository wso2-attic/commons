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
import org.wso2.siddhi.api.eventstream.query.inputstream.property.WindowType;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.event.EventImpl;
import org.wso2.siddhi.core.exception.InvalidQueryException;
import org.wso2.siddhi.core.exception.ProcessorInitializationException;
import org.wso2.siddhi.core.exception.SiddhiException;
import org.wso2.siddhi.core.node.CallbackHandler;
import org.wso2.siddhi.core.node.InputHandler;

import static org.wso2.siddhi.api.condition.where.ConditionOperator.EQUAL;
import static org.wso2.siddhi.api.condition.where.ConditionOperator.GREATERTHAN_EQUAL;


public class JoinFilterTestCase {

    private static final Logger log = Logger.getLogger(JoinFilterTestCase.class);
    private volatile int i = 0;
    private volatile boolean eventCaptured = false;

    public static void main(String[] args)
            throws InvalidQueryException, ProcessorInitializationException, InterruptedException,
                   SiddhiException {
        // Test code
        JoinFilterTestCase simSimpleFilterTestCase = new JoinFilterTestCase();
        simSimpleFilterTestCase.testCase();
    }

    @Before
    public void info() {
        log.debug("-----Query processed: Running Join and filtering its output-----");
    }

    @Test
    public void testCase() throws SiddhiException, ProcessorInitializationException,
                                  InvalidQueryException, InterruptedException {

        //Instantiate SiddhiManager
        SiddhiManager siddhiManager = new SiddhiManager();
        QueryFactory qf = siddhiManager.getQueryFactory();

        InputEventStream reserveConfirmationEventStream =
                new InputEventStream("confirmation",
                                             new String[]{"time", "memberId", "payMethod"},
                                             new Class[]{Long.class, Integer.class, String.class}
                );


        InputEventStream requestEventStream =
                new InputEventStream("request",
                                             new String[]{"time", "memberId", "noOfRooms"},
                                             new Class[]{Long.class, Integer.class, Integer.class}
                );


        siddhiManager.addInputEventStream(reserveConfirmationEventStream);
        siddhiManager.addInputEventStream(requestEventStream);

        Query query1 = qf.createQuery(
                "bought",
                qf.output("method=confirmation.payMethod", "rooms=request.noOfRooms"),
                qf.innerJoin(qf.from(reserveConfirmationEventStream).setWindow(WindowType.LENGTH, 50),
                             qf.from(requestEventStream).setWindow(WindowType.LENGTH, 50)),
                qf.condition("confirmation.memberId", EQUAL, "request.memberId")
        );

        Query query2 = qf.createQuery(
                "bought2Rooms",
                qf.output("Method=bought.method", "Rooms=bought.rooms"),
                qf.from(query1),
                qf.condition("bought.rooms", GREATERTHAN_EQUAL, "2")
        );

        siddhiManager.addQuery(query2);
        siddhiManager.addQuery(query1);

        siddhiManager.addCallback(new CallbackHandler("bought2Rooms") {
            public void callBack(Event event) {
                log.debug("       Event captured  " + event + " ");
                if ((Integer) event.getNthAttribute(1) == 2 & i == 0) {
                    eventCaptured = true;
                }
                if ((Integer) event.getNthAttribute(1) == 3 & i == 1) {
                    eventCaptured = true;
                }
                if ((Integer) event.getNthAttribute(1) == 7 & i == 2) {
                    eventCaptured = true;
                }
                i++;
            }
        }
        );

        siddhiManager.update();


        InputHandler inputHandlerRequest = siddhiManager.getInputHandler("request");
        InputHandler inputHandlerConfirmation = siddhiManager.getInputHandler("confirmation");
        inputHandlerRequest.sendEvent(new EventImpl("request", new Object[]{23, 101, 2}));
        inputHandlerConfirmation.sendEvent(new EventImpl("confirmation", new Object[]{24, 101, "card"}));
        inputHandlerRequest.sendEvent(new EventImpl("request", new Object[]{25, 102, 1}));
        inputHandlerRequest.sendEvent(new EventImpl("request", new Object[]{26, 103, 1}));
        inputHandlerRequest.sendEvent(new EventImpl("request", new Object[]{27, 104, 3}));

        inputHandlerConfirmation.sendEvent(new EventImpl("confirmation", new Object[]{28, 104, "cash"}));
        inputHandlerConfirmation.sendEvent(new EventImpl("confirmation", new Object[]{29, 103, "card"}));
        inputHandlerRequest.sendEvent(new EventImpl("request", new Object[]{10, 105, 7}));
        inputHandlerConfirmation.sendEvent(new EventImpl("confirmation", new Object[]{31, 102, "cash"}));
        inputHandlerConfirmation.sendEvent(new EventImpl("confirmation", new Object[]{31, 105, "card"}));
        Thread.sleep(1000);

        Assert.assertTrue(eventCaptured);
        Assert.assertTrue(i == 3);

        siddhiManager.shutDownTask();
    }


}
