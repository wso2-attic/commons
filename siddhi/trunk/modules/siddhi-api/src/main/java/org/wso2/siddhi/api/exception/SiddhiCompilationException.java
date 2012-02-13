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

package org.wso2.siddhi.api.exception;

/**
 * InvalidEventAttributeNameException throws when attribute names are not specified correctly
 */
public class SiddhiCompilationException extends RuntimeException {

    public SiddhiCompilationException(String s) {
        super(s);
    }
    public SiddhiCompilationException(String s, Throwable throwable) {
        super(s,throwable);
    }

    public SiddhiCompilationException(Throwable throwable) {
        super(throwable);
    }


}
