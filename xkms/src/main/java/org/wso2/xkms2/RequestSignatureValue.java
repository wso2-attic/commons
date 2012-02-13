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

/*
 * 
 */

public class RequestSignatureValue {

    private SignatureValueType signatureValueType;

    public RequestSignatureValue(byte[]base64Binary, String id) {
        signatureValueType = new SignatureValueType();
        signatureValueType.setBase64Binary(base64Binary);
        if (id != null) {
            signatureValueType.setId(id);
        }

    }

    public RequestSignatureValue(byte[]base64Binary){
        this(base64Binary,null);
    }

    public byte[] getBase64Binary() {
        return signatureValueType.getBase64Binary();
    }

    public String getId() {
        return signatureValueType.getId();
    }


    private class SignatureValueType {
        private byte[] base64Binary;
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public byte[] getBase64Binary() {
            return base64Binary;
        }

        public void setBase64Binary(byte[] base64Binary) {
            this.base64Binary = base64Binary;
        }
    }

}
