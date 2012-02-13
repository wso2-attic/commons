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
package org.wso2.xkms2.builder;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.wso2.xkms2.*;

import javax.xml.namespace.QName;
/*
 * 
 */

public abstract class ResultTypeBuilder extends MessageAbstractTypeBuilder {


    public void buildElement(OMElement element,MessageAbstractType messageAbstractType) throws XKMSException {
        super.buildElement(element,messageAbstractType);

        ResultType resultType = (ResultType)messageAbstractType;
        OMElement requestSignatureValueEle = element.getFirstChildWithName(
                new QName(XKMS2Constants.XKMS2_NS, "RequestSignatureValue"));
        if (requestSignatureValueEle != null) {
            String text = requestSignatureValueEle.getText();
            RequestSignatureValue requestSignatureValue;

            OMAttribute idAttr = requestSignatureValueEle.getAttribute(new QName("Id"));
            if (idAttr != null) {
                requestSignatureValue =
                        new RequestSignatureValue(text.getBytes(), idAttr.getAttributeValue());
            } else {
                requestSignatureValue = new RequestSignatureValue(text.getBytes());
            }

            resultType.setRequestSignatureValue(requestSignatureValue);
        }

        OMAttribute resultMajorAttr = element.getAttribute(new QName("ResultMajor"));
        if (resultMajorAttr == null) {
            throw new XKMSException("Attribute ResultMajor is not found");
        }
        resultType.setResultMajor(ResultMajor.validate(resultMajorAttr.getAttributeValue()));

        OMAttribute resultMinorAttr = element.getAttribute(new QName("ResultMinor"));
        if (resultMinorAttr != null) {
            resultType.setResultMinor(ResultMinor.valueOf(resultMinorAttr.getAttributeValue()));
        }


        OMAttribute requestIdAttr = element.getAttribute(new QName("RequestId"));

        if (requestIdAttr != null) {
            resultType.setRequestId(requestIdAttr.getAttributeValue());
        }


    }
}
