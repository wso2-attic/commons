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
import org.wso2.carbon.user.api.UserRealm;
import org.wso2.carbon.user.api.UserStoreException;
import org.wso2.carbon.user.api.UserStoreManager;

import javax.jws.WebService;
import java.util.HashMap;
import java.util.Map;


@WebService(endpointInterface = "demo.spring.service.HelloWorld")
public class HelloWorldImpl implements HelloWorld {

    public String sayHi(String text) {

        UserRealm realm =  CarbonContext.getCurrentContext().getUserRealm();
        try {
            UserStoreManager storeManager = realm.getUserStoreManager();
            if(!storeManager.isExistingRole("myapp")) {
                storeManager.addRole("myapp", null, null);
                System.out.println("Role added to the system");
            } else {
                System.out.println("Role already there in the system");
            }
            if (!storeManager.isExistingUser("chamara")) {
                Map<String, String> claims = new HashMap<String, String>();
                storeManager.addUser("chamara","cham123",new String[] {"myapp", "admin"},claims, null);
                System.out.println("User added successfully");
            } else {
                System.out.println("User already in the system");
            }
        } catch (UserStoreException e) {
            e.printStackTrace();
        }
        return "UM accessed Successfully!";
    }
}
// END SNIPPET: service
