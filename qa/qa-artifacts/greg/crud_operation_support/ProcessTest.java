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

public class ProcessTest {

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
            options.setTo(new EndpointReference("https://localhost:9448/standalone/mysql/services/Process"));
//            options.setTo(new EndpointReference("https://localhost:9443/services/API"));
            options.setAction("urn:addProcess");
            options.setManageSession(true);
            OMElement omElement = client.sendReceive(AXIOMUtil.stringToOM("<ser:addProcess xmlns:ser=\"http://services.addProcess.governance.carbon.wso2.org\"><ser:info><metadata xmlns=\"http://www.wso2.org/governance/metadata\"><documentation><documentType>Andromeda</documentType><documentComment>    Abbreviation: And&#xd;\n" +
                    "    Genitive: Andromedae&#xd;\n" +
                    "    Translation: Princess of Ethiopia or the Chained Lady&#xd;\n" +
                    "    Sky Chart&#xd;\n" +
                    "    Peoria Astronomical Society Andromeda Page&#xd;\n" +
                    "    Interactive star chart (Java applet)</documentComment><documentType2>Centaurus</documentType2><url2>http://www.astro.wisc.edu/~dolan/constellations/constellations/Centaurus.html</url2><url>http://www.astro.wisc.edu/~dolan/constellations/constellations/Andromeda.html</url><url1>http://www.astro.wisc.edu/~dolan/constellations/constellations/Sculptor.html</url1><documentComment2>    Abbreviation: Cen&#xd;\n" +
                    "    Genitive: Centauri&#xd;\n" +
                    "    Translation: The Centaur&#xd;\n" +
                    "    Peoria Astronomical Society Centaurus Page&#xd;\n" +
                    "    Interactive star chart (Java applet)</documentComment2><documentType1>Sculptor</documentType1><documentComment1>    Abbreviation: Scl&#xd;\n" +
                    "    Genitive: Sculptoris&#xd;\n" +
                    "    Translation: The Sculptor's Workshop&#xd;\n" +
                    "    Peoria Astronomical Society Sculptor Page&#xd;\n" +
                    "    Interactive star chart (Java applet)</documentComment1></documentation><details><id>Constellations_A</id><executability>false</executability><name>Constellations</name></details></metadata></ser:info></ser:addProcess>"));
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
        options.setTo(new EndpointReference("https://localhost:9448/standalone/mysql/services/Process"));
//        options.setAction("urn:updateURI");
        options.setManageSession(true);

        if(result) {
            /*
            Updating governance artifact
             */
            options.setAction("urn:updateProcess");
            OMElement updateElement = client.sendReceive(AXIOMUtil.stringToOM("<ser:updateProcess xmlns:ser=\"http://services.updateProcess.governance.carbon.wso2.org\"><ser:updatedInfo><metadata xmlns=\"http://www.wso2.org/governance/metadata\"><documentation><documentType>Leo</documentType><documentComment>Abbreviation: Leo&#xd;\n" +
                    "Genitive: Leonis&#xd;\n" +
                    "Translation: The Lion</documentComment><documentType2>Puppis</documentType2><url2>http://www.astro.wisc.edu/~dolan/constellations/constellations/Puppis.html</url2><url>http://www.astro.wisc.edu/~dolan/constellations/constellations/Leo.html</url><url1>http://www.astro.wisc.edu/~dolan/constellations/constellations/Ursa_Major.html</url1><documentComment2>Abbreviation: Pup&#xd;\n" +
                    "Genitive: Puppis&#xd;\n" +
                    "Translation: The Stern</documentComment2><documentType1>Ursa_Major</documentType1><documentComment1>Abbreviation: UMa&#xd;\n" +
                    "Genitive: Ursae Majoris&#xd;\n" +
                    "Translation: The Greater Bear</documentComment1></documentation><details><id>Constellations_A</id><executability>true</executability><name>Constellations</name></details></metadata></ser:updatedInfo></ser:updateProcess>"));
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
        options.setTo(new EndpointReference("https://localhost:9448/standalone/mysql/services/Process"));
//        options.setAction("urn:addAPI");
        options.setManageSession(true);

        if(result) {
            /*
            Getting governance artifact details
             */
            options.setAction("urn:getProcess");
            OMElement element = client.sendReceive(AXIOMUtil.stringToOM("<ser:getProcess xmlns:ser=\"http://services.getProcess.governance.carbon.wso2.org\"><ser:artifactId>" +  "bb3f24a8-796f-4d33-b999-3e55a0320ca1"  + "</ser:artifactId></ser:getProcess>"));
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
        options.setTo(new EndpointReference("https://localhost:9448/standalone/mysql/services/Process"));
//        options.setAction("urn:addAPI");
        options.setManageSession(true);

        if(result) {
            /*
            Deleting governance artifact
             */
            options.setAction("urn:deleteProcess");
            client.setOptions(options);

            OMElement deleteElement = client.sendReceive(AXIOMUtil.stringToOM("<ser:deleteProcess xmlns:ser=\"http://services.deleteProcess.governance.carbon.wso2.org\"><ser:artifactId>" + "bb3f24a8-796f-4d33-b999-3e55a0320ca1" + "</ser:artifactId></ser:deleteProcess>"));
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
