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

public class ResultMinor {

    public static final ResultMinor NO_MATCH = new ResultMinor(
            "http://www.w3.org/2002/03/xkms#NoMatch");

    public static final ResultMinor TOO_MANY_RESPONSES = new ResultMinor(
            "http://www.w3.org/2002/03/xkms#TooManyResponses");

    public static final ResultMinor INCOMPLETE = new ResultMinor(
            "http://www.w3.org/2002/03/xkms#Incomplete");

    public static final ResultMinor FAILURE = new ResultMinor(
            "http://www.w3.org/2002/03/xkms#Failure");

    public static final ResultMinor REFUSED = new ResultMinor(
            "http://www.w3.org/2002/03/xkms#Refused");

    public static final ResultMinor NO_AUTHENTICATION = new ResultMinor(
            "http://www.w3.org/2002/03/xkms#NoAuthentication");

    public static final ResultMinor MESSAGE_NOT_SUPPORTED = new ResultMinor(
            "http://www.w3.org/2002/03/xkms#MessageNotSupported");

    public static final ResultMinor UNKNOWN_RESPONSE_ID = new ResultMinor(
            "http://www.w3.org/2002/03/xkms#UnknownResponseId");

    public static final ResultMinor REPRESENT_REQUIRED = new ResultMinor(
            "http://www.w3.org/2002/03/xkms#RepresentRequired");

    public static final ResultMinor NOT_SYNCHRONOUS = new ResultMinor(
            "http://www.w3.org/2002/03/xkms#NotSynchronous");

    public static final ResultMinor OPTIONAL_ELEMENT_NOT_SUPPORTED = new ResultMinor(
            "http://www.w3.org/2002/03/xkms#OptionalElementNotSupported");

    public static final ResultMinor PROOF_OF_POSSESSION_REQUIRED = new ResultMinor(
            "http://www.w3.org/2002/03/xkms#ProofOfPossessionRequired");

    public static final ResultMinor TIME_INSTANT_NOT_SUPPORTED = new ResultMinor(
            "http://www.w3.org/2002/03/xkms#TimeInstantNotSupported");

    public static final ResultMinor TIME_INSTANT_OUT_OF_RANGE = new ResultMinor(
            "http://www.w3.org/2002/03/xkms#TimeInstantOutOfRange");

    private String anyURI;

    private ResultMinor(String anyURI) {
        this.anyURI = anyURI;
    }

    public String getAnyURI() {
        return anyURI;
    }

    public static ResultMinor valueOf(String anyURI) throws XKMSException {
        if (anyURI.equals(NO_MATCH.getAnyURI())) {
            return NO_MATCH;
        } else if (anyURI.equals(TOO_MANY_RESPONSES.getAnyURI())) {
            return TOO_MANY_RESPONSES;
        } else if (anyURI.equals(INCOMPLETE.getAnyURI())) {
            return INCOMPLETE;
        } else if (anyURI.equals(FAILURE.getAnyURI())) {
            return FAILURE;
        } else if (anyURI.equals(REFUSED.getAnyURI())) {
            return REFUSED;
        } else if (anyURI.equals(NO_AUTHENTICATION.getAnyURI())) {
            return NO_AUTHENTICATION;
        } else if (anyURI.equals(MESSAGE_NOT_SUPPORTED.getAnyURI())) {
            return MESSAGE_NOT_SUPPORTED;
        } else if (anyURI.equals(UNKNOWN_RESPONSE_ID.getAnyURI())) {
            return UNKNOWN_RESPONSE_ID;
        } else if (anyURI.equals(REPRESENT_REQUIRED.getAnyURI())) {
            return REPRESENT_REQUIRED;
        } else if (anyURI.equals(NOT_SYNCHRONOUS.getAnyURI())) {
            return NOT_SYNCHRONOUS;
        } else if (anyURI.equals(OPTIONAL_ELEMENT_NOT_SUPPORTED.getAnyURI())) {
            return OPTIONAL_ELEMENT_NOT_SUPPORTED;
        } else if (anyURI.equals(PROOF_OF_POSSESSION_REQUIRED.getAnyURI())) {
            return PROOF_OF_POSSESSION_REQUIRED;
        } else if (anyURI.equals(TIME_INSTANT_NOT_SUPPORTED.getAnyURI())) {
            return TIME_INSTANT_NOT_SUPPORTED;
        } else if (anyURI.equals(TIME_INSTANT_OUT_OF_RANGE.getAnyURI())) {
            return TIME_INSTANT_OUT_OF_RANGE;
        }

        throw new XKMSException(ResultMinor.class.getName()
                + " validation failed");
    }

}
