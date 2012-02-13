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
package org.wso2.wsdlconverter;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.MessageContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.Util;
import org.wso2.utils.WSO2Constants;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.util.Hashtable;
import java.util.Map;

/**
 * Tools that will be used to convert WSDL 1.1 document into WSDL 2.0 document.
 */
public class WSDLConverter {

    private static Log log = LogFactory.getLog(WSDLConverter.class);

    /**
     * This will convert a given WSDL v1.1 document to WSDL v2.0
     *
     * @param fileId - id to the path of the wsdl 1.1 document
     * @return path of the equivalent wsdl 2.0 document
     * @throws AxisFault return an AxisFault
     */
    public String convert(String fileId) throws AxisFault {
        String wsdl11FilePath = getFilePathFromFileId(fileId);
        return convertWSDL11ToWSDL20(wsdl11FilePath);
    }

    /**
     * @param url any valid URL that can be recognize by URL object
     * @return String
     * @throws AxisFault AxisFault wrapper will be thrown
     */
    public String convertFromURL(String url) throws AxisFault {
        try {
            return convertWSDL11ToWSDL20(Util.writeWSDLToFileSystem(url));
        } catch (AxisFault e) {
            throw e;
        }
    }


    private String convertWSDL11ToWSDL20(String wsdl11FilePath) throws AxisFault {
        try {
            InputStream inXSLTStream = Util.getConverterXSLStream();
            InputStream inXMLStream = new FileInputStream(new File(wsdl11FilePath));
            Source xmlSource = new StreamSource(inXMLStream);
            Source xsltSource = new StreamSource(inXSLTStream);
            String uuid = String.valueOf(System.currentTimeMillis() + Math.random());
            if (File.separatorChar == '\\') {
                wsdl11FilePath = wsdl11FilePath.replace('\\','/');
            }
            int lastIndex = wsdl11FilePath.lastIndexOf('/');
            String filePath = wsdl11FilePath.substring(0, lastIndex);
            File outFile = new File(filePath, uuid + ".xml");
            OutputStream out = new FileOutputStream(outFile);
            Result wsdlResult = new StreamResult(out);
            ClassLoader classLoader =
                    MessageContext.getCurrentMessageContext().getAxisService().getClassLoader();
            Util.transform(xmlSource, xsltSource, wsdlResult, classLoader);
            ConfigurationContext configCtx =
                    MessageContext.getCurrentMessageContext().getConfigurationContext();
            Map fileResourcesMap =
                    (Map) configCtx.getProperty(WSO2Constants.FILE_RESOURCE_MAP);
            if (fileResourcesMap == null) {
                fileResourcesMap = new Hashtable();
                configCtx.setProperty(WSO2Constants.FILE_RESOURCE_MAP, fileResourcesMap);
            }
            fileResourcesMap.put(uuid, outFile.getAbsolutePath());
            return WSO2Constants.ContextPaths.DOWNLOAD_PATH + "?id=" + uuid;
        } catch (FileNotFoundException e) {
            String msg = "File provided cannot be found. " + e.getMessage() +
                         ". Please select a valid file.";
            log.error(e.getMessage(), e);
            throw new AxisFault(msg);
        } catch (TransformerConfigurationException e) {
            String msg = "Problem has occured during the process of transformation. " + e.getMessage();
            log.error(e.getMessage(), e);
            throw new AxisFault(msg);
        } catch (TransformerException e) {
            String msg = "Problem has occured during the process of transformation. " + e.getMessage();
            log.error(msg, e);
            throw new AxisFault(msg);
        } catch (IllegalAccessException e) {
            String msg = "IllegalAccessException. " + e.getMessage();
            log.error(msg, e);
            throw new AxisFault(msg);
        } catch (ClassNotFoundException e) {
            String msg = "ClassNotFoundException. " + e.getMessage();
            log.error(msg, e);
            throw new AxisFault(msg);
        } catch (InstantiationException e) {
            String msg = "InstantiationException. "  + e.getMessage();
            log.error(msg, e);
            throw new AxisFault(msg);
        }
    }

    protected String getTargetFileLocation(String wsdl11FilePath) throws AxisFault {
        File file = new File(wsdl11FilePath);
        if (file.isFile()) {
            String name = file.getName();
            String wsdlFileName = name.substring(0, name.lastIndexOf(".")) + "-wsdl20.wsdl";
            return file.getParent() + File.separator + wsdlFileName;
        } else {
            String s = "File path does not refers to a file";
            log.error(s);
            throw new AxisFault(s);
        }
    }

    private String getFilePathFromFileId(String fileId) {
        ConfigurationContext configCtx =
                MessageContext.getCurrentMessageContext().getConfigurationContext();
        Map fileResMap =
                (Map) configCtx.getProperty(WSO2Constants.FILE_RESOURCE_MAP);
        return (String) fileResMap.get(fileId);
    }

}
