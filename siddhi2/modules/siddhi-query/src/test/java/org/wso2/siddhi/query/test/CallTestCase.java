package org.wso2.siddhi.query.test;

import org.antlr.runtime.RecognitionException;
import org.junit.Test;
import org.wso2.siddhi.query.compiler.SiddhiCompiler;
import org.wso2.siddhi.query.compiler.exception.SiddhiPraserException;

public class CallTestCase {

    @Test
    public void Test1() throws RecognitionException, SiddhiPraserException {
        SiddhiCompiler.parse("from QueryEvent\n" +
                             "insert into OutStream call sql(MyDb,\"select sembol, price,  vol from stockTable where symbol==${QueryEvent.userId} ;\")\n" +
                             "symbol , price , volume  ;");
    }



}
