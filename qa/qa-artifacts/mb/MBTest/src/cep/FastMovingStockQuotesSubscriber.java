package cep;

import cep.JNDIContext;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;	
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.InitialContext;
import javax.xml.stream.XMLStreamException;
import java.util.Enumeration;
public class FastMovingStockQuotesSubscriber implements MessageListener {
    private static InitialContext initContext = null;
    private static TopicConnectionFactory topicConnectionFactory = null;
    private boolean messageReceived = false;
    static String TOPIC = "FastMovingStockQuotes";
    public static void main(String[] args) throws XMLStreamException {
        initContext = JNDIContext.getInstance().getInitContext();
        topicConnectionFactory = JNDIContext.getInstance().getTopicConnectionFactory();
        new FastMovingStockQuotesSubscriber().subscribe(TOPIC);
    }
    public void subscribe(String topicName) {
        // create connection
        TopicConnection topicConnection = null;
        try {
            topicConnection = topicConnectionFactory.createTopicConnection();
        } catch (JMSException e) {
            System.out.println("Can not create topic connection." + e);
            return;
        }
        // create session, subscriber, message listener and listen on that topic
        TopicSession session = null;
        try {
            session = topicConnection.createTopicSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic(topicName);
            TopicSubscriber subscriber = session.createSubscriber(topic);
            subscriber.setMessageListener(this);
            topicConnection.start();
            synchronized (this) {
                while (!messageReceived) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                    }
                }
            }
        } catch (JMSException e) {
            System.out.println("Can not subscribe." + e);
        }
    }
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            try {
                System.out.println("output = " + textMessage.getText());
                synchronized (this) {
                    messageReceived = true;
                }
            } catch (JMSException e) {
                System.out.println("error at getting text out of received message. = " + e);
            }
        } else if (message instanceof MapMessage) {
            try {
                Enumeration enumeration = ((MapMessage) message).getMapNames();
                for (; enumeration.hasMoreElements(); ) {
                    System.out.println(((MapMessage) message).getString((String) enumeration.nextElement()));
                }
            } catch (JMSException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Received message is not a text/map message.");
        }
    }
}
