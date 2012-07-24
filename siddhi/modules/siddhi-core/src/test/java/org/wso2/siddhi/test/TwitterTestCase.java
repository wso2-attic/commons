/*
*  Copyright (c) 2005-2012, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.stream.input.InputHandler;
import org.wso2.siddhi.core.stream.output.Callback;
import org.wso2.siddhi.core.util.EventPrinter;
import org.wso2.siddhi.query.compiler.exception.SiddhiPraserException;

public class TwitterTestCase {

    static final Logger log = Logger.getLogger(TwitterTestCase.class);

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


        SiddhiManager siddhiManager=new SiddhiManager();

        InputHandler allStockQuotesHandler = siddhiManager.defineStream("define stream allStockQuotes ( symbol string, price double )");
        InputHandler twitterFeedHandler = siddhiManager.defineStream("define stream twitterFeed ( company string, wordCount int )");

        siddhiManager.addCallback("outStream", new Callback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                Assert.assertTrue("IBM".equals(inEvents[0].getData(0)) || "WSO2".equals(inEvents[0].getData(0)));
                count++;
            }
        });

        String queryReference1 = siddhiManager.addQuery("                    from allStockQuotes#window.time(600000) \n" +
                                                       "                    insert into fastMovingStockQuotes \n" +
                                                       "                    symbol as symbol, price, avg(price) as averagePrice \n" +
                                                       "                    group by symbol \n" +
                                                       "                    having ( price >  averagePrice*1.02 ) or ( averagePrice*0.98 > price ) ") ;


        String queryReference2 = siddhiManager.addQuery("from twitterFeed#window.time(600000)\n" +
                                                       "                    insert into highFrequentTweets\n" +
                                                       "                    company as company, sum(wordCount) as words\n" +
                                                       "                    group by company\n" +
                                                       "                    having (words > 10);");

      String queryReference3 = siddhiManager.addQuery(" from fastMovingStockQuotes#window.time(60000) as fastMovingStockQuotes join\n" +
                                                      "                    highFrequentTweets#window.time(60000) as highFrequentTweets\n" +
                                                      "\t\t    on fastMovingStockQuotes.symbol == highFrequentTweets.company\n" +
                                                      "                    insert into predictedStockQuotes\n" +
                                                      "                    fastMovingStockQuotes.symbol as company, fastMovingStockQuotes.averagePrice as amount, highFrequentTweets.words as words");



//        inputHandler.send(new Object[]{"IBM", 75.6f, 100});
//        inputHandler.send(new Object[]{"WSO2", 75.6f, 100});


        siddhiManager.removeQuery(queryReference1);
        siddhiManager.removeQuery(queryReference2);
        siddhiManager.removeQuery(queryReference3);
//        InputHandler inputHandler = siddhiManager.getInputHandler("cseEventStream");
//        inputHandler.send(new Object[]{"IBM", 75.6f, 100});
//        inputHandler.send(new Object[]{"WSO2", 75.6f, 100});

        siddhiManager.shutdown();

//        Assert.assertEquals(2, count);

    }
}
