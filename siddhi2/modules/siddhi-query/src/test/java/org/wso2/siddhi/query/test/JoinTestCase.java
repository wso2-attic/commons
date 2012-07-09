package org.wso2.siddhi.query.test;

import junit.framework.Assert;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;
import org.wso2.siddhi.query.api.query.Query;
import org.wso2.siddhi.query.compiler.SiddhiCompiler;
import org.wso2.siddhi.query.compiler.exception.SiddhiPraserException;

public class JoinTestCase {

    @Test
    public void Test1() throws RecognitionException, SiddhiPraserException {
     Query query=   SiddhiCompiler.parseQuery("from TickEvent as t unidirectional left outer join NewsEvent[std.unique(symbol)]as n \n" +
                             "    on t.symbol == n.symbol\n" +
                             "insert into JoinStream ;");
        Assert.assertNotNull(query);
    }

//    from TickEvent as t unidirectional left outer join NewsEvent[std.unique(symbol)]as n
//           on t.symbol == n.symbol
//           insert into JoinStream

    @Test
    public void Test2() throws RecognitionException, SiddhiPraserException {
        Query query=  SiddhiCompiler.parseQuery("from TickEvent[std.lastevent()] join NewsEvent[std.lastevent()]\n" +
                             "insert into JoinStream *");
        Assert.assertNotNull(query);
    }

}
