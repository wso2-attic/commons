package org.wso2.siddhi.query.test;

import junit.framework.Assert;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;
import org.wso2.siddhi.query.api.query.Query;
import org.wso2.siddhi.query.compiler.SiddhiCompiler;
import org.wso2.siddhi.query.compiler.exception.SiddhiPraserException;

public class PatternTestCase {

    @Test
    public void Test1() throws RecognitionException, SiddhiPraserException {
        Query query = SiddhiCompiler.parseQuery("from e1=Stream1[price >= 20] -> e2=Stream2[ price >= e1.price]\n" +
                                                "    insert into ABCStream e1.symbol, avg(e2.price ) as avgPrice\n" +
                                                "    group by e1.symbol\n" +
                                                "    having avgPrice>50;");
        Assert.assertNotNull(query);
    }

//    from e1=Stream1[price >= 20] -> e2=Stream2[ price >= e1.price]
//             insert into ABCStream e1.symbol, avg(e2.price ) as avgPrice
//             group by e1.symbol
//             having avgPrice>50;


    @Test
    public void Test2() throws RecognitionException, SiddhiPraserException {
        Query query = SiddhiCompiler.parseQuery("from every( a1 = infoStock0[action == 'buy'] or a2=infoStock1[action == 'buy'] ->  b1 = cseEventStream0[price > 70] )-> b2 = cseEventStream1[price > 75] " +
                                                " insert into StockQuote " +
                                                " a1.action as action, b1.price as priceA, b2.price as priceB ;"
        );
        Assert.assertNotNull(query);
    }

    @Test
    public void Test3() throws RecognitionException, SiddhiPraserException {
        Query query = SiddhiCompiler.parseQuery("from every a1 = infoStock[action == \"buy\"]<2:> ->\n" +
                                                "          b1 = cseEventStream[price > 70]<3> ->\n" +
                                                "          b2 = cseEventStream[price > 75]<1:4>\n" +
                                                " insert into StockQuote\n" +
                                                " a1.action as action, b1.price as priceA, b2.price as priceB");
        Assert.assertNotNull(query);
    }


    //    from every a1 = infoStock[action == "buy"][2:] ->
//                      b1 = cseEventStream[price > 70][timer.within(30)][3] ->
//                      b2 = cseEventStream[price > 75][1:4]
//             insert into StockQuote
//             a1.action as action, b1.price as priceA, b2.price as priceB
    @Test
    public void Test4() throws RecognitionException, SiddhiPraserException {
        Query query = SiddhiCompiler.parseQuery("from every a1 = infoStock[action == \"buy\"] or\n" +
                                                "    a2 = infoStock[action == \"buy\"] ->\n" +
                                                "          b1 = cseEventStream[price > 70] ->\n" +
                                                "          b2 = cseEventStream[price > 75]\n" +
                                                "insert into StockQuote\n" +
                                                "coalesce(a1.action, a2.action) as action,\n" +
                                                "b1.price as priceA,\n" +
                                                "b2.price as priceB");
        Assert.assertNotNull(query);
    }

    @Test
    public void Test5() throws RecognitionException, SiddhiPraserException {
        Query query = SiddhiCompiler.parseQuery("from every a1 = infoStock[action == \"buy\"] or\n" +
                                                "    a2 = infoStock[action == \"buy\"] ->\n" +
                                                "          b1 = cseEventStream[price > 70] ->\n" +
                                                "          b2 = cseEventStream[price > 75]\n" +
                                                " within 3000 " +
                                                "insert into StockQuote\n" +
                                                "coalesce(a1.action, a2.action) as action,\n" +
                                                "b1.price as priceA,\n" +
                                                "b2.price as priceB");
        Assert.assertNotNull(query);
    }


}
