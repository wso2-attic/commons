/*
* Copyright 2005,2006 WSO2, Inc. http://www.wso2.org
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
*
*
*/
package org.wso2.statistics.module;

import org.apache.axis2.context.MessageContext;

/**
 * A utility class to compute response times.
 */
public class ResponseTimeProcessor {
    private long maxResponseTime = 0;
    private long minResponseTime = -1;
    private double avgResponseTime = 0;
    private double totalresponseTime;

    public synchronized void addResponseTime(long responseTime,
                                             long requestCount,
                                             MessageContext msgctx) {
        if (maxResponseTime < responseTime) {
            maxResponseTime = responseTime;
        }

        if (minResponseTime > responseTime) {
            minResponseTime = responseTime;
        }

        if (minResponseTime == -1) {
            minResponseTime = responseTime;
        }

        totalresponseTime = totalresponseTime + responseTime;
        avgResponseTime = totalresponseTime / requestCount;
    }

    public long getMaxResponseTime() {
        return maxResponseTime;
    }

    public double getAvgResponseTime() {
        return avgResponseTime;
    }

    public long getMinResponseTime() {
        return minResponseTime;
    }
}
