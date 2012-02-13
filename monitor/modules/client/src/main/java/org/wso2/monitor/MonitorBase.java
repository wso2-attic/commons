/*
 * Copyright 2006,2007 WSO2, Inc. http://www.wso2.org
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
package org.wso2.monitor;

import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;

import javax.swing.*;
import java.util.Properties;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Base class for monitor application. Initializes logging and retrieves configuration settings.
 */
public class MonitorBase {
    protected Properties monitorProperties;
    protected Logger log = Logger.getLogger(getClass());

    /**
     * The server can be running, shut down or in an intermediate state (starting or shutting down).
     * Alternatively the monitor may be unaware of server state.
     */
    public enum ServerState {
        UNCONNECTED,
        INTERMEDIATE,
        RUNNING,
        ERROR,
        STOPPED
    }

    /**
     * Initialize logging.
     */
    {
        BasicConfigurator.configure();
    }

    /**
     * Base class constructor makes configuration information available to implementing classes.
     */
    public MonitorBase() {
        monitorProperties = getConfigurationSettings();
    }

    /**
     * Retrieves configuration settings from a properties file.
     *
     * @return A properties object containing the configuration settings for the application.
     */
    private Properties getConfigurationSettings() {
        Properties monitorProperties = new Properties();
        try {
            ClassLoader loader = ClassLoader.getSystemClassLoader();
            InputStream inputStream =
                    loader.getResourceAsStream("properties/servicemonitor.properties");
            monitorProperties.load(inputStream);
            inputStream.close();
        } catch (IOException ioe) {
            log.error("Error retrieving properties.", ioe);
        }
        return monitorProperties;
    }

    /**
     * Retreives a given image from the classpath.
     * @param iconPath The path to the required image.
     * @return ImageIcon object containing the specified image.
     */
    protected ImageIcon getIcon(String iconPath) {
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        URL iconURL = loader.getResource(iconPath);
        if (iconURL != null) {
            return new ImageIcon(iconURL);
        } else {
            log.error("Image not available at: " + iconPath);
            return null;
        }
    }
}
