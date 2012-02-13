package org.test;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.test.MercurymsgsequenceserviceStub;

public class Mercurymsgsequenceclient {
	
	public static void main(String[] args) throws AxisFault {
		ConfigurationContext cc = ConfigurationContextFactory.createConfigurationContextFromFileSystem("C:\\wsas\\Mercury\\client-repo","C:\\wsas\\Mercury\\client-repo\\conf\\axis2.xml");
		MercurymsgsequenceserviceStub stub = new MercurymsgsequenceserviceStub(cc, "http://10.100.1.207:9763/services/Mercurymsgsequenceservice");
		MercurymsgsequenceserviceStub.EchoInt request = new MercurymsgsequenceserviceStub.EchoInt();
		
//		 Sandesha2 provides WSRM support
		stub._getServiceClient().engageModule("Mercury");
		stub._getServiceClient().getOptions().setProperty("MercurySequenceOffer", Constants.VALUE_TRUE);
		
		for (int i = 0; i < 10; i++) {
			request.setX(i);
			try {
				MercurymsgsequenceserviceStub.EchoIntResponse res = stub.echoInt(request);
				System.out.println(res.get_return());
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
//		Setting the second message as last message
		stub._getServiceClient().getOptions().setProperty("WSO2RMLastMessage", Constants.VALUE_TRUE);
		
		
		try {
			MercurymsgsequenceserviceStub.EchoIntResponse res = stub.echoInt(request);
			System.out.println(res.get_return());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}

}
