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
package org.wso2.charon.utils.storage;

import org.wso2.charon.core.attributes.Attribute;
import org.wso2.charon.core.exceptions.CharonException;
import org.wso2.charon.core.extensions.UserManager;
import org.wso2.charon.core.objects.Group;
import org.wso2.charon.core.objects.SCIMObject;
import org.wso2.charon.core.objects.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class InMemroyUserManager implements UserManager {

    private String tenantDomain = null;
    private int tenantId = 0;
    List<SampleUser> userList = new ArrayList<SampleUser>();
    List<SampleGroup> groupList = new ArrayList<SampleGroup>();


    public InMemroyUserManager(int tenantId, String tenantDomain) {
        this.tenantId = tenantId;
        this.tenantDomain = tenantDomain;
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
        if (userList != null && userList.size() != 0) {
            for (SampleUser sampleUser : userList) {
                if (userId.equals(sampleUser.getId())) {
                    //create SCIM User corresponding to the matching user in storage
                    scimUser = new User();

                    scimUser.setSchemaList(new ArrayList<String>());
                    scimUser.setId(sampleUser.getId());
                    scimUser.setUserName(sampleUser.getUserName());
                    scimUser.setEmails(sampleUser.getEmails());
                    scimUser.setExternalId(sampleUser.getFullyQualifiedName());
                }
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
        //TODO:should set last modified date
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
        //TODO:should set last modified date
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
    public User createUser(User user) throws CharonException {
        //check if the user already exist in the system.
        String id = null;
        SampleUser customUser = null;
        if (userList != null && userList.size() != 0) {
            for (SampleUser sampleUser : userList) {
                if (user.getExternalId().equals(sampleUser.getFullyQualifiedName())) {
                    //TODO: log the error
                    String error = "User already in the system";
                    throw new CharonException(error);
                } else {
                    //creates a uuid and assigns to id attribute
                    customUser = createCustomUser(user);
                    userList.add(customUser);
                }
            }
        } else {
            //if this is the first time a user is created, create the user and add it to the list
            customUser = createCustomUser(user);
            userList.add(customUser);

        }
        //TODO:should set last modified date
        //now prepare the SCIM User representation of the created user to be returned.
        //only additionally added value is: id
        //id should not be added here, it should be added in DefaultResourceFactory.
        user.setId(customUser.getId());
        return user;
    }

    /**
     * ****************Group manipulation operations*******************
     */
    @Override
    public Group getGroup(String groupId) throws CharonException {
        Group group = null;
        if (groupList != null && (!groupList.isEmpty())) {
            for (SampleGroup sampleGroup : groupList) {
                if (groupId.equals(sampleGroup.getId())) {
                    group = new Group();
                    group.setId(sampleGroup.getId());
                    group.setExternalId(sampleGroup.getGroupName());
                    group.setDisplayName(sampleGroup.getDisplayName());
                    //create members map
                    Map<String, String> membersMap = new HashMap<String, String>();
                    //fill members map with user member and group member details.
                    List<SampleUser> userMembers = sampleGroup.getUserMembers();
                    if (userMembers != null && !userMembers.isEmpty()) {
                        for (SampleUser userMember : userMembers) {
                            
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public Group createGroup(Group group) throws CharonException {
        return null;
    }

    @Override
    public Group updateGroup(Group group) throws CharonException {
        //TODO:should set last modified date
        return null;
    }

    @Override
    public Group updateGroup(List<Attribute> attributes) throws CharonException {
        //TODO:should set last modified date
        return null;
    }

    @Override
    public Group deleteGroup(String groupId) throws CharonException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public SCIMObject getResource(String resourceId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    /**
     * ****************private methods*************************************
     */

    private SampleUser createCustomUser(User user) throws CharonException {
        SampleUser sampleUser = new SampleUser();
        //it is not the responsibility of the user manager to add id attribute, u should add it in DefaultResourceFactory.
        String id = UUID.randomUUID().toString();
        sampleUser.setId(id);
        sampleUser.setFullyQualifiedName(user.getExternalId());
        sampleUser.setUserName(user.getUserName());
        sampleUser.setEmails(user.getEmails());

        return sampleUser;
    }

    private SampleGroup createSampleGroup(Group group) throws CharonException {
        return new SampleGroup();
    }
}
