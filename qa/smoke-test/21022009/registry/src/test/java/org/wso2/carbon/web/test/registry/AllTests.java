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

package org.wso2.carbon.web.test.registry;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import org.wso2.carbon.web.test.registry.BrowserInitializer;

import java.io.IOException;


public class AllTests extends TestCase {

    public static Test suite() {

        TestSuite suite = new TestSuite();

        suite.addTestSuite(RegistryTags.class);
        suite.addTestSuite(RegistryComment.class);
        suite.addTestSuite(RegistryRating.class);
        suite.addTestSuite(RegistryProperties.class);
//        suite.addTestSuite(RegistryBrowser.class);
//        suite.addTest(new RegistryBrowser("testSyanpserepoCreation"));
//        suite.addTest(new RegistryBrowser("testAddTextResource"));
//        suite.addTest(new RegistryBrowser("testAddTextResource"));
//          suite.addTest(new RegistryBrowser("testaddresource2"));

//        suite.addTest(new RegistryTags("testaddMultipleCollectionTags"));
//        suite.addTest(new RegistryTags("testaddLongResourceTags"));
//        suite.addTest(new RegistryRating("testRatingTable"));
//      suite.addTest(new RegistryComment("testaddCommentOnCollection"));
//        suite.addTest(new RegistryComment("testCommentBoxUivalidation"));
//        suite.addTest(new testAverageRating);


        TestSetup wrapper = new TestSetup(suite) {

            protected void setUp() {
                oneTimeSetUp();
            }

            private void oneTimeSetUp() {

                BrowserInitializer.initBrowser();
            }

            private void oneTimeTearDown() {
                BrowserInitializer.stopBrowser();
            }

            protected void tearDown() {
                oneTimeTearDown();
            }
        };

        return wrapper;
    }

    public static void main(String arg[]) {

        TestRunner.run(suite());

    }
}
