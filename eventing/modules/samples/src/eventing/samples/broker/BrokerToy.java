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

package eventing.samples.broker;

import org.wso2.eventing.impl.ConsoleSink;
import org.wso2.eventing.Event;
import org.wso2.eventing.exceptions.EventException;
import eventing.samples.broker.PersistentBroker;

/**
 * A toy which demonstrates the retry functionality of the PersistentBroker.
 */
public class BrokerToy {
    public static class BrokenSink extends ConsoleSink {
        public BrokenSink(String name) {
            super(name);
        }

        public void onEvent(Event event) throws EventException {
            synchronized (this) {
                // Since this is a *broken* sink, fail about 33% of the time.  This
                // should include redelivery attempts, so we should see multiple
                // retries at some point.
                if (Math.random() < .33) {
                    String message = getName() + " failed delivery (" + event.getMessage() + ")!";
                    PersistentBroker.print(message);
                    throw new EventException(message);
                }
            }
            super.onEvent(event);
        }
    }

    public static void main(String[] args) throws Exception {
        ConsoleSink normal = new ConsoleSink("Good");
        BrokenSink bad = new BrokenSink("Bad");

        PersistentBroker broker = new PersistentBroker();
        broker.subscribe(normal, null);
        broker.subscribe(bad, null);

        int i = 0;
        while (true) {
            broker.onEvent(new Event("Event " + i++));
            Thread.sleep(1000);
        }
    }
}
