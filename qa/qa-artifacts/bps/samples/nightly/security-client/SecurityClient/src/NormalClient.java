//import org.apache.neethi.Policy;
// import org.apache.neethi.PolicyEngine;
//import org.apache.axiom.om.impl.builder.StAXOMBuilder;
//import org.apache.axiom.om.impl.llom.util.AXIOMUtil;
//import org.apache.axiom.om.OMElement;
//import org.apache.rampart.policy.model.RampartConfig;
//import org.apache.rampart.policy.model.CryptoConfig;
//import org.apache.rampart.RampartMessageData;
//import org.apache.axis2.client.ServiceClient;
//import org.apache.axis2.client.Options;
//import org.apache.axis2.addressing.EndpointReference;
//import org.apache.axis2.context.ConfigurationContext;
//import org.apache.axis2.context.ConfigurationContextFactory;
//import org.apache.ws.security.WSPasswordCallback;
//import javax.security.auth.callback.Callback;
//import javax.security.auth.callback.UnsupportedCallbackException;
//import javax.security.auth.callback.CallbackHandler;
//import java.io.File;
//import java.io.IOException;
//import java.io.FileInputStream;
//import java.util.Properties;
//
//
//public class NormalClient {
//    public static void main(String[] args) {
//
//        NormalClient normalClient = new NormalClient();
//        OMElement result = null;
//          try {
//                result = normalClient.runNormalClient();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            System.out.println(result.toString());
//        }
//
//    public OMElement runNormalClient() throws Exception {
//
//        Properties properties = new Properties();
//        FileInputStream freader=new FileInputStream("."+File.separator+"src"+File.separator+"client.properties");
//        properties.load(freader);
//        String client_repo  = properties.getProperty("client_repo");
//        String endpoint_http   = properties.getProperty("endpoint_http");
//
//        OMElement result = null;
//
//        String SoapAction = "square";
////        String body ="<p:hello xmlns:p=\"http://ode/bpel/unit-test.wsdl\"> <TestPart>Wso2</TestPart> </p:hello>";
//        String body ="<p:square xmlns:p=\"http://ws.apache.org/axis2\">  \n" +
//                "<args0 xmlns=\"http://ws.apache.org/axis2\">5</args0>  \n" +
//                "</p:square>";
//        ConfigurationContext ctx = ConfigurationContextFactory.createConfigurationContextFromFileSystem(client_repo, null);
//        ServiceClient sc = new ServiceClient(ctx, null);
//        sc.engageModule("rampart");
//        sc.engageModule("addressing");
//        Options opts = new Options();
//        opts.setTo(new EndpointReference(endpoint_http));
//        opts.setAction(SoapAction);
//        //For rest post uncommnet following
//        opts.setProperty(org.apache.axis2.Constants.Configuration.ENABLE_REST, Boolean.TRUE);
//        //For rest get uncomment the following line
////         opts.setProperty(org.apache.axis2.Constants.Configuration.HTTP_METHOD, org.apache.axis2.Constants.Configuration.HTTP_METHOD_GET);
//        sc.setOptions(opts);
//        result = sc.sendReceive(AXIOMUtil.stringToOM(body));
//        System.out.println(result.getFirstElement().getText());
//        return result;
//    }
//}
