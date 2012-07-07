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

public class URITest {

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

        AuthenticationAdminStub stub = new AuthenticationAdminStub(cc, "https://localhost:9448/standalone/mysql/services/AuthenticationAdmin");
        ServiceClient client = stub._getServiceClient();
        Options options = client.getOptions();
        options.setManageSession(true);

        boolean result = stub.login("admin", "admin", "127.0.0.1");

        if(result) {
            /*
            Adding governance artifact
             */
            options.setTo(new EndpointReference("https://localhost:9448/standalone/mysql/services/URI"));

            options.setAction("urn:addURI");
            options.setManageSession(true);
            OMElement omElement = client.sendReceive(AXIOMUtil.stringToOM("<ser:addURI xmlns:ser=\"http://services.addURI.governance.carbon.wso2.org\"><ser:info><metadata xmlns=\"http://www.wso2.org/governance/metadata\"><overview><type>WSDL</type><name>SimpleStockQuoteService.wsdl</name><uri>http://people.wso2.com/~evanthika/wsdls/SimpleStockQuoteService.wsdl</uri></overview></metadata></ser:info></ser:addURI>"));
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

        AuthenticationAdminStub stub = new AuthenticationAdminStub(cc, "https://localhost:9448/standalone/mysql/services/AuthenticationAdmin");
        ServiceClient client = stub._getServiceClient();
        Options options = client.getOptions();
        options.setManageSession(true);

        boolean result = stub.login("admin", "admin", "127.0.0.1");
        options.setTo(new EndpointReference("https://localhost:9448/standalone/mysql/services/URI"));
        options.setManageSession(true);

        if(result) {
            /*
            Updating governance artifact
             */
            options.setAction("urn:updateURI");
            OMElement updateElement = client.sendReceive(AXIOMUtil.stringToOM("<ser:updateURI xmlns:ser=\"http://services.updateURI.governance.carbon.wso2.org\"><ser:updatedInfo><metadata xmlns=\"http://www.wso2.org/governance/metadata\"><overview><type>WSDL</type><name>SimpleStockQuoteServiceNewName.wsdl</name><uri>http://people.wso2.com/~evanthika/wsdls/SimpleStockQuoteService.wsdl</uri></overview></metadata></ser:updatedInfo></ser:updateURI>"));
            System.out.println(updateElement);

        } else {
            System.out.println("----------------------------------------Bye");
        }
        System.exit(0);

    }

    public static void getElement() throws RemoteException, XMLStreamException, MalformedURLException, LoginAuthenticationExceptionException, JaxenException {
        setSystemProperties();

        ConfigurationContext cc = ConfigurationContextFactory.createConfigurationContextFromFileSystem("/home/evanthika/WSO2/CARBON/CARBON4/GREG/4th-July-2012/standalone/wso2greg-4.5.0-SNAPSHOT/repository/deployment/client",
                "/home/evanthika/WSO2/CARBON/CARBON4/GREG/4th-July-2012/standalone/wso2greg-4.5.0-SNAPSHOT/repository/conf/axis2/axis2_client.xml");

        AuthenticationAdminStub stub = new AuthenticationAdminStub(cc, "https://localhost:9448/standalone/mysql/services/AuthenticationAdmin");
        ServiceClient client = stub._getServiceClient();
        Options options = client.getOptions();
        options.setManageSession(true);

        boolean result = stub.login("admin", "admin", "127.0.0.1");
        options.setTo(new EndpointReference("https://localhost:9448/standalone/mysql/services/URI"));

        options.setManageSession(true);

        if(result) {
            /*
            Getting governance artifact details
             */
            options.setAction("urn:getURI");
            OMElement element = client.sendReceive(AXIOMUtil.stringToOM("<ser:getURI xmlns:ser=\"http://services.getURI.governance.carbon.wso2.org\"><ser:artifactId>" +  "2e6731d9-7723-4883-b2ca-599bee4bb94a"  + "</ser:artifactId></ser:getURI>"));
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

        AuthenticationAdminStub stub = new AuthenticationAdminStub(cc, "https://localhost:9448/standalone/mysql/services/AuthenticationAdmin");

        ServiceClient client = stub._getServiceClient();
        Options options = client.getOptions();
        options.setManageSession(true);

        boolean result = stub.login("admin", "admin", "127.0.0.1");
        options.setTo(new EndpointReference("https://localhost:9448/standalone/mysql/services/URI"));

        options.setManageSession(true);

        if(result) {
            /*
            Deleting governance artifact
             */
            options.setAction("urn:deleteURI");
            client.setOptions(options);

            OMElement deleteElement = client.sendReceive(AXIOMUtil.stringToOM("<ser:deleteURI xmlns:ser=\"http://services.deleteURI.governance.carbon.wso2.org\"><ser:artifactId>" + "2e6731d9-7723-4883-b2ca-599bee4bb94a" + "</ser:artifactId></ser:deleteURI>"));
            System.out.println(deleteElement);
        }else {
        System.out.println("----------------------------------------Bye");
    }
    System.exit(0);

    }

    public static void main(String[] args) throws RemoteException, XMLStreamException, MalformedURLException, LoginAuthenticationExceptionException, JaxenException {
        addElement();
        updateElement();
        getElement();
        deleteElement();
    }

}
