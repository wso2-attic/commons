/*                                                                             
 * Copyright 2005,2006 WSO2, Inc. http://wso2.com
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
package org.wso2.adminui;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.util.*;

/**
 *
 */
public class UIProcessor {

    private static Log log = LogFactory.getLog(UIProcessor.class);

    private static final QName FILE_QN = new QName("file"); // has two differen roles
    private static final QName FILES_QN = new QName("files");
    private static final QName EXTENSIONS_ORDER_QN = new QName("extension-order");
    private static final QName FILE_MAPPINGS_QN = new QName("file-mappings");
    private static final QName ITEM_QN = new QName("item");
    private static final QName MAPPING_QN = new QName("mapping");
    private static final QName TOKEN_QN = new QName("token");
    private static final String UI_EXTENSION_CONFIG_FILE = "ui-extensions-config.xml";

    /**
     * The files defined inside <code>configFileName</code> will be processes and content will
     * be added to the fileContents Map
     *
     * @param resourceBase
     * @param configFileName
     * @throws UIProcessingException
     */
    public static void createPages(final String resourceBase,
                                   String configFileName,
                                   final Map mainFileMap) throws UIProcessingException {
        if (configFileName == null) {
            configFileName = UI_EXTENSION_CONFIG_FILE;
        }
        InputStream inStream =
                Thread.currentThread().getContextClassLoader().getResourceAsStream(configFileName);
        if (inStream == null) {
            throw new UIProcessingException(configFileName + " file not found in classpath");
        }
        OMElement documentElement;
        try {
            documentElement = new StAXOMBuilder(inStream).getDocumentElement();
        } catch (XMLStreamException e) {
            throw new UIProcessingException(e);
        }


        Collection files = new ArrayList();
        Collection orderedExts = new ArrayList();
        Map mappings = new HashMap();


        parseFiles(documentElement, files);

        parseExtensionOrder(documentElement, orderedExts);

        parseFileMappings(documentElement, mappings, orderedExts, resourceBase);

        // mainFileMap is filled with the necessary files at this point

        for (Iterator fileIterator = files.iterator(); fileIterator.hasNext();) {
            String mainFileName = (String) fileIterator.next();
            String fileContents;
            try {
                fileContents = getFileStr(resourceBase, mainFileName);
            } catch (IOException e) {
                throw new UIProcessingException(e);
            }
            for (Iterator mappingIterator = mappings.keySet().iterator();
                 mappingIterator.hasNext();) {
                String token = (String) mappingIterator.next();
                fileContents = fileContents.replaceAll(token, (String) mappings.get(token));
            }
            if (fileContents != null) {
                mainFileMap.put(mainFileName, fileContents);
            }

        }

    }

    private static void parseFiles(OMElement documentElement, Collection filesCollection) {
        OMElement filesElement = documentElement.getFirstChildWithName(FILES_QN);

        for (Iterator fileIterator = filesElement.getChildrenWithName(FILE_QN);
             fileIterator.hasNext();) {
            OMElement file = (OMElement) fileIterator.next();
            filesCollection.add(file.getText());
        }

    }

    private static void parseFileMappings(OMElement documentElement, Map fileMappingMap,
                                          Collection orderedExts, String resourceBase) {

        OMElement fileMappingElement = documentElement.getFirstChildWithName(FILE_MAPPINGS_QN);

        for (Iterator mappingIterator =
                fileMappingElement.getChildrenWithName(MAPPING_QN);
             mappingIterator.hasNext();) {
            OMElement mapping = (OMElement) mappingIterator.next();

            OMElement tokenEle = mapping.getFirstChildWithName(TOKEN_QN);
            OMElement filesEle = mapping.getFirstChildWithName(FILE_QN);
            //First pass the given extensions

            String convertedFileToString =
                    convertMappingFileToString(resourceBase, filesEle.getText(), orderedExts);

            fileMappingMap.put(tokenEle.getText(), convertedFileToString);
        }


    }

    private static void parseExtensionOrder(OMElement documentElement,
                                            Collection extensionCollection) {

        OMElement extensionOrderElement =
                documentElement.getFirstChildWithName(EXTENSIONS_ORDER_QN);


        for (Iterator itemIterator = extensionOrderElement.getChildrenWithName(ITEM_QN);
             itemIterator.hasNext();) {
            OMElement item = (OMElement) itemIterator.next();
            extensionCollection.add(item.getText());

        }

    }

    private static String getFileStr(String resourceBase,
                                     String fileName) throws IOException {
        File file = new File(resourceBase + "/" + fileName);
        FileInputStream fis = new FileInputStream(file);
        byte[] bytes = new byte[(int) file.length()];
        int length = fis.read(bytes);
        if (length == 0) {
            log.warn(fileName + " read is 0 bytes");
        }
        return new String(bytes);
    }

    /**
     * @param resourceBase
     * @param mappingFile
     * @param orderedExts
     * @return String
     */
    private static String convertMappingFileToString(String resourceBase, String mappingFile,
                                                     Collection orderedExts) {
        // Find the verty first one
        String finalContentet = "";
        for (Iterator fileCollectionIterator = orderedExts.iterator();
             fileCollectionIterator.hasNext();) {
            String extension = (String) fileCollectionIterator.next();
            try {
                File menuHTML = new File(resourceBase + "/extensions/" +
                                         extension + "/" + mappingFile);
                if (menuHTML.exists()) {
                    FileInputStream fis2 = new FileInputStream(menuHTML);
                    byte[] bytesMenusHtml = new byte[(int) menuHTML.length()];
                    int length = fis2.read(bytesMenusHtml);
                    if (length == 0) {
                        log.warn(menuHTML + " read is 0 bytes");
                    }
                    finalContentet = finalContentet + new String(bytesMenusHtml);
                }
            } catch (FileNotFoundException e) {
                log.error(mappingFile + " file not found for UI extension " + extension + e);
            } catch (IOException e) {
                log.error(e);
            }

        }
        // Load the unordered extensions
        File[] unorderedExtensions = new File(resourceBase + "/extensions").listFiles();
        if (unorderedExtensions != null) {
            for (int i = 0; i < unorderedExtensions.length; i++) {
                String extension = unorderedExtensions[i].getName();
                // If not in ordered Extensions
                if (!orderedExts.contains(extension)) {
                    File dir = new File(resourceBase + "/extensions/" + extension);
                    if (dir.isDirectory()) {
                        try {
                            File menuHTML = new File(resourceBase + "/extensions/" +
                                                     extension + "/" + mappingFile);
                            if (menuHTML.exists()) {
                                FileInputStream fis2 = new FileInputStream(menuHTML);
                                byte[] bytesMenusHtml = new byte[(int) menuHTML.length()];
                                int length = fis2.read(bytesMenusHtml);
                                if (length == 0) {
                                    log.warn(menuHTML + " read is 0 bytes");
                                }
                                finalContentet = finalContentet + new String(bytesMenusHtml);
                            }
                        } catch (FileNotFoundException e) {
                            log.error(e);
                        } catch (IOException e) {
                            log.error(e);
                        }
                    }
                }
            }
        }
        return finalContentet;
    }

}
