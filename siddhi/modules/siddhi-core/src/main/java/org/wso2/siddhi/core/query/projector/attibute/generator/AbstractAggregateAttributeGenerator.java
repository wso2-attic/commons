/*
*  Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/
package org.wso2.siddhi.core.query.projector.attibute.generator;

import org.wso2.siddhi.core.persistence.PersistenceStore;
import org.wso2.siddhi.core.persistence.Persister;
import org.wso2.siddhi.core.query.projector.OutputAttributeGenerator;
import org.wso2.siddhi.query.api.expression.Expression;
import org.wso2.siddhi.query.api.query.QueryEventStream;

import java.util.List;

public abstract class AbstractAggregateAttributeGenerator implements OutputAttributeGenerator,
                                                                     Persister {

    protected String nodeId;
    protected PersistenceStore persistenceStore;

    public abstract void assignExpressions(Expression[] expressions,
                                           List<QueryEventStream> queryEventStreamList);

    public abstract AbstractAggregateAttributeGenerator createNewInstance();

    @Override
    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    @Override
    public void setPersistenceStore(PersistenceStore persistenceStore) {
        this.persistenceStore=persistenceStore;
    }
}
