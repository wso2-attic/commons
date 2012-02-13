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
package org.wso2.utils.transport.http;


import org.mortbay.jetty.Server;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *  
 */
public final class HttpServerManager {
    private static Map serverMap = new HashMap();

    public static Server getServer(String serverKey) {
        Server server;
        Object obj = serverMap.get(serverKey);
        if (obj == null) {
            server = new Server();
            serverMap.put(serverKey, server);
        } else {
            server = (Server) obj;
        }
        return server;
    }

    public static void startServer(String serverKey) throws Exception {
        Server server = getServer(serverKey);
        if (!server.isStarted()) {
            server.start();
        }
    }

    public static void stopServer(String serverKey) throws Exception {
        Server server = getServer(serverKey);
        server.stop();
        serverMap.remove(serverKey);
    }

    public static void stopAllServers() throws Exception {
        for(Iterator iter = serverMap.values().iterator(); iter.hasNext();){
            Server httpServer = (Server) iter.next();
            httpServer.stop();
        }
        serverMap.clear();
    }
}
