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
package org.wso2.siddhi.query.api.query.projection;

import org.wso2.siddhi.query.api.condition.Condition;
import org.wso2.siddhi.query.api.exception.AttributeAlreadyExist;
import org.wso2.siddhi.query.api.expression.Expression;
import org.wso2.siddhi.query.api.expression.Variable;
import org.wso2.siddhi.query.api.query.projection.attribute.AggregationAttribute;
import org.wso2.siddhi.query.api.query.projection.attribute.OutputAttribute;
import org.wso2.siddhi.query.api.query.projection.attribute.SimpleAttribute;

import java.util.ArrayList;
import java.util.List;

public class Projector {
    private List<OutputAttribute> projectionList = new ArrayList<OutputAttribute>();
    private List<Variable> groupByList = new ArrayList<Variable>();
    private Call call;
    private Condition havingCondition;

    public Projector project(String rename, Expression expression) {
        OutputAttribute outputAttribute = new SimpleAttribute(rename, expression);
        checkProjection(outputAttribute);
        projectionList.add(outputAttribute);
        return this;
    }

    private void checkProjection(OutputAttribute newAttribute) {
        for (OutputAttribute attribute : projectionList) {
            if (attribute.getRename().equals(newAttribute.getRename())) {
                throw new AttributeAlreadyExist(attribute.getRename() + " is already defined as an output attribute ");
            }
        }
    }

    public Projector project(String rename, String aggregationName, Expression... expressions) {
        OutputAttribute outputAttribute = new AggregationAttribute(rename, aggregationName, expressions);
        checkProjection(outputAttribute);
        projectionList.add(outputAttribute);
        return this;
    }

    public Projector having(Condition condition) {
        havingCondition = condition;
        return this;
    }

    public Projector groupBy(String streamId, String attributeName) {
        groupByList.add(new Variable(streamId, attributeName));
        return this;
    }

    public Projector groupBy(String attributeName) {
        groupByList.add(new Variable(attributeName));
        return this;
    }

    public Projector addGroupByList(List<Variable>  list) {
        if(list!=null){
            groupByList.addAll(list);
        }
        return this;
    }


    public Projector call(String callName, Object... parameters) {
        call = new Call(callName, parameters);
        return this;
    }

    public List<OutputAttribute> getProjectionList() {
        return projectionList;
    }

    public List<Variable> getGroupByList() {
        return groupByList;
    }

    public Call getCall() {
        return call;
    }

    public Condition getHavingCondition() {
        return havingCondition;
    }

    public Projector addProjectionList(List<OutputAttribute> projectionList) {
        for (OutputAttribute outputAttribute : projectionList) {
            checkProjection(outputAttribute);
            this.projectionList.add(outputAttribute);
        }
        return this;
    }
}
