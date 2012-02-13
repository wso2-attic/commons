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
package org.wso2;

import org.apache.axis2.context.MessageContext;
import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.transform.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;

/**
 * Utility class for the project
 */
public class Util {

    private static Log log = LogFactory.getLog(Util.class);

    /**
     * This will return location of the file written to the system filesystem.
     *
     * @param url WSDL location
     * @return String file absolute path
     * @throws AxisFault will be thrown
     */
    public static String writeWSDLToFileSystem(String url) throws AxisFault {
        return writeWSDLToFileSystemHelpler(url).getAbsolutePath();
    }

    /**
     * This will return an InputStream for the written file into the filesystem.
     *
     * @param url WSDL location
     * @return InputStream of the file
     * @throws IOException will be thrown
     */
    public static InputStream writeWSDLToStream(String url) throws IOException {
        return new FileInputStream(writeWSDLToFileSystemHelpler(url));
    }

    /**
     * Return the generic wsdl11to20.xsl as an input stream
     *
     * @return InputStream
     * @throws AxisFault will be thrown
     */
    public static InputStream getConverterXSLStream() throws AxisFault {
        InputStream inXSLTStream = Util.class.getClassLoader()
                .getResourceAsStream("org/wso2/wsdlconverter/wsdl11to20.xsl");
        if (inXSLTStream == null) {
            inXSLTStream = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream("org/wso2/wsdlconverter/wsdl11to20.xsl");
        }
        if (inXSLTStream == null) {
            String e = Util.class.getName() + " wsdl11to20.xsl cannot be located.";
            log.error(e);
            throw new AxisFault(e);
        }
        return inXSLTStream;
    }

    /**
     * Transformation helper.
     *
     * @param xmlIn       XML
     * @param xslIn       XSL
     * @param result      Result
     * @param classLoader classloader
     * @throws TransformerException   will be thrown
     * @throws IllegalAccessException will be thrown
     * @throws ClassNotFoundException will be thrown
     * @throws InstantiationException will be thrown
     */
    public static void transform(Source xmlIn, Source xslIn, Result result, ClassLoader classLoader)
            throws TransformerException, IllegalAccessException, InstantiationException,
                   ClassNotFoundException {
        transform(xmlIn, xslIn, result, null, classLoader);
    }

    /**
     * Transform based on parameters
     *
     * @param xmlIn       XML
     * @param xslIn       XSL
     * @param result      Result
     * @param paramMap    Parameter map
     * @param classLoader classloader
     * @throws TransformerException   will be thrown
     * @throws IllegalAccessException will be thrown
     * @throws InstantiationException will be thrown
     * @throws ClassNotFoundException will be thrown
     */
    public static void transform(Source xmlIn, Source xslIn, Result result, Map paramMap,
                                 ClassLoader classLoader)
            throws TransformerException, ClassNotFoundException, IllegalAccessException,
                   InstantiationException {
        try {
            Class providerClass =
                    Class.forName("net.sf.saxon.TransformerFactoryImpl", false, classLoader);
            TransformerFactory transformerFactory =
                    (TransformerFactory) providerClass.newInstance();
            Transformer transformer = transformerFactory.newTransformer(xslIn);
            if (paramMap != null) {
                Set set = paramMap.keySet();
                for (Iterator iterator = set.iterator(); iterator.hasNext();) {
                    String key = (String) iterator.next();
                    String value = (String) paramMap.get(key);
                    transformer.setParameter(key, value);
                }
            }
            transformer.transform(xmlIn, result);
        } catch (TransformerException e) {
            log.error(e.getMessage(), e);
            throw e;
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage(), e);
            throw e;
        } catch (IllegalAccessException e) {
            log.error(e.getMessage(), e);
            throw e;
        } catch (InstantiationException e) {
            log.error(e.getMessage(), e);
            throw e;
        }

    }

    /**
     * Get generic location to write a file with the given suffix.
     *
     * @param suffix should be given with dot; ex: .xml, .wsdl
     * @return
     */
    public static FileInfo getOutputFileLocation(String suffix) {
        String uuid = String.valueOf(System.currentTimeMillis() + Math.random());
        String extraFileLocation =
                MessageContext.
                        getCurrentMessageContext().
                        getConfigurationContext().getProperty("WORK_DIR") +
                                                                          File.separator +
                                                                          "extra" +
                                                                          File.separator +
                                                                          uuid + File.separator;
        File dirs = new File(extraFileLocation);
        if (!dirs.exists()) {
            dirs.mkdirs();
        }
        File outFile = new File(extraFileLocation, uuid + suffix);
        return new FileInfo(uuid, outFile);
    }

    /**
     * Helper method to write the WSDL file to the filesytem.
     *
     * @param url WSDL location
     * @return File object
     * @throws AxisFault will be thrown
     */
    public static File writeWSDLToFileSystemHelpler(String url) throws AxisFault {
        try {
            URL wsdlURL = new URL(url);
            URLConnection connection = wsdlURL.openConnection();
            HttpURLConnection uconn;
            if (connection instanceof HttpURLConnection) {
                uconn = (HttpURLConnection) connection;
            } else {
                String msg = "Unable to process given URL. " +
                             "Only HTTP protocol is currently supported.";
                log.error(msg);
                throw new AxisFault(msg);
            }
            uconn.setRequestMethod("GET");
            uconn.setAllowUserInteraction(false);
            uconn.setDefaultUseCaches(false);
            uconn.setDoInput(true);
            uconn.setDoOutput(false);
            uconn.setInstanceFollowRedirects(true);
            uconn.setUseCaches(false);
            uconn.connect();
            InputStream inStrm = uconn.getInputStream();
            BufferedInputStream bIn = new BufferedInputStream(inStrm);
            File outFile = getOutputFileLocation(".xml").getFile();
            FileOutputStream out = new FileOutputStream(outFile);
            // Transfer bytes from in to out
            byte[] buf = new byte[1024];
            int len;
            while ((len = bIn.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            bIn.close();
            inStrm.close();
            uconn.disconnect();
            out.close();
            return outFile;
        } catch (IllegalArgumentException e) {
            String msg = "URL provided is invalid. " + e.getMessage() +
                         ". Please use a valid URL";
            log.error(msg, e);
            throw new AxisFault(msg);
        } catch (MalformedURLException e) {
            String msg = "URL provided is invalid. " + e.getMessage() +
                         ". Please use a valid URL";
            log.error(msg, e);
            throw new AxisFault(msg);
        } catch (FileNotFoundException e) {
            String msg = "File provied is invalid. " + e.getMessage() +
                         ". Please use a valid file.";
            log.error(msg, e);
            throw new AxisFault(msg);
        } catch (IOException e) {
            String msg = "Problems has encountered in connection. " + e.getMessage() +
                         ". Please check the connection and try this again.";
            log.error(msg, e);
            throw new AxisFault(msg);
        }

    }

    /**
     * Class that holds uuid and file info.
     */
    public static class FileInfo {

        private String uuid;
        private File file;

        public FileInfo(String uuid, File file) {
            this.uuid = uuid;
            this.file = file;
        }

        public String getUuid() {
            return uuid;
        }

        public File getFile() {
            return file;
        }
    }
}
