/*
 * Copyright 2001-2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wso2.xfer;

import javax.xml.namespace.QName;

public class WSTransferException extends Exception {
    
    private QName code;
    private QName subCode;
    private String reason;
    private String detail;
    
    public WSTransferException(QName code, QName subCode, String reason, String detail) {
        this.code = code;
        this.subCode = subCode;
        this.reason = reason;
        this.detail =detail;
        
    }
    public String getDetail() {
        return detail;
    }
    
    public void setDetail(String detail) {
        this.detail = detail;
    }
    
    public QName getSubCode() {
        return subCode;
    }
    
    public void setSubCode(QName faultCode) {
        this.subCode = faultCode;
    }
    
    public String getReason() {
        return reason;
    }
    
    public void setReason(String reason) {
        this.reason = reason;
    }
    
    public QName getCode() {
        return code;
    }
    
    public void setCode(QName subCode) {
        this.code = subCode;
    }
}
