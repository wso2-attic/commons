package org.wso2.carbon.web.test.client;

import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.OperationClient;
import org.apache.axis2.Constants;
import org.apache.axis2.AxisFault;
import org.apache.axis2.wsdl.WSDLConstants;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.context.ServiceContext;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axiom.soap.SOAP12Constants;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMNamespace;

import javax.xml.namespace.QName;
import java.util.Iterator;

public class ESBAggregateClient {

      public int aggregateClient(String trpUrl,String epr,int payload_iterate) throws Exception {
        int cCounter = 0;
        try {
            ServiceClient client = new ServiceClient();
            OperationClient operationClient = client.createClient(ServiceClient.ANON_OUT_IN_OP);
            //creating message context
            MessageContext outMsgCtx = new MessageContext();
            //assigning message context’s option object into instance variable
            Options opts = outMsgCtx.getOptions();
            //setting properties into option
             if (trpUrl != null && !"null".equals(trpUrl)) {
             opts.setProperty(Constants.Configuration.TRANSPORT_URL, trpUrl);
             }
            opts.setTo(new EndpointReference(epr));
            opts.setAction("urn:getQuote");

            //Create a SOAPEnvelope and add that to the message context
            outMsgCtx.setEnvelope(createPayload(payload_iterate));

            //Add a message context to operationClient
            operationClient.addMessageContext(outMsgCtx);

            //long startTime = System.currentTimeMillis();
            //call the execute method to send the message
            operationClient.execute(true);

            //pass message label as method argument
            MessageContext inMsgtCtx = operationClient.getMessageContext("In");

            SOAPEnvelope response = inMsgtCtx.getEnvelope();
            //System.out.println(response);
            //long endTime=System.currentTimeMillis();
            //long totalResponseTime=endTime-startTime;
            //System.out.println("totalResponseTime== "+totalResponseTime);            

            String str=response.getBody().toString();
            String search="<ns:getQuoteResponse";
            int index =0;
            for(int searchIndex=0;searchIndex<str.length();) {
                index=str.indexOf(search,searchIndex);
                if( index != -1){
                    cCounter=cCounter+1;
                    searchIndex=index+search.length();
               }
                else
                    break;
             } 
       }
        catch (AxisFault e) {
            e.printStackTrace();
        }
        return cCounter;
    }


    public SOAPEnvelope createPayload(int payload_iterate) throws Exception{
        int count=0;
        OMElement value1[]=new OMElement[100];
        OMElement value2[]=new OMElement[100];

        //OMFactory fac = OMAbstractFactory.getOMFactory();
        SOAPFactory fac = OMAbstractFactory.getSOAP11Factory();
        SOAPEnvelope envelope = fac.getDefaultEnvelope();
        OMNamespace omNs = fac.createOMNamespace("http://services.samples", "ns");
        // creating the payload
        OMElement method = fac.createOMElement("getQuote", omNs);
        while(count!=payload_iterate){
            value1[count] = fac.createOMElement("request", omNs);
            value2[count] = fac.createOMElement("symbol", omNs);

            value2[count].addChild(fac.createOMText(value1[count], "IBM"));
            value1[count].addChild(value2[count]);
            method.addChild(value1[count]);
            count=count+1;
        }
        envelope.getBody().addChild(method);
    return envelope;

/*  <ns:getQuote xmlns:ns="http://services.samples/xsd">
        <ns:request>
          <ns:symbol>IBM</ns:symbol>
        </ns:request>
    </ns:getQuote>

    <soapenv:Body>
     <m0:getQuote xmlns:m0="http://services.samples">
        <m0:request>
           <m0:symbol>IBM</m0:symbol>
        </m0:request>
        <m0:request>
           <m0:symbol>IBM</m0:symbol>
        </m0:request>
        <m0:request>
           <m0:symbol>IBM</m0:symbol>
        </m0:request>
        <m0:request>
           <m0:symbol>IBM</m0:symbol>
        </m0:request>
        <m0:request>
           <m0:symbol>IBM</m0:symbol>
        </m0:request>
     </m0:getQuote>
  </soapenv:Body>
*/
    }
}
