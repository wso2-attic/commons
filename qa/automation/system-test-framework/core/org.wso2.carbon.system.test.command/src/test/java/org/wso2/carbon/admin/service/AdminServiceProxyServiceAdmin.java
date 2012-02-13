/*
*Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*WSO2 Inc. licenses this file to you under the Apache License,
*Version 2.0 (the "License"); you may not use this file except
*in compliance with the License.
*You may obtain a copy of the License at
*
*http://www.apache.org/licenses/LICENSE-2.0
*
*Unless required by applicable law or agreed to in writing,
*software distributed under the License is distributed on an
*"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*KIND, either express or implied.  See the License for the
*specific language governing permissions and limitations
*under the License.
*/
package org.wso2.carbon.admin.service;

import junit.framework.Assert;
import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axis2.AxisFault;
import org.apache.axis2.description.WSDL2Constants;
import org.apache.axis2.wsdl.WSDLConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.config.xml.XMLConfigConstants;
import org.apache.synapse.core.axis2.ProxyService;
import org.wso2.carbon.admin.service.utils.AuthenticateStub;
import org.wso2.carbon.proxyadmin.stub.ProxyAdminException;
import org.wso2.carbon.proxyadmin.stub.ProxyServiceAdminStub;
import org.wso2.carbon.proxyadmin.stub.types.carbon.Entry;
import org.wso2.carbon.proxyadmin.stub.types.carbon.ProxyData;

import javax.activation.DataHandler;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;


public class AdminServiceProxyServiceAdmin {
    private static final Log log = LogFactory.getLog(AdminServiceProxyServiceAdmin.class);

    private final String serviceName = "ProxyServiceAdmin";
    private ProxyServiceAdminStub proxyServiceAdminStub;
    private String endPoint;

    public AdminServiceProxyServiceAdmin(String backEndUrl) {
        this.endPoint = backEndUrl + serviceName;
        try {
            proxyServiceAdminStub = new ProxyServiceAdminStub(endPoint);
        } catch (AxisFault axisFault) {
            log.error("Initialization of ProxyServiceAdminStub initialization failed: " + axisFault.getMessage());
            Assert.fail("Initialization of ProxyServiceAdminStub initialization failed: " + axisFault.getMessage());
        }
    }

    public void addProxyService(String sessionCookie, String proxyName, String wsdlURI, String serviceEndPoint) {

        new AuthenticateStub().authenticateStub(sessionCookie, proxyServiceAdminStub);

        String[] transport = {"http", "https"};
        ProxyData data = new ProxyData();
        data.setName(proxyName);
        data.setWsdlURI(wsdlURI);
        data.setTransports(transport);
        data.setStartOnLoad(true);
        //data.setEndpointKey("http://localhost:9000/services/SimpleStockQuoteService");
        data.setEndpointXML("<endpoint xmlns=\"http://ws.apache.org/ns/synapse\"><address uri=\"" + serviceEndPoint + "\" /></endpoint>");
        data.setEnableSecurity(true);

        try {
            proxyServiceAdminStub.addProxy(data);
            log.info("Proxy Added");
        } catch (RemoteException e) {
            log.error(e);
            Assert.fail("RemoteException while adding proxy : " + e.getMessage());
        } catch (ProxyAdminException e) {
            log.error(e);
            Assert.fail("ProxyAdminException while adding proxy : " + e.getMessage());
        }


    }

    public void addProxyService(String sessionCookie, DataHandler dh) {
        try {

            new AuthenticateStub().authenticateStub(sessionCookie, proxyServiceAdminStub);

            XMLStreamReader parser = XMLInputFactory.newInstance().createXMLStreamReader(dh.getInputStream());
            //create the builder
            StAXOMBuilder builder = new StAXOMBuilder(parser);
            //get the root element (in this case the envelope)
            OMElement documentElement = builder.getDocumentElement();

            ProxyData proxyData = getDesignView(documentElement.toString());
            proxyServiceAdminStub.addProxy(proxyData);
            log.info("Proxy Added");

        } catch (AxisFault axisFault) {
            log.error(axisFault);
            Assert.fail("AxisFault while adding proxy : " + axisFault.getMessage());
        } catch (RemoteException e) {
            log.error(e);
            Assert.fail("RemoteException while adding proxy : " + e.getMessage());
        } catch (IOException e) {
            log.error(e);
            Assert.fail("IOException while processing proxy data : " + e.getMessage());
        } catch (ProxyAdminException e) {
            log.error(e);
            Assert.fail("ProxyAdminException while adding proxy : " + e.getMessage());
        } catch (XMLStreamException e) {
            log.error(e);
            Assert.fail("XMLStreamException while processing proxy data : " + e.getMessage());
        }

    }

    public void deleteProxy(String sessionCookie, String proxyName) {

        new AuthenticateStub().authenticateStub(sessionCookie, proxyServiceAdminStub);
        try {
            proxyServiceAdminStub.deleteProxyService(proxyName);
            log.info("Proxy Deleted");
        } catch (RemoteException e) {
            log.error(e);
            Assert.fail("RemoteException while deleting proxy : " + e.getMessage());
        } catch (ProxyAdminException e) {
            log.error(e);
            Assert.fail("ProxyAdminException while deleting proxy : " + e.getMessage());
        }


    }

    public void startProxyService(String sessionCookie, String proxyName) {

        new AuthenticateStub().authenticateStub(sessionCookie, proxyServiceAdminStub);
        try {
            proxyServiceAdminStub.startProxyService(proxyName);
            log.info("Proxy Activated");
        } catch (RemoteException e) {
            log.error(e);
            Assert.fail("RemoteException while starting Proxy Service : " + e.getMessage());
        } catch (ProxyAdminException e) {
            log.error(e);
            Assert.fail("ProxyAdminException while starting Proxy Service : " + e.getMessage());
        }

    }

    public void stopProxyService(String sessionCookie, String proxyName) {

        new AuthenticateStub().authenticateStub(sessionCookie, proxyServiceAdminStub);
        try {
            proxyServiceAdminStub.stopProxyService(proxyName);
            log.info("Proxy Deactivated");
        } catch (RemoteException e) {
            log.error(e);
            Assert.fail("RemoteException while stopping ProxyService : " + e.getMessage());
        } catch (ProxyAdminException e) {
            log.error(e);
            Assert.fail("ProxyAdminException while stopping ProxyService : " + e.getMessage());
        }


    }

    public void reloadProxyService(String sessionCookie, String proxyName) {

        new AuthenticateStub().authenticateStub(sessionCookie, proxyServiceAdminStub);
        try {
            proxyServiceAdminStub.redeployProxyService(proxyName);
            log.info("Proxy Redeployed");
        } catch (RemoteException e) {
            log.error(e);
            Assert.fail("RemoteException while reloading ProxyService : " + e.getMessage());
        } catch (ProxyAdminException e) {
            log.error(e);
            Assert.fail("ProxyAdminException while reloading ProxyService : " + e.getMessage());
        }
    }

    public ProxyData getProxyDetails(String sessionCookie, String proxyName) {

        new AuthenticateStub().authenticateStub(sessionCookie, proxyServiceAdminStub);
        try {
            return proxyServiceAdminStub.getProxy(proxyName);
        } catch (RemoteException e) {
            log.error(e);
            Assert.fail("RemoteException while getting Proxy Details : " + e.getMessage());
        } catch (ProxyAdminException e) {
            log.error(e);
            Assert.fail("ProxyAdminException while getting Proxy Details : " + e.getMessage());
        }
        return null;
    }
    //this function from org.wso2.carbon.proxyadmin.service in proxy-admin module
    private ProxyData getDesignView(String proxyString) {
        final String BUNDLE = "org.wso2.carbon.proxyadmin.Resources";
        ProxyData pd = new ProxyData();
        ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE, Locale.US);
        try {
            OMElement elem = new StAXOMBuilder(
                    new ByteArrayInputStream(proxyString.getBytes())).getDocumentElement();

            // check whether synapse namespace is present in the configuration.
            Iterator itr = elem.getAllDeclaredNamespaces();
            OMNamespace ns;
            boolean synapseNSPresent = false;
            while (itr.hasNext()) {
                ns = (OMNamespace) itr.next();
                if (XMLConfigConstants.SYNAPSE_NAMESPACE.equals(ns.getNamespaceURI())) {
                    synapseNSPresent = true;
                    break;
                }
            }
            if (!synapseNSPresent) {
                // Oops! synpase namespace is not present
                Assert.fail(bundle.getString("synapse.namespace.not.present"));
            }
            OMAttribute name = elem.getAttribute(new QName("name"));
            if (name != null) {
                pd.setName(name.getAttributeValue());
            }

            OMAttribute statistics = elem.getAttribute(new QName(XMLConfigConstants.STATISTICS_ATTRIB_NAME));
            if (statistics != null) {
                String statisticsValue = statistics.getAttributeValue();
                if (statisticsValue != null) {
                    if (XMLConfigConstants.STATISTICS_ENABLE.equals(statisticsValue)) {
                        pd.setEnableStatistics(true);
                    } else if (XMLConfigConstants.STATISTICS_DISABLE.equals(statisticsValue)) {
                        pd.setEnableStatistics(false);
                    }
                }
            }

            OMAttribute trans = elem.getAttribute(new QName("transports"));
            if (trans != null) {
                String transports = trans.getAttributeValue();
                if (transports == null || "".equals(transports) || ProxyService.ALL_TRANSPORTS.equals(transports)) {
                    // default to all transports using service name as destination
                } else {
                    String[] arr = transports.split(",");
                    if (arr != null && arr.length != 0) {
                        pd.setTransports(arr);
                    }
                }
            }

            OMAttribute pinnedServers = elem.getAttribute(new QName("pinnedServers"));
            if (pinnedServers != null) {
                String pinnedServersValue = pinnedServers.getAttributeValue();
                if (pinnedServersValue == null || "".equals(pinnedServersValue)) {
                    // default to all servers
                } else {
                    String[] arr = pinnedServersValue.split(",");
                    if (arr != null && arr.length != 0) {
                        pd.setPinnedServers(arr);
                    }
                }
            }

            OMAttribute trace = elem.getAttribute(new QName(XMLConfigConstants.TRACE_ATTRIB_NAME));
            if (trace != null) {
                String traceValue = trace.getAttributeValue();
                if (traceValue != null) {
                    if (traceValue.equals(XMLConfigConstants.TRACE_ENABLE)) {
                        pd.setEnableTracing(true);
                    } else if (traceValue.equals(XMLConfigConstants.TRACE_DISABLE)) {
                        pd.setEnableTracing(false);
                    }
                }
            }

            OMAttribute startOnLoad = elem.getAttribute(new QName("startOnLoad"));
            String val;
            if (startOnLoad != null && (val = startOnLoad.getAttributeValue()) != null && !"".equals(val)) {
                pd.setStartOnLoad(Boolean.valueOf(val));
            } else {
                pd.setStartOnLoad(true);
            }

            // read definition of the target of this proxy service. The target could be an 'endpoint'
            // or a named sequence. If none of these are specified, the messages would be mediated
            // by the Synapse main mediator
            OMElement target = elem.getFirstChildWithName(
                    new QName(XMLConfigConstants.SYNAPSE_NAMESPACE, "target"));
            if (target != null) {
                OMAttribute inSequence = target.getAttribute(new QName("inSequence"));
                if (inSequence != null) {
                    pd.setInSeqKey(inSequence.getAttributeValue());
                } else {
                    OMElement inSequenceElement = target.getFirstChildWithName(
                            new QName(XMLConfigConstants.SYNAPSE_NAMESPACE, "inSequence"));
                    if (inSequenceElement != null) {
                        pd.setInSeqXML(inSequenceElement.toString());
                    }
                }
                OMAttribute outSequence = target.getAttribute(new QName("outSequence"));
                if (outSequence != null) {
                    pd.setOutSeqKey(outSequence.getAttributeValue());
                } else {
                    OMElement outSequenceElement = target.getFirstChildWithName(
                            new QName(XMLConfigConstants.SYNAPSE_NAMESPACE, "outSequence"));
                    if (outSequenceElement != null) {
                        pd.setOutSeqXML(outSequenceElement.toString());
                    }
                }
                OMAttribute faultSequence = target.getAttribute(new QName("faultSequence"));
                if (faultSequence != null) {
                    pd.setFaultSeqKey(faultSequence.getAttributeValue());
                } else {
                    OMElement faultSequenceElement = target.getFirstChildWithName(
                            new QName(XMLConfigConstants.SYNAPSE_NAMESPACE, "faultSequence"));
                    if (faultSequenceElement != null) {
                        pd.setFaultSeqXML(faultSequenceElement.toString());
                    }
                }
                OMAttribute tgtEndpt = target.getAttribute(new QName("endpoint"));
                if (tgtEndpt != null) {
                    pd.setEndpointKey(tgtEndpt.getAttributeValue());
                } else {
                    OMElement endpointElement = target.getFirstChildWithName(
                            new QName(XMLConfigConstants.SYNAPSE_NAMESPACE, "endpoint"));
                    if (endpointElement != null) {
                        pd.setEndpointXML(endpointElement.toString());
                    }
                }
            }

            Iterator props = elem.getChildrenWithName(
                    new QName(XMLConfigConstants.SYNAPSE_NAMESPACE, "parameter"));
            ArrayList<Entry> params = new ArrayList<Entry>();
            Entry entry = null;
            while (props.hasNext()) {
                Object o = props.next();
                if (o instanceof OMElement) {
                    OMElement prop = (OMElement) o;
                    OMAttribute pname = prop.getAttribute(new QName("name"));
                    OMElement propertyValue = prop.getFirstElement();
                    if (pname != null) {
                        if (propertyValue != null) {
                            entry = new Entry();
                            entry.setKey(pname.getAttributeValue());
                            entry.setValue(propertyValue.toString());
                            params.add(entry);
                        } else {
                            entry = new Entry();
                            entry.setKey(pname.getAttributeValue());
                            entry.setValue(prop.getText().trim());
                            params.add(entry);
                        }
                    }
                }
            }
            pd.setServiceParams(params.toArray(new Entry[params.size()]));


            OMElement wsdl = elem.getFirstChildWithName(
                    new QName(XMLConfigConstants.SYNAPSE_NAMESPACE, "publishWSDL"));
            if (wsdl != null) {
                OMAttribute wsdlkey = wsdl.getAttribute(
                        new QName(XMLConfigConstants.NULL_NAMESPACE, "key"));
                if (wsdlkey != null) {
                    pd.setWsdlKey(wsdlkey.getAttributeValue());
                } else {
                    OMAttribute wsdlURI = wsdl.getAttribute(
                            new QName(XMLConfigConstants.NULL_NAMESPACE, "uri"));
                    if (wsdlURI != null) {
                        pd.setWsdlURI(wsdlURI.getAttributeValue());
                    } else {
                        OMElement wsdl11 = wsdl.getFirstChildWithName(
                                new QName(WSDLConstants.WSDL1_1_NAMESPACE, "definitions"));
                        String wsdlDef;
                        if (wsdl11 != null) {
                            wsdlDef = wsdl11.toString().replaceAll("\n|\\r|\\f|\\t", "");
                            wsdlDef = wsdlDef.replaceAll("> +<", "><");
                            pd.setWsdlDef(wsdlDef);
                        } else {
                            OMElement wsdl20 = wsdl.getFirstChildWithName(
                                    new QName(WSDL2Constants.WSDL_NAMESPACE, "description"));
                            if (wsdl20 != null) {
                                wsdlDef = wsdl20.toString().replaceAll("\n|\\r|\\f|\\t", "");
                                wsdlDef = wsdlDef.replaceAll("> +<", "><");
                                pd.setWsdlDef(wsdlDef);
                            }
                        }
                    }
                }

                Iterator it = wsdl.getChildrenWithName(
                        new QName(XMLConfigConstants.SYNAPSE_NAMESPACE, "resource"));
                ArrayList<Entry> resources = new ArrayList<Entry>();
                Entry resource = null;
                while (it.hasNext()) {
                    OMElement resourceElem = (OMElement) it.next();
                    OMAttribute location = resourceElem.getAttribute
                            (new QName(XMLConfigConstants.NULL_NAMESPACE, "location"));
                    if (location == null) {
                        // todo handle exception
                    }
                    OMAttribute key = resourceElem.getAttribute(
                            new QName(XMLConfigConstants.NULL_NAMESPACE, "key"));
                    if (key == null) {
                        // todo handle exception
                    }
                    resource = new Entry();
                    resource.setKey(location.getAttributeValue());
                    resource.setValue(key.getAttributeValue());
                    resources.add(resource);
                }
                pd.setWsdlResources(resources.toArray(new Entry[resources.size()]));
            }

            OMElement enableSec = elem.getFirstChildWithName(new QName(XMLConfigConstants.SYNAPSE_NAMESPACE,
                    "enableSec"));
            if (enableSec != null) {
                pd.setEnableSecurity(true);
            }

        } catch (XMLStreamException e) {
            log.error(bundle.getString("unable.to.build.the.design.view.from.the.given.xml"), e);
            Assert.fail(bundle.getString("unable.to.build.the.design.view.from.the.given.xml"));
        }
        return pd;
    }


}
