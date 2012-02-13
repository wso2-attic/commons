package org.wso2.carbon.web.test.wsas;

import junit.framework.TestSuite;
import junit.framework.Test;
import junit.textui.TestRunner;
import junit.extensions.TestSetup;



/**
 * Created by IntelliJ IDEA.
 * User: Suminda Chamara
 * Date: Mar 18, 2009
 * Time: 11:21:21 AM
 * To change this template use File | Settings | File Templates.
 */
public class AllTests extends TestSuite
{
    public AllTests(String Name)
    {
        super(Name);
    }

    public static void main(String srgs[])
    {
        try {
            TestRunner.run(suite());
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public static Test suite()
    {
        TestSuite suite = new TestSuite();
       suite.addTestSuite(UserManagementTest.class);
       suite.addTestSuite(ServiceUploadTest.class);
   //     suite.addTestSuite(ManageAxis1ServiceTest.class);
       suite.addTestSuite(ManageAxis2ServicesTest.class);
    //    suite.addTestSuite(ManagePojoServiceTest.class);
   //     suite.addTestSuite(ManageJaxWsServiceTest.class);
     //  suite.addTestSuite(MonitorTest.class);
     //   suite.addTestSuite(KeyStoreTest.class);
    //    suite.addTestSuite(ToolsTest.class);
    //   suite.addTestSuite(test.class);
   //     suite.addTestSuite(MTOMTest.class);
   //     suite.addTestSuite(SWATest.class);


        TestSetup wrapper = new TestSetup(suite)
        {

              protected void setUp() throws Exception {
                 oneTimeSetUp();
                 }

                 private void oneTimeSetUp() throws Exception {
                 //Settings.loadProperties();
                 //PrepareDatabase.blowAwayDatabase();
                 BrowserInitializer.initBrowser();
                 }

                 private void oneTimeTearDown()
                 {
                 BrowserInitializer.stopBrowser();
                 }

                 protected void tearDown() throws Exception
                 {
                    oneTimeTearDown();
                 }
                 };

                 return wrapper;
                 }
        }
            