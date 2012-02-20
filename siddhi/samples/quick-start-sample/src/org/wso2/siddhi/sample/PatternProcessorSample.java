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
 * Sample demonstrating a pattern processing
 */
public class PatternProcessorSample {

    public static void main(String[] args) throws SiddhiPraserException, SiddhiException {

        // Create Siddhi Manager
        SiddhiManager siddhiManager = new SiddhiManager();

        // Assign the Callback to receive the outputs
        siddhiManager.addConfigurations("CSEStream:=  symbol[string], price[int];" +
                                        "InfoStock:=  action[string], symbol[string];" +
                                        "StockQuote:=  select symbol=$cond1.symbol, price1=$cond2.price, price2=$cond3.price" +
                                        "              from CSEStream,InfoStock                            " +
                                        "              pattern [                                           " +
                                        "                  cond1=InfoStock.action == 'buy',                " +
                                        "                  cond2=CSEStream.price > 100                     " +
                                        "                        and CSEStream.symbol == $cond1.symbol,    " +
                                        "                  cond3=CSEStream.price > $cond2.price            " +
                                        "                        and CSEStream.symbol == $cond1.symbol     " +
                                        "              ]                                                   " +
                                        "              every($cond1) -> $cond2 -> $cond3 ;                 ");

        // Define the Input Streams and the Queries
        siddhiManager.addCallback(new CallbackHandler("StockQuote") {
            public void callBack(Event event) {
                System.out.println("Event captured  " + event + " ");
            }
        }
        );

        // Initiate Siddhi Manager
        siddhiManager.init();

        // Get the input handler to send the events
        InputHandler inputHandlerCSEStream = siddhiManager.getInputHandler("CSEStream");
        InputHandler inputHandlerInfoStock = siddhiManager.getInputHandler("InfoStock");

        // Create the Event Generator to easily build the events
        // You can also create Siddhi events manually
        EventGenerator eventGeneratorCSEStream = EventGenerator.DefaultFactory.create(inputHandlerCSEStream.getEventStream());
        EventGenerator eventGeneratorInfoStock = EventGenerator.DefaultFactory.create(inputHandlerInfoStock.getEventStream());

        // Generating and sending the events
        inputHandlerCSEStream.sendEvent(eventGeneratorCSEStream.createEvent("IBM", 200));
        inputHandlerCSEStream.sendEvent(eventGeneratorCSEStream.createEvent("WSO2", 101));
        inputHandlerInfoStock.sendEvent(eventGeneratorInfoStock.createEvent("buy", "WSO2"));
        inputHandlerInfoStock.sendEvent(eventGeneratorInfoStock.createEvent("buy", "IBM"));
        inputHandlerCSEStream.sendEvent(eventGeneratorCSEStream.createEvent("IBM", 201));
        inputHandlerCSEStream.sendEvent(eventGeneratorCSEStream.createEvent("WSO2", 97));
        inputHandlerCSEStream.sendEvent(eventGeneratorCSEStream.createEvent("IBM", 205));
        inputHandlerCSEStream.sendEvent(eventGeneratorCSEStream.createEvent("WSO2", 101));
        inputHandlerCSEStream.sendEvent(eventGeneratorCSEStream.createEvent("IBM", 200));
        inputHandlerCSEStream.sendEvent(eventGeneratorCSEStream.createEvent("WSO2", 110));

        siddhiManager.shutDownTask();
    }
}
