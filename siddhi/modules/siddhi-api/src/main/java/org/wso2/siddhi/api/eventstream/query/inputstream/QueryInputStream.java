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
package org.wso2.siddhi.api.eventstream.query.inputstream;


import org.wso2.siddhi.api.eventstream.EventStream;
import org.wso2.siddhi.api.eventstream.query.inputstream.property.StandardView;
import org.wso2.siddhi.api.eventstream.query.inputstream.property.WindowType;

public class QueryInputStream {

    private EventStream eventStream;
    private WindowType windowType = WindowType.NONE;
    private int windowValue;
    private StandardView standardView = StandardView.NONE;
    private String viewValue;

    public QueryInputStream(EventStream eventStream) {
        this.eventStream = eventStream;
    }

    public EventStream getEventStream() {
        return eventStream;
    }

    public QueryInputStream setWindow(WindowType windowType, int windowValue) {
        this.windowValue = windowValue;
        this.windowType = windowType;
        return this;
    }

    public WindowType getWindowType() {
        return windowType;
    }

    public int getWindowValue() {
        return windowValue;
    }


    public QueryInputStream setStandardView(StandardView standardView, String viewValue) {
        if(viewValue.contains(".")){
            viewValue=viewValue.split("\\.")[1];
        }
        this.viewValue = viewValue;
        this.standardView = standardView;
        return this;
    }

    public StandardView getStandardView() {
        return standardView;
    }

    public String getStandardViewValue() {
        return viewValue;
    }

}
