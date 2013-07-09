package messageTypes;

*  Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.


import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

public class QueueProperties {
    public static final String QPID_ICF = "org.wso2.andes.jndi.PropertiesFileInitialContextFactory";
    private static final String CF_NAME_PREFIX = "connectionfactory.";
    private static final String QUEUE_NAME_PREFIX = "queue.";
    private static final String CF_NAME = "qpidConnectionfactory";
    String userName = "admin";
    String password = "admin";
    private static String CARBON_CLIENT_ID = "carbon";
    private static String CARBON_VIRTUAL_HOST_NAME = "carbon";
    private static String CARBON_DEFAULT_HOSTNAME = "localhost";//localhost
    private static String CARBON_DEFAULT_PORT = "5672";
    String queueName = "lanka";
 
    public static void main(String[] args) throws NamingException, JMSException {
        QueueProperties queueSender = new QueueProperties();
        queueSender.sendMessages();
    }
    public void sendMessages() throws NamingException, JMSException {

        Properties properties = new Properties();
        properties.put(Context.INITIAL_CONTEXT_FACTORY, QPID_ICF);
        properties.put(CF_NAME_PREFIX + CF_NAME, getTCPConnectionURL(userName, password));
        properties.put(QUEUE_NAME_PREFIX + queueName, queueName);

        System.out.println("getTCPConnectionURL(userName,password) = " + getTCPConnectionURL(userName, password));

        InitialContext ctx = new InitialContext(properties);

        // Lookup connection factory
        QueueConnectionFactory connFactory = (QueueConnectionFactory) ctx.lookup(CF_NAME);
        QueueConnection queueConnection = connFactory.createQueueConnection();
        queueConnection.start();

        QueueSession queueSession =
                queueConnection.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);

        // Send message
        Queue queue = (Queue)ctx.lookup(queueName);
        // create the message to send


	javax.jms.QueueSender queueSender = queueSession.createSender(queue);

		javax.jms.Message message=queueSession.createMessage();
		
		message.setJMSRedelivered(false);
			
		queueSender.send(message);

	}

        queueSender.close();
        queueSession.close();
        queueConnection.close();


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


