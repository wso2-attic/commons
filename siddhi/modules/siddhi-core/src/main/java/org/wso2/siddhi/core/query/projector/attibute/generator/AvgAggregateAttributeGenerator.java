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

import org.wso2.siddhi.core.event.AtomicEvent;
import org.wso2.siddhi.core.event.management.PersistenceManagementEvent;
import org.wso2.siddhi.core.event.remove.RemoveStream;
import org.wso2.siddhi.core.executor.expression.ExpressionExecutor;
import org.wso2.siddhi.core.persistence.PersistenceObject;
import org.wso2.siddhi.core.persistence.Persister;
import org.wso2.siddhi.core.query.projector.attibute.aggregator.Aggregator;
import org.wso2.siddhi.core.util.parser.AggregatorParser;
import org.wso2.siddhi.core.util.parser.ExecutorParser;
import org.wso2.siddhi.query.api.definition.Attribute;
import org.wso2.siddhi.query.api.expression.Expression;
import org.wso2.siddhi.query.api.query.QueryEventStream;

import java.util.List;

public class AvgAggregateAttributeGenerator extends AbstractAggregateAttributeGenerator implements
                                                                                        Persister{
    private ExpressionExecutor expressionExecutor;
    private Aggregator aggregator;



    public AvgAggregateAttributeGenerator() {
    }

    private AvgAggregateAttributeGenerator(Aggregator aggregator,
                                           ExpressionExecutor expressionExecutor) {
        this.aggregator = aggregator;
        this.expressionExecutor = expressionExecutor;
    }

    @Override
    public void assignExpressions(Expression[] expressions,
                                  List<QueryEventStream> queryEventStreamList) {
        expressionExecutor = ExecutorParser.parseExpression(expressions[0], queryEventStreamList, null);
        aggregator = AggregatorParser.createAvgAggregator(expressionExecutor.getType());
    }

    @Override
    public AbstractAggregateAttributeGenerator createNewInstance() {
        return new AvgAggregateAttributeGenerator(aggregator.createNewInstance(),expressionExecutor);
    }

    @Override
    public Attribute.Type getType() {
        return aggregator.getType();
    }

    @Override
    public Object process(AtomicEvent event) {
        if (event instanceof RemoveStream) {
            return aggregator.remove(expressionExecutor.execute(event));
        } else {
            return aggregator.add(expressionExecutor.execute(event));
        }
    }

    @Override
    public void save(PersistenceManagementEvent persistenceManagementEvent) {
        persistenceStore.save(persistenceManagementEvent,nodeId,new PersistenceObject(aggregator));
    }

    @Override
    public void load(PersistenceManagementEvent persistenceManagementEvent) {
        PersistenceObject persistenceObject = persistenceStore.load(persistenceManagementEvent,nodeId);
        aggregator=((Aggregator)persistenceObject.getData()[0]);
    }
}
