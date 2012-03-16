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
import org.wso2.charon.core.exceptions.NotFoundException;
import org.wso2.charon.core.schema.AttributeSchema;
import org.wso2.charon.core.schema.SCIMConstants;
import org.wso2.charon.core.schema.SCIMSchemaDefinitions;
import org.wso2.charon.core.schema.SCIMSchemaDefinitions.DataType;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the User object which is a collection of attributes defined by SCIM User-schema.
 */
public class User extends AbstractSCIMObject {

    public User() {
        super();
    }

    /***********************UserName manipulation methods*************************************/
    /**
     * Set UserName attribute of the User.
     *
     * @param userName
     * @throws CharonException
     */
    public void setUserName(String userName) throws CharonException {
        setSimpleAttribute(SCIMConstants.UserSchemaConstants.USER_NAME, SCIMSchemaDefinitions.USER_NAME,
                           userName, DataType.STRING);
        /*if (isAttributeExist(SCIMConstants.UserSchemaConstants.USER_NAME)) {
            ((SimpleAttribute) attributeList.get(
                    SCIMConstants.UserSchemaConstants.USER_NAME)).updateValue(
                    userName, DataType.STRING);
        } else {
            //TODO:since the constructor is too long, pass an attribute schema.
            SimpleAttribute userNameAttribute = new SimpleAttribute(
                    SCIMConstants.UserSchemaConstants.USER_NAME, userName);
            *//*SimpleAttribute userNameAttribute = new SimpleAttribute(
                    SCIMConstants.UserSchemaConstants.USER_NAME,
                    SCIMConstants.CORE_SCHEMA_URI, userName, DataType.STRING,
                    false, false);*//*
            userNameAttribute = (SimpleAttribute) DefaultAttributeFactory.createAttribute(
                    SCIMSchemaDefinitions.USER_NAME, userNameAttribute);
            attributeList.put(SCIMConstants.UserSchemaConstants.USER_NAME, userNameAttribute);
        }*/
    }

    /**
     * Get UserName attribute of the user.
     *
     * @return
     * @throws NotFoundException
     * @throws CharonException
     */
    public String getUserName() throws CharonException {
        return getSimpleAttributeStringVal(SCIMConstants.UserSchemaConstants.DISPLAY_NAME);
        /*if (isAttributeExist(SCIMConstants.UserSchemaConstants.USER_NAME)) {
            return ((SimpleAttribute) attributeList.get(
                    SCIMConstants.UserSchemaConstants.USER_NAME)).getStringValue();

        } else {
            return null;
        }*/
    }

    /**
     * ******************************Email manipulation methods**************************
     */

    /**
     * Set the work email in the multi valued attribute - emails
     *
     * @param email
     * @param isPrimary
     * @throws CharonException
     */
    public void setWorkEmail(String email, boolean isPrimary) throws CharonException {
        MultiValuedAttribute emailsAttribute = new MultiValuedAttribute(
                SCIMConstants.UserSchemaConstants.EMAILS, SCIMConstants.CORE_SCHEMA_URI);
        emailsAttribute.setAttributeValue(SCIMConstants.UserSchemaConstants.WORK,
                                          isPrimary, null, email, DataType.STRING);
        attributeList.put(SCIMConstants.UserSchemaConstants.EMAILS, emailsAttribute);
    }

    /**
     * Get the work email from the multi valued attribute - emails
     *
     * @return
     * @throws CharonException
     * @throws NotFoundException
     */
    public String getWorkEmail() throws CharonException, NotFoundException {
        MultiValuedAttribute emailsAttribute = (MultiValuedAttribute) attributeList.get(
                SCIMConstants.UserSchemaConstants.EMAILS);
        return (String) emailsAttribute.getAttributeValueByType(
                SCIMConstants.UserSchemaConstants.WORK);
    }

    /**
     * Set home email in the multi valued attribute - emails.
     *
     * @param email
     * @param isPrimary
     * @throws CharonException
     */
    public void setHomeEmail(String email, boolean isPrimary) throws CharonException {
        MultiValuedAttribute emailsAttribute = new MultiValuedAttribute(
                SCIMConstants.UserSchemaConstants.EMAILS, SCIMConstants.CORE_SCHEMA_URI);
        emailsAttribute.setAttributeValue(SCIMConstants.UserSchemaConstants.HOME,
                                          isPrimary, null, email, DataType.STRING);
        attributeList.put(SCIMConstants.UserSchemaConstants.EMAILS, emailsAttribute);
    }

    /**
     * Get the home email from the multi valued attribute - emails
     *
     * @return
     */
    public String getHomeEmail() throws CharonException, NotFoundException {
        MultiValuedAttribute emailsAttribute = (MultiValuedAttribute) attributeList.get(
                SCIMConstants.UserSchemaConstants.EMAILS);
        return (String) emailsAttribute.getAttributeValueByType(
                SCIMConstants.UserSchemaConstants.HOME);
    }

    /**
     * Set any custom type email provided by user, in the multi valued attribute - email
     *
     * @param email
     * @param isPrimary
     * @param type
     * @throws CharonException
     */
    public void setOtherEmail(String email, boolean isPrimary, String type) throws CharonException {
        MultiValuedAttribute emailsAttribute = new MultiValuedAttribute(
                SCIMConstants.UserSchemaConstants.EMAILS, SCIMConstants.CORE_SCHEMA_URI);
        emailsAttribute.setAttributeValue(type,
                                          isPrimary, null, email, DataType.STRING);
        attributeList.put(SCIMConstants.UserSchemaConstants.EMAILS, emailsAttribute);
    }


    public void setEmails(String[] emails) throws CharonException {
        MultiValuedAttribute emailsAttribute = new MultiValuedAttribute(
                SCIMConstants.UserSchemaConstants.EMAILS, SCIMConstants.CORE_SCHEMA_URI);
        for (String email : emails) {
            emailsAttribute.setSimpleAttributeValue(email, DataType.STRING);
        }
        attributeList.put(SCIMConstants.UserSchemaConstants.EMAILS, emailsAttribute);
    }

    /**
     * Get the email addresses as an array of Strings. Since emails is an multi-valued attribute
     * and since a multi-valued attribute can contain the values in different ways, needs to check
     * for all those possible ways.
     *
     * @return
     */
    public String[] getEmails() throws CharonException {

        //get the emails attribute
        MultiValuedAttribute emailsAttribute = (MultiValuedAttribute) attributeList.get(
                SCIMConstants.UserSchemaConstants.EMAILS);
        //check if the values are stored just as a list of Strings.
        if (emailsAttribute.getValuesAsStrings() != null &&
            emailsAttribute.getValuesAsStrings().size() != 0) {

            return (String[]) emailsAttribute.getValuesAsStrings().toArray();
        } else {
            //check is the values are stored as simple of complex attributes
            List<Attribute> subAttributes = emailsAttribute.getValuesAsSubAttributes();
            List<String> values = new ArrayList<String>();
            if (subAttributes != null && subAttributes.size() != 0) {
                for (Attribute subAttribute : subAttributes) {
                    //if value is a simple attribute of type: "value : "email";
                    if (subAttribute instanceof SimpleAttribute) {
                        values.add((String) ((SimpleAttribute) subAttribute).getValue());
                    } else if (subAttribute instanceof ComplexAttribute) {
                        //if the value is a complex attribute itself, obtain the "value" sub attribute and get the value
                        SimpleAttribute valueAttribute =
                                (SimpleAttribute) (((ComplexAttribute) subAttribute).getSubAttribute(
                                        SCIMConstants.CommonSchemaConstants.VALUE));
                        values.add((String) valueAttribute.getValue());
                    }
                }

            }
            String[] valuesAsStrings = null;
            if (values.size() != 0) {
                valuesAsStrings = new String[values.size()];
                int i = 0;
                for (String value : values) {
                    valuesAsStrings[i] = value;
                    i++;
                }
            }
            //return (String[])values.toString gave class cast exception
            return valuesAsStrings;
        }
    }

    public String getPrimaryEmail() throws CharonException, NotFoundException {
        MultiValuedAttribute emailsAttribute = (MultiValuedAttribute) attributeList.get(
                SCIMConstants.UserSchemaConstants.EMAILS);
        return (String) emailsAttribute.getPrimaryValue();
    }

    /**
     * Retrieve any custom type email set by the user in the multi valued attribute - email
     *
     * @param type
     * @return
     * @throws CharonException
     * @throws NotFoundException
     */
    public String getEmailByType(String type) throws CharonException, NotFoundException {
        MultiValuedAttribute emailsAttribute = (MultiValuedAttribute) attributeList.get(
                SCIMConstants.UserSchemaConstants.EMAILS);
        return (String) emailsAttribute.getAttributeValueByType(type);
    }

    /**
     * ************DisplayName manipulation methods.*************************************
     */

    public String getDisplayName() throws CharonException {
        return getSimpleAttributeStringVal(SCIMConstants.UserSchemaConstants.DISPLAY_NAME);
    }

    public void setDisplayName(String displayName) throws CharonException {
        setSimpleAttribute(SCIMConstants.UserSchemaConstants.DISPLAY_NAME,
                           SCIMSchemaDefinitions.USER_DISPLAY_NAME, displayName, DataType.STRING);
    }

    /**
     * ***********************Name manipulation methods**********************************
     */
    private void createName() {
                
    }

    /**
     * Update the attribute value by attribute name. Needs to be overloaded by specific types of
     * attributes.
     *
     * @param attributeName
     * @param attributeValue public void updateValue(Object value) {
     *                       this.value = value;
     *                       }
     *                       public void updateValue(Object value) {
     *                       this.value = value;
     *                       }
     */
    /*@Override
    public void updateAttribute(String attributeName, Object attributeValue) {
        //To change body of implemented methods use File | Settings | File Templates.
    }*/

    /**
     * Validates whether the given SCIM object adheres to the SCIM schema.
     *
     * @param scimObject
     * @return
     */
    public boolean validate(SCIMObject scimObject) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Take common functionality of setting a value to a simple attribute, into one place.
     *
     * @param attributeName
     * @param attributeSchema
     * @param value
     * @param dataType
     * @throws CharonException
     */
    private void setSimpleAttribute(String attributeName, AttributeSchema attributeSchema,
                                    Object value, DataType dataType) throws CharonException {
        if (isAttributeExist(attributeName)) {
            ((SimpleAttribute) attributeList.get(attributeName)).updateValue(value, dataType);
        } else {
            SimpleAttribute simpleAttribute = new SimpleAttribute(
                    attributeName, value);
            /*SimpleAttribute userNameAttribute = new SimpleAttribute(
                    SCIMConstants.UserSchemaConstants.USER_NAME,
                    SCIMConstants.CORE_SCHEMA_URI, userName, DataType.STRING,
                    false, false);*/
            simpleAttribute = (SimpleAttribute) DefaultAttributeFactory.createAttribute(
                    attributeSchema, simpleAttribute);
            attributeList.put(attributeName, simpleAttribute);
        }
    }

    private String getSimpleAttributeStringVal(String attributeName) throws CharonException {
        if (isAttributeExist(attributeName)) {
            return ((SimpleAttribute) attributeList.get(attributeName)).getStringValue();
        } else {
            return null;
        }
    }


}
