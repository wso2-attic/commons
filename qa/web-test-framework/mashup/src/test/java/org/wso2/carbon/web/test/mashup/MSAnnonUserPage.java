package org.wso2.carbon.web.test.mashup;

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


public class MSAnnonUserPage extends CommonSetup{

    public MSAnnonUserPage(String text) {
           super(text);
    }

    //Test link is available for the service name.
    public static void annonUser_Services(String serviceName) throws Exception{
        selenium.open(MSCommon.loadProperties().getProperty("context.root")+"/carbon/admin/login.jsp");
        Thread.sleep(10000);
        selenium.click("link=List");
		selenium.waitForPageToLoad("30000");
		assertTrue(selenium.isTextPresent("Deployed Services"));

        int k=1;
        boolean next=selenium.isTextPresent("next >");
        while(next==true){
               selenium.click("link=next >");
               Thread.sleep(6000);
               next = selenium.isTextPresent("next >");
               k=k+1;
        }

        int i;
        selenium.click("link=List");
        selenium.waitForPageToLoad("30000");

        for(i=1;i<=k;i++){
           if(selenium.isTextPresent(serviceName)==true)
                assertTrue(selenium.isElementPresent("link="+serviceName)==false);

           else{
              int temp=i+1;
              if(temp<=k)
                selenium.click("link="+temp); 
           }
       }
    }


    //Test WSDL1.1 and WSDL2.0 links of the annonymous user.
    public static void AnnonUser_WSDL_Links(String jsservice,String IP,String port,String wsdl,String wsdl2) throws Exception{

        String expected1 ="http://"+IP+":"+port+MSCommon.loadProperties().getProperty("context.root")+"/services/"+jsservice+"?"+wsdl;
        String expected2 ="http://"+IP+":"+port+MSCommon.loadProperties().getProperty("context.root")+"/services/"+jsservice+"?"+wsdl2;

        String expected3 ="http://"+MSCommon.loadProperties().getProperty("host.ip")+":"+port+MSCommon.loadProperties().getProperty("context.root")+"/services/"+jsservice+"?"+wsdl;
        String expected4 ="http://"+MSCommon.loadProperties().getProperty("host.ip")+":"+port+MSCommon.loadProperties().getProperty("context.root")+"/services/"+jsservice+"?"+wsdl2;

        selenium.open(MSCommon.loadProperties().getProperty("context.root")+"/carbon/admin/login.jsp");
        selenium.click("link=List");
		selenium.waitForPageToLoad("30000");

        int k=1;
        boolean next=selenium.isTextPresent("next >");
        while(next==true){
               selenium.click("link=next >");
               Thread.sleep(7000);
               next = selenium.isTextPresent("next >");
               k=k+1;
        }

        int i;
        selenium.click("link=List");
        selenium.waitForPageToLoad("30000");

        for(i=1;i<=k;i++){
           boolean mashup = selenium.isTextPresent(jsservice);
           if(mashup == true){
                 int j=1;
                 while(!selenium.getTable("sgTable."+j+".1").equals(jsservice)){
                     j=j+1;
                 }

                 if(j==1){
                     selenium.click("link=WSDL1.1");
                     String tryitwinid = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
                     selenium.selectWindow(tryitwinid);
                     String actual1 = selenium.getLocation();

                     if ((expected1.equals(actual1))||(expected3.equals(actual1))) {
                        System.out.println("Test passed");
                     } else {
                        System.out.println("Test failed");
                     }
                     selenium.close();
                     selenium.selectWindow("");


                     selenium.click("link=WSDL2.0");
                     String tryitwinid1 = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
                     selenium.selectWindow(tryitwinid1);
                     String actual2 = selenium.getLocation();

                     if ((expected2.equals(actual2))||(expected4.equals(actual2))) {
                        System.out.println("Test passed");
                     } else {
                        System.out.println("Test failed");
                     }
                     selenium.close();
                     selenium.selectWindow("");

                 }
                 else{
                     selenium.click("//table[@id='sgTable']/tbody/tr["+j+"]/td[4]/a");
                     String tryitwinid = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
                     selenium.selectWindow(tryitwinid);
                     String actual1 = selenium.getLocation();

                     if ((expected1.equals(actual1))||(expected3.equals(actual1))) {
                        System.out.println("Test passed");
                     } else {
                        System.out.println("Test failed");
                     }
                     selenium.close();
                     selenium.selectWindow("");


                     selenium.click("//table[@id='sgTable']/tbody/tr["+j+"]/td[5]/a");
                     String tryitwinid1 = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
                     selenium.selectWindow(tryitwinid1);
                     String actual2 = selenium.getLocation();

                     if ((expected2.equals(actual2))||(expected4.equals(actual2))) {
                        System.out.println("Test passed");
                     } else {
                        System.out.println("Test failed");
                     }
                     selenium.close();
                     selenium.selectWindow("");
                 }
                 i=k+1;
           }
           else{
              int temp=i+1;
              if(temp<=k)
                selenium.click("link="+temp);
                Thread.sleep(7000);
           }
       }
    }

}
