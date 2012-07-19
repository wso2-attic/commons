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

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.stream.input.InputHandler;
import org.wso2.siddhi.core.stream.output.Callback;
import org.wso2.siddhi.core.util.EventPrinter;
import org.wso2.siddhi.query.api.QueryFactory;
import org.wso2.siddhi.query.api.condition.Condition;
import org.wso2.siddhi.query.api.definition.Attribute;
import org.wso2.siddhi.query.api.expression.Expression;
import org.wso2.siddhi.query.api.query.Query;
import org.wso2.siddhi.query.api.stream.pattern.Pattern;

public class PatternEveryTestCase {
    static final Logger log = Logger.getLogger(PatternEveryTestCase.class);
    private int eventCount;
    private boolean eventArrived;

    @Before
    public void init() {
        eventCount = 0;
        eventArrived = false;
    }

    @Test
    public void testQuery1() throws InterruptedException {
        log.info("test1 - OUT 1");

        SiddhiManager siddhiManager = new SiddhiManager();

        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("Stream1").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT));
        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("Stream2").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT));

        Query query = QueryFactory.createQuery();
        query.from(
                QueryFactory.patternStream(
                        Pattern.followedBy(
                                QueryFactory.inputStream("e1", "Stream1").handler(
                                        Condition.compare(Expression.variable("price"),
                                                          Condition.Operator.GREATER_THAN,
                                                          Expression.value(20))),
                                QueryFactory.inputStream("e2", "Stream2").handler(
                                        Condition.compare(Expression.variable("price"),
                                                          Condition.Operator.GREATER_THAN,
                                                          Expression.variable("e1", "price"))))));

        query.insertInto("OutStream");
        query.project(
                QueryFactory.outputProjector().
                        project("symbol1", Expression.variable("e1", "symbol")).
                        project("symbol2", Expression.variable("e2", "symbol"))

        );


        siddhiManager.addQuery(query);
        siddhiManager.addCallback("OutStream", new Callback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                Assert.assertArrayEquals(new Object[]{"WSO2", "IBM"},inEvents[0].getData());
                eventCount++;
                eventArrived = true;
            }
        });
        InputHandler stream1 = siddhiManager.getInputHandler("Stream1");
        InputHandler stream2 = siddhiManager.getInputHandler("Stream2");
        stream1.send(new Object[]{"WSO2", 55.6f, 100});
        Thread.sleep(500);
        stream1.send(new Object[]{"GOOG", 55.6f, 100});
        Thread.sleep(500);
        stream2.send(new Object[]{"IBM", 55.7f, 100});
        Thread.sleep(500);

        Assert.assertEquals("Number of success events", 1, eventCount);
        Assert.assertEquals("Event arrived", true, eventArrived);
//        stream1.send(new Object[]{"WSO2", 55.6f, 100});
//        Thread.sleep(500);
//
//        stream2.send(new Object[]{"IBM", 55.7f, 100});
//        Thread.sleep(500);

    }

    @Test
    public void testQuery2() throws InterruptedException {
        log.info("test2 - OUT 2");

        SiddhiManager siddhiManager = new SiddhiManager();

        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("Stream1").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT));
        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("Stream2").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT));

        Query query = QueryFactory.createQuery();
        query.from(
                QueryFactory.patternStream(
                        Pattern.followedBy(
                                Pattern.every(
                                        QueryFactory.inputStream("e1", "Stream1").handler(
                                                Condition.compare(Expression.variable("price"),
                                                                  Condition.Operator.GREATER_THAN,
                                                                  Expression.value(20)))),
                                QueryFactory.inputStream("e2", "Stream2").handler(
                                        Condition.compare(Expression.variable("price"),
                                                          Condition.Operator.GREATER_THAN,
                                                          Expression.variable("e1", "price"))))));

        query.insertInto("OutStream");
        query.project(
                QueryFactory.outputProjector().
                        project("symbol1", Expression.variable("e1", "symbol")).
                        project("symbol2", Expression.variable("e2", "symbol"))

        );


        siddhiManager.addQuery(query);
        siddhiManager.addCallback("OutStream", new Callback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                if (eventCount == 0) {
                    Assert.assertArrayEquals(new Object[]{"WSO2", "IBM"},inEvents[0].getData());
                } else if (eventCount == 1) {
                    Assert.assertArrayEquals(new Object[]{"GOOG", "IBM"},inEvents[0].getData());
                } else {
                    Assert.fail();
                }
                eventCount++;
                eventArrived = true;
            }
        });
        InputHandler stream1 = siddhiManager.getInputHandler("Stream1");
        InputHandler stream2 = siddhiManager.getInputHandler("Stream2");
        stream1.send(new Object[]{"WSO2", 55.6f, 100});
        Thread.sleep(500);
        stream1.send(new Object[]{"GOOG", 54f, 100});
        Thread.sleep(500);
        stream2.send(new Object[]{"IBM", 55.7f, 100});
        Thread.sleep(500);
        Assert.assertEquals("Number of success events", 2, eventCount);
        Assert.assertEquals("Event arrived", true, eventArrived);

    }

    @Test
    public void testQuery3() throws InterruptedException {
        log.info("test3 - OUT 1");

        SiddhiManager siddhiManager = new SiddhiManager();

        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("Stream1").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT));
        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("Stream2").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT));

        Query query = QueryFactory.createQuery();
        query.from(
                QueryFactory.patternStream(
                        Pattern.followedBy(
                                Pattern.every(
                                        Pattern.followedBy(QueryFactory.inputStream("e1", "Stream1").handler(
                                                Condition.compare(Expression.variable("price"),
                                                                  Condition.Operator.GREATER_THAN,
                                                                  Expression.value(20))), QueryFactory.inputStream("e3", "Stream1").handler(
                                                Condition.compare(Expression.variable("price"),
                                                                  Condition.Operator.GREATER_THAN,
                                                                  Expression.value(20))))),
                                QueryFactory.inputStream("e2", "Stream2").handler(
                                        Condition.compare(Expression.variable("price"),
                                                          Condition.Operator.GREATER_THAN,
                                                          Expression.variable("e1", "price"))))));

        query.insertInto("OutStream");
        query.project(
                QueryFactory.outputProjector().
                        project("price1", Expression.variable("e1", "price")).
                        project("price3", Expression.variable("e3", "price")).
                        project("price2", Expression.variable("e2", "price"))

        );


        siddhiManager.addQuery(query);
        siddhiManager.addCallback("OutStream", new Callback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                Assert.assertArrayEquals(new Object[]{55.6f, 54f, 57.7f},inEvents[0].getData());
                eventCount++;
                eventArrived = true;
            }
        });
        InputHandler stream1 = siddhiManager.getInputHandler("Stream1");
        InputHandler stream2 = siddhiManager.getInputHandler("Stream2");
        stream1.send(new Object[]{"WSO2", 55.6f, 100});
        Thread.sleep(500);
        stream1.send(new Object[]{"GOOG", 54f, 100});
        Thread.sleep(500);
        stream2.send(new Object[]{"IBM", 57.7f, 100});
        Thread.sleep(500);
        Assert.assertEquals("Number of success events", 1, eventCount);
        Assert.assertEquals("Event arrived", true, eventArrived);

    }

    @Test
    public void testQuery4() throws InterruptedException {
        log.info("test4 - OUT 2");

        SiddhiManager siddhiManager = new SiddhiManager();

        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("Stream1").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT));
        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("Stream2").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT));

        Query query = QueryFactory.createQuery();
        query.from(
                QueryFactory.patternStream(
                        Pattern.followedBy(
                                Pattern.every(
                                        Pattern.followedBy(
                                                QueryFactory.inputStream("e1", "Stream1").handler(
                                                        Condition.compare(Expression.variable("price"),
                                                                          Condition.Operator.GREATER_THAN,
                                                                          Expression.value(20))),
                                                QueryFactory.inputStream("e3", "Stream1").handler(
                                                        Condition.compare(Expression.variable("price"),
                                                                          Condition.Operator.GREATER_THAN,
                                                                          Expression.value(20))))),
                                QueryFactory.inputStream("e2", "Stream2").handler(
                                        Condition.compare(Expression.variable("price"),
                                                          Condition.Operator.GREATER_THAN,
                                                          Expression.variable("e1", "price"))))));

        query.insertInto("OutStream");
        query.project(
                QueryFactory.outputProjector().
                        project("price1", Expression.variable("e1", "price")).
                        project("price3", Expression.variable("e3", "price")).
                        project("price2", Expression.variable("e2", "price"))

        );


        siddhiManager.addQuery(query);
        siddhiManager.addCallback("OutStream", new Callback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                if (eventCount == 0) {
                    Assert.assertArrayEquals(new Object[]{55.6f, 54f, 57.7f},inEvents[0].getData());
                } else if (eventCount == 1) {
                    Assert.assertArrayEquals(new Object[]{53.6f, 53f, 57.7f},inEvents[0].getData());
                } else {
                    Assert.fail();
                }
                eventCount++;
                eventArrived = true;
            }
        });
        InputHandler stream1 = siddhiManager.getInputHandler("Stream1");
        InputHandler stream2 = siddhiManager.getInputHandler("Stream2");
        stream1.send(new Object[]{"WSO2", 55.6f, 100});
        Thread.sleep(500);
        stream1.send(new Object[]{"GOOG", 54f, 100});
        Thread.sleep(500);
        stream1.send(new Object[]{"WSO2", 53.6f, 100});
        Thread.sleep(500);
        stream1.send(new Object[]{"GOOG", 53f, 100});
        Thread.sleep(500);
        stream2.send(new Object[]{"IBM", 57.7f, 100});
        Thread.sleep(500);
        Assert.assertEquals("Number of success events", 2, eventCount);
        Assert.assertEquals("Event arrived", true, eventArrived);

    }

    @Test
    public void testQuery5() throws InterruptedException {
        log.info("test5  - OUT 2");

        SiddhiManager siddhiManager = new SiddhiManager();

        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("Stream1").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT));
        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("Stream2").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT));

        Query query = QueryFactory.createQuery();
        query.from(
                QueryFactory.patternStream(
                        Pattern.followedBy(QueryFactory.inputStream("e4", "Stream1").handler(
                                Condition.compare(Expression.variable("symbol"),
                                                  Condition.Operator.EQUAL,
                                                  Expression.value("MSFT"))),
                                           Pattern.followedBy(
                                                   Pattern.every(
                                                           Pattern.followedBy(QueryFactory.inputStream("e1", "Stream1").handler(
                                                                   Condition.compare(Expression.variable("price"),
                                                                                     Condition.Operator.GREATER_THAN,
                                                                                     Expression.value(20))), QueryFactory.inputStream("e3", "Stream1").handler(
                                                                   Condition.compare(Expression.variable("price"),
                                                                                     Condition.Operator.GREATER_THAN,
                                                                                     Expression.value(20))))),
                                                   QueryFactory.inputStream("e2", "Stream2").handler(
                                                           Condition.compare(Expression.variable("price"),
                                                                             Condition.Operator.GREATER_THAN,
                                                                             Expression.variable("e1", "price")))))));

        query.insertInto("OutStream");
        query.project(
                QueryFactory.outputProjector().
                        project("price1", Expression.variable("e1", "price")).
                        project("price3", Expression.variable("e3", "price")).
                        project("price2", Expression.variable("e2", "price"))

        );


        siddhiManager.addQuery(query);
        siddhiManager.addCallback("OutStream", new Callback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                if (eventCount == 0) {
                    Assert.assertArrayEquals(new Object[]{55.6f, 54f, 57.7f},inEvents[0].getData());
                } else if (eventCount == 1) {
                    Assert.assertArrayEquals(new Object[]{53.6f, 53f, 57.7f},inEvents[0].getData());
                } else {
                    Assert.fail();
                }
                eventCount++;
                eventArrived = true;
            }
        });
        InputHandler stream1 = siddhiManager.getInputHandler("Stream1");
        InputHandler stream2 = siddhiManager.getInputHandler("Stream2");
        stream1.send(new Object[]{"MSFT", 55.6f, 100});
        Thread.sleep(500);
        stream1.send(new Object[]{"WSO2", 55.6f, 100});
        Thread.sleep(500);
        stream1.send(new Object[]{"GOOG", 54f, 100});
        Thread.sleep(500);
        stream1.send(new Object[]{"WSO2", 53.6f, 100});
        Thread.sleep(500);
        stream1.send(new Object[]{"GOOG", 53f, 100});
        Thread.sleep(500);
        stream2.send(new Object[]{"IBM", 57.7f, 100});
        Thread.sleep(500);
        Assert.assertEquals("Number of success events", 2, eventCount);
        Assert.assertEquals("Event arrived", true, eventArrived);

    }

    @Test
    public void testQuery6() throws InterruptedException {
        log.info("test6  - OUT 2");

        SiddhiManager siddhiManager = new SiddhiManager();

        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("Stream1").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT));
        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("Stream2").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT));

        Query query = QueryFactory.createQuery();
        query.from(
                QueryFactory.patternStream(
                        Pattern.every(
                                Pattern.followedBy(
                                        QueryFactory.inputStream("e1", "Stream1").handler(
                                                Condition.compare(Expression.variable("price"),
                                                                  Condition.Operator.GREATER_THAN,
                                                                  Expression.value(20))),
                                        QueryFactory.inputStream("e3", "Stream1").handler(
                                                Condition.compare(Expression.variable("price"),
                                                                  Condition.Operator.GREATER_THAN,
                                                                  Expression.value(20)))))));

        query.insertInto("OutStream");
        query.project(
                QueryFactory.outputProjector().
                        project("price1", Expression.variable("e1", "price")).
                        project("price3", Expression.variable("e3", "price"))

        );


        siddhiManager.addQuery(query);
        siddhiManager.addCallback("OutStream", new Callback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                if (eventCount == 0) {
                    Assert.assertArrayEquals(new Object[]{55.6f, 57.6f},inEvents[0].getData());
                } else if (eventCount == 1) {
                    Assert.assertArrayEquals(new Object[]{54f, 53.6f},inEvents[0].getData());
                } else {
                    Assert.fail();
                }
                eventCount++;
                eventArrived = true;
            }
        });
        InputHandler stream1 = siddhiManager.getInputHandler("Stream1");
        InputHandler stream2 = siddhiManager.getInputHandler("Stream2");
        stream1.send(new Object[]{"MSFT", 55.6f, 100});
        Thread.sleep(500);
        stream1.send(new Object[]{"WSO2", 57.6f, 100});
        Thread.sleep(500);
        stream1.send(new Object[]{"GOOG", 54f, 100});
        Thread.sleep(500);
        stream1.send(new Object[]{"WSO2", 53.6f, 100});
        Thread.sleep(500);
        Assert.assertEquals("Number of success events", 2, eventCount);
        Assert.assertEquals("Event arrived", true, eventArrived);


    }

    @Test
    public void testQuery7() throws InterruptedException {
        log.info("test7  - OUT 2");

        SiddhiManager siddhiManager = new SiddhiManager();

        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("Stream1").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT));
        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("Stream2").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT));

        Query query = QueryFactory.createQuery();
        query.from(
                QueryFactory.patternStream(
                        Pattern.every(
                                QueryFactory.inputStream("e1", "Stream1").handler(
                                        Condition.compare(Expression.variable("price"),
                                                          Condition.Operator.GREATER_THAN,
                                                          Expression.value(20f)))
                        )));

        query.insertInto("OutStream");
        query.project(
                QueryFactory.outputProjector().
                        project("price1", Expression.variable("e1", "price"))

        );


        siddhiManager.addQuery(query);
        siddhiManager.addCallback("OutStream", new Callback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                if (eventCount == 0) {
                    Assert.assertArrayEquals(new Object[]{55.6f},inEvents[0].getData());
                } else if (eventCount == 1) {
                    Assert.assertArrayEquals(new Object[]{57.6f},inEvents[0].getData());
                } else {
                    Assert.fail();
                }
                eventCount++;
                eventArrived = true;
            }
        });
        InputHandler stream1 = siddhiManager.getInputHandler("Stream1");
        InputHandler stream2 = siddhiManager.getInputHandler("Stream2");
        stream1.send(new Object[]{"MSFT", 55.6f, 100});
        Thread.sleep(500);
        stream1.send(new Object[]{"WSO2", 57.6f, 100});
        Thread.sleep(500);
        Assert.assertEquals("Number of success events", 2, eventCount);
        Assert.assertEquals("Event arrived", true, eventArrived);

    }
//    @Test
//    public void testQuery1() throws InterruptedException {
//        log.info("test1");
//
//        SiddhiManager siddhiManager = new SiddhiManager();
//
//        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("cseEventStream").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT));
//        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("twitterStream").attribute("user", Attribute.Type.STRING).attribute("tweets", Attribute.Type.STRING).attribute("symbol", Attribute.Type.STRING));
//
//        Query query = QueryFactory.createQuery();
//        query.from(
//                QueryFactory.joinStream(
//                        QueryFactory.inputStream("cseEventStream").
//                                handler(Handler.Type.WIN, "time", 500),
//                        JoinStream.Type.JOIN,
//                        QueryFactory.inputStream("twitterStream").
//                                handler(Handler.Type.WIN, "time", 500).
//                                handler(Handler.Type.FILTER, null,
//                                        Condition.and(Condition.compare(Expression.add(Expression.value(7), Expression.value(9.5)),
//                                                                        Condition.Operator.GREATER_THAN,
//                                                                        Expression.variable("cseEventStream", "price")),
//                                                      Condition.compare(Expression.value(100),
//                                                                        Condition.Operator.GREATER_THAN_EQUAL,
//                                                                        Expression.variable("cseEventStream", "volume")
//                                                      )
//                                        )
//                                ),
//                        Condition.compare(Expression.variable("cseEventStream", "price"),
//                                          Condition.Operator.EQUAL,
//                                          Expression.variable("cseEventStream", "price"))
//
//
//                )
//        );
//        query.insertInto("StockQuote");
//        query.project(
//                QueryFactory.outputProjector().
//                        project("symbol", Expression.variable("cseEventStream", "symbol")).
//                        project("", null, Expression.variable("cseEventStream", "symbol")).
//                        groupBy("cseEventStream", "symbol").
//                        having(Condition.compare(Expression.add(Expression.value(7), Expression.value(9.5)),
//                                                 Condition.Operator.GREATER_THAN,
//                                                 Expression.variable(null, "price"))
//                        )
//        );
//
//
//        siddhiManager.addQuery(query);
//        siddhiManager.addCallback("StockQuote", new Callback() {
//            public void receive(long timeStamp, Object[] newEventData, Object[] removeEventData,
//                                Object[] faultEventData) {
//                System.out.println(toString(timeStamp, newEventData, removeEventData, faultEventData));
//            }
//
//        });
//        InputHandler inputHandler = siddhiManager.getInputHandler("cseEventStream");
//        inputHandler.send(new Object[]{"WSO2", 55.6f, 100});
//        inputHandler.send(new Object[]{"IBM", 75.6f, 100});
//        Thread.sleep(500);
//        inputHandler.send(new Object[]{"WSO2", 57.6f, 100});
//        Thread.sleep(7000);
//
//    }

}
