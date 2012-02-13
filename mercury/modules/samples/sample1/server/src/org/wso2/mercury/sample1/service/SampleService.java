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
package org.wso2.mercury.sample1.service;

import org.wso2.mercury.sample1.exception.CustomException;

public class SampleService {

    public void receiveInt(int param) {
        System.out.println("Receive the param ==> " + param);
    }

    public int echoInt(int param) {
        System.out.println("Receive the param ==> " + param);
        return param;
    }

    public int throwFault(int param) throws CustomException {
        CustomException customException = new CustomException("Sample Error message");
        customException.setErrorMessage("Error Message");
        throw customException;
    }
}
