package org.wso2.carbon.web.test.esb;

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;

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

public class ESBAddProxyServiceTest   extends TestCase {
    Selenium selenium;

    public ESBAddProxyServiceTest(Selenium _browser){
        selenium = _browser;
    }

   /*
    This method is used to add a new Proxy Service and specify the publish WSDL
    */
    public void testAddProxyService(String proxyName, String publishWsdl, String sourceUrl, String inlineXml, String resourceName, String startOnLoad, String pinnedServer) throws Exception{
	    ESBCommon esbCommon = new ESBCommon(selenium);

        selenium.click("link=Proxy Service");
        selenium.waitForPageToLoad("30000");
		selenium.type("psName", proxyName);

        if (publishWsdl.equals("Specify source URL")){
            selenium.select("publishWsdlCombo", "label=Specify source URL");
    		selenium.type("wsdlUriText", sourceUrl);
        } else if (publishWsdl.equals("Specify in-line")){
            selenium.select("publishWsdlCombo", "label=Specify in-line");
		    selenium.type("wsdlInlineText", inlineXml);
        } else if (publishWsdl.equals("Pick from registry")){
            selenium.select("publishWsdlCombo", "label=Pick from registry");
            selenium.click("link=Registry Browser");
            esbCommon.testSelectResource("Entry", resourceName);
        } else if (publishWsdl.equals("None")){
            selenium.select("publishWsdlCombo", "label=None");
        }

       if (startOnLoad.equals("true")){
           selenium.click("startOnLoad");
           selenium.click("startOnLoad");
       }else if (startOnLoad.equals("false")){
           selenium.click("startOnLoad");
       }

       if (pinnedServer!=null){
       selenium.type("pnnedServers", pinnedServer);
       }
   }

    /*
    This method will select the inSequence
     */
    public void testSelectInSequence(String inSeqOpt, String resourceName, String nodeType, String mediatorCategory, String mediatorName ) throws Exception{

        if (inSeqOpt.equals("inSeqOpAnon")){
            selenium.click("inSeqOpAnon");
            Thread.sleep(2000);
            selenium.click("inAnonAddEdit");
            selenium.waitForPageToLoad("30000");
            
            ESBCommon esbCommon = new ESBCommon(selenium);
            esbCommon.testAddRootLevelChildren(nodeType,mediatorCategory,mediatorName);
            esbCommon.testSequenceSave();
        } else if (inSeqOpt.equals("inSeqOpReg")){
            selenium.click("inSeqOpReg");
            selenium.click("//a[@onclick=\"showInLinedRegistryBrowser('proxy.in.registry');\"]");

            ESBCommon esbCommon = new ESBCommon(selenium);
            esbCommon.testSelectResource("Sequence",resourceName);
        } else if (inSeqOpt.equals("inSeqOpImp")){
		    selenium.click("inSeqOpImp");
    		selenium.select("inImportSeq", "label="+resourceName);
        } else if (inSeqOpt.equals("inSeqOpNone")){
		    selenium.click("inSeqOpNone");
        }
    }

    /*
    This method will select the endpoint
     */
    public void testSelectEndpoint(String endpointSeq, String resourceName, String epr) throws Exception{
        if (endpointSeq.equals("epOpAnon")){
            selenium.click("epOpAnon");
            Thread.sleep(2000);
            selenium.click("epAnonAddEdit");
            selenium.waitForPageToLoad("30000");
            
            ESBAddAddressEndpointTest esbAddAddressEndpointTest = new ESBAddAddressEndpointTest(selenium);
            esbAddAddressEndpointTest.testAddAnonAddressEndpoint();
            esbAddAddressEndpointTest.testAddAddressEprMandatoryInfo(null,epr);
            esbAddAddressEndpointTest.testSaveAddressEndpoint();
        } else if (endpointSeq.equals("epOpReg")){
            selenium.click("epOpReg");
            selenium.click("//a[@onclick=\"showInLinedRegistryBrowser('proxy.epr.registry')\"]");

            ESBCommon esbCommon = new ESBCommon(selenium);
            esbCommon.testSelectResource("Enpoint",resourceName);
        } else if (endpointSeq.equals("epOpImp")){
            selenium.click("epOpImp");
            selenium.select("importEp", "label="+resourceName);
        } else if (endpointSeq.equals("epOpNone")){
            selenium.click("epOpNone");
        }
    }

    /*
    This method will select the outSequence
     */
    public void testSelectOutSequence(String outSeqOpt, String resourceName, String nodeType, String mediatorCategory, String mediatorName) throws Exception{

        if (outSeqOpt.equals("outSeqOpAnon")){
            selenium.click("outSeqOpAnon");
            Thread.sleep(2000);
            selenium.click("outAnonAddEdit");
            selenium.waitForPageToLoad("30000");            

            ESBCommon esbCommon = new ESBCommon(selenium);
            esbCommon.testAddRootLevelChildren(nodeType,mediatorCategory,mediatorName);
            esbCommon.testSequenceSave();
        } else if (outSeqOpt.equals("outSeqOpReg")){
            selenium.click("outSeqOpReg");
            selenium.click("//a[@onclick=\"showInLinedRegistryBrowser('proxy.out.registry')\"]");

            ESBCommon esbCommon = new ESBCommon(selenium);
            esbCommon.testSelectResource("Sequence",resourceName);
        } else if (outSeqOpt.equals("outSeqOpImp")){
		    selenium.click("outSeqOpImp");
    		selenium.select("outImportSeq", "label="+resourceName);
        } else if (outSeqOpt.equals("outSeqOpNone")){
		    selenium.click("outSeqOpNone");
        }
    }

    /*
    This method will select the faultSequence
     */
    public void testSelectFaultSequence(String faultSeqOpt, String resourceName, String nodeType, String mediatorCategory, String mediatorName) throws Exception{
        if (faultSeqOpt.equals("faultSeqOpAnon")){
            selenium.click("faultSeqOpAnon");
            Thread.sleep(2000);
            selenium.click("faultAnonAddEdit");
            selenium.waitForPageToLoad("30000");

            ESBCommon esbCommon = new ESBCommon(selenium);
            esbCommon.testAddRootLevelChildren(nodeType,mediatorCategory,mediatorName);
            esbCommon.testSequenceSave();
        } else if (faultSeqOpt.equals("faultSeqOpReg")){
            selenium.click("faultSeqOpReg");
            selenium.click("//a[@onclick=\"showInLinedRegistryBrowser('proxy.fault.registry')\"]");

            ESBCommon esbCommon = new ESBCommon(selenium);
            esbCommon.testSelectResource("Sequence",resourceName);
        } else if (faultSeqOpt.equals("faultSeqOpImp")){
		    selenium.click("faultSeqOpImp");
		    selenium.select("faultImportSeq", "label="+resourceName);            
        } else if (faultSeqOpt.equals("faultSeqOpNone")){
		    selenium.click("faultSeqOpNone");
        }
    }

    /*
    This method will save the Proxy service
     */
    public void testSaveProxyService() throws Exception{
        Thread.sleep(2000);
        selenium.click("saveBtn");
        selenium.waitForPageToLoad("30000");
    }

    /*
    This method will set the transport settings
     */
    public void testTransportSettings(String http, String https, String jms, String fix, String mail)throws Exception {
        if (http.equals("http")) {
            selenium.click("http");
            selenium.click("http");
        } else if (http.equals("https")){
            selenium.click("https");
            selenium.click("https");
        } else if (http.equals("jms")){
            selenium.click("jms");
            selenium.click("jms");
        } else if (http.equals("fix")){
            selenium.click("fix");
            selenium.click("fix");
        } else if (http.equals("mail")){
            selenium.click("mail");
            selenium.click("mail");            
        }
    }

    /*
    This method will add parameters to the Proxy Service
     */
    public void testAddParams(String paramName, String paramValue,String manyParam) throws Exception{
		selenium.type("headerName", paramName);
		selenium.type("headerValue", paramValue);

        if (manyParam.equals("true")){
            selenium.click("link=Add Parameter");
            Thread.sleep(1000);
        }
    }

    /*
    This method is used to go to the next step of the wizard
     */
    public void testClickNext() throws Exception{
       selenium.click("nextBtn");
       Thread.sleep(2000);
    }

    /*
    Verify whether the proxy service is added successfully
     */
    public void testVerifyProxy(String proxyName) throws Exception{
		assertTrue(selenium.isTextPresent(proxyName));        
    }
}
