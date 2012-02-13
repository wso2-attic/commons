package org.wso2.registry.secure;

import org.wso2.registry.exceptions.RegistryException;
import org.wso2.registry.*;
import org.wso2.registry.jdbc.realm.RegistryRealm;
import org.wso2.registry.jdbc.EmbeddedRegistry;
import org.wso2.registry.session.UserRegistry;
import org.wso2.registry.users.UserRealm;
import org.wso2.registry.users.UserStoreException;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class HorizontalDataPopulate extends PerfTestingSetup{

    UserRegistry adminRegistry = null;
    private static RegistryRealm realm = null;
    DataSource dataSource = null;
    RegistryRealm registryRealm = null;
    EmbeddedRegistry registry = null;

    int maxUserCount = 100;
    int maxRoleCount = 10;
    int maxFileToputCollections = 100;
    /* maxFileToputResource must be less than maxResourceCount*/
    long maxResourceCount = 100;
    long maxFileToputResource = 100;
    long loopIterator = 1000;
    long numberofLevels = 5;
    long maxChildPerNodes = maxResourceCount/numberofLevels;
    long maxChildPerNodesTesting =  maxFileToputResource/numberofLevels;

    public HorizontalDataPopulate(String text) {
      super(text);
    }



    public void testHorizontalDatapopulate() throws Exception {

        long maxChildPerNodes = maxResourceCount/numberofLevels;
        HorizaontalPathGenerator obj = new HorizaontalPathGenerator(numberofLevels, maxChildPerNodes, "/c" ,maxResourceCount, "r1.txt");

         //obj.generateTree(5,100, registry,"c" , 500);

        for (int j=0;j<maxResourceCount;j++)
        {
            String resourcePathNew = obj.getPath();
            //System.out.println(resourcePathNew);

            try {
                Resource r1 = adminRegistry.newResource();
                r1.setDescription("This is a file to test performance");
                String fileContent = "this is the file content";

                for (int filec=1;filec<=12;filec++){
                    fileContent += fileContent;
                }

                byte[] r1content = (fileContent).getBytes();
                r1.setContent(r1content);
                r1.setMediaType("text/plain");

                Comment c1 = new Comment();
                c1.setResourcePath(resourcePathNew);
                c1.setText("This is a test comment1");

                Comment c2 = new Comment();
                c2.setResourcePath(resourcePathNew);
                c2.setText("This is a test comment2");

                r1.setProperty("key1", "value1");
                r1.setProperty("key2", "value2");
                r1.setProperty("key3", "value3");
                r1.setProperty("key4", "value4");

                adminRegistry.put(resourcePathNew,r1);
                adminRegistry.addComment(resourcePathNew,c1);
                adminRegistry.addComment(resourcePathNew,c2);
                adminRegistry.applyTag(resourcePathNew, "tag1");
                adminRegistry.applyTag(resourcePathNew, "tag2");
                adminRegistry.applyTag(resourcePathNew, "tag3");
                adminRegistry.rateResource(resourcePathNew, 4);

            }catch (RegistryException e) {
                e.printStackTrace();
            }
        }
        System.out.println("End of creating"+" "+maxResourceCount+" Resources Horizontally");
    }

    public void testpopulateUserBasedAuthorization() throws RegistryException {

        UserRealm adminRealm = adminRegistry.getUserRealm();
        String user;
        String newRoleName = "registryTeam";

        /*add registry-team role*/
        try{
            adminRealm.getUserStoreAdmin().addRole(newRoleName);
        }catch (UserStoreException e) {
            e.printStackTrace();
        }

        /*create users and assing to a role*/
        for (int i=1; i<=maxUserCount;i++){
            user= "user" +i;

            try {
                adminRealm.getUserStoreAdmin().addUser(user, "psw");
                Map currentProperties = adminRealm.getUserStoreAdmin().getUserProperties(user);
                currentProperties.put("friendlyName", "friendlyName");
                adminRealm.getUserStoreAdmin().setUserProperties(user, currentProperties);
                adminRealm.getUserStoreAdmin().addUserToRole(user,newRoleName);

            }catch (UserStoreException e) {
               e.printStackTrace();
            }
        }

        System.out.println("End of Adding"+" "+maxUserCount+" users to the role"+" "+newRoleName);

        /*creating more roles*/
        for (int i=1; i<=maxRoleCount;i++){
            String role= "role" +i;
            try {
                adminRealm.getUserStoreAdmin().addRole(role);
            }catch (UserStoreException e) {
               e.printStackTrace();
            }
         }
        System.out.println("End of Adding"+" "+maxRoleCount+" "+"roles");

    }

    public void testassignpermissiontoresources() throws Exception {

        UserRealm adminRealm = adminRegistry.getUserRealm();
        String user;

        HorizaontalPathGenerator obj = new HorizaontalPathGenerator(numberofLevels, maxChildPerNodes, "/c" ,maxResourceCount, "r1.txt");
        for (int i=1;i<=maxUserCount;i++){
            String resource_path = obj.getPath();

            user= "user" +i;

            try{
            adminRealm.getAccessControlAdmin()
                    .authorizeUser(user, resource_path, ActionConstants.PUT);
            adminRealm.getAccessControlAdmin()
                    .authorizeUser(user, resource_path, ActionConstants.DELETE);
            }catch (UserStoreException e) {
               e.printStackTrace();
            }
        }
        System.out.println("End of Assign User level write and delete permission to "+" "+maxUserCount+" "+"users");
        /*assing DELETE permission to resources for each role*/
        HorizaontalPathGenerator obj2 = new HorizaontalPathGenerator(numberofLevels, maxChildPerNodes, "/c" ,maxResourceCount, "r1.txt");
        for (int i=1;i<=maxResourceCount;i++){
            for (int j=1;j<=maxRoleCount;j++){
                if ((i==j)||(i%maxRoleCount==j)||(i%maxRoleCount==0)){
                    String role = "role" +j;
                    String resource_path = obj2.getPath();

                    try{
                    adminRealm.getAccessControlAdmin()
                            .authorizeRole(role,resource_path,ActionConstants.PUT);
                    adminRealm.getAccessControlAdmin()
                            .authorizeRole(role,resource_path,ActionConstants.DELETE);
                    }catch (UserStoreException e) {
                       e.printStackTrace();
                    }
                    if (j==10||(i%maxRoleCount==0)){
                        break;
                    }

                }
            }
        }
        System.out.println("End of Assign role level write and delete permission to "+" "+maxResourceCount+" "+"resources");
    }

    /* update existing resource to increase version numbers */
    public void testpopulateResourceVersioning() throws RegistryException,Exception{
        HorizaontalPathGenerator obj3 = new HorizaontalPathGenerator(numberofLevels, maxChildPerNodes, "/c" ,maxResourceCount, "r1.txt");
        for (int i=1;i<=maxResourceCount;i++){
            String resource_path = obj3.getPath();

            try {
                Resource r1 = adminRegistry.newResource();
                r1.setDescription("This is a file to test performance");
                String fileContent = "this is the file content";

                for (int filec=1;filec<=12;filec++){
                    fileContent += fileContent;
                }

                r1.setProperty("key1", "value1");
                r1.setProperty("key2", "value2");
                r1.setProperty("key3", "value3");
                r1.setProperty("key4", "value4");

                byte[] r1content = (fileContent).getBytes();
                r1.setContent(r1content);
                r1.setMediaType("text/plain");
                adminRegistry.put(resource_path,r1);

            }catch (RegistryException e) {
                e.printStackTrace();
            }
        }
        System.out.println("End of creating 2nd version of "+" "+maxResourceCount+" "+"resources");
    }
    public void testgetresourcetime() throws RegistryException{

            UserRegistry adminRegistry = registry.
                getUserRegistry(RegistryConstants.ADMIN_USER, RegistryConstants.ADMIN_PASSWORD);

            String path = "/c/c1/c1/r1.txt";
            try {
            long totaltime = 0;
            long numberOfTimes = 10000;

            for (int i=1;i<=numberOfTimes;i++){
                long startTime = System.nanoTime();
                adminRegistry.get(path);
                long runTime = System.nanoTime() - startTime;
                totaltime += runTime;
            }
            System.out.println("Average Resource get time: " + ((totaltime/1000000)/numberOfTimes)+ " ms");
            } catch (RegistryException e) {
                    fail("Couldn't get content from path" + path);
            }
        }

    public void testgetresourceversiontime() throws RegistryException{

            UserRegistry adminRegistry = registry.
                getUserRegistry(RegistryConstants.ADMIN_USER, RegistryConstants.ADMIN_PASSWORD);

            String path = "/c/c1/c1/r1.txt";

            String[] versionArray = adminRegistry.getVersions(path);

            String version_path = versionArray[0];

            try {
                long totaltime = 0;
                long numberOfTimes = 10000;

                for (int i=1;i<=numberOfTimes;i++){
                    long startTime = System.nanoTime();
                    adminRegistry.get(version_path);
                    long runTime = System.nanoTime() - startTime;
                    totaltime += runTime;
                }
                System.out.println("Average Resource version get time: " + ((totaltime/1000000)/numberOfTimes)+ " ms");
            } catch (RegistryException e) {
                fail("Couldn't get content from path" + version_path);
            }
    }

    public void testgetCollectiontime() throws RegistryException{

         UserRegistry adminRegistry = registry.
                getUserRegistry(RegistryConstants.ADMIN_USER, RegistryConstants.ADMIN_PASSWORD);

        String path = "/c/c1/c1";
        long totaltime = 0;
        long numberOfTimes = 10000;

        for (int i=1;i<=numberOfTimes;i++){
            try {
                long startTime = System.nanoTime();
                adminRegistry.get(path);
                long runTime = System.nanoTime() - startTime;
                totaltime += runTime;

            } catch (RegistryException e) {
                fail("Couldn't get content from path" + path);
            }
        }

        System.out.println("Average Collection get time: " +((totaltime/1000000)/numberOfTimes)+ " ms");
    }

    public void testputresourcetime() throws RegistryException,Exception{

         UserRegistry adminRegistry = registry.
                getUserRegistry(RegistryConstants.ADMIN_USER, RegistryConstants.ADMIN_PASSWORD);

        long totaltime = 0;

        HorizaontalPathGenerator obj3 = new HorizaontalPathGenerator(numberofLevels, maxChildPerNodesTesting, "/b" ,maxFileToputResource, "r1.txt");
        for (int i=1;i<=maxFileToputResource;i++){
            String path = obj3.getPath();

            try {
                Resource r1 = adminRegistry.newResource();
                r1.setDescription("This is new file description");
                String fileContent = "This is file the content";

                for (int filec=1;filec<=12;filec++){
                    fileContent += fileContent;
                }

                byte[] r1content = (fileContent).getBytes();
                r1.setContent(r1content);
                r1.setMediaType("txt");

                Comment c1 = new Comment();
                c1.setResourcePath(path);
                c1.setText("This is a test comment1");

                Comment c2 = new Comment();
                c2.setResourcePath(path);
                c2.setText("This is a test comment2");

                r1.setProperty("key1", "value1");
                r1.setProperty("key2", "value2");
                r1.setProperty("key3", "value3");
                r1.setProperty("key4", "value4");

                long startTime = System.nanoTime();
                adminRegistry.put(path,r1);
                long runTime = System.nanoTime() - startTime;
                totaltime += runTime;

                adminRegistry.addComment(path,c1);
                adminRegistry.addComment(path,c2);
                adminRegistry.applyTag(path, "tag1");
                adminRegistry.applyTag(path, "tag2");
                adminRegistry.applyTag(path, "tag3");
                adminRegistry.rateResource(path, 4);

            }catch (RegistryException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Average Resource put time: " + ((totaltime/1000000)/maxFileToputResource)+ " ms");
    }
    public void testputcollectiontime() throws RegistryException, Exception{
        UserRegistry adminRegistry = registry.
                getUserRegistry(RegistryConstants.ADMIN_USER, RegistryConstants.ADMIN_PASSWORD);

        long totaltime = 0;

                HorizaontalPathGenerator obj3 = new HorizaontalPathGenerator(numberofLevels, maxChildPerNodesTesting, "/a" ,maxFileToputResource, "aa");
                for (int i=1;i<=maxFileToputResource;i++){
                    String path_2 = obj3.getPath();

                    try {
                        Resource r1 = adminRegistry.newCollection();
                        r1.setDescription("This is new collection description");

                        Comment c1 = new Comment();
                        c1.setResourcePath(path_2);
                        c1.setText("This is a test comment1");

                        Comment c2 = new Comment();
                        c2.setResourcePath(path_2);
                        c2.setText("This is a test comment2");

                        r1.setProperty("key1", "value1");
                        r1.setProperty("key2", "value2");
                        r1.setProperty("key3", "value3");
                        r1.setProperty("key4", "value4");

                        long startTime = System.nanoTime();
                        adminRegistry.put(path_2,r1);
                        long runTime = System.nanoTime() - startTime;
                        totaltime += runTime;

                        adminRegistry.addComment(path_2,c1);
                        adminRegistry.addComment(path_2,c2);
                        adminRegistry.applyTag(path_2, "tag1");
                        adminRegistry.applyTag(path_2, "tag2");
                        adminRegistry.applyTag(path_2, "tag3");
                        adminRegistry.rateResource(path_2, 4);

                    }catch (RegistryException e) {
                        e.printStackTrace();
                    }
            }
            System.out.println("Average Collection put time: " +((totaltime/1000000)/maxFileToputResource)+ " ms");
    }

    public void testrenameresourcetime() throws RegistryException, Exception{

         UserRegistry adminRegistry = registry.
                getUserRegistry(RegistryConstants.ADMIN_USER, RegistryConstants.ADMIN_PASSWORD);

        long totaltime = 0;
        HorizaontalPathGenerator obj3 = new HorizaontalPathGenerator(numberofLevels, maxChildPerNodesTesting, "/b" ,maxFileToputResource, "r1.txt");
        HorizaontalPathGenerator obj4 = new HorizaontalPathGenerator(numberofLevels, maxChildPerNodesTesting, "/b" ,maxFileToputResource, "r2.txt");

        for (int i=1;i<=maxFileToputResource;i++){
            String path = obj3.getPath();
            String new_path = obj4.getPath();

            try {
                long startTime = System.nanoTime();
                adminRegistry.rename(path,new_path);
                long runTime = System.nanoTime() - startTime;
                totaltime += runTime;
            }catch (RegistryException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Average Time for resource rename: " +((totaltime/1000000)/maxFileToputResource)+ " ms");
    }

    public void testrenamecollectiontime() throws RegistryException, Exception{

         UserRegistry adminRegistry = registry.
                getUserRegistry(RegistryConstants.ADMIN_USER, RegistryConstants.ADMIN_PASSWORD);

        long totaltime = 0;
        HorizaontalPathGenerator obj3 = new HorizaontalPathGenerator(numberofLevels, maxChildPerNodesTesting, "/a" ,maxFileToputResource, "aa");
        HorizaontalPathGenerator obj4 = new HorizaontalPathGenerator(numberofLevels, maxChildPerNodesTesting, "/a" ,maxFileToputResource, "bb");

        for (int i=1;i<=maxFileToputResource;i++){
            String path = obj3.getPath();
            String new_path = obj4.getPath();
            try {
                long startTime = System.nanoTime();
                adminRegistry.rename(path,new_path);
                long runTime = System.nanoTime() - startTime;
                totaltime += runTime;

            }catch (RegistryException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Average time for collection rename: " +((totaltime/1000000)/maxFileToputResource)+ " ms");
    }

    public void testcopyresourcetime() throws RegistryException, Exception{

         UserRegistry adminRegistry = registry.
                getUserRegistry(RegistryConstants.ADMIN_USER, RegistryConstants.ADMIN_PASSWORD);
        HorizaontalPathGenerator obj3 = new HorizaontalPathGenerator(numberofLevels, maxChildPerNodesTesting, "/b" ,maxFileToputResource, "r2.txt");
        HorizaontalPathGenerator obj4 = new HorizaontalPathGenerator(numberofLevels, maxChildPerNodesTesting, "/d" ,maxFileToputResource, "r3.txt");

        long totaltime = 0;
        for (int i=1;i<=maxFileToputResource;i++){
            String path = obj3.getPath();
            String new_path = obj4.getPath();

            try {
                long startTime = System.nanoTime();
                adminRegistry.copy(path,new_path);
                long runTime = System.nanoTime() - startTime;
                totaltime += runTime;

            }catch (RegistryException e) {
                        e.printStackTrace();
            }
        }
        System.out.println("Average Time for resource copy: " +((totaltime/1000000)/maxFileToputResource)+ " ms");
    }

    public void testcopycollectiontime() throws RegistryException, Exception{

         UserRegistry adminRegistry = registry.
                getUserRegistry(RegistryConstants.ADMIN_USER, RegistryConstants.ADMIN_PASSWORD);
        HorizaontalPathGenerator obj3 = new HorizaontalPathGenerator(numberofLevels, maxChildPerNodesTesting, "/a" ,maxFileToputResource, "bb");
        HorizaontalPathGenerator obj4 = new HorizaontalPathGenerator(numberofLevels, maxChildPerNodesTesting, "/p" ,maxFileToputResource, "pp");

        long totaltime = 0;
        for (int i=1;i<=maxFileToputResource;i++){
            String path = obj3.getPath();
            String new_path = obj4.getPath();
            try {
                long startTime = System.nanoTime();
                adminRegistry.copy(path,new_path);
                long runTime = System.nanoTime() - startTime;
                totaltime += runTime;

            }catch (RegistryException e) {
                        e.printStackTrace();
            }
        }
        System.out.println("Average Time for collection copy: " +((totaltime/1000000)/maxFileToputResource)+ " ms");
    }

    public void testmoveresourcetime() throws RegistryException,Exception{

         UserRegistry adminRegistry = registry.
                getUserRegistry(RegistryConstants.ADMIN_USER, RegistryConstants.ADMIN_PASSWORD);
        HorizaontalPathGenerator obj3 = new HorizaontalPathGenerator(numberofLevels, maxChildPerNodesTesting, "/d" ,maxFileToputResource, "r3.txt");
        HorizaontalPathGenerator obj4 = new HorizaontalPathGenerator(numberofLevels, maxChildPerNodesTesting, "/dd" ,maxFileToputResource, "r4.txt");

        long totaltime = 0;

        for (int i=1;i<=maxFileToputResource;i++){
            String path = obj3.getPath();
            String new_path = obj4.getPath();
            try {
                long startTime = System.nanoTime();
                adminRegistry.move(path,new_path);
                long runTime = System.nanoTime() - startTime;
                totaltime += runTime;

            }catch (RegistryException e) {
                        e.printStackTrace();
            }
        }
        System.out.println("Average Time for resource move: " + ((totaltime/1000000)/maxFileToputResource)+ " ms");
    }

    public void testmovecollectiontime() throws RegistryException, Exception{

        UserRegistry adminRegistry = registry.
                    getUserRegistry(RegistryConstants.ADMIN_USER, RegistryConstants.ADMIN_PASSWORD);
        HorizaontalPathGenerator obj3 = new HorizaontalPathGenerator(numberofLevels, maxChildPerNodesTesting, "/p" ,maxFileToputResource, "pp");
        HorizaontalPathGenerator obj4 = new HorizaontalPathGenerator(numberofLevels, maxChildPerNodesTesting, "/q" ,maxFileToputResource, "ppq");

        long totaltime = 0;
        for (int i=1;i<=maxFileToputResource;i++){
            String path = obj3.getPath();
            String new_path = obj4.getPath();

            try {
                long startTime = System.nanoTime();
                adminRegistry.move(path,new_path);
                long runTime = System.nanoTime() - startTime;
                totaltime += runTime;
            }catch (RegistryException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Average Time for collection move: " +((totaltime/1000000)/maxFileToputResource)+ " ms");
    }

    public void testdeleteresourcetime() throws RegistryException, Exception{

        UserRegistry adminRegistry = registry.
                getUserRegistry(RegistryConstants.ADMIN_USER, RegistryConstants.ADMIN_PASSWORD);

        HorizaontalPathGenerator obj3 = new HorizaontalPathGenerator(numberofLevels, maxChildPerNodesTesting, "/dd" ,maxFileToputResource, "r4.txt");

        long totaltime = 0;
        for (int i=1;i<=maxFileToputResource;i++){
            String path = obj3.getPath();
            try {
                long startTime = System.nanoTime();
                adminRegistry.delete(path);
                long runTime = System.nanoTime() - startTime;
                totaltime += runTime;
            }catch (RegistryException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Average Resource delete time: " + ((totaltime/1000000)/maxFileToputResource)+ " ms");
    }
    public void testdeletecollectiontime() throws RegistryException, Exception{

        UserRegistry adminRegistry = registry.
                getUserRegistry(RegistryConstants.ADMIN_USER, RegistryConstants.ADMIN_PASSWORD);

        HorizaontalPathGenerator obj3 = new HorizaontalPathGenerator(numberofLevels, maxChildPerNodesTesting, "/q" ,maxFileToputResource, "ppq");

        long totaltime = 0;
        for (int i=1;i<=maxFileToputResource;i++){
            String path = obj3.getPath();
            try {
                long startTime = System.nanoTime();
                adminRegistry.delete(path);
                long runTime = System.nanoTime() - startTime;
                totaltime += runTime;
            }catch (RegistryException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Average Resource delete time: " + ((totaltime/1000000)/maxFileToputResource)+ " ms");
    }

    public void testImportresourcetime() throws RegistryException, Exception{

            UserRegistry adminRegistry = registry.
                    getUserRegistry(RegistryConstants.ADMIN_USER, RegistryConstants.ADMIN_PASSWORD);
            long totalTime = 0;
            String url = "https://wso2.org/repos/wso2/trunk/registry/modules/core/src/test/java/org/wso2/registry/app/UserTest.java";
            HorizaontalPathGenerator obj3 = new HorizaontalPathGenerator(numberofLevels, maxChildPerNodes, "/c" ,maxResourceCount, "UserTest.java");

            for (int i=1;i<=maxFileToputResource;i++){
                Resource r1 = adminRegistry.newResource();
                r1.setDescription("this is a file imported from url");
                String path = obj3.getPath();
                try {
                    long startTime = System.currentTimeMillis();
                    adminRegistry.importResource(path, url, r1);
                    long stopTime = System.currentTimeMillis();
                    long runTime = stopTime - startTime;
                    totalTime += runTime;
                } catch (RegistryException e) {
                    fail("Couldn't import content to path:" + path);
                }
            }
            System.out.println("Average time to import a Resource: " + totalTime/maxFileToputResource+ " ms");
    }

    public void testImportWsdlTime() throws RegistryException, Exception{

        UserRegistry adminRegistry = registry.
                getUserRegistry(RegistryConstants.ADMIN_USER, RegistryConstants.ADMIN_PASSWORD);
        long totalTime = 0;
        String url = "http://131.107.72.15/SoapWsdl_ComplexDataTypes_XmlFormatter_Service_Indigo/ComplexDataTypesDocLitB.svc?wsdl";

        HorizaontalPathGenerator obj3 = new HorizaontalPathGenerator(numberofLevels, maxChildPerNodes, "/c" ,maxResourceCount, "ComplexDataTypesDocLitB.wsdl");

        for (int i=1;i<=maxFileToputResource;i++){
            Resource r1 = adminRegistry.newResource();
            r1.setDescription("WSDL imported from url");
            r1.setMediaType("application/wsdl+xml");
            String path = obj3.getPath();
            try {
                long startTime = System.currentTimeMillis();
                adminRegistry.importResource(path, url, r1);
                long stopTime = System.currentTimeMillis();
                long runTime = stopTime - startTime;
                totalTime += runTime;
            } catch (RegistryException e) {
                fail("Couldn't import content to path:" + path);
            }
        }
        System.out.println("Average time to import a wsdl with dependencies: " + totalTime/maxFileToputResource+ " ms");
    }

    public void testWsdlValidationTime() throws RegistryException, Exception{

        UserRegistry adminRegistry = registry.
                getUserRegistry(RegistryConstants.ADMIN_USER, RegistryConstants.ADMIN_PASSWORD);
        long totalTime = 0;
        String url = "http://www.webservicex.net/RealTimeMarketData.asmx?wsdl";
        HorizaontalPathGenerator obj3 = new HorizaontalPathGenerator(numberofLevels, maxChildPerNodes, "/c" ,maxResourceCount, "RealTimeMarketData.wsdl");

        for (int i=1;i<=maxFileToputResource;i++){
            Resource r1 = adminRegistry.newResource();
            r1.setDescription("WSDL imported from url");
            r1.setMediaType("application/wsdl+xml");
            String path = obj3.getPath();
            try {
                long startTime = System.currentTimeMillis();
                adminRegistry.importResource(path, url, r1);
                long stopTime = System.currentTimeMillis();
                long runTime = stopTime - startTime;
                totalTime += runTime;
            } catch (RegistryException e){
                fail("Couldn't import content to path:" + path);
            }
        }
        System.out.println("Average time to validate a WSDL: " + totalTime/maxFileToputResource+ " ms");
    }

    public void testdatabaseStatistics() throws RegistryException{

         try{
            Connection con = dataSource.getConnection();
            con.setAutoCommit(false);

            String[] tableArray= {"COMMENT",
                    "DEPENDENCY","LOG","PROPERTY","PROPERTY_VERSION","RESOURCE",
                    "RESOURCE_VERSION","SNAPSHOT_RESOURCE_VERSION","SNAPSHOT",
                    "RATING","TAG","UM_PERMISSIONS","UM_ROLES",
                    "UM_ROLE_ATTRIBUTES","UM_ROLE_PERMISSIONS","UM_USERS",
                    "UM_USER_ATTRIBUTES","UM_USER_PERMISSIONS","UM_USER_ROLES",
                    "ASSOCIATION","CONTENT","CONTENT_VERSION",
                    "DEPENDENCY_VERSION"};

             System.out.println("Row Count of each Registry Table"+'\n');

             for (int i=0;i<tableArray.length;i++){
                 String query =  "SELECT COUNT(*) as RowCount FROM"+" "+tableArray[i];
                 PreparedStatement pstmt = con.prepareStatement(query);
                 ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    int rowcount= rs.getInt("RowCount");
                    System.out.println("Row count"+" "+tableArray[i]+": "+rowcount);
                }

                 rs.close();
                 pstmt.close();

             }
             con.close();

             }catch (SQLException e){
                e.printStackTrace();
         }
    }

    public void testAddCommentTime() throws RegistryException, Exception {
        long totalTime = 0;
        HorizaontalPathGenerator obj3 = new HorizaontalPathGenerator(numberofLevels, maxChildPerNodes, "/c" ,maxResourceCount, "r1.txt");

        for (int i=1;i<=maxFileToputResource;i++){
            String path = obj3.getPath();
            try {
                Comment c1 = new Comment();
                c1.setResourcePath(path);
                c1.setText("This is a test comment new");

                long startTime = System.nanoTime();
                adminRegistry.addComment(path,c1);
                long runTime = System.nanoTime() - startTime;
                totalTime += runTime;
            }catch (Exception e) {
                fail("Failed to add comments.");
                e.printStackTrace();
            }
        }
        System.out.println("Average time to add resource comment: "+ ((totalTime/1000000)/maxFileToputResource)+" ms");
    }

    public void testAccessComment() throws Exception {
        long totalTime = 0;
        HorizaontalPathGenerator obj3 = new HorizaontalPathGenerator(numberofLevels, maxChildPerNodes, "/c" ,maxResourceCount, "r1.txt");
        for (int i=1;i<=maxFileToputResource;i++){
            String path = obj3.getPath();
            try {
                long startTime = System.nanoTime();
                Comment[] comments1 = adminRegistry.getComments(path);
                long runTime = System.nanoTime() - startTime;
                totalTime += runTime;
            }catch (Exception e) {
                fail("Failed to read comments.");
                e.printStackTrace();
            }
        }
        System.out.println("Average time to access resource comment: "+ ((totalTime/1000000)/maxFileToputResource)+" ms");
    }

    public void testAddProperties() throws RegistryException,Exception {
        long totalTime = 0;
        HorizaontalPathGenerator obj3 = new HorizaontalPathGenerator(numberofLevels, maxChildPerNodes, "/c" ,maxResourceCount, "r1.txt");

        try{
            for (int i=1;i<maxFileToputResource;i++){
                String path = obj3.getPath();
                String key = "Registykey"+i;
                Resource r1 = adminRegistry.get(path);

                long startTime = System.nanoTime();
                r1.setProperty(key,"keyvalue");
                adminRegistry.put(path,r1);
                long runTime = System.nanoTime() - startTime;
                totalTime += runTime;
            }
        }catch (Exception e) {
            fail("Failed to initialize the registry.");
            e.printStackTrace();
        }
        System.out.println("Average time to add property: "+ ((totalTime/1000000)/maxFileToputResource)+" ms");
    }

    public void testReadProperties() throws RegistryException, Exception {
        long totalTime = 0;
        HorizaontalPathGenerator obj3 = new HorizaontalPathGenerator(numberofLevels, maxChildPerNodes, "/c" ,maxResourceCount, "r1.txt");

        try{
            for (int i=1;i<maxFileToputResource;i++){
                Resource r1 = adminRegistry.newResource();
                String path = obj3.getPath();

                long startTime = System.nanoTime();
                r1 = adminRegistry.get(path);
                r1.getProperties();
                long runTime = System.nanoTime() - startTime;
                totalTime += runTime;
            }

        }catch (Exception e) {
            fail("Failed to initialize the registry.");
            e.printStackTrace();
        }
        System.out.println("Average time to access Properties: "+((totalTime/1000000)/maxFileToputResource)+" ms");
    }

    public void testPropertySearch() throws RegistryException, Exception {

        long totalTime = 0;
        HorizaontalPathGenerator obj3 = new HorizaontalPathGenerator(numberofLevels, maxChildPerNodes, "/c" ,maxResourceCount, "r1.txt");

        try{
            for (int i=1;i<maxFileToputResource;i++){
                String path = obj3.getPath();

                String sql1 = "SELECT R.PATH FROM RESOURCE R, PROPERTY P WHERE P.NAME LIKE ? AND R.RID=P.RID";

                Resource q1 = adminRegistry.newResource();
                q1.setContent(sql1);
                q1.setMediaType(RegistryConstants.SQL_QUERY_MEDIA_TYPE);
                adminRegistry.put(path, q1);

                Map<String, String> params = new HashMap<String, String>();
                params.put("1", "%Registykey%");

                long startTime = System.nanoTime();
                Collection result = adminRegistry.executeQuery(path, params);
                String[] qPaths = (String[]) result.getContent();
                long runTime = System.nanoTime() - startTime;
                totalTime += runTime;
            }
        }catch (Exception e) {
            fail("Failed to execute query for resource name.");
            e.printStackTrace();
        }
        System.out.println("Average time to search Property Names: "+((totalTime/1000000)/maxFileToputResource)+" ms");
    }

    public void testAddTagToResources() throws RegistryException, Exception {

        int totaltime = 0;
        HorizaontalPathGenerator obj3 = new HorizaontalPathGenerator(numberofLevels, maxChildPerNodes, "/c" ,maxResourceCount, "r1.txt");

        for (int i=1;i<=maxFileToputResource;i++){
            String path = obj3.getPath();

            long startTime = System.nanoTime();
            adminRegistry.applyTag(path,"wso2_soa_registry");
            long stopTime = System.nanoTime();
            long runTime = stopTime - startTime;
            totaltime += runTime;
        }
        System.out.println("Average time to apply tag to resource file"+" "+((totaltime/1000000)/maxFileToputResource)+" "+"ms");
    }

    public void testAddTagToCollection() throws RegistryException, Exception {
        int totaltime = 0;
        HorizaontalPathGenerator obj3 = new HorizaontalPathGenerator(numberofLevels, maxChildPerNodes, "/a" ,maxFileToputResource, "bb");

        for (int i=1;i<=maxFileToputResource;i++){
            String path = obj3.getPath();
            try {
                Resource r1 = adminRegistry.newCollection();
                r1.setDescription("This is the collection description");
                adminRegistry.put(path, r1);
                long startTime = System.nanoTime();
                adminRegistry.applyTag(path,"wso2_soa_registy_col");
                long stopTime = System.nanoTime();
                long runTime = stopTime - startTime;
                totaltime += runTime;
            }catch (RegistryException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Average time to apply tag to Collection"+" "+((totaltime/1000000)/maxFileToputResource)+" "+"ms");
    }

    public void testReadTagFromResourceTime() throws RegistryException {
        long loopcount = 10000;
        String path = "/c/c1/c1/r1.txt";
        long totalTime = 0;

        for (int i=1;i<=loopcount;i++){
            long startTime = System.nanoTime();
            adminRegistry.getTags(path);
            long runTime = System.nanoTime() - startTime;
            totalTime += runTime;
        }
        System.out.println("Average time to read tag from resource file(getTags)"+" "+((totalTime/1000000)/loopcount)+" "+"ms");
    }

    public void testReadTagFromResourcepathTime() throws RegistryException {
        long loopcount = 1000;

        long totalTime = 0;
        TaggedResourcePath[] paths;

        for (int i=1;i<=loopcount;i++){
            long startTime = System.nanoTime();
            paths = adminRegistry.getResourcePathsWithTag("wso2_soa_registry");
            long runTime = System.nanoTime() - startTime;
            totalTime += runTime;
        }
        System.out.println("Average time to read tag from resource path(getResourcePathsWithTag)"+" "+((totalTime/1000000)/loopcount)+" "+"ms");
    }
}
