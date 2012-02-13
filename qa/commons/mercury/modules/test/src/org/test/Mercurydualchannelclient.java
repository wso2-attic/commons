package org.test;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.description.AxisOperation;
import org.apache.axis2.description.OutInAxisOperation;



public class Mercurydualchannelclient {
	public static void main(String[] args) throws AxisFault{
		ConfigurationContext cc = ConfigurationContextFactory.createConfigurationContextFromFileSystem("C:\\wsas\\Mercury\\client-repo","C:\\wsas\\Mercury\\client-repo\\conf\\axis2.xml");
		MercurymsgsequenceserviceStub stub = new MercurymsgsequenceserviceStub(cc,"http://10.100.1.207:9763/services/Mercuryservice");
		
		MercurymsgsequenceserviceCallbackHandler callback = new MercurymsgsequenceserviceCallbackHandler(){
			public void receiveResultechoInt(
		            MercurymsgsequenceserviceStub.EchoIntResponse result
		                ) {
			     	    	 System.out.println(result.get_return());
		   }
		 
		};
				
		MercurymsgsequenceserviceStub.EchoInt req = new MercurymsgsequenceserviceStub.EchoInt();
		req.setX(23);
		
//		stub._getServiceClient().getAxisService().addOperation(new OutInAxisOperation(ServiceClient.ANON_OUT_IN_OP));
//		 Mercury provides WSRM support
		stub._getServiceClient().engageModule("Mercury");
		stub._getServiceClient().getOptions().setProperty("MercurySequenceOffer", Constants.VALUE_TRUE);
		
		stub._getServiceClient().getOptions().setUseSeparateListener(true);
		stub._getServiceClient().getOptions().setTransportInProtocol(Constants.TRANSPORT_HTTP);
		stub._getServiceClient().getOptions().setTo(new EndpointReference("http://10.100.1.207:9763/services/Mercuryservice"));
		stub._getServiceClient().engageModule("addressing");
		
		try {
			stub.startechoInt(req, callback);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			Thread.sleep(1000000);
			cc.getListenerManager().stop();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
