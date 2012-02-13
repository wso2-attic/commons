package org.wso2.mb.sqs.sample;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.xml.security.utils.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Copyright (c) 2009, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class SQSClient {

    public static final String QUEUE_NAME = "QUEUE_BY_M";
    public static final String DEFAULT_VISIBILITY_TIMEOUT = "60";
    public static final String MAX_NUMBER_OF_MESSAGES = "10";
//    public static final String EPR = "http://mb.cloud-test.wso2.com:9774/services/a/mb1.com/QueueService";
//    public static final String EPR = "http://messaging.stratoslive.wso2.com/services/a/mb1.com/QueueService";
    public static final String EPR = "http://localhost:9763/services/QueueService";
    private String accessKey = "47a1be30f86b92c525b1";
    private String secretAccessKey = "92c525b1394e8125197a4a608cefe01e93250d92";



    public static void main(String[] args) throws RemoteException {

        SQSClient sqsClient = new SQSClient();

//        sqsClient.testBasicScenario();
           ConfigurationContext configurationContext = ConfigurationContextFactory.createConfigurationContextFromFileSystem(null, null);
//           sqsClient.testChangeMessageVisibility(configurationContext);
        int i = 0;
        for (; i < 10000; i++) {
            sqsClient.sendMessages(configurationContext);
            sqsClient.receiveMessages();
            sqsClient.deleteMessages(configurationContext);
            sqsClient.deleteQueue(configurationContext);
            System.out.println(i + "---->" + Thread.currentThread().getName());
        }
    }


    public  void testBasicScenario() {
        String epr = "http://localhost:9763/services/MessageQueue/admin/TestQueueB";

        sendMessage(epr, "MyMessage by "+Thread.currentThread().getName(), 10);

//        String[] receiptHandlers = receiveMessages(epr, 10, 10000);
       /* try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }*/

//        changeVisibility(epr, receiptHandlers,550000);

//        deleteMessages(epr, receiptHandlers);
//        receiveMessages(epr, 10, 5000);

    }

    public String createQueue(String queueName) {
        QueueServiceStub queueServiceStub = getQueueServiceStub();
        QueueServiceStub.CreateQueue createQueue = new QueueServiceStub.CreateQueue();
        createQueue.setQueueName(queueName);
        createQueue.setDefaultVisibilityTimeout(new BigInteger(DEFAULT_VISIBILITY_TIMEOUT));
        // add security soap header for action CreateQueue
        addSoapHeader(queueServiceStub, "CreateQueue");
        queueServiceStub._getServiceClient().getOptions().setTimeOutInMilliSeconds(300000000);
        QueueServiceStub.CreateQueueResponse createQueueResponse = null;
        try {
            createQueueResponse = queueServiceStub.createQueue(createQueue);
            queueServiceStub._getServiceClient().cleanupTransport();
            return createQueueResponse.getCreateQueueResult().getQueueUrl().toString();
        } catch (RemoteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    public void sendMessage(String epr, String messageBody, int count) {
        MessageQueueStub messageQueueStub = getMessageQueueStub(epr);
        MessageQueueStub.SendMessage sendMessage = new MessageQueueStub.SendMessage();
        sendMessage.setMessageBody(messageBody);
        addSoapHeader(messageQueueStub, "SendMessage");
        messageQueueStub._getServiceClient().getOptions().setTimeOutInMilliSeconds(300000000);
        try {
            for (int i = 0; i < count; i++) {
                messageQueueStub.sendMessage(sendMessage);
                messageQueueStub._getServiceClient().cleanupTransport();
//                System.out.println("Message sent:" + messageBody);

            }
        } catch (RemoteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public String[] receiveMessages(String epr, int numberOfMessages, long visibilityTimeout) {
        MessageQueueStub messageQueueStub = getMessageQueueStub(epr);
        MessageQueueStub.ReceiveMessage receiveMessage = new MessageQueueStub.ReceiveMessage();
        receiveMessage.setMaxNumberOfMessages(new BigInteger(Integer.toString(numberOfMessages)));
        receiveMessage.setVisibilityTimeout(new BigInteger(Long.toString(visibilityTimeout)));
        addSoapHeader(messageQueueStub, "ReceiveMessage");
        MessageQueueStub.ReceiveMessageResponse receiveMessageResponse = null;
        List<String> receiptHandlers = new ArrayList<String>();
        try {
            receiveMessageResponse = messageQueueStub.receiveMessage(receiveMessage);
            messageQueueStub._getServiceClient().cleanupTransport();
            MessageQueueStub.Message_type0[] message_type0s = receiveMessageResponse.getReceiveMessageResult().getMessage();
            if (message_type0s != null) {
                for (MessageQueueStub.Message_type0 message_type0 : message_type0s) {
//                    System.out.println("message = '" + message_type0.getBody() + "' received by thread = " + Thread.currentThread().getName());
                    receiptHandlers.add(message_type0.getReceiptHandle());

                }
                System.out.println("message_type0s.length = " + message_type0s.length);
            } else {
                System.out.println("No messages received.");
            }
        } catch (RemoteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return receiptHandlers.toArray(new String[receiptHandlers.size()]);
    }
    public List<MessageQueueStub.Message_type0> receiveMessages2(String epr, int numberOfMessages, long visibilityTimeout) {
        MessageQueueStub messageQueueStub = getMessageQueueStub(epr);
        MessageQueueStub.ReceiveMessage receiveMessage = new MessageQueueStub.ReceiveMessage();
        receiveMessage.setMaxNumberOfMessages(new BigInteger(Integer.toString(numberOfMessages)));
        receiveMessage.setVisibilityTimeout(new BigInteger(Long.toString(visibilityTimeout)));
        addSoapHeader(messageQueueStub, "ReceiveMessage");
        MessageQueueStub.ReceiveMessageResponse receiveMessageResponse = null;
        List<MessageQueueStub.Message_type0> messages = new ArrayList<MessageQueueStub.Message_type0>();
        try {
            receiveMessageResponse = messageQueueStub.receiveMessage(receiveMessage);
            messageQueueStub._getServiceClient().cleanupTransport();
            MessageQueueStub.Message_type0[] message_type0s = receiveMessageResponse.getReceiveMessageResult().getMessage();
            if (message_type0s != null) {
                for (MessageQueueStub.Message_type0 message_type0 : message_type0s) {
//                    System.out.println("message = '" + message_type0.getBody() + "' received by thread = " + Thread.currentThread().getName());
                    messages.add(message_type0);

                }
                System.out.println("message_type0s.length = " + message_type0s.length);
            } else {
                System.out.println("No messages received.");
            }
        } catch (RemoteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return messages;
    }

    public void deleteMessages(String epr, String[] receiptHandlers) {
        if (receiptHandlers != null && receiptHandlers.length > 0) {
            MessageQueueStub messageQueueStub = getMessageQueueStub(epr);
            MessageQueueStub.DeleteMessage deleteMessage = new MessageQueueStub.DeleteMessage();
            deleteMessage.setReceiptHandle(receiptHandlers);
            addSoapHeader(messageQueueStub, "DeleteMessage");
            messageQueueStub._getServiceClient().getOptions().setTimeOutInMilliSeconds(300000000);
            try {
                messageQueueStub.deleteMessage(deleteMessage);
                messageQueueStub._getServiceClient().cleanupTransport();
            } catch (RemoteException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    public void changeVisibility(String epr, String[] receiptHandlers, long newVisibilityValue) {
        MessageQueueStub messageQueueStub = getMessageQueueStub(epr);

        MessageQueueStub.ChangeMessageVisibility changeMessageVisibility = new MessageQueueStub.ChangeMessageVisibility();
        if (receiptHandlers != null && receiptHandlers.length > 0) {
            for (String receiptHandler : receiptHandlers) {
                changeMessageVisibility.setReceiptHandle(receiptHandler);
                changeMessageVisibility.setVisibilityTimeout(new BigInteger(Long.toString(newVisibilityValue)));
                addSoapHeader(messageQueueStub, "ChangeMessageVisibility");
                messageQueueStub._getServiceClient().getOptions().setTimeOutInMilliSeconds(300000000);
                try {
                    messageQueueStub.changeMessageVisibility(changeMessageVisibility);
                    messageQueueStub._getServiceClient().cleanupTransport();
                } catch (RemoteException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

            }
        }
    }


    public void deleteQueue(String epr) {
        MessageQueueStub messageQueueStub = getMessageQueueStub(epr);
        MessageQueueStub.DeleteQueue deleteQueue = new MessageQueueStub.DeleteQueue();
        addSoapHeader(messageQueueStub, "DeleteQueue");
        messageQueueStub._getServiceClient().getOptions().setTimeOutInMilliSeconds(300000000);
        try {
            messageQueueStub.deleteQueue(deleteQueue);
            messageQueueStub._getServiceClient().cleanupTransport();
        } catch (RemoteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public QueueServiceStub getQueueServiceStub() {
        try {
            return new QueueServiceStub(ConfigurationContextFactory.createConfigurationContextFromFileSystem(null, null), EPR);
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    public MessageQueueStub getMessageQueueStub(String epr) {
        try {
            return new MessageQueueStub(ConfigurationContextFactory.createConfigurationContextFromFileSystem(null, null), epr);
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    /**
     * Create a queue, send a message
     *
     * @throws RemoteException
     */
    public void sendMessages(ConfigurationContext configurationContext) throws RemoteException {
        QueueServiceStub queueServiceStub = new QueueServiceStub(configurationContext, EPR);
        QueueServiceStub.CreateQueue createQueue = new QueueServiceStub.CreateQueue();
        createQueue.setQueueName(QUEUE_NAME);
        createQueue.setDefaultVisibilityTimeout(new BigInteger(DEFAULT_VISIBILITY_TIMEOUT));
        // add security soap header for action CreateQueue
        addSoapHeader(queueServiceStub, "CreateQueue");
        queueServiceStub._getServiceClient().getOptions().setTimeOutInMilliSeconds(300000000);
        QueueServiceStub.CreateQueueResponse createQueueResponse = queueServiceStub.createQueue(createQueue);
        queueServiceStub._getServiceClient().cleanupTransport();
        System.out.println("createQueueResponse.getCreateQueueResult().getQueueUrl() = " + createQueueResponse.getCreateQueueResult().getQueueUrl());

        MessageQueueStub messageQueueStub = new MessageQueueStub(configurationContext, createQueueResponse.getCreateQueueResult().getQueueUrl().toString());
        MessageQueueStub.SendMessage sendMessage = new MessageQueueStub.SendMessage();
        sendMessage.setMessageBody("TEST MESSAGE" + Thread.currentThread().getName());
        addSoapHeader(messageQueueStub, "SendMessage");
        messageQueueStub._getServiceClient().getOptions().setTimeOutInMilliSeconds(300000000);
        messageQueueStub.sendMessage(sendMessage);
        messageQueueStub._getServiceClient().cleanupTransport();

    }

    /**
     * Receive messages from queue
     *
     * @throws RemoteException
     */
    public void receiveMessages() throws RemoteException {
        QueueServiceStub queueServiceStub = new QueueServiceStub(EPR);
        QueueServiceStub.CreateQueue createQueue = new QueueServiceStub.CreateQueue();
        createQueue.setQueueName(QUEUE_NAME);
        createQueue.setDefaultVisibilityTimeout(new BigInteger(DEFAULT_VISIBILITY_TIMEOUT));
        addSoapHeader(queueServiceStub, "CreateQueue");
        QueueServiceStub.CreateQueueResponse createQueueResponse = queueServiceStub.createQueue(createQueue);
        System.out.println("createQueueResponse.getCreateQueueResult().getQueueUrl() = " + createQueueResponse.getCreateQueueResult().getQueueUrl());

        MessageQueueStub messageQueueStub = new MessageQueueStub(createQueueResponse.getCreateQueueResult().getQueueUrl().toString());
        MessageQueueStub.ReceiveMessage receiveMessage = new MessageQueueStub.ReceiveMessage();
        receiveMessage.setMaxNumberOfMessages(new BigInteger(MAX_NUMBER_OF_MESSAGES));
        receiveMessage.setVisibilityTimeout(new BigInteger("20"));
        addSoapHeader(messageQueueStub, "ReceiveMessage");
        MessageQueueStub.ReceiveMessageResponse receiveMessageResponse = messageQueueStub.receiveMessage(receiveMessage);
        MessageQueueStub.Message_type0[] message_type0s = receiveMessageResponse.getReceiveMessageResult().getMessage();
        if (message_type0s != null) {
            for (MessageQueueStub.Message_type0 message_type0 : message_type0s) {
//                System.out.println("message_type0.getBody() = " + message_type0.getBody());

            }
        }
    }

    /**
     * Delete messages from queue
     *
     * @throws RemoteException
     */
    public synchronized void deleteMessages(ConfigurationContext configurationContext)
            throws RemoteException {
        QueueServiceStub queueServiceStub = new QueueServiceStub(configurationContext, EPR);
        QueueServiceStub.CreateQueue createQueue = new QueueServiceStub.CreateQueue();
        createQueue.setQueueName(QUEUE_NAME);
        createQueue.setDefaultVisibilityTimeout(new BigInteger(DEFAULT_VISIBILITY_TIMEOUT));
        addSoapHeader(queueServiceStub, "CreateQueue");
        queueServiceStub._getServiceClient().getOptions().setTimeOutInMilliSeconds(300000000);
        QueueServiceStub.CreateQueueResponse createQueueResponse = queueServiceStub.createQueue(createQueue);
        queueServiceStub._getServiceClient().cleanupTransport();
        System.out.println("createQueueResponse.getCreateQueueResult().getQueueUrl() = " + createQueueResponse.getCreateQueueResult().getQueueUrl());

        MessageQueueStub messageQueueStub = new MessageQueueStub(configurationContext, createQueueResponse.getCreateQueueResult().getQueueUrl().toString());
        MessageQueueStub.ReceiveMessage receiveMessage = new MessageQueueStub.ReceiveMessage();
        receiveMessage.setMaxNumberOfMessages(new BigInteger(MAX_NUMBER_OF_MESSAGES));
        receiveMessage.setVisibilityTimeout(new BigInteger("5000"));
        addSoapHeader(messageQueueStub, "ReceiveMessage");
        messageQueueStub._getServiceClient().getOptions().setTimeOutInMilliSeconds(300000000);
        MessageQueueStub.ReceiveMessageResponse receiveMessageResponse = messageQueueStub.receiveMessage(receiveMessage);
        messageQueueStub._getServiceClient().cleanupTransport();
        MessageQueueStub.Message_type0[] message_type0s = receiveMessageResponse.getReceiveMessageResult().getMessage();
        List<String> receiptHandlers = new ArrayList<String>();
        if (message_type0s != null) {
            for (MessageQueueStub.Message_type0 message_type0 : message_type0s) {
                receiptHandlers.add(message_type0.getReceiptHandle());
                System.out.println("message_type0.getBody() = " + message_type0.getBody());
            }
        }
        if (receiptHandlers != null && receiptHandlers.size() > 0) {
            MessageQueueStub.DeleteMessage deleteMessage = new MessageQueueStub.DeleteMessage();
            deleteMessage.setReceiptHandle(receiptHandlers.toArray(new String[receiptHandlers.size()]));
            addSoapHeader(messageQueueStub, "DeleteMessage");
            messageQueueStub._getServiceClient().getOptions().setTimeOutInMilliSeconds(300000000);
            messageQueueStub.deleteMessage(deleteMessage);
            messageQueueStub._getServiceClient().cleanupTransport();
        }
    }

    /**
     * Delete queue
     *
     * @throws RemoteException
     */
    public synchronized void deleteQueue(ConfigurationContext configurationContext)
            throws RemoteException {
        QueueServiceStub queueServiceStub = new QueueServiceStub(configurationContext, EPR);
        QueueServiceStub.CreateQueue createQueue = new QueueServiceStub.CreateQueue();
        createQueue.setQueueName(QUEUE_NAME);
        createQueue.setDefaultVisibilityTimeout(new BigInteger(DEFAULT_VISIBILITY_TIMEOUT));
        addSoapHeader(queueServiceStub, "CreateQueue");
        queueServiceStub._getServiceClient().getOptions().setTimeOutInMilliSeconds(300000000);
        QueueServiceStub.CreateQueueResponse createQueueResponse = queueServiceStub.createQueue(createQueue);
        queueServiceStub._getServiceClient().cleanupTransport();
        System.out.println("createQueueResponse.getCreateQueueResult().getQueueUrl() = " + createQueueResponse.getCreateQueueResult().getQueueUrl());

        MessageQueueStub messageQueueStub = new MessageQueueStub(configurationContext, createQueueResponse.getCreateQueueResult().getQueueUrl().toString());
        MessageQueueStub.DeleteQueue deleteQueue = new MessageQueueStub.DeleteQueue();
        addSoapHeader(messageQueueStub, "DeleteQueue");
        messageQueueStub._getServiceClient().getOptions().setTimeOutInMilliSeconds(300000000);
        messageQueueStub.deleteQueue(deleteQueue);
        messageQueueStub._getServiceClient().cleanupTransport();
    }

    /**
     * Add security headers for queue service stub
     *
     * @param queueServiceStub - queue service stub created with given end point
     * @param action           - the action to be performed as CreateQueue, ListQueue
     */
    private void addSoapHeader(QueueServiceStub queueServiceStub, String action) {

        OMFactory factory = OMAbstractFactory.getOMFactory();
        OMNamespace awsNs = factory.createOMNamespace("http://security.amazonaws.com/doc/2007-01-01/", "aws");
        OMElement accessKeyId = factory.createOMElement("AWSAccessKeyId", awsNs);
        accessKeyId.setText(accessKey);
        OMElement timestamp = factory.createOMElement("Timestamp", awsNs);
        timestamp.setText(new Date().toString());
        OMElement signature = factory.createOMElement("Signature", awsNs);

        try {
            signature.setText(getSignature(action + timestamp.getText(), secretAccessKey));
        } catch (SignatureException e) {
        }

        queueServiceStub._getServiceClient().removeHeaders();

        queueServiceStub._getServiceClient().addHeader(accessKeyId);
        queueServiceStub._getServiceClient().addHeader(timestamp);
        queueServiceStub._getServiceClient().addHeader(signature);


    }

    /**
     * Add security headers for message queue service stub
     *
     * @param messageQueueStub - message queue service stub created with queue url
     * @param action           - the action to be performed as SendMessage,DeleteMessage
     */
    private void addSoapHeader(MessageQueueStub messageQueueStub, String action) {
        OMFactory factory = OMAbstractFactory.getSOAP11Factory();
        OMNamespace awsNs = factory.createOMNamespace("http://security.amazonaws.com/doc/2007-01-01/", "aws");
        OMElement header = factory.createOMElement("Header", awsNs);
        OMElement accessKeyId = factory.createOMElement("AWSAccessKeyId", awsNs);
        accessKeyId.setText(accessKey);
        OMElement timestamp = factory.createOMElement("Timestamp", awsNs);
        timestamp.setText(new Date().toString());
        OMElement signature = factory.createOMElement("Signature", awsNs);

        try {
            signature.setText(getSignature(action + timestamp.getText(), secretAccessKey));
        } catch (SignatureException e) {
        }

        messageQueueStub._getServiceClient().removeHeaders();

        messageQueueStub._getServiceClient().addHeader(accessKeyId);
        messageQueueStub._getServiceClient().addHeader(timestamp);
        messageQueueStub._getServiceClient().addHeader(signature);
    }

    /**
     * Calculate signature for given data using secret access key
     *
     * @param data - data to be signed, action+timestamp
     * @param secretAccessKeyId- secret access key
     * @return signature
     * @throws java.security.SignatureException
     *
     */
    public static String getSignature(String data, String secretAccessKeyId)
            throws java.security.SignatureException {
        final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
        String result;
        try {
            SecretKeySpec signingKey = new SecretKeySpec(secretAccessKeyId.getBytes(), HMAC_SHA1_ALGORITHM);
            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(data.getBytes());
            result = Base64.encode(rawHmac);
        } catch (Exception e) {
            throw new SignatureException("Failed to get signature with data, "+data, e);
        }
        return result;
    }

    public void testChangeMessageVisibility(ConfigurationContext configurationContext) {
        try {

            QueueServiceStub queueServiceStub = new QueueServiceStub(configurationContext, EPR);
            QueueServiceStub.CreateQueue createQueue = new QueueServiceStub.CreateQueue();
            createQueue.setQueueName("mySendQueue");
            createQueue.setDefaultVisibilityTimeout(new BigInteger("5000"));
            QueueServiceStub.CreateQueueResponse createQueueResponse = null;
            addSoapHeader(queueServiceStub, "CreateQueue");
            createQueueResponse = queueServiceStub.createQueue(createQueue);

            assert (createQueueResponse.getCreateQueueResult().getQueueUrl() != null) : "Created queue url can not be null.";

            MessageQueueStub messageQueueStub = new MessageQueueStub(configurationContext, createQueueResponse.getCreateQueueResult().getQueueUrl().toString());
            for (int i = 0; i < 5; i++) {
                MessageQueueStub.SendMessage sendMessage = new MessageQueueStub.SendMessage();
                sendMessage.setMessageBody("test Message " + i);
                addSoapHeader(messageQueueStub, "SendMessage");
                messageQueueStub.sendMessage(sendMessage);
            }


            for (int i = 0; i < 5; i++) {
                // retrive the message
                MessageQueueStub.ReceiveMessage receiveMessage = new MessageQueueStub.ReceiveMessage();
                receiveMessage.setMaxNumberOfMessages(new BigInteger("1"));
                receiveMessage.setVisibilityTimeout(new BigInteger("12000"));
                addSoapHeader(messageQueueStub, "ReceiveMessage");
                MessageQueueStub.ReceiveMessageResponse response = messageQueueStub.receiveMessage(receiveMessage);


                List<String> receiptHandlers = new ArrayList<String>();

                for (MessageQueueStub.Message_type0 message_type0 : response.getReceiveMessageResult().getMessage()) {
                    assert (message_type0.getBody() != null) : "Message body is null";
                    assert (message_type0.getReceiptHandle() != null) : "Message receipt handler is null";
                    MessageQueueStub.ChangeMessageVisibility changeMessageVisibility = new MessageQueueStub.ChangeMessageVisibility();
                    changeMessageVisibility.setReceiptHandle(message_type0.getReceiptHandle());
                    changeMessageVisibility.setVisibilityTimeout(new BigInteger("30000"));
                    addSoapHeader(messageQueueStub, "ChangeMessageVisibility");
                    messageQueueStub.changeMessageVisibility(changeMessageVisibility);


                    receiptHandlers.add(message_type0.getReceiptHandle());
                }

                MessageQueueStub.DeleteMessage deleteMessage = new MessageQueueStub.DeleteMessage();
                deleteMessage.setReceiptHandle(receiptHandlers.toArray(new String[receiptHandlers.size()]));
                addSoapHeader(messageQueueStub, "DeleteMessage");
                messageQueueStub.deleteMessage(deleteMessage);

            }

            MessageQueueStub.ReceiveMessage receiveMessage = new MessageQueueStub.ReceiveMessage();
            receiveMessage.setMaxNumberOfMessages(new BigInteger("5"));
            receiveMessage.setVisibilityTimeout(new BigInteger("60"));
            addSoapHeader(messageQueueStub, "ReceiveMessage");
            MessageQueueStub.ReceiveMessageResponse response = messageQueueStub.receiveMessage(receiveMessage);
            assert (response.getReceiveMessageResult().getMessage() == null) : "No messages should returned as all messages are deleted";

        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
