package org.wso2.carbon.web.test.is;

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;

/*
 * Copyright 2004,2005 The Apache Software Foundation.
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
 */

public class CommonSetup extends TestCase {

    static Selenium selenium;

    public CommonSetup(String text) {
        super(text);
    }

    public void setUp() throws Exception {
        selenium = BrowserInitializer.getbrowser();
    }
}
