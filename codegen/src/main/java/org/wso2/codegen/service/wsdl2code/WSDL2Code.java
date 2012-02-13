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
package org.wso2.codegen.service.wsdl2code;

import org.apache.axiom.om.*;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axiom.om.xpath.AXIOMXPath;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.util.CommandLineOption;
import org.apache.axis2.util.CommandLineOptionConstants;
import org.apache.axis2.util.CommandLineOptionParser;
import org.apache.axis2.wsdl.codegen.CodeGenerationEngine;
import org.apache.axis2.wsdl.util.WSDL2JavaOptionsValidator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jaxen.JaxenException;
import org.jaxen.XPath;
import org.wso2.utils.ArchiveManipulator;
import org.wso2.utils.FileManipulator;
import org.wso2.utils.WSO2Constants;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.*;

/**
 * Tool that generate code for the given options
 */
public class WSDL2Code {

    private static Log log = LogFactory.getLog(WSDL2Code.class);
    private static final String CODEGEN_POM_XSL = "codegen-pom.xsl";
    //Codegen option file is taken from classpath
    private static final String CODEGEN_OPTIONS = "codegen-options.xml";

    /**
     * User will be able to get the codegen options file
     *
     * @return OMElement
     * @throws AxisFault throws an AxisFault
     */
    public OMElement getCodegenOptions() throws AxisFault {
        OMElement docEle = loadCodegenOptions();
        docEle.build();
        return docEle;
    }

    private OMElement loadCodegenOptions() throws AxisFault {
        InputStream inStream = getClass().getResourceAsStream(CODEGEN_OPTIONS);
        if (inStream == null) {
            inStream = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream(CODEGEN_OPTIONS);
            if (inStream == null) {
                String s = "Codegen option file is not available";
                log.error(s);
                throw new AxisFault(s);
            }
        }
        XMLStreamReader streamReader;
        try {
            streamReader = XMLInputFactory.newInstance().createXMLStreamReader(inStream);
        } catch (XMLStreamException e) {
            log.error("Error occurred while creating XMLStreamReader", e);
            throw AxisFault.makeFault(e);
        }
        StAXOMBuilder builder = new StAXOMBuilder(streamReader);
        return builder.getDocumentElement();
    }

    /**
     * uploadFileValue element will be generated for given codegen option and value.
     *
     * @param codegenOption      codegen option that need to be fill with
     * @param codegenOptionValue value
     * @return OMElement return
     * @throws AxisFault with be thrown
     */
    public OMElement getCodegenOptionsWithValues(String codegenOption, String codegenOptionValue)
            throws AxisFault {
        OMElement docEle = loadCodegenOptions();
        try {
            XPath xp = new AXIOMXPath("/codegen/argument[@name='" + codegenOption + "']");
            OMElement nameEle = (OMElement) xp.selectSingleNode(docEle);
            if (nameEle != null) {
                OMFactory fac = OMAbstractFactory.getOMFactory();
                OMElement uploadFileValueEle = fac.createOMElement(new QName("uploadFileValue"));
                nameEle.addChild(uploadFileValueEle);
                Map fileResourcesMap =
                    (Map) MessageContext.getCurrentMessageContext().getConfigurationContext()
                            .getProperty(WSO2Constants.FILE_RESOURCE_MAP);
                if (fileResourcesMap == null) {
                    String msg = "File resource is not available";
                    log.error(msg);
                    throw new AxisFault(msg);
                }
                String absFilePath = (String)fileResourcesMap.get(codegenOptionValue);
                if (absFilePath == null) {
                    String msg = "Uploaded file is not available";
                    log.error(msg);
                    throw new AxisFault(msg);
                }
                OMText omText = fac.createOMText(absFilePath, XMLStreamConstants.CDATA);
                uploadFileValueEle.addChild(omText);
            }
            return nameEle;
        } catch (JaxenException e) {
            String msg = "Xpath error has been encounted while looking for the argument : " + codegenOption;
            log.error(msg, e);
            throw new AxisFault(msg, e);
        }
    }

    /**
     * This method will generate the code based on the options array. Options arrya should be as
     * follows,
     * new String[] {"-uri", "location of wsdl", "-g" ...}. Thus, the incoming XML should be as
     * follows,
     * <p/>
     * <ns:codegenRequest xmlns:ns="http://org.wso2.wsf/tools">
     * <options>-uri</options>
     * <options>file://foo</options>
     * ...
     * </ns:codegenRequest>
     * <p/>
     * Once codegenerated, location of genereated code will be send as an ID, thus, one could easily
     * download artifact as a zip file or jar file.
     *
     * @param options
     * @return String
     * @throws AxisFault
     */
    public String codegen(String[] options) throws AxisFault {

        String uuid = String.valueOf(System.currentTimeMillis() + Math.random());
        String codegenOutputDir =
                MessageContext.getCurrentMessageContext().getConfigurationContext()
                        .getProperty(WSO2Constants.WORK_DIR) + File.separator + "tools_codegen" +
                                                             File.separator + uuid + File.separator;
        System.getProperties().remove("project.base.dir");
        System.getProperties().remove("name");
        System.setProperty("project.base.dir", codegenOutputDir);

        ArrayList optionsList = new ArrayList();
        for (int j = 0; j < options.length; j++) {
            optionsList.add(options[j]);
        }
        optionsList.add("-o");
        optionsList.add(codegenOutputDir);
        String[] args = (String[]) optionsList.toArray(new String[optionsList.size()]);
        Map allOptions;
        try {
            CommandLineOptionParser commandLineOptionParser = new CommandLineOptionParser(args);
            allOptions = commandLineOptionParser.getAllOptions();
            //validation
            List list = commandLineOptionParser.getInvalidOptions(new WSDL2JavaOptionsValidator());
            if (list.size() > 0) {
                String faultOptions = "";
                for (Iterator iterator = list.iterator(); iterator.hasNext();) {
                    CommandLineOption commandLineOption = (CommandLineOption) iterator.next();
                    String optionValue = commandLineOption.getOptionValue();
                    faultOptions += "Invalid input for [ " + commandLineOption.getOptionType() +
                                    (optionValue != null ? " : " + optionValue + " ]" : " ]") +
                                    "\n";
                }

                log.error(faultOptions);
                throw new AxisFault(faultOptions);
            }

            new CodeGenerationEngine(commandLineOptionParser).generate();
        } catch (Exception e) {
            String rootMsg = "Code generation failed";
            Throwable throwable = e.getCause();
            if (throwable != null) {
                String msg = throwable.getMessage();
                if (msg != null) {
                    log.error(rootMsg + " " + msg, throwable);
                    throw new AxisFault(throwable.toString());
                }
            }
            log.error(rootMsg, e);
            throw AxisFault.makeFault(e);
        }
        //set the output name
        CommandLineOption option =
                (CommandLineOption) allOptions.
                        get(CommandLineOptionConstants.WSDL2JavaConstants.SERVICE_NAME_OPTION);

        try {
            //achive destination
            uuid = String.valueOf(System.currentTimeMillis() + Math.random());
            File destDir = new File(MessageContext.
                    getCurrentMessageContext().getConfigurationContext().
                    getProperty(WSO2Constants.WORK_DIR) + File.separator + "tools_codegen" +
                                                        File.separator +
                                                        uuid);
            if (!destDir.exists()) {
                destDir.mkdirs();
            }
            String destArchive = destDir.getAbsolutePath() + File.separator + uuid + ".zip";
            InputStream pomXslInputStream = getClass().getResourceAsStream(CODEGEN_POM_XSL);
            if (pomXslInputStream == null) {
                pomXslInputStream = Thread.currentThread().getContextClassLoader()
                        .getResourceAsStream(CODEGEN_POM_XSL);
            }

            String name = uuid;
            boolean isBuildXml = false;
            String version = "1.0";
            File buildXml = new File(codegenOutputDir, "build.xml");
            if (buildXml.exists() && buildXml.isFile()) {
                isBuildXml = true;
                InputStream buildInputStream = new FileInputStream(buildXml);
                XMLStreamReader streamReader =
                        XMLInputFactory.newInstance().createXMLStreamReader(buildInputStream);
                StAXOMBuilder builder = new StAXOMBuilder(streamReader);
                XPath xp = new AXIOMXPath("/project/property[@name='name']");
                OMElement documentElement = builder.getDocumentElement();
                OMElement nameEle = (OMElement) xp.selectSingleNode(documentElement);
                if (nameEle != null) {
                    OMAttribute omAttribute = nameEle.getAttribute(new QName("value"));
                    String nameVal = omAttribute.getAttributeValue();
                    if (nameVal != null) {
                        name = nameVal;
                    }
                }
            }
            OMFactory fac = OMAbstractFactory.getOMFactory();
            OMElement infoEle = fac.createOMElement(new QName("info"));
            OMElement nameEle = fac.createOMElement(new QName("name"));
            nameEle.setText(name);
            infoEle.addChild(nameEle);
            OMElement isBuildXmlEle = fac.createOMElement(new QName("isBuildXml"));
            isBuildXmlEle.setText(Boolean.valueOf(isBuildXml).toString());
            infoEle.addChild(isBuildXmlEle);
            OMElement versionEle = fac.createOMElement(new QName("version"));
            versionEle.setText(version);
            infoEle.addChild(versionEle);
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            infoEle.serialize(bao);
            InputStream xmlInputStream = new ByteArrayInputStream(bao.toByteArray());

            if (pomXslInputStream != null) {
                File pomFileOut = new File(codegenOutputDir, "pom.xml");
                FileOutputStream pomFileOutputStream = new FileOutputStream(pomFileOut);
                Source xmlSource = new StreamSource(xmlInputStream);
                Source xslSource = new StreamSource(pomXslInputStream);
                Result result = new StreamResult(pomFileOutputStream);
                Transformer transformer =
                        TransformerFactory.newInstance().newTransformer(xslSource);
                transformer.transform(xmlSource, result);
            }

            new ArchiveManipulator().archiveDir(destArchive, new File(codegenOutputDir).getPath());
            new FileManipulator().deleteDir(new File(codegenOutputDir));
            Map fileResourcesMap =
                    (Map) MessageContext.getCurrentMessageContext().getConfigurationContext()
                            .getProperty(WSO2Constants.FILE_RESOURCE_MAP);

            if (fileResourcesMap == null) {
                fileResourcesMap = new Hashtable();
                MessageContext.getCurrentMessageContext().getConfigurationContext()
                        .setProperty(WSO2Constants.FILE_RESOURCE_MAP,
                                     fileResourcesMap);
            }
            fileResourcesMap.put(uuid, destArchive);
            return WSO2Constants.ContextPaths.DOWNLOAD_PATH + "?id=" + uuid;

        } catch (IOException e) {
            String msg = WSDL2Code.class.getName() + " IOException has occured.";
            log.error(msg, e);
            throw new AxisFault(msg, e);
        } catch (XMLStreamException e) {
            String msg =
                    WSDL2Code.class.getName() + " error encountred while reading the build.xml";
            log.error(msg, e);
            throw new AxisFault(msg, e);
        } catch (JaxenException e) {
            String msg = WSDL2Code.class.getName() + " xpath error has occured";
            log.error(msg, e);
            throw new AxisFault(msg, e);
        } catch (TransformerConfigurationException e) {
            String msg = WSDL2Code.class.getName() + " transformation error has occured";
            log.error(msg, e);
            throw new AxisFault(msg, e);
        } catch (TransformerException e) {
            String msg = WSDL2Code.class.getName() + " transformation error has occured";
            log.error(msg, e);
            throw new AxisFault(msg, e);
        }
    }

}
