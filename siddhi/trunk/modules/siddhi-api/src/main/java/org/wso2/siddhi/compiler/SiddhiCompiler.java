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

package org.wso2.siddhi.compiler;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeNodeStream;
import org.wso2.siddhi.api.exception.SiddhiPraserException;
import org.wso2.siddhi.api.eventstream.EventStream;

import java.util.ArrayList;
import java.util.List;

public class SiddhiCompiler {

//    private static SiddhiCompiler compiler = null;
//
//    private SiddhiCompiler() {
//    }
//
//    /**
//     * get the SiddhiCompiler instance
//     *
//     * @return the instance of query factory
//     */
//    public static SiddhiCompiler getInstance() {
//        if (null == compiler) {
//            compiler = new SiddhiCompiler();
//        }
//        return compiler;
//    }

    public static List<EventStream> parse(String source) throws SiddhiPraserException {
        return parse(source, null);
    }

    public static List<EventStream> parse(String source, List<EventStream> existingStreams)
            throws SiddhiPraserException {
        try {
            if (existingStreams == null) {
                existingStreams = new ArrayList<EventStream>();
            }
            SiddhiGrammarLexer lexer = new SiddhiGrammarLexer();
            lexer.setCharStream(new ANTLRStringStream(source));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            SiddhiGrammarParser parser = new SiddhiGrammarParser(tokens);

            SiddhiGrammarParser.siddhiGrammar_return r = parser.siddhiGrammar();
            CommonTree t = (CommonTree) r.getTree();

            CommonTreeNodeStream nodes = new CommonTreeNodeStream(t);
            nodes.setTokenStream(tokens);
            SiddhiGrammarWalker walker = new SiddhiGrammarWalker(nodes);
            return walker.siddhiGrammar(existingStreams);

        } catch (Exception e) {
            throw new SiddhiPraserException(e.getMessage(), e);
        }
    }


}
