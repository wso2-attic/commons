package org.wso2.mbtest.topics.subscriber;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;
import java.lang.*;

public class ManyTopicSubscriptions {

	    public static final String QPID_ICF = "org.wso2.andes.jndi.PropertiesFileInitialContextFactory";
	    private static final String CF_NAME_PREFIX = "connectionfactory.";
	    private static final String CF_NAME = "qpidConnectionfactory";
	    String userName = "admin";
	    String password = "admin";
	    private static String CARBON_CLIENT_ID = "carbon";
	    private static String CARBON_VIRTUAL_HOST_NAME = "carbon";
	    private static String CARBON_DEFAULT_HOSTNAME = "localhost";
	    private static String CARBON_DEFAULT_PORT = "5672";
	    String topicName = "Lankafm";
	 
	    public static void main(String[] args) throws NamingException, JMSException,InterruptedException {
	    	ManyTopicSubscriptions topicSubscriber = new ManyTopicSubscriptions();
	        topicSubscriber.subscribe();
	    }
	    public void subscribe() throws NamingException, JMSException,InterruptedException {

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
	 
	        Topic topic = topicSession.createTopic(topicName);

		for(int i=0;i<1000;i++){
	        javax.jms.TopicSubscriber topicSubscriber = topicSession.createSubscriber(topic);
	        
		}

		
		
		Thread.sleep(50000);
	       topicSession.close();
	       topicConnection.close();
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

