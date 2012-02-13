package org.wso2.carbon.web.test.wsas;

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;
import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.ServiceManagement;
import org.wso2.carbon.web.test.ds.common.BrowserInitializer;

import java.awt.event.KeyEvent;


/**
 * Created by IntelliJ IDEA.
 * User: Suminda Chamara
 * Date: Mar 24, 2009
 * Time: 4:39:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class test  extends TestCase {
    Selenium browser;
    public void setUp() throws Exception
    {
		browser = BrowserInitializer.getBrowser();
	}
    public void testMyTest()throws Exception
    {

       

        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
        mySeleniumTestBase.loginToUI("admin","admin");
       mySeleniumTestBase.addNewUser("Chamara","Chamara");
       mySeleniumTestBase.addNewRole("wso2","Chamara","Login to admin console");

       mySeleniumTestBase.logOutUI();
       mySeleniumTestBase.loginToUI("Chamara","Chamara");
       assertTrue(browser.isTextPresent("Signed-in as Chamara"));
       mySeleniumTestBase.logOutUI();
       mySeleniumTestBase.loginToUI("admin","admin");
       browser.click("link=User Management");
	   browser.waitForPageToLoad("30000");
	   browser.click("link=Users");
	   browser.waitForPageToLoad("30000");
       String readUserName="";
       String user = "Chamara";
       int i=1;
       while (!user.equals(readUserName))
       {
            readUserName = browser.getText("//table[@id='userTable']/tbody/tr[" + Integer.toString(i) + "]/td");
            i = i + 1;
        }
        i = i-1;
        browser.click("//table[@id='userTable']/tbody/tr["+Integer.toString(i)+"]/td[2]/a[1]");
		browser.waitForPageToLoad("30000");
		browser.type("newPassword", "Chamara123");
		browser.type("checkPassword", "Chamara123");
		browser.click("//input[@value='Change']");
		browser.waitForPageToLoad("30000");
		assertTrue(browser.isTextPresent("Password of user "+user+" successfully changed"));
		browser.click("//button[@type='button']");
        mySeleniumTestBase.logOutUI();
        mySeleniumTestBase.loginToUI("Chamara","Chamara123");
        assertTrue(browser.isTextPresent("Signed-in as Chamara"));

    }

    }



