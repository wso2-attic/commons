package org.wso2.startos.system.test.stratosUtils.msUtils;

import org.wso2.carbon.admin.service.utils.FrameworkSettings;
import org.wso2.carbon.system.test.core.utils.TenantDetails;
import org.wso2.carbon.system.test.core.utils.TenantListCsvReader;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

public class TopicSubscriber {
    public static final String QPID_ICF = "org.apache.qpid.jndi.PropertiesFileInitialContextFactory";
    protected static final String CF_NAME_PREFIX = "connectionfactory.";
    protected static final String CF_NAME = "qpidConnectionfactory";
    protected static String userName;
    protected static String password;

    private static String CARBON_CLIENT_ID = "carbon";
    private static String CARBON_VIRTUAL_HOST_NAME = "carbon";
    private static String CARBON_DEFAULT_HOSTNAME = FrameworkSettings.MB_SERVER_HOST_NAME;
    private static String CARBON_DEFAULT_PORT =  FrameworkSettings.MB_QPID_PORT;
    static String queueName = "testQueueQA2";
    String topicName = "MYTopic";

    public TopicSubscriber(){
        TenantDetails tenantDetails = TenantListCsvReader.getTenantDetails(TenantListCsvReader.getTenantId("4"));
        this.userName = tenantDetails.getTenantName().replaceAll("@", "!");
        this.password = tenantDetails.getTenantPassword();
    }

    public String subscribe() throws NamingException, JMSException {
        System.out.print("Request listning ................. \n");
        Properties properties = new Properties();
        properties.put(Context.INITIAL_CONTEXT_FACTORY, QPID_ICF);
        properties.put(CF_NAME_PREFIX + CF_NAME, getTCPConnectionURL(userName, password));

        System.out.println("getTCPConnectionURL(userName,password) = " + getTCPConnectionURL(userName, password));

        InitialContext ctx = new InitialContext(properties);
        // Lookup connection factory
        TopicConnectionFactory connFactory = (TopicConnectionFactory) ctx.lookup(CF_NAME);
        TopicConnection topicConnection = connFactory.createTopicConnection();
        topicConnection.start();
        TopicSession topicSession =
                topicConnection.createTopicSession(false, QueueSession.AUTO_ACKNOWLEDGE);

        // Send message
        Topic topic = topicSession.createTopic(topicName);
        javax.jms.TopicSubscriber topicSubscriber = topicSession.createSubscriber(topic);
        Message message = topicSubscriber.receive();
        TextMessage textMessage = null;
        if (message instanceof TextMessage) {
            textMessage = (TextMessage) message;
            System.out.println("textMessage.getText() = " + textMessage.getText());
        }
        topicSession.close();
        topicConnection.close();
        System.out.print("Connection Closed................ \n");
        return textMessage.getText();
    }

    public String getTCPConnectionURL(String username, String password) {
        // amqp://{username}:{password}@carbon/carbon?brokerlist='tcp://{hostname}:{port}'
        return new StringBuffer()
                .append("amqp://").append(username).append(":").append(password)
                .append("@").append(CARBON_CLIENT_ID)
                .append("/").append(CARBON_VIRTUAL_HOST_NAME)
                .append("?brokerlist='tcp://").append(CARBON_DEFAULT_HOSTNAME).append(":").append(CARBON_DEFAULT_PORT).append("'")
                .toString();
    }

}
