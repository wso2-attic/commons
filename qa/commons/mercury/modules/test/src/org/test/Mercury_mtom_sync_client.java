package org.test;

import java.io.File;
import java.rmi.RemoteException;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.ws.axis2.mtomsample.RM_MTOM_ServiceStub;

public class Mercury_mtom_sync_client {
	
	public static void main(String[] args)throws AxisFault {
		String filepath = "C:\\mydocs\\000_0469.jpg";
		File file = new File(filepath);
		ConfigurationContext cc = ConfigurationContextFactory.createConfigurationContextFromFileSystem("C:\\wsas\\Mercury\\client-repo","C:\\wsas\\Mercury\\client-repo\\conf\\axis2.xml");
		
		RM_MTOM_ServiceStub stub = new RM_MTOM_ServiceStub(cc,"http://10.100.1.207:9763/services/RM_MTOM_Service");
		stub._getServiceClient().getOptions().setProperty(Constants.Configuration.ENABLE_MTOM, Constants.VALUE_TRUE);
		RM_MTOM_ServiceStub.AttachmentRequest attachmentrequest = new RM_MTOM_ServiceStub.AttachmentRequest();
		RM_MTOM_ServiceStub.AttachmentType attachmenttype = new RM_MTOM_ServiceStub.AttachmentType();
		RM_MTOM_ServiceStub.Base64Binary base64binary = new RM_MTOM_ServiceStub.Base64Binary();
		
		stub._getServiceClient().engageModule("Mercury");
		stub._getServiceClient().getOptions().setProperty("MercurySequenceOffer", Constants.VALUE_TRUE);
		
        FileDataSource fileDataSource = new FileDataSource(file);
		DataHandler dataHandler = new DataHandler(fileDataSource);
		base64binary.setBase64Binary(dataHandler);
		RM_MTOM_ServiceStub.ContentType_type0 param = new RM_MTOM_ServiceStub.ContentType_type0();
		
		param.setContentType_type0(dataHandler.getContentType());
        base64binary.setContentType(param);
		attachmenttype.setBinaryData(base64binary);
		attachmenttype.setFileName("C:\\mtom.jpg");
		attachmentrequest.setAttachmentRequest(attachmenttype);

		try {
			RM_MTOM_ServiceStub.AttachmentResponse response = stub.attachment(attachmentrequest);
			System.out.println(response.getAttachmentResponse());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
//		Setting the second message as last message
		stub._getServiceClient().getOptions().setProperty("WSO2RMLastMessage", Constants.VALUE_TRUE);
		try {
			RM_MTOM_ServiceStub.AttachmentResponse response = stub.attachment(attachmentrequest);
			System.out.println(response.getAttachmentResponse());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
