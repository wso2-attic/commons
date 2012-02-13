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
package org.wso2.codegen.service;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.util.XMLUtils;
import org.apache.woden.WSDLFactory;
import org.apache.woden.WSDLReader;
import org.apache.woden.WSDLSource;
import org.apache.woden.internal.DOMWSDLSource;
import org.apache.woden.wsdl20.Description;
import org.apache.woden.wsdl20.Endpoint;
import org.apache.woden.wsdl20.Service;
import org.w3c.dom.Document;

import javax.xml.namespace.QName;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
/*
 * 
 */

public class WSDL20MetaDataFactory extends WSDLMetaDataFactory {


    private Description wsdlDescription;
    private URL wsdlLocation;

    public WSDL20MetaDataFactory(String wsdlVersion, String serviceName,
                                 MessageContext currentMessageContext) {
        this.wsdlVersion = wsdlVersion;
        this.serviceName = serviceName;
        this.currentMessageContext = currentMessageContext;
    }

    public void printWSDL(OutputStream out, String ip, String serviceContextPath,
                          AxisService axisService) throws AxisFault {
        axisService.printWSDL2(out);
    }

    public void readWSDL(URL wsdlLocation) throws Exception {
        WSDLReader reader = WSDLFactory.newInstance().newWSDLReader();
        wsdlDescription = reader.readWSDL(wsdlLocation.toString());
    }

    public ServiceEndpointsData[] createServiceEndpointsDataArray(URL wsdlLocation) throws AxisFault {
        try {
            this.wsdlLocation = wsdlLocation;
            readWSDL(wsdlLocation);
            QName[] serviceBindings = getServiceList();
            ServiceEndpointsData[] servicePortData = new ServiceEndpointsData[serviceBindings.length];

            for (int i = 0; i < serviceBindings.length; i++) {
                ServiceEndpointsData data = new ServiceEndpointsData();
                // setting service
                data.setServiceName(serviceBindings[i].getLocalPart());

                String[] endpoints = getEndpointNameList(serviceBindings[i]);
                data.setEndpointNames(endpoints);
                servicePortData[i] = data;
            }

            return servicePortData;
        } catch (Exception e) {
            log.error(e);
            throw AxisFault.makeFault(e);
        }


    }

    public URL getWSDLLocation() {
        return this.wsdlLocation;
    }

    private String[] getEndpointNameList(QName serviceBinding) {
        ArrayList returnList = new ArrayList();
        Service service = wsdlDescription.getService(serviceBinding);
        Endpoint[] endpoints = service.getEndpoints();
        if (endpoints != null) {
            for (int j = 0; j < endpoints.length; j++) {
                Endpoint endpoint = endpoints[j];
                returnList.add(endpoint.getName().toString());
            }
        }

        return (String[]) returnList.toArray(new String[returnList.size()]);


    }

    private QName[] getServiceList() {
        ArrayList returnList = new ArrayList();

        Service[] services = wsdlDescription.getServices();

        if (services != null) {
            for (int i = 0; i < services.length; i++) {
                Service service = services[i];
                returnList.add(service.getName());
            }
        }

        return (QName[]) returnList.toArray(new QName[returnList.size()]);


    }
}
