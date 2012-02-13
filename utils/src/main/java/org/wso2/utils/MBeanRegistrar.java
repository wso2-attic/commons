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
package org.wso2.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.util.Set;

/**
 * The class which is responsible for registering MBeans
 */
public class MBeanRegistrar {
    private static Log log = LogFactory.getLog(MBeanRegistrar.class);

    public static void registerMBean(Object mbeanInstance,
                                     String objectName) throws Exception {

        MBeanServer mbs = ManagementFactory.getMBeanServer();
        Set set = mbs.queryNames(new ObjectName(objectName), null);
        if (set.isEmpty()) {
            mbs.registerMBean(mbeanInstance, new ObjectName(objectName));
        } else {
            throw new Exception("MBean " + objectName + " already exists");
        }
    }

    public static void registerMBean(Object mbeanInstance) {
        String jmxPort = ServerConfiguration.getInstance().getFirstProperty("Ports.JMX");
        String serverPackage = ServerConfiguration.getInstance().getFirstProperty("Package");
        if (serverPackage == null) {
            serverPackage = "wso2";
        }
        if (jmxPort != null) {
            try {
                String className = mbeanInstance.getClass().getName();
                if(className.indexOf(".") != -1){
                    className = className.substring(className.lastIndexOf(".") + 1);
                }
                MBeanRegistrar.
                        registerMBean(mbeanInstance, serverPackage + ":type=" + className);
            } catch (Exception e) {
                String msg = "Could not register " + mbeanInstance.getClass() + " MBean";
                log.error(msg, e);
                throw new RuntimeException(msg, e);
            }
        }
    }
}
