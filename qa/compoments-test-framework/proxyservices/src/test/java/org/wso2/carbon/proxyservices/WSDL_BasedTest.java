package org.wso2.carbon.proxyservices;

import org.apache.axis2.AxisFault;
import org.apache.axis2.transport.http.HTTPConstants;
import org.junit.Assert;
import org.junit.Test;
import org.wso2.carbon.proxyadmin.ui.ProxyServiceAdminStub;
import org.wso2.carbon.proxyadmin.ui.types.carbon.ProxyData;
import org.wso2.carbon.test.framework.ComponentsTestCase;


/**
 * Created by IntelliJ IDEA.
 * User: chamara
 * Date: Jun 24, 2010
 * Time: 11:06:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class WSDL_BasedTest extends ComponentsTestCase {
    ProxyServiceAdminStub proxyStub;
    String serviceConfiguration = "<endpoint xmlns=\"http://ws.apache.org/ns/synapse\" name=\"endpoint_urn_uuid_BF3EA29FE89028624C133048718824542-1433661704\"><wsdl service=\"echo\" port=\"echoHttpsEndpoint\" uri=\"http://localhost:8280/services/echo?wsdl\" /></endpoint>";
    ReadPropertyFile readProperty = new ReadPropertyFile();

@Test
    public void testWSDLService()throws Exception
    {

            readProperty.getProperty();
            proxyStub = new ProxyServiceAdminStub("https://"+readProperty.HOST_NAME+":"+readProperty.HTTPS_PORT+"/services/ProxyServiceAdmin");
            proxyStub._getServiceClient().getOptions().setManageSession(true);
            proxyStub._getServiceClient().getOptions().setTimeOutInMilliSeconds(60000000);
            proxyStub._getServiceClient().getOptions().setProperty(HTTPConstants.COOKIE_STRING, sessionCookie);

            ProxyData proxyData = new ProxyData();
            try
            {
                proxyData = proxyStub.getProxy("WSDL_based");
                if(proxyData != null)
                {
                    proxyStub.deleteProxyService("WSDL_based");
                    Thread.sleep(3000);
                    proxyData.setName("WSDL_based");
                    proxyData.setEndpointXML(serviceConfiguration);
                    proxyData.setOutSeqXML("<outSequence xmlns=\"http://ws.apache.org/ns/synapse\"><send /></outSequence>");
                    proxyStub.addProxy(proxyData);

                }

            }
            catch (Exception e)
            {
                if(e.toString().equalsIgnoreCase("org.apache.axis2.AxisFault: Unable to get the proxy service definition for : WSDL_based :: A proxy service named : WSDL_based does not exist"))
                {
                    proxyData.setName("WSDL_based");
                    proxyData.setEndpointXML(serviceConfiguration);
                    proxyData.setOutSeqXML("<outSequence xmlns=\"http://ws.apache.org/ns/synapse\"><send /></outSequence>");
                    proxyStub.addProxy(proxyData);
                }
                else
                {
                    Assert.fail("Unable to add proxy service");
                }
             }

            InvokeClient callClient = new InvokeClient();
            String result = callClient.invokeClient("http://localhost:8280/services/WSDL_based","urn:echoString","http://echo.services.core.carbon.wso2.org","echoString","in");

            System.out.println(result);
            if (result.endsWith("<ns:echoStringResponse xmlns:ns=\"http://echo.services.core.carbon.wso2.org\"><return>WSO2 QA ...</return></ns:echoStringResponse>"))
            {}
            else {Assert.fail("Invocation failed,Response not matched");}
            proxyStub.deleteProxyService("WSDL_based");
    }
}
