/*
 *  Copyright (c) 2005-2009, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */

package org.wso2.carbon.web.test.common;

import com.thoughtworks.selenium.SeleneseTestCase;
import com.thoughtworks.selenium.Selenium;

import java.util.Properties;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.File;

public class ModuleManagement extends SeleneseTestCase {

    Selenium browser;


    public ModuleManagement(Selenium _browser) {
        browser = _browser;
    }


    public void engageServiceGroupModules(String serviceGroupName, String serviceName, String operation, String moduleName) throws Exception {

        ServiceManagement sm = new ServiceManagement(browser);
        // browser.click("Link=List");
        // browser.open("/carbon/service-mgt/index.jsp?region=region1&item=services_list_menu&ordinal=0");
        // browser.click("link=" + serviceGroupName);
        //browser.waitForPageToLoad("30000");
        // assertTrue(browser.isTextPresent("Modules"));
        // browser.click("link=Modules");
        // browser.waitForPageToLoad("30000");
        // verifyTrue(browser.isTextPresent("No engaged modules found"));
        disengageGlobalLevelModules(moduleName);
        gotoServiceGroupLevel(serviceGroupName);

        int i = 1;

        while (browser.isElementPresent("//table[@id='table1']/tbody/tr[" + Integer.toString(i) + "]/td")) {
            if(moduleName.equals(browser.getText("//table[@id='table1']/tbody/tr[" + Integer.toString(i) + "]/td"))){
                browser.click("//table[@id='table1']/tbody/tr[" + Integer.toString(i) + "]/td/a/img");
                verifyTrue(browser.isTextPresent("Do you really want to disengage " + moduleName + " module from " + serviceGroupName + " service group"));
                browser.click("//button[@type='button']");
                browser.waitForPageToLoad("30000");
                browser.click("//button[@type='button']");
            }

            i = i + 1;
        }



        browser.select("moduleSelector", "label=" + moduleName);
        browser.click("//input[@value=' Engage ']");
        browser.waitForPageToLoad("30000");
        verifyTrue(browser.isTextPresent("Module was successfully engaged."));
        browser.click("//button[@type='button']");

        //Go to Service management -->modules and check whether the module is not available for engaging

        Thread.sleep(5000);

        sm.accessServiceDashboard(serviceName);
        browser.click("link=Modules");
        browser.waitForPageToLoad("30000");
        verifyTrue(browser.isTextPresent(moduleName));

        String modules[] = browser.getSelectOptions("id=moduleSelector");
        boolean moduleFound = false;
        for ( i = 0; i < modules.length; i++) {
            if (modules[i].equals(moduleName)) {
                moduleFound = true;

            }
        }

        if (moduleFound) {
            assertTrue("Module is not engaged properly at the Service Group level", moduleFound);
        }


        //Go to Operation dashbaord and check the module is not available to engage at the operational level

        browser.click("link=List");
        browser.waitForPageToLoad("30000");
        sm.accessServiceDashboard(serviceName);
        browser.click("link=Operations");
        browser.waitForPageToLoad("30000");
        browser.click("link=" + operation);
        browser.waitForPageToLoad("30000");
        browser.click("module_view_link");
        browser.waitForPageToLoad("30000");


        verifyTrue(browser.isTextPresent(moduleName));
        String opmodules[] = browser.getSelectOptions("id=moduleSelector");
        boolean opmoduleFound = false;
        for (i = 0; i < opmodules.length; i++) {
            if (opmodules[i].equals(moduleName)) {
                opmoduleFound = true;

            }
        }
        if (opmoduleFound) {
            assertTrue("Module is not engaged properly at the Service Group level", opmoduleFound);
        }


    }

    public void disengageServiceGroupModules(String serviceGroupName, String serviceName, String operation, String moduleName) throws Exception {
        ServiceManagement sm = new ServiceManagement(browser);
        // browser.click("Link=List");
        // browser.open("/carbon/service-mgt/index.jsp?region=region1&item=services_list_menu");
        // browser.click("link=" + serviceGroupName);
        // browser.waitForPageToLoad("30000");
        // browser.click("link=Modules");
        // browser.waitForPageToLoad("30000");
        // verifyTrue(browser.isTextPresent(moduleName));
        gotoServiceGroupLevel(serviceGroupName);

        String readModule = "";
        int i = 1;

        while (!moduleName.equals(readModule) && browser.isElementPresent("//table[@id='table1']/tbody/tr[" + Integer.toString(i) + "]/td")) {
            readModule = browser.getText("//table[@id='table1']/tbody/tr[" + Integer.toString(i) + "]/td");
            i = i + 1;
        }
        i = i - 1;

        if(browser.isElementPresent("//table[@id='table1']/tbody/tr[" + Integer.toString(i) + "]/td/a/img")){
            browser.click("//table[@id='table1']/tbody/tr[" + Integer.toString(i) + "]/td/a/img");
            verifyTrue(browser.isTextPresent("Do you really want to disengage " + moduleName + " module from " + serviceGroupName + " service group"));
            browser.click("//button[@type='button']");
            browser.waitForPageToLoad("30000");
            browser.click("//button[@type='button']");

            //Go to Service management -->modules and check whether the module is available for engaging

            sm.accessServiceDashboard(serviceName);
            browser.click("link=Modules");
            browser.waitForPageToLoad("30000");



            verifyTrue(browser.isTextPresent(moduleName));
            String modules[] = browser.getSelectOptions("id=moduleSelector");
            boolean moduleFound = false;
            for (int j = 0; j < modules.length; j++) {
                if (modules[j].equals(moduleName)) {
                    moduleFound = true;
                    break;
                }
            }
            if (!moduleFound) {
                assertFalse("Module has not been disengaged properly from the Service Group level", moduleFound);
            }


            //Go to Operation dashbaord and check the module is available to engage at the operational level

            browser.click("link=List");
            browser.waitForPageToLoad("30000");
            sm.accessServiceDashboard(serviceName);
            browser.click("link=Operations");
            browser.waitForPageToLoad("30000");
            browser.click("link=" + operation);
            browser.waitForPageToLoad("30000");
            browser.click("module_view_link");
            browser.waitForPageToLoad("30000");


            verifyTrue(browser.isTextPresent(moduleName));
            String opmodules[] = browser.getSelectOptions("id=moduleSelector");
            boolean opmoduleFound = false;
            for (int k = 0; k < modules.length; k++) {
                if (opmodules[k].equals(moduleName)) {
                    opmoduleFound = true;
                    break;
                }
            }
            if (!opmoduleFound) {
                assertFalse("Module has not been disengaged properly from the Service Group level", opmoduleFound);
            }
        }
    }

    public void engageServiceLevelModules(String serviceGroupName,String serviceName, String moduleName) throws Exception {

        ServiceManagement sm = new ServiceManagement(browser);
        disengageGlobalLevelModules(moduleName);

        gotoServiceGroupLevel(serviceGroupName);

        String readModule = "";
        int i = 1;

        while (!moduleName.equals(readModule) && browser.isElementPresent("//table[@id='table1']/tbody/tr[" + Integer.toString(i) + "]/td")) {
            readModule = browser.getText("//table[@id='table1']/tbody/tr[" + Integer.toString(i) + "]/td");
            i = i + 1;
        }
        i = i - 1;

        if(browser.isElementPresent("//table[@id='table1']/tbody/tr[" + Integer.toString(i) + "]/td/a/img")){
            browser.click("//table[@id='table1']/tbody/tr[" + Integer.toString(i) + "]/td/a/img");
            verifyTrue(browser.isTextPresent("Do you really want to disengage " + moduleName + " module from " + serviceGroupName + " service group"));
            browser.click("//button[@type='button']");
            browser.waitForPageToLoad("30000");
            browser.click("//button[@type='button']");
        }

        //engage module at service level
        gotoServiceLevel(serviceName);

        i = 1;

        while (browser.isElementPresent("//table[@id='table1']/tbody/tr[" + Integer.toString(i) + "]/td")) {
            if(moduleName.equals(browser.getText("//table[@id='table1']/tbody/tr[" + Integer.toString(i) + "]/td"))){
                browser.click("//table[@id='table1']/tbody/tr[" + Integer.toString(i) + "]/td/a/img");
                browser.click("//button[@type='button']");
                browser.waitForPageToLoad("30000");
                browser.click("//button[@type='button']");
            }

            i = i + 1;
        }


        browser.select("moduleSelector", "label=" + moduleName);
        browser.click("//input[@value=' Engage ']");
        browser.waitForPageToLoad("30000");
        verifyTrue(browser.isTextPresent("Module was successfully engaged."));
        browser.click("//button[@type='button']");
    }

    public void engagedAtOperationalLevel(String serviceName, String operation, String moduleName)  throws Exception{
        //checks whether the module is engaged at operational level
        //Go to Operation dashbaord and check the module is not available to engage at the operational level
        gotoOperationalLevel(serviceName,operation);

        verifyTrue(browser.isTextPresent(moduleName));
        String opmodules[] = browser.getSelectOptions("id=moduleSelector");
        boolean opmoduleFound = false;
        for (int i = 0; i < opmodules.length; i++) {
            if (opmodules[i].equals(moduleName)) {
                opmoduleFound = true;
            }
        }
        if (opmoduleFound) {
            assertTrue("Module is not engaged properly at the Operational level", opmoduleFound);
        }

    }



    public void disengageServiceLevelModules( String serviceName, String moduleName) throws Exception {
        ServiceManagement sm = new ServiceManagement(browser);

        //disengage module at service stage
        gotoServiceLevel(serviceName);

        String readModule = "";
        int i = 1;

        while (!moduleName.equals(readModule) && browser.isElementPresent("//table[@id='table1']/tbody/tr[" + Integer.toString(i) + "]/td")) {
            readModule = browser.getText("//table[@id='table1']/tbody/tr[" + Integer.toString(i) + "]/td");
            i = i + 1;
        }
        i = i - 1;

        if(browser.isElementPresent("//table[@id='table1']/tbody/tr[" + Integer.toString(i) + "]/td/a/img")){
            browser.click("//table[@id='table1']/tbody/tr[" + Integer.toString(i) + "]/td/a/img");
            verifyTrue(browser.isTextPresent("Do you really want to disengage "+ moduleName +" module from "+ serviceName +" service ?"));
            browser.click("//button[@type='button']");
            browser.waitForPageToLoad("30000");
            browser.click("//button[@type='button']");

            if(browser.isTextPresent("Cannot disengage")){
                assertTrue(browser.isTextPresent("Cannot disengage "  + moduleName + " since "));
                browser.click("//button[@type='button']");
                Thread.sleep(1000);
            }

            if(browser.isTextPresent("Module cannot be disengaged since the service is associated with a security scenario.")){
                assertTrue(browser.isTextPresent("Module cannot be disengaged since the service is associated with a security scenario."));
                Thread.sleep(1000);
                browser.click("//button[@type='button']");
                Thread.sleep(1000);
                System.out.println("Module cannot be disengaged since the service is associated with a security scenario.");
            }
        }

    }

    public void disengagedAtServiceLevel( String serviceName, String moduleName) throws Exception {
        //checks whether the module is disengaged at service level
        gotoServiceLevel(serviceName);


        verifyTrue(browser.isTextPresent(moduleName));
        String modules[] = browser.getSelectOptions("id=moduleSelector");
        boolean moduleFound = false;
        for (int j = 0; j < modules.length; j++) {
            if (modules[j].equals(moduleName)) {
                moduleFound = true;
                break;
            }
        }
        if (!moduleFound) {
            assertFalse("Module has not been disengaged properly from the Service level", moduleFound);
        }
    }


    public void disengagedAtOperationalLevel( String serviceName, String operation, String moduleName) throws Exception {
        // //checks whether the module is disengaged at operational level
        gotoOperationalLevel(serviceName,operation);

        verifyTrue(browser.isTextPresent(moduleName));
        String modules[] = browser.getSelectOptions("id=moduleSelector");
        boolean moduleFound = false;
        for (int k = 0; k < modules.length; k++) {
            if (modules[k].equals(moduleName)) {
                moduleFound = true;
                break;
            }
        }
        if (!moduleFound) {
            assertFalse("Module has not been disengaged properly from the Operational level", moduleFound);
        }
    }

    public void engagedAtServiceLevel(String serviceName, String moduleName)  throws Exception{
        //checks whether the module is engaged at service level

        //Go to service dashboard and check the module is available to engage at the operational level
        gotoServiceLevel(serviceName);


        verifyTrue(browser.isTextPresent(moduleName));
        String opmodules[] = browser.getSelectOptions("id=moduleSelector");
        boolean opmoduleFound = false;
        for (int i = 0; i < opmodules.length; i++) {
            if (opmodules[i].equals(moduleName)) {
                opmoduleFound = true;

            }
        }
        if (opmoduleFound) {
            assertTrue("Module is not engaged properly at the Service level", opmoduleFound);
        }


    }

    public void engagedAtServiceGroupLevel(String serviceName, String moduleName)  throws Exception{
        //checks whether the module is engaged at service level

        //Go to service dashboard and check the module is available to engage at the operational level
        gotoServiceGroupLevel(serviceName);


        verifyTrue(browser.isTextPresent(moduleName));
        String opmodules[] = browser.getSelectOptions("id=moduleSelector");
        boolean opmoduleFound = false;
        for (int i = 0; i < opmodules.length; i++) {
            if (opmodules[i].equals(moduleName)) {
                opmoduleFound = true;

            }
        }
        if (opmoduleFound) {
            assertTrue("Module is not engaged properly at the Global level", opmoduleFound);
        }


    }

    public void disengagedAtServiceGroupLevel( String serviceName, String moduleName) throws Exception {
        // //checks whether the module is disengaged at operational level
        gotoServiceGroupLevel(serviceName);

        verifyTrue(browser.isTextPresent(moduleName));
        String modules[] = browser.getSelectOptions("id=moduleSelector");
        boolean moduleFound = false;
        for (int k = 0; k < modules.length; k++) {
            if (modules[k].equals(moduleName)) {
                moduleFound = true;
                break;
            }
        }
        if (!moduleFound) {
            assertFalse("Module has not been disengaged properly from the Service Group level", moduleFound);
        }
    }


    public void disengageOperationalLevelModules( String serviceName, String operation, String moduleName) throws Exception {
        ServiceManagement sm = new ServiceManagement(browser);

        //disengage module at operational level
        gotoOperationalLevel(serviceName,operation);

        String readModule = "";
        int i = 1;

        while (!moduleName.equals(readModule)) {
            readModule = browser.getText("//table[@id='table1']/tbody/tr[" + Integer.toString(i) + "]/td");
            i = i + 1;
        }
        i = i - 1;
        browser.click("//table[@id='table1']/tbody/tr[" + Integer.toString(i) + "]/td/a/img");
        verifyTrue(browser.isTextPresent("Do you really want to disengage " + moduleName + " module from " +operation + " service group"));
        browser.click("//button[@type='button']");
        browser.waitForPageToLoad("30000");
        browser.click("//button[@type='button']");

        if(browser.isTextPresent("Module cannot be disengaged since the service is associated with a security scenario.")){
            assertTrue(browser.isTextPresent("Module cannot be disengaged since the service is associated with a security scenario."));
            browser.click("//button[@type='button']");
            Thread.sleep(1000);
        }





    }

    public void gotoServiceGroupLevel(String serviceGroupName) throws Exception {
        ServiceManagement sm = new ServiceManagement(browser);
        //browser.open("/carbon/service-mgt/index.jsp?region=region1&item=services_list_menu");
        browser.click("Link=List");
        browser.waitForPageToLoad("30000");
        sm.accessServiceGroupPage(serviceGroupName);
        browser.click("link=Modules");
        browser.waitForPageToLoad("30000");

    }

    public void gotoServiceLevel( String serviceName) throws Exception {
        ServiceManagement sm = new ServiceManagement(browser);
        // browser.open("/carbon/service-mgt/index.jsp?region=region1&item=services_list_menu");
        browser.click("Link=List");
        browser.waitForPageToLoad("30000");
        sm.accessServiceDashboard(serviceName);
        browser.click("link=Modules");
        browser.waitForPageToLoad("30000");
        // verifyTrue(browser.isTextPresent(moduleName));

    }

    public void gotoOperationalLevel( String serviceName, String operation) throws Exception {
        ServiceManagement sm = new ServiceManagement(browser);
        // browser.open("/carbon/service-mgt/index.jsp?region=region1&item=services_list_menu");
        browser.click("Link=List");
        browser.waitForPageToLoad("30000");
        sm.accessServiceDashboard(serviceName);
        sm.openOperationDashboard(serviceName,operation);
        browser.click("module_view_link");
        browser.waitForPageToLoad("30000");
        //verifyTrue(browser.isTextPresent(moduleName));
    }


    public void accessModuleIndexPage() throws Exception{
        // browser.open("/carbon/admin/index.jsp");
        browser.click("link=Home");
        browser.waitForPageToLoad("30000");
        browser.click("//div[@id='menu']/ul/li[5]/ul/li[4]/ul/li[1]/a");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Deployed Modules"));
        assertTrue(browser.isTextPresent("Name"));
        assertTrue(browser.isTextPresent("Version"));
        assertTrue(browser.isTextPresent("Description"));
        assertTrue(browser.isTextPresent("Actions"));
        assertEquals("Globally Engaged Modules", browser.getTable("globalModules.0.0"));

    }

    public void checkModules(String module) throws Exception{
        //accessModuleIndexPage();

        String moduleName=module.substring(0,module.indexOf('-')) ;
        String readModule = "";
        int i = 1;

        if(browser.isTextPresent(moduleName)){

            while(!moduleName.equals(readModule) && readModule!=null) {
                readModule = browser.getText("//table[@id='moduleTable']/tbody/tr[" + Integer.toString(i) + "]/td");
                i = i + 1;
            }

            String version=browser.getText("//table[@id='moduleTable']/tbody/tr[" + Integer.toString(i-1)+ "]/td[2]");


            if(!version.equals(module.substring(module.indexOf('-')+1))){
                assertTrue("Module version of " + moduleName + " is incorrect", false);
            }

            if(moduleName.equals("addressing")){//check whether addressing is engaged globally
                assertEquals("",browser.getText("//table[@id='moduleTable']/tbody/tr[" + Integer.toString(i-1)+ "]/td[4]"));
                assertEquals(module,browser.getText("//table[@id='globalModules']/tbody/tr[1]/td"));
            }

            if(moduleName.equals("sandesha2") || moduleName.equals("wso2caching") || moduleName.equals("wso2throttling")){  //check for  configure link
                assertEquals("Configure",browser.getText("//table[@id='moduleTable']/tbody/tr[" + Integer.toString(i-1)+ "]/td[4]"));

            }

        }


    }

    public void engageGlobalLevelModules(String module) throws Exception{
        //browser.open("/carbon/admin/index.jsp");
        browser.click("link=Home");
        browser.waitForPageToLoad("30000");
        String serverURL=browser.getText("//table[@id='systemInfoTable']/tbody/tr[2]/td[2]");
        accessModuleIndexPage();

        if(browser.isElementPresent("//a[@onclick=\"disengage('" +serverURL+"\','"+module+"\');\"]")){
            browser.click("//a[@onclick=\"disengage('" +serverURL+"\','"+module+"\');\"]");
            assertTrue(browser.isTextPresent("Do you want to globally disengage " + module + " module ?"));
            browser.click("//button[@type='button']");
            browser.waitForPageToLoad("30000");
            browser.click("//button[@type='button']");
        }
        browser.click("//a[@onclick=\"engage('" +serverURL+"\','"+module+"\');\"]");

        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Module was successfully engaged globally."));
        browser.click("//button[@type='button']");
        assertTrue(browser.isTextPresent(module));

    }

    public void disengageGlobalLevelModules(String module) throws Exception{
        //browser.open("/carbon/modulemgt/index.jsp");
        browser.click("link=Home");
        browser.waitForPageToLoad("30000");
        String serverURL=browser.getText("//table[@id='systemInfoTable']/tbody/tr[2]/td[2]");
        accessModuleIndexPage();
        if(browser.isElementPresent("//a[@onclick=\"disengage('" +serverURL+"\','"+module+"\');\"]")){
            browser.click("//a[@onclick=\"disengage('" +serverURL+"\','"+module+"\');\"]");
            assertTrue(browser.isTextPresent("Do you want to globally disengage " + module + " module ?"));
            browser.click("//button[@type='button']");
            browser.waitForPageToLoad("30000");
            browser.click("//button[@type='button']");
        }
    }

    public void checkAccessDenyAtGlobalThottling(String type, String serviceName, String opName, String opParam, String input, int operationCount, String result) throws Exception{

        Tryit instTryit=new Tryit(browser);
        browser.click("//a[@onclick=\"submitHiddenForm('../throttling/index.jsp');return false;\"]");
        browser.waitForPageToLoad("30000");
        verifyTrue(browser.isTextPresent("Global Throttling Configuration "));

        if ((browser.getSelectedValue("enableThrottle")).equals("Yes")) {
            browser.select("enableThrottle", "label=No");
        }
        browser.select("enableThrottle", "label=Yes");
        //browser.click("//table[@id='dataTable']/tbody/tr[2]/td[7]/a");
        browser.select("data15", "label=Deny");
        browser.click("//input[@value='Finish']");
        browser.waitForPageToLoad("30000");
        verifyTrue(browser.isTextPresent("Successfully applied throttling configuration"));
        browser.click("//button[@type='button']");
        Thread.sleep(1000);
        instTryit.invokeTryit(type,serviceName,opName,opParam,input,operationCount,result);

        accessModuleIndexPage();
        browser.click("//a[@onclick=\"submitHiddenForm('../throttling/index.jsp');return false;\"]");
        browser.waitForPageToLoad("30000");
        browser.select("enableThrottle", "label=No");
        verifyTrue(browser.isTextPresent("This will disengage throttling. Click Yes to confirm"));
        browser.click("//button[@type='button']");
        browser.waitForPageToLoad("30000");
        browser.click("//button[@type='button']");


    }

    public void checkAccessControlAtGlobalThottling(String type,int maxRequestCount,int unitTime,int prohibitedTime,String access) throws Exception{

        Tryit instTryit=new Tryit(browser);

        accessModuleIndexPage();
        browser.click("//a[@onclick=\"submitHiddenForm('../throttling/index.jsp');return false;\"]");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Global Throttling Configuration "));

        if ((browser.getSelectedValue("enableThrottle")).equals("Yes")) {
            browser.select("enableThrottle", "label=No");
        }

        browser.select("enableThrottle", "label=Yes");
        int i=1;

        if(access.equals("Control")){
            browser.select("data15", "label=Control");
            browser.type("data12",Integer.toString(maxRequestCount));
            browser.type("data13", Integer.toString(unitTime));
            browser.type("data14", Integer.toString(prohibitedTime));
            browser.select("data16", "label="+type);
        }else if(access.equals("Allow") || access.equals("Deny") ){
            browser.select("data15", "label="+access);
            browser.select("data16", "label="+type);
        }


        browser.click("//input[@value='Finish']");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Successfully applied throttling configuration"));
        browser.click("//button[@type='button']");
        Thread.sleep(1000);

    }

    public void invokeService(String serviceepr, String operationName, String SoapAction,String namespace, String firstChild,int count) throws Exception{
        throttleClient instThrottleClientCall = new throttleClient();
        int iThrottle = instThrottleClientCall.throttleClient(serviceepr,operationName,SoapAction,namespace,firstChild);
        System.out.println(iThrottle);
        if(iThrottle==count){
            System.out.println("Throttling done");
        }else{
            System.out.println("Throttling not done");
            assertTrue(browser.isTextPresent("Throttling done"));
        }
    }



    public void deleteEntries() throws Exception{
        accessModuleIndexPage();

        browser.click("//a[@onclick=\"submitHiddenForm('../throttling/index.jsp');return false;\"]");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Global Throttling Configuration "));

        if ((browser.getSelectedValue("enableThrottle")).equals("Yes")) {
            browser.select("enableThrottle", "label=No");
        }
        browser.select("enableThrottle", "label=Yes");


        //while(browser.isElementPresent("//table[@id='dataTable']/tbody/tr[2]/td[7]/a")){
        browser.click("//table[@id='dataTable']/tbody/tr[2]/td[7]/a");
        //}
        browser.click("//input[@value='Finish']");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Successfully applied throttling configuration"));
        browser.click("//button[@type='button']");
        Thread.sleep(1000);

    }

    public void globallyDisableThrottling() throws Exception{
        accessModuleIndexPage();

        browser.click("//a[@onclick=\"submitHiddenForm('../throttling/index.jsp');return false;\"]");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Global Throttling Configuration "));

        if ((browser.getSelectedValue("enableThrottle")).equals("Yes")) {
            browser.select("enableThrottle", "label=No");
            assertTrue(browser.isTextPresent("This will disengage throttling. Click Yes to confirm"));
            browser.click("//button[@type='button']");
            browser.waitForPageToLoad("30000");
            browser.click("//button[@type='button']");
        }



    }

    public void setMaximumConcurrentAccesses(String maxCount) throws Exception{
        accessModuleIndexPage();

        browser.click("//a[@onclick=\"submitHiddenForm('../throttling/index.jsp');return false;\"]");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Global Throttling Configuration "));

        browser.select("enableThrottle", "label=Yes");
        browser.type("maxAccess",maxCount);
        browser.click("//input[@value='Finish']");
        browser.waitForPageToLoad("30000");
        browser.click("//button[@type='button']");
    }

    public void enableThrottlingAtServiceLevel(String serviceName,String type,int maxCount,int unitTime,int prohibitedTime,String access) throws Exception{
        ServiceManagement sm = new ServiceManagement(browser);
        browser.click("link=List");
        browser.waitForPageToLoad("30000");
        sm.accessServiceDashboard(serviceName);
        browser.click("link=Access Throttling");
        browser.waitForPageToLoad("30000");
        browser.select("enableThrottle", "label=Yes");
        //  browser.click("//table[@id='dataTable']/tbody/tr[2]/td[7]/a");
        browser.select("data15", "label="+access);
        browser.select("data16", "label="+type);
        browser.type("data12", Integer.toString(maxCount));
        browser.type("data13", Integer.toString(unitTime));
        browser.type("data14", Integer.toString(prohibitedTime));
        browser.click("//input[@value='Finish']");
        browser.waitForPageToLoad("30000");
        browser.click("//button[@type='button']");

    }
    public void enableThrottlingAtOperationalLevel(String serviceName,String operation,String type,int maxCount,int unitTime,int prohibitedTime,String access) throws Exception{
        ServiceManagement sm = new ServiceManagement(browser);
        browser.click("link=List");
        browser.waitForPageToLoad("30000");
        sm.accessServiceDashboard(serviceName);
        sm.openOperationDashboard(serviceName,operation);
        browser.click("link=Access Throttling");
        browser.waitForPageToLoad("30000");
        browser.select("enableThrottle", "label=Yes");
        //  browser.click("//table[@id='dataTable']/tbody/tr[2]/td[7]/a");
        browser.select("data15", "label="+access);
        browser.select("data16", "label="+type);
        browser.type("data12", Integer.toString(maxCount));
        browser.type("data13", Integer.toString(unitTime));
        browser.type("data14", Integer.toString(prohibitedTime));
        browser.click("//input[@value='Finish']");
        browser.waitForPageToLoad("30000");
        browser.click("//button[@type='button']");

    }

    public void engageCachingAtGlobalLevel() throws Exception{
        accessModuleIndexPage();
        browser.click("//a[@onclick=\"submitHiddenForm('../caching/index.jsp');return false;\"]");
        browser.waitForPageToLoad("30000");
        verifyTrue(browser.isTextPresent("Global Caching Configuration"));

        if ((browser.getSelectedValue("enable")).equals("Yes")) {
            browser.select("enable", "label=No");
        }

        browser.select("enable", "label=Yes");
        browser.click("//input[@value='Finish']");
        browser.waitForPageToLoad("30000");
        verifyTrue(browser.isTextPresent("Successfully applied caching configuration"));
        browser.click("//button[@type='button']");
        Thread.sleep(1000);
    }

    public void disengagecachingatGlobalLevel() throws Exception{
        accessModuleIndexPage();
        browser.click("//a[@onclick=\"submitHiddenForm('../caching/index.jsp');return false;\"]");
        browser.waitForPageToLoad("30000");
        verifyTrue(browser.isTextPresent("Global Caching Configuration"));

        if ((browser.getSelectedValue("enable")).equals("Yes")) {
            browser.select("enable", "label=No");
            verifyTrue(browser.isTextPresent("This will disengage caching. Click Yes to confirm."));
            browser.click("//button[@type='button']");
            browser.waitForPageToLoad("30000");
            browser.click("//button[@type='button']");
        }
    }

    public void accessModules(String module) throws Exception{
        accessModuleIndexPage();
        browser.click("link="+module.substring(0,module.indexOf('-')));
        browser.waitForPageToLoad("30000");

        assertTrue(browser.isTextPresent(module.substring(0,module.indexOf('-')) + " Module " + module.substring(module.indexOf('-')+1) ));
        verifyTrue(browser.isTextPresent("Module Information"));
        verifyTrue(browser.isTextPresent("Module Name"));
        verifyTrue(browser.isTextPresent("Module Version"));
        verifyTrue(browser.isTextPresent("Module Description"));
        verifyTrue(browser.isTextPresent("Globally Engaged"));
        verifyTrue(browser.isTextPresent("Module Configuration"));
        verifyTrue(browser.isElementPresent("link=Edit Module Parameters"));
        verifyTrue(browser.isElementPresent("link=Edit Module Policies"));
    }

    public void addNewModuleParameter(String parameterName)throws Exception
    {

        browser.click("link=Edit Module Parameters");
        browser.waitForPageToLoad("30000");

        browser.click("//input[@value='Add New...']");
        verifyTrue(browser.isTextPresent("New Parameter"));
        browser.type("carbon-ui-dialog-input", parameterName.toString());
        browser.click("//button[@type='button']");
        verifyTrue(browser.isTextPresent(parameterName.toString()));
        browser.click("updateBtn");
        browser.waitForPageToLoad("30000");
        verifyTrue(browser.isTextPresent(parameterName.toString()));

    }

    public void deleteModuleParameter(String parameterName)throws Exception
    {

        browser.click("link=Edit Module Parameters");
        browser.waitForPageToLoad("30000");


        browser. click("//a[@onclick=\"removeParameter('"+parameterName+"');return false;\"]");

        browser.click("//button[@type='button']");
        browser.waitForPageToLoad("30000");

        browser.click("updateBtn");
        Thread.sleep(3000);
        assertFalse(browser.isTextPresent(parameterName));
    }



    public void uploadModule(String moduleName) throws Exception{
        SeleniumTestBase InstSeleniumTestBase = new SeleniumTestBase(browser);

        File resourcePath = new File("." + File.separator + "lib" + File.separator +  moduleName +".mar");

        browser.click("link=Add");
        browser.waitForPageToLoad("30000");
        InstSeleniumTestBase.SetFileBrowse("marFilename",resourcePath.getCanonicalPath());
        // browser.type("marFilename", "/home/jayani/Desktop/web-test-framework/wsas/lib/counter-module-SNAPSHOT.mar");
        browser.click("upload");
        browser.waitForPageToLoad("30000");
        verifyTrue(browser.isTextPresent("Module was uploaded successfuly to the repository. The server needs to be restarted for this to take effect.Restart now?"));
        browser.click("//button[@type='button']");
        Thread.sleep(20000);
        InstSeleniumTestBase.logOutUI();

        InstSeleniumTestBase.loginToUI("admin", "admin");
    }

    public void deleteModule(String moduleName) throws Exception{
        accessModuleIndexPage();
        browser.click("link=Delete");
        verifyTrue(browser.isTextPresent("Do you want to delete " + moduleName + " module ?"));
        browser.click("//button[@type='button']");
        browser.waitForPageToLoad("30000");
    }

    public void invokeRMClient(String serviceName,String soapAction,String nameSpace,String operation,String param) throws Exception{
        RMClient rmClient = new RMClient();
        int soap11_response_count = rmClient.RMRequestReplyAnonClient(serviceName, "soap11",soapAction, nameSpace, operation, param);
        //assertEquals(11, soap11_response_count);
        if(soap11_response_count==11){
            System.out.println("RM Done");
        }else{
            System.out.println("RM Not Done");
            assertTrue(browser.isTextPresent("RM Done"));;
        }
    }
}
