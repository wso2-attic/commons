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

import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.impl.llom.OMTextImpl;
import org.apache.axiom.attachments.ByteArrayDataSource;

import javax.activation.DataHandler;

/**
 * The ResultType type is the class from which all XKMS response element classes are derived.
 * The ResultType  class inherits the element and attributes of the MessageAbstractType
 * abstract class and in addition contains the following attributes
 */

public class ResultType extends MessageAbstractType {

    private RequestSignatureValue requestSignatureValue;
    private ResultMajor resultMajor;
    private ResultMinor resultMinor;
    private String requestId;

    public RequestSignatureValue getRequestSignatureValue() {
        return requestSignatureValue;
    }

    public void setRequestSignatureValue(RequestSignatureValue requestSignatureValue) {
        this.requestSignatureValue = requestSignatureValue;
    }

    public ResultMajor getResultMajor() {
        return resultMajor;
    }

    public void setResultMajor(ResultMajor resultMajor) {
        this.resultMajor = resultMajor;
    }

    public ResultMinor getResultMinor() {
        return resultMinor;
    }

    public void setResultMinor(ResultMinor resultMinor) {
        this.resultMinor = resultMinor;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    protected void serialize(OMFactory factory, OMElement container) throws XKMSException {
        super.serialize(factory, container);
        OMNamespace emptyNs = factory.createOMNamespace("", "");
        if (requestSignatureValue != null) {
            OMElement requestSignatureValueEle =
                    factory.createOMElement(XKMS2Constants.ELE_REQUEST_SIGNATURE_VALUE);

            DataHandler dataHandler =
                    new DataHandler(
                            new ByteArrayDataSource(requestSignatureValue.getBase64Binary()));
            OMTextImpl omText = new OMTextImpl(dataHandler, true, factory);
            requestSignatureValueEle.addChild(omText);
            String id = requestSignatureValue.getId();
            if (id != null) {
                requestSignatureValueEle.addAttribute(XKMS2Constants.ATTR_ID, id, emptyNs);
            }
            container.addChild(requestSignatureValueEle);

        }

        if (resultMajor == null) {
            throw new XKMSException("ResultMajor is not found");
        }

        container.addAttribute(XKMS2Constants.ATTR_RESULT_MAJOR,resultMajor.getAnyURI(),emptyNs);

        if (resultMinor != null) {
            container.addAttribute(XKMS2Constants.ATTR_RESULT_MINOR,resultMinor.getAnyURI(),emptyNs);
        }

        if (requestId != null) {
            container.addAttribute(XKMS2Constants.ATTR_REQUEST_ID,requestId,emptyNs);
        }
    }

}
