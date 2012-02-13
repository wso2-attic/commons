package org.wso2.carbon.common.test.utils.server;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.net.*;

public class ServerAlternate
        extends Thread {
    private static final Log log = LogFactory.getLog(ServerAlternate.class);


    public ServerAlternate(int listenPort, ServerAlternateStarter to_send_message_to) {
        message_to = to_send_message_to;
        port = listenPort;

        this.start();
    }

    private void sendMessage(String s2) {
        message_to.sendMegToWindow(s2);
    }

    private ServerAlternateStarter message_to;
    private int port;

    public void run() {
        ServerSocket serversocket = null;

        try {

            sendMessage("Binding to localhost on port" + Integer.toString(port) + "... \n");
            serversocket = new ServerSocket(port);
        }
        catch (Exception e) {
            sendMessage(e.getMessage());
            return;
        }
        sendMessage("OK!\n");
        while (true) {
            sendMessage("\nReady, Waiting for requests... \n");
            try {
                Socket connectionSocket = serversocket.accept();
                InetAddress client = connectionSocket.getInetAddress();

                sendMessage(client.getHostName() + " connected to server \n");

                BufferedReader input =
                        new BufferedReader(new InputStreamReader(connectionSocket.
                                getInputStream()));

                DataOutputStream output =
                        new DataOutputStream(connectionSocket.getOutputStream());

                httpHandler(input, output);
            }
            catch (Exception e) {
                sendMessage("\nError:" + e.getMessage());
            }

        }
    }

    private void httpHandler(BufferedReader input, DataOutputStream output) {
        int method = 0;
        String http = new String();
        String path = new String();
        String file = new String();
        String user_agent = new String();
        try {

            String tmp = input.readLine();
            String tmp2 = new String(tmp);
            tmp.toUpperCase();
            if (tmp.startsWith("GET")) {
                method = 0;
            }
            if (tmp.startsWith("HEAD")) {
                method = 2;
            }

            if (method == 0) {
                try {
                    output.writeBytes(construct_http_header(200, 0));
                    output.flush();
                    Thread.sleep(100);
                    output.close();
                    return;
                }
                catch (Exception e3) {
                    sendMessage("error:" + e3.getMessage());
                }
            }

            int start = 0;
            int end = 0;
            for (int a = 0; a < tmp2.length(); a++) {
                if (tmp2.charAt(a) == ' ' && start != 0) {
                    end = a;
                    break;
                }
                if (tmp2.charAt(a) == ' ' && start == 0) {
                    start = a;
                }
            }
            path = tmp2.substring(start + 2, end);
        }
        catch (Exception e) {
            sendMessage("errorr" + e.getMessage());
        }

        sendMessage("\n Client requested:" + new File(path).getAbsolutePath() + " \n");
        FileInputStream fileInSt = null;

        try {
            fileInSt = new FileInputStream(path);
        }
        catch (Exception e) {
            try {
                output.writeBytes(construct_http_header(404, 0));
                output.close();
            }
            catch (Exception e2) {
            }
            sendMessage("error" + e.getMessage());
        }

        try {
            int type_is = 0;

            if (path.endsWith(".zip") || path.endsWith(".exe")
                || path.endsWith(".tar")) {
                type_is = 3;
            }
            if (path.endsWith(".jpg") || path.endsWith(".jpeg")) {
                type_is = 1;
            }
            if (path.endsWith(".gif")) {
                type_is = 2;

            }
            output.writeBytes(construct_http_header(200, 5));

            if (method == 1) {
                while (true) {

                    int b = fileInSt.read();
                    if (b == -1) {
                        break;
                    }
                    output.write(b);
                }

            }
            output.write(new byte[]{'a', 'v', 'c'});
            output.close();
            fileInSt.close();
        }

        catch (Exception e) {
        }

    }

    private String construct_http_header(int return_code, int file_type) {
        String protocol = "HTTP/1.1 ";

        switch (return_code) {
            case 200:
                protocol = protocol + "200 OK";
                break;
            case 400:
                protocol = protocol + "400 Bad Request";
                break;
            case 403:
                protocol = protocol + "403 Forbidden";
                break;
            case 404:
                protocol = protocol + "404 Not Found";
                break;
            case 500:
                protocol = protocol + "500 Internal Server Error";
                break;
            case 501:
                protocol = protocol + "501 Not Implemented";
                break;
        }

        protocol = protocol + "\r\n";
        protocol = protocol + "Connection: close\r\n";
        protocol = protocol + "Server: SimpleHTTPtutorial v0\r\n";

        switch (file_type) {

            case 0:
                protocol = protocol + "Content-Type: image/jpeg\r\n";
                break;
            case 1:
                protocol = protocol + "Content-Type: image/gif\r\n";
            case 2:
                protocol = protocol + "Content-Type: application/x-zip-compressed\r\n";
            case 3:
                protocol = protocol + "Content-Type: application/x-www-form\r\n";
            case 4:
                protocol = protocol
                           + "Content-Type: application/soap+xml\r\n";
            case 5:
                protocol = protocol
                           + "Content-Type: multiform/part\r\n";
            default:
                protocol = protocol + "Content-Type: text/html\r\n";
                break;
        }

        protocol = protocol + "Content-Type: application/x-www-form-urlencoded\r\n";
        protocol = protocol + "\r\n";
        protocol = protocol + "param1=value1&param2=value2";
        return protocol;
    }

}
