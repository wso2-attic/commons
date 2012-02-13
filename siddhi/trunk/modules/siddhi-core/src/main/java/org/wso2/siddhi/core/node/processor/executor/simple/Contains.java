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

import org.apache.log4j.Logger;
import org.wso2.siddhi.core.exception.SiddhiException;

import java.util.Set;

/**
 * this checks whether the left string contains the right string
 * and this also not case sensitive
 * @param <T>
 */
public class Contains<T> extends AbstractSimpleExecutor<T> {

    private static final Logger log = Logger.getLogger(AbstractSimpleExecutor.class);

    public Contains(Set<String> checkingStreamNames, Class checkingClass) throws SiddhiException {
        super(checkingStreamNames,checkingClass);
    }

    protected boolean executeEventsLogic(T left, T right) {
//        log.debug("Simple executer executing "+left+" "+right);
        return left.toString().toLowerCase().contains(right.toString().toLowerCase());
    }



}
