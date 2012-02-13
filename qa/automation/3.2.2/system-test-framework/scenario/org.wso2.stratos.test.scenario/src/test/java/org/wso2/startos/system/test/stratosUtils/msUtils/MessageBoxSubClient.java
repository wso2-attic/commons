package org.wso2.startos.system.test.stratosUtils.msUtils;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.util.Base64;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.databinding.utils.ConverterUtil;
import org.apache.axis2.transport.http.HTTPConstants;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;
import org.wso2.carbon.event.client.broker.BrokerClient;
import org.wso2.carbon.event.client.broker.BrokerClientException;
import org.wso2.carbon.event.client.stub.generated.authentication.AuthenticationExceptionException;
import org.wso2.carbon.messagebox.stub.*;
import org.wso2.carbon.messagebox.stub.admin.internal.MessageBoxAdminServiceMessageBoxAdminExceptionException;
import org.wso2.carbon.messagebox.stub.admin.internal.MessageBoxAdminServiceStub;
import org.wso2.carbon.messagebox.stub.admin.internal.xsd.SQSKeys;
import org.wso2.carbon.system.test.core.utils.TenantDetails;
import org.wso2.carbon.system.test.core.utils.TenantListCsvReader;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.jms.JMSException;
import javax.naming.NamingException;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.security.SignatureException;
import java.util.Calendar;

public class MessageBoxSubClient {

    private SQSKeys sqsKeys;
    private BrokerClient brokerClient;
    private String messageBoxURI;
    private String subscriptionID;
    private String backEndServicesString = FrameworkSettings.MB_BACKEND_URL;
    private TenantDetails tenantDetails;
    private String userName;
    private String password;

    public MessageBoxSubClient() {
        backEndServicesString = FrameworkSettings.MB_BACKEND_URL;
        this.tenantDetails = TenantListCsvReader.getTenantDetails(TenantListCsvReader.getTenantId("5"));
        this.userName = tenantDetails.getTenantName();
        this.password = tenantDetails.getTenantPassword();

    }


    public boolean getAccessKeys(String sessionCookie) throws JMSException, NamingException {
        boolean result = true;
        String eventBrokerServiceEPR = backEndServicesString + "/t/EventBrokerService";
        try {

            MessageBoxAdminServiceStub messageBoxAdminServiceStub = new MessageBoxAdminServiceStub(backEndServicesString + "MessageBoxAdminService");
            messageBoxAdminServiceStub._getServiceClient().getOptions().setManageSession(true);
            messageBoxAdminServiceStub._getServiceClient().getOptions().setProperty(HTTPConstants.COOKIE_STRING, sessionCookie);

            SQSKeys sqsKeys = messageBoxAdminServiceStub.getSQSKeys(userName);
            this.sqsKeys = sqsKeys;

            //create the broker client
            this.brokerClient = new BrokerClient(eventBrokerServiceEPR, userName, password);

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

    public String createMessageBox() throws JMSException {
        try {
            String[] userIDs =  tenantDetails.getTenantName().split("@");
            String userID = userIDs[0];
            QueueServiceStub stub = new QueueServiceStub(backEndServicesString + "/t/" + tenantDetails.getTenantDomain() + "/" + userID + "/QueueService");
            addSoapHeader("CreateQueue", stub._getServiceClient());
            CreateQueue createQueue = new CreateQueue();
            createQueue.setQueueName("testMessageBox");
            createQueue.setDefaultVisibilityTimeout(new BigInteger("30"));
            CreateQueueResponse response = stub.createQueue(createQueue);
            this.messageBoxURI = response.getCreateQueueResult().getQueueUrl().toString();

            System.out.println("Queue Created with URI ==> " + this.messageBoxURI);

        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return this.messageBoxURI;
    }

    public boolean subscribe() throws JMSException {
        boolean result = true;
        // subscribe with message box.
        try {
            this.subscriptionID = this.brokerClient.subscribe("foo/messagebox", "sqs://admin/testMessageBox");
        } catch (BrokerClientException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    public boolean publish() throws JMSException {
        boolean result = true;
        try {
            this.brokerClient.publish("foo/messagebox", getOMElementToSend());
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
            result = false;
        }
        return result;
    }

    public String retriveAndDeleteMessage() throws JMSException {
        String result = null;
        try {
            MessageQueueStub stub = null;

            stub = new MessageQueueStub(this.messageBoxURI);
            addSoapHeader("ReceiveMessage", stub._getServiceClient());

            ReceiveMessage receiveMessage = new ReceiveMessage();
            receiveMessage.setMaxNumberOfMessages(new BigInteger("1"));
            receiveMessage.setVisibilityTimeout(new BigInteger("30000"));
            ReceiveMessageResponse response = stub.receiveMessage(receiveMessage);

            Message_type0[] messages = response.getReceiveMessageResult().getMessage();
            for (Message_type0 message_type0 : messages) {
                result = message_type0.getBody();
                System.out.println("Got the message ==> " + message_type0.getBody());

                stub = new MessageQueueStub(this.messageBoxURI);
                addSoapHeader("DeleteMessage", stub._getServiceClient());
                DeleteMessage deleteMessage = new DeleteMessage();
                deleteMessage.setReceiptHandle(new String[]{message_type0.getReceiptHandle()});
                stub.deleteMessage(deleteMessage);
            }

        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean deleteMessageBox() {
        boolean result = true;
        MessageQueueStub stub = null;
        try {
            stub = new MessageQueueStub(this.messageBoxURI);
            addSoapHeader("DeleteQueue", stub._getServiceClient());

            DeleteQueue deleteQueue = new DeleteQueue();
            stub.deleteQueue(deleteQueue);

        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
            result = false;
        } catch (RemoteException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    public boolean unsubscribe() throws JMSException {
        boolean result = true;
        try {
            this.brokerClient.unsubscribe(this.subscriptionID);
        } catch (RemoteException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }


    private OMElement getOMElementToSend() {
        OMFactory omFactory = OMAbstractFactory.getOMFactory();
        OMNamespace omNamespace = omFactory.createOMNamespace("http://ws.sample.org", "ns1");
        OMElement receiveElement = omFactory.createOMElement("receive", omNamespace);
        OMElement messageElement = omFactory.createOMElement("message", omNamespace);
        messageElement.setText("Test publish message");
        receiveElement.addChild(messageElement);
        return receiveElement;
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

    public static String calculateRFC2104HMAC(String data, String key)
            throws java.security.SignatureException {

        final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
        String result;
        try {
            // get an hmac_sha1 key from the raw key bytes
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);
            // get an hmac_sha1 Mac instance and initialize with the signing key
            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            mac.init(signingKey);
            // compute the hmac on input data bytes
            byte[] rawHmac = mac.doFinal(data.getBytes());
            // base64-encode the hmac
            result = Base64.encode(rawHmac);
        } catch (Exception e) {
            throw new SignatureException("Failed to generate HMAC : " + e.getMessage());
        }

        return result;
    }
}
