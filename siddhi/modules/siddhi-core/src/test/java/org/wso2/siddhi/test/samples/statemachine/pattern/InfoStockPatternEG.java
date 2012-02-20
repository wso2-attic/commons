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
package org.wso2.siddhi.test.samples.statemachine.pattern;

import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.event.generator.EventGenerator;

public class InfoStockPatternEG {

    String[] action = new String[]{"buy", "cancel"};
    final EventGenerator eventGeneratorType;
    String streamName;
    long timeStamp =0;

    public InfoStockPatternEG() {
        streamName = "infoStock";
        eventGeneratorType =
                EventGenerator.DefaultFactory.create(streamName,
                        new String[]{"action","timeStamp"},
                        new Class[]{String.class,Long.class}
                );
    }

    String getAction() {
        return action[0];
    }

    public long getTimeStamp() {
        return timeStamp++;
    }

    public Event generateEvent() {
        return eventGeneratorType.createEvent(getAction(),getTimeStamp());
    }

    public Event generateEvent(int i) {
        return eventGeneratorType.createEvent(action[i],getTimeStamp());
    }

    public EventGenerator getEventStream() {
        return eventGeneratorType;
    }
}
