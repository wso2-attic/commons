package org.wso2.wsas.client;

import java.rmi.RemoteException;

import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.neethi.Policy;
import org.apache.neethi.PolicyEngine;
import org.apache.rampart.RampartMessageData;
import org.wso2.wsas.service.Rampart_UT_Echo_serviceStub;

public class UTClient{
	
	
public static void main(String args[])throws AxisFault{
	
	if(args.length != 3) {
        System.out.println("Usage: $java Client WSAS_HOME client_repo_path policy_xml_path endpoint");
    }
	
	System.setProperty("javax.net.ssl.trustStore", args[0]+"\\resources\\security\\wso2carbon.jks");
	System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
	
	ConfigurationContext ctx = ConfigurationContextFactory.createConfigurationContextFromFileSystem(args[1], null);
	Rampart_UT_Echo_serviceStub stub = new Rampart_UT_Echo_serviceStub(ctx,args[3]);
	Rampart_UT_Echo_serviceStub.EchoString request = new Rampart_UT_Echo_serviceStub.EchoString();
	request.setS("Rampart UT over HTTPS");
	
	Options options = stub._getServiceClient().getOptions();
	try {
		options.setProperty(RampartMessageData.KEY_RAMPART_POLICY,  loadPolicy(args[2]));
	} catch (Exception e) {		
		e.printStackTrace();
	}
	stub._getServiceClient().setOptions(options);
	stub._getServiceClient().engageModule("rampart");
	
	try {
		Rampart_UT_Echo_serviceStub.EchoStringResponse response = stub.echoString(request);
		System.out.println(response.get_return());
	} catch (RemoteException e) {
		e.printStackTrace();
	}
	

}

private static Policy loadPolicy(String xmlPath) throws Exception {
    StAXOMBuilder builder = new StAXOMBuilder(xmlPath);
    return PolicyEngine.getPolicy(builder.getDocumentElement());
}

}