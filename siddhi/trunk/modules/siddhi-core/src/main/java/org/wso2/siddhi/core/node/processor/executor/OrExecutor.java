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

import java.util.HashSet;
import java.util.Set;

/**
 * Performs OR logic for query matching of events using the left and right executors provided
 *
 * @param <T>
 */
public class OrExecutor<T> implements Executor {
    private Executor leftExq;
    private Executor rightExq;

    /**
     * initializes the executor
     * @param leftExq left Executor
     * @param rightExq right executor
     */
    public OrExecutor(Executor leftExq, Executor rightExq) {
        this.leftExq = leftExq;
        this.rightExq = rightExq;
    }

    /**
     * OR execution
     *
     * @param eventArray The event array for processing
     * @return true/false on whether the events matches the given condition
     */
    public boolean execute(Event[] eventArray) {
        return leftExq.execute(eventArray) || rightExq.execute(eventArray);
    }

    /**
     * OR execution
     *
     * @param eventArray The two-dimensional event array for processing
     * @return true/false on whether the events matches the given condition
     */
    public boolean execute(Event[][] eventArray) {
        return leftExq.execute(eventArray) || rightExq.execute(eventArray);
    }

    /**
     * OR execution
     *
     * @param event an event for processing
     * @return true/false on whether the events matches the given condition
     */    
    public boolean execute(Event event) {
        return leftExq.execute(event) || rightExq.execute(event);
    }

    /**
     * Returns a Set<String> of stream names containing in the condition of leftExq and rightExq.
     * @return
     */
    public Set<String> getCheckingStreamNames() {
        Set<String> set = new HashSet<String>();
        set.addAll(leftExq.getCheckingStreamNames());
        set.addAll(rightExq.getCheckingStreamNames());
        return set;
    }
}
