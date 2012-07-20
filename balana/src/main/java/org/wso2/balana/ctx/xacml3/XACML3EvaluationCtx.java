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
import org.wso2.balana.*;
import org.wso2.balana.attr.*;
import org.wso2.balana.cond.EvaluationResult;
import org.wso2.balana.ctx.AbstractRequestCtx;
import org.wso2.balana.ctx.Attribute;
import org.wso2.balana.ctx.BasicEvaluationCtx;
import org.wso2.balana.ctx.EvaluationCtx;
import org.wso2.balana.finder.ResourceFinderResult;
import org.wso2.balana.xacml3.Attributes;
import org.wso2.balana.xacml3.AttributesReference;
import org.wso2.balana.xacml3.MultiRequests;
import org.wso2.balana.xacml3.RequestReference;

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
     * whether multiple attributes are present or not
     */
    private boolean multipleAttributes;

    /**
     * attributes categorized as Map
     *
     * Category  --> Attributes Set
     */
    private Map<String, Set<Attributes>> mapAttributes;


    /**
     * obligations results
     */
//    private Set<ObligationResult>  obligationResults;

    /**
     * Advice results
     */
//    private Set<Advice> advices;

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
                    attributeValues.add(attribute.getValue());
                }
            }

            if(attributeValues.size() < 1){
                return callHelper(type, id, issuer, category);
            }
        }
        // if we got here, then we found at least one useful AttributeValue
        return new EvaluationResult(new BagAttribute(type, attributeValues));
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

            if(XACMLConstants.RESOURCE_CATEGORY.equals(category)){
                for(Attribute attribute : attributes.getAttributes()){
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

        if(multiRequests != null){

            MultipleCtxResult result = processMultiRequestElement(this);
            if(result.isIndeterminate()){
                return result;
            } else {
                evaluationCtxSet.addAll(result.getEvaluationCtxSet());
            }
        }

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
            }
        }

        if(evaluationCtxSet.size() > 0){
            return new MultipleCtxResult(evaluationCtxSet, null, false);
        } else {
            evaluationCtxSet.add(this);
            return new MultipleCtxResult(evaluationCtxSet, null, false);
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
                            for(Attributes attribute : evaluationCtx.getRequestCtx().getAttributesSet()){
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

        return new MultipleCtxResult(children, null, false);
    }

    /**
     *
     * @param evaluationCtx
     * @return
     */
    private MultipleCtxResult processMultipleAttributes(XACML3EvaluationCtx evaluationCtx) {

        Set<EvaluationCtx> children = new HashSet<EvaluationCtx>();
        Set<RequestCtx> newRequestCtxSet = new HashSet<RequestCtx>();

        for(Map.Entry<String, Set<Attributes>> mapAttributesEntry : mapAttributes.entrySet()){
            if(mapAttributesEntry.getValue().size() > 1){
                for(Attributes attributesElement :  mapAttributesEntry.getValue()){
                    Set<Attributes> newSet = new HashSet<Attributes>(attributesSet);
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

        return new MultipleCtxResult(children, null, false);

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

        return new MultipleCtxResult(children, null, false);

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

    public boolean isMultipleAttributes() {
        return multipleAttributes;
    }

    public AbstractRequestCtx getRequestCtx() {
        return requestCtx;
    }

}
