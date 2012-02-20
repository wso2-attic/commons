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
package org.wso2.siddhi.core.node.processor.executor.simple.property;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * this Object contains fields of Complex Property
 */
public class ComplexProperty implements Property {
    private Serializable compiledExpression = null;
    private List<String> expressionInputs = new ArrayList<String>();
    private List<String> streamInputs = new ArrayList<String>();
    private Map<String, int[]> expressionPropertyPositionMap;

    public Serializable getCompiledExpression() {
        return compiledExpression;
    }

    public void setCompiledExpression(Serializable compiledExpression) {
        this.compiledExpression = compiledExpression;
    }

    public List<String> getExpressionInputs() {
        return expressionInputs;
    }

    public void setExpressionInputs(List<String> expressionInputs) {
        this.expressionInputs = expressionInputs;
    }

    public List<String> getStreamInputs() {
        return streamInputs;
    }

    public void setStreamInputs(List<String> streamInputs) {
        this.streamInputs = streamInputs;
    }

    public Map<String, int[]> getExpressionPropertyPositionMap() {
        return expressionPropertyPositionMap;
    }

    public void setExpressionPropertyPositionMap(Map<String, int[]> expressionPropertyPositionMap) {
        this.expressionPropertyPositionMap = expressionPropertyPositionMap;
    }



}
