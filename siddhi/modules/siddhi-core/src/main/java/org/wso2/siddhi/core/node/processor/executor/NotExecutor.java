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
 * Performs NOT logic for query matching of events using the executor provided
 * @param <T>
 */
public class NotExecutor<T> implements Executor {
    private Executor executor;

    /**
     * initializes the executor
     * @param executor the executor
     */
    public NotExecutor(Executor executor) {
        this.executor = executor;
    }

    /**
     * NOT execution
     *
     * @param eventArray The 2-D event array for processing
     * @return true/false on whether the events matches the given condition
     */
    @Override
    public boolean execute(Event[][] eventArray) {
        return !executor.execute(eventArray);
    }

    /**
     * NOT execution
     *
     * @param eventArray The event array for processing
     * @return true/false on whether the events matches the given condition
     */
    public boolean execute(Event[] eventArray) {
        return !executor.execute(eventArray);
    }

    /**
     * NOT execution
     *
     * @param event The event for processing
     * @return true/false on whether the events matches the given condition
     */
    @Override
    public boolean execute(Event event) {
        return !executor.execute(event);
    }

    public Set<String> getCheckingStreamNames() {
        return new HashSet<String>(executor.getCheckingStreamNames());
    }

}
