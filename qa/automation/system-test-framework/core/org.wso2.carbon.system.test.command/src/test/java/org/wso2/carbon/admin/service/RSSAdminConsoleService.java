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
package org.wso2.carbon.admin.service;

import junit.framework.Assert;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.util.Base64;
import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.utils.AuthenticateStub;
import org.wso2.carbon.adminconsole.ui.stub.RSSAdminRSSDAOExceptionException;
import org.wso2.carbon.adminconsole.ui.stub.RSSAdminStub;
import org.wso2.carbon.adminconsole.ui.stub.types.*;

import java.rmi.RemoteException;


public class RSSAdminConsoleService {
    private static final Log log = LogFactory.getLog(RSSAdminConsoleService.class);

    //        private final String serviceName = "AdminConsoleService";
    private final String serviceName = "RSSManagerAdminService";
    //    private ConsoleAdminStub consoleAdminStub;
    private RSSAdminStub consoleAdminStub;
    private String endPoint;

    private static final String ADMIN_CONSOLE_EXTENSION_NS = "http://www.wso2.org/products/wso2commons/adminconsole";
    private static final OMNamespace ADMIN_CONSOLE_OM_NAMESPACE = OMAbstractFactory.getOMFactory().createOMNamespace(ADMIN_CONSOLE_EXTENSION_NS, "instance");
    private static final OMFactory omFactory = OMAbstractFactory.getOMFactory();
    private static final String NULL_NAMESPACE = "";
    private static final OMNamespace NULL_OMNS = omFactory.createOMNamespace(NULL_NAMESPACE, "");

    public RSSAdminConsoleService(String backEndUrl) {
        this.endPoint = backEndUrl + serviceName;
        log.debug("Endpoint : " + endPoint);
        try {
            consoleAdminStub = new RSSAdminStub(endPoint);
        } catch (AxisFault axisFault) {
            log.error("Initializing RSSAdminStub failed : ", axisFault);
            Assert.fail("Initializing RSSAdminStub failed : " + axisFault.getMessage());
        }
    }

    public void createDatabase(String sessionCookie, String databaseName, int rssInstanceId) {
        new AuthenticateStub().authenticateStub(sessionCookie, consoleAdminStub);
        log.debug("Database Name :" + databaseName);
        log.debug("RSSInstanceId :" + rssInstanceId);
        try {
            consoleAdminStub.createDatabase(serializeDatabaseInstanceData(databaseName, String.valueOf(rssInstanceId), "0").toString());
            log.info("Database Created");
        } catch (RemoteException e) {
            log.error("RemoteException when creating database :", e);
            Assert.fail("RemoteException when creating database: " + e.getMessage());
        } catch (RSSAdminRSSDAOExceptionException e) {
            log.error("RSSAdminRSSDAOExceptionException when creating database :", e);
            Assert.fail("RSSAdminRSSDAOExceptionException when creating database: " + e.getMessage());
        }


    }

    public void dropDatabase(String sessionCookie, int databaseId) {
        new AuthenticateStub().authenticateStub(sessionCookie, consoleAdminStub);
        log.debug("Database InstanceId :" + databaseId);
        try {
            consoleAdminStub.dropDatabase(databaseId);
            log.info("Database Dropped");
        } catch (RemoteException e) {
            log.error("Remote Exception when dropping database :", e);
            Assert.fail("Remote Exception when dropping database : " + e.getMessage());
        } catch (RSSAdminRSSDAOExceptionException e) {
            log.error("RSSAdminRSSDAOExceptionException when dropping database : ", e);
            Assert.fail("RSSAdminRSSDAOExceptionException when dropping database : " + e.getMessage());
        }

    }

    public DatabaseInstanceEntry[] getDatabaseInstanceList(String sessionCookie) {
        new AuthenticateStub().authenticateStub(sessionCookie, consoleAdminStub);
        DatabaseInstanceEntry[] databaseList = null;
        try {
            databaseList = consoleAdminStub.getDatabaseInstanceList();
            log.debug("Database Instance list: " + databaseList);
            log.info("Database instance list received");

        } catch (RemoteException e) {
            log.error("RemoteException when invoking DatabaseInstanceList :", e);
            Assert.fail("RemoteException when invoking DatabaseInstanceList : " + e.getMessage());
        } catch (RSSAdminRSSDAOExceptionException e) {
            log.error("RSSAdminRSSDAOExceptionException when invoking DatabaseInstanceList : ", e);
            Assert.fail("RSSAdminRSSDAOExceptionException when invoking DatabaseInstanceList : " + e.getMessage());
        }
        Assert.assertNotNull("DatabaseInstance List is null ", databaseList);
        return databaseList;
    }

    public DatabaseInstanceEntry getDatabaseInstance(String sessionCookie, String databaseName) {
        DatabaseInstanceEntry[] databaseList = getDatabaseInstanceList(sessionCookie);
        DatabaseInstanceEntry dbInstance = null;
        Assert.assertNotNull("Database Instance List null", databaseList);
        for (DatabaseInstanceEntry dbEntry : databaseList) {
            if (dbEntry.getDbName().equals(databaseName)) {
                dbInstance = dbEntry;
                break;
            }
        }

        Assert.assertNotNull("Database Not Found :" + databaseName, dbInstance);
        return dbInstance;

    }

    public void createPrivilegeGroup(String sessionCookie, String privilegeGroupName) {
        PrivilegeGroup privilegeGroup = new PrivilegeGroup();

        privilegeGroup.setPrivGroupName(privilegeGroupName);
        privilegeGroup.setPrivs(getAllDatabasePermission());

        new AuthenticateStub().authenticateStub(sessionCookie, consoleAdminStub);
        log.debug("Privilege Group Name: " + privilegeGroupName);
        try {
            consoleAdminStub.createPrivilegeGroup(privilegeGroup);
            log.info("Privilege Group Added");
        } catch (RemoteException e) {
            log.error("RemoteException when creating privilege group : " + e.getMessage());
            Assert.fail("RemoteException when creating privilege group : " + e.getMessage());
        } catch (RSSAdminRSSDAOExceptionException e) {
            log.error("RSSAdminRSSDAOExceptionException when creating privilege group : " + e.getMessage());
            Assert.fail("RSSAdminRSSDAOExceptionException when creating privilege group : " + e.getMessage());
        }

    }

    public int getPrivilegeGroupId(String sessionCookie, String privilegeGroupName) {
        PrivilegeGroup[] privilegeGroups = getUserPrivilegeGroups(sessionCookie);
        int privilegeGroupId = -1;
        Assert.assertNotNull("No Privilege Groups Found : privilege group null ", privilegeGroups);
        log.debug("privilege group name :" + privilegeGroupName);
        for (PrivilegeGroup priGroup : privilegeGroups) {
            if (priGroup.getPrivGroupName().equals(privilegeGroupName)) {
                privilegeGroupId = priGroup.getPrivGroupId();
                log.info("Privilege group found");
                break;
            }
        }
        Assert.assertNotSame("No Privilege Group for :" + privilegeGroupName, -1, privilegeGroupId);
        return privilegeGroupId;

    }

    public void deletePrivilegeGroup(String sessionCookie, int privilegeGroupId) {
        new AuthenticateStub().authenticateStub(sessionCookie, consoleAdminStub);
        log.debug("privilege group id :" + privilegeGroupId);
        try {
            consoleAdminStub.removePrivilegeGroup(privilegeGroupId);
            log.info("privilege group removed");
        } catch (RemoteException e) {
            log.error("RemoteException when deleting privilege groups : " + e.getMessage());
            Assert.fail("RemoteException when deleting privilege groups : " + e.getMessage());
        } catch (RSSAdminRSSDAOExceptionException e) {
            log.error("RSSAdminRSSDAOExceptionException when deleting privilege groups : " + e.getMessage());
            Assert.fail("RSSAdminRSSDAOExceptionException when deleting privilege groups : " + e.getMessage());
        }
    }

    public PrivilegeGroup[] getUserPrivilegeGroups(String sessionCookie) {
        new AuthenticateStub().authenticateStub(sessionCookie, consoleAdminStub);
        PrivilegeGroup[] privilegeGroup = null;
        try {
            privilegeGroup = consoleAdminStub.getPrivilegeGroups();
        } catch (RemoteException e) {
            log.error("RemoteException while getting privilege groups : " + e.getMessage());
            Assert.fail("RemoteException while getting privilege groups : " + e.getMessage());
        } catch (RSSAdminRSSDAOExceptionException e) {
            log.error("RSSAdminRSSDAOExceptionException while getting privilege groups : " + e.getMessage());
            Assert.fail("RSSAdminRSSDAOExceptionException while getting privilege groups : " + e.getMessage());
        }
        Assert.assertNotNull("Privilege Group list null ", privilegeGroup);
        return privilegeGroup;

    }


    public DatabaseUserEntry getDatabaseUser(String sessionCookie, String userName, int databaseInstanceId) {
        DatabaseUserEntry[] databaseUsers = getUsersByDatabaseInstanceId(sessionCookie, databaseInstanceId);
        DatabaseUserEntry dbUser = null;
        Assert.assertNotNull("Database users list null ", databaseUsers);
        log.debug("User name " + userName);
        for (DatabaseUserEntry user : databaseUsers) {
            if (userName.equals(user.getUsername())) {
                dbUser = user;
                log.info("User Found on database");
                break;
            }
        }
        Assert.assertNotNull("Database users not found on database", dbUser);
        return dbUser;
    }

    public DatabaseInstanceEntry getDatabaseInstanceById(String sessionCookie, int rssInstanceId, int databaseInstanceId) {
        new AuthenticateStub().authenticateStub(sessionCookie, consoleAdminStub);
        DatabaseInstanceEntry databaseInstanceEntry = null;
        try {
            databaseInstanceEntry = consoleAdminStub.getDatabaseInstanceById(databaseInstanceId);

        } catch (RemoteException e) {
            log.error("RemoteException while getting database instance by id : " + e.getMessage());
            Assert.fail("RemoteException when getting database instance by id : " + e.getMessage());
        } catch (RSSAdminRSSDAOExceptionException e) {
            log.error("RSSAdminRSSDAOExceptionException while getting database instance by id : " + e.getMessage());
            Assert.fail("RSSAdminRSSDAOExceptionException when getting database instance by id : " + e.getMessage());
        }
        Assert.assertNotNull("Database instance null", databaseInstanceEntry);
        return databaseInstanceEntry;
    }

    public RSSInstanceEntry[] getRSSInstanceList(String sessionCookie) {
        new AuthenticateStub().authenticateStub(sessionCookie, consoleAdminStub);
        RSSInstanceEntry[] rssInstance = null;
        try {
            rssInstance = consoleAdminStub.getRSSInstanceList();
            log.info("RSS Instance found");
        } catch (RemoteException e) {
            log.error("RemoteException when getting RSSInstance : " + e.getMessage());
            Assert.fail("RemoteException when getting RSSInstance : " + e.getMessage());
        } catch (RSSAdminRSSDAOExceptionException e) {
            log.error("RSSAdminRSSDAOExceptionException when getting RSSInstance : " + e.getMessage());
            Assert.fail("RSSAdminRSSDAOExceptionException when getting RSSInstance : " + e.getMessage());
        }
        Assert.assertNotNull("RSSInstance List null", rssInstance);
        return rssInstance;
    }

    public RSSInstance getRSSInstanceById(String sessionCookie, int rssInstanceId) {
        new AuthenticateStub().authenticateStub(sessionCookie, consoleAdminStub);
        RSSInstance rssInstance = null;
        try {
            rssInstance = consoleAdminStub.getRSSInstanceDataById(rssInstanceId);
            log.info("RSS Instance found");
        } catch (RemoteException e) {
            log.error("RemoteException when getting RSSInstance : " + e.getMessage());
            Assert.fail("RemoteException when getting RSSInstance : " + e.getMessage());
        } catch (RSSAdminRSSDAOExceptionException e) {
            log.error("RSSAdminRSSDAOExceptionException when getting RSSInstance : " + e.getMessage());
            Assert.fail("RSSAdminRSSDAOExceptionException when getting RSSInstance : " + e.getMessage());
        }
        Assert.assertNotNull("RSSInstance Not Found for given RSSInstanceID", rssInstance);
        return rssInstance;
    }

    public RSSInstanceEntry getRoundRobinAssignedRSSInstance(String sessionCookie) {
        new AuthenticateStub().authenticateStub(sessionCookie, consoleAdminStub);
        RSSInstanceEntry rssInstance = null;
        try {
            rssInstance = consoleAdminStub.getRoundRobinAssignedRSSInstance();
            log.info("RSS Instance found");
        } catch (RemoteException e) {
            log.error("RemoteException when getting RoundRobinAssignedRSSInstance : " + e.getMessage());
            Assert.fail("RemoteException when getting RoundRobinAssignedRSSInstance : " + e.getMessage());
        } catch (RSSAdminRSSDAOExceptionException e) {
            log.error("RSSAdminRSSDAOExceptionException when getting RoundRobinAssignedRSSInstance : " + e.getMessage());
            Assert.fail("RSSAdminRSSDAOExceptionException when getting RoundRobinAssignedRSSInstance : " + e.getMessage());
        }
        Assert.assertNotNull("RoundRobinAssignedRSSInstance null", rssInstance);
        return rssInstance;
    }

    public void createUser(String sessionCookie, String userName, String password, int rssInstanceId, int databaseInstanceId, int privilegeGroupId) {
        new AuthenticateStub().authenticateStub(sessionCookie, consoleAdminStub);
        DatabaseUser user = new DatabaseUser();
        user.setUsername(userName);
        user.setPassword(password);
        user.setRssInstanceId(rssInstanceId);

        log.debug("userName " + userName);
        log.debug("databaseInstanceId " + databaseInstanceId);
        log.debug("privilegeGroupId " + privilegeGroupId);

        try {
            consoleAdminStub.createUser(user, privilegeGroupId, databaseInstanceId);
            log.info("User Created");

        } catch (RemoteException e) {
            log.error("RemoteException when creating user on database : ", e);
            Assert.fail("RemoteException when creating user on database : " + e.getMessage());
        } catch (RSSAdminRSSDAOExceptionException e) {
            log.error("RSSAdminRSSDAOExceptionException when creating user on database : ", e);
            Assert.fail("RSSAdminRSSDAOExceptionException when creating user on database : " + e.getMessage());
        }

    }

    public void deleteUser(String sessionCookie, int userId, int databaseInstanceId) {
        new AuthenticateStub().authenticateStub(sessionCookie, consoleAdminStub);
        log.debug("UserId " + userId);
        try {
            consoleAdminStub.dropUser(userId, databaseInstanceId);
            log.info("User Deleted");
        } catch (RemoteException e) {
            log.error("RemoteException when deleting user  on database : ", e);
            Assert.fail("RemoteException when deleting user on database : " + e.getMessage());
        } catch (RSSAdminRSSDAOExceptionException e) {
            log.error("RSSAdminRSSDAOExceptionException when deleting user on database : ", e);
            Assert.fail("RSSAdminRSSDAOExceptionException when deleting user on database : " + e.getMessage());
        }

    }

    public DatabaseUserEntry[] getUsersByDatabaseInstanceId(String sessionCookie, int databaseInstanceId) {
        new AuthenticateStub().authenticateStub(sessionCookie, consoleAdminStub);
        DatabaseUserEntry[] userList = null;
        log.debug("databaseInstanceId " + databaseInstanceId);
        try {
            userList = consoleAdminStub.getUsersByDatabaseInstanceId(databaseInstanceId);
            log.info("User List received");
        } catch (RemoteException e) {
            log.error("RemoteException when getting users on database : ", e);
            Assert.fail("RemoteException when getting users on database : " + e.getMessage());
        } catch (RSSAdminRSSDAOExceptionException e) {
            log.error("RSSAdminRSSDAOExceptionException when getting users on database  : ", e);
            Assert.fail("RSSAdminRSSDAOExceptionException when getting users on database : " + e.getMessage());
        }
        Assert.assertNotNull("User list is null", userList);
        return userList;
    }

    public String createCarbonDSFromDatabaseUserEntry(String sessionCookie, int databaseInstanceId, int dbUserId) {
        new AuthenticateStub().authenticateStub(sessionCookie, consoleAdminStub);
        String carbonDataSource = null;
        log.debug("databaseInstanceId " + databaseInstanceId);
        try {
            carbonDataSource = consoleAdminStub.createCarbonDSFromDatabaseUserEntry(databaseInstanceId, dbUserId);
            log.debug(carbonDataSource);
            Assert.assertNotNull("carbon data source create response message null", carbonDataSource);
            carbonDataSource = carbonDataSource.substring((carbonDataSource.indexOf(" '") + 2), carbonDataSource.indexOf("' "));
            log.debug("Data Source Name : " + carbonDataSource);
            log.info("Data Source Created");
        } catch (RemoteException e) {
            log.error("RemoteException when creating CarbonDSFromDatabaseUserEntry  : ", e);
            Assert.fail("RemoteException when creating CarbonDSFromDatabaseUserEntry : " + e.getMessage());
        } catch (RSSAdminRSSDAOExceptionException e) {
            log.error("RSSAdminRSSDAOExceptionException when creating CarbonDSFromDatabaseUserEntry  : ", e);
            Assert.fail("RSSAdminRSSDAOExceptionException when creating CarbonDSFromDatabaseUserEntry : " + e.getMessage());
        }
        Assert.assertNotNull("carbon data source not created", carbonDataSource);
        Assert.assertNotSame("Data source name empty in response message", "", carbonDataSource.trim());
        return carbonDataSource;
    }

    private static OMElement serializeDatabaseInstanceData(String dbName, String rssInstId, String dbInstId) {
        OMElement dbEl = omFactory.createOMElement("db", ADMIN_CONSOLE_OM_NAMESPACE);

        if (!"".equals(rssInstId) && rssInstId != null) {
            dbEl.addAttribute("rssInsId", rssInstId, NULL_OMNS);
        }

        if (!"".equals(dbName) && dbName != null) {
            dbEl.addAttribute("name", dbName, NULL_OMNS);
        }

        if (!"".equals(dbInstId) && dbInstId != null) {
            dbEl.addAttribute("dbInsId", dbInstId, NULL_OMNS);
        }
        log.debug(dbEl);
        return dbEl;
    }

    private DatabasePrivilege[] getAllDatabasePermission() {

        DatabasePrivilege[] databasePrivilegeList = new DatabasePrivilege[19];

        databasePrivilegeList[0] = new DatabasePrivilege();
        databasePrivilegeList[0].setPrivName("Alter_priv");
        databasePrivilegeList[0].setPrivValue("Y");

        databasePrivilegeList[1] = new DatabasePrivilege();
        databasePrivilegeList[1].setPrivName("Alter_routine_priv");
        databasePrivilegeList[1].setPrivValue("Y");

        databasePrivilegeList[2] = new DatabasePrivilege();
        databasePrivilegeList[2].setPrivName("Create_priv");
        databasePrivilegeList[2].setPrivValue("Y");

        databasePrivilegeList[3] = new DatabasePrivilege();
        databasePrivilegeList[3].setPrivName("Create_routine_priv");
        databasePrivilegeList[3].setPrivValue("Y");

        databasePrivilegeList[4] = new DatabasePrivilege();
        databasePrivilegeList[4].setPrivName("Create_tmp_table_priv");
        databasePrivilegeList[4].setPrivValue("Y");

        databasePrivilegeList[5] = new DatabasePrivilege();
        databasePrivilegeList[5].setPrivName("Create_view_priv");
        databasePrivilegeList[5].setPrivValue("Y");

        databasePrivilegeList[6] = new DatabasePrivilege();
        databasePrivilegeList[6].setPrivName("Delete_priv");
        databasePrivilegeList[6].setPrivValue("Y");

        databasePrivilegeList[7] = new DatabasePrivilege();
        databasePrivilegeList[7].setPrivName("Drop_priv");
        databasePrivilegeList[7].setPrivValue("Y");

        databasePrivilegeList[8] = new DatabasePrivilege();
        databasePrivilegeList[8].setPrivName("Event_priv");
        databasePrivilegeList[8].setPrivValue("Y");

        databasePrivilegeList[9] = new DatabasePrivilege();
        databasePrivilegeList[9].setPrivName("Execute_priv");
        databasePrivilegeList[9].setPrivValue("Y");

        databasePrivilegeList[10] = new DatabasePrivilege();
        databasePrivilegeList[10].setPrivName("Grant_priv");
        databasePrivilegeList[10].setPrivValue("Y");

        databasePrivilegeList[11] = new DatabasePrivilege();
        databasePrivilegeList[11].setPrivName("Index_priv");
        databasePrivilegeList[11].setPrivValue("Y");

        databasePrivilegeList[12] = new DatabasePrivilege();
        databasePrivilegeList[12].setPrivName("Insert_priv");
        databasePrivilegeList[12].setPrivValue("Y");

        databasePrivilegeList[13] = new DatabasePrivilege();
        databasePrivilegeList[13].setPrivName("Lock_tables_priv");
        databasePrivilegeList[13].setPrivValue("Y");

        databasePrivilegeList[14] = new DatabasePrivilege();
        databasePrivilegeList[14].setPrivName("References_priv");
        databasePrivilegeList[14].setPrivValue("Y");

        databasePrivilegeList[15] = new DatabasePrivilege();
        databasePrivilegeList[15].setPrivName("Select_priv");
        databasePrivilegeList[15].setPrivValue("Y");

        databasePrivilegeList[16] = new DatabasePrivilege();
        databasePrivilegeList[16].setPrivName("Show_view_priv");
        databasePrivilegeList[16].setPrivValue("Y");

        databasePrivilegeList[17] = new DatabasePrivilege();
        databasePrivilegeList[17].setPrivName("Trigger_priv");
        databasePrivilegeList[17].setPrivValue("Y");

        databasePrivilegeList[18] = new DatabasePrivilege();
        databasePrivilegeList[18].setPrivName("Update_priv");
        databasePrivilegeList[18].setPrivValue("Y");

        return databasePrivilegeList;

    }

    public String getFullyQualifiedUsername(String username, String tenantDomain) {
        if (tenantDomain != null) {

            /* The maximum number of characters allowed for the username in mysql system tables is
             * 16. Thus, to adhere the aforementioned constraint as well as to give the username
             * an unique identification based on the tenant domain, we append a hash value that is
             * created based on the tenant domain */
            byte[] bytes = intToByteArray(tenantDomain.hashCode());
            return username + "_" + Base64.encode(bytes);
        }
        return username;
    }

    private static byte[] intToByteArray(int value) {
        byte[] b = new byte[6];
        for (int i = 0; i < 6; i++) {
            int offset = (b.length - 1 - i) * 8;
            b[i] = (byte) ((value >>> offset) & 0xFF);
        }
        return b;
    }
}
