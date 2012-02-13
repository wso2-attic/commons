package org.wso2.carbon.common.test.utils.server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;


public class ServerAlternateStarter
        extends JFrame {

    JPanel jPanel1 = new JPanel();
    JScrollPane jScrollPane1 = new JScrollPane();
    JTextArea jTextArea2 = new JTextArea();
    static Integer listen_port = null;

    public ServerAlternateStarter() {
        try {
            jbInit();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        try {
            listen_port = new Integer(args[0]);

        }
        catch (Exception e) {
            listen_port = new Integer(9002);
        }

        ServerAlternateStarter webServer = new ServerAlternateStarter();
    }

    private void jbInit() throws Exception {
        jTextArea2.setForeground(new Color(151, 138, 255));
        jTextArea2.setBorder(BorderFactory.createLoweredBevelBorder());
        jTextArea2.setToolTipText("");
        jTextArea2.setEditable(false);
        jTextArea2.setColumns(30);
        jTextArea2.setRows(15);
        this.setTitle("Server Alternate");
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                thisWindowClosed(e);
            }
        });

        jScrollPane1.getViewport().add(jTextArea2);
        jPanel1.add(jScrollPane1);
        this.getContentPane().add(jPanel1, BorderLayout.EAST);

        this.setVisible(true);
        this.setSize(350, 350);
        this.setResizable(true);

        this.validate();

        new ServerAlternate(listen_port.intValue(), this);
    }


    void thisWindowClosed(WindowEvent e) {
        System.exit(1);
    }


    public void sendMegToWindow(String s) {
        jTextArea2.append(s);
    }
}
