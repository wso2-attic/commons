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

package eventing.samples.atom;

import org.wso2.eventing.EventSink;
import org.wso2.eventing.Event;

/**
 * An example "toy" which simulates event generation from a polled source, such as
 * an Atom feed.  This version simply generates an event every other time the poll
 * cycle occurs.
 */
public class AtomPoller extends Thread {
    String url;
    boolean done;
    EventSink sink;

    public AtomPoller(String name, String url, EventSink sink) {
        super(name);
        this.url = url;
        this.sink = sink;
    }
    
    public void cutItOut() {
        done = true;
    }

    public void run() {
        try {
            int i = 0;
            while (!done) {

                // A real version would do something like:
                // Check time
                // grab feed if necessary
                // check for new items
                // for each new item, send event

                Thread.sleep(1000);
                System.out.println(getName() + " polling url " + url);

                if (i % 2 == 0) {
                    sink.onEvent(new Event("hi there! This is event #" + i));
                }
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
