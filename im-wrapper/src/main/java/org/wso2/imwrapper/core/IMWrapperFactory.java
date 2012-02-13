/*
 * Copyright 2006,2007 WSO2, Inc. http://www.wso2.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wso2.imwrapper.core;

import org.wso2.imwrapper.jabber.JabberWrapperImpl;
import org.wso2.imwrapper.msn.MSNWrapperImpl;
import org.wso2.imwrapper.oscar.OscarWrapperImpl;
import org.wso2.imwrapper.yahoo.YahooWrapperImpl;

public class IMWrapperFactory {

    public static String MSN = "msn";
    public static String AIM = "aim";
    public static String ICQ = "icq";
    public static String JABBER = "jabber";
    public static String YAHOO = "yahoo";

    public static IMWrapper createIMProtocolImpl(String protocol) throws IMException {
        if (MSN.equalsIgnoreCase(protocol)) {
            return new MSNWrapperImpl();
        } else if (AIM.equalsIgnoreCase(protocol) || ICQ.equalsIgnoreCase(protocol)) {
            return new OscarWrapperImpl();
        } else if (JABBER.equalsIgnoreCase(protocol)) {
            return new JabberWrapperImpl();
        } else if (YAHOO.equalsIgnoreCase(protocol)) {
            return new YahooWrapperImpl();
        } else {
            throw new IMException(
                    "Unsupported protocol, Supported protocols are msn, aim, icq and jabber");
        }
    }
}
