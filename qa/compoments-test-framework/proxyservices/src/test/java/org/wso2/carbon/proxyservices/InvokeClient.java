package org.wso2.carbon.proxyservices;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;

/**
 * Created by IntelliJ IDEA.
 * User: chamara
 * Date: Jun 11, 2010
 * Time: 5:44:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class InvokeClient {
    public static String output;

    public String invokeClient(String epr, String soapAction, String namespace, String operationName, String firstChild) throws AxisFault {
        ServiceClient sc = new ServiceClient();
        Options opts = new Options(); // need to create soap option object for assign soap options.
        opts.setTo(new EndpointReference(epr));
        opts.setAction(soapAction); // soap action field copied from the wsdl file
        sc.setOptions(opts);

        OMElement result = sc.sendReceive(CreatePayload(namespace, operationName, firstChild));
        output = result.toString();
        return output;
    }

    public static OMElement CreatePayload(String namespace, String operationName, String firstChild) // this function create a message to send to server
    {
        OMFactory fac = OMAbstractFactory.getOMFactory(); // Create object(fac)from OM Factory this is used to create all the elements.
        OMNamespace omns = fac.createOMNamespace(namespace, "b"); // creating namespace to assign to message, b =  namespace
        OMElement OP1 = fac.createOMElement(operationName, omns); // Operation name
        OMElement value = fac.createOMElement(firstChild, omns);
        value.addChild(fac.createOMText(value, "WSO2 QA ..."));
        OP1.addChild(value);
        return OP1;
    }

}
