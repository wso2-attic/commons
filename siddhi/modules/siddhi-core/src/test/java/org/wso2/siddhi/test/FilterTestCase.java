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

import junit.framework.Assert;
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
import org.wso2.siddhi.query.api.stream.handler.Handler;

public class FilterTestCase {
    private int count;
    private boolean eventArrived;

    @Before
    public void init() {
        count = 0;
        eventArrived = false;
    }

    @Test
    public void testFilterQuery1() throws InterruptedException {


        System.out.println("Filter test1");
        SiddhiManager siddhiManager = new SiddhiManager();

        InputHandler inputHandler = siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("cseEventStream").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT));

        Query query = QueryFactory.createQuery();
        query.from(QueryFactory.inputStream("cseEventStream"));
        query.insertInto("StockQuote");
        query.project(
                QueryFactory.outputProjector().
                        project("symbol", Expression.variable("symbol")).
                        project("price", Expression.variable("price")).
                        project("volume", Expression.variable("volume"))
        );

        siddhiManager.addQuery(query);

        siddhiManager.addCallback("StockQuote", new Callback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents,
                                Event[] faultEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents, faultEvents);
                Assert.assertTrue("IBM".equals(inEvents[0].getData(0)) || "WSO2".equals(inEvents[0].getData(0)));
                count++;
            }
        });
//        InputHandler inputHandler = siddhiManager.getInputHandler("cseEventStream");
        inputHandler.send(new Object[]{"IBM", 75.6f, 100});
        inputHandler.send(new Object[]{"WSO2", 75.6f, 100});
        Thread.sleep(500);
        Assert.assertEquals(2, count);

    }

    @Test
    public void testFilterQuery2() throws InterruptedException {
        System.out.println("Filter test2");
        SiddhiManager siddhiManager = new SiddhiManager();

        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("cseEventStream").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT));

        Query query = QueryFactory.createQuery();
        query.from(QueryFactory.inputStream("cseEventStream"));
        query.insertInto("StockQuote");

        siddhiManager.addQuery(query);

        siddhiManager.addCallback("StockQuote", new Callback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents,
                                Event[] faultEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents, faultEvents);
                Assert.assertTrue("IBM".equals(inEvents[0].getData(0)) || "WSO2".equals(inEvents[0].getData(0)));
                count++;
            }

        });
        InputHandler inputHandler = siddhiManager.getInputHandler("cseEventStream");
        inputHandler.send(new Object[]{"IBM", 75.6f, 100});
        inputHandler.send(new Object[]{"WSO2", 75.6f, 100});
        Thread.sleep(500);
        Assert.assertEquals(2, count);

    }

    @Test
    public void testFilterQuery3() throws InterruptedException {
        System.out.println("Filter test3");

        SiddhiManager siddhiManager = new SiddhiManager();

        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("cseEventStream").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT));

        Query query = QueryFactory.createQuery();
        query.from(QueryFactory.inputStream("cseEventStream"));
        query.insertInto("StockQuote");
        query.project(
                QueryFactory.outputProjector().
                        project("symbol", Expression.variable("symbol"))
        );

        siddhiManager.addQuery(query);

        siddhiManager.addCallback("StockQuote", new Callback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents,
                                Event[] faultEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents, faultEvents);
                Assert.assertTrue("IBM".equals(inEvents[0].getData(0)) || "WSO2".equals(inEvents[0].getData(0)));
                count++;
            }

        });
        InputHandler inputHandler = siddhiManager.getInputHandler("cseEventStream");
        inputHandler.send(new Object[]{"IBM", 75.6f, 100});
        inputHandler.send(new Object[]{"WSO2", 75.6f, 100});
        Thread.sleep(500);
        Assert.assertEquals(2, count);

    }

    @Test
    public void testFilterQuery4() throws InterruptedException {
        System.out.println("Filter test4");

        SiddhiManager siddhiManager = new SiddhiManager();

        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("cseEventStream").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT));

        Query query = QueryFactory.createQuery();
        query.from(QueryFactory.inputStream("cseEventStream").
                handler(Condition.compare(Expression.value(70),
                                          Condition.Operator.GREATER_THAN,
                                          Expression.variable("price"))
                )
        );
        query.insertInto("StockQuote");
        query.project(
                QueryFactory.outputProjector().
                        project("symbol", Expression.variable("symbol")).
                        project("price", Expression.variable("price"))
        );

        siddhiManager.addQuery(query);

        siddhiManager.addCallback("StockQuote", new Callback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents,
                                Event[] faultEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents, faultEvents);
                Assert.assertTrue("WSO2".equals(inEvents[0].getData(0)));
                count++;
            }

        });
        InputHandler inputHandler = siddhiManager.getInputHandler("cseEventStream");
        inputHandler.send(new Object[]{"WSO2", 55.6f, 100});
        inputHandler.send(new Object[]{"IBM", 75.6f, 100});
        inputHandler.send(new Object[]{"WSO2", 57.6f, 100});
        Thread.sleep(500);
        Assert.assertEquals(2, count);

    }

    @Test
    public void testFilterQuery5() throws InterruptedException {
        System.out.println("Filter test5");

        SiddhiManager siddhiManager = new SiddhiManager();

        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("cseEventStream").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT));

        Query query = QueryFactory.createQuery();
        query.from(
                QueryFactory.inputStream("cseEventStream").
                        handler(Handler.Type.WIN, "time", Expression.value(5000))//.
        );
        query.insertInto("StockQuote");
        query.project(
                QueryFactory.outputProjector().
                        project("symbol", Expression.variable("symbol")).
                        project("price", Expression.variable("price"))
        );


        siddhiManager.addQuery(query);
        siddhiManager.addCallback("StockQuote", new Callback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents,
                                Event[] faultEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents, faultEvents);
                if (inEvents != null) {
                    Assert.assertTrue("IBM".equals(inEvents[0].getData(0)) || "WSO2".equals(inEvents[0].getData(0)));
                    count++;
                } else {
                    Assert.assertTrue("IBM".equals(removeEvents[0].getData(0)) || "WSO2".equals(removeEvents[0].getData(0)));
                    count--;
                }
                eventArrived = true;
            }
        });
        InputHandler inputHandler = siddhiManager.getInputHandler("cseEventStream");
        inputHandler.send(new Object[]{"WSO2", 55.6f, 100});
        inputHandler.send(new Object[]{"IBM", 75.6f, 100});
        Thread.sleep(500);
        inputHandler.send(new Object[]{"WSO2", 57.6f, 100});
        Thread.sleep(6000);
        Assert.assertEquals("In and Remove events has to be equal", 0, count);
        Assert.assertEquals("Event arrived", true, eventArrived);

    }

    @Test
    public void testFilterQuery6() throws InterruptedException {
        System.out.println("Filer test6");

        SiddhiManager siddhiManager = new SiddhiManager();

        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("cseEventStream").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT));

        Query query = QueryFactory.createQuery();
        query.from(
                QueryFactory.inputStream("cseEventStream").
                        handler(Handler.Type.WIN, "time", Expression.value(5000))//.
        );
        query.insertInto("StockQuote");
        query.project(
                QueryFactory.outputProjector().
                        project("symbol", Expression.variable("symbol")).
                        project("price", "sum", Expression.variable("price"))
        );


        siddhiManager.addQuery(query);
        siddhiManager.addCallback("StockQuote", new Callback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents,
                                Event[] faultEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents, faultEvents);
                if (inEvents != null) {
                    Assert.assertTrue("IBM".equals(inEvents[0].getData(0)) || "WSO2".equals(inEvents[0].getData(0)));
                    count++;
                } else {
                    Assert.assertTrue("IBM".equals(removeEvents[0].getData(0)) || "WSO2".equals(removeEvents[0].getData(0)));
                    count--;
                }
                eventArrived = true;
            }

        });
        InputHandler inputHandler = siddhiManager.getInputHandler("cseEventStream");
        inputHandler.send(new Object[]{"WSO2", 55.6f, 100});
        inputHandler.send(new Object[]{"IBM", 75.6f, 100});
        Thread.sleep(500);
        inputHandler.send(new Object[]{"WSO2", 57.6f, 100});
        Thread.sleep(15000);
        Assert.assertEquals("In and Remove events has to be equal", 0, count);
        Assert.assertEquals("Event arrived", true, eventArrived);

    }

    @Test
    public void testFilterQuery7() throws InterruptedException {
        System.out.println("Filer test7");

        SiddhiManager siddhiManager = new SiddhiManager();

        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("cseEventStream").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT));

        Query query = QueryFactory.createQuery();
        query.from(
                QueryFactory.inputStream("cseEventStream").
                        handler(Handler.Type.WIN, "timeBatch", 5000L)//.
        );
        query.insertInto("StockQuote");
        query.project(
                QueryFactory.outputProjector().
                        project("symbol", Expression.variable("symbol")).
                        project("price", "sum", Expression.variable("price"))
        );


        siddhiManager.addQuery(query);
        siddhiManager.addCallback("StockQuote", new Callback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents,
                                Event[] faultEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents, faultEvents);
                if (inEvents != null) {
                    Assert.assertTrue("IBM".equals(inEvents[0].getData(0)) || "WSO2".equals(inEvents[0].getData(0)));
                    count++;
                } else {
                    Assert.assertTrue("IBM".equals(removeEvents[0].getData(0)) || "WSO2".equals(removeEvents[0].getData(0)));
                    count--;
                }
                eventArrived = true;
            }

        });
        InputHandler inputHandler = siddhiManager.getInputHandler("cseEventStream");
        inputHandler.send(new Object[]{"WSO2", 55.6f, 100});
        inputHandler.send(new Object[]{"IBM", 75.6f, 100});
        Thread.sleep(500);
        inputHandler.send(new Object[]{"WSO2", 57.6f, 100});
        Thread.sleep(11000);
        Assert.assertEquals("In and Remove event diff", 0, count);
        Assert.assertEquals("Event arrived", true, eventArrived);
    }

    @Test
    public void testFilterQuery8() throws InterruptedException {


        System.out.println("Filter test8");
        SiddhiManager siddhiManager = new SiddhiManager();

        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("cseEventStream").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT));

        Query query = QueryFactory.createQuery();
        query.from(QueryFactory.inputStream("cseEventStream"));
        query.insertInto("StockQuote");
        query.project(
                QueryFactory.outputProjector().
                        project("symbol", Expression.variable("symbol")).
                        project("price", Expression.variable("price")).
                        project("volume", Expression.variable("volume")).groupBy("symbol")
        );

        siddhiManager.addQuery(query);

        siddhiManager.addCallback("StockQuote", new Callback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents,
                                Event[] faultEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents, faultEvents);
                Assert.assertTrue("IBM".equals(inEvents[0].getData(0)) || "WSO2".equals(inEvents[0].getData(0)));
                count++;
                eventArrived = true;
            }
        });
        InputHandler inputHandler = siddhiManager.getInputHandler("cseEventStream");
        inputHandler.send(new Object[]{"IBM", 75.6f, 100});
        inputHandler.send(new Object[]{"WSO2", 75.6f, 100});
        Thread.sleep(500);
        Assert.assertEquals(2, count);
        Assert.assertEquals("Event arrived", true, eventArrived);


    }


    @Test
    public void testFilterQuery9() throws InterruptedException {
        System.out.println("Filer test9");

        SiddhiManager siddhiManager = new SiddhiManager();

        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("cseEventStream").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT));

        Query query = QueryFactory.createQuery();
        query.from(
                QueryFactory.inputStream("cseEventStream").
                        handler(Handler.Type.WIN, "time", Expression.value(5000))//.
        );
        query.insertInto("StockQuote");
        query.project(
                QueryFactory.outputProjector().
                        project("symbol", Expression.variable("symbol")).
                        project("price", "sum", Expression.variable("price")).
                        groupBy("symbol")
        );


        siddhiManager.addQuery(query);
        siddhiManager.addCallback("StockQuote", new Callback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents,
                                Event[] faultEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents, faultEvents);
                if (inEvents != null) {
                    Assert.assertTrue("IBM".equals(inEvents[0].getData(0)) || "WSO2".equals(inEvents[0].getData(0)));
                    count++;
                } else {
                    Assert.assertTrue("IBM".equals(removeEvents[0].getData(0)) || "WSO2".equals(removeEvents[0].getData(0)));
                    if ((new Double(0)).equals(removeEvents[0].getData(1))) {
                        count--;
                    }
                }
                eventArrived = true;
            }

        });
        InputHandler inputHandler = siddhiManager.getInputHandler("cseEventStream");
        inputHandler.send(new Object[]{"WSO2", 55.6f, 100});
        inputHandler.send(new Object[]{"IBM", 75.6f, 100});
        Thread.sleep(500);
        inputHandler.send(new Object[]{"WSO2", 57.6f, 100});
        Thread.sleep(7000);
        Assert.assertEquals("Remove events has two 0.0s out of three total Remove events", 3 - 2, count);
        Assert.assertEquals("Event arrived", true, eventArrived);

    }

    @Test
    public void testFilterQuery10() throws InterruptedException {
        System.out.println("Filer test10");

        SiddhiManager siddhiManager = new SiddhiManager();

        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("cseEventStream").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT));

        Query query = QueryFactory.createQuery();
        query.from(
                QueryFactory.inputStream("cseEventStream").
                        handler(Handler.Type.WIN, "time", Expression.value(5000))//.
        );
        query.insertInto("StockQuote");
        query.project(
                QueryFactory.outputProjector().
                        project("symbol", Expression.variable("symbol")).
                        project("totalPrice", "sum", Expression.variable("price")).
                        groupBy("symbol").
                        having(Condition.compare(Expression.variable("totalPrice"),
                                                 Condition.Operator.GREATER_THAN,
                                                 Expression.value(100.0)))
        );


        siddhiManager.addQuery(query);
        siddhiManager.addCallback("StockQuote", new Callback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents,
                                Event[] faultEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents, faultEvents);
                if (inEvents != null) {
                    Assert.assertTrue("WSO2".equals(inEvents[0].getData(0)));
                    count++;
                } else {
                    Assert.fail("No remove events expected");
                }
                eventArrived = true;
            }

        });
        InputHandler inputHandler = siddhiManager.getInputHandler("cseEventStream");
        inputHandler.send(new Object[]{"WSO2", 55.6f, 100});
        inputHandler.send(new Object[]{"IBM", 75.6f, 100});
        Thread.sleep(500);
        inputHandler.send(new Object[]{"WSO2", 57.6f, 100});
        Thread.sleep(7000);
        Assert.assertEquals("Expected events after having clause", 1, count);
        Assert.assertEquals("Event arrived", true, eventArrived);

    }

//  @Test
//    public void testCreatingFilterQuery() {
//
//        SiddhiManager siddhiManager = new SiddhiManager();
//
//        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("cseEventStream").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.INT).attribute("volume", Attribute.Type.FLOAT));
//
//        Query query = QueryFactory.getQuery();
//        query.from(
//                QueryFactory.inputStream("cseEventStream").
//                        handler(Handler.Type.WIN, "lengthBatch", 50).
//                        handler(Condition.and(Condition.compare(Expression.add(Expression.value(7), Expression.value(9.5)),
//                                                                Condition.Operator.GREATER_THAN,
//                                                                Expression.variable("price")),
//                                              Condition.compare(Expression.value(100),
//                                                                Condition.Operator.GREATER_THAN_EQUAL,
//                                                                Expression.variable("volume")
//                                              )
//                        )
//                        )
//        );
//        query.insertInto("StockQuote");
//        query.project(
//                QueryFactory.outputProjector().
//                        project("symbol", Expression.variable("symbol")).
//                        project("avgPrice", "avg", Expression.variable("symbol")).
//                        groupBy("symbol").
//                        having(Condition.compare(Expression.variable("avgPrice"),
//                                                 Condition.Operator.GREATER_THAN_EQUAL,
//                                                 Expression.value(50)
//                        ))
//        );
//
//        siddhiManager.addQuery(query);
//
//    }


//    @Test
//    public void testCreatingNestedFilterQuery() {
//
//        SiddhiManager siddhiManager = new SiddhiManager();
//
//        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("cseEventStream").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.INT).attribute("volume", Attribute.Type.FLOAT));
//
//        Query query = QueryFactory.getQuery();
//        query.from(
//                QueryFactory.getQuery().
//                        from(
//                                QueryFactory.inputStream("cseEventStream").handler(Condition.compare(Expression.variable("cseEventStream", "price"),
//                                                                                                   Condition.Operator.GREATER_THAN_EQUAL,
//                                                                                                   Expression.value(20)))).
//                        project(
//                                QueryFactory.outputProjector().
//                                        project("symbol", Expression.variable("symbol")).
//                                        project("avgPrice", "avg", Expression.variable("symbol"))).
//                        returnStream()
//
//        );
//        query.project(
//                QueryFactory.outputProjector().
//                        project("symbol", Expression.variable("symbol")).
//                        project("avgPrice", Expression.variable("avgPrice"))
//        );
//        query.insertInto("IBMStockQuote");
//        siddhiManager.addQuery(query);
//    }
}
