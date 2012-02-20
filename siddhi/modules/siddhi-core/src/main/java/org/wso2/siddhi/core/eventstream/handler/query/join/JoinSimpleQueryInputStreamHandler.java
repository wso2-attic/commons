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

package org.wso2.siddhi.core.eventstream.handler.query.join;

import org.wso2.siddhi.api.eventstream.query.inputstream.QueryInputStream;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.eventstream.handler.query.SimpleQueryInputStreamHandler;

public class JoinSimpleQueryInputStreamHandler extends SimpleQueryInputStreamHandler {


    public JoinSimpleQueryInputStreamHandler(QueryInputStream queryInputStream) {
        super(queryInputStream);
    }

    @Override
    protected void assignTimeStamp(Event event) {
        event.setTimeStamp(System.currentTimeMillis());
    }
}
