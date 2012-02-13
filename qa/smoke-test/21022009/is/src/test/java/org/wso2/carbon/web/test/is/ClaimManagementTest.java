package org.wso2.carbon.web.test.is;

import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.ClaimManagement;

/**
 * Created by IntelliJ IDEA.
 * User: Yumani Ranaweera
 * Date: May 19, 2009
 * Time: 1:06:37 PM
 * To change this template use File | Settings | File Templates.
 */

public class ClaimManagementTest extends CommonSetup {

    public ClaimManagementTest(String text) {
        super(text);
    }


    //Login to admin console and test Logging.
    public void testRun() throws Exception {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(selenium);
        mySeleniumTestBase.loginToUI("admin", "admin");
    }


    /* Tests Profile Management UI */
    public void testClaimManagementUI() throws Exception {
        ClaimManagement instProfileManagement = new ClaimManagement(selenium);
        instProfileManagement.testInternalUSClaimsUI("http://schemas.xmlsoap.org/ws/2005/05/identity");
        instProfileManagement.testExternalUSClaimsUI();
    }

    public void testSignout() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(selenium);
        instSeleniumTestBase.logOutUI();
    }

}
