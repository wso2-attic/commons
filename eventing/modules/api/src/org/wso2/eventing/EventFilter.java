package org.wso2.eventing;

import org.wso2.eventing.Event;
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

public interface EventFilter<T> {
    /**
     * Implementations must override this to provide actual filtering
     * functionality.  If an Exception is thrown, the event should be thrown away.
     *
     * @param event an Event
     * @return true if the event should be passed through, false if it should be dropped
     */
    boolean match(Event<T> event);
   
}
