/*
 * Copyright 2005-2007 WSO2, Inc. (http://wso2.com)
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

package org.wso2.usermanager.i18n;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * 
 * i18n message retriever.
 */
public class Messages {

    public final static String DEFAULT_RESOURCES = "org.wso2.usermanager.resources";

    private static ResourceBundle messages;

    private static Messages msgs;

    private Messages(ResourceBundle rb) {
        messages = rb;
    }

    public static Messages getInstance() {
        if (messages == null) {
            messages = ResourceBundle.getBundle(DEFAULT_RESOURCES);
            msgs = new Messages(messages);
        }
        return msgs;
    }

    public String getMessage(String key, Object[] args) {
        return MessageFormat.format(messages.getString(key), args);
    }

    public String getMessage(String key) {
        return messages.getString(key);
    }
}
