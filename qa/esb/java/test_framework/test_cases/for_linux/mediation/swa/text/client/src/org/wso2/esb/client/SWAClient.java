package org.wso2.esb.client;

import org.apache.axiom.om.*;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axiom.soap.SOAPBody;
import org.apache.axiom.attachments.Attachments;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.OperationClient;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.Constants;
import org.apache.axis2.wsdl.WSDLConstants;
import org.apache.axis2.context.MessageContext;

import javax.activation.FileDataSource;
import javax.activation.DataHandler;
import javax.xml.namespace.QName;
import java.io.*;

public class SWAClient {

    private static final int BUFFER = 2048;

    private static String getProperty(String name, String def) {
        String result = System.getProperty(name);
        if (result == null || result.length() == 0) {
            result = def;
        }
        return result;
    }

    public static void main(String[] args) throws Exception {

        String targetEPR = getProperty("opt_url", args[0]);
        String fileName = getProperty("opt_file", "/opt/sign_only/resources/synapse.txt");
        String mode = getProperty("opt_mode", "mtom");

        if (args.length > 0) mode = args[0];
        if (args.length > 1) targetEPR = args[1];
        if (args.length > 2) fileName = args[2];

        sendUsingSwA(fileName, targetEPR);
        
    }

    public static MessageContext sendUsingSwA(String fileName, String targetEPR) throws IOException {

        Options options = new Options();
        options.setTo(new EndpointReference(targetEPR));
        options.setAction("urn:uploadFileUsingSwA");
        options.setProperty(Constants.Configuration.ENABLE_SWA, Constants.VALUE_TRUE);

        ServiceClient sender = new ServiceClient();
        sender.setOptions(options);
        OperationClient mepClient = sender.createClient(ServiceClient.ANON_OUT_IN_OP);

        MessageContext mc = new MessageContext();

        System.out.println("Sending file : " + fileName + " as SwA");
        FileDataSource fileDataSource = new FileDataSource(new File(fileName));
        DataHandler dataHandler = new DataHandler(fileDataSource);
        String attachmentID = mc.addAttachment(dataHandler);


        SOAPFactory factory = OMAbstractFactory.getSOAP11Factory();
        SOAPEnvelope env = factory.getDefaultEnvelope();
        OMNamespace ns = factory.createOMNamespace("http://services.samples/xsd", "m0");
        OMElement payload = factory.createOMElement("uploadFileUsingSwA", ns);
        OMElement request = factory.createOMElement("request", ns);
        OMElement imageId = factory.createOMElement("imageId", ns);
        imageId.setText(attachmentID);
        request.addChild(imageId);
        payload.addChild(request);
        env.getBody().addChild(payload);
        mc.setEnvelope(env);

        mepClient.addMessageContext(mc);
        mepClient.execute(true);
        MessageContext response = mepClient.getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);

        SOAPBody body = response.getEnvelope().getBody();
        String imageContentId = body.
                getFirstChildWithName(new QName("http://services.samples/xsd", "uploadFileUsingSwAResponse")).
                getFirstChildWithName(new QName("http://services.samples/xsd", "response")).
                getFirstChildWithName(new QName("http://services.samples/xsd", "imageId")).
                getText();

        Attachments attachment = response.getAttachmentMap();
        dataHandler = attachment.getDataHandler(imageContentId);
        File tempFile = File.createTempFile("swa-", ".txt");
        FileOutputStream fos = new FileOutputStream(tempFile);
        dataHandler.writeTo(fos);
        fos.flush();
        fos.close();

        System.out.println("Saved response to file : " + tempFile.getAbsolutePath());

        return response;
    }
}
