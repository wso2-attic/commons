/*
 * Copyright 2005-2007 WSO2, Inc. (http://wso2.com)
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

import org.apache.axis2.AxisFault;
import org.apache.axis2.deployment.DeploymentConstants;
import org.apache.axis2.deployment.DeploymentEngine;
import org.apache.axis2.description.AxisModule;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.engine.AxisConfigurator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * WSO2 WSAS's implementation of AxisConfigurator to load Axis2
 * configuration for WSO2 WSAS.
 */
public class ServerConfigurator extends DeploymentEngine implements AxisConfigurator {

    private static Log log = LogFactory.getLog(ServerConfigurator.class);

    private List globallyEngagedModules = new ArrayList();
    private String axis2xml;
    private String repoLocation;
    private String webLocation;

    private static ServerConfigurator instance;

    private boolean isInitialized;
    private boolean isUrlRepo;
    private boolean isUrlAxis2Xml;

    public boolean isInitialized() {
        return isInitialized;
    }

    public static ServerConfigurator getInstance() {
        if (instance == null) {
            instance = new ServerConfigurator();
        }
        return instance;
    }

    private ServerConfigurator() {
    }

    /**
     * Load an AxisConfiguration from the repository directory specified
     *
     * @param repoLocation
     * @param weblocation
     * @throws ServerException
     */
    public void init(String repoLocation, String weblocation) throws ServerException {
        if (repoLocation == null) {
            throw new ServerException("Axis2 repository not specified!");
        }
        this.webLocation = weblocation;

        // Check whether this is a URL
        isUrlRepo = WSO2Utils.isURL(repoLocation);

        if (isUrlRepo) { // Is repoLocation a URL Repo?
            try {
                new URL(repoLocation).openConnection().connect();
            } catch (IOException e) {
                throw new ServerException("Cannot connect to URL repository " + repoLocation, e);
            }
            this.repoLocation = repoLocation;
        } else { // Is repoLocation a file repo?
            File repo = new File(repoLocation);
            if (repo.exists()) {
                this.repoLocation = repo.getAbsolutePath();
            } else {
                this.repoLocation =
                        System.getProperty("wso2wsas.home") + File.separator +
                        repoLocation;
                repo = new File(this.repoLocation);
                if (!repo.exists()) {
                    this.repoLocation = null;
                    throw new ServerException("Repository location '" + repoLocation +
                                              "' not found!");
                }
            }
        }

        axis2xml = WSO2Utils.getAxis2Xml();

        isUrlAxis2Xml = WSO2Utils.isURL(axis2xml);

        if (!isUrlAxis2Xml) { // Is axis2xml a URL to the axis2.xml file?
            File configFile = new File(axis2xml);
            if (!configFile.exists()) {
                this.axis2xml = null;
                throw new ServerException("axis2.xml '" + axis2xml + "' not found!");
            }
        } else {
            try {
                URLConnection urlConnection = new URL(axis2xml).openConnection();
                urlConnection.connect();
            } catch (IOException e) {
                throw new ServerException("Cannot connect to axis2.xml URL " + repoLocation, e);
            }
            isInitialized = true;
        }
    }

    /**
     * First create a Deployment engine, use that to create an AxisConfiguration
     *
     * @return Axis Configuration
     * @throws AxisFault
     */
    public synchronized AxisConfiguration getAxisConfiguration() throws AxisFault {
        axisConfig = null;
        try {
            InputStream axis2xmlStream;
            if (axis2xml != null && axis2xml.trim().length() != 0) {
                if (isUrlAxis2Xml) { // Is it a URL?
                    try {
                        axis2xmlStream = new URL(axis2xml).openStream();
                    } catch (IOException e) {
                        throw new AxisFault("Cannot load axis2.xml from URL", e);
                    }
                } else { // Is it a File?
                    axis2xmlStream = new FileInputStream(axis2xml);
                }
            } else {
                ClassLoader cl = Thread.currentThread().getContextClassLoader();
                axis2xmlStream =
                        cl.getResourceAsStream(DeploymentConstants.AXIS2_CONFIGURATION_RESOURCE);
            }
            populateAxisConfiguration(axis2xmlStream);
        } catch (FileNotFoundException e) {
            throw new AxisFault("System cannot find the given axis2.xml " + axis2xml);
        }
        globallyEngagedModules = (List) axisConfig.getEngagedModules();
        if (repoLocation != null && repoLocation.trim().length() != 0) {
            try {
                if (isUrlRepo) {
                    URL axis2Repository = new URL(repoLocation);
                    axisConfig.setRepository(axis2Repository);
                    loadRepositoryFromURL(axis2Repository);
                } else {
                    axisConfig.setRepository(new URL("file://" + repoLocation));
                    loadRepository(repoLocation);
                }
            } catch (MalformedURLException e) {
                throw new AxisFault("Invalid URL", e);
            }
        } else {
            loadFromClassPath();
        }
        
        for (Iterator iterator = globallyEngagedModules.iterator(); iterator.hasNext();) {
            String name = (String) iterator.next();
            log.info("Globally engaging module: " + name);
        }
        axisConfig.setConfigurator(this);
        return axisConfig;
    }

    public boolean isGlobalyEngaged(AxisModule axisModule) {
        String modName = axisModule.getName();
        for (Iterator iterator = globallyEngagedModules.iterator(); iterator.hasNext();) {
            String name = (String) iterator.next();
            if (modName.startsWith(name)) {
                return true;
            }
        }
        return false;
    }

    public void engageGlobalModules() throws AxisFault {
        engageModules();
    }

    public void loadServices() {
        setWebLocationString(webLocation);
        if (repoLocation != null && repoLocation.trim().length() != 0) {
            if (isUrlRepo) {
                try {
                    loadServicesFromUrl(new URL(repoLocation));
                } catch (MalformedURLException e) {
                    log.error("Services repository URL " + repoLocation + " is invalid");
                }
            } else {
                super.loadServices();
            }
        }
    }
}
