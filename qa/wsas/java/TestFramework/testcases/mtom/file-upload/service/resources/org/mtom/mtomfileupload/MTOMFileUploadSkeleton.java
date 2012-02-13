
/**
 * MTOMFileUploadSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.35  Built on : Jan 15, 2008 (07:08:20 PST)
 */
    package org.mtom.mtomfileupload;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.activation.DataHandler;


    /**
     *  MTOMFileUploadSkeleton java skeleton for the axisService
     */
    public class MTOMFileUploadSkeleton{
        
         
        /**
         * Auto generated method signature
         * 
                                     * @param attachmentRequest
         */
        
                 public org.mtom.mtomfileupload.AttachmentResponse attachment
                  (
                  org.mtom.mtomfileupload.AttachmentRequest attachmentRequest
                  )throws IOException, FileNotFoundException
            {
                	 AttachmentType attachmenttype = attachmentRequest.getAttachmentRequest();
             		org.w3.www._2005._05.xmlmime.Base64Binary binaryData = attachmenttype.getBinaryData();
             		DataHandler dataHandler = binaryData.getBase64Binary();
             		File file = new File(
             				attachmenttype.getFileName());
             		FileOutputStream fileOutputStream = new FileOutputStream(file);
             		dataHandler.writeTo(fileOutputStream);
             		fileOutputStream.flush();
             		fileOutputStream.close();
             		
             		AttachmentResponse response = new AttachmentResponse();
             		response.setAttachmentResponse("File saved succesfully.");
             		return response;
        }
     
    }
    