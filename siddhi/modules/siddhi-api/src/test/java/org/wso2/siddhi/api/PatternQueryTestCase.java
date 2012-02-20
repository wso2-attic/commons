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
import org.wso2.siddhi.api.condition.pattern.FollowedByCondition;
import org.wso2.siddhi.api.eventstream.EventStream;
import org.wso2.siddhi.api.eventstream.query.Query;
import org.wso2.siddhi.api.exception.SiddhiPraserException;
import org.wso2.siddhi.compiler.SiddhiCompiler;

import java.util.List;

public class PatternQueryTestCase {

    @Test
    public void Query1estCase1() throws SiddhiPraserException {
        List<EventStream> eventStreamList =
                SiddhiCompiler.parse("cseEventStream:=symbol[string], price[int];\n" +
                                     "infoStock:=action[string], timeStamp[long];\n" +
                                     "StockQuote:=select action=$a1.action, priceA=$b1.price, priceA=$b2.price\n" +
                                     "from cseEventStream, infoStock\n" +
                                     "pattern [a1=infoStock.action==\"buy\",\n" +
                                     "b1=cseEventStream.price>70,\n" +
                                     "b2=cseEventStream.price>75]\n" +
                                     "$a1 -> every($b1 -> $b2) ;");
        Assert.assertTrue(eventStreamList.size() == 3);
    }

    @Test
    public void Query1estCase2() throws SiddhiPraserException {
        List<EventStream> eventStreamList =
                SiddhiCompiler.parse("cseEventStream:=symbol[string], price[int];\n" +
                                     "infoStock:=action[string], timeStamp[long];\n" +
                                     "StockQuote:=select action=$a1.action, priceA=$b1.price, priceA=$b2.price\n" +
                                     "from cseEventStream, infoStock\n" +
                                     "pattern [a1=infoStock.action==\"buy\",\n" +
                                     "b1=cseEventStream.price>70,\n" +
                                     "b2=cseEventStream.price>75]\n" +
                                     "$a1 -> $b1[within.time=30] -> $b2 ;");
        Assert.assertTrue(eventStreamList.size() == 3);
    }

    @Test
    public void Query1estCase3() throws SiddhiPraserException {
        List<EventStream> eventStreamList =
                SiddhiCompiler.parse("cseEventStream:=symbol[string], price[int];\n" +
                                     "infoStock:=action[string], timeStamp[long];\n" +

                                     "StockQuote:=select action=$a1.action, priceA=$a2.price\n" +
                                     "from cseEventStream, infoStock\n" +
                                     "pattern [a1=infoStock.action==\"buy\",\n" +
                                     "a2=cseEventStream.price==125,\n" +
                                     "a3=cseEventStream.price==75]\n" +
                                     "$a1 -> ! $a3 -> $a2;");
        Assert.assertTrue(eventStreamList.size() == 3);
        Assert.assertTrue (((FollowedByCondition)((Query)eventStreamList.get(2)).getCondition()).getFollowingConditions().size()==2);
    }

    @Test
    public void Query1estCase4() throws SiddhiPraserException {
        List<EventStream> eventStreamList =
                SiddhiCompiler.parse("CSEStream:= symbol[string], price [int]; \n" +
                                     "infoStock:= action[string], timeStamp[long]; \n" +
                                     "" +
                                     "StockQuote:=select action=$a1.action,timeStamp=$a1.timeStamp, priceA=$b3.price \n" +
                                     "from CSEStream, infoStock\n" +
                                     "pattern [a1=infoStock.action==\"buy\",\n" +
                                     "b1=CSEStream.price==75,\n" +
                                     "b2=infoStock.action==\"cancel\",\n" +
                                     "b3= CSEStream.price ==125  ]\n" +
                                     "$a1 -> ! $b1 -> !$b2-> $b3 ;");
        Assert.assertTrue(eventStreamList.size() == 3);
    }
}
