package org.wso2.carbon.common.test.utils.client;

import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.Constants;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.ByteArrayInputStream;

/**
 * JSONClient which calls to the JSONService
 */
public class JSONClient {

    public static final String PARAM_HELP = "-help";
    public static final String PARAM_CT = "-ct";
    public static final String CT_AJ = "aj";
    public static final String CT_AJB = "ajb";

    public static final String APPLICATION_JSON = "application/json";
    public static final String APPLICATION_JSON_BADGERFISH = "application/json/badgerfish";

    public static final String ECHO_STRING = "Hello JSON Service";
    private static final Log log = LogFactory.getLog(JSONClient.class);

    public static void main(String[] args) {

        // check whether the user is asking for help
        for (String arg : args) {
            if (PARAM_HELP.equalsIgnoreCase(arg)) {
                printUsage();
                System.exit(0);
            }
        }

        String contentType = APPLICATION_JSON;
        // ideally, number of args should be 2. if it is 0, we consider
        // "application/json" as the default
        if (args.length == 2) {
            String ct = args[0];
            String option = args[1];
            if (PARAM_CT.equals(ct)) {
                if (CT_AJB.equals(option)) {
                    contentType = APPLICATION_JSON_BADGERFISH;
                } else if (!CT_AJ.equals(option)) {
                    exitDueToInvalidArgs();
                }
            } else {
                exitDueToInvalidArgs();
            }
        } else if (!(args.length == 0)) {
            // if the number of args is not 2 or 0, it's invalid
            exitDueToInvalidArgs();
        }

        try {
            FrameworkSettings.getProperty();
            // We've set port in the EPR to 9763, which is the default port for AppServer.
            // But if you want to see the JSON messages on the wire, chenge this and use TCPMON
            EndpointReference targetEPR = new EndpointReference("http://localhost:9000/services/SimpleStockQuoteService");

            Options options = new Options();
            options.setTo(targetEPR);

            // IMPORTANT : It is a must to properly set the message Type when using JSON
            options.setProperty(Constants.Configuration.MESSAGE_TYPE, contentType);

            File configFile = new File("conf/axis2.xml");
            ConfigurationContext clientConfigurationContext = ConfigurationContextFactory.createConfigurationContextFromFileSystem(FrameworkSettings.CARBON_HOME + "/repository/conf/axis2.xml", null);
            ServiceClient sender = new ServiceClient(clientConfigurationContext, null);
            sender.setOptions(options);
            options.setTo(targetEPR);

            OMElement echoPayload = getEchoPayload(contentType);
            OMElement result = sender.sendReceive(echoPayload);
            if (result != null && echoPayload.toString().equals(result.toString().trim())) {
                System.out.println("\nJSON Service invocation successfull..\n");
            }
        } catch (Exception e) {
            log.error("error occured: " + e.getMessage());
        }

    }

    private static OMElement getEchoPayload(String contentType) throws XMLStreamException {
        String payload = "<echo><value>" + ECHO_STRING + "</value></echo>";

        // If the content type is "application/json/badgerfish", we
        // can have namespaces within our payload
        if (APPLICATION_JSON_BADGERFISH.equals(contentType)) {
            payload = "<echo><ns:value xmlns:ns=\"http://services.wsas.training.wso2.org\">" +
                      ECHO_STRING + "</ns:value></echo>";
        }

        // If you want to send JSON Arrays, use the following payload
        // payload = "<echo><value>Hello1</value><value>Hello2</value><value>Hello3</value></echo>";

        // return an OMElement from the payload..
        return new StAXOMBuilder(new ByteArrayInputStream(payload.getBytes())).getDocumentElement();
    }

    private static void printUsage() {
        System.out.println("\n=============== JSON Sample HELP ===============\n");
        System.out.println("Following optional parameters can be used" +
                           " when running the client\n");
        System.out.println(PARAM_CT + " : Content type can be set using this parameter. " +
                           "Valid content types are..");
        System.out.println("        " + CT_AJ +
                           "       - Content type is set as \"application/json\" ");
        System.out.println("        " + CT_AJB +
                           "      - Content type is set as \"application/json/badgerfish\" \n");
    }

    private static void exitDueToInvalidArgs() {
        System.out.println("\n\nInvalid parameters. Use " + PARAM_HELP +
                           " to get valid list of parameters..\n");
        System.exit(0);
    }

}