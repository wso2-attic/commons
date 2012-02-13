/*
 * Copyright 2004,2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.carbon.web.test.common;

import com.thoughtworks.selenium.*;
import junit.framework.TestCase;

import java.awt.event.KeyEvent;


public class SeleniumTestBase extends TestCase {
    // General functions

    Selenium browser;

    public SeleniumTestBase(Selenium _browser) {
        browser = _browser;
    }

    public void loginToUI(String UserName, String Password) throws Exception   // Login to UI Console
    {
        browser.open("/carbon/admin/login.jsp");
        Thread.sleep(8000);
	    browser.type("txtbackendURL","https://localhost:9443/services/");
        browser.type("txtUserName", UserName);
        browser.type("txtPassword", Password);
        browser.click("//input[@value='Sign-in']");
        browser.waitForPageToLoad("30000");
    }

    public void logOutUI() {
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
        assertTrue(browser.isTextPresent("Add New User"));
        browser.click("link=Add New User");
        browser.waitForPageToLoad("30000");
        browser.type("username", Uname);
        browser.type("password", Password);
        browser.type("retype", Password);
        browser.click("//input[@value='Finish']");
        browser.waitForPageToLoad("30000");
   //     assertTrue(browser.isTextPresent("User " + Uname + " added successfully."));
   //     browser.click("//button[@type='button']");
        assertTrue(browser.isTextPresent(Uname));
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
            browser.click("//input[@value='Next >']");
            browser.waitForPageToLoad("30000");
            browser.type("org.wso2.usermgt.role.add.filter", "*");
            browser.click("//input[@value='Search']");
            browser.waitForPageToLoad("30000");
            browser.click("//input[@name='selectedUsers' and @value='" + AsignUser + "']");
            browser.click("//input[@value='Finish']");
            browser.waitForPageToLoad("30000");
            assertTrue(browser.isTextPresent("Role " + RoleName + " added successfully."));
            browser.click("//button[@type='button']");
            assertTrue(browser.isTextPresent(RoleName));
            Thread.sleep(2000);
        } else {
            browser.click("selectedPermissions");
            browser.click("//input[@name='selectedPermissions' and @value='" + Permission1 + "']");
            browser.click("//input[@value='Next >']");
            browser.waitForPageToLoad("30000");
            browser.type("org.wso2.usermgt.role.add.filter", "*");
            browser.click("//input[@value='Search']");
            browser.waitForPageToLoad("30000");
            browser.click("//input[@name='selectedUsers' and @value='" + AsignUser + "']");
            browser.click("//input[@value='Finish']");
            browser.waitForPageToLoad("30000");
            assertTrue(browser.isTextPresent("Role " + RoleName + " added successfully."));
            browser.click("//button[@type='button']");
            assertTrue(browser.isTextPresent(RoleName));
            Thread.sleep(2000);
        }

    }

    public void deleteUser(String Uname) throws Exception {
        browser.click("link=User Management");
        browser.waitForPageToLoad("30000");
        browser.click("link=Users");
        browser.waitForPageToLoad("30000");

        String tmp = "//a[@onclick=\"deleteUser('" + Uname + "')\"]";
       // System.out.println(tmp);
        browser.click(tmp);
        browser.click("//button[@type='button']");
        Thread.sleep(1000);
        browser.click("//button[@type='button']");
        browser.waitForPageToLoad("30000");
    }

    public void deleteRole(String RoleName) {
        browser.open("/carbon/admin/index.jsp?loginStatus=true");
        browser.click("link=User Management");
        browser.waitForPageToLoad("30000");
        browser.click("link=Roles");
        browser.waitForPageToLoad("30000");
        browser.click("//a[@onclick=\"deleteUserGroup('" + RoleName + "')\"]");
        browser.click("//button[@type='button']");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Role " + RoleName + " deleted successfully."));
        browser.click("//button[@type='button']");
    }

    // Service Management Functions - 18.03.2009

    public void serviceActivation(String ServiceName, String Option) throws Exception {
        browser.open("/carbon/service-mgt/index.jsp?region=region1&item=services_list_menu&ordinal=0");
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
		assertTrue(browser.isTextPresent("Service parameter name"));
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
        assertTrue(browser.isTextPresent("Changed MTOM state"));
		browser.click("//button[@type='button']");
        if(!selectedString.equals(option.toLowerCase()))
        {
            System.out.println("MTM State unmatched");
            assertTrue(browser.isTextPresent(""));
        }
    }
    public void SetFileBrowse(String ID,String FilePath)throws Exception
     {

           browser.windowFocus();
           System.out.println("id="+ID);
           browser.focus(ID);

            Thread.sleep(10000);
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

}
