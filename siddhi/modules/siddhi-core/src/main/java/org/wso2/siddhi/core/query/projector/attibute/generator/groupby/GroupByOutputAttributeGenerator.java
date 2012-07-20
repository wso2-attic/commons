/*
*  Copyright (c) 2005-2012, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
package org.wso2.siddhi.core.query.projector.attibute.generator.groupby;

import org.wso2.siddhi.core.event.AtomicEvent;
import org.wso2.siddhi.core.event.management.PersistenceManagementEvent;
import org.wso2.siddhi.core.executor.expression.ExpressionExecutor;
import org.wso2.siddhi.core.persistence.PersistenceObject;
import org.wso2.siddhi.core.query.projector.attibute.generator.AbstractAggregateAttributeGenerator;
import org.wso2.siddhi.core.util.parser.ExecutorParser;
import org.wso2.siddhi.query.api.definition.Attribute;
import org.wso2.siddhi.query.api.expression.Expression;
import org.wso2.siddhi.query.api.expression.Variable;
import org.wso2.siddhi.query.api.query.QueryEventStream;

import java.util.List;
import java.util.Map;

public class GroupByOutputAttributeGenerator extends AbstractAggregateAttributeGenerator {

    private ExpressionExecutor[] groupByExecutors;

    //stateful
    private GroupByAttributeGeneratorMap attributeGeneratorMap;
    private AbstractAggregateAttributeGenerator attributeGenerator;

    public GroupByOutputAttributeGenerator(List<Variable> groupByList,
                                           List<QueryEventStream> queryEventStreamList) {
        groupByExecutors = new ExpressionExecutor[groupByList.size()];
        for (int i = 0, expressionsSize = groupByList.size(); i < expressionsSize; i++) {
            groupByExecutors[i] = ExecutorParser.parseExpression(groupByList.get(i), queryEventStreamList, null);
        }

    }

    private GroupByOutputAttributeGenerator(ExpressionExecutor[] groupByExecutors) {
        this.groupByExecutors = groupByExecutors;
    }

    public GroupByOutputAttributeGenerator assignAggregateAttributeGenerator(
            AbstractAggregateAttributeGenerator attributeGenerator) {
        this.attributeGeneratorMap = new GroupByAttributeGeneratorMap(groupByExecutors, attributeGenerator);
        this.attributeGenerator = attributeGenerator;
        return this;
    }

    @Override
    public void assignExpressions(Expression[] expressions,
                                  List<QueryEventStream> queryEventStreamList) {
        attributeGenerator.assignExpressions(expressions, queryEventStreamList);
    }


    @Override
    public GroupByOutputAttributeGenerator createNewInstance() {
        return new GroupByOutputAttributeGenerator(groupByExecutors);

    }

    @Override
    public Attribute.Type getType() {
        return attributeGenerator.getType();
    }

    @Override
    public Object process(AtomicEvent event) {
        return attributeGeneratorMap.get(event).process(event);
    }

    @Override
    public void save(PersistenceManagementEvent persistenceManagementEvent) {
        persistenceStore.save(persistenceManagementEvent,nodeId,new PersistenceObject(attributeGeneratorMap.getAttributeGeneratorMap()));
    }

    @Override
    public void load(PersistenceManagementEvent persistenceManagementEvent) {
        PersistenceObject persistenceObject = persistenceStore.load(persistenceManagementEvent,nodeId);
        attributeGeneratorMap.setAttributeGeneratorMap(((Map<String,AbstractAggregateAttributeGenerator>)persistenceObject.getData()[0]));
    }
}
