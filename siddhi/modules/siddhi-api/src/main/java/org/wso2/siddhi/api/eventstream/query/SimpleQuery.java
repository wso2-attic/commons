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
package org.wso2.siddhi.api.eventstream.query;

import org.wso2.siddhi.api.OutputDefinition;
import org.wso2.siddhi.api.condition.Condition;
import org.wso2.siddhi.api.eventstream.query.inputstream.QueryInputStream;
import org.wso2.siddhi.api.exception.UnsupportedQueryFormatException;

import java.util.List;

public class SimpleQuery extends Query {

    private QueryInputStream queryInputStream = null;

    /**
     * @param streamId         id of the query (output id)
     * @param outputDefinition definition of the output
     * @param queryInputStream input event stream of the query
     * @param condition        condition of the query
     */
    public SimpleQuery(String streamId, OutputDefinition outputDefinition,
                       QueryInputStream queryInputStream, Condition condition) {
        super(streamId, outputDefinition, condition);
        this.queryInputStream = queryInputStream;
        setSchema(getAttributeNames(), getAttributeClasses());
    }

    /**
     * Determine the classes of the output-stream's attributes
     *
     * @return An array containing the classes of the attributes
     */
    protected Class[] getAttributeClasses() {
        List<String> propertyList = outputDefinition.getPropertyList();
        Class[] classArray = new Class[propertyList.size()];
        if (queryInputStream != null) {   //for normal Query
            for (int i = 0; i < propertyList.size(); i++) {

                String function = propertyList.get(i).split("=")[1];

                if (function.contains("(")) {    //Aggregator //avg(CSEStream.price)
                    String[] funcArr = function.split("\\("); //[0]=functionName, [1]=functionParameters

                    //If the aggregator function is COUNT, then set the output class type as Long
                    if ("COUNT".equals(funcArr[0].replaceAll(" ", "").toUpperCase())) {
                        classArray[i] = java.lang.Long.class;
                    } else if ("".equals(funcArr[0].replaceAll(" ", "").toUpperCase())) {
                        String classTypeString = funcArr[1].split("\\)")[0].replace(" ", "").toUpperCase();
                        if (classTypeString.equals("INT")) {
                            classArray[i] = Integer.class;
                        } else if (classTypeString.equals("LONG")) {
                            classArray[i] = Long.class;
                        } else if (classTypeString.equals("FLOAT")) {
                            classArray[i] = Float.class;
                        } else if (classTypeString.equals("DOUBLE")) {
                            classArray[i] = Double.class;
                        } else {//    if(classTypeString.equals("STRING")){
                            classArray[i] = String.class;
                        }
                    } else {    //For other aggregators
                        classArray[i] = queryInputStream.getEventStream().getTypeForName(
                                funcArr[1].split("\\.")[1].replace(")", "") //ex: price
                        );
                    }
                } else {        //Without an aggregator
                    classArray[i] = queryInputStream.getEventStream().getTypeForName(
                            propertyList.get(i).split("=")[1].split("\\.")[1]
                    );
                }
            }
            return classArray;
        } else {
            throw new UnsupportedQueryFormatException("Unsupported query type");
        }
    }

    /**
     * get the input event eventstream
     *
     * @return the input event eventstream
     */

    public QueryInputStream getQueryInputStream() {
        return queryInputStream;
    }


}
