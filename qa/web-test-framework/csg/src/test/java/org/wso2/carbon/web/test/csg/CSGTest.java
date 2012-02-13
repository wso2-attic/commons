package org.wso2.carbon.web.test.csg;

/**
 * Created by IntelliJ IDEA.
 * User: chamara
 * Date: Nov 5, 2009
 * Time: 2:38:25 PM
 * To change this template use File | Settings | File Templates.
 */

import com.thoughtworks.selenium.*;
import junit.framework.TestCase;
import org.wso2.carbon.web.test.common.SeleniumTestBase;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.File;
import java.util.Properties;

public class CSGTest extends TestCase{
     Selenium browser;
     String username,password ;
    public  CSGTest(String s){
        super(s);
    }

    public void setUp() throws Exception {
        browser = BrowserInitializer.getBrowser();
    }
      public void testLogin() throws Exception                 //Add new system user and login to wsas console
    {
        SetupClientService instSetupClientService = new SetupClientService(browser);
        instSetupClientService.Login();
    }
    public void testAddServer() throws Exception                 //Add new system user and login to wsas console
    {
        SetupClientService instSetupClientService = new SetupClientService(browser);
        instSetupClientService.AddServer("75.101.131.185","admin","admin","443");
    }

  /*  public void testUploadService()throws Exception
    {
        ServiceUpload instServiceUpload = new ServiceUpload(browser);
        instServiceUpload.testUploadAxis1Service();
        instServiceUpload.testUploadAxis2Service();
        instServiceUpload.testUploadPojoService();
        instServiceUpload.testUploadJarService();
        instServiceUpload.testUploadSpringService();
    }*/

      public void testPublishService() throws Exception                 //Add new system user and login to wsas console
    {
        SetupClientService instSetupClientService = new SetupClientService(browser);
        instSetupClientService.PublishService("HelloService","Instance1");
        instSetupClientService.PublishService("Axis1Service","Instance1");
        instSetupClientService.PublishService("Axis2Service","Instance1");
        instSetupClientService.PublishService("JarService2","Instance1");
        instSetupClientService.PublishService("PojoService","Instance1");
        instSetupClientService.PublishService("SpringBean","Instance1");
    }
       public void testHelloServiceCall() throws Exception                 //Add new system user and login to wsas console
    {
        ServiceCall instServiceCall = new ServiceCall();
        String serviceepr = "http://75.101.131.185:8280/services/HelloService_admin";//"http://" + loadProperties().getProperty("host.name") + ":" + loadProperties().getProperty("http.port") + loadProperties().getProperty("context.root") + "/services/" + "Axis1Service";
       String a =  instServiceCall.ServiceClient(serviceepr,"urn:greet","http://www.wso2.org/types","greet");
        System.out.println(a.toString());
    }
       public void testAxis1ServiceCall() throws Exception                 //Add new system user and login to wsas console
    {
        ServiceCall instServiceCall = new ServiceCall();
        String serviceepr = "http://75.101.131.185:8280/services/Axis1Service_admin";//"http://" + loadProperties().getProperty("host.name") + ":" + loadProperties().getProperty("http.port") + loadProperties().getProperty("context.root") + "/services/" + "Axis1Service";
       String a =  instServiceCall.ServiceClient(serviceepr,"","http://ws.apache.org/axis","echoString");
        System.out.println(a.toString());
    }
       public void testRemoveService() throws Exception                 //Add new system user and login to wsas console
    {
        SetupClientService instSetupClientService = new SetupClientService(browser);
        instSetupClientService.RemoveInst("Instance1");
    }
       public void testLogout() throws Exception                 //Add new system user and login to wsas console
    {
        SetupClientService instSetupClientService = new SetupClientService(browser);
        instSetupClientService.logOut();
    }


}
