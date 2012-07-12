/*
*  Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/
package org.wso2.siddhi.test.pattern;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.stream.input.InputHandler;
import org.wso2.siddhi.core.stream.output.Callback;
import org.wso2.siddhi.core.util.EventPrinter;
import org.wso2.siddhi.query.api.query.Query;
import org.wso2.siddhi.query.api.QueryFactory;
import org.wso2.siddhi.query.api.condition.Condition;
import org.wso2.siddhi.query.api.expression.Expression;
import org.wso2.siddhi.query.api.definition.Attribute;
import org.wso2.siddhi.query.api.stream.pattern.Pattern;

public class PatternCountTestCase {

    int eventCount;

    @Before
    public void inti() {
        eventCount = 0;
    }

    @Test
    public void testQuery1() throws InterruptedException {
        System.out.println("test1 - OUT 1");

        SiddhiManager siddhiManager = new SiddhiManager();

        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("Stream1").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT));
        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("Stream2").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT));

        Query query = QueryFactory.createQuery();
        query.from(
                QueryFactory.patternStream(
                        Pattern.followedBy(
                                Pattern.count(
                                        QueryFactory.inputStream("e1", "Stream1").handler(
                                                Condition.compare(Expression.variable("price"),
                                                                  Condition.Operator.GREATER_THAN,
                                                                  Expression.value(20))), 2, 5),
                                QueryFactory.inputStream("e2", "Stream2").handler(
                                        Condition.compare(Expression.variable("price"),
                                                          Condition.Operator.GREATER_THAN,
                                                          Expression.value(20))))));

        query.insertInto("OutStream");
        query.project(
                QueryFactory.outputProjector().
                        project("price1.1", Expression.variable("e1", 0, "price")).
                        project("price1.2", Expression.variable("e1", 1, "price")).
                        project("price1.3", Expression.variable("e1", 2, "price")).
                        project("price1.4", Expression.variable("e1", 3, "price")).
                        project("price2", Expression.variable("e2", "price"))

        );


        siddhiManager.addQuery(query);
        siddhiManager.addCallback("OutStream", new Callback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents,
                                Event[] faultEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents, faultEvents);
                    Assert.assertArrayEquals(new Object[]{25.6f, 47.6f, 47.8f, null, 45.7f},inEvents[0].getData());
                eventCount++;
            }
        });
        InputHandler stream1 = siddhiManager.getInputHandler("Stream1");
        InputHandler stream2 = siddhiManager.getInputHandler("Stream2");
        stream1.send(new Object[]{"WSO2", 25.6f, 100});
        Thread.sleep(500);
        stream1.send(new Object[]{"GOOG", 47.6f, 100});
        Thread.sleep(500);
        stream1.send(new Object[]{"GOOG", 13.7f, 100});
        Thread.sleep(500);
        stream1.send(new Object[]{"GOOG", 47.8f, 100});
        Thread.sleep(500);
        stream2.send(new Object[]{"IBM", 45.7f, 100});
        Thread.sleep(500);
        stream2.send(new Object[]{"IBM", 55.7f, 100});
        Thread.sleep(500);

        Assert.assertEquals(1, eventCount);

    }

    @Test
    public void testQuery2() throws InterruptedException {
        System.out.println("test2 - OUT 1");

        SiddhiManager siddhiManager = new SiddhiManager();

        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("Stream1").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT));
        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("Stream2").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT));

        Query query = QueryFactory.createQuery();
        query.from(
                QueryFactory.patternStream(
                        Pattern.followedBy(
                                Pattern.count(
                                        QueryFactory.inputStream("e1", "Stream1").handler(
                                                Condition.compare(Expression.variable("price"),
                                                                  Condition.Operator.GREATER_THAN,
                                                                  Expression.value(20))), 2, 5),
                                QueryFactory.inputStream("e2", "Stream2").handler(
                                        Condition.compare(Expression.variable("price"),
                                                          Condition.Operator.GREATER_THAN,
                                                          Expression.value(20))))));

        query.insertInto("OutStream");
        query.project(
                QueryFactory.outputProjector().
                        project("price1.1", Expression.variable("e1", 0, "price")).
                        project("price1.2", Expression.variable("e1", 1, "price")).
                        project("price1.3", Expression.variable("e1", 2, "price")).
                        project("price1.4", Expression.variable("e1", 3, "price")).
                        project("price2", Expression.variable("e2", "price"))

        );


        siddhiManager.addQuery(query);
        siddhiManager.addCallback("OutStream", new Callback() {
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents,
                                Event[] faultEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents, faultEvents);
                Assert.assertArrayEquals(new Object[]{25.6f, 47.6f, null, null, 45.7f},inEvents[0].getData());
                eventCount++;
            }

        });
        InputHandler stream1 = siddhiManager.getInputHandler("Stream1");
        InputHandler stream2 = siddhiManager.getInputHandler("Stream2");
        stream1.send(new Object[]{"WSO2", 25.6f, 100});
        Thread.sleep(500);
        stream1.send(new Object[]{"GOOG", 47.6f, 100});
        Thread.sleep(500);
        stream1.send(new Object[]{"GOOG", 13.7f, 100});
        Thread.sleep(500);

        stream2.send(new Object[]{"IBM", 45.7f, 100});
        Thread.sleep(500);
        stream1.send(new Object[]{"GOOG", 47.8f, 100});
        Thread.sleep(500);
        stream2.send(new Object[]{"IBM", 55.7f, 100});
        Thread.sleep(500);

        Assert.assertEquals(1, eventCount);
    }

    @Test
    public void testQuery3() throws InterruptedException {
        System.out.println("test3 - OUT 1");

        SiddhiManager siddhiManager = new SiddhiManager();

        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("Stream1").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT));
        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("Stream2").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT));

        Query query = QueryFactory.createQuery();
        query.from(
                QueryFactory.patternStream(
                        Pattern.followedBy(
                                Pattern.count(
                                        QueryFactory.inputStream("e1", "Stream1").handler(
                                                Condition.compare(Expression.variable("price"),
                                                                  Condition.Operator.GREATER_THAN,
                                                                  Expression.value(20))), 2, 5),
                                QueryFactory.inputStream("e2", "Stream2").handler(
                                        Condition.compare(Expression.variable("price"),
                                                          Condition.Operator.GREATER_THAN,
                                                          Expression.value(20))))));

        query.insertInto("OutStream");
        query.project(
                QueryFactory.outputProjector().
                        project("price1.1", Expression.variable("e1", 0, "price")).
                        project("price1.2", Expression.variable("e1", 1, "price")).
                        project("price1.3", Expression.variable("e1", 2, "price")).
                        project("price1.4", Expression.variable("e1", 3, "price")).
                        project("price2", Expression.variable("e2", "price"))

        );


        siddhiManager.addQuery(query);
        siddhiManager.addCallback("OutStream", new Callback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents,
                                Event[] faultEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents, faultEvents);
                Assert.assertArrayEquals(new Object[]{25.6f, 47.8f, null, null, 55.7f},inEvents[0].getData());
                eventCount++;
            }

        });
        InputHandler stream1 = siddhiManager.getInputHandler("Stream1");
        InputHandler stream2 = siddhiManager.getInputHandler("Stream2");
        stream1.send(new Object[]{"WSO2", 25.6f, 100});
        Thread.sleep(500);
        stream2.send(new Object[]{"IBM", 45.7f, 100});
        Thread.sleep(500);
        stream1.send(new Object[]{"GOOG", 47.8f, 100});
        Thread.sleep(500);
        stream2.send(new Object[]{"IBM", 55.7f, 100});
        Thread.sleep(500);

        Assert.assertEquals(1, eventCount);
    }

    @Test
    public void testQuery4() throws InterruptedException {
        System.out.println("test4 - OUT 0");

        SiddhiManager siddhiManager = new SiddhiManager();

        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("Stream1").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT));
        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("Stream2").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT));

        Query query = QueryFactory.createQuery();
        query.from(
                QueryFactory.patternStream(
                        Pattern.followedBy(
                                Pattern.count(
                                        QueryFactory.inputStream("e1", "Stream1").handler(
                                                Condition.compare(Expression.variable("price"),
                                                                  Condition.Operator.GREATER_THAN,
                                                                  Expression.value(20))), 2, 5),
                                QueryFactory.inputStream("e2", "Stream2").handler(
                                        Condition.compare(Expression.variable("price"),
                                                          Condition.Operator.GREATER_THAN,
                                                          Expression.value(20))))));

        query.insertInto("OutStream");
        query.project(
                QueryFactory.outputProjector().
                        project("price1.1", Expression.variable("e1", 0, "price")).
                        project("price1.2", Expression.variable("e1", 1, "price")).
                        project("price1.3", Expression.variable("e1", 2, "price")).
                        project("price1.4", Expression.variable("e1", 3, "price")).
                        project("price2", Expression.variable("e2", "price"))

        );


        siddhiManager.addQuery(query);
        siddhiManager.addCallback("OutStream", new Callback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents,
                                Event[] faultEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents, faultEvents);
                Assert.fail();
                eventCount++;
            }

        });
        InputHandler stream1 = siddhiManager.getInputHandler("Stream1");
        InputHandler stream2 = siddhiManager.getInputHandler("Stream2");
        stream1.send(new Object[]{"WSO2", 25.6f, 100});
        Thread.sleep(500);
        stream2.send(new Object[]{"IBM", 45.7f, 100});
        Thread.sleep(500);

        Assert.assertEquals(0, eventCount);
    }

    @Test
    public void testQuery5() throws InterruptedException {
        System.out.println("test5 - OUT 1");

        SiddhiManager siddhiManager = new SiddhiManager();

        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("Stream1").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT));
        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("Stream2").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT));

        Query query = QueryFactory.createQuery();
        query.from(
                QueryFactory.patternStream(
                        Pattern.followedBy(
                                Pattern.count(
                                        QueryFactory.inputStream("e1", "Stream1").handler(
                                                Condition.compare(Expression.variable("price"),
                                                                  Condition.Operator.GREATER_THAN,
                                                                  Expression.value(20))), 2, 5),
                                QueryFactory.inputStream("e2", "Stream2").handler(
                                        Condition.compare(Expression.variable("price"),
                                                          Condition.Operator.GREATER_THAN,
                                                          Expression.value(20))))));

        query.insertInto("OutStream");
        query.project(
                QueryFactory.outputProjector().
                        project("price1.1", Expression.variable("e1", 0, "price")).
                        project("price1.2", Expression.variable("e1", 1, "price")).
                        project("price1.3", Expression.variable("e1", 2, "price")).
                        project("price1.4", Expression.variable("e1", 3, "price")).
                        project("price2", Expression.variable("e2", "price"))

        );


        siddhiManager.addQuery(query);
        siddhiManager.addCallback("OutStream", new Callback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents,
                                Event[] faultEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents, faultEvents);
                Assert.assertArrayEquals(new Object[]{25.6f, 47.6f, 23.7f, 24.7f, 45.7f},inEvents[0].getData());
                eventCount++;
            }
        });
        InputHandler stream1 = siddhiManager.getInputHandler("Stream1");
        InputHandler stream2 = siddhiManager.getInputHandler("Stream2");
        stream1.send(new Object[]{"WSO2", 25.6f, 100});
        Thread.sleep(500);
        stream1.send(new Object[]{"GOOG", 47.6f, 100});
        Thread.sleep(500);
        stream1.send(new Object[]{"GOOG", 23.7f, 100});
        Thread.sleep(500);
        stream1.send(new Object[]{"GOOG", 24.7f, 100});
        Thread.sleep(500);
        stream1.send(new Object[]{"GOOG", 25.7f, 100});
        Thread.sleep(500);
        stream1.send(new Object[]{"WSO2", 27.6f, 100});
        Thread.sleep(500);
        stream2.send(new Object[]{"IBM", 45.7f, 100});
        Thread.sleep(500);
        stream1.send(new Object[]{"GOOG", 47.8f, 100});
        Thread.sleep(500);
        stream2.send(new Object[]{"IBM", 55.7f, 100});
        Thread.sleep(500);

        Assert.assertEquals(1, eventCount);
    }


    @Test
    public void testQuery6() throws InterruptedException {
        System.out.println("test6 - OUT 1");

        SiddhiManager siddhiManager = new SiddhiManager();

        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("Stream1").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT));
        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("Stream2").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT));

        Query query = QueryFactory.createQuery();
        query.from(
                QueryFactory.patternStream(
                        Pattern.followedBy(
                                Pattern.count(
                                        QueryFactory.inputStream("e1", "Stream1").handler(
                                                Condition.compare(Expression.variable("price"),
                                                                  Condition.Operator.GREATER_THAN,
                                                                  Expression.value(20))), 2, 5),
                                QueryFactory.inputStream("e2", "Stream2").handler(
                                        Condition.compare(Expression.variable("price"),
                                                          Condition.Operator.GREATER_THAN,
                                                          Expression.variable("e1", 1, "price"))))));

        query.insertInto("OutStream");
        query.project(
                QueryFactory.outputProjector().
                        project("price1", Expression.variable("e1", 0, "price")).
                        project("price2.1", Expression.variable("e1", 1, "price")).
                        project("price2.2", Expression.variable("e2", "price"))

        );


        siddhiManager.addQuery(query);
        siddhiManager.addCallback("OutStream", new Callback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents,
                                Event[] faultEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents, faultEvents);
                Assert.assertArrayEquals(new Object[]{25.6f, 47.6f, 55.7f},inEvents[0].getData());
                eventCount++;
            }
        });
        InputHandler stream1 = siddhiManager.getInputHandler("Stream1");
        InputHandler stream2 = siddhiManager.getInputHandler("Stream2");
        stream1.send(new Object[]{"WSO2", 25.6f, 100});
        Thread.sleep(500);
        stream1.send(new Object[]{"GOOG", 47.6f, 100});
        Thread.sleep(500);
        stream2.send(new Object[]{"IBM", 45.7f, 100});
        Thread.sleep(500);
        stream2.send(new Object[]{"IBM", 55.7f, 100});
        Thread.sleep(500);
        Assert.assertEquals(1, eventCount);

    }

    @Test
    public void testQuery7() throws InterruptedException {
        System.out.println("test7 - OUT 1");

        SiddhiManager siddhiManager = new SiddhiManager();

        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("Stream1").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT));
        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("Stream2").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT));

        Query query = QueryFactory.createQuery();
        query.from(
                QueryFactory.patternStream(
                        Pattern.followedBy(
                                Pattern.count(
                                        QueryFactory.inputStream("e1", "Stream1").handler(
                                                Condition.compare(Expression.variable("price"),
                                                                  Condition.Operator.GREATER_THAN,
                                                                  Expression.value(20))), 0, 5),
                                QueryFactory.inputStream("e2", "Stream2").handler(
                                        Condition.compare(Expression.variable("price"),
                                                          Condition.Operator.GREATER_THAN,
                                                          Expression.value(20))))));
//                                                          Expression.variable("e1", 1, "price"))))));

        query.insertInto("OutStream");
        query.project(
                QueryFactory.outputProjector().
                        project("price1", Expression.variable("e1", 0, "price")).
                        project("price2.1", Expression.variable("e1", 1, "price")).
                        project("price2.2", Expression.variable("e2", "price"))

        );


        siddhiManager.addQuery(query);
        siddhiManager.addCallback("OutStream", new Callback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents,
                                Event[] faultEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents, faultEvents);
                Assert.assertArrayEquals(new Object[]{null, null, 45.7f},inEvents[0].getData());
                eventCount++;
            }

        });
        InputHandler stream1 = siddhiManager.getInputHandler("Stream1");
        InputHandler stream2 = siddhiManager.getInputHandler("Stream2");
        stream2.send(new Object[]{"IBM", 45.7f, 100});
        Thread.sleep(500);
        Assert.assertEquals(1, eventCount);

    }

    @Test
    public void testQuery8() throws InterruptedException {
        System.out.println("test8 - OUT 1");

        SiddhiManager siddhiManager = new SiddhiManager();

        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("Stream1").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT));
        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("Stream2").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT));

        Query query = QueryFactory.createQuery();
        query.from(
                QueryFactory.patternStream(
                        Pattern.followedBy(
                                Pattern.count(
                                        QueryFactory.inputStream("e1", "Stream1").handler(
                                                Condition.compare(Expression.variable("price"),
                                                                  Condition.Operator.GREATER_THAN,
                                                                  Expression.value(20))), 0, 5),
                                QueryFactory.inputStream("e2", "Stream2").handler(
                                        Condition.compare(Expression.variable("price"),
                                                          Condition.Operator.GREATER_THAN,
//                                                          Expression.value(20))))));
                                                          Expression.variable("e1", 0, "price"))))));

        query.insertInto("OutStream");
        query.project(
                QueryFactory.outputProjector().
                        project("price1", Expression.variable("e1", 0, "price")).
                        project("price2.1", Expression.variable("e1", 1, "price")).
                        project("price2.2", Expression.variable("e2", "price"))

        );


        siddhiManager.addQuery(query);
        siddhiManager.addCallback("OutStream", new Callback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents,
                                Event[] faultEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents, faultEvents);
                Assert.assertArrayEquals(new Object[]{25.6f, null, 45.7f},inEvents[0].getData());
                eventCount++;
            }
        });
        InputHandler stream1 = siddhiManager.getInputHandler("Stream1");
        InputHandler stream2 = siddhiManager.getInputHandler("Stream2");
        stream1.send(new Object[]{"WSO2", 25.6f, 100});
        Thread.sleep(500);
        stream1.send(new Object[]{"GOOG", 7.6f, 100});
        Thread.sleep(500);
        stream2.send(new Object[]{"IBM", 45.7f, 100});
        Thread.sleep(500);
        Assert.assertEquals(1, eventCount);

    }


}
