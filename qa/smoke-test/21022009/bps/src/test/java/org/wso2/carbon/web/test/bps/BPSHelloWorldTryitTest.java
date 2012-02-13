package org.wso2.carbon.web.test.bps;

import com.thoughtworks.selenium.SeleneseTestCase;
import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.DefaultSelenium;


public class BPSHelloWorldTryitTest extends SeleneseTestCase {
     private Selenium browser;

    public void setUp() {
        browser = new DefaultSelenium("localhost",4444, "*iexplore", "https://localhost:9443");
        browser.start();

    }

    public void testBPELUpload() throws Exception {
        browser.open("/carbon/admin/login.jsp");
		browser.type("txtUserName", "admin");
		browser.type("txtPassword", "admin");
		browser.click("//input[@value='Sign-in']");
		browser.waitForPageToLoad("30000");
		browser.click("link=List");
		browser.waitForPageToLoad("30000");
		browser.click("//table[@id='sgTable']/tbody/tr[1]/td[3]/a");
		browser.waitForPageToLoad("30000");
		verifyTrue(browser.isTextPresent("Service Dashboard (HelloWorldNew)"));
		browser.click("link=Try this service");
		//browser.waitForPopUp("Try the HelloWorldNew service", "30000");
		//browser.selectWindow("name=undefined");
        //browser.openWindow("","title=Try the HelloWorldNew service");
        //browser.windowFocus();
        Thread.sleep(10000);
        browser.selectWindow("title=Try the HelloWorldNew service");
		browser.type("input_process_input_0", "test11");
		browser.click("button_process");
    }
}
