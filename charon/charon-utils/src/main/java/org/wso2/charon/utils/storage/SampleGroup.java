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

import java.util.List;

/**
 * Representation of a custom Group entity used in InMemoryUserStoreManager.
 * This can be mapped to LDAP Group object when using LDAPUserStoreManager.
 */
public class SampleGroup {

    private String id;
    private String displayName;
    /*Members of a SCIM Group can either be users or groups.*/
    private List<SampleUser> userMembers;
    private List<SampleGroup> sampleGroup;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<SampleUser> getUserMembers() {
        return userMembers;
    }

    public void setUserMembers(List<SampleUser> userMembers) {
        this.userMembers = userMembers;
    }

    public List<SampleGroup> getSampleGroup() {
        return sampleGroup;
    }

    public void setSampleGroup(List<SampleGroup> sampleGroup) {
        this.sampleGroup = sampleGroup;
    }


}
