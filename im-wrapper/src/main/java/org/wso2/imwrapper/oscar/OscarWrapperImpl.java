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
package org.wso2.imwrapper.oscar;

import net.kano.joustsim.Screenname;
import net.kano.joustsim.oscar.AimConnection;
import net.kano.joustsim.oscar.AimConnectionProperties;
import net.kano.joustsim.oscar.AimSession;
import net.kano.joustsim.oscar.DefaultAppSession;
import net.kano.joustsim.oscar.State;
import net.kano.joustsim.oscar.StateEvent;
import net.kano.joustsim.oscar.StateListener;
import net.kano.joustsim.oscar.oscar.service.icbm.ImConversation;
import net.kano.joustsim.oscar.oscar.service.icbm.SimpleMessage;
import net.kano.joustsim.oscar.oscar.service.icbm.ConversationListener;
import net.kano.joustsim.oscar.oscar.service.icbm.Conversation;
import net.kano.joustsim.oscar.oscar.service.icbm.MessageInfo;
import net.kano.joustsim.oscar.oscar.service.icbm.ConversationEventInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.imwrapper.core.IMException;
import org.wso2.imwrapper.core.IMWrapper;

public class OscarWrapperImpl implements IMWrapper {

    private static Log log = LogFactory.getLog(OscarWrapperImpl.class);

    AimConnection aimConnection;

    boolean loginProcessed = false;
    boolean loggedIn = false;

    int messageCount = 0;
    boolean disconnectCalled = false;

    public void login(String userID, String password) throws IMException {
        DefaultAppSession session = new DefaultAppSession();

        AimSession aimSession = session.openAimSession(
                new Screenname(userID));
        aimConnection = aimSession.openConnection(
                new AimConnectionProperties(
                        new Screenname(userID)
                        , password));

        AimConnStateListener aimConnStateListener = new AimConnStateListener();
        aimConnection.addStateListener(aimConnStateListener);
        aimConnection.connect();
        do {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.error(e);
            }
        } while (!loginProcessed);

        if (!loggedIn) {
            throw new IMException("Connection was not succesfull");
        }
    }

    public void sendMessage(String to, String message) throws IMException {
        if (disconnectCalled) {
            throw new IMException("Unable to send message cause the IM connection is been " +
                    "disconnected");
        }
        if (loginProcessed && loggedIn) {
            ImConversation imConversation = aimConnection.getIcbmService()
                    .getImConversation(new Screenname(to));
            imConversation.open();
            imConversation.addConversationListener(new AimConversationListener());
            if (imConversation.canSendMessage()) {
                messageCount++;
                imConversation.sendMessage(new SimpleMessage(message));
            } else {
                log.error("Cound not send the message to: " + to);
                throw new IMException("Cound not send the message to: " + to);
            }
            imConversation.close();
        } else {
            log.error("Got to Log in before a message can be sent.");
            throw new IMException("Got to Log in before a message can be sent.");
        }
    }

    public void disconnect() throws IMException {
        if (aimConnection != null) {
            do {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    log.error(e);
                }
            } while (messageCount > 0);
            aimConnection.disconnect();
            if (log.isDebugEnabled()) {
                log.debug("Connection disconnected");
            }
        } else {
            log.error("Cannot disconnect cause the connection is not made as yet");
            throw new IMException("Cannot disconnect cause the connection is not made as yet");
        }


    }

    private class AimConnStateListener implements StateListener {
        public void handleStateChange(StateEvent event) {
            State newState = event.getNewState();
            if (newState == State.ONLINE) {
                loggedIn = true;
                loginProcessed = true;
                if (log.isDebugEnabled()) {
                    log.debug("Connection made. Login Succesfull");
                }
            } else if (newState == State.FAILED) {
                if (log.isDebugEnabled()) {
                    log.debug("Connection failed.");
                }
                loggedIn = false;
                loginProcessed = true;
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("Current state is: " + newState);
                }
            }
        }
    }

    private class AimConversationListener implements ConversationListener {

        public void conversationOpened(Conversation conversation) {
        }

        public void conversationClosed(Conversation conversation) {
        }

        public void gotMessage(Conversation conversation, MessageInfo messageInfo) {
        }

        public void sentMessage(Conversation conversation, MessageInfo messageInfo) {
            messageCount--;
        }

        public void canSendMessageChanged(Conversation conversation, boolean b) {

        }

        public void gotOtherEvent(Conversation conversation,
                                  ConversationEventInfo conversationEventInfo) {
        }

        public void sentOtherEvent(Conversation conversation,
                                   ConversationEventInfo conversationEventInfo) {
        }
    }
}
