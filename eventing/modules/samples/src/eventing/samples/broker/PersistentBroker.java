/*
 * Copyright 2005-2007 WSO2, Inc. (http://wso2.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eventing.samples.broker;

import org.wso2.eventing.EventSink;
import org.wso2.eventing.Event;
import org.wso2.eventing.impl.Broker;

import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;

/**
 * A store-and-forward broker which deals with transient failures in delivery
 * by retrying.
 */
public class PersistentBroker extends Broker {
    // A Postman which tries delivery every 5 seconds
    Postman postman = new Postman(5000);

    private static final Object lock = new Object();

    /**
     * Quickie timestamped-and-threadsafe log routine.  Messages from multiple threads
     * won't overlap each other.
     *
     * @param message message to print
     */
    public static void print(String message) {
        synchronized (lock) {
            System.out.print(DateFormat.getTimeInstance(DateFormat.LONG).format(new Date()));
            System.out.println(" " + message);
        }
    }

    /**
     * Represents a pending order to redeliver an Event to a particular subscriber.  These
     * get created when initial delivery fails for some reason.
     */
    class DeliveryOrder {
        EventSink sink;
        Event event;
        long creationTime;

        // Default time-to-live is 30 seconds
        long timeToLive = 30000;

        public DeliveryOrder(EventSink sink, Event event) {
            this.sink = sink;
            this.event = event;
            this.creationTime = new Date().getTime();
        }

        public boolean deliver() throws Exception {
            print("Trying to redeliver '" + event.getMessage() + "'");
            try {
                sink.onEvent(event);
            } catch (Exception e) {
                if (new Date().getTime() > creationTime + timeToLive) {
                    // This guy has passed it's expiration date.
                    throw new Exception("TIMED OUT: " + event.getMessage());
                }
                return false;
            }

            return true;
        }
    }

    /**
     * Handle incoming events by sending them to all of my subscribers.  If any deliveries
     * fail, record the failure and try again at whatever periodicity the Postman is set for.
     *
     * In the real world this would persist the event before even attempting delivery.
     * 
     * @param event event to handle
     */
    public void onEvent(Event event) {
        ArrayList deliveryList = new ArrayList(subscribers);

        int i = 0;
        while (i < deliveryList.size()) {
            EventSink sink = (EventSink)deliveryList.get(i);
            try {
                sink.onEvent(event);
            } catch (Exception e) {
                // Couldn't deliver, try again later
                DeliveryOrder order = new DeliveryOrder(sink, event);
                postman.submitOrder(order);
                i++; // Skip it and continue
                continue;
            }

            deliveryList.remove(i);
        }
    }

    /**
     * This is our redelivery thread.  It sleeps until someone wakes it up, and then
     * runs through the queue of pending DeliveryOrders, attempting to redeliver each one.
     * If it reaches the end of the queue and all deliveries were successful, it goes back
     * to waiting for a notification.  Otherwise it sleeps for a bit and tries again.
     */
    class Postman implements Runnable {
        boolean running;
        long periodicity;

        public Postman(long periodicity) {
            this.periodicity = periodicity;
        }

        ArrayList pendingOrders = new ArrayList();

        public synchronized void submitOrder(DeliveryOrder order) {
            if (!running) {
                running = true;
                new Thread(this).start();
            }
            pendingOrders.add(order);
            notify();
        }

        public void run() {
            while (running) {
                // try delivering each order
                int i = 0;
                while (i < pendingOrders.size()) {
                    DeliveryOrder order = (DeliveryOrder)pendingOrders.get(i);
                    try {
                        if (order.deliver()) {
                            pendingOrders.remove(i);
                        } else {
                            i++;
                        }
                    } catch (Exception e) {
                        // Something bad happened while trying to deliver.  This
                        // represents an unrecoverable failure.  Log and dequeue.
                        // (in the real world maybe we'd also write this to a dead-message
                        // area or something similar.)

                        System.out.println(e.getMessage());
                        pendingOrders.remove(i);
                    }
                }

                try {
                    synchronized (this) {
                        if (pendingOrders.isEmpty()) {
                            this.wait();
                        }
                    }
                    Thread.sleep(periodicity);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
