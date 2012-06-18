/*
*  Copyright (c) WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.balana.xacml3;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.wso2.balana.ParsingException;
import org.wso2.balana.ctx.EvaluationCtx;
import org.wso2.balana.Indenter;
import org.wso2.balana.ObligationResult;
import org.wso2.balana.ctx.AbstractResult;
import org.wso2.balana.ctx.Status;
import org.wso2.balana.xacml3.Obligation;
import org.wso2.balana.xacml3.advice.Advice;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * XACML 3 implementation of  <code>AbstractResult</code>
 */
public class Result extends AbstractResult{


    public Result(int decision, Status status, Set<ObligationResult> obligationResults, 
                  Set<Advice> advices, EvaluationCtx evaluationCtx) throws IllegalArgumentException {
        super(decision, status, obligationResults, advices, evaluationCtx);
    }

    public Result(int decision, Status status, Set<ObligationResult> obligationResults,
                  Set<Advice> advices) throws IllegalArgumentException {
        super(decision, status, obligationResults, advices, null);
    }
    /**
     * Creates a new instance of a <code>Result</code> based on the given
     * DOM root node. A <code>ParsingException</code> is thrown if the DOM
     * root doesn't represent a valid ResultType.
     *
     * @param root the DOM root of a ResultType
     *
     * @return a new <code>Result</code>
     *
     * @throws ParsingException if the node is invalid
     */
    public static AbstractResult getInstance(Node root) throws ParsingException {
        int decision = -1;
        Status status = null;
        Set<ObligationResult> obligations = null;
        Set<Advice> advices = null;

        NodeList nodes = root.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            String name = node.getNodeName();

            if (name.equals("Decision")) {
                String type = node.getFirstChild().getNodeValue();
                for (int j = 0; j < DECISIONS.length; j++) {
                    if (DECISIONS[j].equals(type)) {
                        decision = j;
                        break;
                    }
                }

                if (decision == -1)
                    throw new ParsingException("Unknown Decision: " + type);
            } else if (name.equals("Status")) {
                status = Status.getInstance(node);
            } else if (name.equals("Obligations")) {
                obligations = parseObligations(node);
            } else if (name.equals("Obligations")) {
                advices = parseAdvices(node);
            }
        }

        return new Result(decision, status, obligations, advices);
    }

    /**
     * Helper method that handles the obligations
     */
    private static Set parseObligations(Node root) throws ParsingException {
        Set<Obligation> set = new HashSet<Obligation>();

        NodeList nodes = root.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeName().equals("Obligation"))
                set.add(Obligation.getInstance(node));
        }

        if (set.size() == 0) {
            throw new ParsingException("ObligationsType must not be empty");
        }
        return set;
    }

    /**
     * Helper method that handles the Advices
     */
    private static Set parseAdvices(Node root) throws ParsingException {
        Set<Advice> set = new HashSet<Advice>();

        NodeList nodes = root.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeName().equals("Advices"));
                //set.add(Advice.getInstance(node));
        }

        if (set.size() == 0) {
            throw new ParsingException("ObligationsType must not be empty");
        }
        return set;
    }
    /**
     * Encodes this <code>Result</code> into its XML form and writes this out to the provided
     * <code>OutputStream<code> with indentation.
     *
     * @param output a stream into which the XML-encoded data is written
     * @param indenter an object that creates indentation strings
     */
    public void encode(OutputStream output, Indenter indenter) {
        PrintStream out = new PrintStream(output);
        String indent = indenter.makeString();

        indenter.in();
        String indentNext = indenter.makeString();
        out.println(indent + "<Result>");
        // encode the decision
        out.println(indentNext + "<Decision>" + DECISIONS[decision] + "</Decision>");

        // encode the status
        if (status != null)
            status.encode(output, indenter);

        // encode the obligations
        if (obligations != null  && obligations.size() != 0) {
            out.println(indentNext + "<Obligations>");

            Iterator it = obligations.iterator();
            indenter.in();

            while (it.hasNext()) {
                Obligation obligation = (Obligation) (it.next());
                obligation.encode(output, indenter);
            }

            indenter.out();
            out.println(indentNext + "</Obligations>");
        }

        indenter.out();

        // finish it off
        out.println(indent + "</Result>");
    }

}
