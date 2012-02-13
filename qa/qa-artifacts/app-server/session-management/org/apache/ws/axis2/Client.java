package org.apache.ws.axis2;

import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;

import java.util.Random;
import java.rmi.RemoteException;

/**
 * Created by IntelliJ IDEA.
 * User: charitha
 * Date: Nov 11, 2009
 * Time: 6:34:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class Client {

      public static void main(String[] args) throws AxisFault {

        Random random = new Random();

		for(int x=0; x<100000; x++){
			 int j = random.nextInt(1000);
             int k = random.nextInt(2000);
		ConfigurationContext configContext = ConfigurationContextFactory.createConfigurationContextFromFileSystem("/home/charitha/products/wsas/wso2wsas-3.1.2/repository", null);

		SpsessionservctxserviceStub stb1 = new SpsessionservctxserviceStub(configContext, "http://192.168.122.1:9763/services/spsessionservctxservice/");
		SpsessionservctxserviceStub.Multiply request = new SpsessionservctxserviceStub.Multiply();

		ServiceClient sc = stb1._getServiceClient();
		//sc.engageModule("addressing");
		stb1._getServiceClient().engageModule("addressing");

		Options opts = sc.getOptions();
		opts.setManageSession(true);

		sc.setOptions(opts);
		request.setX(j);
		request.setY(k);


		try {
			stb1.multiply(request);
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			Thread.sleep(1);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		SpsessionservctxserviceStub stb2 = new SpsessionservctxserviceStub(configContext,"http://192.168.122.1:9763/services/spsessionservctxservice/");
		stb2._setServiceClient(sc);
		stb2._getServiceClient().getOptions().setTo(new EndpointReference("http://192.168.122.1:9763/services/spsessionservctxservice/"));
		try {
			SpsessionservctxserviceStub.GetResultResponse res = stb2.getResult();
			System.out.println(res.get_return());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


    }
}
