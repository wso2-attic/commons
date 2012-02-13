package org.wso2.carbon.proxyservices;

import org.apache.axis2.transport.http.HTTPConstants;
import org.junit.Assert;
import org.junit.Test;
import org.wso2.carbon.proxyadmin.ui.ProxyServiceAdminStub;
import org.wso2.carbon.proxyadmin.ui.types.carbon.ProxyData;
import org.wso2.carbon.test.framework.ComponentsTestCase;


/**
 * Created by IntelliJ IDEA.
 * User: chamara
 * Date: Jun 21, 2010
 * Time: 10:48:10 AM
 * To change this template use File | Settings | File Templates.
 */

public class PassThroughServiceTest extends ComponentsTestCase {

    ProxyServiceAdminStub proxyStub;
    String serviceConfiguration = "<endpoint xmlns=\"http://ws.apache.org/ns/synapse\" name=\"endpoint_urn_uuid_AF28ECF69FFFC0CF64756329237204901427254053\"><address uri=\"http://localhost:8280/services/echo\" /></endpoint>";
    ReadPropertyFile readProperty = new ReadPropertyFile();


    // Test Pass Through Proxy Service

    @Test
    public void testSimpleService() throws Exception {

        readProperty.getProperty();
        proxyStub = new ProxyServiceAdminStub("https://" + readProperty.HOST_NAME + ":" + readProperty.HTTPS_PORT + "/services/ProxyServiceAdmin");
        proxyStub._getServiceClient().getOptions().setManageSession(true);
        proxyStub._getServiceClient().getOptions().setTimeOutInMilliSeconds(60000000);
        proxyStub._getServiceClient().getOptions().setProperty(HTTPConstants.COOKIE_STRING, sessionCookie);

        ProxyData proxyData = new ProxyData();
        try {
            proxyData = proxyStub.getProxy("sampleProxyService");
            if (proxyData != null) {
                proxyStub.deleteProxyService("sampleProxyService");
                Thread.sleep(3000);
                proxyData.setName("sampleProxyService");
                proxyData.setEndpointXML(serviceConfiguration);
                proxyStub.addProxy(proxyData);

            }

        }
        catch (Exception e) {
            if (e.toString().equalsIgnoreCase("org.apache.axis2.AxisFault: Unable to get the proxy service definition for : sampleProxyService :: A proxy service named : sampleProxyService does not exist")) {
                proxyData.setName("sampleProxyService");
                proxyData.setEndpointXML(serviceConfiguration);
                proxyStub.addProxy(proxyData);
            } else {
                Assert.fail("Unable to add proxy service");
            }
        }


        InvokeClient callClient = new InvokeClient();
        String result = callClient.invokeClient("http://" + readProperty.HOST_NAME + ":" + readProperty.HTTP_PORT + "/services/sampleProxyService", "urn:echoString", "http://echo.services.core.carbon.wso2.org", "echoString", "in");

        System.out.println(result);
        if (result.endsWith("<ns:echoStringResponse xmlns:ns=\"http://echo.services.core.carbon.wso2.org\"><return>WSO2 QA ...</return></ns:echoStringResponse>")) {
        } else {
            Assert.fail("Invocation failed,Response not matched");
        }

        proxyStub.deleteProxyService("sampleProxyService");

    }
}

