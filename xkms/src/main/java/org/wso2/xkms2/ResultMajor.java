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

public class ResultMajor {

    public static final ResultMajor SUCCESS =
            new ResultMajor("http://www.w3.org/2002/03/xkms#Success");
    public static final ResultMajor VERSION_MISMATCH =
            new ResultMajor("http://www.w3.org/2002/03/xkms#VersionMismatch");
    public static final ResultMajor SENDER =
            new ResultMajor("http://www.w3.org/2002/03/xkms#Sender");
    public static final ResultMajor RECEIVER =
            new ResultMajor("http://www.w3.org/2002/03/xkms#Receiver");
    public static final ResultMajor REPRESENT =
            new ResultMajor("http://www.w3.org/2002/03/xkms#Represent");
    public static final ResultMajor PENDING =
            new ResultMajor("http://www.w3.org/2002/03/xkms#Pending");

    private String anyURI;

    private ResultMajor(String anyURI) {
        this.anyURI = anyURI;
    }

    public String getAnyURI() {
        return anyURI;
    }

    public static ResultMajor validate(String anyURI) throws XKMSException {
        if (anyURI.equals(SUCCESS.getAnyURI()) || anyURI.equals(VERSION_MISMATCH.getAnyURI()) ||
            anyURI.equals(SENDER.getAnyURI()) || anyURI.equals(RECEIVER.getAnyURI()) ||
            anyURI.equals(REPRESENT.getAnyURI()) || anyURI.equals(PENDING.getAnyURI())) {
            return new ResultMajor(anyURI);

        }
        throw new XKMSException(ResultMajor.class.getName() + " validation failed");
    }


}
