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
package org.wso2.siddhi.test.management;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.persistence.InMemoryPersistenceStore;
import org.wso2.siddhi.core.persistence.PersistenceStore;
import org.wso2.siddhi.core.stream.input.InputHandler;
import org.wso2.siddhi.core.stream.output.Callback;
import org.wso2.siddhi.core.util.EventPrinter;
import org.wso2.siddhi.query.api.QueryFactory;
import org.wso2.siddhi.query.api.condition.Condition;
import org.wso2.siddhi.query.api.definition.Attribute;
import org.wso2.siddhi.query.api.definition.StreamDefinition;
import org.wso2.siddhi.query.api.expression.Expression;
import org.wso2.siddhi.query.api.query.Query;
import org.wso2.siddhi.query.api.stream.pattern.Pattern;
import org.wso2.siddhi.query.compiler.exception.SiddhiPraserException;

public class PersistenceTestCase {
    static final Logger log = Logger.getLogger(PersistenceTestCase.class);

    private int count;
    private long lastValue;
    private boolean eventArrived;

    @Before
    public void init() {
        lastValue=0;
        count = 0;
        eventArrived = false;
    }

    @Test
    public void persistWindowTestQuery() throws InterruptedException, SiddhiPraserException {
        log.info("Persistence test on Window Query test1");

        PersistenceStore persistenceStore = new InMemoryPersistenceStore();
        String revision;

        String streamDefinition = "define stream cseStream ( symbol string, price float, volume int )";
        String query = "from cseStream[price>10][win.length(10)] " +
                                  "insert into outStream symbol, price, sum(volume) as totalVol ";
        Callback callback = new Callback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                Assert.assertTrue("IBM".equals(inEvents[0].getData(0)) || "WSO2".equals(inEvents[0].getData(0)));
                lastValue= (Long)inEvents[0].getData(2);
                count++;
                eventArrived=true;
            }
        } ;


        SiddhiManager siddhiManager = new SiddhiManager();
        siddhiManager.setPersistStore(persistenceStore);

        InputHandler inputHandler = siddhiManager.defineStream(streamDefinition);
        siddhiManager.addQuery(query);
        siddhiManager.addCallback("outStream", callback);

        inputHandler.send(new Object[]{"IBM", 75.6f, 100});
        inputHandler.send(new Object[]{"WSO2", 75.6f, 100});

        //persisting
        revision = siddhiManager.persist();

        inputHandler.send(new Object[]{"IBM", 75.6f, 100});
        inputHandler.send(new Object[]{"WSO2", 75.6f, 100});

        //restarting Siddhi
        siddhiManager.shutdown();
        siddhiManager = new SiddhiManager();
        siddhiManager.setPersistStore(persistenceStore);

        inputHandler = siddhiManager.defineStream(streamDefinition);
        siddhiManager.addQuery(query);
        siddhiManager.addCallback("outStream", callback);

        //loading
        siddhiManager.restoreLastRevision();

        inputHandler.send(new Object[]{"IBM", 75.6f, 100});
        inputHandler.send(new Object[]{"WSO2", 75.6f, 100});

        siddhiManager.shutdown();

        Assert.assertEquals(6, count);
        Assert.assertEquals(400, lastValue);
        Assert.assertEquals(true, eventArrived);

    }

    @Test
    public void testQuery2() throws InterruptedException {
        log.info("test2 - OUT 1");
        PersistenceStore persistenceStore = new InMemoryPersistenceStore();
        String revision;

        StreamDefinition streamDefinition1 = QueryFactory.createStreamDefinition().name("Stream1").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT);
        StreamDefinition streamDefinition2 = QueryFactory.createStreamDefinition().name("Stream2").attribute("symbol", Attribute.Type.STRING).attribute("price", Attribute.Type.FLOAT).attribute("volume", Attribute.Type.INT);

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

        Callback callback = new Callback() {
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                org.junit.Assert.assertArrayEquals(new Object[]{25.6f, 47.6f, null, null, 45.7f}, inEvents[0].getData());
                count++;
                eventArrived=true;
            }

        };

        SiddhiManager siddhiManager = new SiddhiManager();
        siddhiManager.setPersistStore(persistenceStore);

        InputHandler stream1 = siddhiManager.defineStream(streamDefinition1);
        InputHandler stream2 = siddhiManager.defineStream(streamDefinition2);

        siddhiManager.addQuery(query);

        siddhiManager.addCallback("OutStream", callback);

        stream1.send(new Object[]{"WSO2", 25.6f, 100});
        Thread.sleep(500);
        stream1.send(new Object[]{"GOOG", 47.6f, 100});
        Thread.sleep(500);
        stream1.send(new Object[]{"GOOG", 13.7f, 100});
        Thread.sleep(1000);

        //persisting
        revision = siddhiManager.persist();
        Thread.sleep(1000);

        //Restarting siddhi
        siddhiManager.shutdown();
        siddhiManager = new SiddhiManager();
        siddhiManager.setPersistStore(persistenceStore);

        stream1 = siddhiManager.defineStream(streamDefinition1);
        stream2 = siddhiManager.defineStream(streamDefinition2);
        siddhiManager.addQuery(query);
        siddhiManager.addCallback("OutStream", callback);

        //loading
        siddhiManager.restoreLastRevision();
        Thread.sleep(1000);

        stream2.send(new Object[]{"IBM", 45.7f, 100});
        Thread.sleep(500);
        stream1.send(new Object[]{"GOOG", 47.8f, 100});
        Thread.sleep(500);
        stream2.send(new Object[]{"IBM", 55.7f, 100});
        Thread.sleep(500);

        siddhiManager.shutdown();

        Assert.assertEquals(1, count);
        Assert.assertEquals(true, eventArrived);

    }


}
