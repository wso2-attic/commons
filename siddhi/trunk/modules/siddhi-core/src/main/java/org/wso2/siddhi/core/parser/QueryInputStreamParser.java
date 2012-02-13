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

package org.wso2.siddhi.core.parser;

import org.wso2.siddhi.api.eventstream.query.inputstream.QueryInputStream;
import org.wso2.siddhi.core.eventstream.handler.InputStreamHandler;
import org.wso2.siddhi.core.eventstream.handler.query.SimpleQueryInputStreamHandler;
import org.wso2.siddhi.core.eventstream.handler.query.join.JoinSimpleQueryInputStreamHandler;
import org.wso2.siddhi.core.eventstream.handler.query.join.window.JoinLengthBatchWindowQueryInputStreamHandler;
import org.wso2.siddhi.core.eventstream.handler.query.join.window.JoinLengthWindowQueryInputStreamHandler;
import org.wso2.siddhi.core.eventstream.handler.query.join.window.JoinTimeBatchWindowQueryInputStreamHandler;
import org.wso2.siddhi.core.eventstream.handler.query.join.window.JoinTimeWindowQueryInputStreamHandler;
import org.wso2.siddhi.core.eventstream.handler.query.window.LengthBatchWindowQueryInputStreamHandler;
import org.wso2.siddhi.core.eventstream.handler.query.window.LengthWindowQueryInputStreamHandler;
import org.wso2.siddhi.core.eventstream.handler.query.window.TimeBatchWindowQueryInputStreamHandler;
import org.wso2.siddhi.core.eventstream.handler.query.window.TimeWindowQueryInputStreamHandler;
import org.wso2.siddhi.core.exception.InvalidQueryInputStreamException;

public class QueryInputStreamParser {

    public static InputStreamHandler parse(QueryInputStream queryInputStream)
            throws InvalidQueryInputStreamException {
        switch (queryInputStream.getWindowType()) {
            case NONE:
                return new SimpleQueryInputStreamHandler(queryInputStream);
            case TIME:
                return new TimeWindowQueryInputStreamHandler(queryInputStream);
            case TIME_BATCH:
                return new TimeBatchWindowQueryInputStreamHandler(queryInputStream);
            case LENGTH:
                return new LengthWindowQueryInputStreamHandler(queryInputStream);
            case LENGTH_BATCH:
                return new LengthBatchWindowQueryInputStreamHandler(queryInputStream);
            default:
                throw new InvalidQueryInputStreamException("Invalid window type:" + queryInputStream.getWindowType());
        }

    }

    public static InputStreamHandler parseForJoin(QueryInputStream queryInputStream)
            throws InvalidQueryInputStreamException {
        switch (queryInputStream.getWindowType()) {
            case NONE:
                return new JoinSimpleQueryInputStreamHandler(queryInputStream);
            case TIME:
                return new JoinTimeWindowQueryInputStreamHandler(queryInputStream);
            case TIME_BATCH:
                return new JoinTimeBatchWindowQueryInputStreamHandler(queryInputStream);
            case LENGTH:
                return new JoinLengthWindowQueryInputStreamHandler(queryInputStream);
            case LENGTH_BATCH:
                return new JoinLengthBatchWindowQueryInputStreamHandler(queryInputStream);
            default:
                throw new InvalidQueryInputStreamException("Invalid window type:" + queryInputStream.getWindowType());
        }

    }
}
