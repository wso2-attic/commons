package org.wso2.eventing;

import org.wso2.eventing.exceptions.EventException;

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
 *   Notification Manager manage the subscription info,
 */
public interface NotificationManager {
    /**
     * publish the events to subscribers
     * @param event the event to publish
     */
    public void publishEvent(Event event) throws EventException;

    /**
     * register an event dispatcher
     * @param eventDispatcher the event dispatcher
     */
    public void registerEventDispatcher(EventDispatcher eventDispatcher) throws EventException;
}
