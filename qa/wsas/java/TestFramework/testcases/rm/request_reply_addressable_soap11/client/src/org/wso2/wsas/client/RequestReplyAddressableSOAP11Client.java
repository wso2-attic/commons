package org.wso2.wsas.client;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.wso2.wsas.service.SandeshaServiceCallbackHandler;
import org.wso2.wsas.service.SandeshaServiceStub;
import org.apache.sandesha2.client.SandeshaClientConstants;

public class RequestReplyAddressableSOAP11Client{

public static void main(String args[])throws AxisFault{
	
	ConfigurationContext cc = ConfigurationContextFactory.createConfigurationContextFromFileSystem(args[0],args[0]+"\\conf\\axis2.xml");

	SandeshaServiceStub stub = new SandeshaServiceStub(cc);
	SandeshaServiceCallbackHandler callback = new SandeshaServiceCallbackHandler(){
		public void receiveResultAdd(org.wso2.wsas.service.SandeshaServiceStub.AddResponse result){
			System.out.println(result.get_return());
		}
	};
	
	SandeshaServiceStub.Add request = new SandeshaServiceStub.Add();
	request.setA(2);
	request.setB(8);
	
	stub._getServiceClient().engageModule("sandesha2");
	stub._getServiceClient().engageModule("addressing");
	stub._getServiceClient().getOptions().setUseSeparateListener(true);
	
	for (int i = 0; i < 9; i++) {

	try {
		stub.startAdd(request, callback);
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
		stub.startAdd(request, callback);
	} catch (RemoteException e) {
		e.printStackTrace();
	}
	
	try {
		Thread.sleep(5000);
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
	cc.getListenerManager().stop();
}
}