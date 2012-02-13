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
package org.wso2.xkms2;

import org.apache.xml.security.signature.XMLSignature;

public class Result {
    
    private XMLSignature requestSignatureValue;
    private String requestId;
    private ResultMajor resultMajor;
    private ResultMinor resultMinor;
    
    
    public void setRequestSignatureValue(XMLSignature requestSignatureValue) {
        this.requestSignatureValue = requestSignatureValue;
    }
    
    public XMLSignature getRequestSignatureValue() {
        return requestSignatureValue;
    }
    
    public void setReqeustId(String requestId) {
        this.requestId =requestId;
    }
    
    public String getRequestId() {
        return requestId;
    }
    
    public void setResultMajor(ResultMajor resultMajor) {
        this.resultMajor = resultMajor;
    }
    
    public ResultMajor getResultMajor() {
        return resultMajor;
    }
    
    public void setResultMinor(ResultMinor resultMinor) {
        this.resultMinor = resultMinor;
    }
    
    public ResultMinor getResultMinor() {
        return resultMinor;
    }
}
