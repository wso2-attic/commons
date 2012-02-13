package org.wso2.wsas.client;

/*Client to invoke Echo service through Secure Conversation policy
 * Written by Charitha Kankanamge
 */


import java.rmi.RemoteException;

import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.neethi.Policy;
import org.apache.neethi.PolicyEngine;
import org.apache.rampart.RampartMessageData;
import org.wso2.wsas.service.Rampart_SecureConversation_Echo_serviceStub;
import org.apache.axis2.Constants;

public class SecureConClient{
	
	
public static void main(String args[])throws AxisFault{
	
	if(args.length != 3) {
        System.out.println("Usage: $java Client WSAS_HOME client_repo_path policy_xml_path endpoint");
    }
	
	System.setProperty("javax.net.ssl.trustStore", args[0]+"\\resources\\security\\wso2carbon.jks");
	System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
	
	ConfigurationContext ctx = ConfigurationContextFactory.createConfigurationContextFromFileSystem(args[1], null);
	Rampart_SecureConversation_Echo_serviceStub stub = new Rampart_SecureConversation_Echo_serviceStub(ctx,args[3]);
	Rampart_SecureConversation_Echo_serviceStub.EchoString request1 = new Rampart_SecureConversation_Echo_serviceStub.EchoString();
	request1.setS("Secure Conversation Request 1");
	Rampart_SecureConversation_Echo_serviceStub.EchoString request2 = new Rampart_SecureConversation_Echo_serviceStub.EchoString();
	request2.setS("Secure Conversation Request 2");
	Rampart_SecureConversation_Echo_serviceStub.EchoString request3 = new Rampart_SecureConversation_Echo_serviceStub.EchoString();
	request3.setS("Secure Conversation Request 3");
	
	Options options = stub._getServiceClient().getOptions();
	try {
		options.setProperty(RampartMessageData.KEY_RAMPART_POLICY,  loadPolicy(args[2]));
		//options.setSoapVersionURI(Constants.URI_SOAP12_ENV);
	} catch (Exception e) {		
		e.printStackTrace();
	}
	stub._getServiceClient().setOptions(options);
	//stub._getServiceClient().engageModule("addressing");
	stub._getServiceClient().engageModule("rampart");
	
	try {
		Rampart_SecureConversation_Echo_serviceStub.EchoStringResponse response1 = stub.echoString(request1);
		System.out.println("Response 1 of "+response1.get_return());
		Rampart_SecureConversation_Echo_serviceStub.EchoStringResponse response2 = stub.echoString(request2);
		System.out.println("Response 2 of "+response2.get_return());
		Rampart_SecureConversation_Echo_serviceStub.EchoStringResponse response3 = stub.echoString(request3);
		System.out.println("Response 3 of "+response3.get_return());
	} catch (RemoteException e) {
		e.printStackTrace();
	}
}


private static Policy loadPolicy(String xmlPath) throws Exception {
    StAXOMBuilder builder = new StAXOMBuilder(xmlPath);
    return PolicyEngine.getPolicy(builder.getDocumentElement());
}

}