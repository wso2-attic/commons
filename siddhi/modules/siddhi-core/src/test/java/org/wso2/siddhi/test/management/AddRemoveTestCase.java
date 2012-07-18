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

import junit.framework.Assert;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.stream.input.InputHandler;
import org.wso2.siddhi.core.stream.output.Callback;
import org.wso2.siddhi.core.util.EventPrinter;
import org.wso2.siddhi.query.compiler.exception.SiddhiPraserException;

public class AddRemoveTestCase {
    static final Logger log = Logger.getLogger(AddRemoveTestCase.class);

    private int count;
    private boolean eventArrived;

    @Before
    public void init() {
        count = 0;
        eventArrived = false;
    }

    @Test
    public void testRemoveQuery() throws InterruptedException, SiddhiPraserException {

        log.info("Remove Query test1");
        SiddhiManager siddhiManager = new SiddhiManager();

        InputHandler inputHandler = siddhiManager.defineStream("define stream cseStream ( symbol string, price float, volume int )");

        siddhiManager.addCallback("outStream", new Callback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents,
                                Event[] faultEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents, faultEvents);
                Assert.assertTrue("IBM".equals(inEvents[0].getData(0)) || "WSO2".equals(inEvents[0].getData(0)));
                count++;
            }
        });

        String queryReference = siddhiManager.addQuery("from cseStream[price>10] " +
                                                       "insert into outStream symbol, price, volume ;");

        inputHandler.send(new Object[]{"IBM", 75.6f, 100});
        inputHandler.send(new Object[]{"WSO2", 75.6f, 100});


        siddhiManager.removeQuery(queryReference);
//        InputHandler inputHandler = siddhiManager.getInputHandler("cseEventStream");
        inputHandler.send(new Object[]{"IBM", 75.6f, 100});
        inputHandler.send(new Object[]{"WSO2", 75.6f, 100});

        siddhiManager.shutdown();

        Assert.assertEquals(2, count);

    }

    @Test
    public void testRemoveAddQuery() throws InterruptedException, SiddhiPraserException {

        log.info("Remove then Add Query test1");
        SiddhiManager siddhiManager = new SiddhiManager();

        InputHandler inputHandler = siddhiManager.defineStream("define stream cseStream ( symbol string, price float, volume int )");

        siddhiManager.addCallback("outStream", new Callback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents,
                                Event[] faultEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents, faultEvents);
                Assert.assertTrue("IBM".equals(inEvents[0].getData(0)) || "WSO2".equals(inEvents[0].getData(0)));
                count++;
            }
        });

        String queryReference = siddhiManager.addQuery("from cseStream[price>10] " +
                                                       "insert into outStream symbol, price, volume ;");

        inputHandler.send(new Object[]{"IBM", 75.6f, 100});
        inputHandler.send(new Object[]{"WSO2", 75.6f, 100});
//

        siddhiManager.removeQuery(queryReference);

        inputHandler.send(new Object[]{"IBM", 75.6f, 100});
        inputHandler.send(new Object[]{"WSO2", 75.6f, 100});

        queryReference = siddhiManager.addQuery("from cseStream[price>10] " +
                                                "insert into outStream symbol, price, volume ;");

        inputHandler.send(new Object[]{"IBM", 75.6f, 100});
        inputHandler.send(new Object[]{"WSO2", 75.6f, 100});
        siddhiManager.shutdown();

        Assert.assertEquals(4, count);

    }

    @Test
    public void testRemoveAddQuery2() throws InterruptedException, SiddhiPraserException {

        log.info("Remove then Add different Query test1");
        SiddhiManager siddhiManager = new SiddhiManager();

        InputHandler inputHandler = siddhiManager.defineStream("define stream cseStream ( symbol string, price float, volume int )");

        siddhiManager.addCallback("outStream", new Callback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents,
                                Event[] faultEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents, faultEvents);
                Assert.assertTrue((inEvents[0].getData().length == 2) || (inEvents[0].getData().length == 3));
                if (inEvents[0].getData().length == 2) {
                    count++;
                } else if (inEvents[0].getData().length == 3) {
                    count--;
                }
                eventArrived=true;
            }
        });

        String queryReference = siddhiManager.addQuery("from cseStream[price>10] " +
                                                       "insert into outStream symbol, price, volume ;");

        inputHandler.send(new Object[]{"IBM", 75.6f, 100});
        inputHandler.send(new Object[]{"WSO2", 75.6f, 100});
//

        siddhiManager.removeQuery(queryReference);

        inputHandler.send(new Object[]{"IBM", 75.6f, 100});
        inputHandler.send(new Object[]{"WSO2", 75.6f, 100});

        queryReference = siddhiManager.addQuery("from cseStream[price>10] " +
                                                "insert into outStream symbol, volume ;");

        inputHandler.send(new Object[]{"IBM", 75.6f, 100});
        inputHandler.send(new Object[]{"WSO2", 75.6f, 100});
        siddhiManager.shutdown();

        Assert.assertEquals(0, count);
        Assert.assertEquals(true, eventArrived);

    }

}
