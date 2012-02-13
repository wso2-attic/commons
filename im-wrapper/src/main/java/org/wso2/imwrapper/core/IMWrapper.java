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
package org.wso2.imwrapper.core;

/**
 * This interface intends to be a wrapper API to the various IM protocols. For the moment it just
 * focusses on the basic use case of sending an Instant Message. It expects that the login message
 * will be called with the correct credentials (Username and password) before sendMessage is called.
 */
public interface IMWrapper {

    /**
     * Used to make a login call to the relevant IM server.
     * The method does not return untill the login process is completed. In case the login fails it
     * throws an IMException with the details for the failure.
     * @param userID - The username of the person wishng to send an IM
     * @param password - The password of the person wishng to send an IM
     * @throws IMException - Thrown in case login fails.
     */
    public void login(String userID, String password) throws IMException;

    /**
     * Used to send an IM to a buddy. A person should be looged in before he can send an IM.
     * @param to - The screenName of the buddy you with to IM
     * @param message - The message you wish to send
     * @throws IMException - Thrown in case the message cannot be sent
     */
    public void sendMessage(String to, String message) throws IMException;

    /**
     * Used to termonate the connection with the IM server
     * @throws IMException - Thrown in case the connection cannot be terminated
     */
    public void disconnect() throws IMException;
}
