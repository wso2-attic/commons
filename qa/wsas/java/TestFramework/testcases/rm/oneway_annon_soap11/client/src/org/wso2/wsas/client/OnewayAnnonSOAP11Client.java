package org.wso2.wsas.client;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.wso2.wsas.service.SandeshaServiceStub;
import org.apache.sandesha2.client.SandeshaClientConstants;

public class OnewayAnnonSOAP11Client{

public static void main(String args[])throws AxisFault{
	
	ConfigurationContext cc = ConfigurationContextFactory.createConfigurationContextFromFileSystem(args[0],args[0]+"\\conf\\axis2.xml");

	SandeshaServiceStub stub = new SandeshaServiceStub(cc);
	SandeshaServiceStub.Ping request = new SandeshaServiceStub.Ping();
	request.setS("ping");
	
	stub._getServiceClient().engageModule("sandesha2");
	
	for (int i = 0; i < 9; i++) {

	try {
		stub.Ping(request);
	} catch (RemoteException e) {
		e.printStackTrace();
	}
	
	try {
		Thread.sleep(1000);
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
	}
	
//	Setting the last message
	stub._getServiceClient().getOptions().setProperty(SandeshaClientConstants.LAST_MESSAGE, Constants.VALUE_TRUE);
	
	try {
		stub.Ping(request);
	} catch (RemoteException e) {
		e.printStackTrace();
	}
	
	try {
		Thread.sleep(1000);
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
}
}