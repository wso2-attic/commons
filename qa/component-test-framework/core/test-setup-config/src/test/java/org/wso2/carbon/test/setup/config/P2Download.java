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

package org.wso2.carbon.test.setup.config;

import org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

class P2Download {

/*    This class can be used any module which needs to download components from P2 Repository.
    destination - eg: /home/wso2esb-3.0.1/repository/components/dropins
    resource - resource name eg:jruby-complete-1.3.0.wso2v1.jar*/

    public void getFile(String destination, String resource) {

        FrameworkSettings.getProperty();
        try {

            /*Get a connection to the URL and start up a buffered reader*/
            long startTime = System.currentTimeMillis();

            System.out.println("Connecting......\n");

            URL url = new URL(FrameworkSettings.P2_REPO + "plugins" + File.separator);
            url.openConnection();
            InputStream reader = url.openStream();
            System.out.println(url);

            /*Setup a buffered file writer to write-out what we read from the online repo*/
            // eg:  FileOutputStream writer = new FileOutputStream(FrameworkSettings.CARBON_HOME + File.separator + "repository" + File.separator + "components" + File.separator + "dropins" + File.separator + "jruby-complete-1.3.0.wso2v1.jar");
            FileOutputStream writer = new FileOutputStream(destination + File.separator + resource);

            byte[] buffer = new byte[153600];
            int totalBytesRead = 0;
            int bytesRead = 0;

            System.out.println("Reading jar file 150KB blocks at a time.\n");

            while ((bytesRead = reader.read(buffer)) > 0) {
                writer.write(buffer, 0, bytesRead);
                buffer = new byte[153600];
                totalBytesRead += bytesRead;
            }

            long endTime = System.currentTimeMillis();

            System.out.println("Done. " + (new Integer(totalBytesRead).toString()) + " bytes read (" + (new Long(endTime - startTime).toString()) + " millseconds).\n");
            writer.close();
            reader.close();
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

}