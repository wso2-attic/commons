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
package org.wso2.wsdlview;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ws.java2wsdl.Java2WSDLCodegenEngine;
import org.apache.ws.java2wsdl.utils.Java2WSDLCommandLineOptionParser;
import org.apache.ws.java2wsdl.utils.Java2WSDLOptionsValidator;
import org.apache.ws.java2wsdl.utils.Java2WSDLCommandLineOption;
import org.wso2.utils.WSO2Constants;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileFilter;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

/**
 * This class will generate a WSDL, either version 1.1 or 2.0 for a given java bytecode. Underline
 * implementation uses org.apache.ws.java2wsdl.Java2WSDLCodegenEngine.
 */
public class WSDLView {

    private static Log log = LogFactory.getLog(WSDLView.class);

    /**
     * Get the wsdlview-options.xml
     *
     * @return OMElement
     * @throws AxisFault
     */
    public OMElement getOptions() throws AxisFault {
        InputStream ins = getClass().getResourceAsStream("wsdlview-options.xml");
        if (ins == null) {
            ins = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream("wsdlview-options.xml");
        }
        if (ins == null) {
            String msg = WSDLView.class.getName() +
                         " wsdlview-options.xml cannot be located in the classpath";
            log.error(msg);
            throw new AxisFault(msg);
        }
        try {
            XMLStreamReader streamReader = XMLInputFactory.newInstance().createXMLStreamReader(ins);
            StAXOMBuilder builder = new StAXOMBuilder(streamReader);
            OMElement ele = builder.getDocumentElement();
            ele.build();
            return ele;
        } catch (XMLStreamException e) {
            log.error(e.getMessage(), e);
            throw AxisFault.makeFault(e);
        }
    }

    /**
     * This will generate the WSDL document and output a Id. User has to send this Id to the
     * filedownload hadler to extract the wsdl document.
     *
     * @param options array of options
     * @return String id
     * @throws AxisFault will be thrown
     */
    public String wsdlview(String[] options) throws AxisFault {
        String uuid = String.valueOf(System.currentTimeMillis() + Math.random());
        String wsdlOutputDir =
                MessageContext.getCurrentMessageContext().getConfigurationContext()
                        .getProperty(WSO2Constants.WORK_DIR) + File.separator + "tools_wsdlview" +
                                                             File.separator + uuid + File.separator;
        ArrayList optionsList = new ArrayList();
        boolean isXCAvailable = false;
        String xcValue = "";
        for (int j = 0; j < options.length; j++) {
            if (options[j].equalsIgnoreCase("-xc")) {
                isXCAvailable = true;
                continue;
            }
            if (isXCAvailable) {
                xcValue = options[j];
                isXCAvailable = false;
                continue;
            }
            optionsList.add(options[j]);
        }
        optionsList.add("-o");
        optionsList.add(wsdlOutputDir);
        optionsList.add("-of");
        optionsList.add(uuid + ".xml");
        parseXC(xcValue, optionsList);
        String[] args = (String[]) optionsList.toArray(new String[optionsList.size()]);
        Java2WSDLCommandLineOptionParser commandLineOptionParser =
                new Java2WSDLCommandLineOptionParser(args);
        try {
            Map allOptions = commandLineOptionParser.getAllOptions();
            List list =
                    commandLineOptionParser.getInvalidOptions(new Java2WSDLOptionsValidator());
            if (list.size() > 0) {
                String faultOptions = "";
                for (Iterator iterator = list.iterator(); iterator.hasNext();) {
                    Java2WSDLCommandLineOption commandLineOption =
                            (Java2WSDLCommandLineOption) iterator.next();
                    String optionValue = commandLineOption.getOptionValue();
                    faultOptions += "Invalid input for [ " + commandLineOption.getOptionType() +
                                    (optionValue != null ? " : " + optionValue + " ]" : " ]") +
                                    "\n";
                }

                log.error(faultOptions);
                throw new AxisFault(faultOptions);
            }

            new Java2WSDLCodegenEngine(allOptions).generate();
        } catch (Exception e) {
            String rootMsg = WSDLView.class.getName() + " Exception has occured.";
            Throwable throwable = e.getCause();
            if (throwable != null) {
                String tmpMsg = throwable.toString();
                if (tmpMsg.toString().indexOf("org.apache.axis2.AxisFault") > -1) {
                    tmpMsg = "Please provide the correct inputs to either -p2n or -xc";
                    log.error(tmpMsg, throwable);
                    throw new AxisFault(tmpMsg);
                }
                log.error(rootMsg, throwable);
                throw new AxisFault(throwable.toString());
            }
            String tmpMsg = e.toString();
            if (tmpMsg.indexOf("java.lang.StringIndexOutOfBoundsException") > -1) {
                tmpMsg = "Please provide the correct inputs to either -p2n or -xc";
                log.error(tmpMsg, e);
                throw new AxisFault(tmpMsg);
            }
            log.error(rootMsg, e);
            throw AxisFault.makeFault(e);
        }

        Map fileResourcesMap =
                (Map) MessageContext.getCurrentMessageContext().getConfigurationContext()
                        .getProperty(WSO2Constants.FILE_RESOURCE_MAP);

        if (fileResourcesMap == null) {
            fileResourcesMap = new Hashtable();
            MessageContext.getCurrentMessageContext().getConfigurationContext()
                    .setProperty(WSO2Constants.FILE_RESOURCE_MAP,
                                 fileResourcesMap);
        }

        File[] files = new File(wsdlOutputDir).listFiles(new FileFilter() {
            public boolean accept(File f) {
                return f.getName().endsWith(".xml");
            }
        });

        if ((files != null) && (files[0] != null) &&
            (files[0].getAbsoluteFile() != null)) {
            fileResourcesMap.put(uuid, files[0].getAbsoluteFile().getAbsolutePath());
        }
        return WSO2Constants.ContextPaths.DOWNLOAD_PATH + "?id=" + uuid;

    }

    /**
     * This is the fall though method for wsdlview. This will check for required resources.
     *
     * @param options options array
     * @param uuids   uuid array
     * @return String id
     * @throws AxisFault will be thrown.
     */
    public String wsdlviewWithResources(String[] options, String[] uuids) throws AxisFault {

        Map fileResourcesMap =
                (Map) MessageContext.getCurrentMessageContext().getConfigurationContext()
                        .getProperty(WSO2Constants.FILE_RESOURCE_MAP);
        if (fileResourcesMap == null) {
            String msg = WSDLView.class.getName() + " File resource map couldn't be located";
            log.error(msg);
            throw new AxisFault(msg);
        }
        ClassLoader prevCl = Thread.currentThread().getContextClassLoader();
        List pathList = new ArrayList();
        if (uuids != null && uuids.length >= 1) {
            for (int j = 0; j < uuids.length; j++) {
                String path = (String) fileResourcesMap.get(uuids[j]);
                try {
                    if (path != null && path.length() != 0) {
                        pathList.add(new File(path).toURL());
                    }
                } catch (MalformedURLException e) {
                    log.error(e.getMessage(), e);
                    throw AxisFault.makeFault(e);
                }
            }

        } else {
            String msg = WSDLView.class.getName() + " resources are missing";
            log.error(msg);
            throw new AxisFault(msg);
        }
        try {
            URL[] urls = (URL[]) pathList.toArray(new URL[pathList.size()]);
            ClassLoader newCl = URLClassLoader.newInstance(urls, prevCl);
            Thread.currentThread().setContextClassLoader(newCl);
            return wsdlview(options);
        } catch (AxisFault e) {
            throw e;
        } finally {
            Thread.currentThread().setContextClassLoader(prevCl);
        }

    }

    private void parseXC(String value, ArrayList options) {
        if (value == null || value.length() == 0) {
            return;
        }
        String[] tokens = value.split(",");
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];
            if (token != null && token.length() != 0) {
                options.add("-xc");
                options.add(token);
            }
        }

    }
}
