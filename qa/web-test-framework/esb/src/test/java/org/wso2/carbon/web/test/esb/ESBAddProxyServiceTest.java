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
    This method will specify the Proxy Service Name
     */
    public void addProxyName(String proxyName, String proxyType) throws Exception{
	    ESBCommon esbCommon = new ESBCommon(selenium);
        selenium.click("link=Proxy Service");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=" + proxyType);
        selenium.waitForPageToLoad("30000");

        if (proxyType == "Custom Proxy") {
		    selenium.type("psName", proxyName);
        } else {
		    selenium.type("proxy_name", proxyName);
        }
   }

    /*
    This method will select None option for specify WSDL
     */
    public void addNoneWsdl() throws Exception{
		selenium.select("publishWsdlCombo", "label=None");        
    }
    /*
    This method will add a source URL WSDL
     */
    public void addSourceUrlWsdl(String sourceUrl) throws Exception{
        selenium.select("publishWsdlCombo", "label=Specify source URL");
        selenium.type("wsdlUriText", sourceUrl);
    }

    /*
    This method will add a Inline WSDL
     */
    public void addInlineWsdl(String inlineXml) throws Exception{
        selenium.select("publishWsdlCombo", "label=Specify in-line");
        selenium.type("wsdlInlineText", inlineXml);
    }

    /*
    This method will add a registry WSDL  selected from local registry
     */
    public void addRegistryLookupWsdl(String resourceName) throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        selenium.select("publishWsdlCombo", "label=Pick from registry");
        selenium.click("link=Registry Browser");
        esbCommon.selectResource("Entry", resourceName);
    }

    /*
    This method will add a registry WSDL  selected from embedded registry
     */
    public void addRegistryLookupWsdlFromEmbededReg(String path) throws Exception{
        selenium.select("publishWsdlCombo", "label=Pick from registry");
        selenium.click("link=Registry Browser");

        if(path!=null){
           selenium.type("pickedPath", path);
           selenium.click("//input[@value='OK']");
        }
    }

    /*
    Specify the start onload option
     */
    public void specifyStartOnload(String startOnLoad) throws Exception{
       if (startOnLoad.equals("true")){
           selenium.click("startOnLoad");
           selenium.click("startOnLoad");
       }else if (startOnLoad.equals("false")){
           selenium.click("startOnLoad");
       }
    }

    /*
    This method will specify the pinned servers
     */
    public void specifyPinnerServers(String pinnedServer) throws Exception{
       if (pinnedServer!=null){
            selenium.type("pnnedServers", pinnedServer);
       }
    }

    /*
    This method will select the inSequence
     */
    public void selectInSequence(String inSeqOpt, String resourceName) throws Exception{

        if (inSeqOpt.equals("inSeqOpAnon")){
            selenium.click("inSeqOpAnon");
            Thread.sleep(2000);
            selenium.click("inAnonAddEdit");
            selenium.waitForPageToLoad("30000");
        } else if (inSeqOpt.equals("inSeqOpReg")){
            selenium.click("inSeqOpReg");
            selenium.click("//a[@onclick=\"showInLinedRegistryBrowser('proxy.in.registry');\"]");

            ESBCommon esbCommon = new ESBCommon(selenium);
            esbCommon.selectResource("Sequence",resourceName);
        } else if (inSeqOpt.equals("inSeqOpImp")){
		    selenium.click("inSeqOpImp");
    		selenium.select("inImportSeq", "label="+resourceName);
        } else if (inSeqOpt.equals("inSeqOpNone")){
		    selenium.click("inSeqOpNone");
        }
    }

    /*
    This method is used to select the in sequence from embedded registry
     */
    public void selectInSequnceFromEmbeddedRegistry(String resourcePath) throws Exception{
        selenium.click("inSeqOpReg");
        selenium.click("//a[@onclick=\"showInLinedRegistryBrowser('proxy.in.registry');\"]");
        Thread.sleep(2000);
        selenium.type("pickedPath", resourcePath);
        selenium.click("//input[@value='OK']");
    }

    /*
    This method will select the endpoint
     */
    public void selectEndpoint(String endpointSeq, String resourceName) throws Exception{
        Thread.sleep(2000);
        if (endpointSeq.equals("epOpAnon")){
            selenium.click("epOpAnon");
            Thread.sleep(2000);
            selenium.click("epAnonAddEdit");
            selenium.waitForPageToLoad("30000");
        } else if (endpointSeq.equals("epOpReg")){
            selenium.click("epOpReg");
            selenium.click("//a[@onclick=\"showInLinedRegistryBrowser('proxy.epr.registry')\"]");
            ESBCommon esbCommon = new ESBCommon(selenium);
            esbCommon.selectResource("Enpoint",resourceName);
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
    public void selectOutSequence(String outSeqOpt, String resourceName) throws Exception{

        if (outSeqOpt.equals("outSeqOpAnon")){
            selenium.click("outSeqOpAnon");
            Thread.sleep(2000);
            selenium.click("outAnonAddEdit");
            selenium.waitForPageToLoad("30000");            
        } else if (outSeqOpt.equals("outSeqOpReg")){
            selenium.click("outSeqOpReg");
            selenium.click("//a[@onclick=\"showInLinedRegistryBrowser('proxy.out.registry')\"]");
            ESBCommon esbCommon = new ESBCommon(selenium);
            esbCommon.selectResource("Sequence",resourceName);
        } else if (outSeqOpt.equals("outSeqOpImp")){
		    selenium.click("outSeqOpImp");
    		selenium.select("outImportSeq", "label="+resourceName);
        } else if (outSeqOpt.equals("outSeqOpNone")){
		    selenium.click("outSeqOpNone");
        }
    }

     /*
    This method is used to select the out sequence from embedded registry
     */
    public void selectOutSequnceFromEmbeddedRegistry(String resourcePath) throws Exception{
        selenium.click("inSeqOpReg");
        selenium.click("//a[@onclick=\"showInLinedRegistryBrowser('proxy.out.registry')\"]");

        selenium.type("pickedPath", resourcePath);
        selenium.click("//input[@value='OK']");
    }

    /*
    This method will select the faultSequence
     */
    public void selectFaultSequence(String faultSeqOpt, String resourceName, String nodeType, String mediatorCategory, String mediatorName) throws Exception{
        if (faultSeqOpt.equals("faultSeqOpAnon")){
            selenium.click("faultSeqOpAnon");
            Thread.sleep(2000);
            selenium.click("faultAnonAddEdit");
            selenium.waitForPageToLoad("30000");

            ESBCommon esbCommon = new ESBCommon(selenium);
            esbCommon.addRootLevelChildren(nodeType,mediatorCategory,mediatorName);
//            esbCommon.sequenceSave();
        } else if (faultSeqOpt.equals("faultSeqOpReg")){
            selenium.click("faultSeqOpReg");
            selenium.click("//a[@onclick=\"showInLinedRegistryBrowser('proxy.fault.registry')\"]");

            ESBCommon esbCommon = new ESBCommon(selenium);
            esbCommon.selectResource("Sequence",resourceName);
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
    public void saveProxyService() throws Exception{
        Thread.sleep(2000);
        selenium.click("saveBtn");
        selenium.waitForPageToLoad("50000");
    }

    /*
    Setting http transport
     */
    public void setTransport(String transport) throws Exception{
        if (transport !=null){
                if (transport.equals(transport)) {
                    selenium.click(transport);
                    selenium.click(transport);
                }
        }
    }

    /*
    Setting http transport
     */
    public void removeTransport(String transport) throws Exception{
        if (transport !=null){
                if (transport.equals(transport)) {
                    selenium.click(transport);
                }
        }
    }

    /*
    This method will add parameters to the Proxy Service
     */
    public void addParameters(String paramName, String paramValue) throws Exception{
		selenium.type("headerName", paramName);
		selenium.type("headerValue", paramValue);
        selenium.click("link=Add Parameter");
        Thread.sleep(1000);
    }

    /*
    This method is used to delete the parameters added in Proxy Service
     */
    public void deleteParameters(String paramName) throws Exception{
        if(selenium.isTextPresent(paramName)){
            int row_no=1;
            String param_available="";
            while(selenium.isElementPresent("//table[@id='headerTable']/tbody/tr["+row_no+"]/td[1]")){
                 param_available=selenium.getText("//table[@id='headerTable']/tbody/tr["+row_no+"]/td[1]");
                 if(param_available.equals(paramName)){
                     selenium.click("//table[@id='headerTable']/tbody/tr["+row_no+"]/td[3]/a");
                     selenium.click("//button[@type='button']");
                     break;
                 }
                 row_no=row_no+1;
           }
        }
    }

    /*
    This method is used to go to the next step of the wizard
     */
    public void clickNext() throws Exception{
       selenium.click("nextBtn");
       Thread.sleep(2000);
    }

    /*
    This method will go back one step of the wizard
     */
    public void clickBack() throws Exception{
       selenium.click("backBtn");
       Thread.sleep(2000);
    }

    /*
    Verify whether the proxy service is added successfully
     */
    public void verifyProxy(String proxyName) throws Exception{
		assertTrue(selenium.isTextPresent(proxyName));        
    }

    /*
    Method to delete the Proxy Service
     */
    public void deleteProxyService(String proxyName)throws Exception{
		selenium.click("link=List");
		selenium.waitForPageToLoad("30000");
		selenium.click("//input[@name='serviceGroups' and @value='"+proxyName+"']");
		selenium.click("delete1");
		selenium.click("//button[@type='button']");
		selenium.waitForPageToLoad("30000");
		selenium.click("//button[@type='button']");
        Thread.sleep(2000);
    }
    
    /*
   This method will verify whether the Proxy service has been deleted
    */
    public void verifyProxyDelete(String proxyName) throws Exception{
		selenium.click("link=List");
		selenium.waitForPageToLoad("30000");        
        boolean proxy = selenium.isTextPresent(proxyName);
        if (proxy){
            System.out.println("FAILURE!!! Proxy Service was not deleted");
            assertTrue(selenium.isTextPresent("proxyName"));
        }else{
            System.out.println("SUCCESS!!! Proxy Service was deleted successfully");
        }
    }

    /*
    This method is used to edit existing proxy service
     */
    public void editProxyService(String proxyName) throws Exception{
        selenium.click("link=List");
        selenium.waitForPageToLoad("30000");

        int row_no=1;
        if(selenium.isTextPresent(proxyName)){
            while(!(selenium.getText("//table[@id='sgTable']/tbody/tr["+row_no+"]/td[3]").equals(proxyName))){
                row_no=row_no+1;
            }
            selenium.click("//table[@id='sgTable']/tbody/tr["+row_no+"]/td[3]/a");
            Thread.sleep(5000);
            selenium.click("link=Edit");
		    selenium.waitForPageToLoad("40000");
        }
        else
            System.out.println("proxy service not found..");
    }

    /*
    This method is used to Deactivate or activate a proxy service
     */
    //parameter action can be activate or deactivate
    public void activateDeactivateProxyService(String proxyName,String action) throws Exception{
        selenium.click("link=List");
        selenium.waitForPageToLoad("30000");

        int row_no=1;
        if(selenium.isTextPresent(proxyName)){
            while(!(selenium.getText("//table[@id='sgTable']/tbody/tr["+row_no+"]/td[3]").equals(proxyName))){
                row_no=row_no+1;
            }
            selenium.click("//table[@id='sgTable']/tbody/tr["+row_no+"]/td[3]/a");
            Thread.sleep(5000);
            if(selenium.isElementPresent("link=Deactivate") && action.equalsIgnoreCase("deactivate")){
                selenium.click("link=Deactivate");
                System.out.println(proxyName+" is deactivated..");
            }
            else if(selenium.isElementPresent("link=Activate") && action.equalsIgnoreCase("activate")){
                selenium.click("link=Activate");
                System.out.println(proxyName+" is activated..");
            }
        }
        else
            System.out.println("proxy not found..");
    }
}
