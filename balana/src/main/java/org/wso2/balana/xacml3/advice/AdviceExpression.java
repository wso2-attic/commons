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

package org.wso2.balana.xacml3.advice;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.wso2.balana.*;
import org.wso2.balana.ctx.Result;
import org.wso2.balana.xacml3.AttributeAssignmentExpression;

import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Represents the AdviceExpressionType XML type in XACML.   // TODO
 */
public class AdviceExpression {

    private URI adviceId;

    private int appliesTo;

    private List attributeAssignmentExpressions;

    public AdviceExpression(URI adviceId, int appliesTo, List attributeAssignmentExpressions) {
        this.adviceId = adviceId;
        this.appliesTo = appliesTo;
        this.attributeAssignmentExpressions = attributeAssignmentExpressions;
    }

    public static AdviceExpression getInstance(Node root, PolicyMetaData metaData) throws ParsingException {
        URI adviceId;
        int appliesTo;
        List expressions = new ArrayList();
        String effect = null;
        NamedNodeMap attrs = root.getAttributes();

        try {
            adviceId = new URI(attrs.getNamedItem("AdviceId").getNodeValue());
        } catch (Exception e) {
            throw new ParsingException("Error parsing required attribute AdviceId", e);
        }

        try {
            effect = attrs.getNamedItem("AppliesTo").getNodeValue();
        } catch (Exception e) {
            throw new ParsingException("Error parsing required attribute AppliesTo", e);
        }

        if (effect.equals("Permit")) {
            appliesTo = Result.DECISION_PERMIT;
        } else if (effect.equals("Deny")) {
            appliesTo = Result.DECISION_DENY;
        } else {
            throw new ParsingException("Invalid Effect type: " + effect);
        }

        NodeList nodes = root.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeName().equals("AttributeAssignmentExpression")) {
                try {
                    AttributeAssignmentExpression expression = AttributeAssignmentExpression.
                            getInstance(node, metaData);
                    expressions.add(expression);
                } catch (Exception e) {
                    throw new ParsingException("Error parsing attribute " + "assignments", e);
                }
            }
        }

        return new AdviceExpression(adviceId, appliesTo, expressions);
    }

    public int getAppliesTo() {
        return appliesTo;
    }

    public URI getAdviceId() {
        return adviceId;
    }


    /**
     *
     * @return
     */
    public Advice evaluate(EvaluationCtx ctx) {
        return new Advice();
    }

    /**
     * Encodes this <code>AdviceExpression</code> into its XML form and writes this out to the provided
     * <code>OutputStream<code> with no indentation.
     *
     * @param output a stream into which the XML-encoded data is written
     */
    public void encode(OutputStream output) {
        encode(output, new Indenter(0));
    }

    /**
     * Encodes this <code>AdviceExpression</code> into its XML form and writes this out to the provided
     * <code>OutputStream<code> with indentation.
     *
     * @param output a stream into which the XML-encoded data is written
     * @param indenter an object that creates indentation strings
     */
    public void encode(OutputStream output, Indenter indenter) {
        PrintStream out = new PrintStream(output);
        String indent = indenter.makeString();

        out.println(indent + "<AdviceExpression AdviceId=\"" + adviceId.toString() + "\" AppliesTo=\""
                + Result.DECISIONS[appliesTo] + "\">");

        indenter.in();

        Iterator it = attributeAssignmentExpressions.iterator();

        while (it.hasNext()) {
//            Attribute attr = (Attribute) (it.next());
//            out.println(indenter.makeString() + "<AttributeAssignment AttributeId=\""
//                    + attr.getId().toString() + "\" DataType=\"" + attr.getType().toString()
//                    + "\">" + attr.getValue().encode() + "</AttributeAssignment>");
        }

        indenter.out();

        out.println(indent + "</AdviceExpression>");
    }
}
