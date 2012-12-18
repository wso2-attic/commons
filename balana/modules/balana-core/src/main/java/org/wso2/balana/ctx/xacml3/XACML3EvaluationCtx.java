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

package org.wso2.balana.ctx.xacml3;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.wso2.balana.*;
import org.wso2.balana.attr.*;
import org.wso2.balana.attr.xacml3.XPathAttribute;
import org.wso2.balana.cond.EvaluationResult;
import org.wso2.balana.ctx.*;
import org.wso2.balana.finder.ResourceFinderResult;
import org.wso2.balana.xacml3.*;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.net.URI;
import java.util.*;

/**
 * This is implementation of XACML3 evaluation context
 *
 */
public class XACML3EvaluationCtx extends BasicEvaluationCtx {

    /**
     * Attributes set of the request
     */
    private Set<Attributes> attributesSet;

    /**
     * multiple content selectors.
     */
    private Set<Attributes> multipleContentSelectors;
    
    /**
     * whether multiple attributes are present or not
     */
    private boolean multipleAttributes;

    /**
     * Set of policy references
     */
    private Set<PolicyReference> policyReferences;

    /**
     * attributes categorized as Map
     *
     * Category  --> Attributes Set
     */
    private Map<String, Set<Attributes>> mapAttributes;

    /**
     * XACML3 request
     */
    private RequestCtx requestCtx;

    /**
     * XACML3 request scope. used with hierarchical resource
     */
    private int resourceScope;

    /**
     * XACML 3 request resource id.  used with hierarchical resource
     */
    private AttributeValue resourceId;

    /**
     * logger
     */
    private static Log logger = LogFactory.getLog(XACML3EvaluationCtx.class);

    /**
     * Creates a new <code>XACML3EvaluationCtx</code>
     *
     * @param requestCtx  XACML3  RequestCtx
     * @param pdpConfig PDP configurations
     */
    public XACML3EvaluationCtx(RequestCtx requestCtx, PDPConfig pdpConfig) {

        // initialize the cached date/time values so it's clear we haven't
        // retrieved them yet
        currentDate = null;
        currentTime = null;
        currentDateTime = null;

        mapAttributes = new HashMap<String, Set<Attributes>> ();

        attributesSet = requestCtx.getAttributesSet();
        this.pdpConfig = pdpConfig;
        this.requestCtx = requestCtx;

        setupAttributes(attributesSet, mapAttributes);
    }

    public EvaluationResult getAttribute(URI type, URI id, String issuer, URI category) {

        List<AttributeValue> attributeValues = new ArrayList<AttributeValue>();
        Set<Attributes> attributesSet = mapAttributes.get(category.toString());
        if(attributesSet != null && attributesSet.size() > 0){
            Set<Attribute> attributeSet  = attributesSet.iterator().next().getAttributes();
            for(Attribute attribute : attributeSet) {
                if(attribute.getId().equals(id) && attribute.getType().equals(type)
                        && (issuer == null || issuer.equals(attribute.getIssuer()))
                        && attribute.getValue() != null){
                    List<AttributeValue> attributeValueList = attribute.getValues();
                    for (AttributeValue attributeVal : attributeValueList) {
                    	attributeValues.add(attributeVal);
                    }
                }
            }

            if(attributeValues.size() < 1){
                return callHelper(type, id, issuer, category);
            }
        }
        // if we got here, then we found at least one useful AttributeValue
        return new EvaluationResult(new BagAttribute(type, attributeValues));
    }


    public EvaluationResult getAttribute(String path, URI type, URI category,
                                         URI contextSelector, String xpathVersion){

        if(pdpConfig.getAttributeFinder() == null){

            logger.warn("Context tried to invoke AttributeFinder but was " +
                           "not configured with one");
            return new EvaluationResult(BagAttribute.createEmptyBag(type));
        }

        Set<Attributes> attributesSet = null;

        if(category != null){
            attributesSet = mapAttributes.get(category.toString());
        }

        if(attributesSet != null && attributesSet.size() > 0){
            Attributes attributes  = attributesSet.iterator().next();
            Object content = attributes.getContent();
            if(content instanceof Node){
                Node root = (Node) content;
                if(contextSelector != null && contextSelector.toString().trim().length() > 0){
                    for(Attribute attribute : attributes.getAttributes()) {
                        if(attribute.getId().equals(contextSelector)){
                            List<AttributeValue> values = attribute.getValues();
                            for(AttributeValue value : values){
                                if(value instanceof XPathAttribute){
                                    XPathAttribute xPathAttribute = (XPathAttribute)value;
                                    if(xPathAttribute.getXPathCategory().
                                                                    equals(category.toString())){
                                        return pdpConfig.getAttributeFinder().findAttribute(path,
                                                            xPathAttribute.getValue(), type,
                                                            root, this, xpathVersion);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    return pdpConfig.getAttributeFinder().findAttribute(path, null, type,
                                                         root, this, xpathVersion);
                }
            }
        }

        return new EvaluationResult(BagAttribute.createEmptyBag(type));
    }


    public int getXacmlVersion() {
        return requestCtx.getXacmlVersion();
    }


    /**
     * Generic routine for resource, attribute and environment attributes to build the lookup map
     * for each. The Form is a Map that is indexed by the String form of the category ids, and that
     * contains Sets at each entry with all attributes that have that id
     *
     * @param attributeSet 
     * @param mapAttributes
     * @return
     */
    private void setupAttributes(Set<Attributes> attributeSet, Map<String,
                                            Set<Attributes>> mapAttributes)  {

        for (Attributes attributes : attributeSet) {
            String category = attributes.getCategory().toString();


            for(Attribute attribute : attributes.getAttributes()){
                if(XACMLConstants.RESOURCE_CATEGORY.equals(category)){
                    if(XACMLConstants.RESOURCE_SCOPE_2_0.equals(attribute.getId().toString())){
                        AttributeValue value = attribute.getValue();
                        if (value instanceof StringAttribute) {
                            String scope = ((StringAttribute) value).getValue();
                            if (scope.equals("Children")) {
                                resourceScope = XACMLConstants.SCOPE_CHILDREN;
                            } else if (scope.equals("Descendants")) {
                                resourceScope = XACMLConstants.SCOPE_DESCENDANTS;
                            }
                        } else {
                            logger.error("scope attribute must be a string");     //TODO
                            //throw new ParsingException("scope attribute must be a string");
                        }
                        break;
                    } else if (XACMLConstants.RESOURCE_ID.equals(attribute.getId().toString())){
                        if(resourceId == null) { //TODO  when there are more than one resource ids??
                            resourceId = attribute.getValue();
                        }
                    }
                }

                if(attribute.getId().toString().equals(XACMLConstants.MULTIPLE_CONTENT_SELECTOR)){
                    if(multipleContentSelectors == null){
                        multipleContentSelectors = new HashSet<Attributes>();
                    }
                    multipleContentSelectors.add(attributes);
                }
            }

            if (mapAttributes.containsKey(category)) {
                Set<Attributes> set = mapAttributes.get(category);
                set.add(attributes);
                multipleAttributes = true;
             } else {
                Set<Attributes> set = new HashSet<Attributes>();
                set.add(attributes);
                mapAttributes.put(category, set);
            }
        }
    }


    /**
     * get multiple <code>EvaluationCtx</code> if, request is combination of multiple requests
     *
     * @return <code>MultipleCtxResult</code> result that contains
     *  multiple <code>EvaluationCtx</code>
     */
    public MultipleCtxResult getMultipleEvaluationCtx()  {

        Set<EvaluationCtx> evaluationCtxSet = new HashSet<EvaluationCtx>();
        MultiRequests multiRequests =  requestCtx.getMultiRequests();

        // 1st check whether there is a multi request attribute
        if(multiRequests != null){

            MultipleCtxResult result = processMultiRequestElement(this);
            if(result.isIndeterminate()){
                return result;
            } else {
                evaluationCtxSet.addAll(result.getEvaluationCtxSet());
            }
        }

        // 2nd check repeated values for category attribute
        if(evaluationCtxSet.size() > 0){
            Set<EvaluationCtx> newSet = new HashSet<EvaluationCtx>(evaluationCtxSet);
            for(EvaluationCtx evaluationCtx : newSet){
                if(multipleAttributes){
                    evaluationCtxSet.remove(evaluationCtx);
                    MultipleCtxResult result = processMultipleAttributes((XACML3EvaluationCtx)evaluationCtx);
                    if(result.isIndeterminate()){
                        return result;
                    } else {
                        evaluationCtxSet.addAll(result.getEvaluationCtxSet());
                    }
                }
            }
        } else {
            if(multipleAttributes){
                MultipleCtxResult result = processMultipleAttributes(this);
                if(result.isIndeterminate()){
                    return result;
                } else {
                    evaluationCtxSet.addAll(result.getEvaluationCtxSet());
                }
            }
        }

        // 3rd check for both scope and multiple-content-selector attributes. Spec does not mention
        // which one to pick when both are present, there for high priority has given to scope
        // attribute
        if(evaluationCtxSet.size() > 0){
            Set<EvaluationCtx> newSet = new HashSet<EvaluationCtx>(evaluationCtxSet);
            for(EvaluationCtx evaluationCtx : newSet){
                if(resourceScope != XACMLConstants.SCOPE_IMMEDIATE){
                    evaluationCtxSet.remove(evaluationCtx);
                    MultipleCtxResult result = processHierarchicalAttributes((XACML3EvaluationCtx)evaluationCtx);
                    if(result.isIndeterminate()){
                        return result;
                    } else {
                        evaluationCtxSet.addAll(result.getEvaluationCtxSet());
                    }
                } else if(((XACML3EvaluationCtx)evaluationCtx).getMultipleContentSelectors() != null){
                    MultipleCtxResult result = processMultipleContentSelectors((XACML3EvaluationCtx)evaluationCtx);
                    if(result.isIndeterminate()){
                        return result;
                    } else {
                        evaluationCtxSet.addAll(result.getEvaluationCtxSet());
                    }
                }
            }
        } else {
            if(resourceScope != XACMLConstants.SCOPE_IMMEDIATE){
                MultipleCtxResult result = processHierarchicalAttributes(this);
                if(result.isIndeterminate()){
                    return result;
                } else {
                    evaluationCtxSet.addAll(result.getEvaluationCtxSet());
                }
            } else if(multipleContentSelectors != null){
                MultipleCtxResult result = processMultipleContentSelectors(this);
                if(result.isIndeterminate()){
                    return result;
                } else {
                    evaluationCtxSet.addAll(result.getEvaluationCtxSet());
                }
            }
        }

        if(evaluationCtxSet.size() > 0){
            return new MultipleCtxResult(evaluationCtxSet);
        } else {
            evaluationCtxSet.add(this);
            return new MultipleCtxResult(evaluationCtxSet);
        }
    }

    /**
     *
     * @param evaluationCtx
     * @return
     */
    private MultipleCtxResult processMultiRequestElement(XACML3EvaluationCtx evaluationCtx)  {

        Set<EvaluationCtx> children = new HashSet<EvaluationCtx>();
        MultiRequests multiRequests =  requestCtx.getMultiRequests();

        if(multiRequests != null){
            Set<RequestReference> requestReferences =  multiRequests.getRequestReferences();
            for(RequestReference reference :  requestReferences) {
                Set<AttributesReference>  attributesReferences = reference.getReferences();
                if(attributesReferences != null && attributesReferences.size() > 0){
                    Set<Attributes> attributes = new HashSet<Attributes>();
                    for(AttributesReference attributesReference : attributesReferences){
                        String referenceId = attributesReference.getId();
                        if(referenceId != null){
                            for(Attributes attribute : evaluationCtx.getAttributesSet()){
                                if(attribute.getId() != null && attribute.getId().equals(referenceId)){
                                    attributes.add(attribute);
                                }
                            }
                        }
                        RequestCtx ctx = new RequestCtx(attributes, null);
                        children.add(new XACML3EvaluationCtx(ctx, pdpConfig));
                    }
                }
            }
        }

        return new MultipleCtxResult(children);
    }

    /**
     *
     * @param evaluationCtx
     * @return
     */
    private MultipleCtxResult processMultipleAttributes(XACML3EvaluationCtx evaluationCtx) {

        Set<EvaluationCtx> children = new HashSet<EvaluationCtx>();
        Set<RequestCtx> newRequestCtxSet = new HashSet<RequestCtx>();

        Map<String, Set<Attributes>> mapAttributes = evaluationCtx.getMapAttributes();

        for(Map.Entry<String, Set<Attributes>> mapAttributesEntry : mapAttributes.entrySet()){
            if(mapAttributesEntry.getValue().size() > 1){
                for(Attributes attributesElement :  mapAttributesEntry.getValue()){
                    Set<Attributes> newSet = new HashSet<Attributes>(evaluationCtx.getAttributesSet());
                    newSet.removeAll(mapAttributesEntry.getValue());
                    newSet.add(attributesElement);
                    RequestCtx newRequestCtx = new RequestCtx(newSet, null);
                    newRequestCtxSet.add(newRequestCtx);
                }
            }
        }

        for(RequestCtx ctx : newRequestCtxSet){
            children.add(new XACML3EvaluationCtx(ctx, pdpConfig));
        }

        return new MultipleCtxResult(children);

    }

    /**
     *
     * @param evaluationCtx
     * @return
     */
    private MultipleCtxResult processHierarchicalAttributes(XACML3EvaluationCtx evaluationCtx) {

        ResourceFinderResult resourceResult = null;
        Set<EvaluationCtx> children = new HashSet<EvaluationCtx>();

        if(resourceId != null){
            if(resourceScope == XACMLConstants.SCOPE_CHILDREN){
                resourceResult = pdpConfig.getResourceFinder().
                                                findChildResources(resourceId, evaluationCtx);
            } else if(resourceScope == XACMLConstants.SCOPE_DESCENDANTS) {
                resourceResult = pdpConfig.getResourceFinder().
                                                findDescendantResources(resourceId, evaluationCtx);
            } else {
                logger.error("Unknown scope type: " );
                //TODO
            }
        } else {
             logger.error("ResourceId Attribute is NULL: " );
            // TODO
        }

        if(resourceResult == null || resourceResult.isEmpty()){
            logger.error("Resource Finder result is NULL: " );
            // TODO
        } else {
            for (AttributeValue resource : resourceResult.getResources()) {
                evaluationCtx.setResourceId(resource, attributesSet);
                children.add(new XACML3EvaluationCtx(new RequestCtx(attributesSet, null), pdpConfig));
            }
        }

        return new MultipleCtxResult(children);

    }

    /**
     *
     * @param evaluationCtx
     * @return
     */
    private MultipleCtxResult processMultipleContentSelectors(XACML3EvaluationCtx evaluationCtx) {

        Set<EvaluationCtx> children = new HashSet<EvaluationCtx>();
        Set<Attributes> newAttributesSet = new HashSet<Attributes>();

        for(Attributes attributes : evaluationCtx.getMultipleContentSelectors()){
            Set<Attribute> newAttributes = null;
            Attribute oldAttribute = null;
            Object content = attributes.getContent();
            if(content != null && content instanceof Node){
                Node root = (Node) content;
                for(Attribute attribute : attributes.getAttributes()){
                    oldAttribute = attribute;
                    if(attribute.getId().toString().equals(XACMLConstants.MULTIPLE_CONTENT_SELECTOR)){
                        List<AttributeValue> values = attribute.getValues();
                        for(AttributeValue value : values){
                            if(value instanceof XPathAttribute){
                                XPathAttribute xPathAttribute = (XPathAttribute)value;
                                if(xPathAttribute.getXPathCategory().
                                                    equals(attributes.getCategory().toString())){
                                    Set<String> xPaths = getChildXPaths(root, xPathAttribute.getValue());
                                    for(String xPath : xPaths){
                                        try {
                                            AttributeValue newValue = Balana.getInstance().getAttributeFactory().
                                                createValue(value.getType(), xPath,
                                                new String[] {xPathAttribute.getXPathCategory()});
                                            Attribute newAttribute =
                                                new Attribute(new URI(XACMLConstants.CONTENT_SELECTOR),
                                                attribute.getIssuer(), attribute.getIssueInstant(),
                                                newValue, attribute.isIncludeInResult(),
                                                XACMLConstants.XACML_VERSION_3_0);
                                            if(newAttributes == null){
                                                newAttributes = new HashSet<Attribute>();
                                            }
                                            newAttributes.add(newAttribute);
                                        } catch (Exception e) {
                                            e.printStackTrace();  // TODO
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if(newAttributes != null){
                    attributes.getAttributes().remove(oldAttribute);
                    for(Attribute attribute : newAttributes){
                        Set<Attribute> set = new HashSet<Attribute>(attributes.getAttributes());
                        set.add(attribute);
                        Attributes attr = new Attributes(attributes.getCategory(), set);
                        newAttributesSet.add(attr);
                    }
                    evaluationCtx.getAttributesSet().remove(attributes);
                }
            }
        }

        for(Attributes attributes : newAttributesSet){
            Set<Attributes> set = new HashSet<Attributes>(evaluationCtx.getAttributesSet());
            set.add(attributes);
            RequestCtx requestCtx = new RequestCtx(set, null);
            children.add(new XACML3EvaluationCtx(requestCtx, pdpConfig));
        }

        return new MultipleCtxResult(children);

    }
    
    /**
     * Changes the value of the resource-id attribute in this context. This is useful when you have
     * multiple resources (ie, a scope other than IMMEDIATE), and you need to keep changing only the
     * resource-id to evaluate the different effective requests.
     * 
     * @param resourceId  resourceId the new resource-id value
     * @param attributesSet a <code>Set</code> of <code>Attributes</code>
     */
    public void setResourceId(AttributeValue resourceId, Set<Attributes> attributesSet) {

        for(Attributes attributes : attributesSet){
            if(XACMLConstants.RESOURCE_CATEGORY.equals(attributes.getCategory().toString())){
                Set<Attribute> attributeSet = attributes.getAttributes();
                Set<Attribute> newSet = new HashSet<Attribute>(attributeSet);
                Attribute resourceIdAttribute = null;

                for (Attribute attribute : newSet){
                    if(XACMLConstants.RESOURCE_ID.equals(attribute.getId().toString())){
                        resourceIdAttribute = attribute;
                        attributeSet.remove(attribute);
                    } else if(XACMLConstants.RESOURCE_SCOPE_2_0.equals(attribute.getId().toString())){
                        attributeSet.remove(attribute);
                    }
                }

                if(resourceIdAttribute != null) {
                    attributeSet.add(new Attribute(resourceIdAttribute.getId(), resourceIdAttribute.getIssuer(),
                    resourceIdAttribute.getIssueInstant(), resourceId, resourceIdAttribute.isIncludeInResult(),
                                                                XACMLConstants.XACML_VERSION_3_0));
                }
                break;
            }

        }
    }

    private Set<String> getChildXPaths(Node root, String xPath){

        Set<String> xPaths = new HashSet<String>();
        NamespaceContext namespaceContext;
        
        //see if the request root is in a namespace
        String namespace = root.getNamespaceURI();

        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();

        if(namespace != null){
            // name spaces are used, so we need to lookup the correct
            // prefix to use in the search string
            NamedNodeMap namedNodeMap = root.getAttributes();
            String prefix = "ns";
            String nodeName = null;

            for (int i = 0; i < namedNodeMap.getLength(); i++) {
                Node n = namedNodeMap.item(i);
                if (n.getNodeValue().equals(namespace)) {
                    // we found the matching namespace, so get the prefix
                    // and then break out
                    nodeName = n.getNodeName();
                    break;
                }
            }

            if(nodeName != null){
                int pos = nodeName.indexOf(':');
                // if the namespace is the default namespace
                // use a default prefix
                if (pos != -1) {
                    // we found a prefixed namespace
                    prefix = nodeName.substring(pos + 1);
                } else {
                    xPath = Utils.prepareXPathForDefaultNs(xPath);
                }
            } else {
                xPath = Utils.prepareXPathForDefaultNs(xPath);
            }

            namespaceContext = new DefaultNamespaceContext(prefix, namespace);

            xpath.setNamespaceContext(namespaceContext);

        }

        try {
            XPathExpression expression = xpath.compile(xPath);
            NodeList matches = (NodeList) expression.evaluate(root, XPathConstants.NODESET);
            if(matches != null && matches.getLength() > 0){

                for (int i = 0; i < matches.getLength(); i++) {
                    String text = null;
                    Node node = matches.item(i);
                    short nodeType = node.getNodeType();

                    // see if this is straight text, or a node with data under
                    // it and then get the values accordingly
                    if ((nodeType == Node.CDATA_SECTION_NODE) || (nodeType == Node.COMMENT_NODE)
                            || (nodeType == Node.TEXT_NODE) || (nodeType == Node.ATTRIBUTE_NODE)) {
                        // there is no child to this node
                        text = node.getNodeValue();
                    } else {
                        // the data is in a child node
                        text = "/" + node.getNodeName();
                    }

                    xPaths.add(text);
                }
            }
        } catch (Exception e) {
            // TODO
        }

        return xPaths;
    }

    public boolean isMultipleAttributes() {
        return multipleAttributes;
    }

    public AbstractRequestCtx getRequestCtx() {
        return requestCtx;
    }

    /**
     *
     * @return
     */
    public Set<PolicyReference> getPolicyReferences() {
        return policyReferences;
    }

    /**
     *
     * @param policyReferences
     */
    public void setPolicyReferences(Set<PolicyReference> policyReferences) {
        this.policyReferences = policyReferences;
    }

    public Set<Attributes> getMultipleContentSelectors() {
        return multipleContentSelectors;
    }

    public Map<String, Set<Attributes>> getMapAttributes() {
        return mapAttributes;
    }

    public Set<Attributes> getAttributesSet() {
        return attributesSet;
    }
}
