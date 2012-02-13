package src;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.client.Options;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;

import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.util.Properties;

import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.soap.SOAP12Constants;
import org.apache.axis2.client.async.AxisCallback;
import org.apache.axis2.Constants;
import org.apache.axis2.context.MessageContext;
import org.apache.sandesha2.client.SandeshaClientConstants;

public class RMClient {
    public static void main(String[] args) {
        RMClient rmClient = new RMClient();
        OMElement result = null;

        try{
            Properties properties = new Properties();
            FileInputStream freader=new FileInputStream("."+File.separator+"src"+File.separator+"client.properties");
            properties.load(freader);
            String carbon_home = properties.getProperty("carbon.home");
            String client_repo  = properties.getProperty("client_repo");
            String endpoint_https    = properties.getProperty("endpoint_https");
            String endpoint_http   = properties.getProperty("endpoint_http");
            String client_home = properties.getProperty("client_home");
            String serviceName = properties.getProperty("service_name");
            String namespace = properties.getProperty("namespace");
            String soapAction = properties.getProperty("soap_action");
            String operation = properties.getProperty("operation");
            String parameter = properties.getProperty("parameter");

//            int soap11_response_count = rmClient.RMRequestReplyAnonClient(serviceName, "soap11", soapAction, namespace, operation, parameter);
//            System.out.println("soap12_response_count");
//            int soap12_response_count = rmClient.RMRequestReplyAnonClient(serviceName, "soap12", soapAction, namespace, operation, parameter);
//            System.out.println("Oneway invocation");
//            rmClient.RMOnewayAnonClient(serviceName, "soap11", soapAction, namespace, operation, parameter);
            System.out.println("Dual Channel Twoway invocation");
            int soap11_addressable_response_count = rmClient.RMRequestReplyAddressableClient(serviceName, "soap11", soapAction, namespace, operation, parameter);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private int RMRequestReplyAddressableClient(String serviceName, String soapVersion, String soapAction, String namespace, String operation, String parameter) {
        int count = 0;
        try {
//            ConfigurationContext cc = ConfigurationContextFactory.createConfigurationContextFromFileSystem(loadProperties().getProperty("client_repo"), loadProperties().getProperty("carbon.home") + File.separator + "samples" + File.separator + "axis2Server" + File.separator + "repository" + File.separator + "conf" + File.separator + "axis2.xml");
            ConfigurationContext cc = ConfigurationContextFactory.createConfigurationContextFromFileSystem(loadProperties().getProperty("client_repo"),null);
            ServiceClient sc = new ServiceClient(cc, null);
            AxisCallback Callback = new AxisCallback() {
                public void onMessage(MessageContext msgContext) {
                    System.out.println("Got the message ==> " + msgContext.getEnvelope().getBody().getFirstElement());
                }

                public void onFault(MessageContext msgContext) {
                    System.out.println("Faulty message");
                }

                public void onError(Exception e) {
                    e.printStackTrace();
                    System.out.println("Received an error ...");
                }

                public void onComplete() {
                    System.out.println("Message complete");
                }
            };
            sc.engageModule("sandesha2");
            sc.engageModule("addressing");
            Options opts = new Options();
            opts.setTo(new EndpointReference(loadProperties().getProperty("endpoint_http")));
            opts.setAction(soapAction);
            opts.setUseSeparateListener(true);
            if (soapVersion.equals("soap12")) {
                opts.setSoapVersionURI(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
            }
            sc.setOptions(opts);
            for (int i = 0; i < 10; i++) {
                sc.sendReceiveNonBlocking(CreateRequestReplyPayload(namespace, operation, parameter), Callback);
                count++;
            }
            //Setting the last message
            sc.getOptions().setProperty(SandeshaClientConstants.LAST_MESSAGE, Constants.VALUE_TRUE);
            sc.sendReceiveNonBlocking(CreateRequestReplyPayload(namespace, operation, parameter), Callback);
            cc.getListenerManager().stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count + 1;
    }

    private void RMOnewayAnonClient(String serviceName, String soapVersion, String soapAction, String nameSpace, String operation, String parameter) {
        int count = 0;

        ConfigurationContext cc = null;
        try {
            cc = ConfigurationContextFactory.createConfigurationContextFromFileSystem(loadProperties().getProperty("client_repo"), null);
            ServiceClient sc = new ServiceClient(cc, null);
            sc.engageModule("sandesha2");
            sc.engageModule("addressing");
            Options opts = new Options();
            opts.setTo(new EndpointReference(loadProperties().getProperty("endpoint_http")));
            opts.setAction(soapAction);
            if (soapVersion.equals("soap12")) {
                opts.setSoapVersionURI(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
            }
            sc.setOptions(opts);
            for (int i = 0; i < 10; i++) {
                sc.fireAndForget(CreateOneWayPayload(nameSpace, operation, parameter));
            }
            sc.getOptions().setProperty(SandeshaClientConstants.LAST_MESSAGE, Constants.VALUE_TRUE);
            sc.fireAndForget(CreateOneWayPayload(nameSpace, operation, parameter));
            } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private OMElement CreateOneWayPayload(String nameSpace, String operation, String parameter) {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omns = fac.createOMNamespace(nameSpace, "b");
        OMElement OP1 = fac.createOMElement(operation, omns);
        OMElement value = fac.createOMElement(parameter, null);
        value.addChild(fac.createOMText(value, "ping"));
        OP1.addChild(value);
        return OP1;
    }

    public static Properties loadProperties() throws IOException {
        Properties properties = new Properties();
        FileInputStream freader = new FileInputStream("."+File.separator+"src"+File.separator+"client.properties");
        properties.load(freader);
        freader.close();
        return properties;
    }

        public OMElement CreateRequestReplyPayload(String NameSpace, String operation, String param) throws Exception {

        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omns = fac.createOMNamespace(NameSpace, "b");
        OMElement OP1 = fac.createOMElement(operation, omns);
        OMElement value = fac.createOMElement(param, null);
        value.addChild(fac.createOMText(value, "RM Two way messaging!!!!"));
        OP1.addChild(value);
        return OP1;
    }

    private int RMRequestReplyAnonClient(String serviceName, String soapVersion, String soapAction, String nameSpace, String operation, String parameter) throws Exception {
        int count = 0;
        ConfigurationContext cc = null;
        try {
            cc = ConfigurationContextFactory.createConfigurationContextFromFileSystem(loadProperties().getProperty("client_repo"), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ServiceClient sc = new ServiceClient(cc, null);
        sc.engageModule("sandesha2");
        sc.engageModule("addressing");
        Options opts = new Options();
        try {
            opts.setTo(new EndpointReference(loadProperties().getProperty("endpoint_http")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            opts.setAction(loadProperties().getProperty("soap_action"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (soapVersion.equals("soap12")) {
            opts.setSoapVersionURI(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
        }
        sc.setOptions(opts);
        for (int i = 0; i < 10; i++) {
            OMElement result = sc.sendReceive(CreateRequestReplyPayload(nameSpace, operation, parameter));
            System.out.println(result.getFirstElement().getText());
            count++;
        }
        //Setting the last message
        sc.getOptions().setProperty(SandeshaClientConstants.LAST_MESSAGE, Constants.VALUE_TRUE);


        OMElement result = sc.sendReceive(CreateRequestReplyPayload(nameSpace, operation, parameter));
        System.out.println(result.getFirstElement().getText());
        return count + 1;
    }
}
