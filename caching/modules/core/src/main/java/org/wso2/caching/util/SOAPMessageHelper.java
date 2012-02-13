package org.wso2.caching.util;

import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAP11Constants;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMNode;
import org.apache.axis2.saaj.util.SAAJUtil;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPException;
import java.util.Iterator;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * 
 */
public class SOAPMessageHelper {

    /**
     * This method will clone the provided SOAPEnvelope and returns the cloned envelope
     * as an exact copy of the provided envelope
     *
     * @param envelope - this will be cloned to get the new envelope
     * @return cloned SOAPEnvelope from the provided one
     */
    public static SOAPEnvelope cloneSOAPEnvelope(SOAPEnvelope envelope) {
        SOAPEnvelope newEnvelope;
        if (SOAP11Constants.SOAP_ENVELOPE_NAMESPACE_URI
            .equals(envelope.getBody().getNamespace().getNamespaceURI())) {
            newEnvelope = OMAbstractFactory.getSOAP11Factory().getDefaultEnvelope();
        } else {
            newEnvelope = OMAbstractFactory.getSOAP12Factory().getDefaultEnvelope();
        }

        if (envelope.getHeader() != null) {
            Iterator itr = envelope.getHeader().cloneOMElement().getChildren();
            while (itr.hasNext()) {
                newEnvelope.getHeader().addChild((OMNode) itr.next());
            }
        }

        if (envelope.getBody() != null) {
            Iterator itr = envelope.getBody().cloneOMElement().getChildren();
            while (itr.hasNext()) {
                newEnvelope.getBody().addChild((OMNode) itr.next());
            }
        }

        return newEnvelope;
    }

    public static SOAPEnvelope buildSOAPEnvelopeFromBytes(byte[] data)
            throws SOAPException, IOException {
        
        MessageFactory mf = MessageFactory.newInstance();
        SOAPMessage smsg = mf.createMessage(new MimeHeaders(), new ByteArrayInputStream(data));
        return SAAJUtil.toOMSOAPEnvelope(smsg.getSOAPPart().getDocumentElement());
    }
}
