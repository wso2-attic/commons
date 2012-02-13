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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: suho
 * Date: Jun 23, 2010
 * Time: 12:02:50 PM
 * This reads the .properties file
 */
public abstract class initTestCase {
    private static final Log log = LogFactory.getLog(ComponentsTestCase.class);
    public static String ESB_HOME;// = "/home/suho/esb/wso2esb-3.0.0";
    public static String AXIS2S_HOME;//= ESB_HOME + "/samples/axis2Server";
    public static String AXIS2C_HOME;//= ESB_HOME + "/samples/axis2Client";
    public static String AXIS2S_XML;//= ESB_HOME + "/samples/axis2Server/repository/conf/axis2.xml";
    public static String HOST_NAME;
    public static String HTTPS_PORT;
    public static String HTTP_PORT;


    public initTestCase() {

        Properties prop = new Properties();
        File filePath = new File("./../");
        try {
            String canonicalPath = filePath.getCanonicalPath();
            File findFile = new File(canonicalPath + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "framework.properties");
            if (!findFile.isFile()) {
                filePath = new File("./");
                canonicalPath = filePath.getCanonicalPath();
            }
            log.debug("Home:" + canonicalPath);
            FileInputStream fReader = new FileInputStream(canonicalPath + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "framework.properties");
            prop.load(fReader);
            fReader.close();

            ESB_HOME = (prop.getProperty("esb.home"));
            log.debug(ESB_HOME);
            AXIS2S_HOME = ((prop.getProperty("axis2s.home")).replaceAll("\\$\\{esb.home\\}", ESB_HOME));
            AXIS2C_HOME = (prop.getProperty("axis2c.home").replaceAll("\\$\\{esb.home\\}", ESB_HOME));
            AXIS2S_XML = (prop.getProperty("axis2s.xml").replaceAll("\\$\\{esb.home\\}", ESB_HOME));
            HOST_NAME = (prop.getProperty("host.name"));
            HTTPS_PORT = (prop.getProperty("https.port"));
            HTTP_PORT = (prop.getProperty("http.port"));

        } catch (IOException e) {

            e.printStackTrace();
        }


    }

}
