/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.wso2.xkms2.core;

import org.apache.axis2.context.MessageContext;
import org.w3c.dom.Document;
import org.wso2.xkms2.RequestAbstractType;

public class XKMSRequestData {
    
    
    private RequestAbstractType request;
    private Document document;
    private MessageContext msgCtx;
    
    
    public void setMessageContext(MessageContext msgCtx) {
        this.msgCtx = msgCtx;
    }
    
    public MessageContext getMessageContext() {
        return msgCtx;
    }
    
    public void setRequest(RequestAbstractType request) { 
        this.request = request;
    }
    
    public RequestAbstractType getRequest() {
        return request;
    }
    
    public void setDocument(Document document) {
        this.document = document;
    }
    
    public Document getDocument() {
        return document;
    }
    
    
}
