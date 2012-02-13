/*
 * Copyright 2005-2007 WSO2, Inc. (http://wso2.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eventing.samples.filter;

import org.wso2.eventing.impl.ConsoleSink;
import org.wso2.eventing.impl.BasicSource;
import org.wso2.eventing.EventSink;
import org.wso2.eventing.FilteredSink;
import org.wso2.eventing.EventFilter;
import org.wso2.eventing.Event;
import eventing.samples.filter.ColoredEvent;
import eventing.samples.filter.ColorFilteringSink;

/**
 * This is a toy which generates "color" events by cycling through a list of colors.
 * Two EventSinks are subscribed to the event source, and each looks for different
 * colors.
 */
public class ColorToy {
    static class ColorGenerator extends BasicSource implements Runnable {
        private boolean done;
        private String [] colors = new String [] { "red", "pink", "blue", "green" };

        public void stop() {
            done = true;
        }

        public void run() {
            try {
                int i = 0;
                while (!done) {
                    Thread.sleep(1000);
                    // generate a colored event
                    String color = colors[i % colors.length];
                    ColoredEvent e = new ColoredEvent(color);
                    e.setMessage("event #" + i + " (" + color + ")");
                    System.out.println("Emit - " + e.getMessage());
                    publish(e);
                    i++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        // Here's one way to do it... this requires a special kind of Sink
        EventSink boy = new ColorFilteringSink("blue", new ConsoleSink("Boy"));

        // And here's another, which uses the built-in FilteredSink class and an anonymous filter
        EventSink girl = new FilteredSink(
                new EventFilter() {
                    public boolean match(Event event) {
                        try {
                            return "pink".equals(((ColoredEvent)event).getColor());
                        } catch (Exception e) {
                            // Just in case we get a ClassCastException
                            return false;
                        }
                    }
                },
                new ConsoleSink("Girl"));

        ColorGenerator gen = new ColorGenerator();
        Thread thread = new Thread(gen);
        thread.start();

        gen.subscribe(boy, null);
        gen.subscribe(girl, null);
    }
}
