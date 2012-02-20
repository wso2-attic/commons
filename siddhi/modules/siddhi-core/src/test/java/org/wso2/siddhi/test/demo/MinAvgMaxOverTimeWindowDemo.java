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


package org.wso2.siddhi.test.demo;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.wso2.siddhi.api.QueryFactory;
import org.wso2.siddhi.api.eventstream.InputEventStream;
import org.wso2.siddhi.api.eventstream.query.Query;
import org.wso2.siddhi.api.eventstream.query.inputstream.property.WindowType;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.exception.SiddhiException;
import org.wso2.siddhi.core.node.CallbackHandler;
import org.wso2.siddhi.core.node.InputHandler;

import static org.wso2.siddhi.api.condition.where.ConditionOperator.CONTAINS;

public class MinAvgMaxOverTimeWindowDemo {

    private static int NUMBER_OF_EVENTS = 500;
    private static final Logger log = Logger.getLogger(MinAvgMaxOverTimeWindowDemo.class);

    public static void main(String[] args) throws InterruptedException, SiddhiException {
        // Test code
        (new MinAvgMaxOverTimeWindowDemo()).testCase();
    }

    @Before
    public void info() {
        System.out.println("------------------------Query being processed...------------------------------------------------");
    }

    @Test
    public void testCase() throws SiddhiException, InterruptedException {
        //Instantiate SiddhiManager
        SiddhiManager siddhiManager = new SiddhiManager();
        //instantiate query factory for creating the queries        
        QueryFactory qf = SiddhiManager.getQueryFactory();

        InputEventStream stockExchangeStream = new InputEventStream("stockExchangeStream",
                                                                    new String[]{"symbol", "time", "price", "volume"},
                                                                    new Class[]{String.class, Long.class, Double.class, Integer.class}
        );

        InputHandler inputHandler = siddhiManager.addInputEventStream(stockExchangeStream);

        //create the query
        Query query = qf.createQuery(
                "StockQuoteMaxAvgMin",
                qf.output("symbol=stockExchangeStream.symbol", "price=stockExchangeStream.price", "maxPrice=max(stockExchangeStream.price)", "avgPrice=avg(stockExchangeStream.price)", "minPrice=min(stockExchangeStream.price)"),
                qf.from(stockExchangeStream).setWindow(WindowType.TIME,5),
                qf.condition("stockExchangeStream.symbol", CONTAINS, "")
        );
        query.groupBy("stockExchangeStream.symbol");

        siddhiManager.addQuery(query);

        siddhiManager.addCallback(new CallbackHandler("StockQuoteMaxAvgMin") {
            public void callBack(Event event) {
                System.out.println(" >>> Event captured: " + event);
            }
        }
        );

        siddhiManager.update();


        MinAvgMaxStockQuoteDemoEG eventGenerator = new MinAvgMaxStockQuoteDemoEG();
        //printing the Events
        for (int i = 0; i < NUMBER_OF_EVENTS; i++) {
            Event event = eventGenerator.generateEvent();
//                System.out.println("Event send: " + event);
            inputHandler.sendEvent(event);

        }
        Thread.sleep(5000);
        siddhiManager.shutDownTask();

    }

}
