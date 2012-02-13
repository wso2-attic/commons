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
package org.wso2.imwrapper.jabber;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.wso2.imwrapper.core.IMException;
import org.wso2.imwrapper.core.IMWrapper;

public class JabberWrapperImpl implements IMWrapper {

    private static Log log = LogFactory.getLog(JabberWrapperImpl.class);
    boolean loginProcessed = false;
    boolean loggedIn = false;

    XMPPConnection connection;

    public void login(String userID, String password) throws IMException {
        int index = userID.indexOf("@");
        if (index < 1 || userID.length() == (index + 1)) {
            log.error(
                    "The format of the userID is incorrect. The userID must be of the form userName@jabberServer");
            throw new IMException(
                    "The format of the userID is incorrect. The userID must be of the form userName@jabberServer");
        }
        String username = userID.substring(0, index);
        String serverName = userID.substring(index + 1);
        connection = new XMPPConnection(serverName);
        try {
            connection.connect();
            connection.login(username, password);
        } catch (XMPPException e) {
            loginProcessed = true;
            loggedIn = false;
            log.error(e);
            throw new IMException(e);
        }
        loginProcessed = true;
        loggedIn = true;
    }

    public void sendMessage(String to, String message) throws IMException {
        if (loginProcessed && loggedIn) {
            Message chatMessage = new Message();
            chatMessage.setTo(to);
            chatMessage.setBody(message);
            chatMessage.setType(Message.Type.normal);
            connection.sendPacket(chatMessage);
        } else {
            log.error("Got to Log in before a message can be sent.");
            throw new IMException("Got to Log in before a message can be sent.");
        }
    }

    public void disconnect() throws IMException {
        if (connection != null) {
            connection.disconnect();
            loginProcessed = false;
            loggedIn = false;
            if (log.isDebugEnabled()) {
                log.debug("Connection to jabber server disconnected");
            }
        } else {
            log.error("Cannot disconnect cause the connection is not made as yet");
            throw new IMException("Cannot disconnect cause the connection is not made as yet");
        }
    }
}
