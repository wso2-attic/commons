/*
 * @(#)RequestCtx.java
 *
 * Copyright 2003-2004 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   1. Redistribution of source code must retain the above copyright notice,
 *      this list of conditions and the following disclaimer.
 * 
 *   2. Redistribution in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *
 * Neither the name of Sun Microsystems, Inc. or the names of contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 * ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN MICROSYSTEMS, INC. ("SUN")
 * AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE
 * AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL,
 * INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY
 * OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE,
 * EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that this software is not designed or intended for use in
 * the design, construction, operation or maintenance of any nuclear facility.
 */

package org.wso2.balana.xacml3.ctx;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.wso2.balana.Indenter;
import org.wso2.balana.ParsingException;
import org.wso2.balana.XACMLConstants;
import org.wso2.balana.ctx.*;
import org.wso2.balana.xacml3.Attributes;
import org.wso2.balana.xacml3.MultiRequests;
import org.wso2.balana.xacml3.RequestDefaults;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Represents a XACML3 request made to the PDP. This is the class that contains all the data used to start
 * a policy evaluation.
 *
 * @since 1.0
 * @author Seth Proctor
 * @author Marco Barreno
 */
public class RequestCtx extends AbstractRequestCtx {

    /**
     * define boolean value whether to send back the applicable policies to PEP or not
     */
    private boolean returnPolicyIdList;

    /**
     * uses for when multiple decisions is enabled in PDP. This is defined whether to combine
     * multiple decisions or not
     */
    private boolean combinedDecision;

    /**
     * lists multiple request contexts by references to the <Attributes> elements
     */
    private MultiRequests multiRequests;

    /**
     * contains default values for the request, such as XPath version.
     */
    private RequestDefaults defaults;


    /**
     * XACML3 attributes as <code>Attributes</code> objects
     */
    private Set<Attributes> attributesSet = null;


    /**
     * Constructor that creates a <code>RequestCtx</code> from components.
     *
     * @param documentRoot the root node of the DOM tree for this request
     * @param attributesSet a <code>Set</code> of <code>Attributes</code>s
     * @throws IllegalArgumentException if the inputs are not well formed
     */
    public RequestCtx(Node documentRoot, Set<Attributes> attributesSet) {
        this(documentRoot, attributesSet, false, false, null, null);
    }

    /**
     * Constructor that creates a <code>RequestCtx</code> from components.
     *
     * @param documentRoot the root node of the DOM tree for this request
     * @param attributesSet a <code>Set</code> of <code>Attributes</code>s
     * @param returnPolicyIdList a <code>boolean</code> value whether to send back policy list of not
     * @param combinedDecision  a <code>boolean</code> value whether to combine decisions or not
     * @param multiRequests a <code>MultiRequests</code> for the  MultiRequests element in request
     * @param defaults  a <code>RequestDefaults</code>  for the  RequestDefaults element in request
     * @throws IllegalArgumentException if the inputs are not well formed
     */
    public RequestCtx(Node documentRoot, Set<Attributes> attributesSet, boolean returnPolicyIdList,
                      boolean combinedDecision, MultiRequests multiRequests,
                      RequestDefaults defaults) throws IllegalArgumentException {

        Set<Subject> newSubjects = new HashSet<Subject>();
        Set<Attribute> newResource = null;
        Set<Attribute> newAction = null;
        Set<Attribute> newEnvironment = null;

        for(Attributes attributesElement : attributesSet){
            Set<Attribute> attributeElements = attributesElement.getAttributes();
            if(XACMLConstants.SUBJECT_CATEGORY.equals(attributesElement.getCategory().toString())){
                newSubjects.add(new Subject(attributeElements));
            } else if(XACMLConstants.RESOURCE_CATEGORY.equals(attributesElement.getCategory().toString())){
                newResource = attributeElements;
            } else if(XACMLConstants.ACTION_CATEGORY.equals(attributesElement.getCategory().toString())){
                newAction = attributeElements;
            } else if(XACMLConstants.ENT_CATEGORY.equals(attributesElement.getCategory().toString())){
                newEnvironment = attributeElements;
            }
        }

        // make sure subjects is well formed
        Iterator sIter = subjects.iterator();
        while (sIter.hasNext()) {
            if (!(sIter.next() instanceof Subject))
                throw new IllegalArgumentException("Subjects input is not " + "well formed");
        }
        this.subjects = Collections.unmodifiableSet(new HashSet<Subject>(newSubjects));

        // make sure resource is well formed
        Iterator rIter = resource.iterator();
        while (rIter.hasNext()) {
            if (!(rIter.next() instanceof Attribute))
                throw new IllegalArgumentException("Resource input is not " + "well formed");
        }
        this.resource = Collections.unmodifiableSet(new HashSet<Attribute>(newResource));

        // make sure action is well formed
        Iterator aIter = action.iterator();
        while (aIter.hasNext()) {
            if (!(aIter.next() instanceof Attribute))
                throw new IllegalArgumentException("Action input is not " + "well formed");
        }
        this.action = Collections.unmodifiableSet(new HashSet<Attribute>(newAction));

        // make sure environment is well formed
        Iterator eIter = environment.iterator();
        while (eIter.hasNext()) {
            if (!(eIter.next() instanceof Attribute))
                throw new IllegalArgumentException("Environment input is not" + " well formed");
        }
        this.environment = Collections.unmodifiableSet(new HashSet<Attribute>(newEnvironment));

        this.documentRoot = documentRoot;
        this.attributesSet = attributesSet;
        this.returnPolicyIdList = returnPolicyIdList;
        this.combinedDecision = combinedDecision;
    }

    /**
     * Create a new <code>RequestCtx</code> by parsing a node. This node should be created by
     * schema-verified parsing of an <code>XML</code> document.
     *
     * @param root the node to parse for the <code>RequestCtx</code>
     *
     * @return a new <code>RequestCtx</code> constructed by parsing
     *
     * @throws org.wso2.balana.ParsingException if the DOM node is invalid
     */
    public static RequestCtx getInstance(Node root) throws ParsingException {


        Set<Attributes> attributesElements;
        boolean returnPolicyIdList = false;
        boolean combinedDecision = false;
        MultiRequests multiRequests = null;
        RequestDefaults defaults = null;

        // First check to be sure the node passed is indeed a Request node.
        String tagName = root.getNodeName();
        if (!tagName.equals("Request")) {
            throw new ParsingException("Request cannot be constructed using " + "type: "
                    + root.getNodeName());
        }

        NamedNodeMap attrs = root.getAttributes();
        try {
            String attributeValue = attrs.getNamedItem(XACMLConstants.RETURN_POLICY_LIST).
                    getNodeValue();
            if("true".equals(attributeValue)){
                returnPolicyIdList = true;
            }
        } catch (Exception e) {
            throw new ParsingException("Error parsing required attribute "
                    + "ReturnPolicyIdList in RequestType", e);
        }

        try {
            String attributeValue = attrs.getNamedItem(XACMLConstants.COMBINE_DECISION).
                    getNodeValue();
            if("true".equals(attributeValue)){
                combinedDecision = true;
            }
        } catch (Exception e) {
            throw new ParsingException("Error parsing required attribute "
                    + "CombinedDecision in RequestType", e);
        }

        attributesElements = new HashSet<Attributes>();
        NodeList children = root.getChildNodes();

        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            String tag = node.getNodeName();
            if (tag.equals(XACMLConstants.ATTRIBUTES_ELEMENT)) {
                Attributes attributes = Attributes.getInstance(node);
                attributesElements.add(attributes);
            }

            if(tag.equals(XACMLConstants.MULTI_REQUESTS)){
                if (multiRequests != null){
                    throw new ParsingException("Too many MultiRequests elements are defined.");
                }
                multiRequests = MultiRequests.getInstance(node);
            }
            
            if(tag.equals(XACMLConstants.REQUEST_DEFAULTS)){
                if (multiRequests != null){
                    throw new ParsingException("Too many RequestDefaults elements are defined.");
                }
                defaults = RequestDefaults.getInstance(node);
            }
        }

        if(attributesElements.isEmpty()){
            throw new ParsingException("Request must contain at least one AttributesType");            
        }

        return new RequestCtx(root, attributesElements, returnPolicyIdList, combinedDecision,
                multiRequests, defaults);
    }

    /*
     * Helper method that parses a set of Attribute types. The Subject, Action and Environment
     * sections all look like this.
     */
    private static Set<Attribute> parseAttributes(Node root) throws ParsingException {
        Set<Attribute> set = new HashSet<Attribute>();

        // the Environment section is just a list of Attributes
        NodeList nodes = root.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeName().equals("Attribute"))
                set.add(Attribute.getInstance(node));
        }

        return set;
    }

    /**
     * Creates a new <code>RequestCtx</code> by parsing XML from an input stream. Note that this a
     * convenience method, and it will not do schema validation by default. You should be parsing
     * the data yourself, and then providing the root node to the other <code>getInstance</code>
     * method. If you use this convenience method, you probably want to turn on validation by
     * setting the context schema file (see the programmer guide for more information on this).
     *
     * @param input a stream providing the XML data
     *
     * @return a new <code>RequestCtx</code>
     *
     * @throws <code>ParserException</Code> if there is an error parsing the input
     */
    public static RequestCtx getInstance(InputStream input) throws ParsingException {
        return getInstance(InputParser.parseInput(input, "Request"));
    }


    /**
     * Encodes this context into its XML representation and writes this encoding to the given
     * <code>OutputStream</code>. No indentation is used.
     *
     * @param output a stream into which the XML-encoded data is written
     */
    public void encode(OutputStream output) {
        encode(output, new Indenter(0));
    }

    /**
     * Encodes this context into its XML representation and writes this encoding to the given
     * <code>OutputStream</code> with indentation.
     *
     * @param output a stream into which the XML-encoded data is written
     * @param indenter an object that creates indentation strings
     */
    public void encode(OutputStream output, Indenter indenter) {

        // Make a PrintStream for a nicer printing interface
        PrintStream out = new PrintStream(output);

        // Prepare the indentation string
        String topIndent = indenter.makeString();
        out.println(topIndent + "<Request>");

        // go in one more for next-level elements...
        indenter.in();
        String indent = indenter.makeString();

        // ...and go in again for everything else
        indenter.in();


        indenter.out();
        indenter.out();

        out.println(topIndent + "</Request>");
    }

    /**
     * Private helper function to encode the attribute sets
     */
    private void encodeAttributes(Set attributes, PrintStream out, Indenter indenter) {
        Iterator it = attributes.iterator();
        while (it.hasNext()) {
            Attribute attr = (Attribute) (it.next());
            attr.encode(out, indenter);
        }
    }

    /**
     *  Returns a <code>Set</code> containing <code>Attribute</code> objects.
     *
     * @return  the request' s all attributes as <code>Set</code>
     */
    public Set<Attributes> getAttributesSet() {
        return attributesSet;
    }

    /**
     * Returns a <code>boolean</code> value whether to combine decisions or not
     *
     * @return  true of false
     */
    public boolean isCombinedDecision() {
        return combinedDecision;
    }

    /**
     * Returns a <code>boolean</code> value whether to send back policy list of not
     *
     * @return  true or false
     */
    public boolean isReturnPolicyIdList() {
        return returnPolicyIdList;
    }

    /**
     * Returns a <code>MultiRequests</code> that encapsulates MultiRequests element in request
     *
     * @return MultiRequests element in request
     */
    public MultiRequests getMultiRequests() {
        return multiRequests;
    }

    /**
     * Returns a <code>RequestDefaults</code> that encapsulates RequestDefaults element in request
     *
     * @return  RequestDefaults element in request
     */
    public RequestDefaults getDefaults() {
        return defaults;
    }
}