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
package org.wso2.utils.transport;

import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.SessionContext;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.description.TransportInDescription;
import org.apache.axis2.description.Parameter;
import org.apache.axis2.transport.TransportListener;
import org.wso2.utils.NetworkUtils;
import org.wso2.utils.SessionContextUtil;
import org.wso2.utils.WSO2Constants;

import java.net.SocketException;

/**
 *  This class will provide basic functionality to write http and https listeners for WSO2 projects.
 */
public abstract class AbstractTransportListener implements TransportListener {
    protected ConfigurationContext configurationContext;
    protected int port;
    protected int proxyPort = -1;
    protected TransportInDescription tInDescription;

    public void init(ConfigurationContext configContext,
                     TransportInDescription transportIn) throws AxisFault {
        this.configurationContext = configContext;
        this.port = Integer.parseInt(transportIn.getParameter("port").getValue().toString().trim());
        this.tInDescription = transportIn;
        Parameter proxyParam = transportIn.getParameter(WSO2Constants.PROXY_PORT);
        if (proxyParam != null) {
            proxyPort = Integer.parseInt(proxyParam.getValue().toString().trim());
        }
    }

    public void stop() throws AxisFault {
        // Do nothing. Servers will be stopped from elsewhere
    }

    public EndpointReference getEPR(String protocol,
                                    String serviceName,
                                    String ip) throws AxisFault {
        if (configurationContext == null) {
            return null;
        }
        String serviceContextPath = configurationContext.getServiceContextPath();
        if (serviceContextPath == null) {
            throw new AxisFault("couldn't find service path");
        }
        return genEpr(protocol, ip, serviceContextPath, serviceName);
    }

    public EndpointReference getEPR(String protocol,
                                    String serviceName,
                                    String ip,
                                    String contextPath) throws AxisFault {
        if (configurationContext == null) {
            return null;
        }
        return genEpr(protocol, ip, configurationContext.getServiceContextPath(), serviceName);
    }

    public SessionContext getSessionContext(MessageContext messageContext) {
        return SessionContextUtil.createSessionContext(messageContext);
    }

    public void destroy() {
        this.configurationContext = null;
    }

    protected EndpointReference genEpr(String protocol, String ip, String serviceContextPath,
                                       String serviceName) throws AxisFault {
        try {
            if (ip == null) {
                ip = NetworkUtils.getLocalHostname();
            }
            String tmp = protocol + "://" + ip;
            if (proxyPort == 80 || proxyPort == 443) {
                tmp += serviceContextPath + "/" + serviceName;
                return new EndpointReference(tmp);
            } else if (proxyPort > -1) {
                tmp += ":" + proxyPort + serviceContextPath + "/" + serviceName;
                return new EndpointReference(tmp);
            } else {
                tmp += ":" + port + serviceContextPath + "/" + serviceName;
            }
            return new EndpointReference(tmp);
        } catch (SocketException e) {
            throw AxisFault.makeFault(e);
        }
    }
}
