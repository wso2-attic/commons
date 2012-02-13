/*
 * Copyright 2008,2009 WSO2, Inc. http://www.wso2.org.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wso2.mercury.util;

import org.apache.axis2.description.AxisOperation;

import java.util.Map;
import java.util.HashMap;

/**
 * This class is used to keep the sequence ID and axis operation details
 * when dispatching the response operations in started sqeunce after a
 * persistance.
 */
public class RMDispatchInfo {

    private Map sequenceIDAxisOperationMap;

    public RMDispatchInfo() {
        this.sequenceIDAxisOperationMap = new HashMap();
    }

    public synchronized void addMapping(String sequenceID, AxisOperation axisOperation){
        this.sequenceIDAxisOperationMap.put(sequenceID, axisOperation);
    }

    public synchronized AxisOperation getAxisOperation(String sequenceID){
        return (AxisOperation) this.sequenceIDAxisOperationMap.get(sequenceID);
    }

}
