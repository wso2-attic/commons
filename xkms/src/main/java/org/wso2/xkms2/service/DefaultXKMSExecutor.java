/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
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
package org.wso2.xkms2.service;

import java.util.Properties;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.context.ServiceContext;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.description.Parameter;
import org.wso2.xkms2.ResultType;
import org.wso2.xkms2.XKMSException;
import org.wso2.xkms2.core.XKMSRequestData;
import org.wso2.xkms2.core.XKMSServiceExecutor;

public class DefaultXKMSExecutor implements XKMSServiceExecutor {

    XKMSServerCrypto crypto = null;

    public ResultType execute(XKMSRequestData data,
            MessageContext messageContext) throws XKMSException, AxisFault {

        createXKMSServerCrypto(data);
        ResultType result = crypto.process(data);
        return result;
    }

    public String[] getAssociatedElemenTypes() {
        return new String[] { "RegisterRequest", "ReissueRequest",
                "RecoverRequest", "LocateRequest", "ValidateRequest" };
    }

    public void init(ServiceContext serviceContext) throws AxisFault {
    }

    private void createXKMSServerCrypto(XKMSRequestData data) throws XKMSException {
        if (crypto == null) {
            Properties properties = new Properties();
            MessageContext msgCtx = data.getMessageContext();

            properties.put(XKMSServerCrypto.XKMS_SERVER_AUTHENTICATION_CODE,
                    getParameterValue(
                            XKMSServerCrypto.XKMS_SERVER_AUTHENTICATION_CODE,
                            msgCtx));

            properties
                    .put(XKMSServerCrypto.XKMS_KEY_STORE_LOCATION,
                            getParameterValue(
                                    XKMSServerCrypto.XKMS_KEY_STORE_LOCATION,
                                    msgCtx));

            properties
                    .put(XKMSServerCrypto.XKMS_KEY_STORE_PASSWORD,
                            getParameterValue(
                                    XKMSServerCrypto.XKMS_KEY_STORE_PASSWORD,
                                    msgCtx));

            properties
                    .put(
                            XKMSServerCrypto.XKMS_DEFAULT_PRIVATE_KEY_PASSWORD,
                            getParameterValue(
                                    XKMSServerCrypto.XKMS_DEFAULT_PRIVATE_KEY_PASSWORD,
                                    msgCtx));

            properties
                    .put(XKMSServerCrypto.XKMS_ISSUER_CERT_ALIACE,
                            getParameterValue(
                                    XKMSServerCrypto.XKMS_ISSUER_CERT_ALIACE,
                                    msgCtx));

            properties
                    .put(
                            XKMSServerCrypto.XKMS_ISSUER_KEY_PASSWORD,
                            getParameterValue(
                                    XKMSServerCrypto.XKMS_ISSUER_KEY_PASSWORD,
                                    msgCtx));

            properties
                    .put(
                            XKMSServerCrypto.XKMS_DEFAULT_PRIVATE_KEY_PASSWORD,
                            getParameterValue(
                                    XKMSServerCrypto.XKMS_DEFAULT_PRIVATE_KEY_PASSWORD,
                                    msgCtx));

            properties
                    .put(XKMSServerCrypto.XKMS_SERVER_CERT_ALIACE,
                            getParameterValue(
                                    XKMSServerCrypto.XKMS_SERVER_CERT_ALIACE,
                                    msgCtx));

            properties
                    .put(
                            XKMSServerCrypto.XKMS_SERVER_KEY_PASSWORD,
                            getParameterValue(
                                    XKMSServerCrypto.XKMS_SERVER_KEY_PASSWORD,
                                    msgCtx));

            properties.put(XKMSServerCrypto.XKMS_DEFAULT_EXPIRY_INTERVAL,
                    getParameterValue(
                            XKMSServerCrypto.XKMS_DEFAULT_EXPIRY_INTERVAL,
                            msgCtx));

            // setting persistence flag
            String parameterValue = getParameterValue(
                    XKMSServerCrypto.XKMS_ENABLE_PERSISTENCE, msgCtx);
            if (parameterValue != null) {
                properties.put(XKMSServerCrypto.XKMS_ENABLE_PERSISTENCE,
                        parameterValue);
            }

            AxisService axisService = msgCtx.getAxisService();
            ClassLoader loader = axisService.getClassLoader();
            crypto = new XKMSServerCrypto(properties, loader);
        }
    }

    private String getParameterValue(String key, MessageContext msgCtx) {
        Parameter param = msgCtx.getParameter(key);
        if (param != null) {
            return (String) param.getValue();
        }
        return null;
    }
}
