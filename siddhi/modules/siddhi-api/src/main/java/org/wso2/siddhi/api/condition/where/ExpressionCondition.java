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


package org.wso2.siddhi.api.condition.where;

import org.wso2.siddhi.api.condition.ExpirableCondition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpressionCondition extends ExpirableCondition implements WhereCondition {

    private String expression;
    static private final String regex = "[^a-zA-Z0-9_][a-zA-Z_][a-zA-Z0-9_]*\\.[a-zA-Z_][a-zA-Z0-9_]*[^a-zA-Z0-9_]";
    static private final String expressionRegex = "==|>=|<=|!=|>|<";
    static private final Random random = new Random();
    private List<String> streamInputs;
    private List<String> expressionInputs;

    private static final char[] symbols = new char[26];

    private final char[] buf = new char[7];

    static {

        for (int idx = 0; idx < 26; ++idx)
            symbols[idx] = (char) ('a' + idx);
    }


    public ExpressionCondition(String inputExpression)  {
        this.expression = " "+inputExpression+" ";
        this.streamInputs = new ArrayList<String>();
        this.expressionInputs = new ArrayList<String>();
//        compiledExpression = MVEL.compileExpression(expression);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(expression);
        while (matcher.find()) {
            String matchingString = matcher.group().substring(1, matcher.group().length() - 1);
            if (!streamInputs.contains(matchingString)) {
                expressionInputs.add(nextUniqueString());
                streamInputs.add(matchingString);
                this.expression = this.expression.replaceAll(matchingString, expressionInputs.get(expressionInputs.size() - 1));
            }
        }
        pattern = Pattern.compile(expressionRegex);
        matcher = pattern.matcher(this.expression);
        if (!matcher.find()) {
            try {
                throw new Exception("No Comparison Operator found");
            } catch (Exception e) {
                e.printStackTrace();  //todo handle
            }
        }else {
            matcher.reset();
        }
//        System.out.println(expression);
//        System.out.println(expressionInputs);
//        System.out.println(streamInputs);

    }

    public String nextUniqueString() {
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = symbols[random.nextInt(symbols.length)];
        String newString = new String(buf);
        if (expressionInputs.contains(newString)) {
            return nextUniqueString();
        } else {
            return newString;
        }
    }


    public String getExpression() {
        return expression;
    }

    public List<String> getStreamInputs() {
        return streamInputs;
    }

    public List<String> getExpressionInputs() {
        return expressionInputs;
    }

//    public static void main(String[] args) {
//        try {
//            new ExpressionCondition("(7+(8*2)>0 CSEStream.price)&& ( CSEStream.symbol =='IBM')&& ( CSEStream.symbol =='IBM')");
//        } catch (Exception e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
//    }

    @Override
    public WhereCondition getNewInstance(Map<String, String> referenceConversion) {
        if (referenceConversion == null) {
            return (WhereCondition) new ExpressionCondition(expression).within(getLifeTime());
        } else {
            String exp = expression.replaceAll(" ", "");
            for (String ref : referenceConversion.keySet()) {
                exp = exp.replace("$" + ref, "$" + referenceConversion.get(ref));
            }
            return (WhereCondition) new ExpressionCondition(exp).within(getLifeTime());
        }
    }
}
