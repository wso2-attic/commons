/*
*Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*WSO2 Inc. licenses this file to you under the Apache License,
*Version 2.0 (the "License"); you may not use this file except
*in compliance with the License.
*You may obtain a copy of the License at
*
*http://www.apache.org/licenses/LICENSE-2.0
*
*Unless required by applicable law or agreed to in writing,
*software distributed under the License is distributed on an
*"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*KIND, either express or implied.  See the License for the
*specific language governing permissions and limitations
*under the License.
*/
package org.wso2.carbon.system.test.core.utils.httpClient;


import junit.framework.Assert;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.stream.XMLStreamException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClientUtil {
    private static final Log log = LogFactory.getLog(HttpClientUtil.class);
    private static final int connectionTimeOut = 30000;

    public OMElement get(String endpoint) {
        log.info("Endpoint : " + endpoint);
        HttpURLConnection httpCon = null;
        String xmlContent = null;
        int responseCode = -1;
        try {
            URL url = new URL(endpoint);
            httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setConnectTimeout(connectionTimeOut);
            InputStream in = httpCon.getInputStream();
            xmlContent = getStringFromInputStream(in);
            responseCode = httpCon.getResponseCode();
            in.close();
        } catch (Exception e) {
            log.error("Failed to get the response " + e.getMessage());
            Assert.fail("Failed to get the response :" + e.getMessage());
        } finally {
            if(httpCon != null) {
                httpCon.disconnect();
            }
        }
        Assert.assertEquals("Response code not 200", 200, responseCode);
        if(xmlContent != null) {
            try {
                return AXIOMUtil.stringToOM(xmlContent);
            } catch (XMLStreamException e) {
                log.error("Error while processing response to OMElement" + e.getMessage());
                Assert.fail("Error while processing response to OMElement" + e.getMessage());
                return  null;
            }
        } else{
            return  null;
        }
    }

    public void delete(String endpoint, String params) {
        log.info("Endpoint : " + endpoint);
        HttpURLConnection httpCon = null;
        int responseCode = -1;
        try {
            URL url = new URL(endpoint + "?" + params);
            httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setConnectTimeout(connectionTimeOut);
            httpCon.setRequestMethod("DELETE");
            responseCode = httpCon.getResponseCode();
        } catch (Exception e) {
            log.error("Failed to get the response " + e.getMessage());
            Assert.fail("Failed to get the response :" + e.getMessage());
        }finally{
           if(httpCon != null) {
                httpCon.disconnect();
            }
        }
        Assert.assertEquals("Response Code not 202",202, responseCode);
    }

    public void post(String endpoint, String params) {
        log.info("Endpoint : " + endpoint);
        HttpURLConnection httpCon = null;
        int responseCode = -1;
        try {
            URL url = new URL(endpoint);
            httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setConnectTimeout(connectionTimeOut);
            httpCon.setDoOutput(true);
            OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());
            out.write(params);
            out.close();
            responseCode = httpCon.getResponseCode();
            httpCon.getInputStream().close();

        } catch (Exception e) {
            log.error("Failed to get the response " + e.getMessage());
            Assert.fail("Failed to get the response :" + e.getMessage());
        } finally {
              if(httpCon != null) {
                httpCon.disconnect();
            }
        }
        Assert.assertEquals("Response Code not 202", 202, responseCode);
    }

public void post(String endpoint, String params, String contentType) {
        log.info("Endpoint : " + endpoint);
        HttpURLConnection httpCon = null;
        int responseCode = -1;
        try {
            URL url = new URL(endpoint);
            httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setConnectTimeout(connectionTimeOut);
            httpCon.setRequestProperty("Content-type", contentType);
            httpCon.setDoOutput(true);
            OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());
            out.write(params);
            out.close();
            responseCode = httpCon.getResponseCode();
            httpCon.getInputStream().close();

        } catch (Exception e) {
            log.error("Failed to get the response " + e.getMessage());
            Assert.fail("Failed to get the response :" + e.getMessage());
        } finally {
              if(httpCon != null) {
                httpCon.disconnect();
            }
        }
        Assert.assertEquals("Response Code not 202", 202, responseCode);
    }


    public void put(String endpoint, String params) {
        log.info("Endpoint : " + endpoint);
        HttpURLConnection httpCon = null;
        int responseCode = -1;
        try {
            URL url = new URL(endpoint);
            httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setConnectTimeout(connectionTimeOut);
            httpCon.setDoOutput(true);
            httpCon.setRequestMethod("PUT");
            httpCon.setRequestProperty("Content-Length", String.valueOf(params.length()));
            httpCon.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
            OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());
            out.write(params);
            out.close();
            responseCode = httpCon.getResponseCode();
            httpCon.getInputStream().close();
        } catch (Exception e) {
            log.error("Failed to get the response " + e.getMessage());
            Assert.fail("Failed to get the response :" + e.getMessage());
        } finally {
              if(httpCon != null) {
                httpCon.disconnect();
            }
        }
        Assert.assertEquals("Response Code not 202", 202, responseCode);
    }

    private static String getStringFromInputStream(InputStream in) {
        InputStreamReader reader = new InputStreamReader(in);
        char[] buff = new char[1024];
        int i;
        StringBuffer retValue = new StringBuffer();
        try {
            while ((i = reader.read(buff)) > 0) {
                retValue.append(new String(buff, 0, i));
            }
        } catch (Exception e) {
            log.error("Failed to get the response " + e.getMessage());
            Assert.fail("Failed to get the response :" + e.getMessage());
        }
        return retValue.toString();
    }
}

