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

import org.jdesktop.jdic.tray.SystemTray;
import org.jdesktop.jdic.tray.TrayIcon;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

/**
 * Service monitor displays a system tray icon indicating the status of the service being monitored.
 * Also displays a menu using which server instances can be controlled.
 */
public class ServiceMonitor extends MonitorBase implements MessageDisplay, ActionListener {
    SystemTray defaultTray = SystemTray.getDefaultSystemTray();
    TrayIcon trayIcon;
    JPopupMenu monitorMenu;
    JMenuItem quitMenuItem;
    HashMap monitoredInstances;
    String[] serverHomes;
    boolean singleInstance = false;

    /**
     * Constructor, calls base class constructor.
     */
    public ServiceMonitor() {
        super();
    }

    /**
     * Starts termination listener thread, then instantiates pop-up menu and hash map which stores
     * instances of server monitors.
     */
    private void init() {
        Thread stopListenerThread = new Thread(new TerminateListener());
        stopListenerThread.start();

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        monitorMenu = new JPopupMenu();
        monitoredInstances = new HashMap();
        buildMenu();
        initTrayIcon();
    }

    /**
     * Signal the running instance of the monitor to shutdown.
     */
    private void shutDown() {
        int monitorPort = Integer.parseInt(monitorProperties.getProperty("monitor.listenport"));

        // Connect to the listener and send the termination string.
        try {
            Socket senderSocket = new Socket(MonitorConstants.LOCALHOST, monitorPort);
            PrintWriter sender = new PrintWriter(senderSocket.getOutputStream(), true);
            sender.println(MonitorConstants.TRANSMIT_TERMINATE);
        } catch (UnknownHostException e) {
            log.error("Unknown localhost", e);
        } catch (IOException e) {
            log.error("Error sending terminate transmission", e);
        }
    }

    /**
     * Instantiates the service monitor application.
     *
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        ServiceMonitor serviceMonitor = new ServiceMonitor();

        // If no arguments have been provided, assume start.
        if (args.length == 0) {
            serviceMonitor.init();
        } else {
            // If arguments have been provided, first is either start or stop.
            if (args[0].equalsIgnoreCase(MonitorConstants.COMMAND_STOP)) {
                serviceMonitor.shutDown();
            } else {
                // Anything after that would be the home directories of monitored servers.
                if (args.length > 1) {
                    String[] serverLocations = new String[args.length - 1];
                    System.arraycopy(args, 1, serverLocations, 0, serverLocations.length);
                    serviceMonitor.setServerHomes(serverLocations);
                }
                serviceMonitor.init();
            }
        }
    }

    /**
     * Displays a given message with the speficified title and content.
     *
     * @param title    Message title.
     * @param message  Message text.
     * @param severity determines icon to be used when dispaying message.
     */
    public void showMessage(String title, String message, int severity) {
        // Ensure that out of bound input is set to type 'none'.
        if (severity < 0 || severity > 3) {
            severity = TrayIcon.NONE_MESSAGE_TYPE;
        }

        if (trayIcon != null) {
            trayIcon.displayMessage(title, message, severity);
        }
    }

    /**
     * Builds the monitor control menu adding a submenu for each monitored instance.
     */
    private void buildMenu() {
        int instanceNumber = 0;
        InstanceMonitor instanceMonitor;
        String instanceHome = null;

        // Add a menu for each configured server instance.
        String instanceName = monitorProperties.getProperty("monitor.service." + instanceNumber +
                ".name");
        while (instanceName != null) {
            // If server home directories have been specified at startup, use them.
            if (serverHomes != null && serverHomes.length > instanceNumber) {
                log.debug("Home for instance " + instanceNumber + " is " + serverHomes[
                        instanceNumber]);
                instanceHome = serverHomes[instanceNumber];
            }

            // If only a single instance is being monitored, it should be on the main menu.
            if (instanceNumber == 0 && monitorProperties.getProperty("monitor.service.1.name") ==
                    null) {
                singleInstance = true;
                instanceMonitor =
                        new InstanceMonitor(this, instanceNumber, instanceHome, monitorMenu);
            } else {
                instanceMonitor = new InstanceMonitor(this, instanceNumber, instanceHome);
                monitorMenu.add(instanceMonitor.getInstanceMenu());
            }


            monitoredInstances.put(instanceName, instanceMonitor);

            instanceNumber++;
            instanceName = monitorProperties.getProperty("monitor.service." + instanceNumber +
                    ".name");
        }

        // Quit menu item.
        monitorMenu.addSeparator();
        quitMenuItem = new JMenuItem(monitorProperties.getProperty("monitor.options.quit"));
        quitMenuItem.addActionListener(this);
        monitorMenu.add(quitMenuItem);
    }

    /**
     * Set the tray icon to indicate server status.
     *
     * @param state       Status to be indicated.
     * @param serviceName Name of service to be shown on display.
     */
    public void showStatus(ServerState state, String serviceName) {
        String trayImage = "";

        // Make sure icon is available before attempting to set it.
        if (trayIcon != null) {
            // Add the tray image.
            switch (state) {
                case RUNNING:
                    trayImage = MonitorConstants.SERVER_UP_IMAGE;
                    break;
                case ERROR:
                    trayImage = MonitorConstants.SERVER_ERROR_IMAGE;
                    break;
                case INTERMEDIATE:
                    trayImage = MonitorConstants.SERVER_INTERMEDIATE_IMAGE;
                    break;
                case UNCONNECTED:
                    trayImage = MonitorConstants.SERVER_UNCONNECTED_IMAGE;
                    break;
                case STOPPED:
                    trayImage = MonitorConstants.SERVER_DOWN_IMAGE;
                    break;
            }
            String trayText = serviceName + " - " + monitorProperties.getProperty(
                    "monitor.status." + state.toString().toLowerCase());
            log.debug("Tray Text" + trayText);
            trayIcon.setIcon(getIcon(trayImage));
            trayIcon.setCaption(trayText);
        }
    }

    /**
     * Initialize tray icon.
     */
    private void initTrayIcon() {
        String trayImage = MonitorConstants.SERVER_UNCONNECTED_IMAGE;
        String serviceName = singleInstance ? monitorProperties.getProperty(
                "monitor.service.0.name") : "Overall";
        String iconText = serviceName + " - " + monitorProperties.getProperty("monitor.status." +
                ServerState.UNCONNECTED.toString().toLowerCase());

        // Create and add the new status icon with the status message.
        ImageIcon i = getIcon(trayImage);
        trayIcon = new TrayIcon(i, iconText, monitorMenu);
        trayIcon.setIconAutoSize(true);

        // Add a listener to respond to clicks - show the menu.
        trayIcon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                monitorMenu.setLocation(trayIcon.getLocationOnScreen());
                monitorMenu.setVisible(true);
            }
        });
        defaultTray.addTrayIcon(trayIcon);
    }

    /**
     * Handles the popup menu action events, callig methods to perform requested actions.
     *
     * @param e The action event.
     */
    public void actionPerformed(ActionEvent e) {
        JMenuItem source = (JMenuItem) (e.getSource());
        String menuText = source.getText();
        if (menuText.equals(monitorProperties.getProperty("monitor.options.quit"))) {
            log.debug("Quit menu item selected!");
            System.exit(0);
        }
    }

    /**
     * Assign server home directory array.
     *
     * @param serverHomes String array of server home directories.
     */
    public void setServerHomes(String[] serverHomes) {
        this.serverHomes = serverHomes;
    }
}