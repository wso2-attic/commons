/*
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
package org.wso2.siddhi.core.util;

import java.io.Serializable;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class SchedulerQueue<T> implements Serializable {
    private volatile AtomicBoolean isScheduledForDispatching = new AtomicBoolean(false);

//    private final ReentrantLock takeLock = new ReentrantLock();
//
//    private final ReentrantLock putLock = new ReentrantLock();


    private ConcurrentLinkedQueue<T> queue = new ConcurrentLinkedQueue<T>();

    public synchronized boolean put(T t) {
        queue.add(t);
        return isScheduledForDispatching.compareAndSet(false, true);
    }


    public synchronized T poll() {
//        takeLock.lock();
//        try {
        T t = queue.poll();
        if (t == null) {
            isScheduledForDispatching.set(false);
            t = queue.poll();
            if (t == null) {
                return null;
            } else {
                isScheduledForDispatching.set(true);
                return t;
            }

        }
        return t;
//        } finally {
//            takeLock.unlock();
//        }


    }


//    public synchronized T take() throws InterruptedException {
//        return linkedBlockingQueue.take();
//    }

    public synchronized T peek() {
//        takeLock.lock();
//        try {
            T t = queue.peek();
            if (t == null) {
                isScheduledForDispatching.set(false);
                t = queue.peek();
                if (t == null) {
                    return null;
                } else {
                    isScheduledForDispatching.set(true);
                    return t;
                }
            }
            return t;
//        } finally {
//            takeLock.unlock();
//        }


    }

    public Iterator<T> iterator() {
        return queue.iterator();
    }

}
