/*
 * Copyright 2004,2005 The Apache Software Foundation.
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
                close();
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
