/*
*  Copyright (c) WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.balana;

/**
 * Utility class
 */
public class Utils {

    public static String prepareXPathForDefaultNs(String xpath){

        StringBuffer buffer = new StringBuffer();
        buffer.append("/");
        String[] splitArray = xpath.split("/");
        for(String s : splitArray){
            if(s != null && s.trim().length() > 0){
                buffer.append("/ns:").append(s);
            }
        }

        return buffer.toString();
    }
}
