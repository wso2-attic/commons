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
import org.wso2.charon.core.objects.User;
import org.wso2.charon.core.schema.SCIMConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemroyUserManager implements UserManager {

    private String tenantDomain = null;
    private int tenantId = 0;
    List<SampleUser> userList = new ArrayList<SampleUser>();
    List<SampleGroup> groupList = new ArrayList<SampleGroup>();

    //in memory user manager stores users
    ConcurrentHashMap<String, User> inMemoryUserList = new ConcurrentHashMap<String, User>();
    ConcurrentHashMap<String, Group> inMemoryGroupList = new ConcurrentHashMap<String, Group>();


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
        /*User scimUser = null;
        //obtain the user from the storage which matches the requested id
        if (userList != null && userList.size() != 0) {
            for (SampleUser sampleUser : userList) {
                if (userId.equals(sampleUser.getId())) {
                    //create SCIM User corresponding to the matching user in storage
                    scimUser = new User();

                    scimUser.setSchemaList(new ArrayList<String>());
                    scimUser.setId(sampleUser.getId());
                    scimUser.setUserName(sampleUser.getUserName());
                    if (sampleUser.getDisplayName() != null) {
                        scimUser.setDisplayName(sampleUser.getDisplayName());
                    }
                    if (sampleUser.getEmails() != null) {
                        scimUser.setEmails(sampleUser.getEmails());
                    }
                    if (sampleUser.getFullyQualifiedName() != null) {
                        scimUser.setExternalId(sampleUser.getFullyQualifiedName());
                    }
                }
            }
        }
        //Validate the constructed SCIMObject against the schema if object not null
        if (scimUser != null) {
            AbstractValidator.validateSCIMObjectForRequiredAttributes(scimUser, SCIMSchemaDefinitions.SCIM_USER_SCHEMA);
        }
        return scimUser;  //To change body of implemented methods use File | Settings | File Templates.*/
        if (userId != null) {
            if (!inMemoryUserList.isEmpty() && inMemoryUserList.containsKey(userId)) {
                return inMemoryUserList.get(userId);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public List<User> listUsers() throws CharonException {
        if (!inMemoryUserList.isEmpty()) {
            List<User> returnedUsers = new ArrayList<User>();
            for (User user : inMemoryUserList.values()) {
                returnedUsers.add(user);
            }
            return returnedUsers;
        } else {
            throw new CharonException("User storage is empty");
        }
    }

    @Override
    public List<User> listUsersByAttribute(Attribute attribute) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<User> listUsersByFilter(String filter) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<User> listUsersBySort(String sortBy, String sortOrder) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<User> listUsersWithPagination(int startIndex, int count) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
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
    public void deleteUser(String userId) throws NotFoundException, CharonException {
        /*if (!userList.isEmpty()) {
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
        }*/
        if (userId != null) {
            if (!inMemoryUserList.isEmpty() && inMemoryUserList.containsKey(userId)) {
                //TODO: remove user from any groups he belongs to.
                validateGroupsOnUserDelete(inMemoryUserList.get(userId));
                inMemoryUserList.remove(userId);
            } else {
                throw new NotFoundException();
            }
        } else {
            throw new NotFoundException();
        }
    }

    /**
     * Create user with the given user object.
     *
     * @param user
     */
    @Override
    public User createUser(User user) throws CharonException {
        //check if the user already exist in the system.
        /*String id = null;
        SampleUser customUser = null;
        if (userList != null && !userList.isEmpty()) {
            for (SampleUser sampleUser : userList) {
                if (user.getExternalId().equals(sampleUser.getFullyQualifiedName())) {
                    //TODO: log the error
                    String error = "User already in the system";
                    throw new CharonException(error);
                }
            }
            customUser = createCustomUser(user);
            userList.add(customUser);
        } else {
            //if this is the first time a user is created, create the user and add it to the list
            customUser = createCustomUser(user);
            userList.add(customUser);

        }
        //now prepare the SCIM User representation of the created user to be returned.
        //only additionally added value is: id
        //id should not be added here, it should be added in DefaultResourceFactory.
        //user.setId(customUser.getId());
        return user;*/

        if (!inMemoryUserList.isEmpty()) {
            if (user.getExternalId() != null) {
                for (Map.Entry<String, User> userEntry : inMemoryUserList.entrySet()) {
                    if (user.getExternalId().equals(userEntry.getValue().getExternalId())) {
                        String error = "User already exist in the system.";
                        //TODO:log error
                        throw new CharonException(error);
                    }
                }
            }
            inMemoryUserList.put(user.getId(), user);
        } else {
            inMemoryUserList.put(user.getId(), user);
        }
        return user;
    }

    /**
     * ****************Group manipulation operations*******************
     */
    @Override
    public Group getGroup(String groupId) throws CharonException {
        /*Group group = null;
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
                                    userMembersMap.put(userMember, getMemberDisplayForUser(sampleUser));
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
        return group;*/
        if (groupId != null) {
            if (!inMemoryGroupList.isEmpty() && inMemoryGroupList.containsKey(groupId)) {
                return inMemoryGroupList.get(groupId);
            } else {
                return null;
            }
        } else {
            return null;
        }

    }

    @Override
    public Group createGroup(Group group) throws CharonException {
        /*SampleGroup customGroup = null;
        if (this.groupList != null && !this.groupList.isEmpty()) {
            for (SampleGroup sampleGroup : groupList) {
                if (group.getExternalId().equals(sampleGroup.getExternalId())) {
                    //TODO: log the error
                    String error = "Group already in the system";
                    throw new CharonException(error);
                }
            }
            customGroup = createCustomGroup(group);
            groupList.add(customGroup);
        } else {
            customGroup = createCustomGroup(group);
            groupList.add(customGroup);
        }
        return group;*/
        if (!inMemoryGroupList.isEmpty()) {
            if (group.getExternalId() != null) {
                for (Group group1 : inMemoryGroupList.values()) {
                    if (group.getExternalId().equals(group1.getExternalId())) {
                        String error = "Group already exist in the system.";
                        //TODO:log error
                        throw new CharonException(error);
                    }
                }
            }
            validateMembersOnGroupCreate(group);
            inMemoryGroupList.put(group.getId(), group);
        } else {
            validateMembersOnGroupCreate(group);
            inMemoryGroupList.put(group.getId(), group);
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
    public void deleteGroup(String groupId) throws NotFoundException, CharonException {
        /*//TODO:when removing group, remove group membership of its members - i.e: consider updating group attribute of Users.
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
        }*/
        if (groupId != null) {
            if (!inMemoryGroupList.isEmpty() && inMemoryGroupList.containsKey(groupId)) {
                validateMembersOnGroupDelete(inMemoryGroupList.get(groupId));
                inMemoryGroupList.remove(groupId);
            } else {
                throw new NotFoundException();
            }
        } else {
            throw new NotFoundException();
        }
    }

    /**
     * ****************private methods*************************************
     */

    private void validateMembersOnGroupCreate(Group group) throws CharonException {
        List<String> userIDs = group.getUserMembers();
        List<String> groupIDs = group.getGroupMembers();
        if (groupIDs != null && !groupIDs.isEmpty()) {
            for (String groupID : groupIDs) {
                if (!inMemoryGroupList.containsKey(groupID)) {
                    //throw exception
                    String error = "Group member: " + groupID + " doesn't exist in the system.";
                    throw new CharonException(error);
                }
            }
        }
        for (String userID : userIDs) {
            if (inMemoryUserList.containsKey(userID)) {
                User user = inMemoryUserList.get(userID);
                //update direct membership
                if (!user.isUserMemberOfGroup(SCIMConstants.UserSchemaConstants.DIRECT_MEMBERSHIP, group.getId())) {
                    user.setGroup(SCIMConstants.UserSchemaConstants.DIRECT_MEMBERSHIP, group.getId());
                }
            } else {
                //throw error.
                String error = "User member: " + userID + " doesn't exist in the system.";
                throw new CharonException(error);
            }
        }
    }

    private void validateMembersOnGroupDelete(Group group) throws CharonException {
        //get user members and remove direct membership from them
        List<String> userMembers = group.getUserMembers();
        if (userMembers != null && !userMembers.isEmpty()) {
            for (String userMember : userMembers) {
                if (inMemoryUserList.containsKey(userMember)) {
                    User user = inMemoryUserList.get(userMember);
                    if (user.isUserMemberOfGroup(SCIMConstants.UserSchemaConstants.DIRECT_MEMBERSHIP,
                                                 group.getId())) {
                        user.removeFromGroup(group.getId());
                    }
                }
            }
        }

    }

    private void validateGroupsOnUserDelete(User user) throws CharonException {
        //get groups in which user is a direct member
        List<String> groupIds = user.getDirectGroups();
        for (String groupId : groupIds) {
            if (inMemoryGroupList.containsKey(groupId)) {
                Group group = inMemoryGroupList.get(groupId);
                group.removeUserMember(user.getId());
            }
        }
    }


    /*private SampleUser createCustomUser(User user) throws CharonException {
        SampleUser sampleUser = new SampleUser();
        //it is not the responsibility of the user manager to add id attribute, u should add it in DefaultResourceFactory.
        //TODO:before setting an attribute value in custom user, check whether the value is null
        sampleUser.setId(user.getId());
        if (user.getExternalId() != null) {
            sampleUser.setFullyQualifiedName(user.getExternalId());
        }
        if (user.getCreatedDate() != null) {
            sampleUser.setCreatedDate(user.getCreatedDate());
        }
        if (user.getLastModified() != null) {
            sampleUser.setLastModified(user.getLastModified());
        }
        sampleUser.setUserName(user.getUserName());
        sampleUser.setEmails(user.getEmails());
        if (user.getDisplayName() != null) {
            sampleUser.setDisplayName(user.getDisplayName());
        }
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
            //to be set in sample group
            List<String> userMemberIDs = new ArrayList<String>();
            //<ID,displayName> - to be set in SCIM Group to be returned.
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
                            userMemberMap.put(memberID, getMemberDisplayForUser(sampleUser));
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

    *//**
     * Display value of multivalued group members attribute is set by SP. This function returns which
     * to set out of available attributes.
     *
     * @param sampleUser
     * @return
     *//*
    private String getMemberDisplayForUser(SampleUser sampleUser) {
        if (sampleUser.getDisplayName() != null) {
            return sampleUser.getDisplayName();
        } else {
            return sampleUser.getFullyQualifiedName();
        }
    }*/
}
