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

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClientUtil {
    private static final Log log = LogFactory.getLog(HttpClientUtil.class);

    public OMElement get(String endpoint) {
        log.info("Endpoint : " + endpoint);
        try {
            URL url = new URL(endpoint);
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            InputStream in = httpCon.getInputStream();
            String xmlContent = getStringFromInputStream(in);
            in.close();
            return AXIOMUtil.stringToOM(xmlContent);
        } catch (Exception e) {
            Assert.fail("Failed to get the response :" + e.getMessage());
        }
        return null;
    }

    public void delete(String endpoint, String params) {
        log.info("Endpoint : " + endpoint);
        try {
            URL url = new URL(endpoint + "?" + params);
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setRequestMethod("DELETE");
            httpCon.getResponseCode();
        } catch (Exception e) {
            Assert.fail("Failed to get the response :" + e.getMessage());
        }
    }

    public void post(String endpoint, String params) {
        log.info("Endpoint : " + endpoint);
        try {
            URL url = new URL(endpoint);
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setDoOutput(true);
            OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());
            out.write(params);
            out.close();
            httpCon.getInputStream().close();
        } catch (Exception e) {
            Assert.fail("Failed to get the response :" + e.getMessage());
        }
    }

    public void put(String endpoint, String params) {
        log.info("Endpoint : " + endpoint);
        try {
            URL url = new URL(endpoint);
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setDoOutput(true);
            httpCon.setRequestMethod("PUT");
            httpCon.setRequestProperty("Content-Length", String.valueOf(params.length()));
            httpCon.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
            OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());
            out.write(params);
            out.close();
            httpCon.getInputStream().close();
        } catch (Exception e) {
            Assert.fail("Failed to get the response :" + e.getMessage());
        }
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
            Assert.fail("Failed to get the response :" + e.getMessage());
        }
        return retValue.toString();
    }
}

