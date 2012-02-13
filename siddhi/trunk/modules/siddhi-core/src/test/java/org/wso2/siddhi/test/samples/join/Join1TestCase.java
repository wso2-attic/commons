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

package org.wso2.siddhi.test.samples.join;

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
import org.wso2.siddhi.core.exception.EventStreamNotFoundException;
import org.wso2.siddhi.core.exception.SiddhiException;
import org.wso2.siddhi.core.node.CallbackHandler;
import org.wso2.siddhi.core.node.InputHandler;

import static org.wso2.siddhi.api.condition.where.ConditionOperator.EQUAL;


public class Join1TestCase {

    private static final Logger log = Logger.getLogger(Join1TestCase.class);
    private volatile int i = 0;
    private volatile boolean eventCaptured = false;

    public static void main(String[] args)
            throws EventStreamNotFoundException, InterruptedException, SiddhiException {
        // Test code
        Join1TestCase simSimpleFilterTestCase = new Join1TestCase();
        simSimpleFilterTestCase.testCase();
    }

    @Before
    public void info() {
        log.debug("-----Query processed: Testing join on time window-----");

    }

    @Test
    public void testCase() throws SiddhiException, InterruptedException,
                                  EventStreamNotFoundException, InterruptedException {

        //Instantiate SiddhiManager
        SiddhiManager siddhiManager = new SiddhiManager();
        QueryFactory qf = SiddhiManager.getQueryFactory();

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

        Query query = qf.createQuery(
                "bought",
                qf.output("id=confirmation.memberId", "payMethod=confirmation.payMethod", "rooms=request.noOfRooms"),
                qf.innerJoin(qf.from(reserveConfirmationEventStream).setWindow(WindowType.LENGTH, 50),
                             qf.from(requestEventStream).setWindow(WindowType.TIME, 1000)),
                qf.condition("confirmation.memberId", EQUAL, "request.memberId")
        );

        siddhiManager.addQuery(query);

        siddhiManager.addCallback(new CallbackHandler("bought") {
            public void callBack(Event event) {
                log.debug("       Event captured  " + event + " ");
                if ((Integer) event.getNthAttribute(0) == 101) {
                    eventCaptured = true;
                    i++;
                } else if ((Integer) event.getNthAttribute(0) == 102) {
                    eventCaptured = true;
                    i++;
                } else if ((Integer) event.getNthAttribute(0) == 103) {
                    eventCaptured = true;
                    i++;
                } else if ((Integer) event.getNthAttribute(0) == 104) {
                    eventCaptured = true;
                    i++;
                } else if ((Integer) event.getNthAttribute(0) == 105) {
                    eventCaptured = true;
                    i++;
                } else {
                    eventCaptured = false;
                }
            }
        }
        );

        siddhiManager.update();

        Thread.sleep(1000);

        InputHandler inputHandlerRequest = siddhiManager.getInputHandler("request");
        InputHandler inputHandlerConfirmation = siddhiManager.getInputHandler("confirmation");

        inputHandlerConfirmation.sendEvent(new EventImpl("confirmation", new Object[]{19, 98, "card"}));
        inputHandlerConfirmation.sendEvent(new EventImpl("confirmation", new Object[]{20, 78, "card"}));
        inputHandlerRequest.sendEvent(new EventImpl("request", new Object[]{21, 199, 2}));
        inputHandlerRequest.sendEvent(new EventImpl("request", new Object[]{22, 19, 2}));

        inputHandlerRequest.sendEvent(new EventImpl("request", new Object[]{23, 101, 2}));
        inputHandlerConfirmation.sendEvent(new EventImpl("confirmation", new Object[]{24, 101, "card"}));
        inputHandlerRequest.sendEvent(new EventImpl("request", new Object[]{25, 102, 4}));
        inputHandlerRequest.sendEvent(new EventImpl("request", new Object[]{26, 103, 1}));
        inputHandlerRequest.sendEvent(new EventImpl("request", new Object[]{27, 104, 3}));

        inputHandlerConfirmation.sendEvent(new EventImpl("confirmation", new Object[]{28, 104, "cash"}));
        inputHandlerConfirmation.sendEvent(new EventImpl("confirmation", new Object[]{29, 103, "card"}));
        inputHandlerRequest.sendEvent(new EventImpl("request", new Object[]{10, 105, 7}));
        inputHandlerConfirmation.sendEvent(new EventImpl("confirmation", new Object[]{31, 102, "cash"}));
        inputHandlerConfirmation.sendEvent(new EventImpl("confirmation", new Object[]{31, 105, "card"}));

        Thread.sleep(1000);

        Assert.assertTrue(eventCaptured);
        Assert.assertTrue(i == 5);
        siddhiManager.shutDownTask();
    }
}
