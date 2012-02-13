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

/**
 *
 */

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.engine.AxisConfigurator;

/**
 * This class is used for holding the ConfigurationContext object used on the WSAS server side
 */
public class WSO2ConfigurationContextFactory {

    private static ConfigurationContext configCtx;

    /**
     * Any server side class can get the currently used ConfigurationContext using this method.
     * If the configCtx is null, and new one is created and returned
     *
     * @param configurator The Axis2 Configurator
     * @return The current server side ConfigurationContext
     * @throws AxisFault If ConfigurationContext creation fails
     */
    public static ConfigurationContext getConfigurationContext(AxisConfigurator configurator)
            throws AxisFault {
        if (configCtx == null) {
            configCtx = ConfigurationContextFactory.
                    createConfigurationContext(configurator);
        }
        return configCtx;
    }

    /**
     * Create a new ConfigurationContext for this WSAS server instance
     *
     * @param configurator The Axis2 Configurator
     * @return The current server side ConfigurationContext
     * @throws AxisFault If ConfigurationContext creation fails
     */
    public static ConfigurationContext
    createNewConfigurationContext(AxisConfigurator configurator) throws AxisFault {
        configCtx = ConfigurationContextFactory.
                createConfigurationContext(configurator);
        return configCtx;
    }
}
