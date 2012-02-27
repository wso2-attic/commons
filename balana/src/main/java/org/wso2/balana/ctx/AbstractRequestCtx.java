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

package org.wso2.balana.ctx;

import org.w3c.dom.Node;

import java.util.Set;

/**
 * Represents a XACML request made to the PDP. This is the class that contains all the data used to start
 * a policy evaluation.abstract class has been defined to give a unique interface for both XACML 2.0
 * and XACML 3.0 RequestCtx
 */
public abstract class AbstractRequestCtx {

    // There must be at least one subject
    protected Set<Subject> subjects = null;

    // There must be exactly one resource
    protected Set<Attribute> resource = null;

    // There must be exactly one action
    protected Set<Attribute> action = null;

    // There may be any number of environment attributes
    protected Set<Attribute> environment = null;

    // Hold onto the root of the document for XPath searches
    protected Node documentRoot = null;

    protected boolean isSearch;

    /**
     * Returns a <code>Set</code> containing <code>Subject</code> objects.
     *
     * @return the request' s subject attributes
     */
    public Set getSubjects() {
        return subjects;
    }

    /**
     * Returns a <code>Set</code> containing <code>Attribute</code> objects.
     *
     * @return the request' s resource attributes
     */
    public Set getResource() {
        return resource;
    }

    /**
     * Returns a <code>Set</code> containing <code>Attribute</code> objects.
     *
     * @return the request' s action attributes
     */
    public Set getAction() {
        return action;
    }


    /**
     * Returns a <code>Set</code> containing <code>Attribute</code> objects.
     *
     * @return the request' s environment attributes
     */
    public Set getEnvironmentAttributes() {
        return environment;
    }

    /**
     * Returns the root DOM node of the document used to create this object, or null if this object
     * was created by hand (ie, not through the <code>getInstance</code> method) or if the root node
     * was not provided to the constructor.
     *
     * @return the root DOM node or null
     */
    public Node getDocumentRoot() {
        return documentRoot;
    }    

    public void setSearch(boolean isSearch) {
        this.isSearch = isSearch;
    }

    public boolean isSearch() {
        return isSearch;
    }
}
