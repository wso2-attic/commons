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
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;


/**
 * Simple utility class 
 *
 * @author Seth Proctor
 */
public class TestUtil {

    private static Log log = LogFactory.getLog(TestUtil.class);

    /**
     * Checks matching of result that got from PDP and expected response from a file.
     *
     * @param resultResponse  result that got from PDP
     * @param expectedResponse  expected response from a file
     * @return True/False
     */
    public static boolean isMatching(String resultResponse, String expectedResponse) {

        boolean result = false;
        resultResponse = processResult(resultResponse);
        if(resultResponse != null && expectedResponse != null){
            result = resultResponse.trim().
                            equals(expectedResponse.trim());
        }

        if(result){
            log.info("Test is Passed........!!!   " +
                    "Result received from the PDP is matched with expected result");
        } else {
            log.info("Test is Failed........!!!     " +
                    "Result received from the PDP is NOT match with expected result");
        }
        return result;
    }

    /**
     * This creates the XACML request from a file
     *
     * @param rootDirectory   root directory of the  request files
     * @param versionDirectory   version directory of the  request files
     * @param requestId  request file name
     * @return String or null if any error
     */
    public static String createRequest(String rootDirectory, String versionDirectory,
                                                   String requestId){

        File file = new File(".");
        StringWriter writer = null;
        try{
            String filePath =  file.getCanonicalPath() + File.separator +   TestConstants.RESOURCE_PATH +
                        File.separator + rootDirectory + File.separator + versionDirectory +
                        File.separator + TestConstants.REQUEST_DIRECTORY + File.separator + requestId;

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setIgnoringComments(true);
            factory.setNamespaceAware(true);
            factory.setValidating(false);
            DocumentBuilder db = factory.newDocumentBuilder();
            Document doc = db.parse(new FileInputStream(filePath));
            DOMSource domSource = new DOMSource(doc);
            writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.transform(domSource, result);
            return writer.toString();
        } catch (Exception e){
            log.error("Error while reading expected response from file ", e);
            //ignore any exception and return null
        } finally {
            if(writer != null){
                try {
                    writer.close();
                } catch (IOException e) {
                    log.error("Error closing stream ", e);
                    //ignore any exception and return null
                }
            }
        }
        return null;
    }

    /**
     * This creates the expected XACML response from a file
     *
     * @param rootDirectory   root directory of the  response files
     * @param versionDirectory   version directory of the  response files
     * @param responseId  response file name
     * @return ResponseCtx or null if any error
     */
    public static ResponseCtx createResponse(String rootDirectory, String versionDirectory,
                                                                            String responseId) {

        File file = new File(".");
        try{
            String filePath =  file.getCanonicalPath() + File.separator +   TestConstants.RESOURCE_PATH +
                        File.separator + rootDirectory + File.separator + versionDirectory +
                        File.separator + TestConstants.RESPONSE_DIRECTORY + File.separator + responseId;

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setIgnoringComments(true);
            factory.setNamespaceAware(true);
            factory.setValidating(false);
            DocumentBuilder db = factory.newDocumentBuilder();
            Document doc = db.parse(new FileInputStream(filePath));
            return ResponseCtx.getInstance(doc.getDocumentElement());
        } catch (Exception e){
            log.error("Error while reading expected response from file ", e);
            //ignore any exception and return null
        }

        return null;
    }


    /**
     * This would remove the StatusMessage from the response. Because StatusMessage depends
     * on the how you have defined it with the PDP, Therefore we can not compare it with
     * conformance tests.
     *
     * @param response  XACML response String
     * @return XACML response String with out StatusMessage 
     */
    private static String processResult(String response){

        if(response.contains("StatusMessage")){
            response = response.substring(0, response.indexOf("<StatusMessage>")) + 
                 response.substring(response.indexOf("</Status>"));
        }

        return response;
    }
}
