package org.wso2.wsas.client;

import java.io.File;
import java.rmi.RemoteException;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.mtom.mtomfileupload.MTOMFileUploadStub;


public class MTOMFileUploadClient{

public static void main(String args[])throws AxisFault{
	

	if(args.length != 2) {
        System.out.println("Usage: $java <File source> <File destination>");
    }
	
	File file = new File(args[0]);
	
	MTOMFileUploadStub stub = new MTOMFileUploadStub();
	stub._getServiceClient().getOptions().setProperty(Constants.Configuration.ENABLE_MTOM, Constants.VALUE_TRUE);
	
	MTOMFileUploadStub.AttachmentRequest attachmentrequest = new MTOMFileUploadStub.AttachmentRequest();
	MTOMFileUploadStub.AttachmentType attachmenttype = new MTOMFileUploadStub.AttachmentType();
	MTOMFileUploadStub.Base64Binary base64binary = new MTOMFileUploadStub.Base64Binary();
	
	FileDataSource fileDataSource = new FileDataSource(file);
	
	DataHandler dataHandler = new DataHandler(fileDataSource);
	base64binary.setBase64Binary(dataHandler);
    MTOMFileUploadStub.ContentType_type0 param = new MTOMFileUploadStub.ContentType_type0();
    param.setContentType_type0(dataHandler.getContentType());
    base64binary.setContentType(param);
	attachmenttype.setBinaryData(base64binary);
	attachmenttype.setFileName(args[1]);
	attachmentrequest.setAttachmentRequest(attachmenttype);

	try {
		MTOMFileUploadStub.AttachmentResponse response = stub.attachment(attachmentrequest);
		System.out.println(response.getAttachmentResponse());
	} catch (RemoteException e) {
		e.printStackTrace();
	}
	

}
}