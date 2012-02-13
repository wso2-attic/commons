/*
*  Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/

package org.wso2.carbon.localentry.test.commands;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.localentry.ui.types.ConfigurationObject;
import org.wso2.carbon.localentry.ui.types.EntryData;
import org.wso2.carbon.localentry.ui.types.LocalEntryAdminException;
import org.wso2.carbon.localentry.ui.types.LocalEntryAdminServiceStub;

import java.rmi.RemoteException;

public class LocalEntryCommand extends TestCase {
    private static final Log log = LogFactory.getLog(LocalEntryCommand.class);
    LocalEntryAdminServiceStub localEntryAdminServiceStub;

    public LocalEntryCommand(LocalEntryAdminServiceStub localEntryAdminServiceStub) {
        this.localEntryAdminServiceStub = localEntryAdminServiceStub;
        log.debug("LocalEntryAdminStub added");

    }

    public boolean addEntrySuccessCase(String data)
            throws RemoteException, LocalEntryAdminException {
        boolean status = false;
        try {
            status = localEntryAdminServiceStub.addEntry(data);

        } catch (RemoteException e) {
            log.error("Invalid Source URL " + e);
            Assert.fail("Invalid Source URL " + e);
        }

        catch (Exception e) {
            log.error("addEntry operation failed " + e);
            Assert.fail("addEntry operation failed " + e);
        }
        return status;
    }

    public boolean addEntryFailureCase(String data)
            throws RemoteException, LocalEntryAdminException {
        boolean status = false;
        try {

            status = localEntryAdminServiceStub.addEntry(data);
            log.error("addEntry operation succeeded without authentication");
            Assert.fail("addEntry operation succeeded without authentication");

        }
        catch (Exception e) {

        }
        return status;
    }

    public boolean deleteEntryKeySuccessCase(String entryKey) throws Exception {
        boolean status = false;
        try {
            status = localEntryAdminServiceStub.deleteEntry(entryKey);
        }
        catch (Exception e) {
            log.error("delete entry operation failed " + e);
            Assert.fail("delete entry operation failed " + e);
        }
        return status;
    }

    public boolean deleteEntryKeyFailureCase(String entryKey) throws Exception {
        boolean status = false;
        try {
            status = localEntryAdminServiceStub.deleteEntry(entryKey);
            log.error("delete entry operation invoked without authentication");
            Assert.fail("delete entry operation invoked without authentication.");
        }
        catch (Exception e) {
        }
        return status;
    }

    public ConfigurationObject[] getDependantSuccessCase(String entryName) throws Exception {
        ConfigurationObject[] configurationObject = null;
        try {
            configurationObject = localEntryAdminServiceStub.getDependents(entryName);
        }
        catch (Exception e) {
            log.error("Get dependant operation failed " + e);
            Assert.fail("Get dependant operation failed " + e);
        }
        return configurationObject;

    }

    public ConfigurationObject[] getDependantFailureCase(String entryName) throws Exception {
        ConfigurationObject[] configurationObject = null;
        try {
            configurationObject = localEntryAdminServiceStub.getDependents(entryName);
            log.error("Get dependant operation invoked without authentication");
            Assert.fail("Get dependant operation invoked without authentication.");
        }
        catch (Exception e) {
        }
        return configurationObject;

    }

    public void getEntrySuccessCase(String entryKey) throws Exception {
        try {
            localEntryAdminServiceStub.getEntry(entryKey);
        }
        catch (Exception e) {
            log.error("Get entry key operation failed " + e);
            Assert.fail("Get entry key operation failed" + e);
        }

    }

    public void getEntryFailureCase(String entryKey) throws Exception {
        try {
            localEntryAdminServiceStub.getEntry(entryKey);
            log.error("Get entry operation invoked without authentication");
            Assert.fail("Get entry operation invoked without authentication.");
        }
        catch (Exception e) {
        }

    }

    public String[] getEntryNameSuccessCase() throws Exception {
        String[] entryName = null;
        try {
            entryName = localEntryAdminServiceStub.getEntryNames();
        }
        catch (Exception e) {
            log.error("Getting entry name failed " + e);
            Assert.fail("Getting entry name failed " + e);
        }
        return entryName;
    }

    public void getEntryNameFailureCase() throws Exception {
        String[] entryName = null;
        try {
            entryName = localEntryAdminServiceStub.getEntryNames();
            log.error("Getting entry without authentication");
            Assert.fail("Getting entry without authentication");
        }
        catch (Exception e) {
        }
    }

    public int getEntryDataCountSuccessCase() throws Exception {
        int dataEntryCount = 0;
        try {
            dataEntryCount = localEntryAdminServiceStub.getEntryDataCount();
        }
        catch (Exception e) {
            log.error("Getting entry count failed " + e);
            Assert.fail("Getting entry count failed " + e);
        }
        return dataEntryCount;
    }

    public void getEntryDataCountFailureCase() throws Exception {
        int dataEntryCount = 0;
        try {
            dataEntryCount = localEntryAdminServiceStub.getEntryDataCount();
            log.error("Getting entry count  without authentication");
            Assert.fail("Getting entry count without authentication");
        }
        catch (Exception e) {
        }
    }

    public String getEntryNamesStringSuccessCase() throws Exception {
        String NameString = null;
        try {
            NameString = localEntryAdminServiceStub.getEntryNamesString();
        }
        catch (Exception e) {
            log.error("Getting entry name failed " + e);
            Assert.fail("Getting entry name failed " + e);
        }
        return NameString;
    }

    public void getEntryNamesStringFailureCase() throws Exception {
        try {
            String NameString = localEntryAdminServiceStub.getEntryNamesString();
            log.error("Getting entry name  without authentication");
            Assert.fail("Getting entry name without authentication");
        }
        catch (Exception e) {
        }
    }

    public EntryData[] paginateEntryDataSuccessCase(int pageNumber) throws Exception {
        EntryData[] entryData = null;
        try {
            entryData = localEntryAdminServiceStub.paginatedEntryData(pageNumber);
        }
        catch (Exception e) {
            log.error("Getting paginated page entry data failed " + e);
            Assert.fail("Getting paginated page entry data failed " + e);
        }
        return entryData;
    }

    public void paginateEntryDataFailureCase(int pageNumber) throws Exception {
        try {
            EntryData[] entryData = localEntryAdminServiceStub.paginatedEntryData(pageNumber);
            log.error("Getting paginated page entry data  without authentication");
            Assert.fail("Getting paginated page entry data without authentication");
        }
        catch (Exception e) {
        }
    }

    public void saveEntryStringSuccessCase(String entryElement) throws Exception {
        try {
            localEntryAdminServiceStub.saveEntry(entryElement);
        }
        catch (Exception e) {
            log.error("Getting entry name failed " + e);
            Assert.fail("Getting entry name failed " + e);
        }
    }

    public void saveEntryStringFailureCase() throws Exception {
        try {
            String NameString = localEntryAdminServiceStub.getEntryNamesString();
            log.error("Getting entry name  without authentication");
            Assert.fail("Getting entry name without authentication");
        }
        catch (Exception e) {
        }
    }
}
