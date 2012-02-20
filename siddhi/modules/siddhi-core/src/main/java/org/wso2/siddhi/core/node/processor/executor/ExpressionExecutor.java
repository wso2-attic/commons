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

import org.mvel2.MVEL;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.exception.SiddhiException;

import java.io.Serializable;
import java.util.*;


public class ExpressionExecutor implements Executor {
    private List<String> checkingStreamNames;
    private Map<String, int[]> expressingNamePositionMap;
    private Map<String, Object> executorDataMap = new Hashtable<String, Object>();
    private Serializable compiledExpression;

    public ExpressionExecutor(String expression, List<String> checkingStreamNames, Map<String, int[]> expressingNamePositionMap) throws SiddhiException {
        this.checkingStreamNames = checkingStreamNames;
        this.expressingNamePositionMap = expressingNamePositionMap;
        if (null == checkingStreamNames || checkingStreamNames.size() == 0) {
            throw new SiddhiException("No checking Stream Name");
        }
        compiledExpression = MVEL.compileExpression(expression);
    }

    @Override
    public boolean execute(Event[] eventArray) {
        for (int[] position : expressingNamePositionMap.values()) {
            if (checkingStreamNames.contains(eventArray[position[0]].getEventStreamId())) {
                return false;
            }
        }
        for (String expressingName : expressingNamePositionMap.keySet()) {
            executorDataMap.put(expressingName, eventArray[expressingNamePositionMap.get(expressingName)[0]].<Object>getNthAttribute(expressingNamePositionMap.get(expressingName)[1]));
        }

        return (Boolean) MVEL.executeExpression(compiledExpression, executorDataMap);
    }

    @Override
    public boolean execute(Event event) {
        if (!checkingStreamNames.contains(event.getEventStreamId())) {
            return false;
        }
        for (String expressingName : expressingNamePositionMap.keySet()) {
            executorDataMap.put(expressingName, event.<Object>getNthAttribute(expressingNamePositionMap.get(expressingName)[1]));
        }
        return (Boolean) MVEL.executeExpression(compiledExpression, executorDataMap);
    }

    @Override
    public boolean execute(Event[][] eventArray) {
        return false;
    }

    public Set<String> getCheckingStreamNames() {
        return new HashSet<String>(checkingStreamNames);
    }
}
