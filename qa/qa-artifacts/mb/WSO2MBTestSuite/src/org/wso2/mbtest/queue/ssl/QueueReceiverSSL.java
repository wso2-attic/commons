package org.wso2.mbtest.queue.ssl;/*
*  Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/
import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;
public class QueueReceiverSSL {
    public static final String QPID_ICF = "org.wso2.andes.jndi.PropertiesFileInitialContextFactory";
    private static final String CF_NAME_PREFIX = "connectionfactory.";
    private static final String CF_NAME = "qpidConnectionfactory";
    String userName = "admin";
    String password = "admin";
    String queueName = "kumara";
    private MessageConsumer queueReceiver ;
    QueueConnection queueConnection;
    QueueSession queueSession;

    public static void main(String[] args) throws NamingException, JMSException, InterruptedException {
        
    	QueueReceiverSSL queueReceiver2 = new QueueReceiverSSL();
        queueReceiver2.receiveMessages();

    }
    public void receiveMessages() throws NamingException, JMSException, InterruptedException {
        try{
            Properties properties = new Properties();
            properties.put(Context.INITIAL_CONTEXT_FACTORY, QPID_ICF);
            properties.put(CF_NAME_PREFIX + CF_NAME, getTCPConnectionURL(userName, password));
            properties.put("queue."+ queueName,queueName);
            System.out.println("getTCPConnectionURL(userName,password) = " + getTCPConnectionURL(userName, password));
            InitialContext ctx = new InitialContext(properties);

            // Lookup connection factory
            QueueConnectionFactory connFactory = (QueueConnectionFactory) ctx.lookup(CF_NAME);
            queueConnection= connFactory.createQueueConnection();
            queueSession =queueConnection.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);

            //Receive message
            Queue queue = (Queue) ctx.lookup(queueName);
            queueReceiver= queueSession.createConsumer(queue);            
            queueConnection.start();
            TextMessage message;
            int messageCount = 0;
            while ((message = (TextMessage) queueReceiver.receive()) != null) {
                System.out.println(messageCount+"------"+message.getText() + " timestamp: " + System.currentTimeMillis()/1000);
                messageCount++;
            }

        }catch (Exception e){
            System.out.println(e);

        } finally {
            queueReceiver.close();
            queueSession.close();
            queueConnection.close();
        }


    }



    public String getTCPConnectionURL(String username, String password) {
    	 // amqp://{username}:{password}@carbon/carbon?brokerlist='tcp://{hostname}:{port}' ?ssl='true'
    	// amqp://{username}:{password}@carbon/carbon?brokerlist='tcp://{hostname}:{port}' ?ssl='true'
    	String url = "amqp://admin:admin@carbon/carbon?failover='roundrobin'&brokerlist='tcp://204.13.85.2:8682?ssl='true'&ssl_cert_alias='RootCA'&trust_store='/home/asankav/servers/fresh/mb/wso2mb-2.1.0/repository/resources/security/client-truststore.jks'&trust_store_password='wso2carbon'&key_store='/home/asankav/servers/fresh/mb/wso2mb-2.1.0/repository/resources/security/wso2carbon.jks'&key_store_password='wso2carbon';tcp://204.13.85.3:8682?ssl='true'&ssl_cert_alias='RootCA'&trust_store='/home/asankav/servers/fresh/mb/wso2mb-2.1.0/repository/resources/security/client-truststore.jks'&trust_store_password='wso2carbon'&key_store='/home/asankav/servers/fresh/mb/wso2mb-2.1.0/repository/resources/security/wso2carbon.jks'&key_store_password='wso2carbon';tcp://204.13.85.4:8682?ssl='true'&ssl_cert_alias='RootCA'&trust_store='/home/asankav/servers/fresh/mb/wso2mb-2.1.0/repository/resources/security/client-truststore.jks'&trust_store_password='wso2carbon'&key_store='/home/asankav/servers/fresh/mb/wso2mb-2.1.0/repository/resources/security/wso2carbon.jks'&key_store_password='wso2carbon''";

        //return url;
       // String url = "amqp://admin:admin@carbon/carbon?brokerlist='tcp://204.13.85.2:8682?ssl='true'&ssl_cert_alias='RootCA'&trust_store='/home/asankav/servers/fresh/mb/wso2mb-2.1.0/repository/resources/security/client-truststore.jks'&trust_store_password='wso2carbon'&key_store='/home/asankav/servers/fresh/mb/wso2mb-2.1.0/repository/resources/security/wso2carbon.jks'&key_store_password='wso2carbon''";

        return url;
        
    }
    
}