package org.wso2.carbon.web.test.is;

import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.ClaimManagement;

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


public class ISClaimManagementTest extends CommonSetup {

    public ISClaimManagementTest(String text) {
        super(text);
    }


    /* Login to admin console and test Logging. */
    public void testSignin() throws Exception {
        SeleniumTestBase myseleniumTestBase = new SeleniumTestBase(selenium);
        myseleniumTestBase.loginToUI("admin", "admin");
    }


    /* Tests Claim Management UI */
    public void testClaimManagementUI() throws Exception {
        ClaimManagement instClaimManagement = new ClaimManagement(selenium);
        instClaimManagement.testCSHelp();
        instClaimManagement.testInternalUSClaimsUI("http://schemas.xmlsoap.org/ws/2005/05/identity");
        instClaimManagement.testExternalUSClaimsUI("http://schemas.xmlsoap.org/ws/2005/05/identity");
    }

    /* Test Basic Claim Management Tests */
    public void testBasicClaimManagementTests() throws Exception{
        ClaimManagement instClaimManagement = new ClaimManagement(selenium);

        /* For Internal User Store */
        instClaimManagement.testAddNewClaim("http://wso2.org/claims","Internal","New Claim","New Claim","http://schemas.xmlsoap.org/ws/2005/05/identity/claims/newclaim","http://wso2.org/claims/test","","","","");
        instClaimManagement.testUpdateClaim("http://wso2.org/claims","Internal","New Claim","New Claim1","http://wso2.org/claims/test1");
        instClaimManagement.testCancelUpdateClaim("http://wso2.org/claims","Internal","New Claim1","New Claim2","http://wso2.org/claims/test2");
        instClaimManagement.testAlreadyExistClaimUri("http://wso2.org/claims","Internal","New Claim3","New Claim3","http://schemas.xmlsoap.org/ws/2005/05/identity/claims/newclaim","http://wso2.org/claims/test3");
        instClaimManagement.testDeleteClaim("http://wso2.org/claims","Internal","New Claim1");

        String storeInfo[]=instClaimManagement.testStoreInformation_OfClaim("http://wso2.org/claims","Internal","State");
        instClaimManagement.testDeleteClaim("http://wso2.org/claims","Internal","State");
        instClaimManagement.testAddClaims("http://wso2.org/claims","Internal",storeInfo[0],storeInfo[1],storeInfo[2],storeInfo[3],storeInfo[4],storeInfo[5],storeInfo[6],storeInfo[7]);

        /* For External User Store */
        if(!instClaimManagement.testExternalUSClaimsUI("http://wso2.org/claims")) {
            instClaimManagement.testAddNewClaim("http://wso2.org/claims","External","New Claim","New Claim","http://schemas.xmlsoap.org/ws/2005/05/identity/claims/newclaim","http://wso2.org/claims/test","","","","");
            instClaimManagement.testUpdateClaim("http://wso2.org/claims","External","New Claim","New Claim1","http://wso2.org/claims/test1");
            instClaimManagement.testCancelUpdateClaim("http://wso2.org/claims","External","New Claim1","New Claim2","http://wso2.org/claims/test2");
            instClaimManagement.testAlreadyExistClaimUri("http://wso2.org/claims","External","New Claim3","New Claim3","http://schemas.xmlsoap.org/ws/2005/05/identity/claims/newclaim","http://wso2.org/claims/test3");
            instClaimManagement.testDeleteClaim("http://wso2.org/claims","External","New Claim1");

            String storeInfo1[]=instClaimManagement.testStoreInformation_OfClaim("http://wso2.org/claims","External","State");
            instClaimManagement.testDeleteClaim("http://wso2.org/claims","External","State");
            instClaimManagement.testAddClaims("http://wso2.org/claims","External",storeInfo1[0],storeInfo1[1],storeInfo1[2],storeInfo1[3],storeInfo1[4],storeInfo1[5],storeInfo1[6],storeInfo1[7]);
        }

    }

    /* Delete the few claims within the default dialect and check the user's default profile. */
     public void testDeleteFewClaims() throws Exception{
         ClaimManagement instClaimManagement = new ClaimManagement(selenium);

        /* For Internal User Store */
         String storeInfo[]=instClaimManagement.testStoreInformation_OfClaim("http://wso2.org/claims","Internal","Telephone");
         instClaimManagement.testDeleteClaim("http://wso2.org/claims","Internal","Telephone");

         String storeInfo1[]=instClaimManagement.testStoreInformation_OfClaim("http://wso2.org/claims","Internal","Address");
         instClaimManagement.testDeleteClaim("http://wso2.org/claims","Internal","Address");

         boolean testprofile[]=ISMyProfile.myProfileUI();

         if(!testprofile[5] && !testprofile[8])
            System.out.println("Claims are successfully deleted...........");
         else
            System.out.println("Claims are not successfully deleted...........");

         String temp1[]=instClaimManagement.testSupported_RequiredAttributes(storeInfo[6],storeInfo[7]);
         String temp2[]=instClaimManagement.testSupported_RequiredAttributes(storeInfo1[6],storeInfo1[7]);

         instClaimManagement.testAddClaims("http://wso2.org/claims","Internal",storeInfo[0],storeInfo[1],storeInfo[2],storeInfo[3],storeInfo[4],storeInfo[5],temp1[0],temp1[1]);
         instClaimManagement.testAddClaims("http://wso2.org/claims","Internal",storeInfo1[0],storeInfo1[1],storeInfo1[2],storeInfo1[3],storeInfo1[4],storeInfo1[5],temp2[0],temp2[1]);

         /* For External User Store */
         if(!instClaimManagement.testExternalUSClaimsUI("http://wso2.org/claims")) {
                      String storeInfo2[]=instClaimManagement.testStoreInformation_OfClaim("http://wso2.org/claims","External","Telephone");
                      instClaimManagement.testDeleteClaim("http://wso2.org/claims","External","Telephone");

                      String storeInfo3[]=instClaimManagement.testStoreInformation_OfClaim("http://wso2.org/claims","External","Address");
                      instClaimManagement.testDeleteClaim("http://wso2.org/claims","External","Address");

                      boolean testprofile1[]=ISMyProfile.myProfileUI();

                      if(!testprofile1[5] && !testprofile1[8])
                         System.out.println("Claims are successfully deleted...........");
                      else
                         System.out.println("Claims are not successfully deleted...........");

                      String temp3[]=instClaimManagement.testSupported_RequiredAttributes(storeInfo2[6],storeInfo2[7]);
                      String temp4[]=instClaimManagement.testSupported_RequiredAttributes(storeInfo3[6],storeInfo3[7]);

                      instClaimManagement.testAddClaims("http://wso2.org/claims","External",storeInfo2[0],storeInfo2[1],storeInfo2[2],storeInfo2[3],storeInfo2[4],storeInfo2[5],temp3[0],temp3[1]);
                      instClaimManagement.testAddClaims("http://wso2.org/claims","External",storeInfo3[0],storeInfo3[1],storeInfo3[2],storeInfo3[3],storeInfo3[4],storeInfo3[5],temp4[0],temp4[1]);
         }
     }

    /* Delete the required claims within the default dialect and try to sign-up */
    public void testRequiredClaim_ForSignUp() throws Exception{
        ClaimManagement instClaimManagement = new ClaimManagement(selenium);
        SeleniumTestBase instseleniumTestBase = new SeleniumTestBase(selenium);

         /* For Internal User Store */
        String RequiredClaim1[] = instClaimManagement.testDeleteRequiredCliam("http://wso2.org/claims","Internal");
        String RequiredClaim2[] = instClaimManagement.testDeleteRequiredCliam("http://wso2.org/claims","Internal");
        String RequiredClaim3[] = instClaimManagement.testDeleteRequiredCliam("http://wso2.org/claims","Internal");

        boolean testprofile[]=ISMyProfile.myProfileUI();
        if(!testprofile[2] && !testprofile[3] && !testprofile[7])
            System.out.println(RequiredClaim1[0] +","+RequiredClaim2[0]+"and"+RequiredClaim3[0]+ " claims are successfully deleted from default profile........");
        else
            System.out.println(RequiredClaim1[0] +","+RequiredClaim2[0]+"and"+RequiredClaim3[0]+ " claims are not deleted from default profile........");

        instseleniumTestBase.logOutUI();

        boolean testSignUp[]=ISSignup.signupUI();
        if(!testSignUp[0] && !testSignUp[1] && !testSignUp[5])
            System.out.println(RequiredClaim1[0] +","+RequiredClaim2[0]+"and"+RequiredClaim3[0]+ " claims are successfully deleted from Sign up........");
        else
            System.out.println(RequiredClaim1[0] +","+RequiredClaim2[0]+"and"+RequiredClaim3[0]+ " claims are not deleted from Sign up........");

        ISSignup.signUpNewUser("tester","tester", "tester", "tester", "ABC Org", "12, flower strt", "SL", "tester@y.com", "0112632436", "0777795242", "tester@tt.com", "http://www.test.com");
        ISSignup.signInSignupUser("tester", "tester");
        instseleniumTestBase.logOutUI();
        instseleniumTestBase.loginToUI("admin","admin");
        ISSignup.DeleteUserFromUM("tester");

        String temp1[]=instClaimManagement.testSupported_RequiredAttributes(RequiredClaim1[7],RequiredClaim1[8]);
        String temp2[]=instClaimManagement.testSupported_RequiredAttributes(RequiredClaim2[7],RequiredClaim2[8]);
        String temp3[]=instClaimManagement.testSupported_RequiredAttributes(RequiredClaim3[7],RequiredClaim3[8]);

        instClaimManagement.testAddClaims("http://wso2.org/claims","Internal",RequiredClaim1[1],RequiredClaim1[2],RequiredClaim1[3],RequiredClaim1[4],RequiredClaim1[5],RequiredClaim1[6],temp1[0],temp1[1]);
        instClaimManagement.testAddClaims("http://wso2.org/claims","Internal",RequiredClaim2[1],RequiredClaim2[2],RequiredClaim2[3],RequiredClaim2[4],RequiredClaim2[5],RequiredClaim2[6],temp2[0],temp2[1]);
        instClaimManagement.testAddClaims("http://wso2.org/claims","Internal",RequiredClaim3[1],RequiredClaim3[2],RequiredClaim3[3],RequiredClaim3[4],RequiredClaim3[5],RequiredClaim3[6],temp3[0],temp3[1]);

        /* For External User Store */
        if(!instClaimManagement.testExternalUSClaimsUI("http://wso2.org/claims")) {
            String RequiredClaim4[] = instClaimManagement.testDeleteRequiredCliam("http://wso2.org/claims","External");
            String RequiredClaim5[] = instClaimManagement.testDeleteRequiredCliam("http://wso2.org/claims","External");
            String RequiredClaim6[] = instClaimManagement.testDeleteRequiredCliam("http://wso2.org/claims","External");

            boolean testprofile1[]=ISMyProfile.myProfileUI();
            if(!testprofile1[2] && !testprofile1[3] && !testprofile1[7])
                System.out.println(RequiredClaim4[0] +","+RequiredClaim5[0]+"and"+RequiredClaim6[0]+ " claims are successfully deleted from default profile........");
            else
                System.out.println(RequiredClaim4[0] +","+RequiredClaim5[0]+"and"+RequiredClaim6[0]+ " claims are not deleted from default profile........");

            instseleniumTestBase.logOutUI();

            boolean testSignUp1[]=ISSignup.signupUI();
            if(!testSignUp1[0] && !testSignUp1[1] && !testSignUp1[5])
                System.out.println(RequiredClaim4[0] +","+RequiredClaim5[0]+"and"+RequiredClaim6[0]+ " claims are successfully deleted from Sign up........");
            else
                System.out.println(RequiredClaim4[0] +","+RequiredClaim5[0]+"and"+RequiredClaim6[0]+ " claims are not deleted from Sign up........");

            ISSignup.signUpNewUser("tester","tester", "tester", "tester", "ABC Org", "12, flower strt", "SL", "tester@y.com", "0112632436", "0777795242", "tester@tt.com", "http://www.test.com");
            ISSignup.signInSignupUser("tester", "tester");
            instseleniumTestBase.logOutUI();
            instseleniumTestBase.loginToUI("admin","admin");
            ISSignup.DeleteUserFromUM("tester");

            String temp4[]=instClaimManagement.testSupported_RequiredAttributes(RequiredClaim4[7],RequiredClaim4[8]);
            String temp5[]=instClaimManagement.testSupported_RequiredAttributes(RequiredClaim5[7],RequiredClaim5[8]);
            String temp6[]=instClaimManagement.testSupported_RequiredAttributes(RequiredClaim6[7],RequiredClaim6[8]);

            instClaimManagement.testAddClaims("http://wso2.org/claims","External",RequiredClaim4[1],RequiredClaim4[2],RequiredClaim4[3],RequiredClaim4[4],RequiredClaim4[5],RequiredClaim4[6],temp4[0],temp4[1]);
            instClaimManagement.testAddClaims("http://wso2.org/claims","External",RequiredClaim5[1],RequiredClaim5[2],RequiredClaim5[3],RequiredClaim5[4],RequiredClaim5[5],RequiredClaim5[6],temp5[0],temp5[1]);
            instClaimManagement.testAddClaims("http://wso2.org/claims","External",RequiredClaim6[1],RequiredClaim6[2],RequiredClaim6[3],RequiredClaim6[4],RequiredClaim6[5],RequiredClaim6[6],temp6[0],temp6[1]);
        }
    }


   /* Delete the required claims within the default dialect, update myprofiles for the claims available, sign-in from openID url. */
    public void testRequiredClaim_forOpenIDUrl() throws Exception{
        ISMyProfile.updateDefaultProfile("admin", "admin", "ABC", "85, testers zone", "SL", "admin@yahoo.com", "123456", "777987676", "admin@t.com", "https://admin.com");
        ClaimManagement instClaimManagement = new ClaimManagement(selenium);
        SeleniumTestBase instseleniumTestBase = new SeleniumTestBase(selenium);

        /* For Internal User Store */
        String RequiredClaim1[] = instClaimManagement.testDeleteRequiredCliam("http://wso2.org/claims","Internal");
        String RequiredClaim2[] = instClaimManagement.testDeleteRequiredCliam("http://wso2.org/claims","Internal");
        String RequiredClaim3[] = instClaimManagement.testDeleteRequiredCliam("http://wso2.org/claims","Internal");

        instseleniumTestBase.logOutUI();
  //      selenium.open("/carbon/admin/login.jsp");
 //       Thread.sleep(10000);
        selenium.click("link=InfoCard/OpenID Sign-in");
		selenium.waitForPageToLoad("30000");
		selenium.type("openIdUrl", "https://"+ISCommon.loadProperties().getProperty("host.name")+":"+ISCommon.loadProperties().getProperty("https.port")+"/openid/admin");
		selenium.click("//input[@value='Login']");
		selenium.waitForPageToLoad("30000");
        selenium.type("password", "admin");
		selenium.click("//input[@value='Login']");
		selenium.waitForPageToLoad("30000");
		assertTrue(selenium.isTextPresent("OpenID User Profile"));
        //..................................................Need to check some assertions.
        selenium.click("approve");
		selenium.waitForPageToLoad("30000");
        Thread.sleep(5000);
        instseleniumTestBase.logOutUI();
        instseleniumTestBase.loginToUI("admin","admin");

        String temp1[]=instClaimManagement.testSupported_RequiredAttributes(RequiredClaim1[7],RequiredClaim1[8]);
        String temp2[]=instClaimManagement.testSupported_RequiredAttributes(RequiredClaim2[7],RequiredClaim2[8]);
        String temp3[]=instClaimManagement.testSupported_RequiredAttributes(RequiredClaim3[7],RequiredClaim3[8]);

        instClaimManagement.testAddClaims("http://wso2.org/claims","Internal",RequiredClaim1[1],RequiredClaim1[2],RequiredClaim1[3],RequiredClaim1[4],RequiredClaim1[5],RequiredClaim1[6],temp1[0],temp1[1]);
        instClaimManagement.testAddClaims("http://wso2.org/claims","Internal",RequiredClaim2[1],RequiredClaim2[2],RequiredClaim2[3],RequiredClaim2[4],RequiredClaim2[5],RequiredClaim2[6],temp2[0],temp2[1]);
        instClaimManagement.testAddClaims("http://wso2.org/claims","Internal",RequiredClaim3[1],RequiredClaim3[2],RequiredClaim3[3],RequiredClaim3[4],RequiredClaim3[5],RequiredClaim3[6],temp3[0],temp3[1]);

        /* For External User Store */
        if(!instClaimManagement.testExternalUSClaimsUI("http://wso2.org/claims")) {
            String RequiredClaim4[] = instClaimManagement.testDeleteRequiredCliam("http://wso2.org/claims","External");
            String RequiredClaim5[] = instClaimManagement.testDeleteRequiredCliam("http://wso2.org/claims","External");
            String RequiredClaim6[] = instClaimManagement.testDeleteRequiredCliam("http://wso2.org/claims","External");

            instseleniumTestBase.logOutUI();
//            selenium.open("/carbon/admin/login.jsp");
//            Thread.sleep(10000);
            selenium.click("link=InfoCard/OpenID Sign-in");
            selenium.waitForPageToLoad("30000");
            selenium.type("openIdUrl", "https://"+ISCommon.loadProperties().getProperty("host.name")+":"+ISCommon.loadProperties().getProperty("https.port")+"/openid/admin");
            selenium.click("//input[@value='Login']");
            selenium.waitForPageToLoad("30000");
            selenium.type("password", "admin");
            selenium.click("//input[@value='Login']");
            selenium.waitForPageToLoad("30000");
            assertTrue(selenium.isTextPresent("OpenID User Profile"));
            //..................................................Need to check some assertions.
            selenium.click("approve");
            selenium.waitForPageToLoad("30000");
            Thread.sleep(5000);
            instseleniumTestBase.logOutUI();
            instseleniumTestBase.loginToUI("admin","admin");

            String temp4[]=instClaimManagement.testSupported_RequiredAttributes(RequiredClaim4[7],RequiredClaim4[8]);
            String temp5[]=instClaimManagement.testSupported_RequiredAttributes(RequiredClaim5[7],RequiredClaim5[8]);
            String temp6[]=instClaimManagement.testSupported_RequiredAttributes(RequiredClaim6[7],RequiredClaim6[8]);

            instClaimManagement.testAddClaims("http://wso2.org/claims","External",RequiredClaim4[1],RequiredClaim4[2],RequiredClaim4[3],RequiredClaim4[4],RequiredClaim4[5],RequiredClaim4[6],temp4[0],temp4[1]);
            instClaimManagement.testAddClaims("http://wso2.org/claims","External",RequiredClaim5[1],RequiredClaim5[2],RequiredClaim5[3],RequiredClaim5[4],RequiredClaim5[5],RequiredClaim5[6],temp5[0],temp5[1]);
            instClaimManagement.testAddClaims("http://wso2.org/claims","External",RequiredClaim6[1],RequiredClaim6[2],RequiredClaim6[3],RequiredClaim6[4],RequiredClaim6[5],RequiredClaim6[6],temp6[0],temp6[1]);
        }
    }

    /*Delete all the claims. */
    public void testDeleteAllClaims() throws Exception{
         ClaimManagement instClaimManagement = new ClaimManagement(selenium);
         instClaimManagement.testDeleteAllClaim("http://wso2.org/claims","Internal");
    }

    /* Sign out from IS server. */
    public void testSignout() throws Exception {
        SeleniumTestBase instseleniumTestBase = new SeleniumTestBase(selenium);
        instseleniumTestBase.logOutUI();
    }

}