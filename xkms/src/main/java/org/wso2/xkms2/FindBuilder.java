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

import org.wso2.xkms2.builder.*;
/*
 * 
 */

public final class FindBuilder {

    public static ElementBuilder find(String type) {

        if (type.equals(XKMS2Constants.ELE_LOCATE_REQUEST.getLocalPart())) {
            return new LocateRequestBuilder();

        } else if (type.equals(XKMS2Constants.ELE_LOCATE_RESULT.getLocalPart())){
            return LocateResultBuilder.INSTANCE;

        } else if (type.equals(XKMS2Constants.ELE_VALIDATE_REQUEST.getLocalPart())) {
            return new ValidateRequestBuilder();

        } else if (type.equals(XKMS2Constants.ELE_VALIDATE_RESULT.getLocalPart())) {
            return ValidateResultBuilder.INSTANCE;

        } else if (type.equals(XKMS2Constants.ELEM_REGISTER_REQUEST)) {
            return RegisterRequestBuilder.INSTANCE;
            
        } else if (type.equals(XKMS2Constants.ELEM_REISSUE_REQUEST)) {
            return ReissueRequestBuilder.INSTANCE;
            
        } else if (type.equals(XKMS2Constants.ELEM_RECOVER_REQUEST)) {
            return RecoverRequestBuilder.INSTANCE;
            
        } else {
            //TODO Need X-KRSS stuff here
            throw new UnsupportedOperationException("TODO Need X-KRSS; RegisterResult, reissue, revoke, and recover");
        }
    }
}
