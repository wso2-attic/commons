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

public class MTOMClient {

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
        String fileName = getProperty("opt_file", "/opt/sign_only/resources/house.jpg");
        String mode = getProperty("opt_mode", "mtom");

        if (args.length > 0) mode = args[0];
        if (args.length > 1) targetEPR = args[1];
        if (args.length > 2) fileName = args[2];

        sendUsingMTOM(fileName, targetEPR);
        
    }

    public static OMElement sendUsingMTOM(String fileName, String targetEPR) throws IOException {
        OMFactory factory = OMAbstractFactory.getOMFactory();
        OMNamespace ns = factory.createOMNamespace("http://services.samples/xsd", "m0");
        OMElement payload = factory.createOMElement("uploadFileUsingMTOM", ns);
        OMElement request = factory.createOMElement("request", ns);
        OMElement image = factory.createOMElement("image", ns);

        System.out.println("Sending file : " + fileName + " as MTOM");
        FileDataSource fileDataSource = new FileDataSource(new File(fileName));
        DataHandler dataHandler = new DataHandler(fileDataSource);
        OMText textData = factory.createOMText(dataHandler, true);
        image.addChild(textData);
        request.addChild(image);
        payload.addChild(request);

        ServiceClient serviceClient = new ServiceClient();
        Options options = new Options();
        options.setTo(new EndpointReference(targetEPR));
        options.setAction("urn:uploadFileUsingMTOM");
        options.setProperty(Constants.Configuration.ENABLE_MTOM, Constants.VALUE_TRUE);

        serviceClient.setOptions(options);
        OMElement response = serviceClient.sendReceive(payload);

        OMText binaryNode = (OMText) response.
                getFirstChildWithName(new QName("http://services.samples/xsd", "response")).
                getFirstChildWithName(new QName("http://services.samples/xsd", "image")).
                getFirstOMChild();
        dataHandler = (DataHandler) binaryNode.getDataHandler();
        InputStream is = dataHandler.getInputStream();

        File tempFile = File.createTempFile("mtom-", ".jpg");
        FileOutputStream fos = new FileOutputStream(tempFile);
        BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);

        byte data[] = new byte[BUFFER];
        int count;
        while ((count = is.read(data, 0, BUFFER)) != -1) {
            dest.write(data, 0, count);
        }

        dest.flush();
        dest.close();
        System.out.println("Saved response to file : " + tempFile.getAbsolutePath());
        return response;
    }
}
