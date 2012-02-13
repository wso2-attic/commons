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

import org.wso2.eventing.SubscriptionData;
import org.wso2.eventing.Subscription;
import org.wso2.eventing.impl.ConsoleSink;
import eventing.samples.atom.SimpleClientSource;

/**
 * Simple sample that simulates pulling data from an Atom feed.
 */
public class Test {

    public static void main(String[] args) throws Exception {
        SimpleClientSource source = new SimpleClientSource();
        ConsoleSink listener1 = new ConsoleSink("Sanjiva");
        ConsoleSink listener2 = new ConsoleSink("Glen");

        SubscriptionData data = new SubscriptionData();
        data.setProperty(SimpleClientSource.URL, "http://foo.com/myAtomFeed");
        Subscription sub = source.subscribe(listener1, data);
        Subscription sub2 = source.subscribe(listener2, data);

        Thread.sleep(5000);        
        Thread.sleep(3000);

    }
}
