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
package org.wso2.tryit;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.deployment.util.PhasesInfo;
import org.apache.axis2.description.*;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.wso2.Util;
import org.wso2.utils.NetworkUtils;
import org.wso2.utils.WSO2Constants;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.util.*;

/**
 * GenericAJAXClient will be used in generating AJAX based application for a given WSDL. Given WSDL
 * could be either version 1.1 or 2.0. If the given WSDL is version 1.1, it will be converted to
 * version 2.0 using wsdl11to20.xsl. This conversion is absolute necessary due to usage of dyanmic-
 * codegen project.
 */
public class GenericAJAXClient {

    public static String TRYIT_SG_NAME = "TryItMockServiceGroup";
    private static String PROXY_TIMER = "_PROXY_TIMER_";
    public static String LAST_TOUCH_TIME = "_LAST_TOUCH_TIME_";
    /*Touch time.*/
    public final static long PERIOD = 20 * 60 * 1000;
    private static Log log = LogFactory.getLog(GenericAJAXClient.class);

    /**
     * This is the Web method. A URL of a WSDL document is given and the ID of the generated AJAX
     * app will be return. User need to do a HTTP GET on filedownload?id=<genid> to get the XHTML
     * app.
     *
     * @param url WSDL document location.
     * @return hostName name of client host.
     * @throws AxisFault if any anomaly occured.
     */
    public String generateAJAXApp(String url, String hostName) throws AxisFault {

        Map fileResourcesMap =
                (Map) MessageContext.getCurrentMessageContext().getConfigurationContext()
                        .getProperty(WSO2Constants.FILE_RESOURCE_MAP);
        if (fileResourcesMap == null) {
            fileResourcesMap = new Hashtable();
            MessageContext.getCurrentMessageContext().getConfigurationContext()
                    .setProperty(WSO2Constants.FILE_RESOURCE_MAP,
                                 fileResourcesMap);
        }
        try {
            ClassLoader classLoader =
                    MessageContext.getCurrentMessageContext().getAxisService().getClassLoader();
            File location = Util.writeWSDLToFileSystemHelpler(url);
            InputStream inXMLStream = new FileInputStream(location);
            AxisService service;
            WSDLToAxisServiceBuilder builder;
            try {
                XMLStreamReader streamReader1 =
                        XMLInputFactory.newInstance().createXMLStreamReader(inXMLStream);
                StAXOMBuilder stAXOMBuilder = new StAXOMBuilder(streamReader1);
                OMElement docEle = stAXOMBuilder.getDocumentElement();
                //switch
                if (docEle.getQName().getLocalPart().equals("definitions")) {
                    builder = new WSDL11ToAxisServiceBuilder(new FileInputStream(location));
                    builder.setBaseUri(getBaseURI(url));
                    ((WSDL11ToAxisServiceBuilder) builder).setAllPorts(true);
                } else if (docEle.getQName().getLocalPart().equals("description")) {
                    builder = new WSDL20ToAxisServiceBuilder(new FileInputStream(location), null,
                                                             null);
                    builder.setBaseUri(getBaseURI(url));
                } else {
                    String msg = GenericAJAXClient.class.getName() +
                                 " standard WSDL document is not found";
                    log.error(msg);
                    throw new AxisFault(msg);
                }
            } catch (XMLStreamException e) {
                log.error(e.getMessage(), e);
                throw AxisFault.makeFault(e);
            }
            service = builder.populateService();
            updateMockProxyServiceGroup(service, MessageContext.getCurrentMessageContext()
                    .getConfigurationContext().getAxisConfiguration());
            List exposeTxList = new ArrayList();
            exposeTxList.add("http");
            service.setExposedTransports(exposeTxList);
            ByteArrayOutputStream wsdl2Bos = new ByteArrayOutputStream();
            if (hostName == null || hostName.length() == 0) {
                hostName = NetworkUtils.getLocalHostname();
            }
            service.printWSDL2(wsdl2Bos, hostName);
            if (log.isDebugEnabled()) {
                try {
                    XMLStreamReader streamReader =
                            XMLInputFactory.newInstance().createXMLStreamReader(
                                    new ByteArrayInputStream(wsdl2Bos.toByteArray()));
                    StAXOMBuilder stAXOMBuilder = new StAXOMBuilder(streamReader);
                    OMElement ele = stAXOMBuilder.getDocumentElement();
                    log.debug("output ==> " + ele.toString());
                } catch (XMLStreamException e) {
                    log.error(e.getMessage(), e);
                }
            }
            Source wsdlSource = new StreamSource(new ByteArrayInputStream(wsdl2Bos.toByteArray()));
            InputStream wsdl2sigXSLTStream =
                    getClass().getClassLoader().getResourceAsStream("wsdl2sig.xslt");
            Source wsdl2sigXSLTSource = new StreamSource(wsdl2sigXSLTStream);
            DocumentBuilder docB = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document docSig = docB.newDocument();
            Result resultSig = new DOMResult(docSig);
            Util.transform(wsdlSource, wsdl2sigXSLTSource, resultSig, classLoader);

            DOMSource sigDomSource = new DOMSource(docSig);
            Util.FileInfo jsFileLocation = Util.getOutputFileLocation(".stub.js");
            File jsFile = jsFileLocation.getFile();
            InputStream jsStubXSLTStream =
                    getClass().getClassLoader().getResourceAsStream("jsstub.xslt");
            Source jsStubXSLSource = new StreamSource(jsStubXSLTStream);
            OutputStream jsStubOutputStream = new FileOutputStream(jsFile);
            Result jsStubResult = new StreamResult(jsStubOutputStream);
            Util.transform(sigDomSource, jsStubXSLSource, jsStubResult, classLoader);

            File[] files0 = jsFile.getParentFile().listFiles(new FileFilter() {
                public boolean accept(File pathname) {
                    return pathname.getName().endsWith(".stub.js");
                }
            });
            if ((files0 != null) && (files0[0] != null) &&
                (files0[0].getAbsoluteFile() != null)) {
                String absolutePath = files0[0].getAbsoluteFile().getAbsolutePath();
                fileResourcesMap.put(jsFileLocation.getUuid(), absolutePath);
            }
            InputStream tryItXSLTStream =
                    getClass().getClassLoader().getResourceAsStream("tryit.xslt");
            Source tryItXSLSource = new StreamSource(tryItXSLTStream);
            Util.FileInfo htmlFileLocation = Util.getOutputFileLocation(".html");
            File tryItOutFile = htmlFileLocation.getFile();
            OutputStream tryItOutFileStream = new FileOutputStream(tryItOutFile);
            Result tryItResult = new StreamResult(tryItOutFileStream);
            Map paramMap = new HashMap();
            paramMap.put("wsrequest-location", "js/WSRequest.js");
            paramMap.put("stub-location", "filedownload?id=" + jsFileLocation.getUuid());
            paramMap.put("show-alternate", "false");
            paramMap.put("fixendpoints", "false");
            Util.transform(sigDomSource, tryItXSLSource, tryItResult, paramMap, classLoader);

            File[] files1 = tryItOutFile.getParentFile().listFiles(new FileFilter() {
                public boolean accept(File pathname) {
                    return pathname.getName().endsWith(".html");
                }
            });
            if ((files1 != null) && (files1[0] != null) &&
                (files1[0].getAbsoluteFile() != null)) {
                fileResourcesMap.put(htmlFileLocation.getUuid(),
                                     files1[0].getAbsoluteFile().getAbsolutePath());
            }
            return WSO2Constants.ContextPaths.DOWNLOAD_PATH + "?id=" + htmlFileLocation.getUuid();
        } catch (IOException e) {
            throw AxisFault.makeFault(e);
        } catch (ParserConfigurationException e) {
            throw AxisFault.makeFault(e);
        } catch (TransformerException e) {
            throw AxisFault.makeFault(e);
        } catch (IllegalAccessException e) {
            throw AxisFault.makeFault(e);
        } catch (ClassNotFoundException e) {
            throw AxisFault.makeFault(e);
        } catch (InstantiationException e) {
            throw AxisFault.makeFault(e);
        }
    }

    private synchronized void updateMockProxyServiceGroup(AxisService axisService,
                                                          AxisConfiguration axisConfig)
            throws AxisFault {
        /*axisService.addParameter("supportSingleOperation", Boolean.TRUE);
        AxisOperation singleOP = new InOutAxisOperation(new QName("invokeTryItProxyService"));
        singleOP.setDocumentation("This operation is a 'passthrough' for all operations in " +
                                  " TryIt proxy service.");
        axisService.addOperation(singleOP);*/
        ProxyMessageReceiver receiver = new ProxyMessageReceiver();
        PhasesInfo phaseInfo = axisConfig.getPhasesInfo();
        for (Iterator i = axisService.getOperations(); i.hasNext();) {
            AxisOperation op = (AxisOperation) i.next();
            op.setMessageReceiver(receiver);
            phaseInfo.setOperationPhases(op);
        }
        AxisServiceGroup serviceGroup;
        synchronized (axisConfig) {
            serviceGroup = axisConfig.getServiceGroup(TRYIT_SG_NAME);
            if (serviceGroup == null) {
                serviceGroup = new AxisServiceGroup();
                serviceGroup.setServiceGroupName(TRYIT_SG_NAME);
            }
            
            // resolving Axis service name conflicts.
            AxisService testService = axisConfig.getService(axisService.getName());
            if (testService != null) {
                for (int loop = 1 ; ;loop++) {
                    String testName = axisService.getName() + "_" + loop;
                    if (axisConfig.getService(testName) == null) {
                        axisService.setName(testName);
                        break;
                    }
                }
            }
            serviceGroup.addService(axisService);
            axisConfig.addServiceGroup(serviceGroup);
            axisService.addParameter(LAST_TOUCH_TIME, new Long(System.currentTimeMillis()));
            // Set the timer. 
            Parameter parameter = axisConfig.getParameter(PROXY_TIMER);
            if (parameter == null) {
                Timer timer = new Timer();
                timer.scheduleAtFixedRate(new ProxyTimerTask(axisConfig), PERIOD, PERIOD);
                parameter = new Parameter(PROXY_TIMER, timer);
                axisConfig.addParameter(parameter);
            }
        }
    }

    private String getBaseURI(String currentURI) {
        String uriFragment = currentURI.substring(0, currentURI.lastIndexOf("/"));
        return uriFragment + (uriFragment.endsWith("/") ? "" : "/");
    }
}
