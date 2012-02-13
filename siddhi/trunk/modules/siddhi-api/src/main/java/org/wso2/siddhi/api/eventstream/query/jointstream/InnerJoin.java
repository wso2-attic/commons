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

package org.wso2.siddhi.api.eventstream.query.jointstream;

import org.wso2.siddhi.api.eventstream.query.inputstream.QueryInputStream;

/**
 * 
 * InnerJoin class handles the InnerJoin operation of EventStreams
 */

public class InnerJoin implements Join {

    private QueryInputStream queryLeftInputStream;
    private QueryInputStream queryRightInputStream;

    /**
     * @param queryLeftInputStream  One of the event streams to be joined
     * @param queryRightInputStream One of the event streams to be joined
     */
    public InnerJoin(QueryInputStream queryLeftInputStream, QueryInputStream queryRightInputStream) {
        this.queryLeftInputStream = queryLeftInputStream;
        this.queryRightInputStream = queryRightInputStream;
    }

    /**
     * get the left event stream
     *
     * @return the left event stream
     */
    public QueryInputStream getQueryLeftInputStream() {
        return queryLeftInputStream;
    }

    /**
     * get the right event stream
     *
     * @return the right event stream
     */
    public QueryInputStream getQueryRightInputStream() {
        return queryRightInputStream;
    }
}
