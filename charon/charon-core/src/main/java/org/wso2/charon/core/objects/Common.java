/*
*  Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.Most of the large enterprise systems consists of many software systems. These different systems provide various functionalities required by the whole system. Therefore in order to provide some features across the platform these smaller software modules has to communicate with each other. However different software modules use different technologies and provide various protocols to communicate with the external systems. Standard transports like HTTP, SMTP, JMS, FTP, FIX and various adapter types such as SAP are widely being used. These transports can use different message formats such as SOAP, POX messages and different text message formats. Hence integrating these systems efficient and manageable manner in a distributed system is a well known problem.

0
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
import org.wso2.charon.core.attributes.SimpleAttribute;
import org.wso2.charon.core.exceptions.CharonException;
import org.wso2.charon.core.exceptions.NotFoundException;
import org.wso2.charon.core.protocol.ResponseCodeConstants;
import org.wso2.charon.core.schema.SCIMConstants;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This represents the object which is a collection of attributes defined by common-schema.
 * These attributes MUST be included in all other objects which become SCIM resources.
 */
public abstract class Common implements SCIMObject {

    /*Collection of attributes which constitute this resource.*/
    protected Map<String, Attribute> attributeList = new HashMap<String, Attribute>();

    /*List of schemas where the attributes of this resource, are defined.*/
    protected List<String> schemaList = new ArrayList<String>();

    public Map<String, Attribute> getAttributeList() {
        return attributeList;
    }

    public void setAttributeList(Map<String, Attribute> attributeList) {
        this.attributeList = attributeList;
    }

    public List<String> getSchemaList() {
        return schemaList;
    }

    public void setSchemaList(List<String> schemaList) {
        this.schemaList = schemaList;
        //TODO:set these as attribute also
    }
                                
    /**
     * Create an attribute and set in the SCIM Object.
     *
     * @param newAttribute
     */
    public void setAttribute(Attribute newAttribute) {
        //and update the schemas list if any new schema used in the attribute, and create schemas array.
        if (!isSchemaExists(newAttribute.getSchema())) {
            schemaList.add(newAttribute.getSchema());
        }
        //add the attribute to attribute map
        if (!isAttributeExist(newAttribute.getName())) {
            attributeList.put(newAttribute.getName(), newAttribute);
        }
    }

    /**
     * Update the attribute value by attribute name. Needs to be overloaded by specific types of
     * attributes.
     *
     * @param attributeName
     * @param attributeValue
     */
    //public abstract void updateAttribute(String attributeName, Object attributeValue);
    public Attribute getAttribute(String attributeName) throws NotFoundException {
        if (attributeList.containsKey(attributeName)) {
            return attributeList.get(attributeName);
        } else {
            throw new NotFoundException();
        }
    }

    protected boolean isSchemaExists(String schemaName) {
        return schemaList.contains(schemaName);
    }

    protected boolean isAttributeExist(String attributeName) {
        return attributeList.containsKey(attributeName);
    }

    /*******Methods to manipulate the common attributes defined in SCIM Core Spec**********/

    //1.id attribute

    /**
     * Set a value for the id attribute. If attribute not already created in the resource,
     * create attribute and set the value.
     * Unique identifier for the SCIM Resource as defined by the Service Provider
     *
     * @param id Unique identifier for the SCIM Resource as defined by the Service Provider.
     */
    public void setId(String id) throws CharonException {
        if (isAttributeExist(SCIMConstants.SCIMCommonSchemaConstants.ID)) {
            ((SimpleAttribute) attributeList.get(
                    SCIMConstants.SCIMCommonSchemaConstants.ID)).updateValue(
                    id, SimpleAttribute.DataType.STRING);
        } else {
            Attribute idAttribute = new SimpleAttribute(
                    SCIMConstants.SCIMCommonSchemaConstants.ID,
                    SCIMConstants.CORE_SCHEMA_URI, id, SimpleAttribute.DataType.STRING, true,
                    false);
            this.setAttribute(idAttribute);
        }

    }

    /**
     * Get the value of id attribute.
     * Unique identifier for the SCIM Resource as defined by the Service Provider.
     *
     * @param id
     * @return
     */
    public String getId(String id) throws CharonException, NotFoundException {
        if (isAttributeExist(SCIMConstants.SCIMCommonSchemaConstants.ID)) {
            return ((SimpleAttribute) attributeList.get(
                    SCIMConstants.SCIMCommonSchemaConstants.ID)).getStringValue();
        } else {
            throw new NotFoundException();
        }
    }

    //2. external id attribute

    /**
     * Set the value for externalId attribute.
     * Unique identifier for the Resource as defined by the Service Consumer.
     *
     * @param externalId
     */
    public void setExternalId(String externalId) throws CharonException {
        if (isAttributeExist(SCIMConstants.SCIMCommonSchemaConstants.EXTERNAL_ID)) {
            ((SimpleAttribute) attributeList.get(
                    SCIMConstants.SCIMCommonSchemaConstants.ID)).updateValue(
                    externalId, SimpleAttribute.DataType.STRING);
        } else {
            Attribute externalIdAttribute = new SimpleAttribute(
                    SCIMConstants.SCIMCommonSchemaConstants.ID,
                    SCIMConstants.CORE_SCHEMA_URI, externalId, SimpleAttribute.DataType.STRING,
                    true, false);
            this.setAttribute(externalIdAttribute);
        }
    }

    /**
     * Get the value of externalId attribute.
     *
     * @param id
     * @return
     * @throws CharonException
     * @throws NotFoundException
     */
    public String getExternalId(String id) throws CharonException, NotFoundException {
        if (isAttributeExist(SCIMConstants.SCIMCommonSchemaConstants.ID)) {
            return ((SimpleAttribute) attributeList.get(
                    SCIMConstants.SCIMCommonSchemaConstants.ID)).getStringValue();
        } else {
            throw new NotFoundException();
        }
    }

    //3. Meta attribute - complex attribute containing resource meta data.

    /**
     * Set created date sub attribute of Meta attribute.
     *
     * @param createdDate
     * @throws org.wso2.charon.core.exceptions.CharonException
     *
     */
    public void setCreatedDate(Date createdDate) throws CharonException {
        //create the created date attribute as defined in schema.
        SimpleAttribute createdDateAttribute = new SimpleAttribute(
                SCIMConstants.SCIMCommonSchemaConstants.CREATED, null, createdDate,
                SimpleAttribute.DataType.DATE_TIME, true, false);
        ComplexAttribute metaAttribute;
        //check meta complex attribute already exist.
        if (getMeAttribute() != null) {
            metaAttribute = getMeAttribute();
            //check created date attribute already exist
            if (metaAttribute.isSubAttributeExist(createdDateAttribute.getName())) {
                //log info level log that created date already set and can't set again.
                throw new CharonException(ResponseCodeConstants.ATTRIBUTE_READ_ONLY);
            } else {

                metaAttribute.setSubAttribute(createdDateAttribute);
            }

        } else {
            //create meta attribute and set the sub attribute.
            createMetaAttribute();
            getMeAttribute().setSubAttribute(createdDateAttribute);

        }
    }

    /**
     * Get the created date attribute of resource meta data.
     *
     * @return
     */
    public Date getCreatedDate() throws NotFoundException, CharonException {
        if (isMetaAttributeExist()) {
            SimpleAttribute createdDate = (SimpleAttribute) getMeAttribute().getSubAttribute(
                    SCIMConstants.SCIMCommonSchemaConstants.CREATED);
            if (createdDate != null) {
                return createdDate.getDateValue();
            } else {
                throw new NotFoundException();
            }
        } else {
            throw new NotFoundException();
        }
    }

    //TODO:do the same above for the other sub attributes defined under meta attribute.

    protected boolean isMetaAttributeExist() {
        return attributeList.containsKey(SCIMConstants.SCIMCommonSchemaConstants.META);
    }

    protected void createMetaAttribute() throws CharonException {
        Attribute metaAttribute = new ComplexAttribute(
                SCIMConstants.SCIMCommonSchemaConstants.META,
                SCIMConstants.CORE_SCHEMA_URI, false, new HashMap<String, Attribute>(), false);
        if (isMetaAttributeExist()) {
            throw new CharonException(ResponseCodeConstants.ATTRIBUTE_ALREADY_EXIST);
        } else {
            attributeList.put(SCIMConstants.SCIMCommonSchemaConstants.META, metaAttribute);
        }
    }

    protected ComplexAttribute getMeAttribute() {
        if (isMetaAttributeExist()) {
            return (ComplexAttribute) attributeList.get(
                    SCIMConstants.SCIMCommonSchemaConstants.META);
        } else {
            return null;
        }
    }

    /**
     * Deleting an attribute is the responsibility of an attribute holder.
     *
     * @param id
     */
    public void deleteAttribute(String id) throws NotFoundException {
        if (attributeList.containsKey(id)) {
            attributeList.remove(id);
        } else {
            throw new NotFoundException();
        }
    }


}
