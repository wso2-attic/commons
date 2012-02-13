/*
 * Copyright (c) 2006, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.caching;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.description.AxisDescription;
import org.apache.axis2.description.AxisModule;
import org.apache.axis2.description.PolicySubject;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.modules.Module;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.neethi.Assertion;
import org.apache.neethi.Policy;
import org.wso2.caching.policy.CachingPolicyProcessor;

public class CachingModule implements Module {

    /**
     * This will hold the log variable for the log4j appender
     */
    Log log = LogFactory.getLog(CachingModule.class);

    /**
     * This will hold the name of the module as a String
     */
    public String moduleName;

    /**
     * This method will be executed in order to initialize the caching module and this
     * will set the CacheManager object to the ConfigurationContext and also populates the
     * CacheConfiguration according to the cache policy
     *
     * @param configContext - ConfigurationContext for the cache objects to be added
     * @param module        - AxisModule to be initialized
     * @throws AxisFault if an error occured in the process of initializing
     */
    public void init(ConfigurationContext configContext, AxisModule module) throws AxisFault {

        this.moduleName = module.getName();
        CacheConfiguration cacheConfig = null;
        CachingObserver cachingObserver = new CachingObserver();
        AxisConfiguration axisConfiguration = configContext.getAxisConfiguration();
        axisConfiguration.addObservers(cachingObserver);

        PolicySubject policySubject = module.getPolicySubject();

        if (policySubject != null) {
            try {
                cacheConfig = CachingPolicyProcessor.processCachingPolicy(policySubject);
            } catch (CachingException e) {
                handleException("Unable to initialize the caching module : " +
                        "Error in processing caching policy", e);
            }
        }

        // it is a must to have a gloabl configuration
        if (cacheConfig == null) {
            if (log.isDebugEnabled()) {
                log.debug("Using the default initializer for the CacheConfiguration");
            }
            cacheConfig = new CacheConfiguration();
        }

        configContext.getAxisConfiguration().addParameter(
                CachingConstants.CACHE_CONFIGURATION, cacheConfig);
        configContext.setNonReplicableProperty(CachingConstants.CACHE_MANAGER, new CacheManager());
    }

    /**
     * @param axisDescription
     * @throws AxisFault
     */
    public void engageNotify(AxisDescription axisDescription) throws AxisFault {
        CachingEngageUtils.enguage(axisDescription);
    }

    /**
     * @param assertion
     * @return
     */
    public boolean canSupportAssertion(Assertion assertion) {
        // TODO
        return false;
    }

    /**
     * @param policy
     * @param axisDescription
     * @throws AxisFault
     */
    public void applyPolicy(Policy policy, AxisDescription axisDescription) throws AxisFault {
        // TODO
    }

    /**
     * @param configurationContext
     * @throws AxisFault
     */
    public void shutdown(ConfigurationContext configurationContext) throws AxisFault {
        configurationContext.removePropertyNonReplicable(CachingConstants.CACHE_MANAGER);
    }

    /**
     * This is a private method to be called for handling exception and
     * loggin the debug information
     *
     * @param message - String message of the exception
     * @param cause   - Throwable cause of the exception
     * @throws AxisFault wrapping the exception encountered, with the passed message
     */
    private void handleException(String message, Throwable cause) throws AxisFault {

        if (log.isDebugEnabled()) {
            log.debug(message, cause);
        }
        throw new AxisFault(message, cause);
    }

    private void handleException(String message) throws AxisFault {

        if (log.isDebugEnabled()) {
            log.debug(message);
        }
        throw new AxisFault(message);
    }
}
