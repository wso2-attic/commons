/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.siddhi.core.exception;

/**
 * When the initialization failed. Mostly used in object initialization in constructor
 */
public class ProcessorInitializationException extends Exception {

    public ProcessorInitializationException() {
        super();
    }

    public ProcessorInitializationException(String s) {
        super(s);
    }

    public ProcessorInitializationException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ProcessorInitializationException(Throwable throwable) {
        super(throwable);
    }

}
