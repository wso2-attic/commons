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
package org.wso2.siddhi.core.util.parser;

import org.wso2.siddhi.core.exception.OperationNotSupportedException;
import org.wso2.siddhi.core.executor.conditon.AndConditionExecutor;
import org.wso2.siddhi.core.executor.conditon.BooleanConditionExecutor;
import org.wso2.siddhi.core.executor.conditon.ConditionExecutor;
import org.wso2.siddhi.core.executor.conditon.NotConditionExecutor;
import org.wso2.siddhi.core.executor.conditon.OrConditionExecutor;
import org.wso2.siddhi.core.executor.expression.ConstantExpressionExecutor;
import org.wso2.siddhi.core.executor.expression.ExpressionExecutor;
import org.wso2.siddhi.core.executor.expression.VariableExpressionExecutor;
import org.wso2.siddhi.query.api.condition.AndCondition;
import org.wso2.siddhi.query.api.condition.BooleanCondition;
import org.wso2.siddhi.query.api.condition.Compare;
import org.wso2.siddhi.query.api.condition.Condition;
import org.wso2.siddhi.query.api.condition.NotCondition;
import org.wso2.siddhi.query.api.condition.OrCondition;
import org.wso2.siddhi.query.api.definition.Attribute;
import org.wso2.siddhi.query.api.expression.Expression;
import org.wso2.siddhi.query.api.expression.Variable;
import org.wso2.siddhi.query.api.expression.constant.BoolConstant;
import org.wso2.siddhi.query.api.expression.constant.Constant;
import org.wso2.siddhi.query.api.expression.constant.DoubleConstant;
import org.wso2.siddhi.query.api.expression.constant.FloatConstant;
import org.wso2.siddhi.query.api.expression.constant.IntConstant;
import org.wso2.siddhi.query.api.expression.constant.LongConstant;
import org.wso2.siddhi.query.api.expression.constant.StringConstant;
import org.wso2.siddhi.query.api.query.QueryEventStream;

import java.util.List;

public class ExecutorParser {


    public static ConditionExecutor parseCondition(Condition condition,
                                                   List<QueryEventStream> queryEventStreamList,
                                                   String currentStreamReference) {
        if (condition instanceof AndCondition) {
            return new AndConditionExecutor(parseCondition(((AndCondition) condition).getLeftCondition(), queryEventStreamList, currentStreamReference),
                                            parseCondition(((AndCondition) condition).getRightCondition(), queryEventStreamList, currentStreamReference));
        } else if (condition instanceof OrCondition) {
            return new OrConditionExecutor(parseCondition(((OrCondition) condition).getLeftCondition(), queryEventStreamList, currentStreamReference),
                                           parseCondition(((OrCondition) condition).getRightCondition(), queryEventStreamList, currentStreamReference));
        } else if (condition instanceof NotCondition) {
            return new NotConditionExecutor(parseCondition(((NotCondition) condition).getCondition(), queryEventStreamList, currentStreamReference));
        } else if (condition instanceof BooleanCondition) {
            return new BooleanConditionExecutor(parseExpression(((BooleanCondition) condition).getExpression(), queryEventStreamList, currentStreamReference));
        } else if (condition instanceof Compare) {
            if (((Compare) condition).getOperator() == Condition.Operator.EQUAL) {
                return ComparatorParser.parseEqualCompare(parseExpression(((Compare) condition).getLeftExpression(), queryEventStreamList, currentStreamReference),
                                                          parseExpression(((Compare) condition).getRightExpression(), queryEventStreamList, currentStreamReference));
            } else if (((Compare) condition).getOperator() == Condition.Operator.NOT_EQUAL) {
                return ComparatorParser.parseNotEqualCompare(parseExpression(((Compare) condition).getLeftExpression(), queryEventStreamList, currentStreamReference),
                                                             parseExpression(((Compare) condition).getRightExpression(), queryEventStreamList, currentStreamReference));
            } else if (((Compare) condition).getOperator() == Condition.Operator.GREATER_THAN) {
                return ComparatorParser.parseGreaterThanCompare(parseExpression(((Compare) condition).getLeftExpression(), queryEventStreamList, currentStreamReference),
                                                                parseExpression(((Compare) condition).getRightExpression(), queryEventStreamList, currentStreamReference));
            } else if (((Compare) condition).getOperator() == Condition.Operator.GREATER_THAN_EQUAL) {
                return ComparatorParser.parseGreaterThanEqualCompare(parseExpression(((Compare) condition).getLeftExpression(), queryEventStreamList, currentStreamReference),
                                                                     parseExpression(((Compare) condition).getRightExpression(), queryEventStreamList, currentStreamReference));
            } else if (((Compare) condition).getOperator() == Condition.Operator.LESS_THAN) {
                return ComparatorParser.parseLessThanCompare(parseExpression(((Compare) condition).getLeftExpression(), queryEventStreamList, currentStreamReference),
                                                             parseExpression(((Compare) condition).getRightExpression(), queryEventStreamList, currentStreamReference));
            } else if (((Compare) condition).getOperator() == Condition.Operator.LESS_THAN_EQUAL) {
                return ComparatorParser.parseLessThanEqualCompare(parseExpression(((Compare) condition).getLeftExpression(), queryEventStreamList, currentStreamReference),
                                                                  parseExpression(((Compare) condition).getRightExpression(), queryEventStreamList, currentStreamReference));

            }
        }
        throw new OperationNotSupportedException(condition + " not supported !");
    }

    public static ExpressionExecutor parseExpression(Expression expression,
                                                     List<QueryEventStream> queryEventStreamList,
                                                     String currentStreamReference) {
        if (expression instanceof Constant) {
            if (expression instanceof BoolConstant) {
                return new ConstantExpressionExecutor(((BoolConstant) expression).getValue(), Attribute.Type.BOOL);
            } else if (expression instanceof StringConstant) {
                return new ConstantExpressionExecutor(((StringConstant) expression).getValue(), Attribute.Type.STRING);
            } else if (expression instanceof IntConstant) {
                return new ConstantExpressionExecutor(((IntConstant) expression).getValue(), Attribute.Type.INT);
            } else if (expression instanceof LongConstant) {
                return new ConstantExpressionExecutor(((LongConstant) expression).getValue(), Attribute.Type.LONG);
            } else if (expression instanceof FloatConstant) {
                return new ConstantExpressionExecutor(((FloatConstant) expression).getValue(), Attribute.Type.FLOAT);
            } else if (expression instanceof DoubleConstant) {
                return new ConstantExpressionExecutor(((DoubleConstant) expression).getValue(), Attribute.Type.DOUBLE);
            }
        } else if (expression instanceof Variable) {
            return new VariableExpressionExecutor(((Variable) expression).getStreamId(), ((Variable) expression).getAttributeName(), ((Variable) expression).getPosition(), queryEventStreamList, currentStreamReference);

        }

        throw new UnsupportedOperationException(expression.toString() + " not supported!");

    }
}
