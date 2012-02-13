/*
 * Copyright 2004,2005 The Apache Software Foundation.
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
package org.wso2.xkms2;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
/*
 * 
 */

public interface ElementSerializable {

    /**
     * All the XKMS request types are inherited from this class and should implement the following
     * method to get the serialize form of the object as OMElement.
     * Implementation of method such that, this will return either fully consructed OMElement or
     * NULL if the element cannot be constructed.
     *
     * The factory passed should be an instance of DOOMAbstractFactory
     * 
     * @param factory
     * @return OMElement
     * @throws XKMSException
     */
    public OMElement serialize(OMFactory factory) throws XKMSException;
}
