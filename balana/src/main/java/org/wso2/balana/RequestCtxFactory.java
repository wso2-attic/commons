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

package org.wso2.balana;

import org.w3c.dom.Node;
import org.wso2.balana.ctx.AbstractRequestCtx;

import java.util.HashMap;

/**
 * Factory that creates the AbstractRequestCtx  //TODO
 */
public class RequestCtxFactory {

    private HashMap<String, AbstractRequestCtx> targetMap = new HashMap<String, AbstractRequestCtx>();

    private static RequestCtxFactory factoryInstance;

    private static void init(){
        // TODO
    }

    public void registerTarget(){
        // TODO
    }

    public AbstractRequestCtx getRequestCtx(Node root) throws ParsingException {

        String requestCtxNs = root.getNamespaceURI();

        if(requestCtxNs != null){
            if(XACMLConstants.REQUEST_CONTEXT_1_0_IDENTIFIER.equals(requestCtxNs.trim())){
                return org.wso2.balana.xacml2.ctx.RequestCtx.getInstance(root);
            } else if(XACMLConstants.REQUEST_CONTEXT_2_0_IDENTIFIER.equals(requestCtxNs.trim())){
                return org.wso2.balana.xacml2.ctx.RequestCtx.getInstance(root);
            } else if(XACMLConstants.REQUEST_CONTEXT_3_0_IDENTIFIER.equals(requestCtxNs.trim())){
                return org.wso2.balana.xacml3.ctx.RequestCtx.getInstance(root);
            }
        } else {
            throw new ParsingException("Namespace of Request cannot be null");     //TODO correct error message
        }

        return null;

    }

    /**
     * Returns an instance of this factory. This method enforces a singleton model, meaning that
     * this always returns the same instance, creating the factory if it hasn't been requested
     * before.
    *
     * @return the factory instance
     */
    public static RequestCtxFactory getFactory() {
        if (factoryInstance == null) {
            synchronized (RequestCtxFactory.class) {
                if (factoryInstance == null) {
                    factoryInstance = new RequestCtxFactory();
                }
            }
        }

        return factoryInstance;
    }

}
