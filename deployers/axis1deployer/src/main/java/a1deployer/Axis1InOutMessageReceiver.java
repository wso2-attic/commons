package a1deployer;

import org.apache.axis2.receivers.AbstractInOutMessageReceiver;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.context.ServiceContext;
import org.apache.axis2.AxisFault;
import org.apache.axis2.description.Parameter;
import org.apache.axis2.transport.TransportUtils;
import org.apache.axis.server.AxisServer;
import org.apache.axis.Message;
import org.apache.axis.session.SimpleSession;
import org.apache.axiom.soap.impl.builder.StAXSOAPModelBuilder;
import org.apache.axiom.soap.SOAPBody;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/*
* Copyright 2007 The Apache Software Foundation.
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
public class Axis1InOutMessageReceiver extends AbstractInOutMessageReceiver {
    static final String A1SESSION = "Axis1Session";

    public void invokeBusinessLogic(MessageContext inMessage, MessageContext outMessage)
            throws AxisFault {

        ByteArrayOutputStream reqByteStream = new ByteArrayOutputStream();
        TransportUtils.writeMessage(inMessage, reqByteStream);
        ByteArrayInputStream reqInputStream = new ByteArrayInputStream(reqByteStream.toByteArray());

        AxisServer engine;
        Parameter parameter = inMessage.getAxisServiceGroup().getParameter("service.axis1.server");
        if (parameter == null) {
            throw new AxisFault("service.axis1.server Parameter cannot be located.");
        }
        engine = (AxisServer) parameter.getValue();
        if (engine == null) {
            throw new AxisFault("Axis1 server not found in configuration context");
        }
        org.apache.axis.MessageContext axis1Context = new org.apache.axis.MessageContext(engine);
        Message msg = new Message(reqInputStream);
        axis1Context.setRequestMessage(msg);
        axis1Context.setClassLoader(inMessage.getAxisService().getClassLoader());
        try {
            axis1Context.setTargetService(inMessage.getAxisService().getName());
        } catch (org.apache.axis.AxisFault axisFault) {
            throw translateFault(axisFault);
        }

        // Set up session mechanics.  Make sure there is an Axis1 session
        // associated with the Axis2 ServiceContext.  Since A2 ServiceContexts
        // are per-session, this makes sure the A2 session maps to an A1
        // session.
        // TODO: Check performance, consider introducing a flag for this behavior
        ServiceContext sc = inMessage.getServiceContext();
        SimpleSession session = (SimpleSession)sc.getProperty(A1SESSION);
        if (session == null) {
            session = new SimpleSession();
            sc.setProperty(A1SESSION, session);
        }
        axis1Context.setSession(session);

        try {
            engine.invoke(axis1Context);
        } catch (org.apache.axis.AxisFault axisFault) {
            // turn this into an Axis2 fault
            throw translateFault(axisFault);
        }

        Message respMsg = axis1Context.getResponseMessage();
        if (respMsg == null) {
            throw new AxisFault("No response from Axis1 server!");
        }

        ByteArrayOutputStream respByteStream = new ByteArrayOutputStream();
        try {
            respMsg.writeTo(respByteStream);
        } catch (Exception e) {
            throw AxisFault.makeFault(e);
        }

        // TODO: Figure out how to do this correctly and deal with MTOM, etc
        ByteArrayInputStream respStream = new ByteArrayInputStream(respByteStream.toByteArray());
        XMLStreamReader parser;
        try {
            parser = XMLInputFactory.newInstance().createXMLStreamReader(respStream);
        } catch (XMLStreamException e) {
            throw AxisFault.makeFault(e);
        }
        StAXSOAPModelBuilder builder = new StAXSOAPModelBuilder(parser);
        outMessage.setEnvelope(builder.getSOAPEnvelope());
    }

    /**
     * Translate an Axis1.X fault into an Axis2 fault by serializing / deserializing
     *
     * @param fault Axis1 fault
     * @return an Axis2 fault
     * @noinspection ThrowableInstanceNeverThrown
     */
    public static AxisFault translateFault(org.apache.axis.AxisFault fault) {
        try {
            org.apache.axis.Message msg = new Message(fault);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            msg.writeTo(os);
            InputStream is = new ByteArrayInputStream(os.toByteArray());
            XMLStreamReader parser = XMLInputFactory.newInstance().createXMLStreamReader(is);
            StAXSOAPModelBuilder builder = new StAXSOAPModelBuilder(parser);
            SOAPBody body = builder.getSOAPEnvelope().getBody();
            return new AxisFault(body.getFault());
        } catch (Exception e) {
            return new AxisFault("Couldn't parse Axis1 fault");
        }
    }
}
