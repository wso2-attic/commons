/*
 * Copyright 2008,2009 WSO2, Inc. http://www.wso2.org.
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
package org.wso2.mercury.state;

import org.apache.axis2.description.AxisService;
import org.apache.axis2.description.TransportOutDescription;
import org.apache.axis2.description.TransportInDescription;
import org.apache.axis2.context.ServiceContext;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.client.Options;

import java.util.Map;
import java.util.HashMap;

/**
 * this is a wrapper class to keep all the details about the
 * wso2 envirionment to send the messages.
 * We store these details from the first messagecontext we receive.
 *
 */
public class Axis2Info {
    // wso2 service to which this sequence attaced
    private ServiceContext serviceContext;
    private Options options;
    private TransportOutDescription transportOut;
    private TransportInDescription transportIn;
    private boolean isServerSide;
    private String soapNamespaceURI;
    private String addressingNamespaceURI;
    private Map properties;

    //these properties are used in persistance
    private int currentHandlerIndex;
    private int currentPhaseIndex;

    public Axis2Info() {
        this.properties = new HashMap();
    }

    public Object getProperty(Object key){
        return this.properties.get(key);
    }

    public AxisService getAxisService() {
        return serviceContext.getAxisService();
    }

    public ServiceContext getServiceContext() {
        return serviceContext;
    }

    public ConfigurationContext getConfigurationContext(){
        return serviceContext.getConfigurationContext();
    }

    public void setServiceContext(ServiceContext serviceContext) {
        this.serviceContext = serviceContext;
    }

    public Options getOptions() {
        return this.options;
    }

    public void setOptions(Options options) {
        this.options = options;
    }

    public TransportOutDescription getTransportOut() {
        return transportOut;
    }

    public void setTransportOut(TransportOutDescription transportOut) {
        this.transportOut = transportOut;
    }

    public TransportInDescription getTransportIn() {
        return transportIn;
    }

    public void setTransportIn(TransportInDescription transportIn) {
        this.transportIn = transportIn;
    }

    public boolean isServerSide() {
        return isServerSide;
    }

    public void setServerSide(boolean serverSide) {
        isServerSide = serverSide;
    }

    public String getSoapNamespaceURI() {
        return soapNamespaceURI;
    }

    public void setSoapNamespaceURI(String soapNamespaceURI) {
        this.soapNamespaceURI = soapNamespaceURI;
    }

    public Map getProperties() {
        return properties;
    }

    public void setProperties(Map properties) {
        this.properties = properties;
    }

    public String getAddressingNamespaceURI() {
        return addressingNamespaceURI;
    }

    public void setAddressingNamespaceURI(String addressingNamespaceURI) {
        this.addressingNamespaceURI = addressingNamespaceURI;
    }

    public int getCurrentHandlerIndex() {
        return currentHandlerIndex;
    }

    public void setCurrentHandlerIndex(int currentHandlerIndex) {
        this.currentHandlerIndex = currentHandlerIndex;
    }

    public int getCurrentPhaseIndex() {
        return currentPhaseIndex;
    }

    public void setCurrentPhaseIndex(int currentPhaseIndex) {
        this.currentPhaseIndex = currentPhaseIndex;
    }

}
