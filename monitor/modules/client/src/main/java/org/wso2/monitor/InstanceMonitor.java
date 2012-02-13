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

import org.jdesktop.jdic.desktop.Desktop;
import org.jdesktop.jdic.desktop.DesktopException;
import org.jdesktop.jdic.tray.TrayIcon;
import org.wso2.monitor.jmx.ServerMonitorClient;
import org.wso2.monitor.jmx.WrapperClient;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Instance monitor controls a single server instance.
 */
public class InstanceMonitor extends MonitorBase implements ActionListener {
    private WrapperClient wrapperClient;
    private ServerMonitorClient monitorClient;
    private JMenu instanceMenu;
    private JPopupMenu parentMenu;
    private JMenuItem startMenuItem;
    private JMenuItem restartMenuItem;
    private JMenuItem stopMenuItem;
    private JMenuItem configMonMenuItem;
    private JMenuItem adminMenuItem;
    private JMenuItem messagesMenuItem;
    private JMenuItem titleMenuItem;
    private String serviceName;
    private ServerState displayedState;
    private Timer pollingTimer;
    private int instanceNumber;
    private MessageDisplay display;
    private int connectionRetries;
    private int retryCount;
    private String serverHome;
    int pollInterval;

    /**
     * Constructor to be used when creating an instance menu as a subordimate menu item.
     * Instantiates menu and initializes the polling process which updates server instance status.
     *
     * @param display        Instance of the message display implementation object.
     * @param instanceNumber Instance number of the server being monitored.
     * @param serverHome     Home directory of monitored server.
     */
    public InstanceMonitor(MessageDisplay display, int instanceNumber, String serverHome) {
        initInstance(display, instanceNumber, serverHome);
        this.instanceMenu = new JMenu(serviceName);
        buildInstanceMenu();
        buildChildUI();
        setStatus(ServerState.UNCONNECTED);
        setupStatusChecker();
    }

    /**
     * Constructor to be used when creating an instance menu as a top level menu item.
     * Instantiates menu and initializes the polling process which updates server instance status.
     *
     * @param display        Instance of the message display implementation object.
     * @param instanceNumber Instance number of the server being monitored.
     * @param serverHome     Home directory of monitored server.
     * @param mainMenu instance of the main menu to which sumenu is to be appended.
     */
    public InstanceMonitor(MessageDisplay display, int instanceNumber, String serverHome,
                           JPopupMenu mainMenu) {
        initInstance(display, instanceNumber, serverHome);
        this.parentMenu = mainMenu;
        titleMenuItem = new JMenuItem(serviceName);
        titleMenuItem.addActionListener(this);
        parentMenu.add(titleMenuItem);
        parentMenu.addSeparator();
        buildInstanceMenu();
        buildTopLevelUI();
        setStatus(ServerState.UNCONNECTED);
        setupStatusChecker();
    }

    /**
     * Instantiates JMX Client and retrieved instance setting from property files.
     * 
     * @param display        Instance of the message display implementation object.
     * @param instanceNumber Instance number of the server being monitored.
     * @param serverHome     Home directory of monitored server.
     */
    private void initInstance(MessageDisplay display, int instanceNumber, String serverHome) {
        this.serverHome = serverHome;
        this.display = display;
        this.instanceNumber = instanceNumber;
        serviceName = getProperty(".name");
        String connectorAddress = getProperty(".url");
        String wrapperBeanName = getProperty(".wrappermbeanname");
        wrapperClient = new WrapperClient(connectorAddress, wrapperBeanName);
        monitorClient = new ServerMonitorClient(display, connectorAddress,
                                                MonitorConstants.SERVER_MBEAN_NAME);

        // Property determines the maximum number of retries to connect.
        connectionRetries = Integer.parseInt(monitorProperties.getProperty(
                "monitor.connectionretries"));
        pollInterval = Integer.parseInt(monitorProperties.getProperty("monitor.pollinterval"));
    }

    /**
     * Setup timer to check status.
     */
    private void setupStatusChecker() {
        pollingTimer = new Timer(pollInterval, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refreshStatus();
            }
        });
        pollingTimer.setInitialDelay(pollInterval);
        pollingTimer.start();
    }

    /**
     * Gets the menu for this instance monitor.
     *
     * @return The submenu representing this monitored instance.
     */
    public JMenu getInstanceMenu() {
        return instanceMenu;
    }

    /**
     * Handles the popup menu action events, calling methods to perform requested actions.
     *
     * @param e The action event.
     */
    public void actionPerformed(ActionEvent e) {
        JMenuItem source = (JMenuItem) (e.getSource());
        String menuText = source.getText();
        if (menuText.equals(monitorProperties.getProperty("monitor.options.start"))) {
            startServer();
        } else if (menuText.equals(monitorProperties.getProperty("monitor.options.restart"))) {
            restartServer();
        } else if (menuText.equals(monitorProperties.getProperty("monitor.options.stop"))) {
            stopServer();
        } else if (menuText.equals(monitorProperties.getProperty("monitor.options.configmon"))) {
            configureMonitor();
        } else if (menuText.equals(monitorProperties.getProperty("monitor.options.messages"))) {
            getMessages();
        } else if (menuText.equals(monitorProperties.getProperty("monitor.options.admin"))) {
            showAdminConsole();
        } else if (menuText.equals(serviceName)) {
            showAdminConsole();
        }
    }

    /**
     * Builds the server control menu.
     */
    private void buildInstanceMenu() {
        // Start menu icon.
        ImageIcon startIcon = getIcon(MonitorConstants.START_IMAGE);
        startMenuItem = new JMenuItem(monitorProperties.getProperty("monitor.options.start"),
                                      startIcon);
        startMenuItem.addActionListener(this);

        // Restart menu item.
        ImageIcon restartIcon = getIcon(MonitorConstants.RESTART_IMAGE);
        restartMenuItem = new JMenuItem(monitorProperties.getProperty("monitor.options.restart"),
                                        restartIcon);
        restartMenuItem.addActionListener(this);

        // Stop menu item.
        ImageIcon stopIcon = getIcon(MonitorConstants.STOP_IMAGE);
        stopMenuItem = new JMenuItem(monitorProperties.getProperty("monitor.options.stop"),
                                     stopIcon);
        stopMenuItem.addActionListener(this);

        // Get messages menu item.
        messagesMenuItem = new JMenuItem(monitorProperties.getProperty("monitor.options.messages"));
        messagesMenuItem.addActionListener(this);

        // Admin console menu item.
        adminMenuItem = new JMenuItem(monitorProperties.getProperty("monitor.options.admin"));
        adminMenuItem.addActionListener(this);

        // Configure monitor menu item.
        configMonMenuItem =
                new JMenuItem(monitorProperties.getProperty("monitor.options.configmon"));
        configMonMenuItem.addActionListener(this);
    }

    /**
     * Builds the menu UI at the top level.
     */
    private void buildTopLevelUI() {
        // Start menu icon.
        parentMenu.add(startMenuItem);

        // Restart menu item.
        parentMenu.add(restartMenuItem);

        // Stop menu item.
        parentMenu.add(stopMenuItem);

        // Get messages menu item.
        parentMenu.addSeparator();
        parentMenu.add(messagesMenuItem);

        // Admin console menu item.
        parentMenu.add(adminMenuItem);

        // Configure monitor menu item.
        parentMenu.add(configMonMenuItem);
    }

    /**
     * Builds the menu UI at a child level.
     */
    private void buildChildUI() {
        // Start menu icon.
        instanceMenu.add(startMenuItem);

        // Restart menu item.
        instanceMenu.add(restartMenuItem);

        // Stop menu item.
        instanceMenu.add(stopMenuItem);

        // Get messages menu item.
        instanceMenu.addSeparator();
        instanceMenu.add(messagesMenuItem);

        // Admin console menu item.
        instanceMenu.add(adminMenuItem);

        // Configure monitor menu item.
        instanceMenu.add(configMonMenuItem);
    }

    /**
     * Enables or disables menu options and set the tray icon based on server status.
     *
     * @param state Status to be indicated.
     */
    private void setStatus(ServerState state) {
        // Only update the display is there is a state change.
        if (displayedState == null || displayedState != state) {
            displayedState = state;

            // Add the tray image.
            switch (state) {
                case RUNNING:
                    startMenuItem.setEnabled(false);
                    restartMenuItem.setEnabled(true);
                    stopMenuItem.setEnabled(true);
                    adminMenuItem.setEnabled(true);
                    messagesMenuItem.setEnabled(true);
                    break;
                case ERROR:
                    // If the server has an error, allow all options.
                    startMenuItem.setEnabled(true);
                    restartMenuItem.setEnabled(true);
                    stopMenuItem.setEnabled(true);
                    adminMenuItem.setEnabled(true);
                    messagesMenuItem.setEnabled(true);
                    break;
                case INTERMEDIATE:
                    startMenuItem.setEnabled(false);
                    restartMenuItem.setEnabled(false);
                    stopMenuItem.setEnabled(false);
                    adminMenuItem.setEnabled(true);
                    messagesMenuItem.setEnabled(false);
                    break;
                case UNCONNECTED:
                    startMenuItem.setEnabled(true);
                    restartMenuItem.setEnabled(false);
                    stopMenuItem.setEnabled(false);
                    adminMenuItem.setEnabled(true);
                    messagesMenuItem.setEnabled(false);
                    break;
                case STOPPED:
                    startMenuItem.setEnabled(true);
                    restartMenuItem.setEnabled(false);
                    stopMenuItem.setEnabled(false);
                    adminMenuItem.setEnabled(false);
                    messagesMenuItem.setEnabled(false);                    
                    break;
            }

            display.showStatus(state, serviceName);
        }
    }

    /**
     * Check if we are connected to the server and if we are, find out the server status.
     */
    private void refreshStatus() {
        try {
            // If we aren't connected, we can't check, so proceed only if we are.
            if (wrapperClient.isConnected() && monitorClient.isConnected()) {
                // Check if the server is running and if it's error free.
                if (monitorClient.isServerErrorFree()) {
                    if (monitorClient.isServerRunning()) {
                        setStatus(ServerState.RUNNING);
                    } else {
                        setStatus(ServerState.STOPPED);
                    }

                    // Ensure monitor is listening to server events.
                    if (!monitorClient.isListenerRegistered()) {
                        monitorClient.registerListener();
                    }
                } else {
                    setStatus(ServerState.ERROR);
                }
            } else {
                if (wrapperClient.getHasConnected()) {
                    setStatus(ServerState.ERROR);
                } else {
                    setStatus(ServerState.UNCONNECTED);
                }

                // If the connection is not active, notification listener needs to be re-registered.
                monitorClient.setListenerRegistered(false);

                // If retry threshold is unlimited or has not been reached, attempt to reconnect.
                if (connectionRetries == 0 || retryCount < connectionRetries) {
                    log.info("Connection attempt: " + retryCount);
                    connect();
                    retryCount++;
                }
            }
        } catch (MonitorException e) {
            log.error("Error determining server status", e);
        }
    }

    /**
     * Connect to the server to be monitored. Catching exception as no errors should be propogated
     * if connection fails.
     */
    private boolean connect() {
        boolean connected = false;
        log.debug("Attempting to connect.");

        try {
            // Connect clients to MBeans.
            wrapperClient.connectToServer();
            monitorClient.connectToServer();

            // Register notification listener.
            monitorClient.registerListener();

            // If the timer has been stopped - start it again.
            if (!pollingTimer.isRunning()) {
                pollingTimer.start();
            }
            connected = true;
        } catch (MonitorException e) {
            log.error("MonitorException connecting to server for monitoring.");
        } catch (Exception e) {
            log.error("Exception connecting to server for monitoring!");
        }
        return connected;
    }

    /**
     * Checks if the wrapper binary exists in the specified path. Using this
     * we can determine if the service being monitored can be accessed locally.
     *
     * @return True if both files are available.
     */
    private boolean isServiceLocal() {
        boolean found;
        File wrapperBinary = new File(getWrapperPath());
        log.debug("Checking for wrapper binary at:" + wrapperBinary.getAbsolutePath());
        found = wrapperBinary.exists();
        log.debug("Binary found =" + found);
        return found;
    }

    /**
     * Provides the path to the monitored service, relative or absolute based on whether the server
     * home directory has been provided.
     *
     * @return String path.
     */
    private String getWrapperPath() {
        String wrapperPath = getProperty(".bin");

        // If monitored server's home directory is known, use that instead of relative path.
        if (serverHome != null && wrapperPath.startsWith("..")) {
            log.debug("Initially serverHome:" + serverHome + " and path:" + wrapperPath);
            wrapperPath = serverHome.concat(wrapperPath.substring(2));
        }

        return wrapperPath;
    }

    /**
     * Try to connect, in case an independantly started server instance is already running. If
     * conection fails, assume server is not running and try to start an instance.
     */
    private void startServer() {
        log.debug("Start menu item selected!");

        setStatus(ServerState.INTERMEDIATE);

        // Try to connect, failing which try to start. 
        if (connect()) {
            log.debug("Connected to a running instance of the server.");
        } else {
            log.debug("No running instance detected. Checking for local executable to start server.");
            if (isServiceLocal()) {
                try {
                    Runtime runtime = Runtime.getRuntime();
                    String[] command = new String[3];
                    command[0] = getWrapperPath();

                    // Setup the start argument based on operating system, defaulting to the command line.
                    String osName = System.getProperty("os.name");
                    if (osName != null) {
                        if (osName.startsWith("Windows")) {
                            command[1] = getProperty(".startwindows");
                        } else {
                            command[1] = getProperty(".startlinux");
                        }
                    } else {
                        command[1] = "-c";
                    }

                    command[2] = getProperty(".conf");
                    log.debug("Start command: " + java.util.Arrays.toString(command));
                    runtime.exec(command);

                    // If the timer has been stopped - start it again, resetting the connection attempts.
                    retryCount = 0;
                    if (!pollingTimer.isRunning()) {
                        pollingTimer.start();
                    }
                } catch (IOException ioe) {
                    log.error("Error executing service start", ioe);
                }
            } else {
                log.error("Service executable not found - cannot start server.");
            }
        }
    }

    /**
     * Restart the server being monitored.
     */
    private void restartServer() {
        try {
            log.debug("Restart menu item selected!");
            monitorClient.removeListener();
            wrapperClient.restartServer();
            retryCount = 0;
            setStatus(ServerState.INTERMEDIATE);
        } catch (MonitorException e) {
            log.error("Error restarting server", e);
        }
    }

    /**
     * Get any messages queued on the server being monitored.
     */
    private void getMessages() {
        try {
            log.debug("Messages menu item selected!");
            String message = monitorClient.getMessages();
            log.debug("Received message" + message);
            display.showMessage("Message", message, TrayIcon.NONE_MESSAGE_TYPE);
        } catch (MonitorException e) {
            log.error("Error getting messages", e);
        }
    }

    /**
     * Display the administration console using the browser.
     */
    private void showAdminConsole() {
        String urlString = getProperty(".adminurl");

        try {
            URL adminURL = new URL(urlString);
            Desktop.browse(adminURL);
        } catch (DesktopException e) {
            log.error("Error opening default browser", e);
        } catch (MalformedURLException e) {
            log.error("Admin URL incorrect", e);
        }
    }

    /**
     * Stop the server being monitored.
     */
    private void stopServer() {
        try {
            log.debug("Stop menu item selected!");

            // If the timer is running - stop it.
            if (pollingTimer.isRunning()) {
                pollingTimer.stop();
            }
            monitorClient.removeListener();
            wrapperClient.stopServer();
            setStatus(ServerState.STOPPED);
        } catch (MonitorException e) {
            log.error("Error stopping server", e);
        }
    }

    /**
     * Reconfigure the monitoring parameters.
     */
    private void configureMonitor() {
        log.debug("Configure menu item selected!");

        // If the timer is running - stop it.
        if (pollingTimer.isRunning()) {
            pollingTimer.stop();
        }
        ConfigScreen confScreen = new ConfigScreen(wrapperClient);
        confScreen.showMonitorSettings();
        setStatus(ServerState.INTERMEDIATE);
        retryCount = 0;
        pollingTimer.start();
    }

    /**
     * Get the value for a given property from the configuration file.
     * @param propertyKey Property being queried.
     * @return value of property.
     */
    private String getProperty(String propertyKey) {
        return monitorProperties.getProperty("monitor.service." + instanceNumber + propertyKey);
    }
}

