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
import org.wso2.balana.ParsingException;
import org.wso2.balana.ctx.AttributeAssignment;

import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the AdviceType XML type in XACML. Advice are introduced with XACML 3
 */
public class Advice {

    /**
     * The value of the advice identifier may be interpreted by the PEP.
     */
    private URI adviceId;

    /**
     *  Advice arguments as a <code>List</code> of <code>AttributeAssignment</code>
     *  The values of the advice arguments may be interpreted by the PEP
     */
    private List<AttributeAssignment> assignments;

    /**
     * Constructor that creates a new <code>Advice</code> based on
     * the given elements.
     *
     * @param adviceId  Identifier that uniquely identify the Advice
     * @param assignments  <code>List</code> of <code>AttributeAssignment</code>
     */
    public Advice(URI adviceId, List<AttributeAssignment> assignments) {
        this.adviceId = adviceId;
        this.assignments = assignments;
    }

    

    /**
     * Encodes this <code>Advice</code> into its XML form and writes this out to the provided
     * <code>OutputStream<code> with no indentation.
     *
     * @param output a stream into which the XML-encoded data is written
     */
    public void encode(OutputStream output){
        encode(output, new Indenter(0));
    }

    /**
     * Creates a <code>Advice</code> based on its DOM node.
     *
     * @param root the DOM root of a AdviceType
     * @return  an instance of an advice
     * @throws ParsingException   if the structure isn't valid
     */
    public static Advice getInstance(Node root) throws ParsingException {

        URI adviceId;
        List<AttributeAssignment> assignments =  new ArrayList<AttributeAssignment>();

        if (!root.getNodeName().equals("Advice")) {
            throw new ParsingException("Advice object cannot be created "
                    + "with root node of type: " + root.getNodeName());
        }

        NamedNodeMap nodeAttributes = root.getAttributes();

        try {
            adviceId = new URI(nodeAttributes.getNamedItem("AdviceId").getNodeValue());
        } catch (Exception e) {
            throw new ParsingException("Error parsing required AdviceId in " +
                    "AdviceType", e);
        }

        NodeList children = root.getChildNodes();

        for(int i = 0; i < children.getLength(); i ++){
            Node child = children.item(i);
            if("AttributeAssignment".equals(child.getNodeName())){
                assignments.add(AttributeAssignment.getInstance(child));
            }
        }

        return new Advice(adviceId, assignments);
    }

    public URI getAdviceId() {
        return adviceId;
    }

    public List<AttributeAssignment> getAssignments() {
        return assignments;
    }

    /**
     * Encodes this <code>Advice</code> into its XML form and writes this out to the provided
     * <code>OutputStream<code> with indentation.
     *
     * @param output a stream into which the XML-encoded data is written
     * @param indenter an object that creates indentation strings
     */
    public void encode(OutputStream output, Indenter indenter){

        PrintStream out = new PrintStream(output);
        String indent = indenter.makeString();

        out.println(indent + "<Advice AdviceId=\"" + adviceId + "\" >");

        indenter.in();

        if(assignments != null && assignments.size() > 0){
            for(AttributeAssignment assignment : assignments){
                assignment.encode(output, indenter);
            }
        }
        
        indenter.out();        
        out.println(indent + "</Advice>");
    }
}
