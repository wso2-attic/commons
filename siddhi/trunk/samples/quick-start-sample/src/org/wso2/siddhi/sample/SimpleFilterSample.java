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

package org.wso2.siddhi.sample;

import org.wso2.siddhi.api.exception.SiddhiPraserException;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.event.generator.EventGenerator;
import org.wso2.siddhi.core.exception.SiddhiException;
import org.wso2.siddhi.core.node.CallbackHandler;
import org.wso2.siddhi.core.node.InputHandler;

/**
 * Sample demonstrating a simple filtering use-case
 */
public class SimpleFilterSample {

    public static void main(String[] args) throws SiddhiPraserException, SiddhiException {

        // Create Siddhi Manager
        SiddhiManager siddhiManager = new SiddhiManager();

        // Define the Input Streams and the Queries
        siddhiManager.addQueries("CSEStream:=  symbol[string], price[int];" +
                                 "StockQuote:=  select *        " +
                                 "              from CSEStream  " +
                                 "              where price > 60 ;");

        // Assign the Callback to receive the outputs
        siddhiManager.addCallback(new CallbackHandler("StockQuote") {
            public void callBack(Event event) {
                System.out.println("Event captured  " + event + " ");
            }
        }
        );

        // Initiate Siddhi Manager
        siddhiManager.init();

        // Get the input handler to send the events
        InputHandler inputHandler = siddhiManager.getInputHandler("CSEStream");

        // Create the Event Generator to easily build the events
        // You can also create Siddhi events manually
        EventGenerator eventGenerator = EventGenerator.DefaultFactory.create(inputHandler.getEventStream());

        // Generating and sending the events
        inputHandler.sendEvent(eventGenerator.createEvent("IBM", 55));
        inputHandler.sendEvent(eventGenerator.createEvent("WSO2", 64));
        inputHandler.sendEvent(eventGenerator.createEvent("IBM", 57));
        inputHandler.sendEvent(eventGenerator.createEvent("IBM", 61));
        inputHandler.sendEvent(eventGenerator.createEvent("wSO2", 65));

        // Stopping the server
        siddhiManager.shutDownTask();
    }
}
