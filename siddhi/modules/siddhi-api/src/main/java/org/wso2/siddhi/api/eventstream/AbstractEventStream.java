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
package org.wso2.siddhi.api.eventstream;

import org.apache.log4j.Logger;
import org.wso2.siddhi.api.exception.InvalidEventAttributeNameException;

import java.util.Arrays;

/**
 * AbstractEventStream class is an implementation of the EventStream interface.
 * Contains the basic functionality of the EventStream.
 */
public abstract class AbstractEventStream implements EventStream {

    private String streamId;
    private String[] names;
    private Class<?>[] types;
//    private long window = -1;
//    private WindowType windowType = WindowType.NONE;

    //std:unique(unique_expression [, unique_expression ...])
//    private StandardView stdViewType = StandardView.NONE;
//    private List<String> stdViewExprArr;

    Logger log = Logger.getLogger(org.wso2.siddhi.api.eventstream.EventStream.class);

    public AbstractEventStream(String streamId, String[] names, Class<?>[] types) {
        if (streamId == null) {
            throw new NullPointerException(
                    "streamId cannot be null and it should have a unique value");
        }
        setSchema(names, types);
        this.streamId = streamId;

    }

    public AbstractEventStream(String streamId) {
        this.streamId = streamId;

    }

    public int size() {
        return types.length;
    }

    public Class<?> getNthAttributeType(int i) {
        return types[i];
    }

    public Class<?> getTypeForName(String name) {
        return types[getAttributePositionForName(name)];
    }

    public String getNthAttributeName(int i) {
        return names[i];
    }

    @Override
    public int getAttributePositionForName(String name) throws InvalidEventAttributeNameException {

        for (int i = 0; i < names.length; i++) {
            if (names[i].equals(name)) {
                return i;
            }
        }
        throw new InvalidEventAttributeNameException("wrong attribute name:" + name);

    }


    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    public String[] getNames() {
        return names;
    }

    public Class<?>[] getTypes() {
        return types;
    }

    /**
     * Sets the output schema of the output events
     *
     * @param names array of attribute names
     * @param types array of attribute class types
     */
    public void setSchema(String[] names, Class<?>[] types) {
        if (names == null && types == null) {
            this.types = new Class<?>[0];
            this.names = new String[0];
        } else if (names != null && types != null) {
            if (names.length != types.length) {
                throw new IllegalArgumentException(
                        "Number of names=" + names.length +
                                " are expected to be equal to their respective number of objects=" +
                                types.length + ", here they are not equal!");
            }
            this.types = types;
            this.names = names;
        } else {
            throw new NullPointerException(
                    "names or types cannot be null");
        }
    }

    @Override
    public String toString() {
        return "EventStream{" +
               "streamId='" + streamId + '\'' +
               ", names=" + (names == null ? null : Arrays.asList(names)) +
               ", types=" + (types == null ? null : Arrays.asList(types)) +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AbstractEventStream that = (AbstractEventStream) o;

        if (!Arrays.equals(names, that.names)) {
            return false;
        }
        if (!streamId.equals(that.streamId)) {
            return false;
        }
        if (!Arrays.equals(types, that.types)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = streamId.hashCode();
        result = 31 * result + (names != null ? Arrays.hashCode(names) : 0);
        result = 31 * result + (types != null ? Arrays.hashCode(types) : 0);
        return result;
    }
    //        windowType = WindowType.TIME;
//        window = timeInMills;
//    }
//
//    public void setLengthWindow(int length) {
//        windowType = WindowType.LENGTH;
//        window = length;
//    }
//
//    public void setBatchLengthWindow(int length) {
//        windowType = WindowType.LENGTH_BATCH;
//        window = length;
//    }
//
//    public void setBatchTimeWindow(int length) {
//        windowType = WindowType.TIME_BATCH;
//        window = length;
//    }
//
//    public long getWindow() {
//        return window;
//    }
//
//    public WindowType getWindowType() {
//        return windowType;
//    }
//
//    /**
//     * Standard view set
//     *
//     * @param standardViewName
//     * @param att1
//     * @param attArray
//     */
//    public void setStandardView(StandardView standardViewName, String att1, String... attArray) {
//        if (stdViewExprArr != null) {
//            log.warn("Only one standard view can be set per Event Stream. Previous view is discarded.");
//        }
//
//        this.stdViewType = standardViewName;
//        stdViewExprArr = new ArrayList<String>();
//
//        stdViewExprArr.add(att1);
//        stdViewExprArr.addAll(Arrays.asList(attArray));
//    }
//
//    /**
//     * get the type of the standard view. See StandardView.
//     *
//     * @return StandardView type
//     */
//    public StandardView getStandardViewType() {
//        return stdViewType;
//    }
//
//    /**
//     *
//     * @return
//     */
//    public List<String> getStandardViewExpressionArray() {
//        return stdViewExprArr;       
//    }
}
