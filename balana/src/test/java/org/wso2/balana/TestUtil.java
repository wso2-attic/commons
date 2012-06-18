/*
 * @(#)TestUtil.java
 *
 * Copyright 2004 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   1. Redistribution of source code must retain the above copyright notice,
 *      this list of conditions and the following disclaimer.
 * 
 *   2. Redistribution in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *
 * Neither the name of Sun Microsystems, Inc. or the names of contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 * ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN MICROSYSTEMS, INC. ("SUN")
 * AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE
 * AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL,
 * INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY
 * OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE,
 * EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that this software is not designed or intended for use in
 * the design, construction, operation or maintenance of any nuclear facility.
 */

package org.wso2.balana;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.wso2.balana.ctx.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;


/**
 * Simple utility class 
 *
 * @author Seth Proctor
 */
public class TestUtil {

    private static Log log = LogFactory.getLog(TestUtil.class);

    /**
     * TODO
     * @param responseCtx
     * @param response
     * @return
     */
    public static boolean isCorrect(ResponseCtx responseCtx, String response) {

        String correctResponse = createResponse(response);
        String actualResponse = getXacmlResponse(responseCtx);

        if(correctResponse != null){
            correctResponse = correctResponse.replaceAll(">\\s+<", "><");
        } else {
            log.info("Response read from the file is null");
            return false;
        }

        if(actualResponse != null){
            actualResponse = actualResponse.replaceAll(">\\s+<", "><");
        } else {
            log.info("Response received from evaluation is null");
            return false;
        }

        log.info("Correct : "  + correctResponse);
        log.info("Actual  :  "  + actualResponse);        

        return  correctResponse.trim().equals(actualResponse.trim());
    }

    /**
     * 
     * @param requestId
     * @return
     * @throws Exception
     */
    public static AbstractRequestCtx createRequest(String requestId) throws Exception {

        File file = new File(".")  ;
        String filePath =  file.getCanonicalPath() + File.separator +
                                                        TestConstants.REQUEST_PATH + requestId;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringComments(true);
        factory.setNamespaceAware(true);
        factory.setValidating(false);
        DocumentBuilder db = factory.newDocumentBuilder();
        Document doc = db.parse(new FileInputStream(filePath));
        Element root = doc.getDocumentElement();
        return RequestCtxFactory.getFactory().getRequestCtx(root);

    }

    /**
     *
     * @param requestId
     * @return
     * @throws Exception
     */
    public static String createResponse(String requestId) {

        StringBuilder response = new StringBuilder();
        File file = new File(".");
        FileInputStream fileInputStream = null;
        DataInputStream dataInputStream = null;
        BufferedReader reader = null;
        try{
            String filePath =  file.getCanonicalPath() + File.separator +
                                                            TestConstants.RESPONSE_PATH + requestId;

            fileInputStream = new FileInputStream(filePath);
            dataInputStream = new DataInputStream(fileInputStream);
            reader = new BufferedReader(new InputStreamReader(dataInputStream));
            String line;
            while((line = reader.readLine()) != null){
                response.append(line);
            }

            return response.toString();
        } catch (Exception e){
            log.error("Error while creating ");
            return null;    
        } finally {
            try{
                if(fileInputStream != null){
                    fileInputStream.close();
                }

                if(dataInputStream != null){
                    dataInputStream.close();
                }

                if(reader != null){
                    reader.close();
                }
            } catch (IOException e) {
                log.error("Error while closing input streams");
            }
        }
    }

    /**
     *
     * @param responseCtx
     * @return
     */
    public static String getXacmlResponse(ResponseCtx responseCtx) {

        OutputStream stream = new ByteArrayOutputStream();
        responseCtx.encode(stream);
        String response = stream.toString();
        try {
            stream.close();
        } catch (IOException e) {
            log.error("Error while closing stream " + e);
        }
        return response;
        
    }

    /**
     *
     * @param requestCtx
     * @return
     */
    public static String getXacmlRequest(AbstractRequestCtx requestCtx) {

        OutputStream stream = new ByteArrayOutputStream();
        requestCtx.encode(stream);
        String request = stream.toString();
        try {
            stream.close();
        } catch (IOException e) {
            log.error("Error while closing stream " + e);
        }
        return request;
    }
    
}
