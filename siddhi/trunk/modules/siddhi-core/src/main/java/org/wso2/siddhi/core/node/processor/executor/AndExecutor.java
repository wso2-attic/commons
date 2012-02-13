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
 * Performs AND logic for query matching of events using the left and right executors provided
 * implements Executor
 * @param <T>
 */
public class AndExecutor<T> implements Executor {
    private Executor leftExq;
    private Executor rightExq;

    /**
     * Initializes the AND executor
     * @param leftExq the left expression. It's another Executor
     * @param rightExq the right expression. It's another Executor
     */
    public AndExecutor(Executor leftExq, Executor rightExq) {
        this.leftExq = leftExq;
        this.rightExq = rightExq;
    }

    /**
     * AND execution
     * @param eventArray The event array for processing
     * @return true/false on whether the events matches the given condition
     */
    public boolean execute(Event[] eventArray) {
        return leftExq.execute(eventArray) && rightExq.execute(eventArray);
    }

    /**
     * AND execution
     * @param event The event for processing
     * @return true/false on whether the events matches the given condition
     */
    @Override
    public boolean execute(Event event) {
        return leftExq.execute(event) && rightExq.execute(event);
    }

    /**
     * AND execution
     * @param eventArray The 2-D event array for processing
     * @return true/false on whether the events matches the given condition
     */
    @Override
    public boolean execute(Event[][] eventArray) {
        return leftExq.execute(eventArray) && rightExq.execute(eventArray);
    }

    public Set<String> getCheckingStreamNames() {
        Set<String> set = new HashSet<String>();
        set.addAll(leftExq.getCheckingStreamNames());
        set.addAll(rightExq.getCheckingStreamNames());
        return set;
    }
}
