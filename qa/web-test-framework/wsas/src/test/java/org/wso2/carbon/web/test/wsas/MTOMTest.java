package org.wso2.carbon.web.test.wsas;

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;
import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.ServiceManagement;
import org.wso2.carbon.web.test.ds.common.BrowserInitializer;
import org.apache.axiom.om.*;
import org.apache.axiom.attachments.utils.DataHandlerUtils;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.client.Options;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.Constants;

import javax.activation.DataHandler;
import java.io.*;
import java.util.Properties;/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *   * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */


public class MTOMTest extends TestCase {

    Selenium browser;
    Properties property;
    String username;
    String password;

    public MTOMTest(String s){
        super(s);
    }

    public void setUp() throws Exception {
        property = BrowserInitializer.getProperties();
        browser = BrowserInitializer.getBrowser();
        username = property.getProperty("admin.username");
        password = property.getProperty("admin.password");
    }

    public void testenableMTOM() throws Exception {
        File aarPath = new File("." + File.separator + "lib" + File.separator + "MtomFileDownloadService.aar");

        SeleniumTestBase InstSeleniumTestBase = new SeleniumTestBase(browser);

        InstSeleniumTestBase.loginToUI(username, password);

        browser.click("link=Axis2 Service");
        browser.waitForPageToLoad("30000");
        InstSeleniumTestBase.SetFileBrowse("aarFilename", aarPath.getCanonicalPath());
        browser.click("upload");
        browser.waitForPageToLoad("30000");
        //	assertTrue(browser.isTextPresent("Files have been uploaded successfully. Please refresh this page in a while to see the status of the created Axis2 service"));
        browser.click("//button[@type='button']");
        Thread.sleep(12000);
        ServiceManagement serviceManagement = new ServiceManagement(browser);
        serviceManagement.accessServiceDashboard("MtomFileDownloadService");
        InstSeleniumTestBase.changeMTOMState("True");
        Thread.sleep(3000);
    }

    public static OMElement createPayload(String fileName) {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace(
                "http://service.wsas.wso2.org", "wsasns");
        OMElement method = fac.createOMElement("getFile", omNs);
        OMElement value = fac.createOMElement("fileName", omNs);

        value.addChild(fac.createOMText(value, fileName));
        method.addChild(value);

        return method;
    }



    public void testMTOMFileDownload() throws Exception {
        String carbon_home = property.getProperty("carbon.home");
        String inputfile = carbon_home + File.separator + "README.txt";
        File outFilePath = new File("." + File.separator + "lib" + File.separator + "temp" + File.separator + "downloaded_readme.txt");
        String outFilename = outFilePath.getCanonicalPath();
        String serviceepr = "http://" +  property.getProperty("host.name") + ":" +  property.getProperty("http.be.port") +  property.getProperty("context.root") + "/services/" + "MtomFileDownloadService";

        ServiceClient sc = new ServiceClient();
        Options opts = new Options();

        opts.setTo(new EndpointReference(serviceepr));
        opts.setAction("urn:getFile");
        opts.setProperty(Constants.Configuration.ENABLE_MTOM, Constants.VALUE_TRUE);
        sc.setOptions(opts);

        OMElement res = sc.sendReceive(createPayload(inputfile));


        File outFile = new File(outFilename);
        OMNode node = (res.getFirstElement()).getFirstOMChild();

        if (node instanceof OMText) {
            OMText txt = (OMText) node;
            if (txt.isOptimized()) {
                DataHandler dh = (DataHandler) txt.getDataHandler();
                System.out.println(outFile);
                dh.writeTo(new FileOutputStream(outFile));

            } else {
                //txt.setOptimize(true);
                DataHandler dh = (DataHandler) DataHandlerUtils.getDataHandlerFromText(txt.getText(), null);
                dh.writeTo(new FileOutputStream(outFile));
            }
        }

        System.out.println("File Downloaded Successfully!!");

        BufferedReader in = new BufferedReader(new FileReader(inputfile));
        String inStream = in.readLine();
        BufferedReader out = new BufferedReader(new FileReader(outFile));
        String outStream = out.readLine();
        assertEquals(inStream, outStream);
    }

    public void testRemoveMtomFileDownloadService() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
        instSeleniumTestBase.deleteService("MtomFileDownloadService");
    }

    public void testLogoutMtomTest() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
        instSeleniumTestBase.logOutUI();
    }

}
