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
package org.wso2.imwrapper.msn;

import net.sf.jml.Email;
import net.sf.jml.MsnMessenger;
import net.sf.jml.MsnContact;
import net.sf.jml.MsnSwitchboard;
import net.sf.jml.event.MsnAdapter;
import net.sf.jml.event.MsnContactListAdapter;
import net.sf.jml.event.MsnSwitchboardAdapter;
import net.sf.jml.impl.MsnMessengerFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.imwrapper.core.IMException;
import org.wso2.imwrapper.core.IMWrapper;

public class MSNWrapperImpl implements IMWrapper {

    MsnMessenger messenger = null;
    boolean loginProcessed = false;
    boolean loggedIn = false;
    String errorMessage;
    int messageCount = 0;
    boolean disconnectCalled = false;

    private static Log log = LogFactory.getLog(MSNWrapperImpl.class);

    public void login(String userID, String password) throws IMException {

        messenger = MsnMessengerFactory.createMsnMessenger(userID, password);
        messenger.addMessengerListener(new MsnAdapter() {

            public void loginCompleted(MsnMessenger messenger) {
                loginProcessed = false;
                loggedIn = true;
                if (log.isDebugEnabled()) {
                    log.debug("Signed into MSN, Waiting till Contact List Init complete");
                }
            }

            public void exceptionCaught(MsnMessenger msnMessenger, Throwable throwable) {
                loginProcessed = true;
                errorMessage = throwable.getMessage();
                String throwableString = throwable.toString();
                if (throwableString != null && throwableString.startsWith("ErrorCode 217 , User not on-line")) {
                    messageCount--;
                }
                log.error(throwable);
            }
        });

        messenger.addContactListListener(new MsnContactListAdapter() {

            public void contactListInitCompleted(MsnMessenger messenger) {
                loginProcessed = true;
                if (log.isDebugEnabled()) {
                    log.debug("Contact List Init completed");
                }
            }

        }); 

        messenger.login();
        do {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.error(e);
            }
        } while (!loginProcessed);

        if (!loggedIn) {
            throw new IMException(errorMessage);
        }
    }

    public void sendMessage(String to, String message) throws IMException {
        if (disconnectCalled) {
            throw new IMException("Unable to send message cause the IM connection is been " +
                    "disconnected");
        }
        if (loginProcessed && loggedIn) {
            final Email email = Email.parseStr(to);
            final String text = message;
            messageCount ++;
            MsnSwitchboard[] switchboards = messenger.getActiveSwitchboards();
            for (int i = 0; i < switchboards.length; i++) {
                MsnSwitchboard switchboard1 = switchboards[i];
                if (switchboard1.containContact(email)
                        && switchboard1.getAllContacts().length == 1) {
                    switchboard1.sendText(text);
                    messageCount --;
                    return;
                }
            }

            final Object attachment = new Object();
            messenger.addSwitchboardListener(new MsnSwitchboardAdapter() {

                public void switchboardStarted(MsnSwitchboard switchboard) {
                    if (switchboard.getAttachment() == attachment) {
                        switchboard.inviteContact(email);
                    }
                }

                public void contactJoinSwitchboard(MsnSwitchboard switchboard,
                                                   MsnContact contact) {
                    if (switchboard.getAttachment() == attachment
                            && email.equals(contact.getEmail())) {
                        switchboard.setAttachment(null);
                        messenger.removeSwitchboardListener(this);
                        switchboard.sendText(text);
                        messageCount --;
                    }
                }

            });
            messenger.newSwitchboard(attachment);
        } else {
            log.error("Got to Log in before a message can be sent.");
            throw new IMException("Got to Log in before a message can be sent.");
        }
    }

    public void disconnect() throws IMException {
        if (messenger != null) {

            // Used to keep track of how long we wait before disconnection. In MSN if a message is
            // sent to a non existing user, the message wont be dillivered at all. We will not get a
            // response too saying that the message was not dillivered. Hence we wait for 5 seconds
            // before disconnection. https://wso2.org/jira/browse/MASHUP-632

            int count = 0;
            while (messageCount > 0 && count < 5) {
                try {
                    count ++;
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    log.error(e);
                }
            }
            if (log.isDebugEnabled()) {
                log.debug("Connection to MSN disconnected");
            }
            messenger.logout();
            loginProcessed = false;
            loggedIn = false;

            // If there were unsent messgaes, probably the messages were sent to non-existing users
            // we throw an exception after disconnection
            if (messageCount > 0) {
                throw new IMException(
                        "There are some messages that were not sent to MSN recipients. " +
                                "The reason could be that these recipients were not online. " +
                                messageCount + " messages were not dillivered");
            }
        } else {
            log.error("Cannot disconnect cause the connection is not made as yet");
            throw new IMException("Cannot disconnect cause the connection is not made as yet");
        }
    }
}
