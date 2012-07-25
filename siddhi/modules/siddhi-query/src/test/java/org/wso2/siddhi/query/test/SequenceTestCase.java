package org.wso2.siddhi.query.test;

import junit.framework.Assert;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;
import org.wso2.siddhi.query.api.query.Query;
import org.wso2.siddhi.query.compiler.SiddhiCompiler;
import org.wso2.siddhi.query.compiler.exception.SiddhiPraserException;

public class SequenceTestCase {

    @Test
    public void Test1() throws RecognitionException, SiddhiPraserException {
        Query query = SiddhiCompiler.parseQuery("from  a1 = infoStock[action == \"buy\"]*,\n" +
                                                "          b1 = cseEventStream[price > 70]?,\n" +
                                                "          b2 = cseEventStream[price > 75]\n" +
                                                "insert into StockQuote\n" +
                                                "a1[0].action as action, b1.price as priceA, b2.price as priceBJoin");
        Assert.assertNotNull(query);
    }
  @Test
    public void Test2() throws RecognitionException, SiddhiPraserException {
        Query query = SiddhiCompiler.parseQuery("from  a1 = infoStock[action == \"buy\"]*,\n" +
                                                "          b1 = cseEventStream[price > 70]?,\n" +
                                                "          b2 = cseEventStream[price > 75]\n" +
                                                "within 2390 " +
                                                "insert into StockQuote\n" +
                                                "a1[0].action as action, b1.price as priceA, b2.price as priceBJoin");
        Assert.assertNotNull(query);
    }

//    from  a1 = infoStock[action == "buy"]*,
//                      b1 = cseEventStream[price > 70]?,
//                      b2 = cseEventStream[price > 75]
//            insert into StockQuote
//            a1[0].action as action, b1.price as priceA, b2.price as priceBJoin

}
