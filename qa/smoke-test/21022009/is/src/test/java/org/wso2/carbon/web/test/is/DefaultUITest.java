package org.wso2.carbon.web.test.is;

import org.wso2.carbon.web.test.common.SeleniumTestBase;

/**
 * Created by IntelliJ IDEA.
 * User: Yumani Ranaweera
 * Date: May 21, 2009
 * Time: 9:31:11 AM
 * To change this template use File | Settings | File Templates.
 */


public class DefaultUITest extends CommonSetup {

    public DefaultUITest(String text) {
        super(text);
    }


    /* Check the default UI */
    public void testMainUI() throws Exception {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(selenium);

        selenium.open("/carbon/admin/login.jsp");
        assertTrue(selenium.isElementPresent("link=Sign Up"));
        assertTrue(selenium.isElementPresent("link=InfoCard/OpenID Sign In"));
        assertTrue(selenium.isElementPresent("//div[@id='menu']/ul/li[2]"));
        selenium.click("link=Sign Up");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Sign Up"));
        assertTrue(selenium.isElementPresent("//div[@id='loginbox']/a/img"));
        assertTrue(selenium.isElementPresent("//td[2]/div/a/img"));


    }
}
