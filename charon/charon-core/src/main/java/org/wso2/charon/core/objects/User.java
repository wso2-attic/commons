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

import org.wso2.charon.core.attributes.MultiValuedAttribute;
import org.wso2.charon.core.attributes.SimpleAttribute;
import org.wso2.charon.core.exceptions.CharonException;
import org.wso2.charon.core.exceptions.NotFoundException;
import org.wso2.charon.core.schema.SCIMConstants;

/**
 * Represents the object which is a collection of attributes defined by SCIM User-schema.
 */
public class User extends Common {

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
        if (isAttributeExist(SCIMConstants.SCIMUserSchemaConstants.USER_NAME)) {
            ((SimpleAttribute) attributeList.get(
                    SCIMConstants.SCIMUserSchemaConstants.USER_NAME)).updateValue(
                    userName, SimpleAttribute.DataType.STRING);
        } else {
            //TODO:since the constrctor is too long, pass an attribute schema. 
            SimpleAttribute userNameAttribute = new SimpleAttribute(
                    SCIMConstants.SCIMUserSchemaConstants.USER_NAME,
                    SCIMConstants.CORE_SCHEMA_URI, userName, SimpleAttribute.DataType.STRING,
                    false, false);
            attributeList.put(SCIMConstants.SCIMUserSchemaConstants.USER_NAME,
                              userNameAttribute);
        }
    }

    /**
     * Get UserName attribute of the user.
     *
     * @return
     * @throws NotFoundException
     * @throws CharonException
     */
    public String getUserName() throws NotFoundException, CharonException {
        if (isAttributeExist(SCIMConstants.SCIMUserSchemaConstants.USER_NAME)) {
            return ((SimpleAttribute) attributeList.get(
                    SCIMConstants.SCIMUserSchemaConstants.USER_NAME)).getStringValue();

        } else {
            throw new NotFoundException();
        }
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
                SCIMConstants.SCIMUserSchemaConstants.EMAILS, SCIMConstants.CORE_SCHEMA_URI);
        emailsAttribute.setAttributeValue(SCIMConstants.SCIMUserSchemaConstants.WORK,
                                          isPrimary, null, email, SimpleAttribute.DataType.STRING);
        attributeList.put(SCIMConstants.SCIMUserSchemaConstants.EMAILS, emailsAttribute);
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
                SCIMConstants.SCIMUserSchemaConstants.EMAILS);
        return (String) emailsAttribute.getAttrbuteValueByType(
                SCIMConstants.SCIMUserSchemaConstants.WORK);
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
                SCIMConstants.SCIMUserSchemaConstants.EMAILS, SCIMConstants.CORE_SCHEMA_URI);
        emailsAttribute.setAttributeValue(SCIMConstants.SCIMUserSchemaConstants.HOME,
                                          isPrimary, null, email, SimpleAttribute.DataType.STRING);
        attributeList.put(SCIMConstants.SCIMUserSchemaConstants.EMAILS, emailsAttribute);
    }

    /**
     * Get the home email from the multi valued attribute - emails
     *
     * @return
     */
    public String getHomeEmail() throws CharonException, NotFoundException {
        MultiValuedAttribute emailsAttribute = (MultiValuedAttribute) attributeList.get(
                SCIMConstants.SCIMUserSchemaConstants.EMAILS);
        return (String) emailsAttribute.getAttrbuteValueByType(
                SCIMConstants.SCIMUserSchemaConstants.HOME);
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
                SCIMConstants.SCIMUserSchemaConstants.EMAILS, SCIMConstants.CORE_SCHEMA_URI);
        emailsAttribute.setAttributeValue(type,
                                          isPrimary, null, email, SimpleAttribute.DataType.STRING);
        attributeList.put(SCIMConstants.SCIMUserSchemaConstants.EMAILS, emailsAttribute);
    }


    public void setEmails(String[] emails) throws CharonException {
        MultiValuedAttribute emailsAttribute = new MultiValuedAttribute(
                SCIMConstants.SCIMUserSchemaConstants.EMAILS, SCIMConstants.CORE_SCHEMA_URI);
        for (String email : emails) {
            emailsAttribute.setSimpleAttributeValue(email, SimpleAttribute.DataType.STRING);
        }
        attributeList.put(SCIMConstants.SCIMUserSchemaConstants.EMAILS, emailsAttribute);
    }

    /**
     * TODO
     * @return
     */
    /*public String[] getEmails() {
        MultiValuedAttribute emailsAttribute = (MultiValuedAttribute) attributeList.get(
                SCIMSchemaConstants.SCIMUserSchemaConstants.EMAILS);
        return emailsAttribute.g       
    }*/

    public String getPrimaryEmail() throws CharonException, NotFoundException {
        MultiValuedAttribute emailsAttribute = (MultiValuedAttribute) attributeList.get(
                SCIMConstants.SCIMUserSchemaConstants.EMAILS);
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
                SCIMConstants.SCIMUserSchemaConstants.EMAILS);
        return (String) emailsAttribute.getAttrbuteValueByType(type);
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
}
