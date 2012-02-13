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
package org.wso2.imwrapper.yahoo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.imwrapper.core.IMException;
import org.wso2.imwrapper.core.IMWrapper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class YahooWrapperImpl implements IMWrapper {

    Class sessionClass;
    Object session = null;
    private boolean loginProcessed = false;
    private boolean loggedIn = false;
    private final String EXCEPTION =
            "Cannot find yahoo library in the class path. Please make sure its in the classpath.";

    private static Log log = LogFactory.getLog(YahooWrapperImpl.class);

    /**
     * Used to make a login call to the relevant IM server.
     * The method does not return untill the login process is completed. In case the login fails it
     * throws an IMException with the details for the failure.
     *
     * @param userID   - The username of the person wishng to send an IM
     * @param password - The password of the person wishng to send an IM
     * @throws org.wso2.imwrapper.core.IMException
     *          - Thrown in case login fails.
     */
    public void login(String userID, String password) throws IMException {
        try {
            sessionClass = Class.forName("ymsg.network.Session");
            session = sessionClass.newInstance();
            Method method = sessionClass.getMethod("login", new Class[] {String.class, String.class});
            method.invoke(session, new Object[] {userID, password});
            loggedIn = true;
            loginProcessed = true;
        } catch (ClassNotFoundException e) {
            throw new IMException(EXCEPTION);
        } catch (IllegalAccessException e) {
            throw new IMException(EXCEPTION);
        } catch (InstantiationException e) {
            throw new IMException(EXCEPTION);
        } catch (NoSuchMethodException e) {
            throw new IMException(EXCEPTION);
        } catch (Exception e) {
            Throwable throwable = e.getCause();
            if (throwable!= null && "ymsg.network.LoginRefusedException".equals(throwable.getClass().getName())) {
                loggedIn = false;
                loginProcessed = true;
                throw new IMException(throwable.getMessage());
            }
        }
    }

    /**
     * Used to send an IM to a buddy. A person should be looged in before he can send an IM.
     *
     * @param to      - The screenName of the buddy you with to IM
     * @param message - The message you wish to send
     * @throws org.wso2.imwrapper.core.IMException
     *          - Thrown in case the message cannot be sent
     */
    public void sendMessage(String to, String message) throws IMException {
        if (loginProcessed && loggedIn) {
            try {
                Method method = sessionClass.getMethod("sendMessage", new Class[]{String.class, String.class});
                method.invoke(session, new Object[] {to, message});
            } catch (NoSuchMethodException e) {
                throw new IMException(EXCEPTION);
            } catch (InvocationTargetException e) {
                throw new IMException(EXCEPTION);
            } catch (IllegalAccessException e) {
                throw new IMException(EXCEPTION);
            }
        } else {
            log.error("Got to Log in before a message can be sent.");
            throw new IMException("Got to Log in before a message can be sent.");
        }
    }

    /**
     * Used to termonate the connection with the IM server
     *
     * @throws org.wso2.imwrapper.core.IMException
     *          - Thrown in case the connection cannot be terminated
     */
    public void disconnect() throws IMException {
        if (session != null) {
            if (log.isDebugEnabled()) {
                log.debug("Connection to Yahoo disconnected");
            }
            try {
                Method method = sessionClass.getMethod("logout", new Class[] {});
                method.invoke(session, new Object[] {});
                loginProcessed = false;
                loggedIn = false;
            } catch (NoSuchMethodException e) {
                throw new IMException(EXCEPTION);
            } catch (InvocationTargetException e) {
                throw new IMException(EXCEPTION);
            } catch (IllegalAccessException e) {
                throw new IMException(EXCEPTION);
            }
        } else {
            log.error("Cannot disconnect cause the connection is not made as yet");
            throw new IMException("Cannot disconnect cause the connection is not made as yet");
        }
    }
}
