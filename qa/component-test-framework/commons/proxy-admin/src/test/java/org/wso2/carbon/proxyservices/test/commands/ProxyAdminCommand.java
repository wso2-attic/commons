/*
*  Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/

package org.wso2.carbon.proxyservices.test.commands;

// Proxy service admin command implementation class

import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.proxyadmin.ui.ProxyAdminException;
import org.wso2.carbon.proxyadmin.ui.ProxyServiceAdminStub;
import org.wso2.carbon.proxyadmin.ui.types.carbon.MetaData;
import org.wso2.carbon.proxyadmin.ui.types.carbon.ProxyData;

import java.rmi.RemoteException;

public class ProxyAdminCommand extends TestCase {

    private static final Log log = LogFactory.getLog(ProxyAdminCommand.class);
    ProxyServiceAdminStub proxyServiceAdminStub;

    public ProxyAdminCommand(ProxyServiceAdminStub proxyServiceAdminStub) {
        this.proxyServiceAdminStub = proxyServiceAdminStub;
        log.debug("proxyAdminStub added");
    }

    public boolean addProxySuccessCase(ProxyData proxyData)
            throws ProxyAdminException, RemoteException {
        boolean result = false;
        try {
            proxyServiceAdminStub.addProxy(proxyData);
            result = true;
        }
        catch (ProxyAdminException e) {
            Assert.fail("Unable to add proxy service");
            e.printStackTrace();
            log.error("Unable to add proxy service" + e.getMessage());

        }
        catch (Exception e) {
            Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }

        return result;
    }

    public boolean addProxyFailureCase(ProxyData proxyData)
            throws ProxyAdminException, RemoteException {
        boolean result = false;
        try {
            proxyServiceAdminStub.addProxy(proxyData);
            result = false;
            log.error("Proxy service added in non login state");
            Assert.fail("Proxy service added in non login state");
        }
        catch (AxisFault e) {
            if (e.toString().contains("AxisEngine Access Denied. Please login first")) {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }

        }
        catch (Exception e) {
            Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
        return result;

    }

    public void deleteProxySuccessCase(String serviceName)
            throws ProxyAdminException, RemoteException {
        try {
            proxyServiceAdminStub.deleteProxyService(serviceName);
        }
        catch (ProxyAdminException e) {
            Assert.fail("Unable to delete proxy service");
            e.printStackTrace();
            log.error("Unable to delete proxy service" + e.getMessage());

        }
        catch (Exception e) {
            Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }

    public void deleteProxyFailureCase(String serviceName)
            throws ProxyAdminException, RemoteException {
        try {
            proxyServiceAdminStub.deleteProxyService(serviceName);
            log.error("Proxy service deleted in non login state");
            Assert.fail("Proxy service deleted in non login state");

        }
        catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }

        }
        catch (Exception e) {
            Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }

    public void disableStatSuccessCase(String proxyName)
            throws ProxyAdminException, RemoteException {
        try {
            proxyServiceAdminStub.disableStatistics(proxyName);
        }
        catch (Exception e) {
            org.junit.Assert.fail("Stat disable failed" + e.getMessage());
            e.printStackTrace();
            log.error("Stat disable failed" + e.getMessage());
        }
    }

    public void disableStatFailureCase(String proxyName)
            throws ProxyAdminException, RemoteException {
        try {
            proxyServiceAdminStub.disableStatistics(proxyName);
            log.error("Stat disabled without authentications");
            org.junit.Assert.fail("Stat disabled without authentications");
        }
        catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                org.junit.Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        }
        catch (Exception e) {
            org.junit.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }

    public void enableStatSuccessCase(String proxyName)
            throws ProxyAdminException, RemoteException {
        try {
            proxyServiceAdminStub.enableStatistics(proxyName);
        }
        catch (Exception e) {
            Assert.fail("Stat enable failed" + e.getMessage());
            e.printStackTrace();
            log.error("Stat enable failed" + e.getMessage());
        }
    }

    public void enableStatFailureCase(String proxyName)
            throws ProxyAdminException, RemoteException {
        try {
            proxyServiceAdminStub.enableStatistics(proxyName);
            log.error("Stat enabled without authentications");
            org.junit.Assert.fail("Stat enabled without authentications");
        }
        catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                org.junit.Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        }
        catch (Exception e) {
            org.junit.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }


    public void enableTracingSuccessCase(String serviceName) throws Exception {
        try {
            proxyServiceAdminStub.enableTracing(serviceName);
        }
        catch (Exception e) {
            e.printStackTrace();
            log.fatal("Enable tracing failed : " + e.toString());
            Assert.fail("Enable tracing failed");
        }
    }

    public void enableTracingFailureCase(String serviceName) throws Exception {
        try {
            proxyServiceAdminStub.enableTracing(serviceName);
            log.error("Tracing enabled without authentications");
            Assert.fail("Tracing enabled without authentications");
        }
        catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                org.junit.Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        }
        catch (Exception e) {
            org.junit.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }

    public void disableTracingSuccessCase(String serviceName) throws Exception {
        try {
            proxyServiceAdminStub.disableTracing(serviceName);
        }
        catch (Exception e) {
            e.printStackTrace();
            log.fatal("disable tracing failed : " + e.toString());
            Assert.fail("disable tracing failed");
        }
    }

    public void disableTracingFailureCase(String serviceName) throws Exception {
        try {
            proxyServiceAdminStub.disableTracing(serviceName);
            log.error("Tracing disabled without authentications");
            Assert.fail("Tracing disabled without authentications");
        }
        catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                org.junit.Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        }
        catch (Exception e) {
            org.junit.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }

    public void startProxySuccessCase(String serviceName) throws Exception {
        try {
            proxyServiceAdminStub.startProxyService(serviceName);
        }
        catch (Exception e) {
            e.printStackTrace();
            log.fatal("start proxy service failed : " + e.toString());
            Assert.fail("start proxy service failed");
        }
    }

    public void startProxyFailureCase(String serviceName) throws Exception {
        try {
            proxyServiceAdminStub.startProxyService(serviceName);
            log.error("start proxy service without authentications");
            Assert.fail("start proxy service without authentications");
        }
        catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                org.junit.Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        }
        catch (Exception e) {
            org.junit.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }

    public void stopProxySuccessCase(String serviceName) throws Exception {
        try {
            proxyServiceAdminStub.stopProxyService(serviceName);
        }
        catch (Exception e) {
            e.printStackTrace();
            log.fatal("stop proxy service failed : " + e.toString());
            Assert.fail("stop proxy service failed");
        }
    }

    public void stopProxyFailureCase(String serviceName) throws Exception {
        try {
            proxyServiceAdminStub.stopProxyService(serviceName);
            log.error("stop proxy service without authentications");
            Assert.fail("stop proxy service without authentications");
        }
        catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                org.junit.Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        }
        catch (Exception e) {
            org.junit.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }

    public void redeployProxySuccessCase(String serviceName) throws Exception {
        try {
            proxyServiceAdminStub.redeployProxyService(serviceName);
        }
        catch (Exception e) {
            e.printStackTrace();
            log.fatal("redeploy proxy service failed : " + e.toString());
            Assert.fail("redeploy proxy service failed");
        }
    }

    public void redeployProxyFailureCase(String serviceName) throws Exception {
        try {
            proxyServiceAdminStub.redeployProxyService(serviceName);
            log.error("redeploy proxy service without authentications");
            Assert.fail("redeploy proxy service without authentications");
        }
        catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                org.junit.Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        }
        catch (Exception e) {
            org.junit.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }

    public ProxyData getProxySuccessCase(String serviceName) throws Exception {
        ProxyData proxyData = null;
        try {
            proxyData = proxyServiceAdminStub.getProxy(serviceName);
        }
        catch (Exception e) {
            e.printStackTrace();
            log.fatal("Getting proxy data failed : " + e.toString());
            Assert.fail("getting proxy data failed");
        }
        return proxyData;
    }

    public ProxyData getProxyFailureCase(String serviceName) throws Exception {
        ProxyData proxyData = null;
        try {
            proxyData = proxyServiceAdminStub.getProxy(serviceName);
            log.error("Getting proxy data without authentications");
            Assert.fail("Getting proxy data without authentications");
        }
        catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                org.junit.Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        }
        catch (Exception e) {
            org.junit.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
        return proxyData;
    }

    public String[] getTransportSuccessCase() throws Exception {
        String[] transport = null;
        try {
            transport = proxyServiceAdminStub.getAvailableTransports();
        }
        catch (Exception e) {
            e.printStackTrace();
            log.fatal("Getting transport data failed : " + e.toString());
            Assert.fail("getting transport data failed");
        }
        return transport;
    }

    public String[] getTransportFailureCase() throws Exception {
        String[] transport = null;
        try {
            transport = proxyServiceAdminStub.getAvailableTransports();
            log.error("Getting transport data without authentications");
            Assert.fail("Getting transport data without authentications");
        }
        catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                org.junit.Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        }
        catch (Exception e) {
            org.junit.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
        return transport;
    }

    public String[] getSequencesSuccessCase() throws Exception {
        String[] sequences = null;
        try {
            sequences = proxyServiceAdminStub.getAvailableSequences();
        }
        catch (Exception e) {
            e.printStackTrace();
            log.fatal("Getting sequences data failed : " + e.toString());
            Assert.fail("getting sequences data failed");
        }
        return sequences;
    }

    public String[] getSequencesFailureCase() throws Exception {
        String[] sequences = null;
        try {
            sequences = proxyServiceAdminStub.getAvailableSequences();
            log.error("Getting sequences data without authentications");
            Assert.fail("Getting sequences data without authentications");
        }
        catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                org.junit.Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        }
        catch (Exception e) {
            org.junit.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
        return sequences;
    }

    public MetaData getMetaDataSuccessCase() throws Exception {
        MetaData metaData = null;
        try {
            metaData = proxyServiceAdminStub.getMetaData();
        }
        catch (Exception e) {
            e.printStackTrace();
            log.fatal("Getting sequences data failed : " + e.toString());
            Assert.fail("getting sequences data failed");
        }
        return metaData;
    }

    public MetaData getMetaDataFailureCase() throws Exception {
        MetaData metaData = null;
        try {
            metaData = proxyServiceAdminStub.getMetaData();
            log.error("Getting meta data without authentications");
            Assert.fail("Getting meta data without authentications");
        }
        catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                org.junit.Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        }
        catch (Exception e) {
            org.junit.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
        return metaData;
    }

    public String[] getAvailableEndpointSuccessCase() throws Exception {
        String[] endpoint = null;
        try {
            endpoint = proxyServiceAdminStub.getAvailableEndpoints();
        }
        catch (Exception e) {
            e.printStackTrace();
            log.fatal("Getting endpoint data failed : " + e.toString());
            Assert.fail("getting endpoint data failed");
        }
        return endpoint;
    }

    public String[] getAvailableEndpointFailureCase() throws Exception {
        String[] endpoint = null;
        try {
            endpoint = proxyServiceAdminStub.getAvailableEndpoints();
            log.error("Getting endpoint data without authentications");
            Assert.fail("Getting endpoint data without authentications");
        }
        catch (AxisFault e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                org.junit.Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        }
        catch (Exception e) {
            org.junit.Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
        return endpoint;
    }


}
