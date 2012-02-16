/*
*  Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/
package org.wso2.charon.deployment.storage;

import org.wso2.charon.core.attributes.Attribute;
import org.wso2.charon.core.exceptions.CharonException;
import org.wso2.charon.core.extensions.UserManager;
import org.wso2.charon.core.objects.*;

import java.util.ArrayList;
import java.util.List;

public class InMemroyUserManager implements UserManager {

    private String tenantDomain = null;
    private int tenantId = 0;
    List<SampleUser> userList;

    public InMemroyUserManager(List<SampleUser> userList, int tenantId, String tenantDomain) {
        this.tenantId = tenantId;
        this.tenantDomain = tenantDomain;
        this.userList = userList;
    }

    /**
     * Obtains the user given the id.
     *
     * @param userId
     * @return
     */
    @Override
    public User getUser(String userId) throws CharonException {
        User scimUser = null;
        //obtain the user from the storage which matches the requested id
        for (SampleUser sampleUser : userList) {
            if (userId.equals(sampleUser.getId())) {
                //create SCIM User corresponding to the matching user in storage
                scimUser = new User();
                
                scimUser.setSchemaList(new ArrayList<String>());
                scimUser.setId(sampleUser.getId());
                scimUser.setUserName(sampleUser.getUserName());
                scimUser.setEmails(sampleUser.getEmails());
            }
        }
        return scimUser;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Update the user in full.
     *
     * @param user
     */
    @Override
    public User updateUser(User user) {
        //To change body of implemented methods use File | Settings | File Templates.
        return null;
    }

    /**
     * Update the user partially only with updated attributes.
     *
     * @param updatedAttributes
     */
    @Override
    public User updateUser(List<Attribute> updatedAttributes) {
        //To change body of implemented methods use File | Settings | File Templates.
        return null;
    }

    /**
     * Delete the user given the user id.
     *
     * @param userId
     */
    @Override
    public void deleteUser(String userId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Create user with the given user object.
     *
     * @param user
     */
    @Override
    public User createUser(User user) {
        //To change body of implemented methods use File | Settings | File Templates.
        return null;
    }

    @Override
    public SCIMObject getResource(String resourceId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
