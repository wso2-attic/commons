package org.wso2.carbon.common.test.utils;

import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.wso2.carbon.authenticator.proxy.test.ServerLogin;
import org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings;

//Template class for running all the test cases.

public abstract class TestTemplate extends TestCase {

    private static final Log log = LogFactory.getLog(TestTemplate.class);
    protected String sessionCookie = null;
    private ServerLogin serverLogin;
    protected String frameworkPath;

    // The template method

    @Test
    public void testTemplate() throws Exception {
        log.debug("loading framework settings");
        FrameworkSettings.getProperty();
        serverLogin = new ServerLogin();
        frameworkPath = FrameworkSettings.getFrameworkPath();

        /* the three different kind of tests that need to tested.
           The common config instantiation for all the tests */
        log.debug("set keystores");
        setKeyStore();
        log.debug("running init method");
        init();

        //Test without login
        log.debug("Run runFailureCase template");
        runFailureCase();

        log.debug("Relogin to the server");
        sessionCookie = login();

        //Test with login
        log.debug("Run runSuccessCase template");
        runSuccessCase();

        log.debug("logout from the server");
        logout();

        //Test with logout
        log.debug("runFailureCase with null session cookie");
        runFailureCase();

        // Cleaning up the configurations
        log.debug("Relogin to the server");
        sessionCookie = login();
        log.debug("run cleanup template method");
        cleanup();

    }

    private void setKeyStore() {
        String clientTrustStorePath = FrameworkSettings.TRUSTSTORE_PATH;
        System.setProperty("javax.net.ssl.trustStore", clientTrustStorePath);
        System.setProperty("javax.net.ssl.trustStorePassword", FrameworkSettings.TRUSTSTORE_PASSWORD);
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");
    }

    public abstract void init();


    //for the test that is expected to pass(in login mode)
    public abstract void runSuccessCase();


    //for the test that is expected to fail(without login and after logout)
    public abstract void runFailureCase();


    public abstract void cleanup();


    //the concrete method for login
    public String login() {
        try {
            return serverLogin.login();
        } catch (Exception e) {
            log.debug("Server login failed");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }


    //the concrete method for logout
    protected void logout() {
        try {
            serverLogin.logout();
        } catch (Exception e) {
            log.debug("Server logout failed");
            e.printStackTrace();
        }
    }

}
