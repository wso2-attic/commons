package org.wso2.carbon.web.test.is;

import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.ProfileManagement;

/**
 * Created by IntelliJ IDEA.
 * User: Yumani Ranaweera
 * Date: May 19, 2009
 * Time: 11:06:37 AM
 * To change this template use File | Settings | File Templates.
 */


public class ProfileManagementTest extends CommonSetup {

    public ProfileManagementTest(String text) {
        super(text);
    }

    //Login to admin console and test Logging.
    public void testRun() throws Exception {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(selenium);
        mySeleniumTestBase.loginToUI("admin", "admin");
    }


    /* Tests Profile Management UI */
    public void testProfileManagementUI() throws Exception {
        ProfileManagement instProfileManagement = new ProfileManagement(selenium);
        instProfileManagement.testProfileManagementUI("http://schemas.xmlsoap.org/ws/2005/05/identity", "default");
       // SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(selenium);
       // instSeleniumTestBase.logOutUI();
    }

    public void testSignOut() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(selenium);
        instSeleniumTestBase.logOutUI();
    }
}


