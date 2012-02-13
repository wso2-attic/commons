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
package org.wso2.siddhi.api.util;

import org.wso2.siddhi.api.condition.*;
import org.wso2.siddhi.api.condition.pattern.EveryCondition;
import org.wso2.siddhi.api.condition.pattern.FollowedByCondition;
import org.wso2.siddhi.api.condition.pattern.NonOccurrenceCondition;
import org.wso2.siddhi.api.condition.sequence.SequenceCondition;
import org.wso2.siddhi.api.condition.sequence.SequenceStarCondition;
import org.wso2.siddhi.api.condition.where.SimpleCondition;
import org.wso2.siddhi.api.condition.where.logical.AndCondition;
import org.wso2.siddhi.api.condition.where.logical.NotCondition;
import org.wso2.siddhi.api.condition.where.logical.OrCondition;

import java.util.LinkedList;
import java.util.List;

/**
 * OutputDefinitionParserUtil class is a Util for parsing the output definition
 */
public class OutputDefinitionParserUtil {

    public static List<String> createStreamIdListFromConditions(FollowedByCondition followedByCondition) {
        List<Condition> conList;
        String leftProp = "";
        List<String> streamIdList = new LinkedList<String>();
        for (Condition condition : followedByCondition.getFollowingConditions())
            if (condition instanceof EveryCondition) {
                List<Condition> everyConList = ((EveryCondition) condition).getFollowingConditions();
                for (Condition tmp : everyConList) {
                    streamIdList.add(((SimpleCondition) getLeftSimpleCondition(tmp)).getLeftProperty().trim().split("\\.")[0]);
                }
            } else if (condition instanceof NonOccurrenceCondition) {
                streamIdList.add(((SimpleCondition) getLeftSimpleCondition(((NonOccurrenceCondition) condition).getOccurringCondition())).getLeftProperty().trim().split("\\.")[0]);
            } else {
                streamIdList.add(((SimpleCondition) getLeftSimpleCondition(condition)).getLeftProperty().trim().split("\\.")[0]);
            }
        return streamIdList;
    }

    public static List<String> createStreamIdListFromConditions(SequenceCondition sequenceCondition) {

        List<String> attributeList = new LinkedList<String>();
        for (Condition condition : sequenceCondition.getPatternConditions())
            if (condition instanceof SequenceStarCondition) {
                Condition tmp = ((SequenceStarCondition) condition).getCondition();
                attributeList.add(((SimpleCondition) getLeftSimpleCondition(tmp)).getLeftProperty().trim().split("\\.")[0]);

            } else {
                attributeList.add(((SimpleCondition) getLeftSimpleCondition(condition)).getLeftProperty().trim().split("\\.")[0]);
            }
        return attributeList;
    }

    private static Condition getLeftSimpleCondition(Condition condition) {
        Condition temp = null;
        if (condition instanceof AndCondition) {
            temp = getAndLeft((AndCondition) condition);
        } else if (condition instanceof OrCondition) {
            temp = getOrLeft((OrCondition) condition);
        } else if (condition instanceof SimpleCondition) {
            temp = condition;
        } else if (condition instanceof NotCondition) {
            temp = getNot((NotCondition) condition);
        }
        return temp;
    }

    private static Condition getAndLeft(AndCondition andCondition) {    //ok
        Condition leftCondition = andCondition.getLeftCondition();
        if (!(leftCondition instanceof SimpleCondition)) {
            return getLeftSimpleCondition(leftCondition);
        } else {
            return leftCondition;
        }
    }

    private static Condition getOrLeft(OrCondition orCondition) {
        Condition leftCondition = orCondition.getLeftCondition();
        if (!(leftCondition instanceof SimpleCondition)) {
            return getLeftSimpleCondition(leftCondition);
        } else {
            return leftCondition;
        }
    }

    private static Condition getNot(NotCondition notCondition) {
        Condition temp = notCondition.getCondition();
        if (!(temp instanceof SimpleCondition)) {
            return getLeftSimpleCondition(temp);
        } else {
            return temp;
        }
    }


}
