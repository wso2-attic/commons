/*
 * Copyright 2004,2005 The Apache Software Foundation.
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

public class XKMSException extends Exception{
    
    public static final int NO_MATCH = 1;
    public static final int TOO_MANY_RESPONSES = 2;
    public static final int INCOMPLETE = 3;
    public static final int FAILURE = 4;
    public static final int REFEUSED = 5;
    public static final int NO_AUTHENTICATION = 6;
    public static final int MESSAGE_NOT_SUPPORTED = 7;
    public static final int UNKNOWN_RESPONSE_ID = 8;
    public static final int REPRESENT_REQUIRED = 9;
    public static final int NOT_SYNCHRONOUS = 10;
    public static final int OPTIONAL_ELEMENT_NOT_SUPPORTED = 11;
    public static final int PROOF_OF_POSSESSION_REQUIRED = 12;
    public static final int TIME_INSTANT_NOT_SUPPORTED = 13;
    public static final int TIME_INSTANT_OUT_OF_RANGE = 14;
    
    private int errorCode = -1;
    
    public XKMSException() {
    }

    public XKMSException(String message) {
        super(message);
    }
    
    public XKMSException(int errorCode, String messageId) {
        super(messageId);
        setErrorCode(errorCode);
    }

    public XKMSException(int errorCode, String messageId, Throwable cause){
        super(messageId, cause);
        setErrorCode(errorCode);
    }
    
    public XKMSException(String message, Throwable cause) {
        super(message, cause);
    }

    public XKMSException(Throwable cause) {
        super(cause);
    }
    
    public XKMSException(int errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }
    
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
    
    public int getErrorCode() {
        return errorCode;
    }
}
