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
import org.wso2.siddhi.core.stream.output.Callback;
import org.wso2.siddhi.core.stream.input.InputHandler;
import org.wso2.siddhi.core.util.EventPrinter;
import org.wso2.siddhi.query.api.query.Query;
import org.wso2.siddhi.query.api.QueryFactory;
import org.wso2.siddhi.query.api.condition.Condition;
import org.wso2.siddhi.query.api.expression.Expression;
import org.wso2.siddhi.query.api.stream.JoinStream;
import org.wso2.siddhi.query.api.definition.Attribute;
import org.wso2.siddhi.query.api.stream.handler.Handler;

public class JoinTestCase {
    static final Logger log = Logger.getLogger(JoinTestCase.class);
    private int eventCount;
    private boolean eventArrived;

    @Before
    public void init() {
        eventCount = 0;
        eventArrived = false;
    }

    @Test
    public void testQuery1() throws InterruptedException {
        log.info("test1");

        SiddhiManager siddhiManager = new SiddhiManager();

        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("cseEventStream").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT));
        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("twitterStream").attribute("user", Attribute.Type.STRING).attribute("tweet", Attribute.Type.STRING).attribute("symbol", Attribute.Type.STRING));

        Query query = QueryFactory.createQuery();
        query.from(
                QueryFactory.joinStream(
                        QueryFactory.inputStream("cseEventStream").
                                handler(Handler.Type.WIN, "time", 1000),
                        JoinStream.Type.INNER_JOIN,
                        QueryFactory.inputStream("twitterStream").
                                handler(Handler.Type.WIN, "time", 1000),
                        Condition.compare(Expression.variable("cseEventStream", "symbol"),
                                          Condition.Operator.EQUAL,
                                          Expression.variable("twitterStream", "symbol"))


                )
        );
        query.insertInto("StockQuote");
        query.project(
                QueryFactory.outputProjector().
                        project("symbol", Expression.variable("cseEventStream", "symbol")).
                        project("tweet", Expression.variable("twitterStream", "tweet")).
                        project("price", Expression.variable("cseEventStream", "price"))
        );


        siddhiManager.addQuery(query);
        siddhiManager.addCallback("StockQuote", new Callback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                if (inEvents != null) {
                    Assert.assertTrue("IBM".equals(inEvents[0].getData(0)) || "WSO2".equals(inEvents[0].getData(0)));
                    eventCount++;
                } else {
                    Assert.assertTrue("IBM".equals(removeEvents[0].getData(0)) || "WSO2".equals(removeEvents[0].getData(0)));
                    eventCount--;
                }
                eventArrived = true;
            }

        });
        InputHandler cseEventStreamHandler = siddhiManager.getInputHandler("cseEventStream");
        InputHandler twitterStreamHandler = siddhiManager.getInputHandler("twitterStream");
        cseEventStreamHandler.send(new Object[]{"WSO2", 55.6f, 100});
        twitterStreamHandler.send(new Object[]{"User1", "Hello World", "WSO2"});
//        Thread.sleep(70000);
        cseEventStreamHandler.send(new Object[]{"IBM", 75.6f, 100});
        Thread.sleep(500);
        cseEventStreamHandler.send(new Object[]{"WSO2", 57.6f, 100});
        Thread.sleep(2000);

        Assert.assertEquals("Number of success events", 0, eventCount);
        Assert.assertEquals("Event arrived", true, eventArrived);


    }

    @Test
    public void testQuery2() throws InterruptedException {
        log.info("test2");

        SiddhiManager siddhiManager = new SiddhiManager();

        siddhiManager.defineStream(QueryFactory.createStreamDefinition().name("cseEventStream").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT));

        Query query = QueryFactory.createQuery();
        query.from(
                QueryFactory.joinStream(
                        QueryFactory.inputStream("a", "cseEventStream").
                                handler(Handler.Type.WIN, "time", 500),
                        JoinStream.Type.JOIN,
                        QueryFactory.inputStream("b", "cseEventStream").
                                handler(Handler.Type.WIN, "time", 500),
                        Condition.compare(Expression.variable("a", "symbol"),
                                          Condition.Operator.EQUAL,
                                          Expression.variable("b", "symbol"))


                )
        );
        query.insertInto("StockQuote");
        query.project(
                QueryFactory.outputProjector().
                        project("symbol", Expression.variable("a", "symbol")).
                        project("priceA", Expression.variable("a", "price")).
                        project("priceB", Expression.variable("b", "price"))
        );


        siddhiManager.addQuery(query);
        siddhiManager.addCallback("StockQuote", new Callback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                if (inEvents != null) {
                    Assert.assertTrue("IBM".equals(inEvents[0].getData(0)) || "WSO2".equals(inEvents[0].getData(0)));
                    eventCount++;
                } else {
                    Assert.assertTrue("IBM".equals(removeEvents[0].getData(0)) || "WSO2".equals(removeEvents[0].getData(0)));
                    eventCount--;
                }
                eventArrived = true;
            }

        });
        InputHandler cseEventStreamHandler = siddhiManager.getInputHandler("cseEventStream");
        cseEventStreamHandler.send(new Object[]{"IBM", 75.6f, 100});
        cseEventStreamHandler.send(new Object[]{"WSO2", 57.6f, 100});
        Thread.sleep(2000);

        Assert.assertEquals("Number of success events", 0, eventCount);
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
