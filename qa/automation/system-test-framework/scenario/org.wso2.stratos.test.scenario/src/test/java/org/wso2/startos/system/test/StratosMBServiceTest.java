/*
 * Copyright (c) 2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.startos.system.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.TenantDetails;
import org.wso2.carbon.system.test.core.utils.TenantListCsvReader;
import org.wso2.startos.system.test.stratosUtils.ServiceLoginClient;
import org.wso2.startos.system.test.stratosUtils.msUtils.MBThread;
import org.wso2.startos.system.test.stratosUtils.msUtils.MessageBoxSubClient;
import org.wso2.startos.system.test.stratosUtils.msUtils.TopicPublisher;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.PriorityQueue;
import java.util.Properties;

//import org.wso2.startos.system.test.stratosUtils.localHostclient;

public class StratosMBServiceTest extends TestTemplate {

    private static final Log log = LogFactory.getLog(StratosMBServiceTest.class);

    public static final String QPID_ICF = "org.apache.qpid.jndi.PropertiesFileInitialContextFactory";
    protected static final String CF_NAME_PREFIX = "connectionfactory.";
    protected static final String CF_NAME = "qpidConnectionfactory";
    private static String userName;
    private static String password;

    private static String CARBON_CLIENT_ID = "carbon";
    private static String CARBON_VIRTUAL_HOST_NAME = "carbon";
    private static String CARBON_DEFAULT_HOSTNAME;
    private static String CARBON_DEFAULT_PORT;

    static String queueName = "testQueueQA2";
    protected static String topicName = "TestTopic";


    @Override
    public void init() {
        testClassName = StratosMBServiceTest.class.getName();
        TenantDetails tenantDetails = TenantListCsvReader.getTenantDetails(TenantListCsvReader.getTenantId("4"));
        userName = tenantDetails.getTenantName().replaceAll("@", "!");
        password = tenantDetails.getTenantPassword();
        CARBON_DEFAULT_HOSTNAME =  FrameworkSettings.MB_SERVER_HOST_NAME;
        CARBON_DEFAULT_PORT = FrameworkSettings.MB_QPID_PORT;
    }

    @Override
    public void runSuccessCase() {
        String mbServerHostName = CARBON_DEFAULT_HOSTNAME;
        String sessionCookie = ServiceLoginClient.loginChecker(mbServerHostName);

        jmsQueueSenderTestClient();
        MessageBoxSubClient messageBoxSubClient = new MessageBoxSubClient();
        try {
            messageBoxSubClient.getAccessKeys(sessionCookie);
            assertTrue("Message box created", messageBoxSubClient.createMessageBox().contains("testMessageBox"));
            assertTrue("Message box created", messageBoxSubClient.subscribe());
            assertTrue("Messagebox published", messageBoxSubClient.publish());
            assertTrue("Message retreaved and box is deleted", messageBoxSubClient.retriveAndDeleteMessage().contains("Test publish message"));
            assertTrue("Messagebox is deleted", messageBoxSubClient.deleteMessageBox());
            assertTrue("Messagebox unsubcribed", messageBoxSubClient.unsubscribe());
        } catch (NamingException e) {
            e.printStackTrace();
            fail("JMS exception occurred : " + e.getMessage());
            //To change body of catch statement use File | Settings | File Templates.
        } catch (JMSException e) {
            e.printStackTrace();
            fail("JMS exception occurred : " + e.getMessage());

        }

        java.util.Queue<String> queue = new PriorityQueue<String>();
        int expectedResults = 2;
        Thread msThread = new MBThread(queue);
        msThread.start();


        TopicPublisher topic = new TopicPublisher();
        try {
            topic.publishMessage();
        } catch (NamingException e) {
            e.printStackTrace();
            fail("JMS exception occurred : " + e.getMessage());
            //To change body of catch statement use File | Settings | File Templates.
        } catch (JMSException e) {
            e.printStackTrace();
            fail("JMS exception occurred : " + e.getMessage());

        }
        int receivedResults = 0;
        while (receivedResults < expectedResults) {
            if (!queue.isEmpty()) {
                String massage = queue.poll();
                if (massage.equalsIgnoreCase("TEST Message")) {
                    assertTrue("Message reciveed", massage.equalsIgnoreCase("TEST Message"));
                    break;
                }
                receivedResults++;
            }
            if (receivedResults >= 100) {
                fail(" Response is absent");

                break;
            }
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.error("Thread Interrupted Exception" + e.getMessage());
            fail("Thread Interrupted Exception" + e.getMessage());
        }
    }

    @Override
    public void cleanup() {
    }

    public static boolean jmsQueueSenderTestClient() {
        Boolean jmsQueueSenderTestClientStatus = false;
        Properties properties = new Properties();
        properties.put(Context.INITIAL_CONTEXT_FACTORY, QPID_ICF);
        properties.put(CF_NAME_PREFIX + CF_NAME, getTCPConnectionURL(userName, password));

        System.out.println("getTCPConnectionURL(userName,password) = " + getTCPConnectionURL(userName, password));

        try {
            InitialContext ctx = new InitialContext(properties);

            // Lookup connection factory
            QueueConnectionFactory connFactory = (QueueConnectionFactory) ctx.lookup(CF_NAME);
            QueueConnection queueConnection = connFactory.createQueueConnection();
            queueConnection.start();
            QueueSession queueSession =
                    queueConnection.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);

            // Send message
            Queue queue = queueSession.createQueue(queueName + ";{create:always, node:{durable: True}}");

            // create the message to send
            TextMessage textMessage = queueSession.createTextMessage("Test Message Hello");
            javax.jms.QueueSender queueSender = queueSession.createSender(queue);
            queueSender.setTimeToLive(100000000);

            QueueReceiver queueReceiver = queueSession.createReceiver(queue);
            queueSender.send(textMessage);

            TextMessage message = (TextMessage) queueReceiver.receiveNoWait();
            System.out.println("message.getText() = " + message.getText());


            if (message.getText().equals("Test Message Hello")) {
                jmsQueueSenderTestClientStatus = true;
            }
            assertTrue("jms Queue - retrive messages failed", jmsQueueSenderTestClientStatus);

            queueSender.close();
            queueSession.close();
            queueConnection.close();

        } catch (JMSException e) {
            log.error("JMS exception occurred : " + e.getMessage());
            fail("JMS exception occurred : " + e.getMessage());

        } catch (NamingException e) {
            log.error("JMS naming exception occurred : " + e.getMessage());
            fail("Naming exception occurred : " + e.getMessage());
        }
        return jmsQueueSenderTestClientStatus;
    }

    public static String getTCPConnectionURL(String username, String password) {
        return new StringBuffer()
                .append("amqp://").append(username).append(":").append(password)
                .append("@").append(CARBON_CLIENT_ID)
                .append("/").append(CARBON_VIRTUAL_HOST_NAME)
                .append("?brokerlist='tcp://").append(CARBON_DEFAULT_HOSTNAME).append(":").append(CARBON_DEFAULT_PORT).append("'")
                .toString();
    }
}
