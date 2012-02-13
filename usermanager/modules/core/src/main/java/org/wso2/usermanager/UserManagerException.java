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

package org.wso2.usermanager;

import org.wso2.usermanager.i18n.Messages;

/**
 * UserManagerException class. 
 */
public class UserManagerException
        extends Exception {

    private static final long serialVersionUID = 773062447392531899L;

    private static Messages messages = Messages.getInstance();

    /** Constructor with message internationalization*/
    public UserManagerException(String key, Object[] args) {
        super(messages.getMessage(key, args));
    }

    /** Constructor with message internationalization*/
    public UserManagerException(String key) {
        this(key, (Object[]) null);
    }

    /** Constructor with message internationalization*/
    public UserManagerException(String key, boolean value) {
        super(key);
    }

    /** Constructor with message internationalization*/
    public UserManagerException(String key, Object[] args, Throwable e) {
        super(messages.getMessage(key, args), e);
    }

    /** Constructor with message internationalization*/
    public UserManagerException(String key, Throwable e) {
        this(key, new Object[] { e.getMessage() }, e);
    }
}
