package org.wso2.carbon.web.test.is;

import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.LDAPUSerStore;
import org.wso2.carbon.web.test.common.KeyStoreManagement;
import org.wso2.carbon.web.test.common.ClaimManagement;
import org.apache.axiom.om.OMElement;

import java.io.File;

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

public class ISSTSScenario extends CommonSetup {

    public ISSTSScenario(String text) {
        super(text);
    }

    //Login to admin console and test Logging.
    public void testRun() throws Exception {
        SeleniumTestBase myseleniumTestBase = new SeleniumTestBase(selenium);
        myseleniumTestBase.loginToUI("admin", "admin");
    }

    //Connecting WSO2 Identity Server 2.0 to an LDAP based user store.
    public void testLDAPConfigure() throws Exception{
        LDAPUSerStore instLDAPUserStore = new LDAPUSerStore(selenium);
        /*Create the LDAP User store*/
        instLDAPUserStore.createDatabase("ldap://10.100.1.120:10389","uid=admin,ou=system","secret","ou=system","ou=system");

        instLDAPUserStore.externalUsers();
        String permissions[]={"Login to admin console"};
        String selectedUser[]={"ashadi"};
        instLDAPUserStore.externalRoles("ldapuserole",permissions,selectedUser);
    }

    //Map the LDAP attributes the claims read by Carbon.
    public void testClaimManagement() throws Exception{
        ClaimManagement instClaimManagement = new ClaimManagement(selenium);
        instClaimManagement.testUpdateClaim("http://wso2.org/claims","External","First Name","First Name","givenName");
    }

        //Import the wso2carbon.cert.cer certificate to wso2carbon.jks
    public void testImportCertificate() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(selenium);
        File path = new File(".." + File.separator + "is" + File.separator + "src" + File.separator + "lib" + File.separator + "wso2carbon.cert.cer");
        selenium.click("link=Key Stores");
        selenium.waitForPageToLoad("30000");
        selenium.click("//table[@id='keymgtTable']/tbody/tr[2]/td[3]/a[1]");
        selenium.waitForPageToLoad("30000");
        instSeleniumTestBase.SetFileBrowse("browseField",path.getCanonicalPath());
        selenium.click("//input[@value='Import']");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Certificate imported successfully."));
        selenium.click("//button[@type='button']");
    }

    //Security Token Service with WSO2 Identity Server 2.0
    public void testSecurityTokenService() throws Exception{
        STS sts = new STS(selenium);

       // String url=""
        sts.testAddTrustedService("http://10.100.1.120:8280/services/echo", "wso2carbon.cert.cer");
        sts.testApplySecPolicy("ldapuserole");
    }



   // STS Client
    public void testSTSClient() throws Exception{
        OMElement result = new ISSTSClient().STSClient();
        System.out.println("********************************************");
        System.out.println(result);
        System.out.println("********************************************");
    }

    //Clear Environmental Changes
    public void testClearUIChanges() throws Exception{
        //Clear Claim Management changes
        ClaimManagement instClaimManagement = new ClaimManagement(selenium);
        instClaimManagement.testUpdateClaim("http://wso2.org/claims","External","First Name","First Name","http://wso2.org/claims/givenname");

        //Clear LDAP User Store changes
        LDAPUSerStore instLDAPUserStore = new LDAPUSerStore(selenium);
        instLDAPUserStore.deleteExternalUserStore();

        //Clear STS(Security Token Service) changes
        STS sts = new STS(selenium);
        sts.testDeleteSecPolicy();
        sts.testDeleteTrustedService();

        //Sign out from IS Server
        SeleniumTestBase inseleniumTestBase = new SeleniumTestBase(selenium);
        inseleniumTestBase.logOutUI();
    }

}