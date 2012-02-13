package org.wso2.eventing;

import org.wso2.eventing.EventSink;
import org.wso2.eventing.Subscription;
import org.wso2.eventing.SubscriptionData;
import org.wso2.eventing.exceptions.EventException;
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

/**
 * An EventSource is something to which an EventSink can be subscribed.
 */
public interface EventSource {
    /**
     * Subscribe to this source.
     * @param listener an EventSink which will receive events.
     * @param data any custom data (filter expressions, etc) needed for this subscription
     * @return a new Subscription
     * @throws Exception something went wrong
     */
    @Deprecated
    Subscription subscribe(EventSink listener, SubscriptionData data) throws EventException;
      /**
     * Method to add subscription to the store
     * @param subscription to be added to the store
     * @return subscription ID
     */
    @Deprecated
    public String subscribe(Subscription subscription, EventSink listener) throws EventException;

    /**
     * Subscribe
     * @param subscription
     * @return
     * @throws EventException
     */
   public Subscription subscribe(Subscription subscription) throws EventException;
    /**
     *
     * @param subManager
     * @return
     */
    public boolean registerSubscriptionManager(SubscriptionManager subManager) throws EventException;

    /**
     *
     * @param notifyManager
     * @return
     */
    public boolean registerNotificationManager(NotificationManager notifyManager) throws EventException;

    /**
     * Initialise the Event Source 
     * @throws EventException
     */
    public void init() throws EventException;


}
