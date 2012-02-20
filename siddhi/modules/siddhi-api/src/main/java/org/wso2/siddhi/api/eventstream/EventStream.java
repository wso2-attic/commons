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

/**
 * Interface for EventStream
 * EventStream should implement this interface.
 */
public interface EventStream {

    /**
     * @return size of the event stream (no of attributes)
     */
    public int size();

    /**
     * get the class type of a particular attribute by giving the attribute position
     *
     * @param i position of the attribute
     * @return the class type of the attribute
     */
    public Class<?> getNthAttributeType(int i);

    /**
     * get the class type of a particular attribute by giving the attribute name
     *
     * @param name name of the attribute
     * @return the class type of the attribute
     */
    public Class<?> getTypeForName(String name);

    /**
     * get the name of the attribute of the given position
     *
     * @param i position of the attribute
     * @return the name of the attribute
     */
    public String getNthAttributeName(int i);

    /**
     * get the position of the attribute from the name
     *
     * @param name of the attribute
     * @return the position of the attribute
     */
    public int getAttributePositionForName(String name);

    /**
     * get the id of the stream
     *
     * @return streamId
     */
    public String getStreamId();

    /**
     * assigning the stream id
     *
     * @param streamId stream id
     */
    public void setStreamId(String streamId);

    /**
     * get all the attribute names
     *
     * @return a string array of attribute names
     */
    public String[] getNames();

    /**
     * get all the class types of attributes
     *
     * @return a array of class types
     */
    public Class<?>[] getTypes();

    /**
     * set the schema of the event stream
     *
     * @param names array of attribute names
     * @param types array of attribute class types
     */
    public void setSchema(String[] names, Class<?>[] types);

//    /**
//     * set time window for the event stream
//     *
//     * @param timeInMills time window in milli seconds
//     */
//    public void setTimeWindow(long timeInMills);
//
//    /**
//     * set the length window for the event stream
//     *
//     * @param length length in no of events
//     */
//    public void setLengthWindow(int length);
//
//    /**
//     * get time window or length window
//     *
//     * @return the window
//     */
//    public long getWindow();
//
//    /**
//     * get the the type of the window (whether a time window or a length window)
//     *
//     * @return window type
//     */
//    public WindowType getWindowType();
//
//
//    /**
//     * get the type of the standard view. See StandardView.
//     *
//     * @return StandardView type
//     */
//    public StandardView getStandardViewType();
//
//    /**
//     * @return
//     */
//    public List<String> getStandardViewExpressionArray();


}
