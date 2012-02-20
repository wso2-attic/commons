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

package org.wso2.siddhi.core.node.processor.executor.simple;

import org.wso2.siddhi.core.exception.SiddhiException;

import java.util.Date;
import java.util.Set;

public class GreatThan<T> extends AbstractSimpleExecutor<T> {

    public GreatThan(Set<String> checkingStreamNames, Class checkingClass) throws SiddhiException {
        super(checkingStreamNames, checkingClass);
    }


    protected boolean executeEventsLogic(T left, T right) {
        //todo check right too
        if (left instanceof String) {
            return (((String) left).compareTo((String) right)) > 0;
        } else if (left instanceof Float) { //todo add other
            return ((Float) left) > ((Float) right);
        } else if (left instanceof Integer) { //todo add other
            return ((Integer) left) > ((Integer) right);
        } else if (left instanceof Long) {
            return ((Long) left) > ((Long) right);
        }  else if (left instanceof Double) { 
            return ((Double) left) > ((Double) right);
        } else if (left instanceof Date) {
            return ((Date) left).after((Date) right);
        } else {
            try {
                throw new Exception("greaterThan failed to match type");//todo exception
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
