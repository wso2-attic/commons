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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.wso2.siddhi.api.QueryFactory;
import org.wso2.siddhi.api.eventstream.InputEventStream;
import org.wso2.siddhi.api.eventstream.query.Query;
import org.wso2.siddhi.api.eventstream.query.inputstream.property.StandardView;
import org.wso2.siddhi.api.eventstream.query.inputstream.property.WindowType;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.event.EventImpl;
import org.wso2.siddhi.core.node.CallbackHandler;
import org.wso2.siddhi.core.node.InputHandler;

import static org.wso2.siddhi.api.condition.where.ConditionOperator.EQUAL;

public class FirstUniqueTimeWindowedTestCase {

    //  private static int NUMBER_OF_EVENTS = 50;
    //event values {10, "A"}, {20,"B"} etc.
    Integer[] userIds = {10, 20, 30, 20, 80, 30, 40};
    String[] names = {"A", "B", "C", "D", "XX", "E", "F"};

    //A,B,C,D in the first 500 seconds. E, F in the second.
    //In this case, D shouldn't be added to the system since it's a duplicate in that timewindow.

    private static final Logger log = Logger.getLogger(FirstUniqueTimeWindowedTestCase.class);
    private volatile boolean eventArrived;

    @Before
    public void info() {
        if (log.isInfoEnabled()) {
            log.debug("-----Query processed: Testing first unique with in time-window-----");
            eventArrived = false;
        }
    }

    @Test
    public void testAPI() throws Exception {
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
                qf.from(cseEventStream).setWindow(WindowType.TIME, 200).
                        setStandardView(StandardView.FIRST_UNIQUE, "userId"),
                qf.condition("UserRegistration.org", EQUAL, "apache")
        );          //condition is always true here. This is included since there must be a condition for a Query.

        siddhiManager.addQuery(query);

        siddhiManager.addCallback(assignCallback());

        siddhiManager.update();

        InputHandler inputHandler = siddhiManager.getInputHandler("UserRegistration");
        sendEvents(inputHandler);

        assertTest();
        siddhiManager.shutDownTask();
    } @Test
    public void testQuery() throws Exception {
        //Instantiate SiddhiManager
        SiddhiManager siddhiManager = new SiddhiManager();

        siddhiManager.addConfigurations("UserRegistration:= userId[int], name[string], org [string]; \n" +
                                        "" +
                                        "RegedUser:= select * " +
                                        "from UserRegistration [std.firstUnique=userId,win.time=200] " +
                                        "where org == 'apache';");

        siddhiManager.addCallback(assignCallback());

        siddhiManager.update();

        InputHandler inputHandler = siddhiManager.getInputHandler("UserRegistration");
        sendEvents(inputHandler);

        assertTest();
        siddhiManager.shutDownTask();
    }

    private void assertTest() throws InterruptedException {
        synchronized (this) {
            Thread.sleep(2000);
        }
        Assert.assertTrue(eventArrived);
    }

    private void sendEvents(InputHandler inputHandler) {
        //printing the Events
        for (int i = 0; i < userIds.length; i++) {
            Object[] values = new Object[]{userIds[i], names[i], "apache"};

            Event event = new EventImpl("UserRegistration", values);

            if ("XX".equals(names[i])) {
                try {
                    synchronized (this) {
                        this.wait(1000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (log.isInfoEnabled()) {
                log.debug("Sending Event: " + event + ". time= " + System.currentTimeMillis());
            }
            inputHandler.sendEvent(event);
        }
    }

    private CallbackHandler assignCallback() {
        return new CallbackHandler("RegedUser") {
            public void callBack(Event event) {
                eventArrived = true;
                int userIdPosition = siddhiManager.getEventStream("RegedUser").
                        getAttributePositionForName("userId");
                int namePosition = siddhiManager.getEventStream("RegedUser").
                        getAttributePositionForName("name");

                if (log.isInfoEnabled()) {
                    log.debug("       Event captured  " + event + " ");
                }

                //D, E are duplicates
                switch ((Integer) event.getNthAttribute(userIdPosition)) {
                    case 10:
                        Assert.assertEquals("test A", event.<String>getNthAttribute(namePosition), "A");
                        break;
                    case 20:                //Should fail if "D" came
                        Assert.assertEquals("test B", event.<String>getNthAttribute(namePosition), "B");
                        break;
                    case 30:
                        Assert.assertTrue("test C/E",
                                          "C".equals(event.<String>getNthAttribute(namePosition)) ||
                                          "E".equals(event.<String>getNthAttribute(namePosition))
                        );
                        break;
                    case 40:
                        Assert.assertEquals("test F", event.<String>getNthAttribute(namePosition), "F");
                        break;
                    case 80:
                        Assert.assertEquals("test XX", event.<String>getNthAttribute(namePosition), "XX");
                        break;
                    default:
                        Assert.fail();
                }
            }
        };
    }


}
