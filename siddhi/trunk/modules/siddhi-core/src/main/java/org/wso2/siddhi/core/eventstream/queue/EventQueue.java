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
import java.util.concurrent.TimeUnit;


public interface EventQueue extends Collection<Event> {

    public boolean isEmpty();

    public void put(Event event) throws InterruptedException;

    public boolean add(Event event);

    public Event take() throws InterruptedException;

    public Event poll(long l, TimeUnit timeUnit) throws InterruptedException;

    public int size();

    public boolean offer(Event event);

    public Event poll();

    public Event peek();

    public boolean remove(Event event);

    public Iterator<Event> iterator();

}
