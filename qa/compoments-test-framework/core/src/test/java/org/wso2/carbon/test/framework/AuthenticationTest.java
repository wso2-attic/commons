
/**
 *  Copyright (c) 2009, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.wso2.carbon.test.framework;
import org.junit.Assert;
import org.junit.Test;


/**
 * Created by IntelliJ IDEA.
 * User: suho
 * Date: Jun 8, 2010
 * Time: 3:14:52 PM
 *  
 */
public class AuthenticationTest extends ComponentsTestCase {

    @Test
    public void PrintSessionCookieTestCase() {
        Assert.assertTrue(sessionCookie.contains("JSESSIONID="));
    }


}
