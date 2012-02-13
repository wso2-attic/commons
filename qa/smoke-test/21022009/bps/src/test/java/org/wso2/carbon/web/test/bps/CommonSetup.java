package org.wso2.carbon.web.test.bps;

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;

/**
 * Created by IntelliJ IDEA.
 * User: Charitha K
 * Date: Mar 23, 2009
 * Time: 6:11:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class CommonSetup extends TestCase {
      static Selenium browser;

     public CommonSetup(String text) {
      super(text);
    }

    public void setUp() throws Exception{

         browser = BrowserInitializer.getBrowser();
     }
}

