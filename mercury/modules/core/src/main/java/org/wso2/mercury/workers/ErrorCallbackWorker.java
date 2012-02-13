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
package org.wso2.mercury.workers;

import org.wso2.mercury.callback.MercuryErrorCallback;
import org.wso2.mercury.exception.RMException;

/**
 * used to invoke the error call backs
 */

public class ErrorCallbackWorker implements Runnable{

    private MercuryErrorCallback errorCallback;
    private RMException rmException;

    public ErrorCallbackWorker(MercuryErrorCallback errorCallback, RMException rmException) {
        this.errorCallback = errorCallback;
        this.rmException = rmException;
    }

    public void run() {
        errorCallback.onError(rmException);
    }
}
