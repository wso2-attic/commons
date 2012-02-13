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
package org.wso2.xkms2.client;

import org.wso2.xkms2.*;
import org.wso2.xkms2.core.ProtocolExchange;
import org.wso2.xkms2.util.XKMSUtil;
import org.apache.axiom.om.util.UUIDGenerator;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.impl.dom.DOOMAbstractFactory;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAP12Constants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.AxisFault;
import org.apache.axis2.wsdl.WSDLConstants;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.OperationClient;
import org.apache.axis2.client.ServiceClient;
import org.apache.xml.security.keys.KeyInfo;

import java.util.List;

/**
 * This class will have utility methods to create XX serivces request objects.
 */

public class XKMSStubHelper {

    /**
     * This will create the proper LocateRequest based on information
     *
     * @param xkmsEndpointReference
     * @param respondWiths
     * @param keyUsages
     * @param keyInfo
     * @param useKeyWiths
     * @param kissRequest
     */
    public static void populateRequest(EndpointReference xkmsEndpointReference,
                                                RespondWith[] respondWiths,
                                                KeyUsage[] keyUsages,
                                                KeyInfo keyInfo,
                                                UseKeyWith[] useKeyWiths,
                                                KISSRequest kissRequest) {
        //Mandatory element
        kissRequest.setId(UUIDGenerator.getUUID());
        kissRequest.setServiceURI(xkmsEndpointReference.getAddress());

        if (respondWiths != null) {
            for (int j = 0; j < respondWiths.length; j++) {
                kissRequest.addRespondWith(respondWiths[j]);
            }
        }

        QueryKeyBinding queryKeyBinding = new QueryKeyBinding();
        kissRequest.setQueryKeyBinding(queryKeyBinding);
        if (keyUsages != null) {
            for (int j = 0; j < keyUsages.length; j++) {
                queryKeyBinding.addKeyUsage(keyUsages[j]);
            }
        } else {
            // Need to add all
            queryKeyBinding.addKeyUsage(KeyUsage.ENCRYPTION);
            queryKeyBinding.addKeyUsage(KeyUsage.EXCHANGE);
            queryKeyBinding.addKeyUsage(KeyUsage.SIGNATURE);
        }


        if (keyInfo != null) {
            queryKeyBinding.setKeyInfo(keyInfo);
        }

        if (useKeyWiths != null) {
            for (int j = 0; j < useKeyWiths.length; j++) {
                queryKeyBinding.addUseKeyWith(useKeyWiths[j]);
            }
        }

    }

    /**
     * Help in when calling to Locate service. The method uses OperatoinClient.
     * @param keyInfo
     * @param useKeyWiths
     * @param respondWiths
     * @param keyUsages
     * @param serviceClient
     * @param request
     * @return
     * @throws XKMSException
     */
    public static List elementHelper(KeyInfo keyInfo, UseKeyWith[] useKeyWiths,
                                     RespondWith[] respondWiths,
                                     KeyUsage[] keyUsages,
                                     ServiceClient serviceClient,
                                     KISSRequest request) throws XKMSException {
        ProtocolExchange protocolExchange = new ProtocolExchange();

        try {
            populateRequest(serviceClient.getOptions().getTo(),
                            respondWiths,
                            keyUsages, keyInfo,
                            useKeyWiths,
                            request);

            OMFactory fac = DOOMAbstractFactory.getOMFactory();
            OMElement omElement = request.serialize(fac);
            omElement = XKMSUtil.getOMElement(omElement);
            MessageContext inMsgCtx = sendReceive(omElement, serviceClient);
            XKMSElement xkmsElement = protocolExchange.exchangeClient(inMsgCtx);
            LocateResult locateResult = (LocateResult) xkmsElement;

            return locateResult.getUnverifiedKeyBindingList();
        } catch (AxisFault e) {
            e.printStackTrace();
            throw new XKMSException(e);
        }

    }


    private static MessageContext sendReceive(OMElement llomElement, ServiceClient serviceClient)
            throws AxisFault {
        MessageContext messageContext = new MessageContext();
        fillSOAPEnvelope(messageContext, llomElement, serviceClient);
        OperationClient operationClient = serviceClient.createClient(ServiceClient.ANON_OUT_IN_OP);
        operationClient.addMessageContext(messageContext);
        operationClient.execute(true);
        MessageContext response = operationClient
                .getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
        if (serviceClient.getOptions().isCallTransportCleanup()) {
            response.getEnvelope().build();
            serviceClient.cleanupTransport();
            return response;
        } else {
            return response;
        }
    }

    private static void fillSOAPEnvelope(MessageContext messageContext, OMElement xmlPayload,
                                         ServiceClient serviceClient)
            throws AxisFault {
        messageContext.setServiceContext(serviceClient.getServiceContext());
        SOAPFactory soapFactory = getSOAPFactory(serviceClient.getOptions());
        SOAPEnvelope envelope = soapFactory.getDefaultEnvelope();
        if (xmlPayload != null) {
            envelope.getBody().addChild(xmlPayload);
        }
        serviceClient.addHeadersToEnvelope(envelope);
        messageContext.setEnvelope(envelope);
    }

    private static SOAPFactory getSOAPFactory(Options options) {
        String soapVersionURI = options.getSoapVersionURI();
        if (SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI.equals(soapVersionURI)) {
            return OMAbstractFactory.getSOAP12Factory();
        } else {
            // make the SOAP 1.1 the default SOAP version
            return OMAbstractFactory.getSOAP11Factory();
        }
    }
}
