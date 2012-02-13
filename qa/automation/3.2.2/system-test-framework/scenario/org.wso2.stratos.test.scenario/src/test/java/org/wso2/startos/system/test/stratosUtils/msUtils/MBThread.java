package org.wso2.startos.system.test.stratosUtils.msUtils;

import javax.jms.JMSException;
import javax.naming.NamingException;
import java.util.Queue;

import junit.framework.Assert;

public class MBThread extends Thread {

    private final Queue<String> results;

    public MBThread(Queue<String> results) {
        this.results = results;

    }

    public void run() {

        TopicSubscriber topicSubscriber = new TopicSubscriber();
        try {
            results.add(topicSubscriber.subscribe());
        } catch (NamingException e) {
            Assert.fail("NamingException exception occurred while subscribing to queue: " + e.getMessage());
        } catch (JMSException e) {
            Assert.fail("JMSException exception occurred while subscribing to queue: " + e.getMessage());
        }
    }
}

