/*
 * Copyright 2001-2004 The Apache Software Foundation.
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
package org.wso2.xfer;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.soap.SOAP11Constants;
import org.apache.axiom.soap.SOAP12Constants;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axiom.soap.SOAPFaultCode;
import org.apache.axiom.soap.SOAPFaultDetail;
import org.apache.axiom.soap.SOAPFaultReason;
import org.apache.axiom.soap.SOAPFaultSubCode;
import org.apache.axiom.soap.SOAPFaultText;
import org.apache.axiom.soap.SOAPFaultValue;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.addressing.AddressingFaultsHelper;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.context.OperationContext;

public class WSTransferUtil {

    public static void triggerMessageInfoHeaderRequiredFault(
            MessageContext messageContext, String missingHeaderName)
            throws AxisFault {

        AddressingFaultsHelper.triggerMessageAddressingRequiredFault(
                messageContext, missingHeaderName);
    }

    public static void triggerInvalidMessageInfoHeaderFault(
            MessageContext messageContext, String invalidHeader) throws AxisFault {

        AddressingFaultsHelper.triggerInvalidCardinalityFault(messageContext, invalidHeader);
    }

    public static void triggerInvalidRepresentationFault(MessageContext messageContext,
                                                         String detail) throws AxisFault {

        QName faultCode = new QName(
                SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI,
                SOAP12Constants.FAULT_CODE_SENDER,
                SOAP12Constants.SOAP_DEFAULT_NAMESPACE_PREFIX);

        QName faultSubcode = new QName(WSTransferConstants.NS_URI_WXF,
                                       WSTransferConstants.FAULT_INVALID_REPRESENTATION,
                                       WSTransferConstants.ATTR_WXF);

        if (!messageContext.isSOAP11()) {
            SOAPFactory factory = OMAbstractFactory.getSOAP12Factory();
            SOAPFaultDetail soapFaultDetail = factory.createSOAPFaultDetail();

            OMElement faultDetailElement = factory.createOMElement(WSTransferConstants.FAULT_DATAIL, WSTransferConstants.NS_URI_WSMAN,WSTransferConstants.ATTR_WSMA);
            faultDetailElement.setText(detail);

            soapFaultDetail.addChild(faultDetailElement);
            messageContext.setProperty(SOAP12Constants.SOAP_FAULT_DETAIL_LOCAL_NAME, soapFaultDetail);
        }

        triggerAxisFault(messageContext, WSTransferConstants.INVALID_REPRESENTATION_DETAIL, detail , faultCode, faultSubcode, WSTransferConstants.FAULT_INVALID_REPRESENTATION_REASON);

    }


    public static void triggerAxisFault(MessageContext msgCtx,
                                        String faultInfoKey, String faultInfoValue, QName faultCode,
                                        QName faultSubcode, String faultReason) throws AxisFault {

        if (faultInfoKey != null) {

            Map faultInfoForHeaders = (Map) msgCtx
                    .getProperty(Constants.FAULT_INFORMATION_FOR_HEADERS);

            if (faultInfoForHeaders == null) {
                faultInfoForHeaders = new HashMap();
                msgCtx.setProperty(Constants.FAULT_INFORMATION_FOR_HEADERS,
                                   faultInfoForHeaders);
            }

            faultInfoForHeaders.put(faultInfoKey, faultInfoValue);
        }
        /*
         * 
         */
        if (!msgCtx.isSOAP11()) {
            setFaultCodeForSOAP12(msgCtx, faultCode, faultSubcode);
        }

        OperationContext opCtx = msgCtx.getOperationContext();

        opCtx.setProperty(
                Constants.Configuration.SEND_STACKTRACE_DETAILS_WITH_FAULTS,
                Boolean.FALSE);

        throw new AxisFault(faultReason, faultSubcode);
    }

    private static void setFaultCodeForSOAP12(MessageContext msgContext,
                                              QName faultCode, QName faultSubcode) throws AxisFault {

        SOAPFactory factory = OMAbstractFactory.getSOAP12Factory();

        SOAPFaultCode soapFaultCode = factory.createSOAPFaultCode();
        SOAPFaultValue soapFaultValue = factory.createSOAPFaultValue(soapFaultCode);

        soapFaultValue.setText(faultCode.getPrefix() + ":"
                               + faultCode.getLocalPart());

        SOAPFaultSubCode soapFaultSubCode = factory
                .createSOAPFaultSubCode(soapFaultCode);
        SOAPFaultValue soapFaultSubCodeValue = factory
                .createSOAPFaultValue(soapFaultSubCode);

        soapFaultSubCodeValue.setText(faultSubcode.getPrefix() + ":"
                                      + faultSubcode.getLocalPart());

        msgContext.setProperty(SOAP12Constants.SOAP_FAULT_CODE_LOCAL_NAME,
                               soapFaultCode);

    }
    
    public static void triggerAxisFault(MessageContext messageContext, WSTransferException wste) throws AxisFault{
        
        SOAPEnvelope envelope = messageContext.getEnvelope();
        SOAPFactory fac = (SOAPFactory) envelope.getOMFactory();
        String soapNSURI = fac.getSoapVersionURI();
        
        SOAPFaultCode faultCode = fac.createSOAPFaultCode();
        SOAPFaultValue faultCodeValue = fac.createSOAPFaultValue(faultCode);
        faultCodeValue.setText(wste.getCode());

        SOAPFaultSubCode faultSubCode = fac.createSOAPFaultSubCode(faultCode);
        SOAPFaultValue faultSubCodeValue = fac.createSOAPFaultValue(faultSubCode);
        faultSubCodeValue.setText(wste.getSubCode());
        
        SOAPFaultReason faultReason = fac.createSOAPFaultReason();
        SOAPFaultText faultText = fac.createSOAPFaultText(faultReason);
        faultText.setText(wste.getReason());        
        
        if (SOAP11Constants.SOAP_ENVELOPE_NAMESPACE_URI.equals(soapNSURI)) {
            messageContext.setProperty(SOAP11Constants.SOAP_FAULT_CODE_LOCAL_NAME, faultCode);
            messageContext.setProperty(SOAP11Constants.SOAP_FAULT_STRING_LOCAL_NAME, faultReason);
            
        } else {
            messageContext.setProperty(SOAP12Constants.SOAP_FAULT_CODE_LOCAL_NAME, faultCode);
            messageContext.setProperty(SOAP12Constants.SOAP_FAULT_REASON_LOCAL_NAME, faultReason);
            
            String detail = wste.getDetail();
            if (detail != null) {
                
                SOAPFaultDetail faultDetail = fac.createSOAPFaultDetail();
                faultDetail.setText(detail);
                messageContext.setProperty(SOAP12Constants.SOAP_FAULT_DETAIL_LOCAL_NAME, faultDetail);    
            }
        }
        
        throw AxisFault.makeFault(wste);           
    }    
}
