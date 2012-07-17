/*
*  Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/
package org.wso2.siddhi.query.api.query;

import org.wso2.siddhi.query.api.ExecutionPlan;
import org.wso2.siddhi.query.api.query.projection.Projector;
import org.wso2.siddhi.query.api.stream.AnonymousStream;
import org.wso2.siddhi.query.api.stream.SingleStream;
import org.wso2.siddhi.query.api.stream.Stream;

public class Query implements ExecutionPlan {

    private Stream inputStream;
    private String outputStreamId;
    private Projector projector=new Projector();

    public Query from(Stream stream) {
        this.inputStream = stream;
        return this;
    }

    public Query insertInto(String outputStreamId) {
        this.outputStreamId = outputStreamId;
        return this;
    }

    public Query project(Projector projector) {
        this.projector = projector;
        return this;
    }


    public SingleStream returnStream() {
       return new AnonymousStream(this);
    }

    public Stream getInputStream() {
        return inputStream;
    }

    public String getOutputStreamId() {
        return outputStreamId;
    }

    public Projector getProjector() {
        return projector;
    }
}
