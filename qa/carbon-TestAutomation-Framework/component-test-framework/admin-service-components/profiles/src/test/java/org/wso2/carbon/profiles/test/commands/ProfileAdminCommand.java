/*                                                                             
 * Copyright 2004,2005 The Apache Software Foundation.                         
 *                                                                             
 * Licensed under the Apache License, Version 2.0 (the "License");             
 * you may not use this file except in compliance with the License.            
 * You may obtain a copy of the License at                                     
 *                                                                             
 *      http://www.apache.org/licenses/LICENSE-2.0                             
 *                                                                             
 * Unless required by applicable law or agreed to in writing, software         
 * distributed under the License is distributed on an "AS IS" BASIS,           
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.    
 * See the License for the specific language governing permissions and         
 * limitations under the License.                                              
 */
package org.wso2.carbon.profiles.test.commands;

import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.registry.profiles.ui.ProfilesAdminServiceCallbackHandler;
import org.wso2.carbon.registry.profiles.ui.ProfilesAdminServiceStub;
import org.wso2.carbon.registry.profiles.ui.RegistryExceptionException;
import org.wso2.carbon.registry.profiles.ui.UserStoreExceptionException;

import javax.security.auth.callback.CallbackHandler;
import java.rmi.RemoteException;

/**
 *
 */
public class ProfileAdminCommand extends TestCase {
    private static final Log log = LogFactory.getLog(ProfileAdminCommand.class);
    ProfilesAdminServiceStub profilesAdminServiceStub;

    public ProfileAdminCommand(ProfilesAdminServiceStub profilesAdminServiceStub) {
        this.profilesAdminServiceStub = profilesAdminServiceStub;
        log.debug("ProfilesAdminServiceStub added");

    }


    public void getUserProfileSuccessCase(String path) throws RemoteException, RegistryExceptionException, UserStoreExceptionException {
        log.debug("GetUserProfile executeSuccessCase");
        try {
            profilesAdminServiceStub.getUserProfile(path);

        } catch (UserStoreExceptionException e) {
            fail("Exception thrown" + e);
            log.error(e);
        } catch (RegistryExceptionException e) {
            fail("Exception thrown" + e);
            log.error(e);
        } catch (RemoteException e) {
            fail("Exception thrown" + e);
            log.error(e);
        } catch (Exception e) {
            fail("Exception thrown" + e);
            log.error(e);
        }
        log.info("GetUserProfile Test Success");
    }

    public void putUserProfileSuccessCase(String path) throws RemoteException, RegistryExceptionException, UserStoreExceptionException {
        log.debug("PutUserProfile executeSuccessCase");
        try {
            profilesAdminServiceStub.putUserProfile(path);

        } catch (UserStoreExceptionException e) {
            fail("Exception thrown" + e);
            log.error(e);
        } catch (RegistryExceptionException e) {
            fail("Exception thrown" + e);
            log.error(e);
        } catch (RemoteException e) {
            fail("Exception thrown" + e);
            log.error(e);
        } catch (Exception e) {
            fail("Exception thrown" + e);
            log.error(e);
        }
        log.info("PutUserProfile Test Success");
    }


    public void startGetUserProfileSuccessCase(String path, CallbackHandler callback) throws RemoteException {
        log.debug("startGetUserProfile executeSuccessCase");
        try {
            profilesAdminServiceStub.startgetUserProfile(path, (ProfilesAdminServiceCallbackHandler) callback);

        } catch (RemoteException e) {
            fail("Exception thrown" + e);
            log.error(e);
        } catch (Exception e) {
            fail("Exception thrown" + e);
            log.error(e);
        }
        log.info("startGetUserProfile Test Success");
    }

    public void startPutUserProfileSuccessCase(String path, CallbackHandler callback) throws RemoteException {
        log.debug("startPutUserProfile executeSuccessCase");
        try {
            profilesAdminServiceStub.startputUserProfile(path, (ProfilesAdminServiceCallbackHandler) callback);

        } catch (RemoteException e) {
            fail("Exception thrown" + e);
            log.error(e);
        } catch (Exception e) {
            fail("Exception thrown" + e);
            log.error(e);
        }
        log.info("startPutUserProfile Test Success");
    }

}
