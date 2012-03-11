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
import org.wso2.charon.core.exceptions.NotFoundException;
import org.wso2.charon.core.extensions.UserManager;
import org.wso2.charon.core.objects.Group;
import org.wso2.charon.core.objects.SCIMObject;
import org.wso2.charon.core.objects.User;
import org.wso2.charon.core.schema.DefaultSchemaValidator;
import org.wso2.charon.core.schema.SCIMConstants;
import org.wso2.charon.core.schema.SCIMSchemaDefinitions;

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
        //Validate the constructed SCIMObject against the schema
        DefaultSchemaValidator.validateSCIMObject(scimUser, SCIMSchemaDefinitions.SCIM_USER_SCHEMA);
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
    public void deleteUser(String userId) throws NotFoundException {
        if (!userList.isEmpty()) {
            for (SampleUser sampleUser : userList) {
                if (userId.equals(sampleUser.getId())) {
                    userList.remove(sampleUser);
                    return;
                }
            }
            //if no user id maps with requested id,
            throw new NotFoundException();

        } else {
            //if user list is empty
            throw new NotFoundException();
        }
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
        if (userList != null && !userList.isEmpty()) {
            for (SampleUser sampleUser : userList) {
                if (user.getExternalId().equals(sampleUser.getFullyQualifiedName())) {
                    //TODO: log the error
                    String error = "User already in the system";
                    throw new CharonException(error);
                }
                customUser = createCustomUser(user);
                userList.add(customUser);
            }
        } else {
            //if this is the first time a user is created, create the user and add it to the list
            customUser = createCustomUser(user);
            userList.add(customUser);

        }
        //now prepare the SCIM User representation of the created user to be returned.
        //only additionally added value is: id
        //id should not be added here, it should be added in DefaultResourceFactory.
        //user.setId(customUser.getId());
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
                    if (sampleGroup.getExternalId() != null) {
                        group.setExternalId(sampleGroup.getGroupName());
                    }
                    if (sampleGroup.getDisplayName() != null) {
                        group.setDisplayName(sampleGroup.getDisplayName());
                    }
                    group.setCreatedDate(sampleGroup.getCreatedDate());
                    group.setLastModified(sampleGroup.getLastModified());

                    //fill members map with user member details.
                    List<String> userMembers = sampleGroup.getUserMembers();
                    if (userMembers != null && !userMembers.isEmpty()) {
                        //create user members map - <ID,displayName>
                        Map<String, String> userMembersMap = new HashMap<String, String>();
                        for (String userMember : userMembers) {
                            //fetch displayName attribute from users and set it in user members map.
                            for (SampleUser sampleUser : userList) {
                                if (userMember.equals(sampleUser.getId())) {
                                    userMembersMap.put(userMember, sampleUser.getUserName());
                                }
                            }
                        }
                        //set above user members in group to be returned.
                        for (Map.Entry<String, String> entry : userMembersMap.entrySet()) {
                            group.setMember(entry.getKey(), entry.getValue(), SCIMConstants.USER);
                        }
                    }
                    //fill members map with group member details.
                    List<String> groupMembers = sampleGroup.getSubGroupMembers();
                    if (groupMembers != null && !groupMembers.isEmpty()) {
                        //create group member map - <ID,displayName>
                        Map<String, String> groupMembersMap = new HashMap<String, String>();
                        for (String groupMember : groupMembers) {
                            for (SampleGroup customGroup : groupList) {
                                if (groupMember.equals(customGroup.getId())) {
                                    groupMembersMap.put(groupMember, customGroup.getDisplayName());
                                }
                            }
                        }
                        for (Map.Entry<String, String> entry : groupMembersMap.entrySet()) {
                            group.setMember(entry.getKey(), entry.getValue(), SCIMConstants.GROUP);
                        }
                    }
                }
            }
        }
        return group;
    }

    @Override
    public Group createGroup(Group group) throws CharonException {
        SampleGroup customGroup = null;
        if (this.groupList != null && !this.groupList.isEmpty()) {
            for (SampleGroup sampleGroup : groupList) {
                if (group.getExternalId().equals(sampleGroup.getExternalId())) {
                    //TODO: log the error
                    String error = "Group already in the system";
                    throw new CharonException(error);
                }
                customGroup = createCustomGroup(group);
                groupList.add(customGroup);
            }
        } else {
            customGroup = createCustomGroup(group);
            groupList.add(customGroup);
        }
        return group;
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
    public void deleteGroup(String groupId) throws NotFoundException {
        if (!groupList.isEmpty()) {
            for (SampleGroup sampleGroup : groupList) {
                if (groupId.equals(sampleGroup.getId())) {
                    groupList.remove(sampleGroup);
                    return;
                }
            }
            //if requested id is not among group list
            throw new NotFoundException();

        } else {
            //if group list is empty
            throw new NotFoundException();
        }
    }

    /*@Override
    public SCIMObject getResource(String resourceId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }*/


    /**
     * ****************private methods*************************************
     */

    private SampleUser createCustomUser(User user) throws CharonException {
        SampleUser sampleUser = new SampleUser();
        //it is not the responsibility of the user manager to add id attribute, u should add it in DefaultResourceFactory.
        //TODO:before setting an attribute value in custom user, check whether the value is null
        String id = UUID.randomUUID().toString();
        sampleUser.setId(id);
        sampleUser.setFullyQualifiedName(user.getExternalId());
        sampleUser.setUserName(user.getUserName());
        sampleUser.setEmails(user.getEmails());

        return sampleUser;
    }

    private SampleGroup createCustomGroup(Group group) throws CharonException {
        SampleGroup sampleGroup = new SampleGroup();
        sampleGroup.setId(group.getId());
        if (group.getExternalId() != null) {
            sampleGroup.setExternalId(group.getExternalId());
        }
        if (group.getCreatedDate() != null) {
            sampleGroup.setCreatedDate(group.getCreatedDate());
        }
        if (group.getLastModified() != null) {
            sampleGroup.setLastModified(group.getLastModified());
        }
        //we assume displayName is not null
        sampleGroup.setDisplayName(group.getDisplayName());

        if (group.getMembers() != null && !group.getMembers().isEmpty()) {
            //get all member ids
            List<String> memberIDs = group.getMembers();
            List<String> userMemberIDs = new ArrayList<String>();
            //<ID,displayName>
            Map<String, String> userMemberMap = new HashMap<String, String>();
            //see whether those ids in existing users
            if (!userList.isEmpty()) {
                for (SampleUser sampleUser : userList) {
                    for (String memberID : memberIDs) {
                        if (memberID.equals(sampleUser.getId())) {
                            //member is a user.
                            //prepare to set the ids in sampleGroup->userList
                            userMemberIDs.add(memberID);
                            //prepare to set id n displayName in group->members
                            userMemberMap.put(memberID, sampleUser.getUserName());
                            //TODO:update group attribute of sample user to update the groups that he belongs to.
                        }
                    }
                }
                //set user members in sampleGroup->userList, if non empty
                if (!userMemberIDs.isEmpty()) {
                    sampleGroup.setUserMembers(userMemberIDs);
                }
            }
            //see whether those ids in existing groups
            List<String> groupMemberIDs = new ArrayList<String>();
            Map<String, String> groupMemberMap = new HashMap<String, String>();
            if (!groupList.isEmpty()) {
                for (SampleGroup customGroup : groupList) {
                    for (String memberID : memberIDs) {
                        if (memberID.equals(customGroup.getId())) {
                            //member is a group
                            //prepare to set the id in sampleGroup->groupList
                            groupMemberIDs.add(memberID);
                            //prepare to set id n displayName in group->members
                            groupMemberMap.put(memberID, customGroup.getDisplayName());
                        }
                    }
                }
                //set group members in sampleGroup->groupList
                sampleGroup.setSubGroupMembers(groupMemberIDs);
            }
            //remove existing members attribute
            group.deleteAttribute(SCIMConstants.GroupSchemaConstants.MEMBERS);
            //set above first in group,as userMembers, by setting display name and type
            for (Map.Entry<String, String> userEntry : userMemberMap.entrySet()) {
                group.setMember(userEntry.getKey(), userEntry.getValue(), SCIMConstants.USER);
            }
            //set above second in group,as groupMembers, by setting display name and type
            for (Map.Entry<String, String> groupEntry : groupMemberMap.entrySet()) {
                group.setMember(groupEntry.getKey(), groupEntry.getValue(), SCIMConstants.GROUP);
            }
        }
        return sampleGroup;
    }
}
