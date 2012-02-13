package org.wso2.carbon.web.test.wsas;
import com.thoughtworks.selenium.*;
import junit.framework.TestCase;


/**
 * Created by IntelliJ IDEA.
 * User: Suminda Chamara
 * Date: Mar 18, 2009
 * Time: 12:35:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class SeleniumTestBase extends TestCase
{
    // General functions

     Selenium browser;
    ReadXMLFile instReadXMLFile = new ReadXMLFile();

    public void setUp() throws Exception {
		browser = BrowserInitializer.getBrowser();
	}

    public void loginToUI(String UserName,String Password) throws Exception   // Login to UI Console
    {
        browser.open("/carbon/admin/login.jsp");
        Thread.sleep(8000);
        browser.type("txtUserName",UserName);
		browser.type("txtPassword",Password);
		browser.click("//input[@value='Sign-in']");
		browser.waitForPageToLoad("30000");
    }
     public void logOutUI()
     {
         browser.click("link=Sign-out");
		 browser.waitForPageToLoad("30000");
     }
 // User Management Functions

   public void addNewUser(String Uname,String Password)throws Exception    // Add new system user
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
		assertTrue(browser.isTextPresent("User "+Uname+" added successfully."));
		browser.click("//button[@type='button']");
		assertTrue(browser.isTextPresent(Uname));
    }

    public void addNewRole(String RoleName,String AsignUser,String Permission1)throws Exception   // Create New internal user store
       {
           browser.click("link=User Management");
           browser.waitForPageToLoad("30000");
           browser.click("link=Roles");
           browser.waitForPageToLoad("30000");
           browser.click("link=Add New Role");
           browser.waitForPageToLoad("30000");
           browser.type("roleName",RoleName);
           browser.click("//input[@value='Next >']");
           browser.waitForPageToLoad("30000");

           if(Permission1 == "Login to admin console")
           {
               browser.click("selectedPermissions");
               browser.click("//input[@value='Next >']");
               browser.waitForPageToLoad("30000");
               browser.type("org.wso2.usermgt.role.add.filter", "*");
               browser.click("//input[@value='Search']");
               browser.waitForPageToLoad("30000");
               browser.click("//input[@name='selectedUsers' and @value='"+AsignUser+"']");
               browser.click("//input[@value='Finish']");
               browser.waitForPageToLoad("30000");
               assertTrue(browser.isTextPresent("Role "+RoleName+" added successfully."));
               browser.click("//button[@type='button']");
               assertTrue(browser.isTextPresent(RoleName));
               Thread.sleep(2000);
           }
           else
           {
               browser.click("selectedPermissions");
               browser.click("//input[@name='selectedPermissions' and @value='"+Permission1+"']");
               browser.click("//input[@value='Next >']");
               browser.waitForPageToLoad("30000");
               browser.type("org.wso2.usermgt.role.add.filter", "*");
               browser.click("//input[@value='Search']");
               browser.waitForPageToLoad("30000");
               browser.click("//input[@name='selectedUsers' and @value='"+AsignUser+"']");
               browser.click("//input[@value='Finish']");
               browser.waitForPageToLoad("30000");
               assertTrue(browser.isTextPresent("Role "+RoleName+" added successfully."));
               browser.click("//button[@type='button']");
               assertTrue(browser.isTextPresent(RoleName));
               Thread.sleep(2000);
           }

       }

     public void deleteUser(String Uname) throws Exception
    {
    	browser.click("link=User Management");
		browser.waitForPageToLoad("30000");
		browser.click("link=Users");
		browser.waitForPageToLoad("30000");

        String tmp = "//a[@onclick=\"deleteUser('"+Uname+"')\"]";
        System.out.println(tmp);
        browser.click(tmp);
     	browser.click("//button[@type='button']");
		browser.waitForPageToLoad("30000");
		browser.click("//button[@type='button']");

   }
    public void deleteRole(String RoleName)
    {
        browser.open("/carbon/admin/index.jsp?loginStatus=true");
		browser.click("link=User Management");
		browser.waitForPageToLoad("30000");
		browser.click("link=Roles");
		browser.waitForPageToLoad("30000");
		browser.click("//a[@onclick=\"deleteUserGroup('"+RoleName+"')\"]");
		browser.click("//button[@type='button']");
		browser.waitForPageToLoad("30000");
		assertTrue(browser.isTextPresent("Role "+RoleName+" deleted successfully."));
		browser.click("//button[@type='button']");
    }

 // Service Management Functions - 18.03.2009

    public void serviceActivation(String ServiceName,String Option)throws Exception
    {
        browser.open("/carbon/service-mgt/index.jsp?region=region1&item=services_list_menu&ordinal=0");
		browser.click("link="+ServiceName);
		browser.waitForPageToLoad("30000");
        if(Option=="Deactivate")
        {
            browser.click("link=Deactivate");
            Thread.sleep(3000);
            assertTrue(browser.isTextPresent("Inactive"));
            Thread.sleep(3000);
            assertTrue(browser.isTextPresent("Activate"));
        }
        else if(Option=="Activate")
        {
            browser.click("link=Activate");
            Thread.sleep(3000);
            assertTrue(browser.isTextPresent("Active"));
        }
    }

  
    



}
