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

import org.wso2.charon.core.schema.SCIMSchemaDefinitions.DataType;

import java.util.Arrays;
import java.util.List;

/**
 * Schema for the sub attributes defined in the SCIMAttributeSchema. Optional.
 */
public class SCIMSubAttributeSchema implements AttributeSchema {

    //The attribute's name.
    private String name;
    //The attribute's data type
    private DataType type;
    //The attribute's human readable description.
    private String description;
    //A Boolean value that specifies if the attribute is mutable
    private Boolean readOnly;
    //A Boolean value that specifies if the attribute is required.
    private Boolean required;
    //A Boolean value that specifies if the String attribute is case sensitive
    private Boolean caseExact;
    //A collection of canonical values
    private List<String> canonicalValues;

    public SCIMSubAttributeSchema(String name, DataType type, String description,
                                  Boolean readOnly, Boolean required, Boolean caseExact,
                                  String... canonicalValues) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.readOnly = readOnly;
        this.required = required;
        this.caseExact = caseExact;
        this.canonicalValues = Arrays.asList(canonicalValues);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DataType getType() {
        return type;
    }

    public void setType(DataType type) {
        this.type = type;
    }

    public Boolean getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public Boolean getCaseExact() {
        return caseExact;
    }

    public void setCaseExact(Boolean caseExact) {
        this.caseExact = caseExact;
    }

    public List<String> getCanonicalValues() {
        return canonicalValues;
    }

    public void setCanonicalValues(List<String> canonicalValues) {
        this.canonicalValues = canonicalValues;
    }
}
