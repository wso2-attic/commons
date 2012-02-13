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
package org.wso2.charon.core.attributes;

import org.wso2.charon.core.exceptions.CharonException;
import org.wso2.charon.core.protocol.ResponseCodeConstants;

import javax.xml.bind.DatatypeConverter;
import java.util.Date;

/**
 * This class represents a SimpleAttribute defined in SCIM Core Schema Spec.
 */
public class SimpleAttribute extends AbstractAttribute {

    /*In a simple attribute, only one attribute value is present.*/
    protected Object value;

    /*Data type of the attribute value.*/
    protected DataType dataType;

    /*Enum that defines the data types that the attribute value can take.*/

    /**
     * Validate whether the attribute adheres to the SCIM schema.
     *
     * @param attribute
     * @return
     */
    public boolean validate(Attribute attribute) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public enum DataType {
        STRING, BOOLEAN, DECIMAL, INTEGER, DATE_TIME, BINARY
    }

    /**
     * Create the attribute with the given name. Attribute name can be set only when creating the
     * attribute.
     *
     * @param attributeName Name of the attribute
     */
    public SimpleAttribute(String attributeName) {
        super(attributeName);
    }

    /**
     * Create the attribute with given name and schema name.
     *
     * @param attributeName - Name of the attribute
     * @param schema        - schema in which the attribute is defined.
     */
    public SimpleAttribute(String attributeName, String schema) {
        super(attributeName, schema);
    }

    /**
     * Create attribute with given name, schema name,whether it is readOnly and optional.
     *
     * @param attributeName - Name of the attribute
     * @param schema        - schema in which the attribute is defined
     * @param readOnly      - whether attribute is readOnly
     * @param optional      - whether attribute is optional
     */
    public SimpleAttribute(String attributeName, String schema, boolean readOnly,
                           boolean optional) {
        super(attributeName, schema, readOnly, optional);
    }

    /**
     * Create attribute with given name, schema name,attribute value, attribute data type,
     * whether it is readOnly and optional.
     *
     * @param attributeName  - Name of the attribute
     * @param schema         - schema in which the attribute is defined
     * @param attributeValue - value of the attribute
     * @param dataType       - data type of the attribute value.
     * @param readOnly       - whether attribute is readOnly
     * @param optional       - whether attribute is optional
     */
    public SimpleAttribute(String attributeName, String schema, Object attributeValue,
                           DataType dataType, boolean readOnly, boolean optional)
            throws CharonException {
        super(attributeName, schema, readOnly, optional);
        try {
            if (isAttributeDataTypeValid(attributeValue, dataType)) {
                this.value = attributeValue;
                this.dataType = dataType;
            }
        } catch (CharonException exception) {
            //log error
            throw exception;
        }
    }

    /**
     * Create attribute with given name, schema name, attribute value and attribute data type.
     *
     * @param attributeName  - Name of the attribute
     * @param schema         - schema in which the attribute is defined
     * @param attributeValue - value of the attribute
     * @param dataType       - data type of the attribute value.
     */
    public SimpleAttribute(String attributeName, String schema, Object attributeValue,
                           DataType dataType) throws CharonException {
        super(attributeName, schema);
        try {
            if (isAttributeDataTypeValid(attributeValue, dataType)) {
                this.value = attributeValue;
                this.dataType = dataType;
            }
        } catch (CharonException exception) {
            //log error
            throw exception;
        }
    }


    /**
     * Get the attribute value. This method is implemented to return the attribute value in the
     * data type of the attribute value which was set when setting the attribute value.
     *
     * @return Value of the attribute.
     */
    //@Override
    public Object getValue() {
        switch (dataType) {
            case STRING:
                return (String) value;
            case BOOLEAN:
                return (Boolean) value;
            case DECIMAL:
                return (Double) value;
            case INTEGER:
                return (Integer) value;
            case DATE_TIME:
                return (Date) value;
            case BINARY:
                return DatatypeConverter.printBase64Binary((byte[]) value);

        }
        //if data type is not set, return the string value.
        return (String) value;
    }

    /**
     * If the caller knows which datatype the attribute value is stored, then the attribute value
     * can be retrieved with correct datatype.
     *
     * @return
     * @throws CharonException
     */
    public String getStringValue() throws CharonException {
        if (dataType.equals(DataType.STRING)) {
            return (String) value;
        } else {
            throw new CharonException(ResponseCodeConstants.MISMATCH_IN_REQUESTED_DATATYPE);
        }
    }
    //TODO:do this to other datatypes as well.

    public Date getDateValue() throws CharonException {
        if (dataType.equals(DataType.DATE_TIME)) {
            return (Date) value;
        } else {
            throw new CharonException(ResponseCodeConstants.MISMATCH_IN_REQUESTED_DATATYPE);
        }
    }

    public Boolean getBooleanValue() throws CharonException {
        if (dataType.equals(DataType.BOOLEAN)) {
            return (Boolean) value;
        } else {
            throw new CharonException(ResponseCodeConstants.MISMATCH_IN_REQUESTED_DATATYPE);
        }
    }

    /**
     * Update the attribute with given value.
     *
     * @param value New value to be updated. This abstract method should be implemented in
     *              respective attribute types.
     */
    public void updateValue(Object value, DataType datatype) throws CharonException {
        if (isAttributeDataTypeValid(value, dataType)) {

            this.value = value;
        }
    }

    /**
     * When an attribute is created with value and data type provided, we need to validate whether
     * they are matching.
     *
     * @param attributeValue
     * @param attributeDataType
     * @return
     * @throws CharonException
     */
    protected boolean isAttributeDataTypeValid(Object attributeValue, DataType attributeDataType)
            throws CharonException {
        switch (attributeDataType) {
            case STRING:
                return attributeValue instanceof String;
            case BOOLEAN:
                return attributeValue instanceof Boolean;
            case DECIMAL:
                return attributeValue instanceof Double;
            case INTEGER:
                return attributeValue instanceof Integer;
            case DATE_TIME:
                return attributeValue instanceof Date;
            case BINARY:
                return attributeValue instanceof Byte[];

        }
        throw new CharonException(ResponseCodeConstants.MISMATCH_IN_REQUESTED_DATATYPE);
    }
}