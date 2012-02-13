package org.wso2.wsas.client;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.wso2.wsas.service.EchoServiceStub;
import org.wso2.wsas.service.EchoStringDocument;
import org.wso2.wsas.service.EchoStringResponseDocument;
import org.wso2.wsas.service.EchoStringDocument.EchoString;


/* This is a sample client to verify XMLBeans code generation
 * Written by Charitha Kankanamge 
 */

public class EchoXmlBeansClient {
	
	public static void main(String[] args)throws AxisFault {
		
		EchoServiceStub stub = new EchoServiceStub();
		EchoStringDocument reqDoc = EchoStringDocument.Factory.newInstance();
		
		EchoStringDocument.EchoString echoString = reqDoc.addNewEchoString(); 
		echoString.setS("XML Beans Code generation and invocation is successful");
		
		reqDoc.setEchoString(echoString);
		
		
		try {
			EchoStringResponseDocument resDoc = stub.echoString(reqDoc);
			System.out.println(resDoc.getEchoStringResponse().getReturn());
		} catch (RemoteException e) {
				e.printStackTrace();
		}
		
	}

}
