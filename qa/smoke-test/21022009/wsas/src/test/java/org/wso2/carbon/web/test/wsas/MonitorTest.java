package org.wso2.carbon.web.test.wsas;

import junit.framework.TestCase;
import org.wso2.carbon.web.test.common.SeleniumTestBase;
import com.thoughtworks.selenium.Selenium;

/**
 * Created by IntelliJ IDEA.
 * User: Suminda Chamara
 * Date: Mar 31, 2009
 * Time: 6:12:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class MonitorTest extends TestCase {
    Selenium browser;

    public void setUp() throws Exception {
        browser = BrowserInitializer.getBrowser();
    }
   public void testLogin()throws Exception
   {
       SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
       instSeleniumTestBase.loginToUI("admin","admin");
   }
    public void testLogs() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
        instSeleniumTestBase.checkSystemLogs();
    }

    public void testFlows() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
        instSeleniumTestBase.checkFlows();

    }
    public void testSystemStats()throws Exception
    {
        browser.click("link=System Statistics");
		browser.waitForPageToLoad("30000");
		assertTrue(browser.isTextPresent("System Statistics"));
		assertTrue(browser.isTextPresent("Service Summary"));
		assertTrue(browser.isTextPresent("Average Response Time"));
		assertTrue(browser.isTextPresent("Minimum Response Time"));
		assertTrue(browser.isTextPresent("Maximum Response Time"));
		assertTrue(browser.isTextPresent("Total Request Count"));
		assertTrue(browser.isTextPresent("Total Response Count"));
		assertTrue(browser.isTextPresent("Total Fault Count"));
		assertTrue(browser.isTextPresent("Active Services"));
		assertTrue(browser.isTextPresent("Server"));
		assertTrue(browser.isTextPresent("Host"));
		assertTrue(browser.isTextPresent("Server Start Time"));
		assertTrue(browser.isTextPresent("System Up Time"));
		assertTrue(browser.isTextPresent("Memory Allocated"));
		assertTrue(browser.isTextPresent("Memory Usage"));
		assertTrue(browser.isTextPresent("Average Response Time(ms) vs. Time(Units)"));
		assertTrue(browser.isTextPresent("Memory(MB) vs. Time(Units)"));
		assertTrue(browser.isTextPresent("Used"));
		assertTrue(browser.isTextPresent("Allocated"));
		assertTrue(browser.isTextPresent("Statistics Configuration"));
		assertTrue(browser.isTextPresent("Statistics Refresh Interval (ms)"));
		assertTrue(browser.isTextPresent("Response Time Graph"));
		assertTrue(browser.isTextPresent("X-Scale (units)"));
		assertTrue(browser.isTextPresent("X-Width (px)"));
		assertTrue(browser.isTextPresent("Memory Graph"));
		assertTrue(browser.isTextPresent("X-Scale (units)"));
		assertTrue(browser.isTextPresent("X-Width (px)"));
		browser.click("//input[@value='Reset']");
		browser.type("responseTimeGraphXScale", "60");
		browser.click("updateStats");
		browser.waitForPageToLoad("30000");
		assertEquals("6", browser.getText("//div[@id='responseTimeGraph']/div/div[9]"));
		browser.type("responseTimeGraphWidth", "600");
		browser.click("updateStats");
		browser.waitForPageToLoad("30000");
		browser.type("memoryGraphXScale", "50");
		browser.click("updateStats");
		browser.waitForPageToLoad("30000");
		assertEquals("5", browser.getText("//div[@id='memoryGraph']/div[1]/div[9]"));
		browser.type("memoryGraphWidth", "600");
		browser.click("updateStats");
		browser.waitForPageToLoad("30000");
		browser.click("//input[@value='Reset']");
    }
    public void testLogout() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
        instSeleniumTestBase.logOutUI();
    }
}
