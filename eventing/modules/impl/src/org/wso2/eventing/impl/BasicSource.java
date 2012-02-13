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

package org.wso2.eventing.impl;

import org.wso2.eventing.*;
import org.wso2.eventing.exceptions.EventException;
import org.wso2.eventing.impl.BasicSubscription;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * A framework EventSource class using our BasicSubscription class.
 */
public class BasicSource implements EventSource {
    protected List subscribers = new ArrayList();

    public Subscription subscribe(EventSink listener, SubscriptionData data) throws EventException {
        BasicSubscription sub = new BasicSubscription();
        sub.setListener(listener);
        subscribers.add(sub);
        return sub;
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

    protected void publish(Event event) {
        for (Iterator i = subscribers.iterator(); i.hasNext();) {
            BasicSubscription sub = (BasicSubscription)i.next();
            try {
                sub.getListener().onEvent(event);
            } catch (Exception e) {
                // log error, move on
            }
        }
    }
}
