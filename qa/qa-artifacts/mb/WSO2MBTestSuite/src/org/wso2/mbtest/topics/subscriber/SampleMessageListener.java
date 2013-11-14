package org.wso2.mbtest.topics.subscriber;


import javax.jms.*;

public class SampleMessageListener implements MessageListener {
    private TopicConnection topicConnection;
    private TopicSession topicSession;
    private TopicSubscriber topicSubscriber;
    private int delay = 0;
    private int messageCount = 0;
    private int currentMsgCount = 0;
    private String subscriptionId = "";
    public SampleMessageListener(TopicConnection topicConnection,
                                 TopicSession topicSession,
                                 TopicSubscriber topicSubscriber, int delay, int messageCount, String subscriptionId) {
        this.topicConnection = topicConnection;
        this.topicSession = topicSession;
        this.topicSubscriber = topicSubscriber;
        this.delay = delay;
        this.messageCount = messageCount;
        this.subscriptionId = subscriptionId;
    }
    public void onMessage(Message message) {
        TextMessage receivedMessage = (TextMessage) message;
        try {
            System.out.println(currentMsgCount+". Got the message ==> " + receivedMessage.getText());
            currentMsgCount++;
            if(currentMsgCount == messageCount){
                stop();
            }
            if(delay != 0) {
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    //silently ignore
                }
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
    public void close() {
        try {
            System.out.println("unSubscribing Subscriber");
            this.topicSession.unsubscribe(subscriptionId);
            
            this.topicSession.close();
            this.topicConnection.close();
            
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
    public void stop() {
        try {
            System.out.println("closing Subscriber");
            topicSubscriber.close();
            this.topicConnection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}

