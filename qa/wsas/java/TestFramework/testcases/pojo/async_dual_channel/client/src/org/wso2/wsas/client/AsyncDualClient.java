package org.wso2.wsas.client;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.wso2.wsas.service.AsyncMathServiceCallbackHandler;
import org.wso2.wsas.service.AsyncMathServiceStub;


public class AsyncDualClient {

	
	
	public static void main(String[] args)throws AxisFault{
		ConfigurationContext ConfigContext = 
		    ConfigurationContextFactory.createConfigurationContextFromFileSystem(args[0] ,args[0]+"\\axis2.xml");
		AsyncMathServiceStub stub = new AsyncMathServiceStub(ConfigContext);
		
		AsyncMathServiceCallbackHandler callback = new AsyncMathServiceCallbackHandler(){
			public void receiveResultsum(AsyncMathServiceStub.SumResponse result){
				System.out.println(result.get_return());
			}
		};
		
		AsyncMathServiceStub.Sum request = new AsyncMathServiceStub.Sum();
		request.setM(10);
		request.setN(20);
		
		//Do dual channel blocking invocation
		
		stub._getServiceClient().getOptions().setUseSeparateListener(true);
		stub._getServiceClient().getOptions().setTransportInProtocol(Constants.TRANSPORT_HTTP);
		stub._getServiceClient().engageModule("addressing");
		
		try {
			stub.startsum(request, callback);
		} catch (RemoteException e1) {
				e1.printStackTrace();
		}
	
	
	try {
		Thread.sleep(10000);
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
	
	ConfigContext.getListenerManager().stop();

		
	
	}

	}