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
package org.wso2.siddhi.query.api.stream.sequence.element;

import org.wso2.siddhi.query.api.stream.SingleStream;

public class OrElement implements SequenceElement {
    SingleStream singleStream1;
    SingleStream singleStream2;

    public OrElement(SingleStream singleStream1,
                     SingleStream singleStream2) {
        this.singleStream1 = singleStream1;
        this.singleStream2 = singleStream2;
    }

    public SingleStream getSingleStream1() {
        return singleStream1;
    }

    public SingleStream getSingleStream2() {
        return singleStream2;
    }
}
