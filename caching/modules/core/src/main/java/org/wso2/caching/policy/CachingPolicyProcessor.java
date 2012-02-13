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

package org.wso2.caching.policy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.neethi.All;
import org.apache.neethi.ExactlyOne;
import org.apache.neethi.Policy;
import org.apache.neethi.PolicyEngine;
import org.apache.neethi.builders.xml.XmlPrimtiveAssertion;
import org.apache.axis2.description.PolicySubject;
import org.wso2.caching.CacheConfiguration;
import org.wso2.caching.CachingConstants;
import org.wso2.caching.CachingException;
import org.wso2.caching.digest.DigestGenerator;

import javax.xml.namespace.QName;
import java.util.Iterator;
import java.util.List;
import java.util.Collection;

public class CachingPolicyProcessor {

    /**
     * This will hold the log variable for the log4j appender
     */
    private static Log log = LogFactory.getLog(CachingPolicyProcessor.class);

    /**
     * This static method will be use to process the caching policy when the module is initialized
     * and get the CacheManager object initialized according to the policy
     *
     * @param policySubject- Policy object from the EffectivePolicy
     * @return CacheManager object initialized and returned
     * @throws org.wso2.caching.CachingException
     *          - If an error occured in processing the policy
     *          or if there is a problem in loading the DigestGenerator
     */
    public static CacheConfiguration processCachingPolicy(PolicySubject policySubject) throws CachingException {

        if (policySubject.getAttachedPolicyComponents().size() != 0) {
            CacheConfiguration cacheConfig = new CacheConfiguration();
            Collection topLevelAssertionList = policySubject.getAttachedPolicyComponents();
            handlePolicyComponents(cacheConfig, topLevelAssertionList);
            return cacheConfig;
        } else {
            return null;
        }
    }

    private static void handlePolicyComponents(CacheConfiguration cacheConfig,
                                               Collection topLevelAssertionList)
            throws CachingException {
        for (Iterator topLevelAssertionsIterator
                = topLevelAssertionList.iterator(); topLevelAssertionsIterator.hasNext();) {

            Object topLevelAssertionObject = topLevelAssertionsIterator.next();

            if (topLevelAssertionObject instanceof Policy) {
                Policy policy = (Policy) topLevelAssertionObject;
                Collection policyComponents = policy.getPolicyComponents();
                handlePolicyComponents(cacheConfig, policyComponents);
            } else if (topLevelAssertionObject instanceof ExactlyOne) {
                ExactlyOne eo = (ExactlyOne) topLevelAssertionObject;
                handlePolicyComponents(cacheConfig, eo.getPolicyComponents());
            } else if (topLevelAssertionObject instanceof All) {
                All all = (All) topLevelAssertionObject;
                handlePolicyComponents(cacheConfig, all.getPolicyComponents());
            } else if (topLevelAssertionObject instanceof XmlPrimtiveAssertion) {
                // Validating the policy

                XmlPrimtiveAssertion topLevelXmlPrimtiveAssertion
                        = (XmlPrimtiveAssertion) topLevelAssertionObject;
                QName qName = topLevelXmlPrimtiveAssertion.getName();
                // validating the Caching assertion
                if (qName.equals(CachingConstants.CACHING_ASSERTION_QNAME)) {

                    Policy cachingPolicyComponent
                            = PolicyEngine.getPolicy(topLevelXmlPrimtiveAssertion.getValue());
                    List assertionList = cachingPolicyComponent.getPolicyComponents();
                    for (Iterator configDataAssertionIterator = assertionList.iterator();
                         configDataAssertionIterator.hasNext();) {

                        Object aConfigDataAssertionObject = configDataAssertionIterator.next();
                        // Validating the caching policy
                        if (aConfigDataAssertionObject instanceof Policy) {

                            long expireTime;
                            DigestGenerator digestGenerator;
                            int maxMessageSize;
                            int maxCacheSize;

                            Policy cachingPolicy = (Policy) aConfigDataAssertionObject;
                            List childAssertionsList = cachingPolicy.getPolicyComponents();
                            for (Iterator childAssertionSIterator = childAssertionsList
                                    .iterator(); childAssertionSIterator.hasNext();) {

                                Object configData = childAssertionSIterator.next();
                                if (configData instanceof All) {

                                    All all = (All) configData;
                                    List configDataList = all.getPolicyComponents();
                                    for (Iterator configIterator = configDataList
                                            .iterator(); configIterator.hasNext();) {

                                        Object configDtaObject = configIterator.next();
                                        if (configDtaObject instanceof XmlPrimtiveAssertion) {

                                            XmlPrimtiveAssertion cachingPrimtiveAssertion
                                                    = (XmlPrimtiveAssertion) configDtaObject;
                                            //todo : break the processing
                                            // Is Identifier specified?
                                            if (cachingPrimtiveAssertion.getName().equals(
                                                    CachingConstants.CACHING_XML_IDENTIFIER_QNAME)) {

                                                String value
                                                        = cachingPrimtiveAssertion.getValue()
                                                        .getText();
                                                try {

                                                    // Loading the class using Reflection
                                                    digestGenerator = (DigestGenerator)
                                                            Class.forName(value).newInstance();
                                                    cacheConfig.setDigestGenerator(digestGenerator);

                                                } catch (ClassNotFoundException e) {

                                                    handleException("Unable to find the " +
                                                            "DigestGenerator implementation \"" +
                                                            value + "\"", e);

                                                } catch (IllegalAccessException e) {

                                                    handleException("Unable to load the " +
                                                            "DigestGenerator implementation \"" +
                                                            value + "\"", e);

                                                } catch (InstantiationException e) {

                                                    handleException("Unable to instantiate the " +
                                                            "DigestGenerator implementation \"" +
                                                            value + "\"", e);

                                                }
                                            }

                                            // Is expiration time specified?
                                            if (cachingPrimtiveAssertion.getName().equals(
                                                    CachingConstants.CACHE_EXPIRATION_TIME_QNAME) &&
                                                    cachingPrimtiveAssertion.getValue() != null) {

                                                expireTime = Long.parseLong(
                                                        cachingPrimtiveAssertion
                                                                .getValue().getText());
                                                cacheConfig.setTimeout(expireTime);
                                            }

                                            // Is max message size specified?
                                            if (cachingPrimtiveAssertion.getName().equals(
                                                    CachingConstants.MAX_MESSAGE_SIZE_QNAME) &&
                                                    cachingPrimtiveAssertion.getValue() != null) {

                                                maxMessageSize = Integer.parseInt(
                                                        cachingPrimtiveAssertion
                                                                .getValue().getText());
                                                cacheConfig.setMaxMessageSize(maxMessageSize);
                                            }

                                            // Is max cache size specified?
                                            if (cachingPrimtiveAssertion.getName().equals(
                                                    CachingConstants.MAX_CACHE_SIZE_QNAME) &&
                                                    cachingPrimtiveAssertion.getValue() != null) {

                                                maxCacheSize = Integer.parseInt(
                                                        cachingPrimtiveAssertion
                                                                .getValue().getText());
                                                cacheConfig.setMaxCacheSize(maxCacheSize);
                                            }

                                        } else {
                                            // invalid caching policy
                                            handleException("Unexpected caching policy " +
                                                    "assertion for the caching module");
                                        }
                                    }
                                } else {
                                    // caching policy All not found
                                    handleException("Unexpected caching " +
                                            "policy, \"wsp:All\" expected");
                                }
                            }
                        } else {
                            // handle policy
                        }
                    }
                }
            } else {
                // handle top level
            }
        }
    }

    /**
     * Private method to be used by the above static method for exception handling
     *
     * @param message - String message of the Exception
     * @param cause   - Throwable cause of the exception
     * @throws CachingException with the provided mesage and cause
     */
    private static void handleException(String message, Throwable cause) throws CachingException {

        if (log.isDebugEnabled()) {
            log.debug(message, cause);
        }
        throw new CachingException(message, cause);
    }

    /**
     * Private method to be used for the exception handling
     *
     * @param message - String message of the exception
     * @throws CachingException with the provided message
     */
    private static void handleException(String message) throws CachingException {

        if (log.isDebugEnabled()) {
            log.debug(message);
        }
        throw new CachingException(message);
    }
}
