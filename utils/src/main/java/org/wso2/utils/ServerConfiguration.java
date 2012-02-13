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
package org.wso2.utils;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class ServerConfiguration {

    public static final String AXIS2_CONFIG_REPO_LOCATION = "Axis2Config.RepositoryLocation";
    public static final String HTTP_PORT = "HTTP.Port";
    public static final String COMMAND_LISTENER_PORT = "CommandListener.Port";

    private static Log log = LogFactory.getLog(ServerConfiguration.class);

    private OMElement documentElement;
    private Map configuration = new HashMap();

    private static ServerConfiguration instance = new ServerConfiguration();

    public static ServerConfiguration getInstance() {
        return instance;
    }

    private ServerConfiguration() {
    }

    public synchronized void init(InputStream xmlInputStream) throws ServerConfigurationException {
        try {
            documentElement = new StAXOMBuilder(xmlInputStream).getDocumentElement();
        } catch (XMLStreamException e) {
            log.fatal("Problem in parsing the configuration file ", e);
            throw new ServerConfigurationException(e);
        }
        Stack nameStack = new Stack();
        readChildElements(documentElement, nameStack);
    }

    /**
     * init should only be called once, for successive calls, it will be checked.
     *
     * @param configurationXMLLocation
     *
     * @throws ServerConfigurationException
     */
    public synchronized void init(String configurationXMLLocation)
            throws ServerConfigurationException {
        if (configurationXMLLocation == null) {
            configurationXMLLocation = "conf/server.xml";
        }

        InputStream xmlInputStream;
        try {
            //URL will parse the location according to respective RFC's and open a connection.
            URL urlXMLLocation = new URL(configurationXMLLocation);
            xmlInputStream = urlXMLLocation.openStream();
        } catch (MalformedURLException e) {
            File f = new File(configurationXMLLocation);
            try {
                xmlInputStream = new FileInputStream(f);
            } catch (FileNotFoundException e1) {
                // As a last resort test in the classpath
                ClassLoader cl = ServerConfiguration.class.getClassLoader();
                xmlInputStream = cl.getResourceAsStream(configurationXMLLocation);
                if (xmlInputStream == null) {
                    log.fatal(
                            "Configuration File cannot be loaded from " + configurationXMLLocation,
                            e1);
                    throw new ServerConfigurationException(e1);

                }
            }
        } catch (IOException e) {
            log.fatal("Configuration File cannot be loaded from " + configurationXMLLocation, e);
            throw new ServerConfigurationException(e);
        }
        init(xmlInputStream);
    }

    private void readChildElements(OMElement serverConfig, Stack nameStack) {
        for (Iterator childElements = serverConfig.getChildElements();
             childElements.hasNext();) {
            OMElement element = (OMElement) childElements.next();
            nameStack.push(element.getLocalName());
            if (elementHasText(element)) {
                String key = getKey(nameStack);
                Object currentObject = configuration.get(key);

// when we try to add object, that location of map may
// 1. not contain an object
// 2. may contain one object
// 3. may contain a list of objects
                String value = replaceSystemProperty(element.getText());
                if (currentObject == null) {
                    configuration.put(key, value);
                } else if (currentObject instanceof ArrayList) {
                    ArrayList list = (ArrayList) currentObject;
                    if (!list.contains(value)) {
                        list.add(value);
                    }
                } else {
                    if (!value.equals(currentObject)) {
                        ArrayList arrayList = new ArrayList(2);
                        arrayList.add(currentObject);
                        arrayList.add(value);
                        configuration.put(key, arrayList);
                    }
                }
            }
            readChildElements(element, nameStack);
            nameStack.pop();
        }
    }

    private String replaceSystemProperty(String text) {
        int indexOfStartingChars;
        int indexOfClosingBrace;

// The following condition deals with properties.
// Properties are specified as ${system.property},
// and are assumed to be System properties
        if ((indexOfStartingChars = text.indexOf("${")) != -1 &&
            (indexOfClosingBrace = text.indexOf("}")) != -1) { // Is a property used?
            String sysProp = text.substring(indexOfStartingChars + 2,
                                            indexOfClosingBrace);
            String propValue = System.getProperty(sysProp);
            if (propValue != null) {
                text = text.substring(0, indexOfStartingChars) + propValue +
                       text.substring(indexOfClosingBrace + 1);
            }
        }
        return text;
    }

    public void setConfigurationProperty(String key, String value) {
        configuration.put(key, value);

        StringTokenizer tokenizer = new StringTokenizer(key, ".");
        OMElement ele = documentElement;
        String token = "";
        while (tokenizer.hasMoreTokens()) {
            token = tokenizer.nextToken();
            if (ele != null) {
                ele = ele.getFirstChildWithName(new QName("", token, ""));
            } else {
                break;
            }
        }
        if (ele != null) {
            ele.getFirstOMChild().detach();
            ele.setText(token);
        }
    }

    private String getKey(Stack nameStack) {
        StringBuffer key = new StringBuffer();
        for (int i = 0; i < nameStack.size(); i++) {
            String name = (String) nameStack.elementAt(i);
            key.append(name).append(".");
        }
        key.deleteCharAt(key.lastIndexOf("."));

        return key.toString();
    }

    private boolean elementHasText(OMElement element) {
        String text = element.getText();
        return text != null && text.trim().length() != 0;
    }

    /**
     * There can be multiple objects with the same key. This will return the first String from them
     *
     * @param key
     *
     * @return value corresponding to the given key
     */
    public String getFirstProperty(String key) {
        Object value = configuration.get(key);
        if (value instanceof ArrayList) {
            return (String) ((ArrayList) value).get(0);
        }
        return (String) value;
    }

    /**
     * There can be multiple object corresponding to the same object.
     *
     * @param key
     *
     * @return the properties corresponding to the <code>key</code>
     */
    public String[] getProperties(String key) {
        Object values = configuration.get(key);
        String[] properties = new String[0];
        if (values instanceof ArrayList) {
            properties =
                    (String[]) ((ArrayList) values).
                            toArray(new String[((ArrayList) values).size()]);
        } else if (values instanceof String) {
            properties = new String[]{(String) values};
        }
        return properties;
    }

    public OMElement getDocumentElement() {
        return documentElement;
    }
}
