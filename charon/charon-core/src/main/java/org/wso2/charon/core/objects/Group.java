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
package org.wso2.charon.core.objects;

import org.wso2.charon.core.attributes.Attribute;
import org.wso2.charon.core.attributes.ComplexAttribute;
import org.wso2.charon.core.attributes.DefaultAttributeFactory;
import org.wso2.charon.core.attributes.MultiValuedAttribute;
import org.wso2.charon.core.attributes.SimpleAttribute;
import org.wso2.charon.core.exceptions.CharonException;
import org.wso2.charon.core.schema.SCIMConstants;
import org.wso2.charon.core.schema.SCIMSchemaDefinitions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a Group object which is a collection of attributes as defined by SCIM-Group schema.
 */
public class Group extends AbstractSCIMObject {

    public Group() {
        super();
    }

    /**
     * *************Methods for manipulating Group attributes*******************
     */
    /**
     * Get the display name of the group.
     *
     * @return
     * @throws CharonException
     */
    public String getDisplayName() throws CharonException {
        if (isAttributeExist(SCIMConstants.GroupSchemaConstants.DISPLAY_NAME)) {
            return ((SimpleAttribute) attributeList.get(
                    SCIMConstants.GroupSchemaConstants.DISPLAY_NAME)).getStringValue();
        } else {
            return null;
        }
    }

    /**
     * Set the display name of the group. If already set, update it.
     *
     * @param displayName
     * @throws CharonException
     */
    public void setDisplayName(String displayName) throws CharonException {
        if (isAttributeExist(SCIMConstants.GroupSchemaConstants.DISPLAY_NAME)) {
            ((SimpleAttribute) attributeList.get(SCIMConstants.GroupSchemaConstants.DISPLAY_NAME)).
                    updateValue(displayName, SCIMSchemaDefinitions.DISPLAY_NAME.getType());
        } else {
            SimpleAttribute displayAttribute = new SimpleAttribute(
                    SCIMConstants.GroupSchemaConstants.DISPLAY_NAME, displayName);
            displayAttribute = (SimpleAttribute) DefaultAttributeFactory.createAttribute(
                    SCIMSchemaDefinitions.DISPLAY_NAME, displayAttribute);
            attributeList.put(SCIMConstants.GroupSchemaConstants.DISPLAY_NAME, displayAttribute);
        }
    }

    /**
     * Get the ID values of the members.
     *
     * @return
     * @throws CharonException
     */
    public List<String> getMembersID() throws CharonException {
        if (attributeList != null && (!attributeList.isEmpty())) {
            List<String> membersID = new ArrayList<String>();
            for (Map.Entry<String, Attribute> attributeEntry : attributeList.entrySet()) {
                //we assume that values of multi-valued attribute are set as complex attributes.
                ComplexAttribute attributeValue = (ComplexAttribute) attributeEntry.getValue();
                if (attributeValue != null) {
                    SimpleAttribute valueSubAttribute = (SimpleAttribute) attributeValue.getSubAttribute(
                            SCIMConstants.CommonSchemaConstants.VALUE);
                    if (valueSubAttribute != null) {
                        membersID.add(valueSubAttribute.getStringValue());
                    }
                }

            }
            return membersID;
        } else {
            return null;
        }
    }

    /**
     * Get ID and display values of the member attribute of group.
     *
     * @return
     * @throws CharonException
     */
    public Map<String, String> getMembersMap() throws CharonException {
        if (attributeList != null && (!attributeList.isEmpty())) {
            Map<String, String> membersMap = new HashMap<String, String>();
            for (Map.Entry<String, Attribute> attributeEntry : attributeList.entrySet()) {
                //we assume that values of multi-valued attribute are set as complex attributes.
                ComplexAttribute attributeValue = (ComplexAttribute) attributeEntry.getValue();
                if (attributeValue != null) {
                    SimpleAttribute valueSubAttribute = (SimpleAttribute) attributeValue.getSubAttribute(
                            SCIMConstants.CommonSchemaConstants.VALUE);
                    SimpleAttribute displaySubAttribute = (SimpleAttribute) attributeValue.getSubAttribute(
                            SCIMConstants.CommonSchemaConstants.DISPLAY);
                    if (valueSubAttribute != null && displaySubAttribute != null) {

                        membersMap.put(valueSubAttribute.getStringValue(), displaySubAttribute.getStringValue());
                    }
                }

            }
            return membersMap;
        } else {
            return null;
        }

    }

    /**
     * Set ID values of the member attribute of group.
     *
     * @param members
     */
    public void setMembersID(List<String> members) throws CharonException {
        //check if exist, if so add
        if (isAttributeExist(SCIMConstants.GroupSchemaConstants.MEMBERS)) {
            MultiValuedAttribute membersAttribute = (MultiValuedAttribute) attributeList.get(
                    SCIMConstants.GroupSchemaConstants.MEMBERS);
            List<Attribute> attributeValues = createMembersAttributeValuesWithIDs(members);
            membersAttribute.setValuesAsSubAttributes(attributeValues);

        } else {
            //if attribute doesn't exist, create complex attribute set its sub attributes.
            // Set that complex attribute as value to multi-valued attribute - members. And create the members attribute.
            //add members attribute to Group.
            List<Attribute> attributeValues = createMembersAttributeValuesWithIDs(members);

            MultiValuedAttribute membersAttribute = new MultiValuedAttribute(
                    SCIMConstants.GroupSchemaConstants.MEMBERS, attributeValues);
            membersAttribute = (MultiValuedAttribute) DefaultAttributeFactory.createAttribute(
                    SCIMSchemaDefinitions.MEMBERS, membersAttribute);
            attributeList.put(SCIMConstants.GroupSchemaConstants.MEMBERS, membersAttribute);

        }

    }

    private List<Attribute> createMembersAttributeValuesWithIDs(List<String> members)
            throws CharonException {
        List<Attribute> attributeValues = new ArrayList<Attribute>();
        for (String member : members) {
            //create value - since this complex attribute is not from a schema, we do not call
            //AttributeFactory.
            ComplexAttribute attributeValue = new ComplexAttribute();
            //create 'value' attribute
            SimpleAttribute valueAttribute = new SimpleAttribute(SCIMConstants.CommonSchemaConstants.VALUE, member);
            valueAttribute = (SimpleAttribute) DefaultAttributeFactory.createAttribute(
                    SCIMSchemaDefinitions.VALUE, valueAttribute);
            //add 'value' attribute to complex value
            attributeValue.setSubAttribute(valueAttribute);
            attributeValues.add(attributeValue);
        }
        return attributeValues;
    }

    /**
     * Set both ID and display values of Members attribute of Group.
     *
     * @param members <value,display>
     */
    public void setMembersMap(Map<String, String> members) throws CharonException {
        //check if exist, if so add
        if (isAttributeExist(SCIMConstants.GroupSchemaConstants.MEMBERS)) {
            MultiValuedAttribute membersAttribute = (MultiValuedAttribute) attributeList.get(
                    SCIMConstants.GroupSchemaConstants.MEMBERS);
            List<Attribute> attributeValues = createMembersAttributeValuesWithMap(members);

            membersAttribute.setValuesAsSubAttributes(attributeValues);
        } else {
            //if attribute doesn't exist, create complex attribute set its sub attributes.
            // Set that complex attribute as value to multi-valued attribute - members. And create the members attribute.
            //add members attribute to Group.
            List<Attribute> attributeValues = createMembersAttributeValuesWithMap(members);

            MultiValuedAttribute membersAttribute = new MultiValuedAttribute(
                    SCIMConstants.GroupSchemaConstants.MEMBERS, attributeValues);
            membersAttribute = (MultiValuedAttribute) DefaultAttributeFactory.createAttribute(
                    SCIMSchemaDefinitions.MEMBERS, membersAttribute);
            attributeList.put(SCIMConstants.GroupSchemaConstants.MEMBERS, membersAttribute);

        }
    }

    private List<Attribute> createMembersAttributeValuesWithMap(Map<String, String> members)
            throws CharonException {
        List<Attribute> attributeValues = new ArrayList<Attribute>();
        for (Map.Entry<String, String> entry : members.entrySet()) {
            //create value - since this complex attribute is not from a schema, we do not call
            //AttributeFactory.
            ComplexAttribute attributeValue = new ComplexAttribute();
            //create 'value' attribute
            SimpleAttribute valueAttribute = new SimpleAttribute(
                    SCIMConstants.CommonSchemaConstants.VALUE, entry.getKey());
            valueAttribute = (SimpleAttribute) DefaultAttributeFactory.createAttribute(
                    SCIMSchemaDefinitions.VALUE, valueAttribute);
            //create display attribute
            SimpleAttribute displayAttribute = new SimpleAttribute(
                    SCIMConstants.CommonSchemaConstants.DISPLAY, entry.getValue());
            displayAttribute = (SimpleAttribute) DefaultAttributeFactory.createAttribute(
                    SCIMSchemaDefinitions.DISPLAY, displayAttribute);
            //add 'value' attribute to complex value
            attributeValue.setSubAttribute(valueAttribute);
            //add 'display' attribute to complexvalue
            attributeValue.setSubAttribute(displayAttribute);
            attributeValues.add(attributeValue);
        }
        return attributeValues;
    }

}
