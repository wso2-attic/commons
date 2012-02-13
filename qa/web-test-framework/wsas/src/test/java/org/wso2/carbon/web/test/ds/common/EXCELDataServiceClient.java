package org.wso2.carbon.web.test.ds.common;

import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.client.Options;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMNamespace;

import javax.xml.namespace.QName;
import java.util.Properties;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: jayani
 * Date: Aug 27, 2009
 * Time: 9:11:18 AM
 * To change this template use File | Settings | File Templates.
 */
public class EXCELDataServiceClient {


    public String EXCELClient(String epr, String operationName, String SoapAction, String NameSpace) throws Exception {

        ServiceClient sc = new ServiceClient();
        Options opts = new Options();
        opts.setTo(new EndpointReference(epr));
        opts.setAction(SoapAction);
        sc.setOptions(opts);
        OMElement result = sc.sendReceive(CreatePayload(operationName, NameSpace));
        System.out.println(result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "name")));
        return result.getFirstElement().getAttributeValue(new QName("http://ws.wso2.org/dataservice", "name"));


    }

    public OMElement CreatePayload(String operationName, String Namespace) throws Exception {

        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omns = fac.createOMNamespace(Namespace, "b");
        OMElement OP1 = fac.createOMElement(operationName, omns);

        return OP1;
    }
}

