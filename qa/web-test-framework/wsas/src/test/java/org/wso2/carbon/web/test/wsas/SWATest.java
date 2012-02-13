package org.wso2.carbon.web.test.wsas;

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;
import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.ds.common.BrowserInitializer;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.client.OperationClient;
import org.apache.axis2.Constants;
import org.apache.axis2.wsdl.WSDLConstants;
import org.apache.axis2.context.MessageContext;
import org.apache.axiom.soap.SOAP11Constants;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPBody;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.OMElement;

import javax.activation.FileDataSource;
import javax.activation.DataHandler;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.util.Properties;

/*
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

public class SWATest extends TestCase {

    Selenium browser;
    Properties property;
    String username;
    String password;

    public SWATest(String s){
        super(s);
    }

    public void setUp() throws Exception {
        browser = BrowserInitializer.getBrowser();
        property = BrowserInitializer.getProperties();
        username = property.getProperty("admin.username");
        password = property.getProperty("admin.password");
    }

    public void testDeploySWAService() throws Exception {
        SeleniumTestBase InstSeleniumTestBase = new SeleniumTestBase(browser);

        File aarPath = new File("." + File.separator + "lib" + File.separator + "swa-test.aar");

        InstSeleniumTestBase.loginToUI(username, password);

        browser.click("link=Axis2 Service");
        browser.waitForPageToLoad("30000");
        InstSeleniumTestBase.SetFileBrowse("aarFilename", aarPath.getCanonicalPath());
        browser.click("upload");
        browser.waitForPageToLoad("30000");
        browser.click("//button[@type='button']");
        Thread.sleep(12000);
    }


    public void testSWAFileUpload() throws Exception {
        String carbon_home = property.getProperty("carbon.home");
        String inputfile = carbon_home + File.separator + "README.txt";
        File outFilePath = new File("." + File.separator + "lib" + File.separator + "temp" + File.separator + "swa_uploaded_readme.txt");
        String destFile = outFilePath.getCanonicalPath();

        File file = new File(inputfile);
        if (file.exists())
            transferFile(file, destFile);
        else
            throw new FileNotFoundException();
    }


    public void transferFile(File file, String destinationFile) throws Exception {
        Options options = new Options();

        String serviceepr = "http://" + property.getProperty("host.name") + ":" + property.getProperty("http.be.port") + property.getProperty("context.root") + "/services/" + "SWATestService";

        options.setTo(new EndpointReference(serviceepr));
        options.setProperty(Constants.Configuration.ENABLE_SWA, Constants.VALUE_TRUE);
        options.setSoapVersionURI(SOAP11Constants.SOAP_ENVELOPE_NAMESPACE_URI);

        // Increase the time out when sending large attachments
        options.setTimeOutInMilliSeconds(10000);
        options.setTo(new EndpointReference(serviceepr));
        options.setAction("urn:uploadFile");

        ServiceClient sender = new ServiceClient();
        sender.setOptions(options);
        OperationClient mepClient = sender
                .createClient(ServiceClient.ANON_OUT_IN_OP);

        MessageContext mc = new MessageContext();
        FileDataSource fileDataSource = new FileDataSource(file);


        DataHandler dataHandler = new DataHandler(fileDataSource);
        String attachmentID = mc.addAttachment(dataHandler);

        SOAPFactory fac = OMAbstractFactory.getSOAP11Factory();
        SOAPEnvelope env = fac.getDefaultEnvelope();
        OMNamespace omNs = fac.createOMNamespace(
                "http://service.soapwithattachments.sample", "swa");
        OMElement uploadFile = fac.createOMElement("uploadFile", omNs);
        OMElement nameEle = fac.createOMElement("name", omNs);
        nameEle.setText(destinationFile);
        OMElement idEle = fac.createOMElement("attchmentID", omNs);
        idEle.setText(attachmentID);
        uploadFile.addChild(nameEle);
        uploadFile.addChild(idEle);
        env.getBody().addChild(uploadFile);
        mc.setEnvelope(env);

        mepClient.addMessageContext(mc);
        mepClient.execute(true);
        MessageContext response = mepClient
                .getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
        SOAPBody body = response.getEnvelope().getBody();
        OMElement element = body.getFirstElement().getFirstChildWithName(
                new javax.xml.namespace.QName("http://service.soapwithattachments.sample", "return"));
        System.out.println(element.getText());
        assertEquals("File saved succesfully.", element.getText());
    }

    public void testRemoveSwaTestService() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
        instSeleniumTestBase.deleteService("swa-test");
    }

    public void testLogoutSWATTest() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
        instSeleniumTestBase.logOutUI();
    }
}
