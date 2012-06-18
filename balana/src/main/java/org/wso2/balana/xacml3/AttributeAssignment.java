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
import org.wso2.balana.Indenter;
import org.wso2.balana.ParsingException;
import org.wso2.balana.attr.AttributeFactory;
import org.wso2.balana.attr.AttributeValue;
import org.wso2.balana.xacml2.*;

import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.util.Iterator;

/**
 * Represents AttributeAssignmentType in the XACML 3.0 policy schema
 * This is used for including arguments in obligations and advices 
 */
public class AttributeAssignment {

    /**
     * attribute id of the AttributeAssignment  element
     */
    private URI attributeId;

    /**
     * category of the AttributeAssignment  element whether it is subject, action and etc
     */
    private URI category;

    /**
     *  issuer of the AttributeAssignment
     */
    private String issuer;

    /**
     * <code>AttributeValue</code> that contains in <code>AttributeAssignment</code>
     */
    private AttributeValue value;

    /**
     * Constructor that creates a new <code>AttributeAssignment</code> based on the given elements.
     * @param attributeId   attribute id of the AttributeAssignment  element
     * @param value <code>AttributeValue</code> that contains in <code>AttributeAssignment</code>
     * @param category category of the AttributeAssignment  element whether it is subject, action and etc
     * @param issuer issuer of the AttributeAssignment
     */
    public AttributeAssignment(URI attributeId, AttributeValue value, URI category, String issuer) {
        this.attributeId = attributeId;
        this.value = value;
        this.category = category;
        this.issuer = issuer;
    }


    /**
     * TODO   remove this method if possible
     * creates a <code>AttributeAssignment</code> based on its DOM node.
     *
     * @param root root the node to parse for the AttributeAssignment
     * @return a new <code>AttributeAssignment</code> constructed by parsing
     * @throws ParsingException if the DOM node is invalid
     */
    public static AttributeAssignment getInstance(Node root) throws ParsingException {

        URI attributeId;
        URI category = null;
        URI type;
        String issuer = null;
        AttributeValue value = null;
        AttributeFactory attrFactory = AttributeFactory.getInstance();

        if (!root.getNodeName().equals("AttributeAssignment")) {
            throw new ParsingException("AttributeAssignment object cannot be created "
                    + "with root node of type: " + root.getNodeName());
        }

        NamedNodeMap nodeAttributes = root.getAttributes();

        try {
            attributeId = new URI(nodeAttributes.getNamedItem("AttributeId").getNodeValue());
        } catch (Exception e) {
            throw new ParsingException("Error parsing required AttributeId in " +
                    "AttributeAssignmentType", e);
        }

        try {
            type = new URI(nodeAttributes.getNamedItem("DataType").getNodeValue());
        } catch (Exception e) {
            throw new ParsingException("Error parsing required AttributeId in " +
                    "AttributeAssignmentType", e);
        }

        try {
            Node categoryNode = nodeAttributes.getNamedItem("Category");
            if(categoryNode != null){
                category = new URI(categoryNode.getNodeValue());
            }

            Node issuerNode = nodeAttributes.getNamedItem("Issuer");
            if(issuerNode != null){
                issuer = issuerNode.getNodeValue();
            }
            value = attrFactory.createValue(root, type);
        } catch (Exception e) {
            throw new ParsingException("Error parsing optional attributes in " +
                    "AttributeAssignmentType", e);
        }

        return new AttributeAssignment(attributeId, value, category, issuer);
    }


    /**
     * Encodes this <code>AttributeAssignment</code> into its XML form and writes this out to the provided
     * <code>OutputStream<code> with indentation.
     *
     * @param output a stream into which the XML-encoded data is written
     * @param indenter an object that creates indentation strings
     */
    public void encode(OutputStream output, Indenter indenter) {

        PrintStream out = new PrintStream(output);
        String indent = indenter.makeString();

        out.print("<AttributeAssignment  AttributeId=\"" + attributeId + "\"");

        if(category != null){
            out.print(" Category=\"" + category + "\"");
        }

        if(issuer != null){
            out.print("\" Issuer=\"" + issuer + "\"");
        }

        out.println(">");

        indenter.in();

        out.println(value.encodeWithTags(true));
        
        indenter.out();

        out.println(indent + "</AttributeAssignment>");
    }

    /**
     * Encodes this <code>AttributeAssignment</code> into its XML form and writes this out to the provided
     * <code>OutputStream<code> with no indentation.
     *
     * @param output a stream into which the XML-encoded data is written
     */
    public void encode(OutputStream output){
        encode(output, new Indenter(0));
    }

}
