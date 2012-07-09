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
package org.wso2.siddhi.query.api.query.projection.attribute;

import org.wso2.siddhi.query.api.expression.Expression;

public class AggregationAttribute implements OutputAttribute {
    private String rename;
    private String aggregationName;
    private Expression[] expressions;

    public AggregationAttribute(String rename, String aggregationName, Expression... expressions) {
        this.rename = rename;
        this.aggregationName = aggregationName;
        this.expressions = expressions;
    }

    public String getRename() {
        return rename;
    }

//    @Override
//    public Attribute.Type getType() {
//        return null;  //todo
//    }

    public String getAggregationName() {
        return aggregationName;
    }

    public Expression[] getExpressions() {
        return expressions;
    }
}
