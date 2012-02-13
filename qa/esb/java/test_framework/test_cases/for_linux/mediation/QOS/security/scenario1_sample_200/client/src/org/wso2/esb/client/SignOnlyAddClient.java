package org.wso2.esb.client;

import java.rmi.RemoteException;

import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.neethi.Policy;
import org.apache.neethi.PolicyEngine;
import org.apache.rampart.RampartMessageData;
import org.wso2.wsas.client.AddingStub;

public class SignOnlyAddClient {
	public static void main(String[] args)throws AxisFault {
		System.setProperty("javax.net.ssl.trustStore", "/home/evanthika/WSO2/releases/wso2esb-1.7/webapp/WEB-INF/classes/conf/identity.jks");
		 System.setProperty("javax.net.ssl.trustStorePassword", "password");
		
		ConfigurationContext ctx = ConfigurationContextFactory.createConfigurationContextFromFileSystem("/opt/sign_only/client_repo", null);
		AddingStub stub = new AddingStub(ctx,"https://localhost:8080/soap");
		AddingStub.Add request = new AddingStub.Add();
		
		request.setX(10);
		request.setY(10);
		
		Options options = stub._getServiceClient().getOptions();
		try {
			options.setProperty(RampartMessageData.KEY_RAMPART_POLICY,  loadPolicy("/opt/sign_only/policy/policy.xml"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		stub._getServiceClient().setOptions(options);
		stub._getServiceClient().engageModule("rampart");
try {
	AddingStub.AddResponse res = stub.add(request);
	System.out.println(res.get_return());
} catch (RemoteException e) {
	e.printStackTrace();
}
	}
	private static Policy loadPolicy(String xmlPath) throws Exception {
        StAXOMBuilder builder = new StAXOMBuilder(xmlPath);
        return PolicyEngine.getPolicy(builder.getDocumentElement());
    }
}

