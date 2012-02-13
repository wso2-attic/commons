/* Sample client to test Soap session */

package org.wso2.wsas.client;

import org.apache.axis2.client.Options;
import org.wso2.wsas.service.SoapSessionServiceStub;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;

public class SoapSessionClient{

public static void main(String args[])throws Exception{

ConfigurationContext ctx = ConfigurationContextFactory.createConfigurationContextFromFileSystem(args[0], null);

SoapSessionServiceStub stub = new SoapSessionServiceStub(ctx);
SoapSessionServiceStub.Multiply req  = new SoapSessionServiceStub.Multiply();
req.setJ(4);
req.setK(5);
Options opts = stub._getServiceClient().getOptions();
opts.setManageSession(true);
stub._getServiceClient().engageModule("addressing");
stub._getServiceClient().setOptions(opts);

for (int i = 0; i < 5; i++) {
SoapSessionServiceStub.MultiplyResponse response = stub.multiply(req);
System.out.println(response.get_return());
}


}
}
