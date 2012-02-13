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

public class UniqueTimeWindowedTestCase {

    //  private static int NUMBER_OF_EVENTS = 50;
    //event values {10, "A"}, {20,"B"} etc.
    Integer[] userIds = {1, 2, 3, 2, 2, 3, 8, 3, 4, 3};
    String[] names = {"A", "B", "C", "D", "E", "F", "X", "G", "H", "I"};
    Integer[] salary = {15, 25, 35, 45, 55, 65, 75, 85, 95, 105};
    //                 15/1   40/2    75/3    95/3   105/3    135/3 ||| 75/1   160/2  255/3  275/3
    //avg.U.TW.          15     20      25    31.7      35       45       75      80     85  91.67

    //A,B,C,D,E,F in the first 500 seconds. the rest in the second.
    //Out of B,D,E only E should be processed. out of C,F only F should be processed. G,I => I

    private static final Logger log = Logger.getLogger(UniqueTimeWindowedTestCase.class);
    private volatile boolean eventArrived;

    @Before
    public void info() {
        if (log.isInfoEnabled()) {
            log.debug("-----Query processed: Testing unique with in time-window with average calculation-----");
            eventArrived = false;
        }
    }

    @Test
    public void testCase() throws Exception {
        //Instantiate SiddhiManager
        SiddhiManager siddhiManager = new SiddhiManager();

        QueryFactory qf = SiddhiManager.getQueryFactory();

        InputEventStream cseEventStream = new InputEventStream(
                "UserRegistration",
                new String[]{"userId", "name", "salary", "org"},
                new Class[]{Integer.class, String.class, Integer.class, String.class}
        );

        siddhiManager.addInputEventStream(cseEventStream);

        Query query = qf.createQuery(
                "RegedUser",
                qf.output("userId=UserRegistration.userId", "name=UserRegistration.name",
                          "salary=UserRegistration.salary", "avgSalary=avg(UserRegistration.salary)"),
                qf.from(cseEventStream).setWindow(WindowType.TIME, 200).setStandardView(StandardView.UNIQUE, "UserRegistration.userId"),
                qf.condition("UserRegistration.org", EQUAL, "apache")
        );

        siddhiManager.addQuery(query);

        //condition is always true here. This is included since there must be
        // some condition for a Query.

        siddhiManager.addCallback(assignCallback());

        siddhiManager.update();

        InputHandler inputHandler = siddhiManager.getInputHandler("UserRegistration");
        sendEvents(inputHandler);

        assertTests();
        siddhiManager.shutDownTask();
    }

    @Test
    public void testQ() throws Exception {
        //Instantiate SiddhiManager
        SiddhiManager siddhiManager = new SiddhiManager();

        QueryFactory qf = SiddhiManager.getQueryFactory();
        siddhiManager.addQueries("UserRegistration:= userId[int], name[string],  salary[int], org [string]; \n" +
                                 "" +
                                 "RegedUser:= select userId , name ,salary,avgSalary=avg(UserRegistration.salary)" +
                                 "from UserRegistration [win.time=200,std.unique=userId] " +
                                 "where org == 'apache';");

        //condition is always true here. This is included since there must be
        // some condition for a Query.

        siddhiManager.addCallback(assignCallback());

        siddhiManager.update();

        InputHandler inputHandler = siddhiManager.getInputHandler("UserRegistration");
        sendEvents(inputHandler);

        assertTests();
        siddhiManager.shutDownTask();
    }

    private void assertTests() {
        try {
            synchronized (this) {
                Thread.sleep(2000);
            }
        } catch (Exception e) {

        }
        Assert.assertTrue(eventArrived);
    }

    private void sendEvents(InputHandler inputHandler) {
        //printing the Events
        for (int i = 0; i < userIds.length; i++) {

            Object[] values = new Object[]{userIds[i], names[i], salary[i], "apache"};

            Event event = new EventImpl("UserRegistration", values);

            if ("X".equals(names[i])) {
                try {
                    synchronized (this) {
                        this.wait(1100);
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
                int userSalary = siddhiManager.getEventStream("RegedUser").
                        getAttributePositionForName("salary");
                int userSalaryAvg = siddhiManager.getEventStream("RegedUser").
                        getAttributePositionForName("avgSalary");

                if (log.isInfoEnabled()) {
                    log.debug("       Event captured  " + event + " ");
                }
                if (event.isNew()) {
                    switch ((Integer) event.getNthAttribute(userSalary)) {
                        case 15:
                            Assert.assertEquals("test A", event.<Integer>getNthAttribute(userSalaryAvg), (Integer) 15);
                            break;
                        case 25:
                            Assert.assertEquals("test B", event.<Integer>getNthAttribute(userSalaryAvg), (Integer) 20);
                            break;
                        case 35:
                            Assert.assertEquals("test C", event.<Integer>getNthAttribute(userSalaryAvg), (Integer) 25);
                            break;
                        case 45:
                            Assert.assertEquals("test D", event.<Integer>getNthAttribute(userSalaryAvg), (Integer) 31);
                            break;
                        case 55:
                            Assert.assertEquals("test E", event.<Integer>getNthAttribute(userSalaryAvg), (Integer) 35);
                            break;
                        case 65:
                            Assert.assertEquals("test F", event.<Integer>getNthAttribute(userSalaryAvg), (Integer) 45);
                            break;
                        case 75:
                            Assert.assertEquals("test X", event.<Integer>getNthAttribute(userSalaryAvg), (Integer) 75);
                            break;
                        case 85:
                            Assert.assertEquals("test G", event.<Integer>getNthAttribute(userSalaryAvg), (Integer) 80);
                            break;
                        case 95:
                            Assert.assertEquals("test H", event.<Integer>getNthAttribute(userSalaryAvg), (Integer) 85);
                            break;
                        case 105:
                            Assert.assertEquals("test I", event.<Integer>getNthAttribute(userSalaryAvg), (Integer) 91);
                            break;
                        default:
                            Assert.fail();
                            break;
                    }
                }
            }
        };
    }


}
