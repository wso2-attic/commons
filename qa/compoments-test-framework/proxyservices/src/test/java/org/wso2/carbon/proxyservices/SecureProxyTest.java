package org.wso2.carbon.proxyservices;

import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.rampart.RampartMessageData;
import org.junit.Assert;
import org.junit.Test;
import org.wso2.carbon.proxyadmin.ui.ProxyServiceAdminStub;
import org.wso2.carbon.proxyadmin.ui.types.carbon.ProxyData;
import org.wso2.carbon.proxyadmin.ui.types.carbon.ProxyServicePolicyInfo;
import org.wso2.carbon.test.framework.ComponentsTestCase;

import javax.swing.text.html.Option;

/**
 * Created by IntelliJ IDEA.
 * User: chamara
 * Date: Jun 27, 2010
 * Time: 1:33:58 PM
 * To change this template use File | Settings | File Templates.
 */

//ToDo Need to create proxy service with security scenario -  issue - how to engage security scenario in to add proxy service?
public class SecureProxyTest extends ComponentsTestCase {


    @Test
    public void testCheckSecurity() throws Exception {
        ProxyServiceAdminStub proxyStub;
        String serviceConfiguration = "<endpoint xmlns=\"http://ws.apache.org/ns/synapse\" name=\"endpoint_urn_uuid_AF28ECF69FFFC0CF64756329237204901427254053\"><address uri=\"http://localhost:8280/services/echo\" /></endpoint>";
        ReadPropertyFile readProperty = new ReadPropertyFile();
        readProperty.getProperty();

        proxyStub = new ProxyServiceAdminStub("https://" + readProperty.HOST_NAME + ":" + readProperty.HTTPS_PORT + "/services/ProxyServiceAdmin");
        proxyStub._getServiceClient().getOptions().setManageSession(true);
        proxyStub._getServiceClient().getOptions().setTimeOutInMilliSeconds(60000000);
        proxyStub._getServiceClient().getOptions().setProperty(HTTPConstants.COOKIE_STRING, sessionCookie);

        ProxyData proxyData = new ProxyData();
        try {
            proxyData = proxyStub.getProxy("sampleProxyService");
            ProxyServicePolicyInfo[] ss = proxyData.getPolicies();
            System.out.println(ss.toString());

            if (proxyData != null)

            {


                proxyStub.deleteProxyService("sampleProxyService");
                Thread.sleep(3000);
                proxyData.setName("sampleProxyService");
                proxyData.setEndpointXML(serviceConfiguration);
                proxyData.setEnableSecurity(true);

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


    }
}
