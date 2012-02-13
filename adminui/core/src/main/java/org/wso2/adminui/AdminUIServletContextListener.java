/*
 * Copyright 2006,2007 WSO2, Inc. http://www.wso2.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wso2.adminui;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;

public class AdminUIServletContextListener implements ServletContextListener {

    public static String contextPath = "/";

    public void contextInitialized(ServletContextEvent event) {
        ServletContext context = event.getServletContext();
        String realPath = context.getRealPath("/");
        realPath = realPath.substring(0, realPath.length()-1);
        int index = realPath.lastIndexOf(File.separator);
        String contextPathString = realPath.substring(index+1);
        if (!"ROOT".equals(contextPathString)) {
            contextPath += contextPathString;
        }
    }

    public void contextDestroyed(ServletContextEvent event) {
    }
}