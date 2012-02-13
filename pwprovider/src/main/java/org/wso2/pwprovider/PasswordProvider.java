package org.wso2.pwprovider;
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
import java.util.Properties;

/**
 * This provide abstraction to get resolve actual password
 * By implementing this, it is possible to securely provide passwords.
 * This is not a solution but just API, implementer has to be cared about how to keep password securely.
 */
public interface PasswordProvider {
    /**
     * Initialization of the password provider with configuration properties.
     *
     * @param properties Properties to be used for initialization
     */
    void init(Properties properties);

    /**
     * Getting resolved alias password
     *
     * @param encryptedPassword Alias password - this can be encrypted value or not
     * @return Resolved password
     */
    String resolve(String encryptedPassword);
}
