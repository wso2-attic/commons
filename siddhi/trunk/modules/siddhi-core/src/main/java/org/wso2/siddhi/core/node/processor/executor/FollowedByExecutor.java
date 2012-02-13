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
package org.wso2.siddhi.core.node.processor.executor;

import org.wso2.siddhi.core.event.Event;

import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class FollowedByExecutor implements Executor {

    private Executor executor;          //holds the simple executor for this FollowedByExecutor
    private int state;                 // State of the pattern i.e. 0->1->2
    private Event[] arrivedEvents;
    private FollowedByExecutor nextExecutor;
    private FollowedByExecutor nextEveryExecutor;
    private long expiringTime = 0;
    private String checkingStreamName = null;
    private String[] checkingStreamNameArray = null;
    private boolean isConditionHolds = true;

    private long lifeTime = -1;          // -1 indicates the unlimited life time

    /**
     * Get the life time of the Executor
     *
     * @return life time of the Executor
     */
    public long getLifeTime() {
        return lifeTime;
    }

    /**
     * Set life time of the executor
     *
     * @param lifeTime life time of the executor
     */
    public void setLifeTime(long lifeTime) {
        this.lifeTime = lifeTime;
    }

    /**
     * Check whether the lifetime is set
     *
     * @return whether the lifetime is set
     */
    public boolean isLifeTimeSet() {
        if (lifeTime != -1) {
            return true;
        }
        return false;
    }

    /**
     * Set expiring time of the executor
     *
     * @param currentTime current system time
     */
    public void setExpiringTime(long currentTime) {
        this.expiringTime = currentTime + lifeTime;
    }

    /**
     * Get the simple executor of the Followedby executor
     *
     * @return executor
     */
    public Executor getExecutor() {
        return executor;
    }

    /**
     * Get the state of the executor
     *
     * @return state
     */
    public int getState() {
        return state;
    }

    /**
     * @param state
     * @param executor           simple executor of Followedby Execitor
     * @param checkingStreamName
     */
    public FollowedByExecutor(int state, Executor executor, String checkingStreamName, String[] checkingStreamNameArray) {
        this.executor = executor;
        this.state = state;
        this.checkingStreamName = checkingStreamName;
        this.checkingStreamNameArray = checkingStreamNameArray;
    }

    /**
     * @param state
     * @param executor simple executor of Followedby Execitor
     */
    public FollowedByExecutor(int state, Executor executor) {
        this.executor = executor;
        this.state = state;
        Set streamNameSet = getCheckingStreamNames();
        if (streamNameSet.size() > 1) {
            String[] strings = new String[streamNameSet.size()];
            this.checkingStreamNameArray = getCheckingStreamNames().toArray(strings);
        } else {
            this.checkingStreamName = (String) getCheckingStreamNames().toArray()[0];
        }
    }

    /**
     * @return whether the executor is alive
     */
    public boolean isAlive() {
        if (lifeTime == -1) {
            return true;
        } else if (expiringTime > System.currentTimeMillis()) {
            // System.out.println("Expiring Time: " +expiringTime + " Current Time: " + System.currentTimeMillis());
            return true;
        }
        return false;
    }

    public boolean isConditionHolds() {
        return isConditionHolds;
    }

    /**
     * initialize the arrivedEvents data structure
     */
    public void initArrivedEventSize(int size) {
        arrivedEvents = new Event[size];
    }

    /**
     * Followed by execution
     *
     * @param eventArray The event array for processing
     * @return true/false on whether the events matches the given condition
     */
    @Override
    public boolean execute(Event[] eventArray) {
        if (checkingStreamName != null || checkStreams(eventArray[0])) {
            arrivedEvents[state] = eventArray[0];     //Only one event is parsed to the executor.(only one event available)
            if (executor instanceof NonOccurrenceExecutor) {
                isConditionHolds = ((NonOccurrenceExecutor) executor).executeNonOccurrence(arrivedEvents);
                if (!isConditionHolds) {
                    return true;
                }
            }
            return this.executor.execute(arrivedEvents);
        }
        return false;
    }

    /**
     * Followed by execution
     * Stores the coming events in an array until a match occurs
     *
     * @param event The event for processing
     * @return true/false on whether the events matches the given condition
     */
    @Override
    public boolean execute(Event event) {
        if (checkingStreamName != null || checkStreams(event)) {
            arrivedEvents[state] = event;     //Only one event is parsed to the executor.(only one event available)
            if (executor instanceof NonOccurrenceExecutor) {
                isConditionHolds = ((NonOccurrenceExecutor) executor).executeNonOccurrence(event);
                if (!isConditionHolds) {
                    return true;
                }
            }
            return this.executor.execute(arrivedEvents);
        }
        return false;
    }

    @Override
    public boolean execute(Event[][] eventArray) {
        return false;
    }

    /**
     * Check stream name for nonOccurring conditions that have different streams
     *
     * @param event
     * @return
     */

    private boolean checkStreams(Event event) {
        for (String streamName : checkingStreamNameArray) {
            if (event.getEventStreamId().equals(streamName)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Set the next executor
     *
     * @param followedByExecutor
     */
    public void setNextExecutor(FollowedByExecutor followedByExecutor) {
        this.nextExecutor = followedByExecutor;
    }

    /**
     * @return Next Executor
     */
    public FollowedByExecutor getNextExecutor() {
        return nextExecutor;
    }

    /**
     * @return Next Executor belongs to Every
     */
    public FollowedByExecutor getNextEveryExecutor() {
        return nextEveryExecutor;
    }

    /**
     * Get a new instance of  Next Followed by Executor
     *
     * @return a new instance of Next Followed by Executor
     */
    public FollowedByExecutor getNewNextExecutor() {
        FollowedByExecutor followedByExecutor = null;
        if (nextExecutor != null) {
            followedByExecutor = new FollowedByExecutor(nextExecutor.state, nextExecutor.executor, nextExecutor.checkingStreamName, nextExecutor.checkingStreamNameArray);

            if (nextExecutor.isLifeTimeSet()) {
                followedByExecutor.lifeTime = nextExecutor.getLifeTime();
                followedByExecutor.setExpiringTime(System.currentTimeMillis());
            }

            followedByExecutor.nextEveryExecutor = (nextExecutor.nextEveryExecutor);
            followedByExecutor.nextExecutor = (nextExecutor.nextExecutor);
            Event[] eventArray = new Event[arrivedEvents.length];
            System.arraycopy(arrivedEvents, 0, eventArray, 0, nextExecutor.state + 1);
            followedByExecutor.arrivedEvents = (eventArray);
        }
        return followedByExecutor;
    }

    /**
     * @return Get a new instance of Next Followed by Executor belongs to Every
     */
    public FollowedByExecutor getNewNextEveryExecutor() {
        FollowedByExecutor followedByExecutor = null;
        if (nextEveryExecutor != null) {
            followedByExecutor = new FollowedByExecutor(nextEveryExecutor.state, nextEveryExecutor.executor, nextEveryExecutor.checkingStreamName, nextEveryExecutor.checkingStreamNameArray);
            followedByExecutor.nextEveryExecutor = (nextEveryExecutor.nextEveryExecutor);
            followedByExecutor.nextExecutor = (nextEveryExecutor.nextExecutor);
            Event[] eventArray = new Event[arrivedEvents.length];
            System.arraycopy(arrivedEvents, 0, eventArray, 0, nextEveryExecutor.state + 1);
            followedByExecutor.arrivedEvents = (eventArray);
        }
        return followedByExecutor;
    }

    /**
     * @return Get a new instance of This Executor
     */
    public FollowedByExecutor getNewInstance() {
        FollowedByExecutor followedByExecutor = null;
        followedByExecutor = new FollowedByExecutor(this.state, this.executor, this.checkingStreamName, this.checkingStreamNameArray);
        followedByExecutor.nextEveryExecutor = (this.nextEveryExecutor);
        followedByExecutor.nextExecutor = (this.nextExecutor);
        followedByExecutor.arrivedEvents = new Event[this.arrivedEvents.length];
        return followedByExecutor;
    }

    /**
     * Check whether the next executor belongs to Every exists
     *
     * @return whether the next executor belongs to Every exists
     */
    public boolean isNextEveryExecutorExist() {
        return nextEveryExecutor != null;
    }


    /**
     * Check whether the next executor  exists
     *
     * @return whether the next executor exists
     */
    public boolean isNextExecutorExist() {
        return nextExecutor != null;
    }

    /**
     * Set the next Executor
     *
     * @param nextEveryExecutor next executor to be set to executor
     */
    public void setNextEveryExecutor(FollowedByExecutor nextEveryExecutor) {
        this.nextEveryExecutor = nextEveryExecutor;
    }


    /**
     * Get the stored events
     *
     * @return arrived events
     */
    public Event[] getArrivedEvents() {
        return arrivedEvents;
    }

    /**
     * Store arrived events
     *
     * @param arrivedEvents
     */
    public void setArrivedEvents(Event[] arrivedEvents) {
        this.arrivedEvents = arrivedEvents;
    }

    /**
     * @return checking eventstream names set
     */
    public Set<String> getCheckingStreamNames() {
        return new HashSet<String>(executor.getCheckingStreamNames());
    }

    /**
     * @return checking eventstream name
     */
    public String getCheckingStreamName() {
        return checkingStreamName;
    }
}
