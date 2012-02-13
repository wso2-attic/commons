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

import org.wso2.eventing.*;
import org.wso2.eventing.exceptions.EventException;
import eventing.samples.atom.AtomPoller;
import eventing.samples.atom.SimpleSubscription;

public class SimpleClientSource implements EventSource {
    public static final String URL = "url";

    public Subscription subscribe(EventSink listener, SubscriptionData data) throws EventException {
        String url = (String)data.getProperty(URL);
        if (url == null) {
            throw new EventException("Not a Valid URL");
        }

        AtomPoller poller = new AtomPoller("poller", url, listener);
        poller.start();

        return new SimpleSubscription(poller);
    }

    public String subscribe(Subscription subscription,EventSink listener) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Subscription subscribe(Subscription subscription) throws EventException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    //TODO: Event injection
    public boolean registerSubscriptionManager(SubscriptionManager subManager) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean registerNotificationManager(NotificationManager notifyManager) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void init() throws EventException {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
