/*
 * Copyright 2008,2009 WSO2, Inc. http://www.wso2.org.
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
package org.wso2.mercury.keys;

/**
 * internal sequence key is used to identify the messages
 * belongs to the same sequence
 *
 */
public class InternalSequenceKey {
    private String endPointAddress;
    private String internalKey;

    public InternalSequenceKey(String endPointAddress, String internalKey) {
        this.endPointAddress = endPointAddress;
        this.internalKey = internalKey;
    }

    public int hashCode() {
        return (endPointAddress + internalKey).hashCode();
    }

    public boolean equals(Object obj) {
        boolean isEqual = false;
        if (obj instanceof InternalSequenceKey){
            InternalSequenceKey internalSequenceKey = (InternalSequenceKey) obj;
            if ((this.endPointAddress == null) && (this.internalKey == null)){
               isEqual = (internalSequenceKey.getEndPointAddress() == null) &&
                       (internalSequenceKey.getInternalKey() == null);
            } else if (this.endPointAddress == null){
               // i.e internal key is not null
               isEqual = (internalSequenceKey.getEndPointAddress() == null) &&
                       (this.internalKey.equals(internalSequenceKey.getInternalKey()));
            } else if (this.internalKey == null) {
               // i.e endpoint address is not null
               isEqual = (this.internalKey == null) &&
                       (this.endPointAddress.equals(internalSequenceKey.getEndPointAddress()));
            } else {
                 // here both are not equal null.
                isEqual = this.endPointAddress.equals(internalSequenceKey.getEndPointAddress()) &&
                        this.internalKey.equals(internalSequenceKey.getInternalKey());
            }
        }
        return isEqual;
    }

    public String getEndPointAddress() {
        return endPointAddress;
    }

    public void setEndPointAddress(String endPointAddress) {
        this.endPointAddress = endPointAddress;
    }

    public String getInternalKey() {
        return internalKey;
    }

    public void setInternalKey(String internalKey) {
        this.internalKey = internalKey;
    }

    public String toString() {
        return "Key ==> " + this.internalKey + " ip ==> " + this.endPointAddress;
    }

}
