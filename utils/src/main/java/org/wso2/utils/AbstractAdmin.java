/*
 * Copyright 2005-2007 WSO2, Inc. (http://wso2.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.utils;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.engine.AxisConfiguration;
import org.wso2.utils.i18n.Messages;


/**
 * Parent class for all admin services
 */
public abstract class AbstractAdmin {

    protected AbstractAdmin() {
    }

    public AxisConfiguration getAxisConfig() {
        try {
            return WSO2ConfigurationContextFactory.
                    getConfigurationContext(ServerConfigurator.getInstance()).getAxisConfiguration();
        } catch (AxisFault axisFault) {
            throw new RuntimeException(Messages.getMessage("InvalidAxisConfiguration"),
                                       axisFault);
        }
    }

    public ConfigurationContext getConfigContext() {
        try {
            return WSO2ConfigurationContextFactory.
                    getConfigurationContext(ServerConfigurator.getInstance());
        } catch (AxisFault axisFault) {
            throw new RuntimeException(Messages.getMessage("InvalidAxisConfiguration"),
                                       axisFault);
        }
    }
}
