package org.wso2.carbon.web.test.gs;

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;

/**
 * Created by IntelliJ IDEA.
 * User: Yumani Ranaweera
 * Date: May 7, 2009
 * Time: 2:49:57 PM
 * To change this template use File | Settings | File Templates.
 */


public class CommonSetup extends TestCase {

    static Selenium selenium;

    public CommonSetup(String text) {
        super(text);
    }

    public void setUp() throws Exception {
        selenium = BrowserInitializer.getbrowser();
    }
}
