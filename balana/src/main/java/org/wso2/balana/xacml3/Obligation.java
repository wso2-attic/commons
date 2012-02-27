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
import org.wso2.balana.ctx.Result;

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
     * effect that will cause this obligation to be included in a response
     */
    private int fulfillOn = -1;

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
     * @param root     TODO fulfillOn??
     * @return
     * @throws ParsingException
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
     *
     * @param output
     * @param indenter
     */
    public void encode(OutputStream output, Indenter indenter){
        // setup the formatting & outstream stuff
        String indent = indenter.makeString();
        PrintStream out = new PrintStream(output);

        // write out the encoded form
        out.println(indent + encode());
    }

    /**
     *
     * @param output
     * @return
     */
    public void encode(OutputStream output){
        encode(output, new Indenter(0));
    }

    /**
     *
     * @return
     */
    public String encode(){

        String encoded = "<Obligation ObligationId=";

        if(obligationId != null){
            encoded += obligationId ;
        } else {

        }

        if(fulfillOn != -1){
            encoded += " FulfillOn=" + Result.DECISIONS[fulfillOn] + ">";    
        }

        for(AttributeAssignment assignment : assignments){
            encoded += assignment.encode();
        }

        encoded += "</Obligation>";

        return encoded;
        
    }
}
