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
package org.wso2.siddhi.test;

import org.apache.log4j.Logger;
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
import org.wso2.siddhi.query.api.stream.sequence.Sequence;
import org.wso2.siddhi.query.api.utils.SiddhiConstants;
import org.wso2.siddhi.query.compiler.exception.SiddhiPraserException;

public class SequenceTestCase {
    static final Logger log = Logger.getLogger(SequenceTestCase.class);
    int eventCount;

    @Before
    public void inti() {
        eventCount = 0;
    }

    @Test
    public void testQuery1() throws InterruptedException {
        log.info("test1 - OUT 1");

        SiddhiManager siddhiManager = new SiddhiManager();

        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("Stream1").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT));
        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("Stream2").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT));


        Query query = QueryFactory.createQuery();
        query.from(
                QueryFactory.sequenceStream(
                        Sequence.next(
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
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents,
                                Event[] faultEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents, faultEvents);
                Assert.assertEquals("WSO2", inEvents[0].getData(0));
                Assert.assertEquals("IBM", inEvents[0].getData(1));
                eventCount++;
            }
        });
        InputHandler stream1 = siddhiManager.getInputHandler("Stream1");
        InputHandler stream2 = siddhiManager.getInputHandler("Stream2");
        stream1.send(new Object[]{"WSO2", 55.6f, 100});
        Thread.sleep(500);
//        stream1.send(new Object[]{"GOOG", 57.6f, 100});
//        Thread.sleep(500);
        stream2.send(new Object[]{"IBM", 55.7f, 100});
        Thread.sleep(500);

        Assert.assertEquals(1, eventCount);
    }

    @Test
    public void testQuery2() throws InterruptedException {
        log.info("test2 - OUT 1");

        SiddhiManager siddhiManager = new SiddhiManager();

        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("Stream1").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT));
        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("Stream2").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT));


        Query query = QueryFactory.createQuery();
        query.from(
                QueryFactory.sequenceStream(
                        Sequence.next(
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
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents,
                                Event[] faultEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents, faultEvents);
                Assert.assertEquals("GOOG", inEvents[0].getData(0));
                Assert.assertEquals("IBM", inEvents[0].getData(1));
                eventCount++;
            }


        });
        InputHandler stream1 = siddhiManager.getInputHandler("Stream1");
        InputHandler stream2 = siddhiManager.getInputHandler("Stream2");
        stream1.send(new Object[]{"WSO2", 55.6f, 100});
        Thread.sleep(500);
        stream1.send(new Object[]{"GOOG", 57.6f, 100});
        Thread.sleep(500);
        stream2.send(new Object[]{"IBM", 65.7f, 100});
        Thread.sleep(500);
        Assert.assertEquals(1, eventCount);
    }

    @Test
    public void testQuery3() throws InterruptedException {
        log.info("test3  - OUT 2");

        SiddhiManager siddhiManager = new SiddhiManager();

        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("Stream1").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT));
        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("Stream2").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT));


        Query query = QueryFactory.createQuery();
        query.from(
                QueryFactory.sequenceStream(
                        Sequence.next(
                                QueryFactory.inputStream("e1", "Stream1").handler(
                                        Condition.compare(Expression.variable("price"),
                                                          Condition.Operator.GREATER_THAN,
                                                          Expression.value(20))),
                                Sequence.zeroOrMany(QueryFactory.inputStream("e2", "Stream2").handler(
                                        Condition.compare(Expression.variable("price"),
                                                          Condition.Operator.GREATER_THAN,
                                                          Expression.variable("e1", "price")))))));
        query.insertInto("OutStream");
        query.project(
                QueryFactory.outputProjector().
                        project("symbol1", Expression.variable("e1", "symbol")).
                        project("symbol2", Expression.variable("e2", 0, "symbol")).
                        project("symbol3", Expression.variable("e2", 1, "symbol"))

        );


        siddhiManager.addQuery(query);
        siddhiManager.addCallback("OutStream", new Callback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents,
                                Event[] faultEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents, faultEvents);
                if (eventCount == 0) {
                    Assert.assertArrayEquals(new Object[]{"WSO2", null, null}, inEvents[0].getData());
                } else if (eventCount == 1) {
                    Assert.assertArrayEquals(new  Object[]{"IBM", null, null}, inEvents[0].getData());
                } else {
                    Assert.fail();
                }
                eventCount++;
            }

        });
        InputHandler stream1 = siddhiManager.getInputHandler("Stream1");
//        InputHandler stream2 = siddhiManager.getInputHandler("Stream2");
        stream1.send(new Object[]{"WSO2", 55.6f, 100});
        Thread.sleep(500);
        stream1.send(new Object[]{"IBM", 55.7f, 100});
        Thread.sleep(500);
        Assert.assertEquals(2, eventCount);

    }

    @Test
    public void testQuery4() throws InterruptedException {
        log.info("test4  - OUT 2");

        SiddhiManager siddhiManager = new SiddhiManager();

        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("Stream1").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT));
        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("Stream2").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT));


        Query query = QueryFactory.createQuery();
        query.from(
                QueryFactory.sequenceStream(
                        Sequence.next(
                                Sequence.zeroOrMany(QueryFactory.inputStream("e1", "Stream2").handler(
                                        Condition.compare(Expression.variable("price"),
                                                          Condition.Operator.GREATER_THAN,
                                                          Expression.value(20)))),
                                QueryFactory.inputStream("e2", "Stream1").handler(
                                        Condition.compare(Expression.variable("price"),
                                                          Condition.Operator.GREATER_THAN,
                                                          Expression.variable("e1", 0, "price")))
                        )));
        query.insertInto("OutStream");
        query.project(
                QueryFactory.outputProjector().
                        project("price1", Expression.variable("e1", 0, "price")).
                        project("price2", Expression.variable("e1", 1, "price")).
                        project("price3", Expression.variable("e2", "price"))

        );


        siddhiManager.addQuery(query);
        siddhiManager.addCallback("OutStream", new Callback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents,
                                Event[] faultEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents, faultEvents);
                if (eventCount == 0) {
                    Assert.assertArrayEquals(new Object[]{55.6f, 55.7f, 57.6f}, inEvents[0].getData());
                } else if (eventCount == 1) {
                    Assert.assertArrayEquals(new  Object[]{55.7f, null, 57.6f}, inEvents[0].getData());
                } else {
                    Assert.fail();
                }
                eventCount++;
            }

        });
        InputHandler stream1 = siddhiManager.getInputHandler("Stream1");
        InputHandler stream2 = siddhiManager.getInputHandler("Stream2");
        stream1.send(new Object[]{"WSO2", 59.6f, 100});
        Thread.sleep(500);
        stream2.send(new Object[]{"WSO2", 55.6f, 100});
        Thread.sleep(500);
        stream2.send(new Object[]{"IBM", 55.7f, 100});
        Thread.sleep(500);
        stream1.send(new Object[]{"WSO2", 57.6f, 100});
        Thread.sleep(500);
        Assert.assertEquals("Number of success events ", 2, eventCount);

    }

    @Test
    public void testQuery5() throws InterruptedException {
        log.info("test5  - OUT 1");

        SiddhiManager siddhiManager = new SiddhiManager();

        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("Stream1").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT));
        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("Stream2").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT));


        Query query = QueryFactory.createQuery();
        query.from(
                QueryFactory.sequenceStream(
                        Sequence.next(
                                Sequence.zeroOrOne(QueryFactory.inputStream("e1", "Stream2").handler(
                                        Condition.compare(Expression.variable("price"),
                                                          Condition.Operator.GREATER_THAN,
                                                          Expression.value(20)))),
                                QueryFactory.inputStream("e2", "Stream1").handler(
                                        Condition.compare(Expression.variable("price"),
                                                          Condition.Operator.GREATER_THAN,
                                                          Expression.variable("e1", 0, "price")))
                        )));
        query.insertInto("OutStream");
        query.project(
                QueryFactory.outputProjector().
                        project("price1", Expression.variable("e1", 0, "price")).
//                        project("price2", Expression.variable("e1", 1, "price")).
        project("price3", Expression.variable("e2", "price"))

        );


        siddhiManager.addQuery(query);
        siddhiManager.addCallback("OutStream", new Callback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents,
                                Event[] faultEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents, faultEvents);
                if (eventCount == 0) {
                    Assert.assertArrayEquals(new Object[]{55.7f, 57.6f}, inEvents[0].getData());
                } else {
                    Assert.fail();
                }
                eventCount++;
            }

        });
        InputHandler stream1 = siddhiManager.getInputHandler("Stream1");
        InputHandler stream2 = siddhiManager.getInputHandler("Stream2");
        stream1.send(new Object[]{"WSO2", 59.6f, 100});
        Thread.sleep(500);
        stream2.send(new Object[]{"WSO2", 55.6f, 100});
        Thread.sleep(500);
        stream2.send(new Object[]{"IBM", 55.7f, 100});
        Thread.sleep(500);
        stream1.send(new Object[]{"WSO2", 57.6f, 100});
        Thread.sleep(500);
        Assert.assertEquals("Number of success events ", 1, eventCount);

    }

    @Test
    public void testQuery6() throws InterruptedException {
        log.info("test6  - OUT 2");

        SiddhiManager siddhiManager = new SiddhiManager();

        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("Stream1").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT));
        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("Stream2").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT));


        Query query = QueryFactory.createQuery();
        query.from(
                QueryFactory.sequenceStream(
                        Sequence.next(
                                QueryFactory.inputStream("e1", "Stream2").handler(
                                        Condition.compare(Expression.variable("price"),
                                                          Condition.Operator.GREATER_THAN,
                                                          Expression.value(20))),
                                Sequence.or(
                                        QueryFactory.inputStream("e2", "Stream2").handler(
                                                Condition.compare(Expression.variable("price"),
                                                                  Condition.Operator.GREATER_THAN,
                                                                  Expression.variable("e1", "price"))),
                                        QueryFactory.inputStream("e3", "Stream2").handler(
                                                Condition.compare(Expression.value("IBM"),
                                                                  Condition.Operator.EQUAL,
                                                                  Expression.variable("symbol")))))));
        query.insertInto("OutStream");
        query.project(
                QueryFactory.outputProjector().
                        project("price1", Expression.variable("e1", "price")).
                        project("price2", Expression.variable("e2", "price")).
                        project("price3", Expression.variable("e3", "price"))

        );


        siddhiManager.addQuery(query);
        siddhiManager.addCallback("OutStream", new Callback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents,
                                Event[] faultEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents, faultEvents);
                if (eventCount == 0) {
                    Assert.assertArrayEquals(new Object[]{55.6f, null, 55.7f}, inEvents[0].getData());
                } else if (eventCount == 1) {
                    Assert.assertArrayEquals(new  Object[]{55.7f, 57.6f, null}, inEvents[0].getData());
                } else {
                    Assert.fail();
                }
                eventCount++;
            }


        });
//        InputHandler stream1 = siddhiManager.getInputHandler("Stream1");
        InputHandler stream2 = siddhiManager.getInputHandler("Stream2");
        stream2.send(new Object[]{"WSO2", 59.6f, 100});
        Thread.sleep(500);
        stream2.send(new Object[]{"WSO2", 55.6f, 100});
        Thread.sleep(500);
        stream2.send(new Object[]{"IBM", 55.7f, 100});
        Thread.sleep(500);
        stream2.send(new Object[]{"WSO2", 57.6f, 100});
        Thread.sleep(500);
        Assert.assertEquals("Number of success events ", 2, eventCount);

    }

    @Test
    public void testQuery7() throws InterruptedException {
        log.info("test7  - OUT 2");

        SiddhiManager siddhiManager = new SiddhiManager();

        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("Stream1").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT));
        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("Stream2").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT));


        Query query = QueryFactory.createQuery();
        query.from(
                QueryFactory.sequenceStream(
                        Sequence.next(
                                QueryFactory.inputStream("e1", "Stream2").handler(
                                        Condition.compare(Expression.variable("price"),
                                                          Condition.Operator.GREATER_THAN,
                                                          Expression.value(20))),
                                Sequence.or(
                                        QueryFactory.inputStream("e2", "Stream2").handler(
                                                Condition.compare(Expression.variable("price"),
                                                                  Condition.Operator.GREATER_THAN,
                                                                  Expression.variable("e1", "price"))),
                                        QueryFactory.inputStream("e3", "Stream2").handler(
                                                Condition.compare(Expression.value("IBM"),
                                                                  Condition.Operator.EQUAL,
                                                                  Expression.variable("symbol")))))));
        query.insertInto("OutStream");
        query.project(
                QueryFactory.outputProjector().
                        project("price1", Expression.variable("e1", "price")).
                        project("price2", Expression.variable("e2", "price")).
                        project("price3", Expression.variable("e3", "price"))

        );


        siddhiManager.addQuery(query);
        siddhiManager.addCallback("OutStream", new Callback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents,
                                Event[] faultEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents, faultEvents);
                if (eventCount == 0) {
                    Assert.assertArrayEquals(new Object[]{55.6f, 57.6f, null}, inEvents[0].getData());
                } else if (eventCount == 1) {
                    Assert.assertArrayEquals(new Object[]{57.6f, null, 55.7f}, inEvents[0].getData());
                } else {
                    Assert.fail();
                }
                eventCount++;
            }


        });
//        InputHandler stream1 = siddhiManager.getInputHandler("Stream1");
        InputHandler stream2 = siddhiManager.getInputHandler("Stream2");
        stream2.send(new Object[]{"WSO2", 59.6f, 100});
        Thread.sleep(500);
        stream2.send(new Object[]{"WSO2", 55.6f, 100});
        Thread.sleep(500);
        stream2.send(new Object[]{"WSO2", 57.6f, 100});
        Thread.sleep(500);
        stream2.send(new Object[]{"IBM", 55.7f, 100});
        Thread.sleep(500);
        Assert.assertEquals("Number of success events ", 2, eventCount);

    }


    @Test
    public void testQuery8() throws InterruptedException {
        log.info("test8  - OUT 1");

        SiddhiManager siddhiManager = new SiddhiManager();

        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("Stream1").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT));
        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("Stream2").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT));


        Query query = QueryFactory.createQuery();
        query.from(
                QueryFactory.sequenceStream(
                        Sequence.next(
                                Sequence.oneOrMany(QueryFactory.inputStream("e1", "Stream2").handler(
                                        Condition.compare(Expression.variable("price"),
                                                          Condition.Operator.GREATER_THAN,
                                                          Expression.value(20)))),
                                QueryFactory.inputStream("e2", "Stream1").handler(
                                        Condition.compare(Expression.variable("price"),
                                                          Condition.Operator.GREATER_THAN,
                                                          Expression.variable("e1", 0, "price")))
                        )));
        query.insertInto("OutStream");
        query.project(
                QueryFactory.outputProjector().
                        project("price1", Expression.variable("e1", 0, "price")).
                        project("price2", Expression.variable("e1", 1, "price")).
                        project("price3", Expression.variable("e2", "price"))

        );


        siddhiManager.addQuery(query);
        siddhiManager.addCallback("OutStream", new Callback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents,
                                Event[] faultEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents, faultEvents);
                if (eventCount == 0) {
                    Assert.assertArrayEquals(new Object[]{55.6f, null, 57.6f}, inEvents[0].getData());
                } else {
                    Assert.fail();
                }
                eventCount++;
            }


        });
        InputHandler stream1 = siddhiManager.getInputHandler("Stream1");
        InputHandler stream2 = siddhiManager.getInputHandler("Stream2");
        stream1.send(new Object[]{"WSO2", 59.6f, 100});
        Thread.sleep(500);
        stream2.send(new Object[]{"WSO2", 55.6f, 100});
        Thread.sleep(500);
//        stream2.send(new Object[]{"IBM", 55.7f, 100});
//        Thread.sleep(500);
        stream1.send(new Object[]{"WSO2", 57.6f, 100});
        Thread.sleep(500);
        Assert.assertEquals("Number of success events ", 1, eventCount);

    }

    @Test
    public void testQuery9() throws InterruptedException {
        log.info("test9  - OUT 1");

        SiddhiManager siddhiManager = new SiddhiManager();

        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("Stream1").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT));
        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("Stream2").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT));


        Query query = QueryFactory.createQuery();
        query.from(
                QueryFactory.sequenceStream(
                        Sequence.next(
                                QueryFactory.inputStream("e1", "Stream1").handler(
                                        Condition.compare(Expression.variable("price"),
                                                          Condition.Operator.GREATER_THAN,
                                                          Expression.value(20))),
                                Sequence.next(
                                        Sequence.oneOrMany(
                                                QueryFactory.inputStream("e2", "Stream1").handler(
                                                        Condition.compare(Expression.variable("price"),
                                                                          Condition.Operator.GREATER_THAN,
                                                                          Expression.variable("e2", SiddhiConstants.PREV, "price")))),
                                        QueryFactory.inputStream("e3", "Stream1").handler(
                                                Condition.compare(Expression.variable("price"),
                                                                  Condition.Operator.LESS_THAN_EQUAL,
                                                                  Expression.variable("e3", SiddhiConstants.PREV, "price")))
                                ))));
        query.insertInto("OutStream");
        query.project(
                QueryFactory.outputProjector().
                        project("price1", Expression.variable("e1", "price")).
                        project("price2", Expression.variable("e2", 0, "price")).
                        project("price3", Expression.variable("e2", 1, "price")).
                        project("price4", Expression.variable("e3", "price"))

        );


        siddhiManager.addQuery(query);
        siddhiManager.addCallback("OutStream", new Callback() {

            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents,
                                Event[] faultEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents, faultEvents);
                if (eventCount == 0) {
                    Assert.assertArrayEquals(new Object[]{29.6f, 35.6f, 57.6f, 47.6f}, inEvents[0].getData());
                } else {
                    Assert.fail();
                }
                eventCount++;
            }


        });
        InputHandler stream1 = siddhiManager.getInputHandler("Stream1");
        //  InputHandler stream2 = siddhiManager.getInputHandler("Stream2");
        stream1.send(new Object[]{"WSO2", 29.6f, 100});
        Thread.sleep(500);
        stream1.send(new Object[]{"WSO2", 35.6f, 100});
        Thread.sleep(500);
//        stream2.send(new Object[]{"IBM", 55.7f, 100});
//        Thread.sleep(500);
        stream1.send(new Object[]{"WSO2", 57.6f, 100});
        Thread.sleep(500);
        stream1.send(new Object[]{"IBM", 47.6f, 100});
        Thread.sleep(500);
        Assert.assertEquals("Number of success events", 1, eventCount);

    }

    @Test
    public void testQuery10() throws InterruptedException, SiddhiPraserException {
        log.info("test10  - OUT 1");

        SiddhiManager siddhiManager = new SiddhiManager();
        siddhiManager.defineStream("define stream cseEventStream ( symbol string, price float, volume int )");
        siddhiManager.defineStream("define stream twiterStream ( symbol string, count int )");
        siddhiManager.addQuery("from e1 = cseEventStream [ price >= 50 and volume > 100 ] , e2 = twiterStream [count > 10 ] " +
                               "insert into StockQuote e1.price as price, e1.symbol as symbol, e2.count as count ;");
        siddhiManager.addCallback("StockQuote", new Callback() {

            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents,
                                Event[] faultEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents, faultEvents);
                if (eventCount == 0) {
                    Assert.assertArrayEquals(new Object[]{76.6f, "IBM", 20}, inEvents[0].getData());
                } else {
                    Assert.fail();
                }
                eventCount++;
            }
        });
        InputHandler cseStreamHandler = siddhiManager.getInputHandler("cseEventStream");
        InputHandler twitterStreamHandler = siddhiManager.getInputHandler("twiterStream");

        cseStreamHandler.send(new Object[]{"IBM", 75.6f, 105});
        cseStreamHandler.send(new Object[]{"GOOG", 51f, 101});
        cseStreamHandler.send(new Object[]{"IBM", 76.6f, 111});

//        Thread.sleep(500);
        twitterStreamHandler.send(new Object[]{"IBM", 20});
        cseStreamHandler.send(new Object[]{"WSO2", 45.6f, 100});

        Thread.sleep(1000);

        twitterStreamHandler.send(new Object[]{"GOOG", 20});

        Thread.sleep(500);
        Assert.assertEquals("Number of success events", 1, eventCount);

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
