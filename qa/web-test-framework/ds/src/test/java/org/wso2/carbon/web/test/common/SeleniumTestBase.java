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

import com.thoughtworks.selenium.*;
import junit.framework.TestCase;

import java.awt.event.KeyEvent;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.File;


public class SeleniumTestBase extends TestCase {
    // General functions

    Selenium browser;
    Properties properties = new Properties();

    public SeleniumTestBase(Selenium _browser) {
        browser = _browser;
    }



    public void loginToUI(String UserName, String Password) throws Exception   // Login to UI Console
    {
        FileInputStream freader=new FileInputStream(".." + File.separator + "commons" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "framework.properties");
        properties.load(freader);
        String context_root =  properties.getProperty("context.root");

        checkError() ;
        if (context_root.equals(null))
        {
            browser.open("/carbon/admin/login.jsp");
//            Thread.sleep(8000);
        }
        else
        {
            browser.open(context_root + "/carbon/admin/login.jsp");
//            Thread.sleep(8000);
        }
        for (int second = 0;; second++) {
            if (second >= 50) fail("timeout");
            try { if (browser.isElementPresent("txtUserName")) break; } catch (Exception e) {}
            Thread.sleep(1000);
        }

        //   browser.type("txtbackendURL","https://localhost:9443/services/");
        browser.type("txtUserName", UserName);
        browser.type("txtPassword", Password);
        browser.click("//input[@value='Sign-in']");
        browser.waitForPageToLoad("90000");
        freader.close();
    }

    public void logOutUI() throws InterruptedException {
        checkError() ;
        for (int second = 0;; second++) {
            if (second >= 15) fail("timeout");
            try { if (browser.isElementPresent("link=Sign-out")) break; } catch (Exception e) {}
            Thread.sleep(1000);
        }
        Thread.sleep(2000);
        browser.click("link=Sign-out");
        browser.waitForPageToLoad("30000");
    }
    // User Management Functions

    public void addNewUser(String Uname, String Password) throws Exception    // Add new system user
    {

        Thread.sleep(1000);
        browser.click("link=User Management");
        browser.waitForPageToLoad("30000");
        browser.click("link=Users");
        browser.waitForPageToLoad("30000");
        browser.type("org.wso2.usermgt.internal.filter", "*");
		browser.click("//input[@value='Search']");
		browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Add New User"));
        browser.click("link=Add New User");
        browser.waitForPageToLoad("30000");
        browser.type("username", Uname);
        browser.type("password", Password);
        browser.type("retype", Password);
        browser.click("//input[@value='Finish']");
        browser.waitForPageToLoad("30000");
        //     assertTrue(browser.isTextPresent("User " + Uname + " added successfully."));
//        browser.click("//button[@type='button']");

        assertTrue(browser.isTextPresent(Uname));
        if (browser.isElementPresent("//div[@id='messagebox-error']/p"))
            browser.click("//button[@type='button']");
    }

    public void addNewRole(String RoleName, String AsignUser, String Permission1) throws Exception   // Create New internal user store
    {
        /* Permissions :-     Login to admin console
                             manage-configuration
                             manage-security
                             upload-services
                             manage-services
                             monitor-system
        */
        browser.click("link=User Management");
        browser.waitForPageToLoad("30000");
        browser.click("link=Roles");
        browser.waitForPageToLoad("30000");
        browser.click("link=Add New Role");
        browser.waitForPageToLoad("30000");
        browser.type("roleName", RoleName);
        browser.click("//input[@value='Next >']");
        browser.waitForPageToLoad("30000");

        if (Permission1 == "Login to admin console") {
            browser.click("selectedPermissions");

        } else if(Permission1 == "SelectAll"){
            browser.click("link=Select All");
            
        } else {
            browser.click("selectedPermissions");
            browser.click("//input[@name='selectedPermissions' and @value='" + Permission1 + "']");
           
        }
         browser.click("//input[@value='Next >']");
            browser.waitForPageToLoad("30000");
            browser.type("org.wso2.usermgt.role.add.filter", "*");
            browser.click("//input[@value='Search']");
            browser.waitForPageToLoad("30000");
            browser.click("//input[@name='selectedUsers' and @value='" + AsignUser + "']");
            browser.click("//input[@value='Finish']");
            browser.waitForPageToLoad("30000");
//            assertTrue(browser.isTextPresent("Role " + RoleName + " added successfully."));
//            browser.click("//button[@type='button']");
            assertTrue(browser.isTextPresent(RoleName));
            Thread.sleep(2000);

    }

    public void deleteUser(String Uname) throws Exception {
        browser.click("link=User Management");
        browser.waitForPageToLoad("30000");
        browser.click("link=Users");
        browser.waitForPageToLoad("30000");
        String tmp = "//a[@onclick=\"deleteUser('" + Uname + "')\"]";
        browser.click(tmp);
        browser.click("//button[@type='button']");
        browser.waitForPageToLoad("30000");
//        browser.click("//button[@type='button']");
        //       browser.waitForPageToLoad("30000");
    }

    public void deleteRole(String RoleName)throws Exception{
        FileInputStream freader=new FileInputStream(".." + File.separator + "commons" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "framework.properties");
        properties.load(freader);
        freader.close();
        String context_root =  properties.getProperty("context.root");
        if (context_root.equals(null))
        {
            browser.open("/carbon/admin/index.jsp?loginStatus=true");

        }
        else
        {
            browser.open(context_root + "/carbon/admin/index.jsp?loginStatus=true");
        }

        Thread.sleep(3000);
        browser.click("link=User Management");
        browser.waitForPageToLoad("30000");
        browser.click("link=Roles");
        browser.waitForPageToLoad("30000");
        browser.click("//a[@onclick=\"deleteUserGroup('" + RoleName + "')\"]");
        browser.click("//button[@type='button']");
        browser.waitForPageToLoad("30000");
        //      assertTrue(browser.isTextPresent("Role " + RoleName + " deleted successfully."));
        //     browser.click("//button[@type='button']");
    }

    // Service Management Functions - 18.03.2009

    public void serviceActivation(String ServiceName, String Option) throws Exception {
        FileInputStream freader=new FileInputStream(".." + File.separator + "commons" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "framework.properties");
        properties.load(freader);
        freader.close();
        String context_root =  properties.getProperty("context.root");
        if (context_root.equals(null))
        {
            browser.open("/carbon/service-mgt/index.jsp?region=region1&item=services_list_menu&ordinal=0");

        }
        else
        {
            browser.open(context_root + "/carbon/service-mgt/index.jsp?region=region1&item=services_list_menu&ordinal=0");
        }

        browser.click("link=" + ServiceName);
        browser.waitForPageToLoad("30000");
        if (Option == "Deactivate") {
            browser.click("link=Deactivate");
            Thread.sleep(3000);
            assertTrue(browser.isTextPresent("Inactive"));
            Thread.sleep(3000);
            assertTrue(browser.isTextPresent("Activate"));
        } else if (Option == "Activate") {
            browser.click("link=Activate");
            Thread.sleep(3000);
            assertTrue(browser.isTextPresent("Active"));
        }
    }

    public void checkSystemLogs() throws Exception {
        String getvalue;
        browser.click("link=System Logs");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("System Logs"));
        assertTrue(browser.isTextPresent("Help"));
        assertTrue(browser.isTextPresent("Search Logs"));
        assertTrue(browser.isTextPresent("View"));
        browser.select("logLevelID", "label=TRACE");
        browser.waitForPageToLoad("30000");
        if (!(browser.getSelectedValue("id=logLevelID")).equals("TRACE")) {
            assertTrue(browser.isTextPresent("Trace Not Selected"));
        }
        browser.select("logLevelID", "label=DEBUG");
        browser.waitForPageToLoad("30000");
        if (!(browser.getSelectedValue("id=logLevelID")).equals("DEBUG")) {
            assertTrue(browser.isTextPresent("DEBUG Not Selected"));
        }
        browser.select("logLevelID", "label=INFO");
        browser.waitForPageToLoad("30000");
        if (!(browser.getSelectedValue("id=logLevelID")).equals("INFO")) {
            assertTrue(browser.isTextPresent("INFO Not Selected"));
        }
        browser.select("logLevelID", "label=WARN");
        browser.waitForPageToLoad("30000");
        if (!(browser.getSelectedValue("id=logLevelID")).equals("WARN")) {
            assertTrue(browser.isTextPresent("WARN Not Selected"));
        }
        browser.select("logLevelID", "label=ERROR");
        browser.waitForPageToLoad("30000");
        if (!(browser.getSelectedValue("id=logLevelID")).equals("ERROR")) {
            assertTrue(browser.isTextPresent("ERROR Not Selected"));
        }
        browser.select("logLevelID", "label=FATAL");
        browser.waitForPageToLoad("30000");
        if (!(browser.getSelectedValue("id=logLevelID")).equals("FATAL")) {
            assertTrue(browser.isTextPresent("FATAL Not Selected"));
        }
        browser.select("logLevelID", "label=ALL");
        browser.waitForPageToLoad("30000");
        if (!(browser.getSelectedValue("id=logLevelID")).equals("ALL")) {
            assertTrue(browser.isTextPresent("ALL Not Selected"));
        }
        browser.type("logkeyword", "INFO");
        browser.click("//a[@onclick='javascript:searchLog(); return false;']");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("INFO"));
        browser.click("clearLogs");
        assertTrue(browser.isTextPresent("exact:Do you want to clear all the logs?"));
        browser.click("//button[@type='button']");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("No log entries found"));
    }

    public void deleteService(String serviceGroupName)throws Exception
    {
        browser.click("link=List");
        browser.waitForPageToLoad("30000");
        browser.click("//input[@name='serviceGroups' and @value='"+serviceGroupName+"']");
        browser.click("delete1");
        Thread.sleep(1000);
        browser.click("//button[@type='button']");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Successfully deleted selected service groups"));
        browser.click("//button[@type='button']");
        Thread.sleep(5000);

    }

    public void checkFlows()throws Exception
    {
        browser.click("link=Message Flows");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Show Text View"));
        assertTrue(browser.isTextPresent("Message Flows (Graphical View)"));
        assertTrue(browser.isTextPresent("In Flow"));
        assertTrue(browser.isTextPresent("Out Flow"));
        assertTrue(browser.isTextPresent("In Fault Flow"));
        assertTrue(browser.isTextPresent("Out Fault Flow"));
        browser.click("link=Show Text View");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Show Graphical View"));
        assertTrue(browser.isTextPresent("In Flow"));
        assertTrue(browser.isTextPresent("Out Flow"));
        assertTrue(browser.isTextPresent("In Fault Flow"));
        assertTrue(browser.isTextPresent("Out Fault Flow"));
        browser.click("link=Show Graphical View");
        browser.waitForPageToLoad("30000");
    }

    public void addNewParameter(String parameterName)throws Exception
    {
        browser.click("link=Parameters");
        browser.waitForPageToLoad("30000");
        browser.click("//input[@value='Add New...']");
        //assertTrue(browser.isTextPresent("Service parameter name"));
        browser.type("carbon-ui-dialog-input", parameterName.toString());
        browser.click("//button[@type='button']");
        assertTrue(browser.isTextPresent(parameterName.toString()));
        browser.click("updateBtn");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent(parameterName.toString()));

    }
    public void addNewGroupParameter(String parameterName)throws Exception
    {
        browser.click("link=Parameters");
        browser.waitForPageToLoad("30000");
        browser.click("//input[@value=' Add New... ']");
        assertTrue(browser.isTextPresent("Service group parameter name"));
        browser.type("carbon-ui-dialog-input", parameterName.toString());
        browser.click("//button[@type='button']");
        assertTrue(browser.isTextPresent(parameterName.toString()));
        browser.click("updateBtn");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent(parameterName.toString()));

    }

    public void deleteOperationalParameter(String parameterName)throws Exception
    {
        browser.click("link=Parameters");
        browser.waitForPageToLoad("30000");


        browser.click("//a[@onclick=\"removeOperationParameter('"+parameterName+"');return false;\"]");
        browser.click("//button[@type='button']");
        browser.waitForPageToLoad("30000");

        browser.click("updateBtn");
        Thread.sleep(3000);
    }


    public void deleteParameter(String parameterName)throws Exception
    {
        browser.click("link=Parameters");
        browser.waitForPageToLoad("30000");

        browser.click("//a[@onclick=\"removeParameter('"+parameterName+"');return false;\"]");
        assertTrue(browser.isTextPresent("exact:Do you want to permanently remove service parameter '"+ parameterName +"' ?"));

        browser.click("//button[@type='button']");
        browser.waitForPageToLoad("30000");

        browser.click("updateBtn");
        Thread.sleep(3000);
    }

    public void deleteGroupParameter(String parameterName)throws Exception
    {
        browser.click("link=Parameters");
        browser.waitForPageToLoad("30000");

        browser.click("//a[@onclick=\"removeParameter('"+parameterName+"');return false;\"]");
        assertTrue(browser.isTextPresent("exact:Do you want to permanently remove service group parameter '"+ parameterName +"' ?"));

        browser.click("//button[@type='button']");
        browser.waitForPageToLoad("30000");

        browser.click("updateBtn");
        Thread.sleep(1000);
    }

    public void changeMTOMState(String option)throws Exception
    {
        browser.select("mtomSelector", "label="+option.toString());
        String selectedString = browser.getSelectedValue("mtomSelector");

        if(browser.isTextPresent("Could not change MTOM state for service")){
            System.out.println("Could not change MTOM state for service");
            browser.click("//button[@type='button']");
        }

        Thread.sleep(1000);
        assertTrue(browser.isTextPresent("Changed MTOM state"));
        System.out.println("Changed MTOM state");
        Thread.sleep(1000);
        browser.click("//button[@type='button']");
        if(!selectedString.equals(option.toLowerCase()))
        {
            System.out.println("MTOM State unmatched");
            assertTrue(browser.isTextPresent(""));
        }
    }

    public void removeTransportProtocol(String protocol)throws Exception{  //removes transport protocol
        browser.open("https://localhost:9443/carbon/transport-mgt/service_transport.jsp?serviceName=Axis2Service");
        browser.waitForPageToLoad("30000");

        assertTrue(browser.isTextPresent("Transport Management"));
        assertTrue(browser.isTextPresent("Add Transports"));
        assertTrue(browser.isTextPresent("Protocol"));
        assertTrue(browser.isTextPresent("Exposed Transports"));
        assertTrue(browser.isTextPresent("Protocol"));
        assertTrue(browser.isTextPresent("https"));
        assertTrue(browser.isTextPresent("http"));

        String readText="";
        int i=1;
        while (!protocol.equals(readText)) {
            readText = browser.getText("//table[@id='exposedTransports']/tbody/tr[" + Integer.toString(i) + "]/td[1]");

            i = i + 1;
        }


        if(i==2){
            browser.click("//img[@alt='Remove Transport']");
        }else{
            browser.click("//table[@id='exposedTransports']/tbody/tr[2]/td[" + Integer.toString(i-1) + "]/a[1]/img");
        }

        if(browser.isTextPresent("exact:Do you want to remove " + protocol + " binding for this service ?")){
            assertTrue(browser.isTextPresent("exact:Do you want to remove " + protocol + " binding for this service ?"));
            browser.click("//button[@type='button']");
            browser.waitForPageToLoad("30000");

        }else{
            browser.isTextPresent("Cannot remove transport binding.A service must contain at least one transport binding!");
            browser.click("//button[@type='button']");
        }

        browser.click("//input[@value='Finish']");
        browser.waitForPageToLoad("30000");


    }

    public void addTransportProtocol(String protocol)throws Exception     //adds transport protocol
    {
        browser.open("https://localhost:9443/carbon/transport-mgt/service_transport.jsp?serviceName=Axis2Service");
        browser.waitForPageToLoad("30000");

        assertTrue(browser.isTextPresent("Transport Management"));
        assertTrue(browser.isTextPresent("Add Transports"));
        assertTrue(browser.isTextPresent("Protocol"));
        assertTrue(browser.isTextPresent("Exposed Transports"));
        assertTrue(browser.isTextPresent("Protocol"));
        assertTrue(browser.isTextPresent("https"));
        assertTrue(browser.isTextPresent("http"));

        if ("http".equals(protocol)) {
            browser.select("protocol", "label=http");
        }
        if ("https".equals(protocol)) {
            browser.select("protocol", "label=https");
        }
        if ("mailto".equals(protocol)){
            browser.select("protocol", "label=mailto");
        }
        browser.click("//input[@value='Add']");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent(protocol));

        browser.click("//input[@value='Finish']");
        browser.waitForPageToLoad("30000");

    }
    public void SetFileBrowse(String ID,String FilePath)throws Exception
    {


        browser.windowFocus();

        browser.focus(ID);
        Thread.sleep(10000);

        String browserName= browser.getEval("javascript:{ navigator.appName;}");

        if(browserName.equals("Microsoft Internet Explorer"))
        {

            char[] cArray = FilePath.toCharArray();
            for(int i =0; i < cArray.length; i++)
            {
                char cElement = cArray[i];
                String cString = String.valueOf(cElement);
                Thread.sleep(500);
                if("A".equals(cString))
                {
                    browser.keyDownNative(""+ KeyEvent.VK_SHIFT);
                    browser.keyPressNative(""+(KeyEvent.VK_A));
                    browser.keyUpNative(""+KeyEvent.VK_SHIFT);
                }
                if("B".equals(cString))
                {
                    browser.keyDownNative(""+KeyEvent.VK_SHIFT);
                    browser.keyPressNative(""+(KeyEvent.VK_B));
                    browser.keyUpNative(""+KeyEvent.VK_SHIFT);
                }
                if("C".equals(cString))
                {
                    browser.keyDownNative(""+KeyEvent.VK_SHIFT);
                    browser.keyPressNative(""+(KeyEvent.VK_C));
                    browser.keyUpNative(""+KeyEvent.VK_SHIFT);
                }
                if("D".equals(cString))
                {
                    browser.keyDownNative(""+KeyEvent.VK_SHIFT);
                    browser.keyPressNative(""+(KeyEvent.VK_D));
                    browser.keyUpNative(""+KeyEvent.VK_SHIFT);
                }
                if("E".equals(cString))
                {
                    browser.keyDownNative(""+KeyEvent.VK_SHIFT);
                    browser.keyPressNative(""+(KeyEvent.VK_E));
                    browser.keyUpNative(""+KeyEvent.VK_SHIFT);
                }
                if("F".equals(cString))
                {
                    browser.keyDownNative(""+KeyEvent.VK_SHIFT);
                    browser.keyPressNative(""+(KeyEvent.VK_F));
                    browser.keyUpNative(""+KeyEvent.VK_SHIFT);
                }
                if("G".equals(cString))
                {
                    browser.keyDownNative(""+KeyEvent.VK_SHIFT);
                    browser.keyPressNative(""+(KeyEvent.VK_G));
                    browser.keyUpNative(""+KeyEvent.VK_SHIFT);
                }
                if("H".equals(cString))
                {
                    browser.keyDownNative(""+KeyEvent.VK_SHIFT);
                    browser.keyPressNative(""+(KeyEvent.VK_H));
                    browser.keyUpNative(""+KeyEvent.VK_SHIFT);
                }
                if("I".equals(cString))
                {
                    browser.keyDownNative(""+KeyEvent.VK_SHIFT);
                    browser.keyPressNative(""+(KeyEvent.VK_I));
                    browser.keyUpNative(""+KeyEvent.VK_SHIFT);
                }
                if("J".equals(cString))
                {
                    browser.keyDownNative(""+KeyEvent.VK_SHIFT);
                    browser.keyPressNative(""+(KeyEvent.VK_J));
                    browser.keyUpNative(""+KeyEvent.VK_SHIFT);
                }
                if("K".equals(cString))
                {
                    browser.keyDownNative(""+KeyEvent.VK_SHIFT);
                    browser.keyPressNative(""+(KeyEvent.VK_K));
                    browser.keyUpNative(""+KeyEvent.VK_SHIFT);
                }
                if("L".equals(cString))
                {
                    browser.keyDownNative(""+KeyEvent.VK_SHIFT);
                    browser.keyPressNative(""+(KeyEvent.VK_L));
                    browser.keyUpNative(""+KeyEvent.VK_SHIFT);
                }
                if("M".equals(cString))
                {
                    browser.keyDownNative(""+KeyEvent.VK_SHIFT);
                    browser.keyPressNative(""+(KeyEvent.VK_M));
                    browser.keyUpNative(""+KeyEvent.VK_SHIFT);
                }
                if("N".equals(cString))
                {
                    browser.keyDownNative(""+KeyEvent.VK_SHIFT);
                    browser.keyPressNative(""+(KeyEvent.VK_N));
                    browser.keyUpNative(""+KeyEvent.VK_SHIFT);
                }
                if("O".equals(cString))
                {
                    browser.keyDownNative(""+KeyEvent.VK_SHIFT);
                    browser.keyPressNative(""+(KeyEvent.VK_O));
                    browser.keyUpNative(""+KeyEvent.VK_SHIFT);
                }
                if("P".equals(cString))
                {
                    browser.keyDownNative(""+KeyEvent.VK_SHIFT);
                    browser.keyPressNative(""+(KeyEvent.VK_P));
                    browser.keyUpNative(""+KeyEvent.VK_SHIFT);
                }
                if("Q".equals(cString))
                {
                    browser.keyDownNative(""+KeyEvent.VK_SHIFT);
                    browser.keyPressNative(""+(KeyEvent.VK_Q));
                    browser.keyUpNative(""+KeyEvent.VK_SHIFT);
                }
                if("R".equals(cString))
                {
                    browser.keyDownNative(""+KeyEvent.VK_SHIFT);
                    browser.keyPressNative(""+(KeyEvent.VK_R));
                    browser.keyUpNative(""+KeyEvent.VK_SHIFT);
                }
                if("S".equals(cString))
                {
                    browser.keyDownNative(""+KeyEvent.VK_SHIFT);
                    browser.keyPressNative(""+(KeyEvent.VK_S));
                    browser.keyUpNative(""+KeyEvent.VK_SHIFT);
                }
                if("T".equals(cString))
                {
                    browser.keyDownNative(""+KeyEvent.VK_SHIFT);
                    browser.keyPressNative(""+(KeyEvent.VK_T));
                    browser.keyUpNative(""+KeyEvent.VK_SHIFT);
                }
                if("U".equals(cString))
                {
                    browser.keyDownNative(""+KeyEvent.VK_SHIFT);
                    browser.keyPressNative(""+(KeyEvent.VK_U));
                    browser.keyUpNative(""+KeyEvent.VK_SHIFT);
                }
                if("V".equals(cString))
                {
                    browser.keyDownNative(""+KeyEvent.VK_SHIFT);
                    browser.keyPressNative(""+(KeyEvent.VK_V));
                    browser.keyUpNative(""+KeyEvent.VK_SHIFT);
                }
                if("W".equals(cString))
                {
                    browser.keyDownNative(""+KeyEvent.VK_SHIFT);
                    browser.keyPressNative(""+(KeyEvent.VK_W));
                    browser.keyUpNative(""+KeyEvent.VK_SHIFT);
                }
                if("X".equals(cString))
                {
                    browser.keyDownNative(""+KeyEvent.VK_SHIFT);
                    browser.keyPressNative(""+(KeyEvent.VK_X));
                    browser.keyUpNative(""+KeyEvent.VK_SHIFT);
                }
                if("Y".equals(cString))
                {
                    browser.keyDownNative(""+KeyEvent.VK_SHIFT);
                    browser.keyPressNative(""+(KeyEvent.VK_Y));
                    browser.keyUpNative(""+KeyEvent.VK_SHIFT);
                }
                if("Z".equals(cString))
                {
                    browser.keyDownNative(""+KeyEvent.VK_SHIFT);
                    browser.keyPressNative(""+(KeyEvent.VK_Z));
                    browser.keyUpNative(""+KeyEvent.VK_SHIFT);
                }
                if("a".equals(cString))
                {
                    browser.keyPressNative(""+(KeyEvent.VK_A));
                }
                if("b".equals(cString))
                {
                    browser.keyPressNative(""+(KeyEvent.VK_B));
                }
                if("c".equals(cString))
                {
                    browser.keyPressNative(""+(KeyEvent.VK_C));
                }
                if("d".equals(cString))
                {
                    browser.keyPressNative(""+(KeyEvent.VK_D));
                }
                if("e".equals(cString))
                {
                    browser.keyPressNative(""+(KeyEvent.VK_E));
                }
                if("f".equals(cString))
                {
                    browser.keyPressNative(""+(KeyEvent.VK_F));
                }
                if("g".equals(cString))
                {
                    browser.keyPressNative(""+(KeyEvent.VK_G));
                }
                if("h".equals(cString))
                {
                    browser.keyPressNative(""+(KeyEvent.VK_H));
                }
                if("i".equals(cString))
                {
                    browser.keyPressNative(""+(KeyEvent.VK_I));
                }
                if("j".equals(cString))
                {
                    browser.keyPressNative(""+(KeyEvent.VK_J));
                }
                if("k".equals(cString))
                {
                    browser.keyPressNative(""+(KeyEvent.VK_K));
                }
                if("l".equals(cString))
                {
                    browser.keyPressNative(""+(KeyEvent.VK_L));
                }
                if("m".equals(cString))
                {
                    browser.keyPressNative(""+(KeyEvent.VK_M));
                }
                if("n".equals(cString))
                {
                    browser.keyPressNative(""+(KeyEvent.VK_N));
                }
                if("o".equals(cString))
                {
                    browser.keyPressNative(""+(KeyEvent.VK_O));
                }
                if("p".equals(cString))
                {
                    browser.keyPressNative(""+(KeyEvent.VK_P));
                }
                if("q".equals(cString))
                {
                    browser.keyPressNative(""+(KeyEvent.VK_Q));
                }
                if("r".equals(cString))
                {
                    browser.keyPressNative(""+(KeyEvent.VK_R));
                }
                if("s".equals(cString))
                {
                    browser.keyPressNative(""+(KeyEvent.VK_S));
                }
                if("t".equals(cString))
                {
                    browser.keyPressNative(""+(KeyEvent.VK_T));
                }
                if("u".equals(cString))
                {
                    browser.keyPressNative(""+(KeyEvent.VK_U));
                }
                if("v".equals(cString))
                {
                    browser.keyPressNative(""+(KeyEvent.VK_V));
                }
                if("w".equals(cString))
                {
                    browser.keyPressNative(""+(KeyEvent.VK_W));
                }
                if("x".equals(cString))
                {
                    browser.keyPressNative(""+(KeyEvent.VK_X));
                }
                if("y".equals(cString))
                {
                    browser.keyPressNative(""+(KeyEvent.VK_Y));
                }
                if("z".equals(cString))
                {
                    browser.keyPressNative(""+(KeyEvent.VK_Z));
                }
                if(".".equals(cString))
                {
                    browser.keyPressNative(""+(KeyEvent.VK_PERIOD));
                }
                if("/".equals(cString))
                {
                    browser.keyPressNative(""+(KeyEvent.VK_SLASH));
                }
                if("\\".equals(cString))
                {
                    browser.keyPressNative(""+(KeyEvent.VK_BACK_SLASH));
                }
                if(":".equals(cString))
                {
                    browser.keyDownNative(""+KeyEvent.VK_SHIFT);
                    browser.keyPressNative(""+KeyEvent.VK_SEMICOLON);
                    browser.keyUpNative(""+KeyEvent.VK_SHIFT);
                }
                if("-".equals(cString))
                {
                    browser.keyPressNative(""+KeyEvent.VK_MINUS);
                }
                if("_".equals(cString))
                {
                    browser.keyDownNative(""+KeyEvent.VK_SHIFT);
                    browser.keyPressNative(""+KeyEvent.VK_UNDERSCORE);
                    browser.keyUpNative(""+KeyEvent.VK_SHIFT);
                }

                if("1".equals(cString))
                {
                    browser.keyPressNative(""+KeyEvent.VK_1);
                }
                if("2".equals(cString))
                {
                    browser.keyPressNative(""+KeyEvent.VK_2);
                }
                if("3".equals(cString))
                {
                    browser.keyPressNative(""+KeyEvent.VK_3);
                }
                if("4".equals(cString))
                {
                    browser.keyPressNative(""+KeyEvent.VK_4);
                }
                if("5".equals(cString))
                {
                    browser.keyPressNative(""+KeyEvent.VK_5);
                }
                if("6".equals(cString))
                {
                    browser.keyPressNative(""+KeyEvent.VK_6);
                }
                if("7".equals(cString))
                {
                    browser.keyPressNative(""+KeyEvent.VK_7);
                }
                if("8".equals(cString))
                {
                    browser.keyPressNative(""+KeyEvent.VK_8);
                }
                if("9".equals(cString))
                {
                    browser.keyPressNative(""+KeyEvent.VK_9);
                }
                if("0".equals(cString))
                {
                    browser.keyPressNative(""+KeyEvent.VK_0);
                }
                if(" ".equals(cString))
                {
                    browser.keyPressNative(""+KeyEvent.VK_SPACE);
                }

            }
        }
        else
        {
            browser.type(ID,FilePath);
        }
    }

    public boolean assignUserToAdminRole(String userName) {

        boolean value = false;

        browser.click("link=User Management");
        browser.waitForPageToLoad("30000");
        browser.click("link=Roles");
        browser.waitForPageToLoad("30000");
        browser.click("link=Edit Users");
        browser.waitForPageToLoad("30000");
        browser.type("org.wso2.usermgt.role.edit.filter", "*");
        browser.click("//input[@value='Search']");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent(userName));
        browser.click("//input[@name='selectedUsers' and @value='" + userName + "']");
        browser.click("//input[@value='Update']");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Role admin updated successfully."));


        if (browser.isTextPresent("Role admin updated successfully.")) {
            value = true;
        }

        browser.click("//button[@type='button']");

        return value;
    }
    
    public void  checkError() {
        if (browser.isElementPresent("link=X")){
            browser.click("link=X");
//            browser.waitForPageToLoad("30000");
        }
    }
    public boolean deleteUsertest(String userName) throws InterruptedException {
        int i = 1;
        boolean found = false;
        while (browser.isElementPresent("//table[@id='userTable']/tbody/tr[" + i + "]/td[1]")) {
            if (browser.getText("//table[@id='userTable']/tbody/tr[" + i + "]/td[1]").equals(userName)) {
                found = true;
                Thread.sleep(1000);
                if (i == 1 || i == 2){
                    browser.click("link=Delete");
                }
                else
                    browser.click("//a[@onclick=\"deleteUser('" + userName + "')\"]");
            }
            i++;
        }
        if (!found)
            return false;
        for (int second = 0; ; second++) {
            if (second >= 60) fail("timeout");
            try {
                if (browser.isElementPresent("messagebox-confirm")) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        browser.click("//button[@type='button']");
        return true;
    }
}
