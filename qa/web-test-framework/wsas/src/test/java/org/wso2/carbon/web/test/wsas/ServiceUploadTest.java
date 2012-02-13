package org.wso2.carbon.web.test.wsas;
/**
 * Created by IntelliJ IDEA.
 * User: Suminda Chamara
 * Date: Mar 11, 2009
 * Time: 2:40:50 PM
 * To change this template use File | Settings | File Templates.
 *
 * Data Services/Jar Services and EJB services not included.
 * // Do not change the uploading files because in verification section it's checking file names.
 */

import com.thoughtworks.selenium.*;

import java.awt.event.KeyEvent;

import junit.framework.TestCase;
import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.ds.common.BrowserInitializer;

public class ServiceUploadTest extends TestCase

{
    Selenium browser;


    public void setUp() throws Exception {
        browser = BrowserInitializer.getBrowser();
    }

    public void testRun() throws Exception                 //Add new system user and login to wsas console
    {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
        instSeleniumTestBase.loginToUI("admin", "admin");

    }

    public void testPojo_upload() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
        ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
        ReadXMLFileTest instConfigTest = new ReadXMLFileTest("Framework.xml");
        String FilePath = instReadXMLFileTest.ReadConfig("PojoUpload", "ServiceUpload");
        String fPath = System.getProperty("user.dir");
        String ServiceName = instReadXMLFileTest.ReadConfig("pojoServiceName", "ServiceUpload");
        String path = fPath.concat(FilePath);
        
        browser.open("/carbon/service-mgt/index.jsp?region=region1&item=services_list_menu");
        browser.click("link=POJO Service");
        browser.waitForPageToLoad("30000");
        instSeleniumTestBase.SetFileBrowse("jarZipFilename", path);
        browser.click("upload");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Files have been uploaded successfully. Please refresh this page in a while to see the status of the created POJO service"));
        Thread.sleep(12000);
        browser.click("//button[@type='button']");
        browser.click("link=List");
        browser.waitForPageToLoad("120000");
        assertTrue(browser.isTextPresent(ServiceName));
    }

     public void testJaxWs_upload() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
        ReadXMLFileTest instConfigTest = new ReadXMLFileTest("Framework.xml");
        String fPath = System.getProperty("user.dir");
        ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
        String FilePath = instReadXMLFileTest.ReadConfig("JaxWsUpload", "ServiceUpload");
        String ServiceName = instReadXMLFileTest.ReadConfig("JaxWsServiceName", "ServiceUpload");
        String path = fPath.concat(FilePath);

        browser.open("/carbon/service-mgt/index.jsp?region=region1&item=services_list_menu");
        browser.click("link=JAX-WS Service");
        browser.waitForPageToLoad("30000");
        instSeleniumTestBase.SetFileBrowse("filename", path);
        browser.click("upload");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Files have been uploaded successfully. Please refresh this page in a while to seethe status of the created JAXWS service"));
        browser.click("//button[@type='button']");
        Thread.sleep(12000);
        browser.click("link=List");
        browser.waitForPageToLoad("120000");
        assertTrue(browser.isTextPresent(ServiceName));

    }

    public void testAxis1_upload() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
        ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
        ReadXMLFileTest instConfigTest = new ReadXMLFileTest("Framework.xml");
        String fPath = System.getProperty("user.dir");
        String resourcefile = instReadXMLFileTest.ReadConfig("Axis1resourcefile", "ServiceUpload");
        String wsdlfile = instReadXMLFileTest.ReadConfig("Axis1wsdlfile", "ServiceUpload");
        String ServiceName = instReadXMLFileTest.ReadConfig("Axis1ServiceName", "ServiceUpload");
        String resourcepath = fPath.concat(resourcefile);
        String wsdlpath = fPath.concat(wsdlfile);

        browser.open("/carbon/service-mgt/index.jsp?region=region1&item=services_list_menu");
        browser.click("link=Axis1 Service");
        browser.waitForPageToLoad("30000");
        instSeleniumTestBase.SetFileBrowse("wsddFilename", wsdlpath);
        instSeleniumTestBase.SetFileBrowse("jarResource", resourcepath);
        browser.click("upload");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Files have been uploaded successfully. Please refresh this page in a while to see the status of the created Axis1 service"));
        browser.click("//button[@type='button']");
        Thread.sleep(12000);
        browser.click("link=List");
        browser.waitForPageToLoad("120000");
        assertTrue(browser.isTextPresent(ServiceName));
    }

    public void testSpring_upload() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
        ReadXMLFileTest instConfigTest = new ReadXMLFileTest("Framework.xml");
        String fPath = System.getProperty("user.dir");
        ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
        String Contextfile = instReadXMLFileTest.ReadConfig("SpringContextfile", "ServiceUpload");
        String Beansfile = instReadXMLFileTest.ReadConfig("SpringBeansfile", "ServiceUpload");
        String contextPath = fPath.concat(Contextfile);
        String beanPath = fPath.concat(Beansfile);

        browser.open("/carbon/service-mgt/index.jsp?pageNumber=0");
        browser.click("link=Spring Service");
        browser.waitForPageToLoad("30000");
        instSeleniumTestBase.SetFileBrowse("springContext", contextPath);
        instSeleniumTestBase.SetFileBrowse("springBeans", beanPath);
        browser.click("upload");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Select Beans to Deploy"));
        assertTrue(browser.isTextPresent("SimpleMathBean"));
        browser.click("chkBeans");
        browser.click("//div[@id='workArea']/form/table/tbody/tr[3]/td/div/input[1]");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Files have been uploaded successfully. Please refresh this page after a while to see the created service"));
        browser.click("//button[@type='button']");
        Thread.sleep(12000);
        browser.click("link=List");
        browser.waitForPageToLoad("120000");
        assertTrue(browser.isTextPresent("SimpleMathBean"));
    }

    public void testAxis2_upload() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
        ReadXMLFileTest instConfigTest = new ReadXMLFileTest("Framework.xml");
        ReadXMLFileTest instReadXMLFileTest = new ReadXMLFileTest("GlobalConfig.xml");
        String fPath = System.getProperty("user.dir");
        String file = instReadXMLFileTest.ReadConfig("Axis2filePath", "ServiceUpload");
        String ServiceName = instReadXMLFileTest.ReadConfig("Axis2ServiceName", "ServiceUpload");
        String filePath = fPath.concat(file);

        browser.open("/carbon/service-mgt/index.jsp?pageNumber=0");
        browser.click("link=Axis2 Service");
        browser.waitForPageToLoad("30000");
        instSeleniumTestBase.SetFileBrowse("aarFilename", filePath);
        browser.click("upload");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Files have been uploaded successfully. Please refresh this page in a while to see the status of the created Axis2 service"));
        browser.click("//button[@type='button']");
        Thread.sleep(12000);
        browser.click("link=List");
        browser.waitForPageToLoad("120000");
        assertTrue(browser.isTextPresent(ServiceName));

       }

      public void testRemoveServices()throws Exception
    {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
        Thread.sleep(5000);
        instSeleniumTestBase.deleteService("SimpleMathBean");
        Thread.sleep(5000);
        instSeleniumTestBase.deleteService("Myservice1");
        Thread.sleep(5000);
        instSeleniumTestBase.deleteService("jax-ws-test.jar");
        Thread.sleep(5000);
        instSeleniumTestBase.deleteService("axis1wsastest.wsdd");
        Thread.sleep(5000);
        instSeleniumTestBase.deleteService("Arrayservice");
        Thread.sleep(5000);
    }
    public void testLogoutSys()throws Exception
    {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
        instSeleniumTestBase.logOutUI();
    }

}
