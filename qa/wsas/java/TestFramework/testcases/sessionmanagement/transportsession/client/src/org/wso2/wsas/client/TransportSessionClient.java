/* Sample client to test Transport session */

package org.wso2.wsas.client;

import org.apache.axis2.client.Options;
import org.wso2.wsas.service.TransportSessionServiceStub;

public class TransportSessionClient{

public static void main(String args[])throws Exception{

TransportSessionServiceStub stub = new TransportSessionServiceStub();
TransportSessionServiceStub.Multiply req  = new TransportSessionServiceStub.Multiply();
req.setJ(2);
req.setK(2);
Options opts = stub._getServiceClient().getOptions();
opts.setManageSession(true);
stub._getServiceClient().setOptions(opts);

for (int i = 0; i < 5; i++) {
TransportSessionServiceStub.MultiplyResponse response = stub.multiply(req);
System.out.println(response.get_return());
}


}
}
