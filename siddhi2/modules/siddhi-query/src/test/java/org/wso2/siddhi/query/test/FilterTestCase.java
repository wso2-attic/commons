package org.wso2.siddhi.query.test;

import org.antlr.runtime.RecognitionException;
import org.junit.Assert;
import org.junit.Test;
import org.wso2.siddhi.query.api.query.Query;
import org.wso2.siddhi.query.compiler.SiddhiCompiler;
import org.wso2.siddhi.query.compiler.exception.SiddhiPraserException;

public class FilterTestCase {

    @Test
    public void Test() throws RecognitionException, SiddhiPraserException {
        Query query = SiddhiCompiler.parseQuery("from  cseEventStream [ price >= 20 ] [win.lenghtBatch(50)] [win.lenght(50)]  " +
                                                "insert into StockQuote symbol, avg(price) as avgPrice" +
                                                " group by symbol" +
                                                " having (price >= 20);"
        );
        Assert.assertNotNull(query);
    }

    @Test
    public void Test1() throws RecognitionException, SiddhiPraserException {
        Query query = SiddhiCompiler.parseQuery("from  cseEventStream [win.lenghtBatch(50)][price >= 20]" +
                                                "insert into StockQuote symbol, avg(price) as avgPrice" +
                                                " group by symbol" +
                                                " having avgPrice>50;");
        Assert.assertNotNull(query);
    }

    //    from cseEventStream[win.lenghtBatch(50)][price >= 20]
//            insert into StockQuote symbol, avg(price) as avgPrice
//            group by symbol
//            having avgPrice>50;
    @Test
    public void Test2() throws RecognitionException, SiddhiPraserException {
        SiddhiCompiler.parse("from  cseEventStream [win.length(50)][price >= 20]" +
                             "insert into StockQuote symbol, avg(price) as avgPricegroup by symbol" +
                                     "group by symbol" +
                             " " +
                             " having avgPrice>50;");
    }

}


