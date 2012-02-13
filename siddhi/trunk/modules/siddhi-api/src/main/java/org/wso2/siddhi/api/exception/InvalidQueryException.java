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
 * InvalidQueryException throws when incorrect query is defined
 */
public class InvalidQueryException extends RuntimeException {


    public InvalidQueryException() {
        super();
    }

    public InvalidQueryException(String s) {
        super(s);
    }

    public InvalidQueryException(String s, Throwable throwable) {
        super(s,throwable);
    }

    public InvalidQueryException(Throwable throwable) {
        super(throwable);
    }

}
