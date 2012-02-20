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

package org.wso2.siddhi.test.samples.simple.std;

import org.apache.log4j.Logger;
import org.junit.*;
import org.junit.Assert;
import org.junit.Test;
import org.wso2.siddhi.api.QueryFactory;
import org.wso2.siddhi.api.eventstream.EventStream;
import org.wso2.siddhi.api.eventstream.InputEventStream;
import org.wso2.siddhi.api.eventstream.query.Query;
import org.wso2.siddhi.api.eventstream.query.inputstream.property.StandardView;
import org.wso2.siddhi.api.exception.SiddhiPraserException;
import org.wso2.siddhi.core.exception.SiddhiException;
import org.wso2.siddhi.core.node.CallbackHandler;
import org.wso2.siddhi.core.node.InputHandler;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.event.EventImpl;
import org.wso2.siddhi.core.exception.ProcessorInitializationException;
import org.wso2.siddhi.core.exception.InvalidQueryException;
import org.wso2.siddhi.core.exception.EventStreamNotFoundException;

import static org.wso2.siddhi.api.condition.where.ConditionOperator.EQUAL;

public class FirstUniqueTestCase {

    //  private static int NUMBER_OF_EVENTS = 50;
    //event values {10, "A"}, {20,"B"} etc.
    Integer[] userIds = {10, 20, 30, 20, 30, 40};
    String[] names = {"A", "B", "C", "D", "E", "F"};

    private static final Logger log = Logger.getLogger(FirstUniqueTestCase.class);
    private volatile boolean eventArrived;

    @Before
    public void info() {
        eventArrived = false;
        log.debug("-----Query processed: Testing basic first unique-----");
    }

    @Test
    public void testAPI() throws SiddhiException, EventStreamNotFoundException,
                                 ProcessorInitializationException, InvalidQueryException,
                                 InterruptedException {
        //Instantiate SiddhiManager
        SiddhiManager siddhiManager = new SiddhiManager();
        QueryFactory qf = SiddhiManager.getQueryFactory();

        InputEventStream cseEventStream = new InputEventStream(
                "UserRegistration",
                new String[]{"userId", "name", "org"},
                new Class[]{Integer.class, String.class, String.class}
        );

        siddhiManager.addInputEventStream(cseEventStream);

        Query query = qf.createQuery(
                "RegedUser",
                qf.output("*"),
                qf.from(cseEventStream).setStandardView(StandardView.FIRST_UNIQUE, "userId"),
                qf.condition("UserRegistration.org", EQUAL, "apache")
        );

        siddhiManager.addQuery(query);

        siddhiManager.addCallback(assignCallback(cseEventStream));

        siddhiManager.update();

        InputHandler inputHandler = siddhiManager.getInputHandler("UserRegistration");
        sendEvents(inputHandler);
        Thread.sleep(1000);
        assertTest();
        siddhiManager.shutDownTask();
    }

    @Test
    public void testQuery() throws SiddhiException, EventStreamNotFoundException,
                                   ProcessorInitializationException, InvalidQueryException,
                                   SiddhiPraserException, InterruptedException {
        //Instantiate SiddhiManager
        SiddhiManager siddhiManager = new SiddhiManager();

        siddhiManager.addConfigurations("UserRegistration:= userId[int], name[string], org [string]; \n" +
                                        "" +
                                        "RegedUser:= select * " +
                                        "from UserRegistration [std.firstUnique=userId] " +
                                        "where org == 'apache';");


        InputHandler inputHandler = siddhiManager.getInputHandler("UserRegistration");
        siddhiManager.addCallback(assignCallback(inputHandler.getEventStream()));

        siddhiManager.update();

        sendEvents(inputHandler);
        Thread.sleep(1000);
        assertTest();
        siddhiManager.shutDownTask();
    }

    private void assertTest() {
        Assert.assertTrue(eventArrived);
    }

    private void sendEvents(InputHandler inputHandler) {
        //printing the Events
        for (int i = 0; i < userIds.length; i++) {

            Object[] values = new Object[]{userIds[i], names[i], "apache"};
            Event event = new EventImpl("UserRegistration", values);
            log.debug("Sending Event: " + event);
            inputHandler.sendEvent(event);
        }
    }

    private CallbackHandler assignCallback(final EventStream eventStream) {
        return new CallbackHandler("RegedUser") {
            public void callBack(Event event) {
                eventArrived = true;
                int userIdPosition = eventStream.getAttributePositionForName("userId");
                int namePosition = eventStream.getAttributePositionForName("name");

                //D, E are duplicates
                switch ((Integer) event.getNthAttribute(userIdPosition)) {
                    case 10:
                        Assert.assertEquals("test A", event.<String>getNthAttribute(namePosition), "A");
                        break;
                    case 20:                //Should fail if "D" came
                        Assert.assertEquals("test B", event.<String>getNthAttribute(namePosition), "B");
                        break;
                    case 30:                //Should fail if "E" came
                        Assert.assertEquals("test C", event.<String>getNthAttribute(namePosition), "C");
                        break;
                    case 40:
                        Assert.assertEquals("test F", event.<String>getNthAttribute(namePosition), "F");
                        break;
                    default:
                        Assert.fail();
                }

                log.debug("       Event captured  " + event + " ");
            }
        };
    }

}
