package org.wso2.eventing;

import org.wso2.eventing.Subscription;
import org.wso2.eventing.exceptions.EventException;

import java.util.List;
import java.util.Collection;

/*
* Copyright 2005-2008 WSO2, Inc. (http://wso2.com)
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
 * Subscription Manager manage the subscription info,
 */
public interface SubscriptionManager<T> {
    /**
     * Method to add subscription to the store
     *
     * @param subscription to be added to the store
     * @return subscription ID
     */
    public String subscribe(Subscription subscription) throws EventException;

    /**
     * Method to remove a subscription from the store
     *
     * @param subscriptionID
     * @return true|false based on the transaction
     */
    public boolean unsubscribe(String subscriptionID) throws EventException;

    /**
     * Renew an existing subscription
     *
     * @param subscription
     * @return subscription ID
     */
    public boolean renew(Subscription subscription) throws EventException;

    /**
     * Return the active subscribers in the store
     *
     * @return List of subscribers
     */
    public List<Subscription> getSubscriptions() throws EventException;

    /**
     * Get all the subscriptions in the store Active || inactive
     *
     * @return
     * @throws EventException
     */
    public List<Subscription> getAllSubscriptions() throws EventException;

    /**
     * Get the matching subscribers for a given Filter and filter value
     *
     * @param event
     * @return
     * @throws EventException
     */
    public List<Subscription> getMatchingSubscriptions(Event<T> event) throws EventException;

    /**
     * Fetch a subscription by Subscription.ID
     *
     * @param subscriptionID
     * @return matching Subscription
     */
    public Subscription getSubscription(String subscriptionID) throws EventException;

    /**
     * Get the status of a given subscription
     *
     * @param subscriptionID
     * @return
     */
    public Subscription getStatus(String subscriptionID) throws EventException;

    /**
     * Set the additional parameters for the subscription
     *
     * @param name  name of the name value pair
     * @param value value of the name value pair
     * @throws EventException event exception
     */
    public void addProperty(String name, String value);

    /**
     * Get the additional parameter value
     *
     * @param name
     * @return property value
     * @throws EventException
     */
    public String getPropertyValue(String name);

    /**
     * Return all the parameters avilable
     *
     * @return property names
     */
    public Collection<String> getPropertyNames();

    /**
     * Return the list of static subscriptions apply only for relevent implementations
     *
     * @return static subscriptions
     */
    public List<Subscription> getStaticSubscriptions();

    /**
     * Initialise the subscription manager
     */
    public void init();

}
