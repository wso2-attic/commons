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
package org.wso2.validator;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.MessageContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.wso2.utils.WSO2Constants;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.util.Hashtable;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * This class will provide utility methods and verifying methods whether module.xml or
 * services.xml should go ahead.
 */
public class Util {

    private static Log log = LogFactory.getLog(Util.class);

    /**
     * This method will return the absolute path of a file to a given id from the
     * WSO2Constants.FILE_RESOURCE_MAP. If the file does not exists NULL wll be returned.
     *
     * @param fileId    file id
     * @param configCtx Configuration context
     * @return String the absolute file path.
     */
    public static String getFilePathFromFileId(String fileId, ConfigurationContext configCtx) {
        Map fileResMap =
                (Map) configCtx.getProperty(WSO2Constants.FILE_RESOURCE_MAP);
        return (String) fileResMap.get(fileId);
    }

    /**
     * This will create an File object based on the arguments of xml file path and xml file name.
     * Since this method should work in all enviroments, inside of the method it will check for
     * File.separatorChar and replace it with "/" for all environments.
     *
     * @param xmlfilePath xml file path
     * @param xmlFileName xml file name.
     * @return File object
     */
    public static File getOutputFilePath(String xmlfilePath, String xmlFileName) {
        if (File.separatorChar == '\\') {
            xmlfilePath = xmlfilePath.replace('\\', '/');
        }
        int lastIndex = xmlfilePath.lastIndexOf('/');
        String filePath = xmlfilePath.substring(0, lastIndex);
        return new File(filePath, xmlFileName);
    }

    /**
     * This method will do the transformation based on xml streams. First an intermediate structure
     * will be created using validator xsl. Then the intermediate structure will be transformed via
     * formatter xsl, which will result in an html document. Location of this html docuement will
     * be available via an return id, which can be obtained from http GET interface. The download of
     * this  file should be available with [context root]/filedownload context path.
     * ex: http://localhost:8080/wso2wsas/filedownload?id=1.234879899E3
     *
     * @param xmlStream            xml stream
     * @param inFilePath           input file
     * @param xslValidatorLocation xsl validator locatoin
     * @param xslFormatterLocation xsl formatter location
     * @param classLoader          classloader that should be used.
     * @return String ; id for request the transform object via http GET
     * @throws AxisFault will be thrown
     */
    public static String doTransformation(InputStream xmlStream, String inFilePath,
                                          String xslValidatorLocation, String xslFormatterLocation,
                                          ClassLoader classLoader)
            throws AxisFault {
        String uuid = String.valueOf(System.currentTimeMillis() + Math.random());
        try {
            InputStream xslStream = classLoader.getResourceAsStream(xslValidatorLocation);
            InputStream xslForamtterStream = classLoader.getResourceAsStream(xslFormatterLocation);
            Source xmlSource = new StreamSource(xmlStream);
            Source xslSource = new StreamSource(xslStream);
            Source xslForamtterSource = new StreamSource(xslForamtterStream);
            String fileName = uuid + ".html";
            File outFile = getOutputFilePath(inFilePath, fileName);
            DocumentBuilder docB = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = docB.newDocument();
            //do the first transformation
            Result result = new DOMResult(doc);
            Transformer transformer = TransformerFactory.newInstance()
                    .newTransformer(xslSource);
            transformer.transform(xmlSource, result);

            ///////////////////////////////////////////////////
//            System.out.println(DOM2Writer.nodeToString(doc));
            ////////////////////////////////////////////////////

            //the second transformation
            OutputStream outFileStream = new FileOutputStream(outFile);
            Result finalResult = new StreamResult(outFileStream);
            transformer = TransformerFactory.newInstance()
                    .newTransformer(xslForamtterSource);
            //setting the FileoutputPath to ConfigurationContext
            transformer.transform(new DOMSource(doc), finalResult);


            ConfigurationContext configCtx =
                    MessageContext.getCurrentMessageContext().getConfigurationContext();
            Map fileResourcesMap =
                    (Map) configCtx.getProperty(WSO2Constants.FILE_RESOURCE_MAP);
            if (fileResourcesMap == null) {
                fileResourcesMap = new Hashtable();
                configCtx.setProperty(WSO2Constants.FILE_RESOURCE_MAP, fileResourcesMap);
            }
            fileResourcesMap.put(uuid, outFile.getAbsolutePath());
        } catch (Exception e) {
            String msg = "Error has been encounted while doing the transformation.";
            log.error(msg, e);
            throw new AxisFault(msg, e);
        }
        return WSO2Constants.ContextPaths.DOWNLOAD_PATH + "?id=" + uuid;

    }

    /**
     * This method will parse the inputstream for XML document. If it is a valid XML document, true
     * will be return and false will be otherwise
     *
     * @param in Inputstream
     * @throws org.apache.axis2.AxisFault will be thrown if validation fails
     *
     */
    public static void continueProcess(InputStream in) throws AxisFault{
        try {
            StAXOMBuilder builder = new StAXOMBuilder(in);
            OMElement element = builder.getDocumentElement();
            element.build();
        } catch (Exception e) {
            String msg = "The XML descriptor file is empty or not well-formed";
            throw new AxisFault(msg, e);
        }
    }

    /**
     * An XML input stream will be created from file path and the extension. The return input stream
     * will be buffered. Thus as a good practise, once you finished with the stream NULL it, thus GC
     * will be efficient.
     *
     * @param filePath   file id
     * @param extension  extension of need to be matched. This extension should be give with "."
     * @param relXMLPath path of the file that needed to be matched.
     * @return BufferedInputStream for the locate xml
     * @throws org.apache.axis2.AxisFault will be thrown in any faliour.
     */
    public static InputStream locateXML(String filePath, String extension, String relXMLPath)
            throws AxisFault {
        try {
            if (!filePath.endsWith(extension)) {
                throw new AxisFault(
                        "The relevent Axis2 archive should be end with " + extension +
                        " extension.");
            } else {
                FileInputStream stream = new FileInputStream(filePath);
                ZipInputStream zin = new ZipInputStream(stream);
                ZipEntry entry;
                boolean xmlFound = false;
                while ((entry = zin.getNextEntry()) != null) {
                    if (entry.getName().equalsIgnoreCase(relXMLPath)) {
                        xmlFound = true;
                        break;
                    }
                }
                if (!xmlFound) {
                    String msg =
                            relXMLPath + " is not found. Check and upload an valid Axis2 archive.";
                    log.error(msg);
                    throw new AxisFault(msg);
                }
                return zin;
            }
        } catch (IOException e) {
            String msg = "File cannot be found";
            log.error(msg, e);
            throw new AxisFault(msg, e);
        }
    }
}
