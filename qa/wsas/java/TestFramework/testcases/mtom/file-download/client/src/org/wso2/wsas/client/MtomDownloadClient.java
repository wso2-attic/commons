/* This is a sample client to test file downloading 
* with MTOM.
*/
package org.wso2.wsas.client;

import java.io.File;
import java.io.FileOutputStream;

import javax.activation.DataHandler;


import org.apache.axiom.attachments.utils.DataHandlerUtils;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.OMNode;
import org.apache.axiom.om.OMText;
import org.apache.axis2.Constants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;



public class MtomDownloadClient {
	
	
	
	public static void main(String[] args)throws Exception {
		String inputfile = args[0];
		String outFilename = args[1];
		String toEpr = args[2];
		ServiceClient sc = new ServiceClient();
        Options opts = new Options();
        opts.setTo(new EndpointReference(toEpr));
        opts.setAction("urn:getFile");
        opts.setProperty(Constants.Configuration.ENABLE_MTOM, Constants.VALUE_TRUE);
        sc.setOptions(opts);

        OMElement res = sc.sendReceive(createPayload(inputfile));


        File outFile = new File(outFilename);
        OMNode node = (res.getFirstElement()).getFirstOMChild();
        
        if (node instanceof OMText) {
            OMText txt = (OMText) node;
            if (txt.isOptimized()) {
                DataHandler dh = (DataHandler) txt.getDataHandler();
                dh.writeTo(new FileOutputStream(outFile));

            } else {
            	//txt.setOptimize(true);
                DataHandler dh = (DataHandler) DataHandlerUtils.getDataHandlerFromText(txt.getText(), null);
                dh.writeTo(new FileOutputStream(outFile));
            }
        }
        
        System.out.println("File Downloaded Successfully!!");
    }
	
	public static OMElement createPayload(String fileName){
		OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace(
                "http://service.wsas.wso2.org", "wsasns");
        OMElement method = fac.createOMElement("getFile", omNs);
        OMElement value = fac.createOMElement("FileName", omNs);
        value.addChild(fac.createOMText(value, fileName));
        method.addChild(value);

        return method;
	}
}
	


