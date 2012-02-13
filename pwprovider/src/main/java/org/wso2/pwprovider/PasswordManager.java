package org.wso2.pwprovider;
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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Properties;

/**
 * Create and initialize PasswordProvider
 * Retrieve password from PasswordProvider
 */
public class PasswordManager {

    private static Log log = LogFactory.getLog(PasswordManager.class);

    private boolean initialized = false;
    private PasswordProvider passwordProvider;
    private final ArrayList<String> protectedTokens = new ArrayList<String>();

    public PasswordManager() {
    }

    /**
     * initialize  SimplePasswordManager with given properties
     *
     * @param properties configuration properties
     */
    public void init(Properties properties) {

        if (initialized) {
            if (log.isDebugEnabled()) {
                log.debug("SimplePasswordManager already has been started.");
            }
            return;
        }

        String provider = getProperty(properties, PWConstants.PASSWORD_PROVIDER, null);

        if (provider != null && !"".equals(provider)) {

            try {
                Class aClass = Thread.currentThread().getContextClassLoader().loadClass(provider);
                Object object = aClass.newInstance();
                if (!(object instanceof PasswordProvider)) {
                    handleException("Invalid class as PasswordProvider : Class Name : " +
                            provider);
                } else {
                    passwordProvider = (PasswordProvider) object;
                }
                passwordProvider.init(properties);

            } catch (ClassNotFoundException e) {
                handleException("A PasswordProvider cannot be found for class name : " +
                        provider, e);
            } catch (IllegalAccessException e) {
                handleException("Error creating a instance from class : " + provider, e);
            } catch (InstantiationException e) {
                handleException("Error creating a instance from class : " + provider, e);
            }
            initialized = true;
        } else {
            log.warn("isPasswordEncrypted has been set to true. But there is " +
                    "no a PasswordProvider has been configured.");
        }

    }

    /**
     * Resolved given password using an instance of a PasswordProvider
     *
     * @param encryptedPassword Encrypted password
     * @return resolved password
     */
    public String resolve(String encryptedPassword) {

        if (encryptedPassword == null || "".equals(encryptedPassword)) {
            if (log.isDebugEnabled()) {
                log.debug("Given Encrypted Password is empty or null. Returning itself");
            }
            return encryptedPassword;
        }
        if (!initialized || passwordProvider == null) {
            if (log.isDebugEnabled()) {
                log.debug("SimplePasswordManager has not been initialized. Returning itself");
            }
            return encryptedPassword;
        }

        return passwordProvider.resolve(encryptedPassword);
    }

    public boolean isInitialized() {
        return initialized;
    }

    /**
     * Helper methods for handle errors.
     *
     * @param msg The error message
     * @param e   Thorwen Exception
     */
    private static void handleException(String msg, Exception e) {
        log.error(msg, e);
        throw new RuntimeException(msg, e);
    }

    /**
     * Helper methods for handle errors.
     *
     * @param msg The error message
     */
    private static void handleException(String msg) {
        log.error(msg);
        throw new RuntimeException(msg);
    }

    /**
     * Helper method to get the value of the property from a given property bag
     *
     * @param properties   The property collection
     * @param name         The name of the property
     * @param defaultValue The default value for the property
     * @return The value of the property if it is found , otherwise , default value
     */
    private static String getProperty(Properties properties, String name, String defaultValue) {

        String result = properties.getProperty(name);
        if ((result == null || result.length() == 0 || "".equals(result)) && defaultValue != null) {
            if (log.isDebugEnabled()) {
                log.debug("The name with ' " + name + " ' cannot be found. " +
                        "Using default value " + defaultValue);
            }
            result = defaultValue;
        }
        if (result != null) {
            return result.trim();
        } else {
            return defaultValue;
        }
    }

    public void addProtectedToken(String token) {
        if (token != null && !"".equals(token)) {
            protectedTokens.add(token.trim());
        }
    }

    public boolean isTokenProtected(String token) {
        return token != null && !"".equals(token) && protectedTokens.contains(token.trim());
    }

    public void shutDown() {
        initialized = false;
        passwordProvider = null;
        protectedTokens.clear();
    }

}
