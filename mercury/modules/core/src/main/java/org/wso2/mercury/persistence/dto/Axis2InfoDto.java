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
package org.wso2.mercury.persistence.dto;

import java.util.List;
import java.util.ArrayList;

public class Axis2InfoDto {

    private long id;
    private String serviceName;
    private int currentHanlderIndex;
    private int currentPhaseIndex;
    private boolean isServerSide;
    private String soapNamespaceURI;
    private String addressingNamespaceURI;

    private List<PropertyDto> properties;
    private List<EngagedModuleDto> engagedModules;

    public Axis2InfoDto() {
        this.properties = new ArrayList<PropertyDto>();
        this.engagedModules = new ArrayList<EngagedModuleDto>();
    }

    public void addProperty(PropertyDto propertyDto){
        this.properties.add(propertyDto);
    }

    public void addEngagedModule(EngagedModuleDto engagedModuleDto){
        this.engagedModules.add(engagedModuleDto);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public int getCurrentHanlderIndex() {
        return currentHanlderIndex;
    }

    public void setCurrentHanlderIndex(int currentHanlderIndex) {
        this.currentHanlderIndex = currentHanlderIndex;
    }

    public int getCurrentPhaseIndex() {
        return currentPhaseIndex;
    }

    public void setCurrentPhaseIndex(int currentPhaseIndex) {
        this.currentPhaseIndex = currentPhaseIndex;
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

    public String getAddressingNamespaceURI() {
        return addressingNamespaceURI;
    }

    public void setAddressingNamespaceURI(String addressingNamespaceURI) {
        this.addressingNamespaceURI = addressingNamespaceURI;
    }

    public List<PropertyDto> getProperties() {
        return properties;
    }

    public void setProperties(List<PropertyDto> properties) {
        this.properties = properties;
    }

    public List<EngagedModuleDto> getEngagedModules() {
        return engagedModules;
    }

    public void setEngagedModules(List<EngagedModuleDto> engagedModules) {
        this.engagedModules = engagedModules;
    }

}
