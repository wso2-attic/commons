package org.wso2.pwprovider;
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


import org.apache.axiom.om.OMElement;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

/**
 * <passwordManager>
 * <protectedTokens>Security.KeyStore.Password</protectedTokens>
 * <passwordProvider>org.wso2.carbon.utils.security.DefaultPasswordProvider</passwordProvider>
 * </passwordManager>
 * Creates Password Managers
 */
public class PasswordManagerFactory {

    public static PasswordManager create(OMElement config, boolean isCapLetter) {

        PasswordManager passwordManager = new PasswordManager();
        QName passwordProviderQName;
        QName isEncryptedQName;
        QName protectedTokensQName;
        QName passwordManagerQName;

        QName parentQName = config.getQName();
        String nsURI = parentQName != null ? parentQName.getNamespaceURI() : XMLConstants.NULL_NS_URI;
        String nsPrefix = parentQName != null ? parentQName.getPrefix() : XMLConstants.DEFAULT_NS_PREFIX;

        if (!isCapLetter) {
            passwordManagerQName = new QName(nsURI, "passwordManager", nsPrefix);
            OMElement child = config.getFirstChildWithName(passwordManagerQName);
            if (child == null) {
                passwordManagerQName = new QName(nsURI, "PasswordManager", nsPrefix);
                isCapLetter = !isCapLetter;
            }
        } else {
            passwordManagerQName = new QName(nsURI, "PasswordManager", nsPrefix);
            OMElement child = config.getFirstChildWithName(passwordManagerQName);
            if (child == null) {
                passwordManagerQName = new QName(nsURI, "passwordManager", nsPrefix);
                isCapLetter = !isCapLetter;
            }
        }

        if (!isCapLetter) {
            passwordProviderQName = new QName(nsURI, "passwordProvider", nsPrefix);
            isEncryptedQName = new QName(nsURI, "isPasswordEncrypted", nsPrefix);
            protectedTokensQName = new QName(nsURI, "protectedTokens", nsPrefix);
        } else {
            passwordProviderQName = new QName(nsURI, "PasswordProvider", nsPrefix);
            isEncryptedQName = new QName(nsURI, "IsPasswordEncrypted", nsPrefix);
            protectedTokensQName = new QName(nsURI, "ProtectedTokens", nsPrefix);
        }

        OMElement child = config.getFirstChildWithName(passwordManagerQName);
        if (child == null) {
            return passwordManager;
        }
        OMElement passwordProviderElement = child.getFirstChildWithName(passwordProviderQName);

        Properties properties = new Properties();
        if (passwordProviderElement != null) {
            String passwordProvider = passwordProviderElement.getText();
            properties.put(PWConstants.PASSWORD_PROVIDER, passwordProvider);
        }

        OMElement isEncryptedElement = child.getFirstChildWithName(isEncryptedQName);
        if (isEncryptedElement != null) {
            String isEncrypted = isEncryptedElement.getText();
            if (isEncrypted == null || "".equals(isEncrypted)) {
                isEncrypted = "false";
            }
            properties.put(PWConstants.PASSWROD_ENCRYPTED, isEncrypted);
        }

        if (!properties.isEmpty() && !passwordManager.isInitialized()) {
            passwordManager.init(properties);
        }
        if (passwordManager.isInitialized()) {
            OMElement protectedTokensElement = child.getFirstChildWithName(protectedTokensQName);
            if (protectedTokensElement != null) {
                String value = protectedTokensElement.getText();
                if (value != null && value.trim().length() > 0) {
                    ArrayList<String> protectedTokens = new ArrayList<String>(Arrays
                            .asList(value.split(",")));
                    for (String token : protectedTokens) {
                        passwordManager.addProtectedToken(token);
                    }
                }
            }
        }
        return passwordManager;
    }

    public static PasswordManager create(Properties properties, String prefix) {
        PasswordManager passwordManager = new PasswordManager();
        Properties passManProperties = new Properties();
        String passwordProvider = properties.getProperty(prefix + ".passwordProvider");
        if (passwordProvider != null && !"".equals(passwordProvider.trim())) {
            passManProperties.put(PWConstants.PASSWORD_PROVIDER, passwordProvider.trim());
        }
        if (!passManProperties.isEmpty() && !passwordManager.isInitialized()) {
            passwordManager.init(passManProperties);
        }
        if (passwordManager.isInitialized()) {
            String protectedTokens = properties.getProperty(prefix + ".protectedTokens");
            if (protectedTokens != null && !"".equals(protectedTokens.trim())) {
                ArrayList<String> tokens = new ArrayList<String>(Arrays
                        .asList(protectedTokens.split(",")));
                for (String token : tokens) {
                    passwordManager.addProtectedToken(token);
                }
            }
        }
        return passwordManager;
    }

    public static PasswordManager create(NamedNodeMap namedNodeMap) {
        PasswordManager passwordManager = new PasswordManager();
        Properties properties = new Properties();
        Node passwordProviderAttrib = namedNodeMap.getNamedItem("passwordProvider");
        if (passwordProviderAttrib != null) {
            String passwordProvider = passwordProviderAttrib.getNodeValue();
            if (passwordProvider != null && passwordProvider.trim().length() > 0) {
                properties.put(PWConstants.PASSWORD_PROVIDER, passwordProvider.trim());
            }
        }

        Node isPasswordEncryptedAttrib = namedNodeMap.getNamedItem("isPasswordEncrypted");
        if (isPasswordEncryptedAttrib != null) {
            String isPasswordEncrypted = isPasswordEncryptedAttrib.getNodeValue();
            if (isPasswordEncrypted != null && isPasswordEncrypted.trim().length() > 0) {
                properties.put(PWConstants.IS_PASSWROD_ENCRYPTED,
                        isPasswordEncrypted.trim());
            }
        }

        if (!properties.isEmpty() && !passwordManager.isInitialized()) {
            passwordManager.init(properties);
        }

        Node protectedTokensAttrib = namedNodeMap.getNamedItem("protectedTokens");
        ArrayList<String> protectedTokenList;
        if (protectedTokensAttrib != null) {
            String protectedTokens = protectedTokensAttrib.getNodeValue();
            if (protectedTokens != null && protectedTokens.trim().length() > 0) {
                protectedTokenList = new ArrayList<String>(Arrays.asList(protectedTokens
                        .split(",")));
                for (String token : protectedTokenList) {
                    if (token != null && !"".equals(token)) {
                        passwordManager.addProtectedToken(token);
                    }
                }
            }
        }
        return passwordManager;
    }
}
