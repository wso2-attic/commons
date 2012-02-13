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

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.soap.SOAPBody;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axiom.soap.SOAPHeader;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.AddressingConstants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.addressing.EndpointReferenceHelper;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.description.AxisOperation;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.receivers.AbstractInOutMessageReceiver;

import javax.xml.namespace.QName;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class WSTransferMessageReceiver extends AbstractInOutMessageReceiver {

    private static final String OP_NAME_PUT = "put";

    private static final String OP_NAME_GET = "get";

    private static final String OP_NAME_CREATE = "create";

    private static final String OP_NAME_DELETE = "delete";

    public void invokeBusinessLogic(MessageContext inMessageContext,
                                    MessageContext outMessageContext) throws AxisFault {

        Object serviceImplObject = getTheImplementationObject(inMessageContext);
        Class serviceImplClass = serviceImplObject.getClass();

        AxisService axisService = inMessageContext.getAxisService();
        AxisOperation axisOperation = inMessageContext.getAxisOperation();

        String methodName = axisOperation.getName().getLocalPart();

        try {
            Method method;

            SOAPEnvelope envelope = inMessageContext.getEnvelope();
            SOAPEnvelope outSoapEnvelope = getSoapEnvelop(inMessageContext);
            outMessageContext.setEnvelope(outSoapEnvelope);


            if (OP_NAME_PUT.equals(methodName)) {

                SOAPHeader headers = envelope.getHeader();
                method = serviceImplClass.getDeclaredMethod(methodName,
                                                            new Class[]{OMElement.class, OMElement.class});

                SOAPBody body = envelope.getBody();
                OMElement firstChild = body.getFirstElement();

                if (firstChild == null) {
                    WSTransferUtil.triggerInvalidRepresentationFault(
                            inMessageContext,
                            WSTransferConstants.FAULT_MISSING_VALUES);
                }

                OMElement resultElement = (OMElement) method.invoke(
                        serviceImplObject, new Object[]{headers, firstChild});

                /*
                 * Here we assumes that if the method returns null, then the
                 * resource specified had not been changed during the method
                 * invocation. Therefore we leave the SOAP body empty in the
                 * response message.
                 */
                if (resultElement != null) {
                    resultElement.declareNamespace(axisService
                            .getTargetNamespace(), axisService
                            .getTargetNamespacePrefix());
                    outSoapEnvelope.getBody().addChild(resultElement);
                }


            } else if (OP_NAME_GET.equals(methodName)) {

                SOAPHeader headers = envelope.getHeader();
                method = serviceImplClass.getDeclaredMethod(methodName,
                                                            new Class[]{OMElement.class});

                OMElement resultElement = (OMElement) method.invoke(
                        serviceImplObject, new Object[]{headers});

                /*
                 * We throw an AxisFault if there is no resource associated with
                 * the Message Id.
                 */
                if (resultElement == null) {
                    WSTransferUtil.triggerInvalidMessageInfoHeaderFault(
                            inMessageContext,
                            WSTransferConstants.ELEM_MESSAGE_ID);
                }

                resultElement.declareNamespace(
                        axisService.getTargetNamespace(), axisService
                                .getTargetNamespacePrefix());

                outSoapEnvelope.getBody().addChild(resultElement);

            } else if (OP_NAME_DELETE.equals(methodName)) {

                SOAPHeader headers = envelope.getHeader();
                method = serviceImplClass.getDeclaredMethod(methodName,
                                                            new Class[]{OMElement.class});
                method.invoke(serviceImplObject, new Object[]{headers});

            } else if (OP_NAME_CREATE.equals(methodName)) {

                method = serviceImplClass.getDeclaredMethod(methodName,
                                                            new Class[]{OMElement.class});

                SOAPBody body = envelope.getBody();
                OMElement firstChild = body.getFirstElement();

                if (firstChild == null) {
                    WSTransferUtil.triggerInvalidRepresentationFault(
                            inMessageContext,
                            WSTransferConstants.FAULT_MISSING_VALUES);
                }

                EndpointReference endpointReference =
                        (EndpointReference) method.invoke(serviceImplObject, new Object[]{firstChild});

                OMFactory factory = outSoapEnvelope.getOMFactory();
                QName qname = new QName(WSTransferConstants.NS_URI_WXF,
                                        WSTransferConstants.ELEM_RESOURCE_CREATED);

                OMElement resultElement =
                        EndpointReferenceHelper.toOM(factory, endpointReference, qname,
                                                     AddressingConstants.Final.WSA_NAMESPACE);
                outSoapEnvelope.getBody().addChild(resultElement);

            } else {

                // should never reach this bock
                throw new AxisFault("should nerver reach this bock");
            }

        } catch (Exception ex) {
            handleError(inMessageContext, ex);
        }
    }

    private void handleError(MessageContext messageContext, Exception ex)
            throws AxisFault {

        if (ex instanceof AxisFault) {
            throw (AxisFault) ex;

        } else if (ex instanceof InvocationTargetException) {
            Throwable throwable = ex.getCause();

            if (throwable instanceof WSTransferException) {
                WSTransferUtil.triggerAxisFault(messageContext, (WSTransferException) throwable);
            }
        }

        throw AxisFault.makeFault(ex);
    }

    private SOAPEnvelope getSoapEnvelop(MessageContext inMessageContext)
            throws AxisFault {
        SOAPFactory soapFactory = getSOAPFactory(inMessageContext);
        return soapFactory.getDefaultEnvelope();
    }
}