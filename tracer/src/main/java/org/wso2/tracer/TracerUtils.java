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
package org.wso2.tracer;

import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axis2.Constants;
import org.apache.axis2.context.MessageContext;
import org.wso2.utils.xml.XMLPrettyPrinter;

import java.io.InputStream;
import java.io.ByteArrayInputStream;

/**
 * A collection of utility methods
 */
public class TracerUtils {

    /**
     * Get a prettified XML string from the SOAPEnvelope
     *
     * @param env The SOAPEnvelope to be prettified
     *
     * @return prettified XML string from the SOAPEnvelope
     */
    public static String getPrettyString(SOAPEnvelope env, MessageContext msgContext) {
        InputStream xmlIn = new ByteArrayInputStream(env.toString().getBytes());
        String encoding =
                (String) msgContext.getProperty(Constants.Configuration.CHARACTER_SET_ENCODING);
        XMLPrettyPrinter xmlPrettyPrinter = new XMLPrettyPrinter(xmlIn, encoding);
        return xmlPrettyPrinter.xmlFormat();
    }
    
}
