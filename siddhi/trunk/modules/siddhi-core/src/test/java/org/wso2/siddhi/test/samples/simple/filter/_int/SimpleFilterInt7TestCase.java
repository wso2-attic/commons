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
package org.wso2.siddhi.test.samples.simple.filter._int;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.wso2.siddhi.api.QueryFactory;
import org.wso2.siddhi.api.condition.where.SimpleCondition;
import org.wso2.siddhi.core.event.Event;

import static org.wso2.siddhi.api.condition.where.ConditionOperator.EQUAL;


public class SimpleFilterInt7TestCase extends SimpleFilterBaseTestCase {

    private final Logger log = Logger.getLogger(this.getClass());

    protected SimpleCondition createAPICondition(QueryFactory qf) {
        return qf.condition("2*3", EQUAL, "CSEStream.price");
    }

    @Override
    protected String createQueryCondition() {
        return "2*3 == price" ;
    }

    protected void assertEvent(Event event) {
        Assert.assertEquals(event.getNthAttribute(0), 6);
    }

}
