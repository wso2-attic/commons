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

import org.apache.axis2.description.AxisService;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.utils.WSO2Constants;

import java.io.*;
import java.net.URL;
/*
 * 
 */

public abstract class WSDLMetaDataFactory {

    protected String wsdlVersion;
    protected String serviceName;
    protected MessageContext currentMessageContext;
    protected static Log log = LogFactory.getLog(WSDLMetaDataFactory.class);

    public static WSDLMetaDataFactory getFactory(String wsdlVersion,
                                                 String serviceName,
                                                 MessageContext currentMessageContext) {
        if (wsdlVersion == null) {
            wsdlVersion = "1.1";
        }
        if (wsdlVersion.equals("1.1")) {
            return new WSDL11MetaDataFactory(wsdlVersion, serviceName, currentMessageContext);
        }

        if (wsdlVersion.equals("2.0")) {
            return new WSDL20MetaDataFactory(wsdlVersion, serviceName, currentMessageContext);
        }

        throw new RuntimeException("Not yet implemented " + wsdlVersion);
    }


    public String getWSDLFileURI(String wsdlDir) throws AxisFault {
        if (!new File(wsdlDir).mkdirs()) {
            String s = WSDLMetaDataFactory.class.getName() + " couldn't create " + wsdlDir;
            log.error(s);
            throw new AxisFault(s);
        }

        String wsdlFile = wsdlDir + serviceName + ".xml";
        AxisService axisService = currentMessageContext.getConfigurationContext()
                .getAxisConfiguration().getService(serviceName);

        if (axisService != null) {
            FileOutputStream out;

            try {
                out = new FileOutputStream(wsdlFile);


                String url = currentMessageContext.getTo().getAddress();
                int ipindex = url.indexOf("//");
                String ip = null;

                if (ipindex >= 0) {
                    ip = url.substring(ipindex + 2, url.length());

                    int seperatorIndex = ip.indexOf(":");

                    if (seperatorIndex > 0) {
                        ip = ip.substring(0, seperatorIndex);
                    }
                }
                printWSDL(out, ip,
                          currentMessageContext.getConfigurationContext().getServiceContextPath(),
                          axisService);

                out.flush();
                out.close();
            } catch (FileNotFoundException e) {
                String s = "Could not create WSDL File : ";
                log.error(s, e);
                throw new AxisFault(s + e);
            } catch (IOException e) {
                String s = "Could not write to WSDL File : ";
                log.error(s, e);
                throw new AxisFault(s + e);
            }
        } else {
            String s =
                    WSDLMetaDataFactory.class.getName() + " can't find AxisService " + serviceName;
            log.error(s);
            throw new AxisFault(s);
        }

        return wsdlFile;
    }

    /**
     * Generating a qunique codegenOutputDir
     *
     * @return String
     */
    public UniqueOutputDirInfo generateUniqueCodegenOutputDir() {
        String uuid = String.valueOf(System.currentTimeMillis() + Math.random());

        String codegenOutputDir = currentMessageContext.getConfigurationContext()
                .getProperty(WSO2Constants.WORK_DIR) +
                                                     File.separator + "codegen" + File.separator +
                                                     uuid + File.separator;

        return new UniqueOutputDirInfo(uuid, codegenOutputDir);


    }

    /**
     * Return wsdlDir
     *
     * @return UniqueOutputDirInfo
     */
    public UniqueOutputDirInfo generateUniqueDir(String infoDir) {
        WSDLMetaDataFactory.UniqueOutputDirInfo uniqueOutputDirInfo =
                generateUniqueCodegenOutputDir();
        String codegenOutputDir = generateUniqueCodegenOutputDir().getOutputDir();
        codegenOutputDir = codegenOutputDir + File.separator + infoDir + File.separator;
        return new UniqueOutputDirInfo(uniqueOutputDirInfo.getUuid(), codegenOutputDir);
    }

    public UniqueOutputDirInfo generateUniqueDir(String infoDir,
                                                 UniqueOutputDirInfo uniqueOutputDirInfo) {
        String dir = uniqueOutputDirInfo.getOutputDir() + File.separator + infoDir + File.separator;
        return new UniqueOutputDirInfo(uniqueOutputDirInfo.uuid, dir);
    }

    protected abstract void printWSDL(OutputStream out, String ip, String serviceContextPath,
                                      AxisService axisService) throws AxisFault;

    protected abstract void readWSDL(URL wsdlLocation) throws Exception;

    public abstract ServiceEndpointsData[] createServiceEndpointsDataArray(URL wsdlLocation) throws AxisFault;

    public abstract URL getWSDLLocation();

    public WSDLMetaData createWSDLMetaData(URL wsdlLocation) throws AxisFault {
        WSDLMetaData wsdlMetaData = new WSDLMetaData();
        wsdlMetaData.setWsdlVersion(wsdlVersion);
        wsdlMetaData.setServiceEndpointsData(createServiceEndpointsDataArray(wsdlLocation));
        return wsdlMetaData;
    }

    protected String getBaseURI(String currentURI) {
        try {
            File file = new File(currentURI);
            if (file.exists()) {
                return file.getCanonicalFile().getParentFile().toURI().toString();
            }
            String uriFragment = currentURI.substring(0, currentURI.lastIndexOf("/"));
            return uriFragment + (uriFragment.endsWith("/") ? "" : "/");
        } catch (IOException e) {
            return null;
        }
    }

    public class UniqueOutputDirInfo {
        private String uuid;
        private String outputDir;

        protected UniqueOutputDirInfo(String uuid, String outputDir) {
            this.uuid = uuid;
            this.outputDir = outputDir;
        }

        public String getUuid() {
            return uuid;
        }

        public String getOutputDir() {
            return outputDir;
        }
    }


}
