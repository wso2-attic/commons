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
import org.w3c.dom.Document;

import javax.wsdl.Binding;
import javax.wsdl.Definition;
import javax.wsdl.Port;
import javax.wsdl.Service;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.xml.namespace.QName;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/*
 * 
 */

public class WSDL11MetaDataFactory extends WSDLMetaDataFactory {

    private Definition wsdlDefinition;
    private URL wsdlLocation;

    public WSDL11MetaDataFactory(String wsdlVersion, String serviceName,
                                 MessageContext currentMessageContext) {
        this.wsdlVersion = wsdlVersion;
        this.serviceName = serviceName;
        this.currentMessageContext = currentMessageContext;
    }

    public ServiceEndpointsData[] createServiceEndpointsDataArray(URL wsdlLocation) throws AxisFault {
        return serviceEndpointsDataHelper1(wsdlLocation);
    }

    public URL getWSDLLocation() {
        return this.wsdlLocation;
    }

    private ServiceEndpointsData[] serviceEndpointsDataHelper1(URL wsdlLocation) throws AxisFault {
        this.wsdlLocation = wsdlLocation;
        try {
            readWSDL(this.wsdlLocation);
            return serviceEndpointsDataHelper0();
        } catch (Exception e) {
            log.error(e);
            throw AxisFault.makeFault(e);
        }

    }

    private ServiceEndpointsData[] serviceEndpointsDataHelper0() {
        QName[] serviceBindings = getServiceList();
        ServiceEndpointsData[] servicePortData = new ServiceEndpointsData[serviceBindings.length];

        for (int i = 0; i < serviceBindings.length; i++) {
            ServiceEndpointsData data = new ServiceEndpointsData();
            // setting service
            data.setServiceName(serviceBindings[i].getLocalPart());

            String[] ports = getPortNameList(serviceBindings[i]);
            data.setEndpointNames(ports);
            servicePortData[i] = data;
        }

        return servicePortData;

    }

    public void printWSDL(OutputStream out, String ip, String serviceContextPath,
                          AxisService axisService) throws AxisFault {
        axisService.printWSDL(out, ip);

    }

    public void readWSDL(URL wsdlLocation) throws Exception {
        WSDLReader reader = WSDLFactory.newInstance().newWSDLReader();
        reader.setFeature("javax.wsdl.verbose", false);
        reader.setFeature("javax.wsdl.importDocuments", true);
        InputStream in = wsdlLocation.openConnection().getInputStream();
        Document document = XMLUtils.newDocument(in);
        wsdlDefinition = reader.readWSDL(getBaseURI(wsdlLocation.toString()),document);
    }

    //Return a arry of  QNames
    private QName[] getServiceList() {
        ArrayList returnList = new ArrayList();
        Service service;
        Map serviceMap = wsdlDefinition.getServices();

        if ((serviceMap != null) && !serviceMap.isEmpty()) {
            Iterator serviceIterator = serviceMap.values().iterator();

            while (serviceIterator.hasNext()) {
                service = (Service) serviceIterator.next();
                returnList.add(service.getQName());
            }
        }

        return (QName[]) returnList.toArray(new QName[returnList.size()]);
    }

    private String[] getPortNameList(QName serviceName) {
        List returnList = new ArrayList();
        Service service = wsdlDefinition.getService(serviceName);
        Port port;

        if (service != null) {
            Map portMap = service.getPorts();

            if ((portMap != null) && !portMap.isEmpty()) {
                Iterator portIterator = portMap.values().iterator();

                while (portIterator.hasNext()) {
                    port = (Port) portIterator.next();

                    //counter for HTTPBinding
                    Binding binding = port.getBinding();
                    QName bindingQName = binding.getQName();
                    String bindingName = bindingQName.getLocalPart();

                    if (bindingName.endsWith("HttpBinding")) {
                        continue;
                    }

                    returnList.add(port.getName());
                }
            }
        }

        return (String[]) returnList.toArray(new String[returnList.size()]);
    }

}
