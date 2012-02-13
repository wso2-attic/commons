/*
*Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*WSO2 Inc. licenses this file to you under the Apache License,
*Version 2.0 (the "License"); you may not use this file except
*in compliance with the License.
*You may obtain a copy of the License at
*
*http://www.apache.org/licenses/LICENSE-2.0
*
*Unless required by applicable law or agreed to in writing,
*software distributed under the License is distributed on an
*"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*KIND, either express or implied.  See the License for the
*specific language governing permissions and limitations
*under the License.
*/
package org.wso2.carbon.system.test.core.utils.seleniumUtils;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.wso2.carbon.admin.service.utils.ProductConstant;

import java.io.File;
import java.io.IOException;


public class SeleniumScreenCapture {

    public void getScreenshot(WebDriver driver,String directoryName, String testCaseID) {
        String resourcePath = ProductConstant.SYSTEM_TEST_RESOURCE_LOCATION;
        String imagePath = resourcePath +File.separator+"seleniumImageFiles"+File.separator+directoryName+File.separator+testCaseID+".png";
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(scrFile, new File(imagePath));

        } catch (IOException e) {

        }

    }
}
