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
import org.wso2.balana.Indenter;
import org.wso2.balana.ObligationResult;
import org.wso2.balana.ParsingException;
import org.wso2.balana.ctx.AttributeAssignment;

import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the ObligationType XML type found in the context scheme in XACML 3.0
 */
public class Obligation implements ObligationResult{

    /**
     *  Identifier that uniquely identify the Obligation
     */
    private URI obligationId;

    /**
     * <code>List</code> of <code>AttributeAssignment</code> that contains in
     *  <code>Obligation</code>
     */
    private List<AttributeAssignment> assignments;

    /**
     * Constructor that creates a new <code>Obligation</code> based on
     * the given elements.
     *
     * @param assignments <code>List</code> of <code>AttributeAssignment</code>
     * @param obligationId  Identifier that uniquely identify the Obligation
     */
    public Obligation(List<AttributeAssignment> assignments, URI obligationId) {
        this.assignments = assignments;
        this.obligationId = obligationId;
    }

    /**
     * creates a <code>Obligation</code> based on its DOM node.
     *
     * @param root  the DOM root of the ObligationType XML type
     * @return  an instance of an obligation
     * @throws ParsingException  if the structure isn't valid
     */
    public static Obligation getInstance(Node root) throws ParsingException {

        URI obligationId;
        List<AttributeAssignment> assignments = new ArrayList<AttributeAssignment>();

        if (!root.getNodeName().equals("Obligation")) {
            throw new ParsingException("Obligation object cannot be created "
                    + "with root node of type: " + root.getNodeName());
        }

        NamedNodeMap nodeAttributes = root.getAttributes();

        try {
            obligationId = new URI(nodeAttributes.getNamedItem("ObligationId").getNodeValue());
        } catch (Exception e) {
            throw new ParsingException("Error parsing required ObligationId in " +
                    "ObligationType", e);
        }

        NodeList children = root.getChildNodes();

        for(int i = 0; i < children.getLength(); i ++){
            Node child = children.item(i);
            if("AttributeAssignment".equals(child.getNodeName())){
                assignments.add(AttributeAssignment.getInstance(child));
            }
        }

        return new Obligation(assignments, obligationId);
    }

    /**
     * Encodes this <code>Obligation</code> into its XML form and writes this out to the provided
     * <code>OutputStream<code> with indentation.
     *
     * @param output a stream into which the XML-encoded data is written
     * @param indenter an object that creates indentation strings
     */
    public void encode(OutputStream output, Indenter indenter){
        // setup the formatting & outstream stuff
        String indent = indenter.makeString();
        PrintStream out = new PrintStream(output);

        out.println(indent + "<Obligation ObligationId=\"" + obligationId + "\">");

        indenter.in();

        if(assignments != null && assignments.size() > 0){
            for(AttributeAssignment assignment : assignments){
                assignment.encode(output, indenter);
            }
        }

        indenter.out();

        out.println(indent + "</Obligation>");
    }

    /**
     * Encodes this <code>Obligation</code> into its XML form and writes this out to the provided
     * <code>OutputStream<code> with no indentation.
     *
     * @param output a stream into which the XML-encoded data is written
     */
    public void encode(OutputStream output){
        encode(output, new Indenter(0));
    }

}
