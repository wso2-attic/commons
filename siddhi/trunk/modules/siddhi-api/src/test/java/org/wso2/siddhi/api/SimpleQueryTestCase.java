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

package org.wso2.siddhi.api;

import org.junit.Assert;
import org.junit.Test;
import org.wso2.siddhi.api.condition.where.SimpleCondition;
import org.wso2.siddhi.api.condition.where.logical.AndCondition;
import org.wso2.siddhi.api.condition.where.logical.NotCondition;
import org.wso2.siddhi.api.condition.where.logical.OrCondition;
import org.wso2.siddhi.api.eventstream.EventStream;
import org.wso2.siddhi.api.eventstream.query.Query;
import org.wso2.siddhi.api.exception.SiddhiPraserException;
import org.wso2.siddhi.compiler.SiddhiCompiler;

import java.util.List;

public class SimpleQueryTestCase {

    @Test
    public void QueryTestCase1() throws SiddhiPraserException {
        List<EventStream> eventStreamList =
                SiddhiCompiler.parse("cseEventStream:=symbol[string], price[int];" +
                                     "StockQuote:=select symbol, price from cseEventStream where symbol=='IBM';");
        Assert.assertTrue(eventStreamList.size() == 2);
    }

    @Test
    public void QueryTestCase2() throws SiddhiPraserException {
        List<EventStream> eventStreamList =
                SiddhiCompiler.parse("cseEventStream:=symbol[string], price[int];" +
                                     "StockQuote:=select symbol, price from cseEventStream [win.time=100, std.firstUnique=symbol] where symbol=='IBM';");
        Assert.assertTrue(eventStreamList.size() == 2);
    }

    @Test
    public void QueryTestCase3() throws SiddhiPraserException {
        List<EventStream> eventStreamList =
                SiddhiCompiler.parse("cseEventStream:=symbol[string], price[int];" +
                                     "StockQuote:=select newSymbol=symbol, price from cseEventStream [win.time=100, std.firstUnique=symbol] where symbol=='IBM';");
        Assert.assertTrue(eventStreamList.size() == 2);
    }

    @Test
    public void QueryTestCase4() throws SiddhiPraserException {
        List<EventStream> eventStreamList =
                SiddhiCompiler.parse("cseEventStream:=symbol[string], price[int];" +
                                     "StockQuote:=select symbol, price from cseEventStream [win.time=100, std.firstUnique=symbol] where symbol=='IBM';");
        Assert.assertTrue(eventStreamList.size() == 2);
    }

    @Test
    public void QueryTestCase5() throws SiddhiPraserException {
        List<EventStream> eventStreamList =
                SiddhiCompiler.parse("cseEventStream:=symbol[string], price[int];" +
                                     "StockQuote:=select symbol, price from cseEventStream where symbol=='IBM' or price>10");
        Assert.assertTrue(eventStreamList.size() == 2);
        Assert.assertTrue(((Query) eventStreamList.get(1)).getCondition() instanceof OrCondition);
    }

    @Test
    public void QueryTestCase6() throws SiddhiPraserException {
        List<EventStream> eventStreamList =
                SiddhiCompiler.parse("cseEventStream:=symbol[string], price[int];" +
                                     "StockQuote:=select symbol, price from cseEventStream where symbol=='IBM' or (price>10 and symbol=='WSO2');");
        Assert.assertTrue(eventStreamList.size() == 2);
        Assert.assertTrue(((Query) eventStreamList.get(1)).getCondition() instanceof OrCondition);
    }

    @Test
    public void QueryTestCase7() throws SiddhiPraserException {
        List<EventStream> eventStreamList =
                SiddhiCompiler.parse("cseEventStream:=symbol[string], price[int];" +
                                     "StockQuote:=select symbol, price from cseEventStream where ( symbol=='IBM' or price>10 ) and symbol=='WSO2';");
        Assert.assertTrue(eventStreamList.size() == 2);
        Assert.assertTrue(((Query) eventStreamList.get(1)).getCondition() instanceof AndCondition);
    }

    @Test
    public void QueryTestCase8() throws SiddhiPraserException {
        List<EventStream> eventStreamList =
                SiddhiCompiler.parse("cseEventStream:=symbol[string], price[int];" +
                                     "StockQuote:=select symbol, price from cseEventStream where not( symbol=='IBM' or price>10 ) and symbol=='WSO2';");
        Assert.assertTrue(eventStreamList.size() == 2);
        Assert.assertTrue(((AndCondition) ((Query) eventStreamList.get(1)).getCondition()).getLeftCondition() instanceof NotCondition);
    }

    @Test
    public void QueryTestCase9() throws SiddhiPraserException {
        List<EventStream> eventStreamList =
                SiddhiCompiler.parse("CSEStream:= symbol [string], price [int]; \n" +
                                     "" +
                                     "StockQuote:= select symbol, avgPrice=avg(price), symbolCount=count(symbol) " +
                                     "from CSEStream[win.time=500] " +
                                     "where  (7+(8*2)< price*2) and symbol=='IBM';");
        Assert.assertTrue(eventStreamList.size() == 2);
        Assert.assertTrue(((Query) eventStreamList.get(1)).getCondition() instanceof AndCondition);
    }

    @Test
    public void QueryTestCase10() throws SiddhiPraserException {
        List<EventStream> eventStreamList =
                SiddhiCompiler.parse("CSEStream:= symbol[string], price [int]; \n" +
                                     "" +
                                     "StockQuote:= select symbol, avgPrice=avg(price), symbolCount=count(symbol) " +
                                     "from CSEStream[win.time=500] " +
                                     "where   (symbol== 'IBM') and (7+ (8*2) < price*3 )  ;");

        Assert.assertTrue(eventStreamList.size() == 2);
        Assert.assertTrue(((Query) eventStreamList.get(1)).getCondition() instanceof AndCondition);
        Assert.assertTrue(((AndCondition) ((Query) eventStreamList.get(1)).getCondition()).getLeftCondition() instanceof SimpleCondition);
        Assert.assertTrue(((AndCondition) ((Query) eventStreamList.get(1)).getCondition()).getRightCondition() instanceof SimpleCondition);
    }

    @Test
    public void QueryTestCase11() throws SiddhiPraserException {
        List<EventStream> eventStreamList =
                SiddhiCompiler.parse("CSEStream:= symbol[string], price [int]; \n" +
                                     "" +
                                     "StockQuote:= select symbol, avgPrice=avg(price), symbolCount=count(symbol) " +
                                     "from CSEStream[win.time=500] " +
                                     "where  7+(8*2)<price*2+price or symbol=='IBM' and symbol=='WSO2' ;");
        Assert.assertTrue(eventStreamList.size() == 2);
        Assert.assertTrue(((Query) eventStreamList.get(1)).getCondition() instanceof OrCondition);
        Assert.assertTrue(((OrCondition) ((Query) eventStreamList.get(1)).getCondition()).getLeftCondition() instanceof SimpleCondition);
        Assert.assertTrue(((OrCondition) ((Query) eventStreamList.get(1)).getCondition()).getRightCondition() instanceof AndCondition);

    }

    @Test
    public void QueryTestCase12() throws SiddhiPraserException {
        List<EventStream> eventStreamList =
                SiddhiCompiler.parse("CSEStream:= symbol[string], price [int]; \n" +
                                     "" +
                                     "StockQuote:= select symbol, avgPrice=avg(price), symbolCount=count(symbol) " +
                                     "from CSEStream[win.time=500] " +
                                     "where  7+(8*2)<price*2+price and symbol=='IBM' and symbol=='WSO2' ;");
        Assert.assertTrue(eventStreamList.size() == 2);
        Assert.assertTrue(((Query) eventStreamList.get(1)).getCondition() instanceof AndCondition);
        Assert.assertTrue(((AndCondition) ((Query) eventStreamList.get(1)).getCondition()).getLeftCondition() instanceof SimpleCondition);
        Assert.assertTrue(((AndCondition) ((Query) eventStreamList.get(1)).getCondition()).getRightCondition() instanceof AndCondition);

    }

    @Test
    public void QueryTestCase13() throws SiddhiPraserException {
        List<EventStream> eventStreamList = SiddhiCompiler.parse("cseEventStream:=symbol[string], price[int];");
        eventStreamList.add(SiddhiCompiler.parseSingleStream("StockQuote:=select symbol, price from cseEventStream where symbol=='IBM';", eventStreamList));
        Assert.assertTrue(eventStreamList.size() == 2);
    }

    @Test
    public void QueryTestCase14() throws SiddhiPraserException {
        List<EventStream> eventStreamList =
                SiddhiCompiler.parse("cseEventStream:=symbol[string], price[int];" +
                                     "StockQuote:=select symbol, avgPrice=avg(price) from cseEventStream where symbol=='IBM' having avgPrice>100;");
        Assert.assertTrue(eventStreamList.size() == 2);
        Assert.assertTrue(((Query) eventStreamList.get(1) ).hasCondition());
        Assert.assertTrue(((Query) eventStreamList.get(1) ).hasHaving());
        Assert.assertTrue(!((Query) eventStreamList.get(1) ).hasGroupBy());
    }

    @Test
    public void QueryTestCase15() throws SiddhiPraserException {
        List<EventStream> eventStreamList =
                SiddhiCompiler.parse("cseEventStream:=symbol[string], price[int];" +
                                     "StockQuote:=select symbol, avgPrice=avg(price) from cseEventStream where price > 1000 group by symbol having avgPrice>100 ;");
        Assert.assertTrue(eventStreamList.size() == 2);
        Assert.assertTrue(((Query) eventStreamList.get(1) ).hasCondition());
        Assert.assertTrue(((Query) eventStreamList.get(1) ).hasHaving());
        Assert.assertTrue(((Query) eventStreamList.get(1) ).hasGroupBy());
    }
    @Test
    public void QueryTestCase16() throws SiddhiPraserException {
        List<EventStream> eventStreamList =
                SiddhiCompiler.parse("cseEventStream:=symbol[string], price[int];" +
                                     "StockQuote:=select symbol, avgPrice=avg(price) from cseEventStream where price > 1000  having avgPrice>100 group by symbol ;");
        Assert.assertTrue(eventStreamList.size() == 2);
        Assert.assertTrue(((Query) eventStreamList.get(1) ).hasCondition());
        Assert.assertTrue(((Query) eventStreamList.get(1) ).hasHaving());
        Assert.assertTrue(!((Query) eventStreamList.get(1) ).hasGroupBy());//Note : having should follow group by
    }

    @Test
    public void QueryTestCase17() throws SiddhiPraserException {
        List<EventStream> eventStreamList =
                SiddhiCompiler.parse("cseEventStream:=symbol[string], price[int];" +
                                     "StockQuote:=select symbol, avgPrice=avg(price) from cseEventStream where price > 1000  group by symbol ;");
        Assert.assertTrue(eventStreamList.size() == 2);
        Assert.assertTrue(((Query) eventStreamList.get(1) ).hasCondition());
        Assert.assertTrue(!((Query) eventStreamList.get(1) ).hasHaving());
        Assert.assertTrue(((Query) eventStreamList.get(1) ).hasGroupBy());
    }

    @Test
    public void QueryTestCase18() throws SiddhiPraserException {
        List<EventStream> eventStreamList =
                SiddhiCompiler.parse("cseEventStream:=symbol[string], price[int];" +
                                     "StockQuote:=select symbol, avgPrice=avg(price) from cseEventStream having avgPrice>100;");
        Assert.assertTrue(eventStreamList.size() == 2);
        Assert.assertTrue(!((Query) eventStreamList.get(1) ).hasCondition());
        Assert.assertTrue(((Query) eventStreamList.get(1) ).hasHaving());
        Assert.assertTrue(!((Query) eventStreamList.get(1) ).hasGroupBy());
    }

    @Test
    public void QueryTestCase19() throws SiddhiPraserException {
        List<EventStream> eventStreamList =
                SiddhiCompiler.parse("cseEventStream:=symbol[string], price[int];" +
                                     "StockQuote:=select symbol, avgPrice=avg(price) from cseEventStream group by symbol having avgPrice>100 ;");
        Assert.assertTrue(eventStreamList.size() == 2);
        Assert.assertTrue(!((Query) eventStreamList.get(1) ).hasCondition());
        Assert.assertTrue(((Query) eventStreamList.get(1) ).hasHaving());
        Assert.assertTrue(((Query) eventStreamList.get(1) ).hasGroupBy());
    }
    @Test
    public void QueryTestCase20() throws SiddhiPraserException {
        List<EventStream> eventStreamList =
                SiddhiCompiler.parse("cseEventStream:=symbol[string], price[int];" +
                                     "StockQuote:=select symbol, avgPrice=avg(price) from cseEventStream having avgPrice>100 group by symbol ;");
        Assert.assertTrue(eventStreamList.size() == 2);
        Assert.assertTrue(!((Query) eventStreamList.get(1) ).hasCondition());
        Assert.assertTrue(((Query) eventStreamList.get(1) ).hasHaving());
        Assert.assertTrue(!((Query) eventStreamList.get(1) ).hasGroupBy());//Note : having should follow group by
    }

    @Test
    public void QueryTestCase21() throws SiddhiPraserException {
        List<EventStream> eventStreamList =
                SiddhiCompiler.parse("cseEventStream:=symbol[string], price[int];" +
                                     "StockQuote:=select symbol, avgPrice=avg(price) from cseEventStream  group by symbol ;");
        Assert.assertTrue(eventStreamList.size() == 2);
        Assert.assertTrue(!((Query) eventStreamList.get(1) ).hasCondition());
        Assert.assertTrue(!((Query) eventStreamList.get(1) ).hasHaving());
        Assert.assertTrue(((Query) eventStreamList.get(1) ).hasGroupBy());
    }

}
