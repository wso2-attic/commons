/*                                                                             
 * Copyright 2004,2005 The Apache Software Foundation.                         
 *                                                                             
 * Licensed under the Apache License, Version 2.0 (the "License");             
 * you may not use this file except in compliance with the License.            
 * You may obtain a copy of the License at                                     
 *                                                                             
 *      http://www.apache.org/licenses/LICENSE-2.0                             
 *                                                                             
 * Unless required by applicable law or agreed to in writing, software         
 * distributed under the License is distributed on an "AS IS" BASIS,           
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.    
 * See the License for the specific language governing permissions and         
 * limitations under the License.                                              
 */
package org.wso2.utils;

import org.apache.axis2.Constants;

import java.net.URL;
import java.net.MalformedURLException;

/**
 *
 */
public class WSO2Utils {

    /**
     * Check whther the specified Strin corresponds to a URL
     *
     * @param location The String to be checked
     * @return true - if <code>location</code> is a URL, false - otherwise
     */
    public static boolean isURL(String location) {
        try {
            new URL(location);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    public static String getAxis2Xml() {
        String axis2XML = ServerConfiguration.getInstance().
                getFirstProperty("Axis2Config.ConfigurationFile");
        if (axis2XML == null) {
            axis2XML = System.getProperty(Constants.AXIS2_CONF);
        }
        return axis2XML;
    }
}
