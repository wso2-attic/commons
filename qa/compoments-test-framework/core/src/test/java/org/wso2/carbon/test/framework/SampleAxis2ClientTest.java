/**
 *  Copyright (c) 2009, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.wso2.carbon.test.framework;

import org.apache.axiom.om.OMElement;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.wso2.carbon.test.framework.util.client.StockQuoteClient;
import org.wso2.carbon.test.framework.util.server.SampleAxis2Server;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;

/**
 * Created by IntelliJ IDEA.
 * User: suho
 * Date: Jun 14, 2010
 * Time: 11:03:03 AM
 * A sample to test to check whether the framework is working
 * To test this, Start the ESB with Sample 0
 */
public class SampleAxis2ClientTest extends initTestCase{

    @Before
    public void startAxis2Server() throws Exception {
        SampleAxis2Server.build("SimpleStockQuoteService");
        SampleAxis2Server.Start();
    }

    @Test
    public void runClient() throws Exception {
        System.setProperty("repository", AXIS2C_HOME +  File.separator+"client_repo");
        System.setProperty("addurl", "http://localhost:9000/services/SimpleStockQuoteService");
        System.setProperty("trpurl", "http://localhost:8280/");

        String resultString = getStringResultOfTest(StockQuoteClient.executeTestClient());

        Assert.assertTrue(resultString.contains(":last>"));
        Assert.assertTrue(resultString.contains("name>IBM Company<"));
    }

    @After
    public void stopAxis2Server() throws Exception {
        SampleAxis2Server.Stop();
    }

    protected String getStringResultOfTest(OMElement elem) throws Exception {
        OutputStream os = new ByteArrayOutputStream();
        elem.serialize(os);
        return os.toString();
    }


}
