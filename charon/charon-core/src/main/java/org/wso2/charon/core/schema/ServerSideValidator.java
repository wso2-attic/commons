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
package org.wso2.charon.core.schema;

import org.wso2.charon.core.attributes.AbstractAttribute;
import org.wso2.charon.core.attributes.Attribute;
import org.wso2.charon.core.exceptions.CharonException;
import org.wso2.charon.core.objects.AbstractSCIMObject;
import org.wso2.charon.core.protocol.endpoints.AbstractResourceEndpoint;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * This is to perform SCIM service provider side validation and additions according to SCIM spec.
 */
public class ServerSideValidator extends AbstractValidator {

    /**
     * Add read-only attributes that only the service provider adds and validate SCIM object
     * and attributes.
     *
     * @param scimObject
     * @param resourceSchema
     */
    public static void validateCreatedSCIMObject(AbstractSCIMObject scimObject,
                                                 SCIMResourceSchema resourceSchema)
            throws CharonException {
        //check if read-only attributes are set, if so put a debug level log and remove them.
        removeAnyReadOnlyAttributes(scimObject, resourceSchema);
        //add created and last modified dates
        String id = UUID.randomUUID().toString();
        if (scimObject.getId() != null) {
            scimObject.deleteAttribute(SCIMConstants.CommonSchemaConstants.ID);
            scimObject.setId(id);
        }
        Date date = new Date();
        scimObject.setCreatedDate(date);
        //created n last modified are the same if not updated.
        scimObject.setLastModified(date);
        scimObject.setLocation(AbstractResourceEndpoint.getResourceEndpointURL(SCIMConstants.USER_ENDPOINT));
        //add version

        //validate for required attributes.
        validateSCIMObjectForRequiredAttributes(scimObject, resourceSchema);
        validateSchemaList(scimObject, resourceSchema);
    }

    public static void validateUpdatedSCIMObject(AbstractSCIMObject scimObject,
                                                 SCIMResourceSchema resourceSchema) {

    }

    public static void validateRetrievedSCIMObject(AbstractSCIMObject scimObject,
                                                   SCIMResourceSchema resourceSchema) {

    }

    /**
     * In the process of validating SCIM objects being created in service provider side, we need to remove
     * any read-only attributes that are added by clients and remove them.
     *
     * @param scimObject
     * @param resourceSchema
     */
    private static void removeAnyReadOnlyAttributes(AbstractSCIMObject scimObject,
                                                    SCIMResourceSchema resourceSchema)
            throws CharonException {
        //get attributes from schema.
        List<AttributeSchema> attributeSchemaList = resourceSchema.getAttributesList();
        //get attribute list from scim object.
        Map<String, Attribute> attributeList = scimObject.getAttributeList();
        for (AttributeSchema attributeSchema : attributeSchemaList) {
            //check for read-only attributes.
            if (attributeSchema.getReadOnly()) {
                if (attributeList.containsKey(attributeSchema.getName())) {
                    String error = "Read only attribute: " + attributeSchema.getName() +
                                   " is set from consumer in the SCIM Object. " + "Removing it.";
                    //TODO:put a debug level log and remove the attribute.
                    scimObject.deleteAttribute(attributeSchema.getName());
                }
            }
            //check for readonly sub attributes.
            AbstractAttribute attribute = (AbstractAttribute) attributeList.get(attributeSchema.getName());
            if (attribute != null) {
                List<SCIMSubAttributeSchema> subAttributesSchemaList =
                        ((SCIMAttributeSchema) attributeSchema).getSubAttributes();

                for (SCIMSubAttributeSchema subAttributeSchema : subAttributesSchemaList) {
                    if (subAttributeSchema.getReadOnly()) {
                        if (attribute.getSubAttribute(subAttributeSchema.getName()) == null) {
                            String error = "Readonly sub attribute: " + subAttributeSchema.getName()
                                           + " is set in the SCIM Attribute: " + attribute.getName() +
                                           ". Removing it.";
                            attribute.removeSubAttribute(subAttributeSchema.getName());
                        }
                    }
                }

            }
        }

    }

}
