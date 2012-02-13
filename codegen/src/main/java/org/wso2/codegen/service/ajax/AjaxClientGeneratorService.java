/*
 * Copyright 2005,2006 WSO2, Inc. http://www.wso2.org
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
package org.wso2.codegen.service.ajax;

import org.apache.axis2.context.MessageContext;
import org.apache.axis2.util.CommandLineOptionParser;
import org.apache.axis2.wsdl.codegen.CodeGenerationEngine;
import org.wso2.codegen.service.ServiceEndpointsData;
import org.wso2.codegen.service.WSDLMetaData;
import org.wso2.codegen.service.WSDLMetaDataFactory;
import org.wso2.utils.WSO2Constants;

import java.io.File;
import java.io.FileFilter;
import java.util.Hashtable;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.net.URL;

/**
 * AjaxClientGeneratorService java skeleton for the axisService
 */
public class AjaxClientGeneratorService implements AjaxClientGeneratorServiceSkeletonInterface {


    public String generateAjaxClient(String serviceName,String wsdlLocation) {
        String uuid;
        String codegenOutputDir;

        WSDLMetaDataFactory wsdlMetaDataFactory = WSDLMetaDataFactory
                .getFactory("1.1", serviceName, MessageContext.getCurrentMessageContext());
        WSDLMetaDataFactory.UniqueOutputDirInfo uniqueOutputDirInfo =
                wsdlMetaDataFactory.generateUniqueCodegenOutputDir();
        uuid = uniqueOutputDirInfo.getUuid();


        codegenOutputDir = uniqueOutputDirInfo.getOutputDir();

        String wsdlDir = wsdlMetaDataFactory
                .generateUniqueDir("dynamic_client", uniqueOutputDirInfo).getOutputDir();

        try {

            WSDLMetaData wsdlMetaData = wsdlMetaDataFactory.createWSDLMetaData(new URL(wsdlLocation));
            Pattern pattern = Pattern.compile(".*SOAP12port_https\\d*");

            ServiceEndpointsData[] serviceEndpointsData = wsdlMetaData.getServiceEndpointsData();
            String endpoint = null;
            if (serviceEndpointsData != null) {
                ServiceEndpointsData sed = serviceEndpointsData[0];
                String[] endpointNames = sed.getEndpointNames();
                if (endpointNames != null) {
                    for (int i = 0 ; i < endpointNames.length ; i++) {
                        Matcher matcher = pattern.matcher(endpointNames[i]);
                        if (matcher.matches()){
                            endpoint = endpointNames[i];
                            break;
                        }
                    }
                }
            }

            String[] args = new String[]{
                    "-uri", wsdlMetaDataFactory.getWSDLLocation().toString(), "-o", codegenOutputDir, "-l",
                    "ajax", "-pn", endpoint, "-sn",
                    serviceName
            };

            //            //if this is unauthodox lets try something else
            CommandLineOptionParser optionParser = new CommandLineOptionParser(args);
            new CodeGenerationEngine(optionParser).generate();
        } catch (Exception e) {
            //            e.printStackTrace();
            throw new RuntimeException(e);
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

        File f = new File(codegenOutputDir + File.separator + "src" +
                          File.separator);
        FileFilter filter = new DynamicClientFileFilter();

        File[] files = f.listFiles(filter);

        if ((files != null) && (files[0] != null) &&
            (files[0].getAbsoluteFile() != null)) {
            fileResourcesMap.put(uuid,
                                 files[0].getAbsoluteFile().getAbsolutePath());
        }

        String contextRoot = "";
        if (!MessageContext.getCurrentMessageContext().getConfigurationContext()
                .getContextRoot().equals("/")) {
            contextRoot = MessageContext.getCurrentMessageContext().getConfigurationContext()
                    .getContextRoot();
        }

        return contextRoot + WSO2Constants.ContextPaths.DOWNLOAD_PATH + "?id=" + uuid;
    }

    private class DynamicClientFileFilter implements FileFilter {
        public boolean accept(File f) {
            return f.getName().endsWith(".html");
        }
    }
}
