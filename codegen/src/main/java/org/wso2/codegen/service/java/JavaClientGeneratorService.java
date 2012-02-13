/*
 * Copyright 2005,2006 WSO2, Inc. http://www.wso2.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wso2.codegen.service.java;

import org.apache.axis2.context.MessageContext;
import org.apache.axis2.util.CommandLineOptionConstants;
import org.apache.axis2.util.CommandLineOptionParser;
import org.apache.axis2.wsdl.codegen.CodeGenerationEngine;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.codegen.service.WSDLMetaDataFactory;
import org.wso2.codegen.service.java.xsd.WsdlRestriction;
import org.wso2.utils.AntBuildInvoker;
import org.wso2.utils.FileManipulator;
import org.wso2.utils.WSO2Constants;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;


/**
 * JavaClientGeneratorService java skeleton for the axisService
 */
public class JavaClientGeneratorService implements JavaClientGeneratorServiceSkeletonInterface {

    private static Log log = LogFactory.getLog(JavaClientGeneratorService.class);

    /**
     * Generating of the stubs based on WSDL2Java
     * @param service
     * @param packageName
     * @param invocationStyle
     * @param generateTestCase
     * @param databindingMethod
     * @param unpackClasses
     * @param serviceBindingName
     * @param servicePortName
     * @param unwrap
     * @param wsdlVersion
     * @param wsdlLocation   Location of the wsdl according to ?wsdl. ?wsdl has been choosen so that
     *                       the  imports will be handled correctly.
     * @return String
     */
    public String generate(String service,
                           String packageName,
                           String invocationStyle,
                           boolean generateTestCase,
                           String databindingMethod,
                           boolean unpackClasses,
                           String serviceBindingName,
                           String servicePortName,
                           boolean unwrap,
                           WsdlRestriction wsdlVersion,
                           String wsdlLocation) {

        String codegenOutputDir;
        String uuid;

        WSDLMetaDataFactory wsdlMetaDataFactory = WSDLMetaDataFactory.getFactory(
                wsdlVersion.getValue(), service, MessageContext.getCurrentMessageContext());

        WSDLMetaDataFactory.UniqueOutputDirInfo uniqueOutputDirInfo =
                wsdlMetaDataFactory.generateUniqueCodegenOutputDir();

        codegenOutputDir = uniqueOutputDirInfo.getOutputDir();
        uuid = uniqueOutputDirInfo.getUuid();

        System.getProperties().remove("project.base.dir");
        System.getProperties().remove("name");
        System.setProperty("project.base.dir", codegenOutputDir);
        System.setProperty("name", service);

        WSDL2JavaGenerator generator = new WSDL2JavaGenerator();

        String wsdlDir = wsdlMetaDataFactory.generateUniqueDir("resources", uniqueOutputDirInfo)
                .getOutputDir();
        try {
//            String wsdlFileURI = wsdlMetaDataFactory.getWSDLFileURI(wsdlDir);
            String[] options = generator.parse(invocationStyle.equals("a"),
                                               invocationStyle.equals("s"),
                                               false,
                                               false,
                                               generateTestCase,
                                               false,
                                               unpackClasses,
                                               serviceBindingName, //ServiceName
                                               servicePortName, //PortName
                                               databindingMethod,
                                               wsdlLocation,
                                               packageName.trim(),
                                               "java",
                                               codegenOutputDir,
                                               unwrap,
                                               wsdlVersion.getValue());

            CommandLineOptionParser parser = new CommandLineOptionParser(options);
            CodeGenerationEngine codeGenerationEngine = new CodeGenerationEngine(parser);

            codeGenerationEngine.generate();

            // Copy the source files into the build/classes dir so that the created client.jar file
            // will contains the sources as well
            File srcDir = new File(codegenOutputDir + File.separator + "src");
            File destDir = new File(codegenOutputDir + File.separator + "build" +
                                    File.separator + "classes");
            destDir.mkdirs();
            new FileManipulator().copyDir(srcDir, destDir);

            // Call ant and create the jar file. This jar file should be uniquely identifiable
            AntBuildInvoker ant = new AntBuildInvoker(new File(codegenOutputDir +
                                                               "build.xml"));
            ant.invokeTarget("jar.client");
        } catch (Exception e) {
            String msg = "Error occurred during code generation. ";
            if (e.getCause() != null && e.getCause().getMessage() != null) {
                msg += e.getCause().getMessage();
            }
            log.error(msg, e);
            throw new RuntimeException(msg);
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

        // Look for the jar in the build/lib dir
        File f = new File(codegenOutputDir + File.separator + "build" +
                          File.separator + "lib");
        File[] files = f.listFiles(new FileFilter() {
            public boolean accept(File f) {
                return f.getName().endsWith("-client.jar");
            }
        });

        if ((files != null) && (files[0] != null) &&
            (files[0].getAbsoluteFile() != null)) {
            fileResourcesMap.put(uuid,
                                 files[0].getAbsoluteFile().getAbsolutePath());
        }

        return WSO2Constants.ContextPaths.DOWNLOAD_PATH + "?id=" + uuid;

    }

    private class WSDL2JavaGenerator {
        /**
         * Creates a list of parameters for the code generator based on the
         * decisions made by the user on the OptionsPage(page2). For each
         * setting, there is a Command-Line option for the Axis2 code
         * generator.
         *
         * @return String[], which the parsed values, as user given in command line
         */
        public String[] parse(boolean isAsyncOnly,
                              boolean isSyncOnly,
                              boolean isServerSide,
                              boolean isServerXML,
                              boolean isTestCase,
                              boolean isGenerateAll,
                              boolean isUnpackClasses,
                              String serviceName,
                              String portName,
                              String databindingName,
                              String wsdlURI,
                              String packageName,
                              String selectedLanguage,
                              String outputLocation,
                              boolean isUnwrap,
                              String wsdlVersion) {

            ArrayList argList = new ArrayList();
            String MINUS = "-";

            //WSDL file name
            argList.add(
                    MINUS + CommandLineOptionConstants.WSDL2JavaConstants.WSDL_LOCATION_URI_OPTION);
            argList.add(wsdlURI);

            //Wrapping

            if (isUnwrap) {

                argList.add(
                        MINUS + CommandLineOptionConstants.WSDL2JavaConstants.UNWRAP_PARAMETERS);

            }

            //Async only
            if (isAsyncOnly) {
                argList.add(MINUS + CommandLineOptionConstants.WSDL2JavaConstants
                        .CODEGEN_ASYNC_ONLY_OPTION);
            }

            //sync only
            if (isSyncOnly) {
                argList.add(MINUS + CommandLineOptionConstants.WSDL2JavaConstants
                        .CODEGEN_SYNC_ONLY_OPTION);
            }

            //serverside
            if (isServerSide) {

                argList.add(MINUS + CommandLineOptionConstants.WSDL2JavaConstants
                        .SERVER_SIDE_CODE_OPTION);

                //server xml
                if (isServerXML) {

                    argList.add(MINUS + CommandLineOptionConstants.WSDL2JavaConstants
                            .GENERATE_SERVICE_DESCRIPTION_OPTION);
                }

                if (isGenerateAll) {
                    argList.add(MINUS + CommandLineOptionConstants.WSDL2JavaConstants
                            .GENERATE_ALL_OPTION);
                }
            }

            //test case
            if (isTestCase) {
                argList.add(MINUS + CommandLineOptionConstants.WSDL2JavaConstants
                        .GENERATE_TEST_CASE_OPTION);
            }

            //unpack classes
            if (isUnpackClasses) {
                argList.add(MINUS + CommandLineOptionConstants.WSDL2JavaConstants
                        .UNPACK_CLASSES_OPTION);
            }

            //package name
            if ((packageName != null) && (packageName.length() != 0)) {

                argList.add(MINUS + CommandLineOptionConstants.WSDL2JavaConstants.PACKAGE_OPTION);
                argList.add(packageName);
            }

            //selected language

            argList.add(MINUS + CommandLineOptionConstants.WSDL2JavaConstants.STUB_LANGUAGE_OPTION);
            argList.add(selectedLanguage);

            //output location

            argList.add(
                    MINUS + CommandLineOptionConstants.WSDL2JavaConstants.OUTPUT_LOCATION_OPTION);
            argList.add(outputLocation);

            //databinding
            if ((databindingName != null) && (databindingName.length() != 0)) {

                argList.add(MINUS + CommandLineOptionConstants.WSDL2JavaConstants
                        .DATA_BINDING_TYPE_OPTION);
                argList.add(databindingName);
            }

            //port name
            if ((portName != null) && (portName.length() != 0)) {

                argList.add(MINUS + CommandLineOptionConstants.WSDL2JavaConstants.PORT_NAME_OPTION);
                argList.add(portName);
            }

            //service name
            if ((serviceName != null) && (serviceName.length() != 0)) {

                argList.add(
                        MINUS + CommandLineOptionConstants.WSDL2JavaConstants.SERVICE_NAME_OPTION);
                argList.add(serviceName);
            }
            //wsdl version
            if (wsdlVersion != null && wsdlVersion.length() != 0) {
                if (wsdlVersion
                        .equals(CommandLineOptionConstants.WSDL2JavaConstants.WSDL_VERSION_2)) {
                    argList.add(MINUS + CommandLineOptionConstants.WSDL2JavaConstants
                            .WSDL_VERSION_OPTION);
                    argList.add(
                            CommandLineOptionConstants.WSDL2JavaConstants.WSDL_VERSION_2_OPTIONAL);
                }
            }

            return (String[]) argList.toArray(new String[argList.size()]);
        }

    }
}
