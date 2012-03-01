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
import org.wso2.siddhi.api.eventstream.EventStream;
import org.wso2.siddhi.api.eventstream.query.Query;
import org.wso2.siddhi.api.exception.SiddhiPraserException;
import org.wso2.siddhi.compiler.SiddhiCompiler;

import java.util.List;

public class SequenceQueryTestCase {


    @Test
    public void QueryTestCase1() throws SiddhiPraserException {
        List<EventStream> eventStreamList =
                SiddhiCompiler.parse("cseEventStream:=symbol[string], price[int];\n" +
                                     "StockQuote:=select priceA=$a2[last].price, priceA=$a2[0].price, priceB=$a3.price \n" +
                                     "from cseEventStream \n" +
                                     "sequence [a1=price<500, a2=price<500, a3=volume<150]\n" +
                                     "$a1 $a2* $a3");
        Assert.assertTrue(eventStreamList.size() == 2);
    }

    @Test
    public void QueryTestCase2() throws SiddhiPraserException {
        List<EventStream> eventStreamList =
                SiddhiCompiler.parse("CSEStream:= symbol[string], price [int]; \n" +
                                     "infoStock:= action[string],timeStamp[long]; \n" +
                                     "" +
                                     "StockQuote:=select priceA=$a1.price, priceA=$a1[0].price, priceB=$a1[last].price, action=$a2.action \n" +
                                     "from CSEStream, infoStock \n" +
                                     "sequence [a1=CSEStream.price > 70,\n" +
                                     "a2= infoStock.action == sell ] \n" +
                                     "$a1* $a2 ;");
        Assert.assertTrue(eventStreamList.size() == 3);
    }

    @Test
    public void QueryTestCase3() throws SiddhiPraserException {
        List<EventStream> eventStreamList =
                SiddhiCompiler.parse("StockQuoteStream:= timestamp[int], symbol [string], price [int], volume [int]; \n" +
                                     "" +
                                     "StockQuote:=select priceA=$a1[last].price, priceB=$a2.price \n" +
                                     "from StockQuoteStream \n" +
                                     "sequence [a1=StockQuoteStream.price>500,\n" +
                                     "a2=StockQuoteStream.price >= $this[prev].price, \n" +
                                     "a3=StockQuoteStream.price < $a2[last].price] \n" +
                                     "$a1 $a2* $a3 ;");
        Assert.assertTrue(eventStreamList.size() == 2);
    }
  @Test
    public void QueryTestCase4() throws SiddhiPraserException {
        List<EventStream> eventStreamList =
                SiddhiCompiler.parse("StockQuoteStream:= timestamp[int], symbol [string], price [int], volume [int]; \n" +
                                     "" +
                                     "StockQuote:=select priceA=$a1[last].price, priceB=$a2.price \n" +
                                     "from StockQuoteStream \n" +
                                     "sequence [a1=StockQuoteStream.price>500,\n" +
                                     "a2=StockQuoteStream.price >= $this[prev].price, \n" +
                                     "a3=StockQuoteStream.price < $a2[last].price] \n" +
                                     "$a1 $a2* $a3 " +
                                     "group by StockQuoteStream.symbol;");
        Assert.assertTrue(eventStreamList.size() == 2);
      Assert.assertTrue(((Query) eventStreamList.get(1) ).hasGroupBy());
    }


}
