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

public class WADLTest {

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
//        AuthenticationAdminStub stub = new AuthenticationAdminStub(cc, "https://localhost:9443/services/AuthenticationAdmin");

        ServiceClient client = stub._getServiceClient();
        Options options = client.getOptions();
        options.setManageSession(true);

        boolean result = stub.login("admin", "admin", "127.0.0.1");

        if(result) {
            /*
            Adding governance artifact
             */
            options.setTo(new EndpointReference("https://localhost:9448/standalone/mysql/services/WADL"));
//            options.setTo(new EndpointReference("https://localhost:9443/services/API"));
            options.setAction("urn:addWADL");
            options.setManageSession(true);
            OMElement omElement = client.sendReceive(AXIOMUtil.stringToOM("<ser:addWADL xmlns:ser=\"http://services.addWADL.governance.carbon.wso2.org\"><ser:info><metadata xmlns=\"http://www.wso2.org/governance/metadata\"><documentation><documentType>Andromeda</documentType><documentComment>Andromeda</documentComment><documentType2>Centaurus</documentType2><url2>http://www.astro.wisc.edu/~dolan/constellations/constellations/Centaurus.html</url2><url>http://www.astro.wisc.edu/~dolan/constellations/constellations/Andromeda.html</url><url1>http://www.astro.wisc.edu/~dolan/constellations/constellations/Sculptor.html</url1><documentComment2>Centaurus</documentComment2><documentType1>Sculptor</documentType1><documentComment1>Sculptor</documentComment1></documentation><overview><name>GooglePlacesAPI</name><namespace>https://maps.googleapis.com</namespace></overview></metadata></ser:info></ser:addWADL>"));
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
//        AuthenticationAdminStub stub = new AuthenticationAdminStub(cc, "https://localhost:9443/services/AuthenticationAdmin");

        ServiceClient client = stub._getServiceClient();
        Options options = client.getOptions();
        options.setManageSession(true);

        boolean result = stub.login("admin", "admin", "127.0.0.1");
        options.setTo(new EndpointReference("https://localhost:9448/standalone/mysql/services/WADL"));
//        options.setAction("urn:updateURI");
        options.setManageSession(true);

        if(result) {
            /*
            Updating governance artifact
             */
            options.setAction("urn:updateWADL");
            OMElement updateElement = client.sendReceive(AXIOMUtil.stringToOM("<ser:updateWADL xmlns:ser=\"http://services.updateWADL.governance.carbon.wso2.org\"><ser:updatedInfo><metadata xmlns=\"http://www.wso2.org/governance/metadata\"><documentation><documentType>Andromeda</documentType><documentComment>Andromeda</documentComment><documentType2>Centaurus12</documentType2><url2>http://www.astro.wisc.edu/~dolan/constellations/constellations/Centaurus.html</url2><url>http://www.astro.wisc.edu/~dolan/constellations/constellations/Andromeda.html</url><url1>http://www.astro.wisc.edu/~dolan/constellations/constellations/Sculptor.html</url1><documentComment2>Centaurus12</documentComment2><documentType1>Sculptor23</documentType1><documentComment1>Sculptor23</documentComment1></documentation><overview><name>GooglePlacesAPI</name><namespace>https://maps.googleapis.com</namespace></overview></metadata></ser:updatedInfo></ser:updateWADL>"));
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

        AuthenticationAdminStub stub = new AuthenticationAdminStub(cc, "https://localhost:9448/standalone/mysql/services/AuthenticationAdmin");
//        AuthenticationAdminStub stub = new AuthenticationAdminStub(cc, "https://localhost:9443/services/AuthenticationAdmin");

        ServiceClient client = stub._getServiceClient();
        Options options = client.getOptions();
        options.setManageSession(true);

        boolean result = stub.login("admin", "admin", "127.0.0.1");
        options.setTo(new EndpointReference("https://localhost:9448/standalone/mysql/services/WADL"));
//        options.setAction("urn:addAPI");
        options.setManageSession(true);

        if(result) {
            /*
            Getting governance artifact details
             */
            options.setAction("urn:getWADL");
            OMElement element = client.sendReceive(AXIOMUtil.stringToOM("<ser:getWADL xmlns:ser=\"http://services.getWADL.governance.carbon.wso2.org\"><ser:artifactId>" +  "282fdc71-789f-4f51-bd99-e3bfe51370ff"  + "</ser:artifactId></ser:getWADL>"));
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
//        AuthenticationAdminStub stub = new AuthenticationAdminStub(cc, "https://localhost:9443/services/AuthenticationAdmin");

        ServiceClient client = stub._getServiceClient();
        Options options = client.getOptions();
        options.setManageSession(true);

        boolean result = stub.login("admin", "admin", "127.0.0.1");
        options.setTo(new EndpointReference("https://localhost:9448/standalone/mysql/services/WADL"));
//        options.setAction("urn:addAPI");
        options.setManageSession(true);

        if(result) {
            /*
            Deleting governance artifact
             */
            options.setAction("urn:deleteWADL");
            client.setOptions(options);

            OMElement deleteElement = client.sendReceive(AXIOMUtil.stringToOM("<ser:deleteWADL xmlns:ser=\"http://services.deleteWADL.governance.carbon.wso2.org\"><ser:artifactId>" + "282fdc71-789f-4f51-bd99-e3bfe51370ff" + "</ser:artifactId></ser:deleteWADL>"));
            System.out.println(deleteElement);
        }else {
        System.out.println("----------------------------------------Bye");
    }
    System.exit(0);

    }

    public static void main(String[] args) throws RemoteException, XMLStreamException, MalformedURLException, LoginAuthenticationExceptionException, JaxenException {
//        addElement();
//        updateElement();
//        getElement();
        deleteElement();
    }

}
