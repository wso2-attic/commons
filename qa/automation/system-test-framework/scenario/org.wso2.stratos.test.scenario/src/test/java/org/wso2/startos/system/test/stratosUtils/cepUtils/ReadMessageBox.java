package org.wso2.startos.system.test.stratosUtils.cepUtils;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.databinding.utils.ConverterUtil;
import org.apache.axis2.transport.http.HTTPConstants;
import org.wso2.carbon.event.client.broker.BrokerClient;
import org.wso2.carbon.event.client.stub.generated.authentication.AuthenticationExceptionException;
import org.wso2.carbon.messagebox.stub.*;
import org.wso2.carbon.messagebox.stub.admin.internal.MessageBoxAdminServiceMessageBoxAdminExceptionException;
import org.wso2.carbon.messagebox.stub.admin.internal.MessageBoxAdminServiceStub;
import org.wso2.carbon.messagebox.stub.admin.internal.xsd.SQSKeys;

import javax.jms.JMSException;
import javax.naming.NamingException;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.security.SignatureException;
import java.util.Calendar;

import static org.wso2.startos.system.test.stratosUtils.msUtils.MessageBoxSubClient.calculateRFC2104HMAC;

public class ReadMessageBox {
    private BrokerClient brokerClient;
    private String messageBoxURI = "https://cep.stratoslive.wso2.com/services/a/manualQA0001.org/MessageQueue/admin123/outPutQueue";
    private SQSKeys sqsKeys;

    public String retriveAndDeleteMessage(String sessionCookie) throws JMSException, AxisFault, RemoteException {
        String result = null;

        try {
            getAccessKeys(sessionCookie);
        } catch (NamingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        MessageQueueStub stub = null;

        stub = new MessageQueueStub(this.messageBoxURI);
        addSoapHeader("ReceiveMessage", stub._getServiceClient());

        ReceiveMessage receiveMessage = new ReceiveMessage();
        receiveMessage.setMaxNumberOfMessages(new BigInteger("1"));
        receiveMessage.setVisibilityTimeout(new BigInteger("60000"));
        ReceiveMessageResponse response = stub.receiveMessage(receiveMessage);

        Message_type0[] messages = response.getReceiveMessageResult().getMessage();
        for (Message_type0 message_type0 : messages) {
            result = message_type0.getBody();
            stub = new MessageQueueStub(this.messageBoxURI);
            addSoapHeader("DeleteMessage", stub._getServiceClient());
            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setReceiptHandle(new String[]{message_type0.getReceiptHandle()});
            stub.deleteMessage(deleteMessage);
        }


        return result;
    }

    private void addSoapHeader(String action, ServiceClient serviceClient) {

        OMFactory factory = OMAbstractFactory.getOMFactory();
        OMNamespace awsNs = factory.createOMNamespace("http://security.amazonaws.com/doc/2007-01-01/", "aws");

        OMElement accessKeyId = factory.createOMElement("AWSAccessKeyId", awsNs);
        accessKeyId.setText(this.sqsKeys.getAccessKeyId());

        OMElement timestamp = factory.createOMElement("Timestamp", awsNs);
        timestamp.setText(ConverterUtil.convertToString(Calendar.getInstance()));

        OMElement signature = factory.createOMElement("Signature", awsNs);

        try {
            signature.setText(calculateRFC2104HMAC(action + timestamp.getText(), this.sqsKeys.getSecretAccessKeyId()));
        } catch (SignatureException e) {
            e.printStackTrace();
        }

        serviceClient.removeHeaders();

        serviceClient.addHeader(accessKeyId);
        serviceClient.addHeader(timestamp);
        serviceClient.addHeader(signature);
    }


    public boolean getAccessKeys(String sessionCookie) throws JMSException, NamingException {
        boolean result = true;
        try {

            String servicesString = "https://cep.stratoslive.wso2.com/services";
            MessageBoxAdminServiceStub messageBoxAdminServiceStub = new MessageBoxAdminServiceStub(servicesString + "MessageBoxAdminService");
            messageBoxAdminServiceStub._getServiceClient().getOptions().setManageSession(true);
            messageBoxAdminServiceStub._getServiceClient().getOptions().setProperty(HTTPConstants.COOKIE_STRING, sessionCookie);

            SQSKeys sqsKeys = messageBoxAdminServiceStub.getSQSKeys("admin123");
            this.sqsKeys = sqsKeys;

            //create the broker client
            this.brokerClient = new BrokerClient("https://cep.stratoslive.wso2.com/services/t/manualQA0001.org/localBrokerService", "admin123@manualQA0001.org", "admin123");

        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
            result = false;
        } catch (AuthenticationExceptionException e) {
            e.printStackTrace();
            result = false;
        } catch (RemoteException e) {
            e.printStackTrace();
            result = false;
        } catch (MessageBoxAdminServiceMessageBoxAdminExceptionException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }


}
