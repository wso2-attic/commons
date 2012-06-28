/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
// START SNIPPET: service
package demo.spring.service;

import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.context.RegistryType;
import org.wso2.carbon.registry.api.Registry;
import org.wso2.carbon.registry.api.RegistryException;
import org.wso2.carbon.registry.api.Resource;

import javax.jws.WebService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


@WebService(endpointInterface = "demo.spring.service.HelloWorld")
public class HelloWorldImpl implements HelloWorld {

    public String sayHi(String text) {
        String CONTENT = "";

        Registry registry = CarbonContext.getCurrentContext().getRegistry(RegistryType.SYSTEM_CONFIGURATION);
        Resource r1 = null;

        try {
            r1 = registry.get("/d1/"+ text);
            System.out.println(r1.getDescription());

            InputStream stream = r1.getContentStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(stream));

            StringBuilder sb = new StringBuilder();
            String content;
            while ((content = br.readLine()) != null) {
                sb.append(content);
            }
            CONTENT = sb.toString();
            System.out.println(CONTENT);


        } catch (RegistryException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Registry access executed successfully");
        return "Accessed resource was " + text + " and content was " + CONTENT;
    }
}
// END SNIPPET: service
