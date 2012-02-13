package org.wso2.carbon.web.test.ds.common;

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;

import java.util.Properties;
import java.util.Iterator;

import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.ServiceManagement;
import org.wso2.carbon.web.test.common.KeyStoreManagement;
import org.wso2.carbon.web.test.common.SecurityClient;
import org.apache.axiom.om.OMElement;

import javax.xml.namespace.QName;

/**
 * Created by IntelliJ IDEA.
 * User: jayani
 * Date: Dec 10, 2009
 * Time: 1:02:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class DataSrevicesWithRolesTest extends TestCase {

    Selenium browser;
    Properties property;
    String username;
    String password;
    String moduleVersion ;
    String hostname;
    String httpport;
    String contextroot;

    public DataSrevicesWithRolesTest(String text) {
        super(text);
    }

    public void setUp() throws Exception  {
        property = BrowserInitializer.getProperties();
        browser = BrowserInitializer.getBrowser();
        username = property.getProperty("admin.username");
        password = property.getProperty("admin.password");
        moduleVersion = property.getProperty("module.version");
        hostname = property.getProperty("host.name");
        httpport = property.getProperty("http.port");
        contextroot = property.getProperty("context.root");
    }

    public void testLogin() throws Exception                 //Add new system user and login to wsas console
    {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
        mySeleniumTestBase.loginToUI(username, password);

    }

    public void testCreateUsers1() throws Exception           // Create users for check user permissions
    {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
        mySeleniumTestBase.addNewUser("user1", "userone");
    }

    public void testCreateUsers2() throws Exception           // Create users for check user permissions
    {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
        mySeleniumTestBase.addNewUser("user2", "usertwo");
    }

    public void testCreateRoles1() throws Exception {
        /* Permissions :-     Login to admin console
                              manage-configuration
                              manage-security
                              upload-services
                              manage-services
                              monitor-system
         */
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
        mySeleniumTestBase.addNewRole("Role1", "user1", "SelectAll");
    }
    public void testCreateRoles2() throws Exception {
        /* Permissions :-     Login to admin console
                              manage-configuration
                              manage-security
                              upload-services
                              manage-services
                              monitor-system
         */
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
        mySeleniumTestBase.addNewRole("Role2", "user2", "SelectAll");

    }

    public void testAddDataSrevicesWithRoles() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(browser);
        CSVDataService csvDataService = new CSVDataService(browser);
        RDBMSDataService rdbmsDataService = new RDBMSDataService(browser);

        rdbmsDataService.setUpDataBase();
        rdbmsDataService.newDataService("RolesDataService");
        rdbmsDataService.addNewDataSource("MySQLDataSource","RDBMS");

        rdbmsDataService.addQueryName("customers-query","MySQLDataSource","select * from customer");
        rdbmsDataService.addResult("customers","customer","");

        browser.click("newOutputMapping");
        browser.waitForPageToLoad("30000");
        browser.select("cmbDataServiceOMType", "label=attribute");
        browser.type("txtDataServiceOMElementName", "name");
        browser.type("txtDataServiceOMColumnName", "name");
        browser.select("xsdType", "label=xs:string");
        browser.click("Role1");
        browser.click("//input[@value='Add']");
        browser.waitForPageToLoad("30000");
        browser.select("cmbDataServiceOMType", "label=attribute");
        browser.type("txtDataServiceOMElementName", "country");
        browser.type("txtDataServiceOMColumnName", "country");
        browser.select("xsdType", "label=xs:string");
        browser.click("Role2");
        browser.click("//input[@value='Add']");
        browser.waitForPageToLoad("30000");
        browser.click("//input[@value='Main Configuration']");
        browser.click("//button[@type='button']");
        browser.waitForPageToLoad("30000");

        rdbmsDataService.saveQuery();

        browser.click("//input[@value='Next >']");
        browser.waitForPageToLoad("30000");

        rdbmsDataService.addOperation("getAllCustomers","customers-query");

        browser.click("//input[@value='Finish']");
        browser.waitForPageToLoad("30000");

        csvDataService.checkService("RolesDataService");

    }

    public void testAddUTsecurityScenario() throws Exception{
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String serviceName = "RolesDataService";
        String scenarioid = "scenario1";
        instServiceManagement.accessServiceDashboard(serviceName);
        Thread.sleep(1000);
        browser.click("link=Security");
        browser.waitForPageToLoad("30000");

        if ((browser.getSelectedValue("securityConfigAction")).equals("Yes")) {
            browser.select("securityConfigAction", "label=No");
            assertTrue(browser.isTextPresent("This will disable security from the service. Click OK to confirm"));
            browser.click("//button[@type='button']");
            browser.waitForPageToLoad("30000");
            assertTrue(browser.isTextPresent("Security disabled successfully."));
            browser.click("//button[@type='button']");
        }

        assertEquals("Security for the service", browser.getText("//div[@id='middle']/h2"));
        assertTrue(browser.isTextPresent("The service \"" + serviceName + "\" is not secured."));
        browser.select("securityConfigAction", "label=Yes");
        assertEquals("Basic Scenarios", browser.getTable("//form[@id='secConfigForm']/table.0.0"));
        assertEquals("Advanced Scenarios", browser.getTable("//form[@id='secConfigForm']/table.6.0"));
        Thread.sleep(1000);
        browser.click("//input[@name='scenarioId' and @value='" + scenarioid + "']");
        browser.click("//input[@value='Next >']");
        browser.waitForPageToLoad("30000");

        browser.click("//input[@name='userGroups' and @value='Role1']");
        browser.click("//input[@name='userGroups' and @value='Role2']");

        browser.click("//input[@value='Finish']");
        browser.waitForPageToLoad("30000");

    }

    public void testRole1() throws Exception{


        String serviceName = "RolesDataService";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllCustomers";
        String operation = "getAllCustomers";
        String csvExpectedResult = "Charitha";

        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);


        OMElement result = new DSSecurityClient().runSecurityClient("scenario1", serviceName, nameSpace, soapAction, operation,"s","user1");
        System.out.println(result);

        String output=result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "name"));
        assertEquals(csvExpectedResult, output);

        if(result.toString().contains("country")){
            assertTrue("Error in roles",false);
        }


    }

    public void testRole2() throws Exception{


        String serviceName = "RolesDataService";
        String nameSpace = "http://ws.wso2.org/dataservice";
        String soapAction = "urn:getAllCustomers";
        String operation = "getAllCustomers";
        String csvExpectedResult = "Sri lanka";

        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);

        OMElement result = new DSSecurityClient().runSecurityClient("scenario1", serviceName, nameSpace, soapAction, operation,"s","user2");
        System.out.println(result);

        String output=result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "country"));
        assertEquals(csvExpectedResult, output);

        if(result.toString().contains("name")){
            assertTrue("Error in roles",false);
        }

    }


    public void testDeleteUsersFromRolesDataService() throws Exception {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
        mySeleniumTestBase.logOutUI();
        mySeleniumTestBase.loginToUI(username, password);
        mySeleniumTestBase.deleteUser("user1");
        mySeleniumTestBase.deleteUser("user2");

    }

    public void testDeleteRolesFromRolesDataService() throws Exception {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(browser);
        mySeleniumTestBase.logOutUI();
        mySeleniumTestBase.loginToUI(username, password);
        mySeleniumTestBase.deleteRole("Role1");
        mySeleniumTestBase.deleteRole("Role2");


    }

    public void testRemoveRolesDataService() throws Exception {
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(browser);
        RDBMSDataService rdbmsDataService = new RDBMSDataService(browser);

        instServiceManagement.Login();
        seleniumTestBase.deleteService("RolesDataService");
        rdbmsDataService.cleanDataBase();

    }


    public void testLogoutRolesDataService() throws Exception {
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(browser);
        seleniumTestBase.logOutUI();
    }



}
