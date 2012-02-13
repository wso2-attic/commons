/*
*  Copyright (c) WSO2 Inc. (http://wso2.com) All Rights Reserved.

  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing,
*  software distributed under the License is distributed on an
*  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*  KIND, either express or implied.  See the License for the
*  specific language governing permissions and limitations
*  under the License.
*
*/

package org.wso2.carbon.usermanagement.test;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.wso2.carbon.common.test.utils.FrameworkSettings;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Read user-mgt.xml and retrieve user store cofiguration param 
 */
public class UserConfigInit extends UserTestInit {

    UserConfigInit() {

        OMElement realmElem = null;
        try {
            realmElem = getRealmElement();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        OMElement mainConfig = realmElem.getFirstChildWithName(new QName(
                UserConstants.LOCAL_NAME_CONFIGURATION));
        OMElement adminUser = mainConfig.getFirstChildWithName(new QName(
                UserConstants.LOCAL_NAME_ADMIN_USER));
        adminUserName = adminUser.getFirstChildWithName(
                new QName(UserConstants.LOCAL_NAME_USER_NAME)).getText();
        adminPassword = adminUser.getFirstChildWithName(
                new QName(UserConstants.LOCAL_NAME_PASSWORD)).getText();
        adminRoleName = mainConfig.getFirstChildWithName(
                new QName(UserConstants.LOCAL_NAME_ADMIN_ROLE)).getText();
        everyOneRoleName = mainConfig.getFirstChildWithName(
                new QName(UserConstants.LOCAL_NAME_EVERYONE_ROLE)).getText();

        OMElement usaConfig = realmElem.getFirstChildWithName(new QName(
                UserConstants.LOCAL_NAME_USER_STORE_MANAGER));
        userStoreClass = usaConfig.getAttributeValue(new QName(
                UserConstants.ATTR_NAME_CLASS));
        userStoreProperties = getChildPropertyElements(usaConfig);
    }

    private OMElement getRealmElement() throws IOException {
        InputStream inStream = null;
        StAXOMBuilder builder = null;
        FrameworkSettings.getProperty();
        File profileConfigXml = new File(FrameworkSettings.USER_MGT_XML);
        if (profileConfigXml.exists()) {
            inStream = new FileInputStream(profileConfigXml);
        }
        try {
            builder = new StAXOMBuilder(inStream);
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
        OMElement documentElement = builder.getDocumentElement();
        OMElement realmElement = documentElement.getFirstChildWithName(new QName(
                UserConstants.LOCAL_NAME_REALM));
        return realmElement;
    }

    private Map<String, String> getChildPropertyElements(OMElement omElement) {
        Map<String, String> map = new HashMap<String, String>();
        Iterator<?> ite = omElement.getChildrenWithName(new QName(
                UserConstants.LOCAL_NAME_PROPERTY));
        while (ite.hasNext()) {
            OMElement propElem = (OMElement) ite.next();
            String propName = propElem.getAttributeValue(new QName(
                    UserConstants.ATTR_NAME_PROP_NAME));
            String propValue = propElem.getText();
            map.put(propName, propValue);
        }
        return map;
    }

}