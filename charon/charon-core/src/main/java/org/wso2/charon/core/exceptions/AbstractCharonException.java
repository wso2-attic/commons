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
package org.wso2.charon.core.exceptions;

/**
 * If an error occurs in SCIM operation,in addition to returning the HTTP response code,
 * an human readable explanation should also be returned in the body.
 * This class abstract out the Exceptions that should be thrown at a failure of SCIM operation and
 * implementers can use code property to decide which HTTP code needs to be set in header of the
 * response.
 */
public class AbstractCharonException extends Exception {

    //human readable explanation of the error.
    protected String description;

    //relevant HTTP code. 
    protected String code;

    public AbstractCharonException(String code, String description){
        this.code = code;
        this.description = description;
    }

    public AbstractCharonException(String message){
        this.description = message;
    }

    public AbstractCharonException() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
