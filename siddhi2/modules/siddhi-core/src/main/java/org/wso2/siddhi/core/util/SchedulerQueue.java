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

import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

public class SchedulerQueue<T> {
    private volatile boolean isScheduledForDispatching = false;

    private final ReentrantLock takeLock = new ReentrantLock();

    private final ReentrantLock putLock = new ReentrantLock();


    private LinkedBlockingQueue<T> queue = new LinkedBlockingQueue<T>();

    public synchronized boolean put(T t) throws InterruptedException {
        putLock.lock();
        try {
            queue.add(t);
            if (!isScheduledForDispatching) {
                isScheduledForDispatching = true;
                return false;
            }
            return true;
        } finally {
            putLock.unlock();
        }

    }


    public synchronized T poll() {
        takeLock.lock();
        try {
            T t = queue.poll();
            if (t == null) {
                isScheduledForDispatching = false;
            }
            return t;
        } finally {
            takeLock.unlock();
        }


    }


//    public synchronized T take() throws InterruptedException {
//        return linkedBlockingQueue.take();
//    }

    public synchronized T peek() {
        takeLock.lock();
        try {
            T t = queue.peek();
            if (t == null) {
                isScheduledForDispatching = false;
            }
            return t;
        } finally {
            takeLock.unlock();
        }
    }

    public Iterator<T> iterator() {
        return queue.iterator();
    }

}
