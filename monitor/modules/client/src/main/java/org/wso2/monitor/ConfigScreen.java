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
package org.wso2.monitor;

import org.wso2.monitor.jmx.WrapperClient;

import javax.management.remote.JMXServiceURL;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;

/**
 * Displays a simple configuration screen to change the JMX Connector URL and MBean name.
 */
public class ConfigScreen extends MonitorBase implements ActionListener {
    JFrame configFrame;
    JTextField serviceURLText;
    JTextField mBeanNameText;
    WrapperClient jmxClient;
    JLabel statusBar;

    /**
     * Sets up the configration screen frame.
     *
     * @param jmxClient The JMX client which uses the configuration settings that can be changed.
     */
    public ConfigScreen(WrapperClient jmxClient) {
        this.jmxClient = jmxClient;
        // Setup frame.
        configFrame = new JFrame(monitorProperties.getProperty("monitor.configmon.title"));
        Image frameIcon = Toolkit.getDefaultToolkit().getImage(java.net.URLClassLoader.
                getSystemResource(MonitorConstants.SERVER_DOWN_IMAGE));
        configFrame.setIconImage(frameIcon);
        configFrame.setSize(410, 150);
    }

    /**
     * Draws the configuration screen and adds the even listener.
     */
    public void showMonitorSettings() {
        // Setup control sizes.
        Dimension fieldSize = new Dimension(310, 23);
        Dimension buttonSize = new Dimension(90, 23);

        // Service URL controls.
        JLabel urlLabel = new JLabel(monitorProperties.getProperty("monitor.configmon.serviceurl"));
        serviceURLText = new JTextField(jmxClient.getConnectorAddress());
        serviceURLText.setPreferredSize(fieldSize);

        // MBean name controls.
        JLabel beanLabel = new JLabel(monitorProperties.getProperty("monitor.configmon.mbeanname"));
        mBeanNameText = new JTextField(jmxClient.getMBeanName());
        mBeanNameText.setPreferredSize(fieldSize);

        // OK, Cancel buttons and status bar.
        final JButton okButton = new JButton(
                monitorProperties.getProperty("monitor.configmon.ok"));
        final JButton cancelButton = new JButton(
                monitorProperties.getProperty("monitor.configmon.cancel"));
        okButton.setPreferredSize(buttonSize);
        cancelButton.setPreferredSize(buttonSize);
        okButton.addActionListener(this);
        cancelButton.addActionListener(this);

        // Show initial status.
        String initMessage;
        if (jmxClient.isConnected()) {
            initMessage = monitorProperties.getProperty("monitor.configmsg.connected");
        } else {
            initMessage = monitorProperties.getProperty("monitor.configmsg.notconnected");
        }
        statusBar = new JLabel(initMessage);
        Border border = BorderFactory.createBevelBorder( BevelBorder.LOWERED );
        statusBar.setBorder(border);

        // Add edit controls to frame using a panel.
        JPanel controlPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        configFrame.add(controlPanel, BorderLayout.NORTH);
        configFrame.add(statusBar, BorderLayout.SOUTH);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.WEST;
        controlPanel.add(urlLabel, constraints);

        constraints.gridx = 1;
        controlPanel.add(serviceURLText, constraints);

        constraints.gridy = 1;
        controlPanel.add(mBeanNameText, constraints);

        constraints.gridx = 0;
        controlPanel.add(beanLabel, constraints);

        // Add buttons to frame using a panel.
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        constraints.gridy = 0;
        constraints.gridx = 0;
        buttonPanel.add(okButton, constraints);
        constraints.gridx = 1;
        buttonPanel.add(cancelButton, constraints);
        configFrame.add(buttonPanel, BorderLayout.CENTER);

        // Show everything.
        configFrame.setVisible(true);
    }

    /**
     * Handles the button click events, callig methods to perform requested actions.
     *
     * @param e The action event.
     */
    public void actionPerformed(ActionEvent e) {
        JButton source = (JButton) (e.getSource());
        String buttonText = source.getText();
        if (buttonText.equals(monitorProperties.getProperty("monitor.configmon.cancel"))) {
            log.debug("Cancel Clicked.");
            configFrame.setVisible(false);
        } else if (buttonText.equals(monitorProperties.getProperty("monitor.configmon.ok"))) {
            log.debug("OK Clicked.");
            reconnectToService();
        }
    }

    /**
     * Connects to the monitored service using the setting entered by the user.
     */
    private void reconnectToService() {
        String serviceURL = serviceURLText.getText();
        String mBeanName = mBeanNameText.getText();

        // Check if valid data has been entered.
        if (!"".equals(serviceURL) && !"".equals(mBeanName)) {
            try {
                JMXServiceURL validURL = new JMXServiceURL(serviceURL);

                // If a new target server and/or bean has been specified, disconnect and reconnect.
                if (!validURL.equals(new JMXServiceURL(jmxClient.getConnectorAddress())) ||
                        !mBeanName.equalsIgnoreCase(jmxClient.getMBeanName())) {
                    statusBar
                            .setText(monitorProperties.getProperty("monitor.configmsg.connecting"));
                    jmxClient.setConnectorAddress(serviceURL);
                    jmxClient.setMBeanName(mBeanName);
                    log.debug("Disconnecting from monitored server.");
                    jmxClient.disconnectFromServer();
                    log.debug("Disconnected. Connecting with updated information.");
                    jmxClient.connectToServer();
                    log.debug("Connected with updated information.");
                    // Close config screen if specified server can be monitored.
                    if (jmxClient.isConnected()) {
                        configFrame.setVisible(false);
                    } else {
                        log.error("Unable to connect using new settings.");
                        statusBar.setText(
                                monitorProperties.getProperty("monitor.configmsg.unabletoconnect"));
                    }
                } else {
                    statusBar.setText(monitorProperties.getProperty("monitor.configmsg.nochanges"));
                }
            } catch (MalformedURLException e) {
                log.error("Invalid service URL specified", e);
                statusBar.setText(monitorProperties.getProperty("monitor.configmsg.invalidurl"));
            } catch (MonitorException e) {
                log.error("Error connecting to specified service", e);
                statusBar.setText(
                        monitorProperties.getProperty("monitor.configmsg.unabletoconnect"));
            }
        } else {
            statusBar.setText(monitorProperties.getProperty("monitor.configmsg.invalidinput"));
        }
    }
}
