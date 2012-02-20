/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.siddhi.core.node.processor.executor;

import org.wso2.siddhi.core.event.Event;

import java.util.Set;

/**
 * The interface defining common methods for the executors
 * This performs the CONDITION of a given query for the events coming through the stream
 */
public interface Executor {

    /**
     * Executes the condition (i.e. the 'WHERE'clause) and returns true or false
     * depending on whether the 
     * @param eventArray the event array
     * @return execution outcome
     */
    public boolean execute(Event[] eventArray);

    /**
     * Does the same functionality as #execute method. Here only one event can be passed.
     * This should be used except for FollowedByExecutors.
     * @param event the event for processing
     * @return execution outcome
     */
    public boolean execute(Event event);

    public boolean execute(Event[][] eventArray);

    /**
     * Returns a Set<String> of eventstream names containing in the condition of leftExq and rightExq.
     * @return string set containg the eventstream names
     */    
    public Set<String> getCheckingStreamNames();
}
