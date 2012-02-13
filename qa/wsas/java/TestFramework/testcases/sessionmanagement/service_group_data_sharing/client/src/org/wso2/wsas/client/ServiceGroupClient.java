//Test client to share data between two services in a service group with SoapSession

package org.wso2.wsas.client;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.rmi.RemoteException;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXBuilder;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axiom.om.impl.llom.OMElementImpl;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.xml.utils.QName;
import org.wso2.wsas.service.ServiceGroupTestService1Stub;
import org.wso2.wsas.service.ServiceGroupTestService2Stub;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;

public class ServiceGroupClient {
	
	public static void main(String[] args) throws AxisFault, Exception{
	
		ConfigurationContext cc = ConfigurationContextFactory.createConfigurationContextFromFileSystem(args[2],null);
		ServiceGroupTestService1Stub stub1 = new ServiceGroupTestService1Stub(cc);
		ServiceGroupTestService1Stub.Multiply request = new ServiceGroupTestService1Stub.Multiply();
		
		ServiceClient sc = stub1._getServiceClient();
		EndpointReference To = new EndpointReference();
		To.setAddress(args[0]);
		
		Options opts1 = stub1._getServiceClient().getOptions();
		opts1.setTo(To);
		opts1.setManageSession(true);
		sc.engageModule("addressing");
		sc.setOptions(opts1);
		request.setX(20);
		request.setY(35);
		
	try {
		stub1.multiply(request);
	} catch (RemoteException e1) {
		e1.printStackTrace();
	}
	
	OMElement serviceGroupID = (OMElement) sc.getServiceContext().getTargetEPR().getAllReferenceParameters().get(new javax.xml.namespace.QName("http://ws.apache.org/namespaces/axis2","ServiceGroupId"));
	
		
		ServiceGroupTestService2Stub stub2 = new ServiceGroupTestService2Stub();
		ServiceClient sc2 = stub2._getServiceClient();
		Options opts2 = sc2.getOptions();
		opts2.setManageSession(true);
		sc2.engageModule("addressing");
		
		EndpointReference newTo = new EndpointReference();
		newTo.setAddress(args[1]);
//		 adding the reference parameter
		newTo.addReferenceParameter(serviceGroupID);

		opts2.setTo(newTo);
		sc2.setOptions(opts2);
		
		try {
			ServiceGroupTestService2Stub.GetResultResponse response = stub2.getResult();
			System.out.println(response.get_return());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	

}
