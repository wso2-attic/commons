import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.axiom.om.xpath.AXIOMXPath;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.jaxen.JaxenException;
import org.wso2.carbon.authenticator.stub.AuthenticationAdminStub;
import org.wso2.carbon.authenticator.stub.LoginAuthenticationExceptionException;

import javax.xml.stream.XMLStreamException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

/***********
************ This test class was provided by Eranda Sooriyabandara***************
************/

public class APITest {

    private static void setSystemProperties(){
        System.setProperty("carbon.home", "/home/evanthika/WSO2/CARBON/CARBON4/GREG/4th-July-2012/standalone/wso2greg-4.5.0-SNAPSHOT");
        String trustStore = "/home/evanthika/WSO2/CARBON/CARBON4/GREG/4th-July-2012/standalone/wso2greg-4.5.0-SNAPSHOT/repository/resources/security/wso2carbon.jks";
        System.setProperty("javax.net.ssl.trustStore", trustStore);
        System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");
        System.setProperty("carbon.repo.write.mode","true");
    }

    public static void addElement() throws RemoteException, XMLStreamException, MalformedURLException, LoginAuthenticationExceptionException, JaxenException{
        setSystemProperties();

        ConfigurationContext cc = ConfigurationContextFactory.createConfigurationContextFromFileSystem("/home/evanthika/WSO2/CARBON/CARBON4/GREG/4th-July-2012/standalone/wso2greg-4.5.0-SNAPSHOT/repository/deployment/client",
                "/home/evanthika/WSO2/CARBON/CARBON4/GREG/4th-July-2012/standalone/wso2greg-4.5.0-SNAPSHOT/repository/conf/axis2/axis2_client.xml");

        AuthenticationAdminStub stub = new AuthenticationAdminStub(cc, "https://localhost:9443/services/AuthenticationAdmin");

        ServiceClient client = stub._getServiceClient();
        Options options = client.getOptions();
        options.setManageSession(true);

        boolean result = stub.login("admin", "admin", "127.0.0.1");

        if(result) {
            /*
            Adding governance artifact
             */
            options.setTo(new EndpointReference("https://10.200.3.172:9443/services/API"));
            options.setAction("urn:addAPI");
            options.setManageSession(true);
            OMElement omElement = client.sendReceive(AXIOMUtil.stringToOM("<ser:addAPI xmlns:ser=\"http://services.addAPI.governance.carbon.wso2.org\"><ser:info><metadata xmlns=\"http://www.wso2.org/governance/metadata\"><overview><status>CREATED</status><context>/galaxy2</context><name>CRUDOperation111</name><version>0.0.3</version><tier>Gold</tier><isLatest>false</isLatest><provider>CRUDOperation111</provider></overview></metadata></ser:info></ser:addAPI>"));
            System.out.println(omElement);

            AXIOMXPath expression = new AXIOMXPath("//return");
            expression.addNamespace("ns" ,omElement.getNamespace().getNamespaceURI());
            String artifactId = ((OMElement) expression.selectSingleNode(omElement)).getText();
            System.out.println(artifactId);
        } else {
            System.out.println("----------------------------------------Bye");
        }
        System.exit(0);
    }


    public static void updateElement() throws RemoteException, XMLStreamException, MalformedURLException, LoginAuthenticationExceptionException, JaxenException {
        setSystemProperties();

        ConfigurationContext cc = ConfigurationContextFactory.createConfigurationContextFromFileSystem("/home/evanthika/WSO2/CARBON/CARBON4/GREG/4th-July-2012/standalone/wso2greg-4.5.0-SNAPSHOT/repository/deployment/client",
                "/home/evanthika/WSO2/CARBON/CARBON4/GREG/4th-July-2012/standalone/wso2greg-4.5.0-SNAPSHOT/repository/conf/axis2/axis2_client.xml");

        AuthenticationAdminStub stub = new AuthenticationAdminStub(cc, "https://localhost:9443/services/AuthenticationAdmin");

        ServiceClient client = stub._getServiceClient();
        Options options = client.getOptions();
        options.setManageSession(true);

        boolean result = stub.login("admin", "admin", "127.0.0.1");
        options.setTo(new EndpointReference("https://10.200.3.172:9443/services/API"));
        options.setAction("urn:addAPI");
        options.setManageSession(true);

        if(result) {
            /*
            Updating governance artifact
             */
            options.setAction("urn:updateAPI");
            OMElement updateElement = client.sendReceive(AXIOMUtil.stringToOM("<ser:updateAPI xmlns:ser=\"http://services.updateAPI.governance.carbon.wso2.org\"><ser:updatedInfo><metadata xmlns=\"http://www.wso2.org/governance/metadata\"><overview><status>PUBLISHED</status><context>/crudoperation</context><name>CRUDOperation111</name><version>0.0.3</version><tier>Other</tier><isLatest>true</isLatest><provider>CRUDOperation111</provider></overview></metadata></ser:updatedInfo></ser:updateAPI>"));
            System.out.println(updateElement);

            //        ServiceClient client = new ServiceClient();
            //        Options options = new Options();
            //        options.setTo(new EndpointReference("http://localhost:9763/services/URI"));

        } else {
            System.out.println("----------------------------------------Bye");
        }
        System.exit(0);

    }

    public static void getElement() throws RemoteException, XMLStreamException, MalformedURLException, LoginAuthenticationExceptionException, JaxenException {
        setSystemProperties();

        ConfigurationContext cc = ConfigurationContextFactory.createConfigurationContextFromFileSystem("/home/evanthika/WSO2/CARBON/CARBON4/GREG/4th-July-2012/standalone/wso2greg-4.5.0-SNAPSHOT/repository/deployment/client",
                "/home/evanthika/WSO2/CARBON/CARBON4/GREG/4th-July-2012/standalone/wso2greg-4.5.0-SNAPSHOT/repository/conf/axis2/axis2_client.xml");

        AuthenticationAdminStub stub = new AuthenticationAdminStub(cc, "https://localhost:9443/services/AuthenticationAdmin");

        ServiceClient client = stub._getServiceClient();
        Options options = client.getOptions();
        options.setManageSession(true);

        boolean result = stub.login("admin", "admin", "127.0.0.1");
        options.setTo(new EndpointReference("https://10.200.3.172:9443/services/API"));
        options.setAction("urn:addAPI");
        options.setManageSession(true);

        if(result) {
            /*
            Getting governance artifact details
             */
            options.setAction("urn:getAPI");
            OMElement element = client.sendReceive(AXIOMUtil.stringToOM("<ser:getAPI xmlns:ser=\"http://services.getAPI.governance.carbon.wso2.org\"><ser:artifactId>" +  "3985dd17-6aba-45b7-ab0b-eb9d2d19fba0"  + "</ser:artifactId></ser:getAPI>"));
            System.out.println("Artifact ID is : "+ element.toString());
        } else {
            System.out.println("----------------------------------------Bye");
        }
        System.exit(0);
    }

    public static void deleteElement() throws RemoteException, XMLStreamException, MalformedURLException, LoginAuthenticationExceptionException, JaxenException{
        setSystemProperties();

        ConfigurationContext cc = ConfigurationContextFactory.createConfigurationContextFromFileSystem("/home/evanthika/WSO2/CARBON/CARBON4/GREG/4th-July-2012/standalone/wso2greg-4.5.0-SNAPSHOT/repository/deployment/client",
                "/home/evanthika/WSO2/CARBON/CARBON4/GREG/4th-July-2012/standalone/wso2greg-4.5.0-SNAPSHOT/repository/conf/axis2/axis2_client.xml");

        AuthenticationAdminStub stub = new AuthenticationAdminStub(cc, "https://localhost:9443/services/AuthenticationAdmin");

        ServiceClient client = stub._getServiceClient();
        Options options = client.getOptions();
        options.setManageSession(true);

        boolean result = stub.login("admin", "admin", "127.0.0.1");
        options.setTo(new EndpointReference("https://10.200.3.172:9443/services/API"));
        options.setAction("urn:addAPI");
        options.setManageSession(true);

        if(result) {
            /*
            Deleting governance artifact
             */
            options.setAction("urn:deleteAPI");
            client.setOptions(options);

            OMElement deleteElement = client.sendReceive(AXIOMUtil.stringToOM("<ser:deleteAPI xmlns:ser=\"http://services.deleteAPI.governance.carbon.wso2.org\"><ser:artifactId>" + "f880f4ef-95d2-4f11-8746-af825582cdb9" + "</ser:artifactId></ser:deleteAPI>"));
            System.out.println(deleteElement);
        }else {
        System.out.println("----------------------------------------Bye");
    }
    System.exit(0);

    }

    public static void main(String[] args) throws RemoteException, XMLStreamException, MalformedURLException, LoginAuthenticationExceptionException, JaxenException {
//        addElement();
//        updateElement();
        getElement();
//        deleteElement();
    }

}

