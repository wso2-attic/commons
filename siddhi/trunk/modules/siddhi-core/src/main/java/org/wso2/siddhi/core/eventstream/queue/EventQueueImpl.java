/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.siddhi.core.eventstream.queue;

import org.wso2.siddhi.core.event.Event;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * Wrapper for BlockingQueue. Serves the purpose of BlockingQueue + keeping the event streamId.
 */
public class EventQueueImpl implements EventQueue {

    private BlockingQueue<Event> eventQueue =new LinkedBlockingDeque<Event>();

    public boolean isEmpty() {
        return eventQueue.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
       return eventQueue.contains(o);
    }

    public void put(Event event) throws InterruptedException {
         eventQueue.put(event);
    }

    public Event take() throws InterruptedException {
        return eventQueue.take();
    }

    public Event poll(long l, TimeUnit timeUnit) throws InterruptedException {
        return eventQueue.poll(l, timeUnit);
    }

    public int remainingCapacity() {
        return eventQueue.remainingCapacity();
    }

    public int drainTo(Collection collection) {
        return eventQueue.drainTo(collection);
    }

    public int drainTo(Collection collection, int i) {
        return eventQueue.drainTo(collection, i);
    }

    public Iterator<Event> iterator() {
        return eventQueue.iterator();
    }

    @Override
    public Object[] toArray() {
        return eventQueue.toArray();
    }

    @Override
    public <T> T[] toArray(T[] ts) {
        return eventQueue.toArray(ts);
    }

    @Override
    public boolean add(Event event) {
        return eventQueue.add(event);
    }

    @Override
    public boolean remove(Object o) {
        return eventQueue.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> objects) {
        return eventQueue.containsAll(objects);
    }

    @Override
    public boolean addAll(Collection<? extends Event> events) {
        return eventQueue.addAll(events);
    }

    @Override
    public boolean removeAll(Collection<?> objects) {
        return eventQueue.remove(objects);
    }

    @Override
    public boolean retainAll(Collection<?> objects) {
        return eventQueue.retainAll(objects);
    }

    @Override
    public void clear() {
        eventQueue.clear();
    }

    public int size() {
        return eventQueue.size();
    }

    public boolean offer(Event event) {
        return eventQueue.offer(event);
    }

    public Event poll() {
        return eventQueue.poll();
    }

    public Event peek() {
        return eventQueue.peek();
    }

    public boolean remove(Event event){
        return eventQueue.remove(event);
    }
}
