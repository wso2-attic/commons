package org.wso2.carbon.web.test.esb;

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


import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;

import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.wsdl.Definition;
import javax.wsdl.Types;
import java.io.*;
import java.util.Map;


public class ESBAddWsdlToRegistryTest extends CommonSetup{
    
    public ESBAddWsdlToRegistryTest(String text) {
        super(text);
    }

    public String getTargetNameSpace(String wsdlFilePath) throws Exception{
        String tnameSpace="";
        try{
            String search="targetNamespace";
            int sIndex=0;
            int count_search=0;
            File f1 = new File(wsdlFilePath);
            // Open the file
            FileInputStream fstream = new FileInputStream(f1);

            // Get the object of DataInputStream
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine=null,tmp;

            String concat_lines="";
            //Read File Line By Line
            while ((tmp = br.readLine()) != null){
               strLine = tmp.trim();
               concat_lines=concat_lines+strLine;
            }
            int index =0;
            //loop used to take the first target namespace
            for(int searchIndex=0;searchIndex<concat_lines.length();){
               index=concat_lines.indexOf(search,searchIndex);
               if( index != -1){
                   count_search=count_search+1;
                   searchIndex=index+search.length();
                   sIndex=searchIndex;
                   break;
               }
               else
                   break;
            }
           tnameSpace=concat_lines.substring(sIndex+2);
           tnameSpace=tnameSpace.substring(0,tnameSpace.indexOf('"'));
           System.out.println(tnameSpace);
           //Close the input stream
           in.close();
        }
        catch(IOException e){
                System.out.println(e.getMessage());
        }
        return tnameSpace;
    }

    
//    public void testFolderStructure() throws Exception{
//        ESBCommon esbCommon = new ESBCommon(selenium);
//        String targetNs=getTargetNameSpace("/home/dinusha/Desktop/customer_issues/3/wsdl_files/EnterprisePersonService.wsdl");
//
//
//    }

//    public void testSchemaLocations() throws Exception{
//         ESBCommon esbCommon = new ESBCommon(selenium);
//         esbCommon.logoutLogin();
//         String[] s1;
//         s1=getwsdlSchemaLocations("/governance/wsdls/http/ihc/org/services/EnterprisePersonService?wsdl/EnterprisePersonService.wsdl");
//         for(int i=0;i<s1.length;i++){
//             System.out.println(s1[i]);
//         }
//    }

//    public void testSchemaKeys() throws Exception{
//         ESBCommon esbCommon = new ESBCommon(selenium);
//         esbCommon.logoutLogin();
//
//          String[] s1=getSchemaKeys("/governance/wsdls/http/ihc/org/services/EnterprisePersonService?wsdl/EnterprisePersonService.wsdl");
//          for(int i=0;i<s1.length;i++){
//             System.out.println(s1[i]);
//          }
//    }

    public void testCreateProxy() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        createProxy("test_wsdl_proxyService","/governance/wsdls/http/ihc/org/services/EnterprisePersonService?wsdl/EnterprisePersonService.wsdl");
    }

    /*
    This method is used to get the wsdl schema locations from wsdl file
     */
    public String[] getwsdlSchemaLocations(String wsdlPath) throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        String wsdlName=wsdlPath.substring(wsdlPath.lastIndexOf("/")+1);
        String wsdlLocation=wsdlPath.substring(1,wsdlPath.lastIndexOf("/"));
        System.out.println(wsdlName);
        System.out.println(wsdlLocation);
        String wsdlContent="";
        String slocation="";
        String[] schemaLocation=new String[10];
        int[] sIndex=new int[10];
        int count_search=0;
        int index=0;
        int k=0;

        //esbCommon.searchResources("/governance/wsdls/http/ihc/org/services/EnterprisePersonService?wsdl","EnterprisePersonService.wsdl");
        boolean findwsdl=esbCommon.searchResources(wsdlLocation,wsdlName);
        System.out.println(findwsdl);
        if(findwsdl){
            int i=1;
            String resourceName="";
            Thread.sleep(2000);
            while(selenium.isElementPresent("resourceView"+i)){
                resourceName=selenium.getText("resourceView"+i);
                if(resourceName.equals(wsdlName)){
                    break;
                }
                i=i+1;
            }
            
            selenium.click("resourceView"+i);
            //selenium.waitForPageToLoad("30000");
            Thread.sleep(20000);
            selenium.click("link=Edit as text");
            wsdlContent=selenium.getText("editTextContentID");
            String search="schemaLocation=";
            //loop use to find the begining indexes of schema locations
            for(int searchIndex=0;searchIndex< wsdlContent.length();){
               index= wsdlContent.indexOf(search,searchIndex);
               if( index != -1){
                   count_search=count_search+1;
                   k=count_search;
                   searchIndex=index+search.length();
                   sIndex[k-1]=searchIndex;

                   //System.out.println("index "+sIndex[count_search]);                   
               }
               else
                   break;
            }

            String tSchemaLocation=null;
            //loop use to get the paths of schema locations
	        int y=0;
            int p=1;
            for(int j=0;j<count_search;j++){
                tSchemaLocation=wsdlContent.substring(sIndex[j]+1);
                slocation=tSchemaLocation.substring(0,tSchemaLocation.indexOf('"'));
                if(slocation.equals(schemaLocation[p-1])){

                }
                else{
                    schemaLocation[y]=slocation;
                    y=y+1;
                }
		    p=y;
            }
        }
        else
            System.out.println("wsdl not found in the specified location!");

        return schemaLocation;
    }


    public String[] getSchemaKeys(String wsdlPath) throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        String[] schemaNames=new String[10];
        String[] schemaKey=new String[10];
        String[] s=getwsdlSchemaLocations(wsdlPath);
        int i=0;
        while(s[i]!=null){
            schemaNames[i]=s[i].substring(s[i].lastIndexOf("/")+1);
            if(esbCommon.searchResources(null,schemaNames[i])){
                schemaKey[i]=selenium.getText("//tr[@id='1']/td[1]");
            }
            
            i++;
        }
        return schemaKey;
    }


    /*
    This method is used to add wsdl resource
    */
    public void addWsdlresourceLocations(String rLocation,String key) throws Exception{
        selenium.type("locationText", rLocation);
        selenium.click("//a[@onclick=\"showInLinedRegistryBrowser('wsdl.resource.key');\"]");
        Thread.sleep(5000);
        selenium.type("pickedPath", key);
		selenium.click("//input[@value='OK']");    
    }

    public void createProxy(String proxyName,String wsdlPath) throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        ESBAddProxyServiceTest esbAddProxyServiceTest = new ESBAddProxyServiceTest(selenium);
        esbCommon.logoutLogin();
        String[] s1,s2;
        int x=0;
        //s1=getwsdlSchemaLocations("/governance/wsdls/http/ihc/org/services/EnterprisePersonService?wsdl/EnterprisePersonService.wsdl");
        s1=getwsdlSchemaLocations(wsdlPath);
        s2=getSchemaKeys(wsdlPath);

        esbCommon.logoutLogin();
        esbAddProxyServiceTest.addProxyName(proxyName, "Custom Proxy");
        //esbAddProxyServiceTest.transportSettings("http", "https", null, null, null);
        esbAddProxyServiceTest.setTransport("http");
        esbAddProxyServiceTest.setTransport("https");
        esbAddProxyServiceTest.addRegistryLookupWsdlFromEmbededReg(wsdlPath);
        //esbAddProxyServiceTest.addRegistryLookupWsdlFromEmbededReg("/governance/wsdls/http/ihc/org/services/EnterprisePersonService?wsdl/EnterprisePersonService.wsdl");
        while(s1[x]!=null && s2[x]!=null){
            addWsdlresourceLocations(s1[x],s2[x]);
            x++;
            selenium.click("link=Add Resource");
        }
        esbAddProxyServiceTest.clickNext();

        //****setting up of the In Sequence of the Proxy Service*****//
        ESBSendMediatorTest esbSendMediatorTest = new ESBSendMediatorTest(selenium);
        esbAddProxyServiceTest.selectInSequence("inSeqOpAnon", null);
        //Adding the In mediator
        esbCommon.addRootLevelChildren("Add Child","Filter","In");

        //Adding a send mediator
        esbCommon.addMediators("Add Child","0","Core","Send");
        esbSendMediatorTest.addAnonSendMediator("0.0");
        esbSendMediatorTest.addMandInfoSendMediator(esbCommon.getServiceAddUrl("SimpleStockQuoteService"));
        esbSendMediatorTest.saveEndpoint();
        esbCommon.mediatorUpdate();

        //Adding the Drop mediator
        esbCommon.addMediators("Add Sibling","0.0","Core","Drop");
        esbCommon.sequenceSave();
        esbAddProxyServiceTest.clickNext();

        //*****setting up of the Out Sequence of the Proxy Service*****//
        esbAddProxyServiceTest.selectOutSequence("outSeqOpAnon", null);
        //Adding the send mediator
        esbCommon.addRootLevelChildren("Add Child","Core","Send");
        esbCommon.mediatorUpdate();
        esbCommon.sequenceSave();

        //****saving the proxy Service*****//
        esbAddProxyServiceTest.saveProxyService();
        Thread.sleep(5000);
        if(selenium.isElementPresent("messagebox-error")){
            if(selenium.getText("messagebox-error").equals("Unable to add proxy service :: Unable to add Proxy service : "+proxyName+" :: Error building service from WSDL"))
                throw new MyCheckedException("Unable to add proxy service :: Unable to add Proxy service : "+proxyName+" :: Error building service from WSDL..!!");
            else
                throw new MyCheckedException("Error in creating proxy..!!");
        }
    }
}
