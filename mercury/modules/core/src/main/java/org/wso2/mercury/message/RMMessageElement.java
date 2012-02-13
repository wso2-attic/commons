/*
 * Copyright 2008,2009 WSO2, Inc. http://www.wso2.org.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wso2.mercury.message;

import org.apache.axiom.om.OMElement;
import org.wso2.mercury.exception.RMMessageBuildingException;
import org.wso2.mercury.util.MercuryConstants;

/**
 * RM Message Elements are used to handle inner element parts
 * of the RM Messages. in addition to toOM element method
 * they are supposed to and a static fromOM method as well.
 */
public abstract class RMMessageElement {

    protected String rmNamespace;

    protected RMMessageElement() {
        this.rmNamespace = MercuryConstants.RM_1_0_NAMESPACE;
    }

    protected RMMessageElement(String rmNamespace) {
        this.rmNamespace = rmNamespace;
    }

    public abstract OMElement toOM() throws RMMessageBuildingException;

    public String getRmNamespace() {
        return rmNamespace;
    }

    public void setRmNamespace(String rmNamespace) {
        this.rmNamespace = rmNamespace;
    }

}
