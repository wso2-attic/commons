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
package org.wso2.siddhi.api;

import org.junit.Assert;
import org.junit.Test;
import org.wso2.siddhi.api.eventstream.EventStream;
import org.wso2.siddhi.api.eventstream.query.Query;
import org.wso2.siddhi.api.exception.SiddhiPraserException;
import org.wso2.siddhi.compiler.SiddhiCompiler;

import java.util.List;

public class JoinQueryTestCase {

    @Test
    public void QueryTestCase1() throws SiddhiPraserException {
        List<EventStream> eventStreamList =
                SiddhiCompiler.parse("cseEventStream:=symbol[string], price[int];" +
                                     "infoStock:=action[string], timeStamp[long];" +
                                     "StockQuote:=select symbol=cseEventStream.symbol, price=cseEventStream.price, timeStamp=infoStock.timeStamp from cseEventStream,infoStock where cseEventStream.symbol==infoStock.action ;");
        Assert.assertTrue(eventStreamList.size() == 3);
        Assert.assertTrue(((Query) eventStreamList.get(2)).hasCondition());
        Assert.assertTrue(!((Query) eventStreamList.get(2)).hasHaving());
        Assert.assertTrue(!((Query) eventStreamList.get(2)).hasGroupBy());
    }

    @Test
    public void QueryTestCase2() throws SiddhiPraserException {
        List<EventStream> eventStreamList =
                SiddhiCompiler.parse("cseEventStream:=symbol[string], price[int];" +
                                     "infoStock:=action[string], timeStamp[long];" +
                                     "StockQuote:=select symbol=cseEventStream.symbol, price=cseEventStream.price, timeStamp=infoStock.timeStamp from cseEventStream,infoStock where cseEventStream.symbol==infoStock.action having price>100  ;");
        Assert.assertTrue(eventStreamList.size() == 3);
        Assert.assertTrue(((Query) eventStreamList.get(2)).hasCondition());
        Assert.assertTrue(((Query) eventStreamList.get(2)).hasHaving());
        Assert.assertTrue(!((Query) eventStreamList.get(2)).hasGroupBy());
    }

    @Test
    public void QueryTestCase3() throws SiddhiPraserException {
        List<EventStream> eventStreamList =
                SiddhiCompiler.parse("cseEventStream:=symbol[string], price[int];" +
                                     "infoStock:=action[string], timeStamp[long];" +
                                     "StockQuote:=select symbol=cseEventStream.symbol, price=cseEventStream.price, timeStamp=infoStock.timeStamp from cseEventStream,infoStock where cseEventStream.symbol==infoStock.action having price>100;");
        Assert.assertTrue(eventStreamList.size() == 3);
        Assert.assertTrue(((Query) eventStreamList.get(2)).hasCondition());
        Assert.assertTrue(((Query) eventStreamList.get(2)).hasHaving());
        Assert.assertTrue(!((Query) eventStreamList.get(2)).hasGroupBy());
    }

    @Test
    public void QueryTestCase4() throws SiddhiPraserException {
        List<EventStream> eventStreamList =
                SiddhiCompiler.parse("cseEventStream:=symbol[string], price[int];" +
                                     "infoStock:=action[string], timeStamp[long];" +
                                     "StockQuote:=select symbol=cseEventStream.symbol, avgPrice=avg(cseEventStream.price), timeStamp=infoStock.timeStamp from cseEventStream,infoStock where cseEventStream.symbol==infoStock.action;");
        Assert.assertTrue(eventStreamList.size() == 3);
        Assert.assertTrue(((Query) eventStreamList.get(2)).hasCondition());
        Assert.assertTrue(!((Query) eventStreamList.get(2)).hasHaving());
        Assert.assertTrue(!((Query) eventStreamList.get(2)).hasGroupBy());
    }

    @Test
    public void QueryTestCase5() throws SiddhiPraserException {
        List<EventStream> eventStreamList =
                SiddhiCompiler.parse("cseEventStream:=symbol[string], price[int];" +
                                     "infoStock:=action[string], timeStamp[long];" +
                                     "StockQuote:=select symbol=cseEventStream.symbol, avgPrice=avg(cseEventStream.price), timeStamp=infoStock.timeStamp from cseEventStream,infoStock where cseEventStream.symbol==infoStock.action group by cseEventStream.symbol;");
        Assert.assertTrue(eventStreamList.size() == 3);
        Assert.assertTrue(((Query) eventStreamList.get(2)).hasCondition());
        Assert.assertTrue(!((Query) eventStreamList.get(2)).hasHaving());
        Assert.assertTrue(((Query) eventStreamList.get(2)).hasGroupBy());
    }

    @Test
    public void QueryTestCase6() throws SiddhiPraserException {
        List<EventStream> eventStreamList =
                SiddhiCompiler.parse("cseEventStream:=symbol[string], price[int];" +
                                     "infoStock:=action[string], timeStamp[long];" +
                                     "StockQuote:=select symbol=cseEventStream.symbol, avgPrice=avg(cseEventStream.price), timeStamp=infoStock.timeStamp from cseEventStream,infoStock where cseEventStream.symbol==infoStock.action group by cseEventStream.symbol having avgPrice>100;");
        Assert.assertTrue(eventStreamList.size() == 3);
        Assert.assertTrue(((Query) eventStreamList.get(2)).hasCondition());
        Assert.assertTrue(((Query) eventStreamList.get(2)).hasHaving());
        Assert.assertTrue(((Query) eventStreamList.get(2)).hasGroupBy());
    }


}
