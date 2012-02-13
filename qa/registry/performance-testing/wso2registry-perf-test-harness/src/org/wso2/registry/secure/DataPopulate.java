package org.wso2.registry.secure;

import junit.framework.TestCase;
import org.wso2.registry.*;
import org.wso2.registry.config.RegistryContext;
import org.wso2.registry.exceptions.RegistryException;
import org.wso2.registry.jdbc.EmbeddedRegistry;
import org.wso2.registry.jdbc.realm.RegistryRealm;
import org.wso2.registry.jdbc.utils.RegistryDataSource;
import org.wso2.registry.session.UserRegistry;
import org.wso2.registry.users.UserRealm;
import org.wso2.registry.users.UserStoreException;

import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;


public class DataPopulate extends PerfTestingSetup {

    public DataPopulate(String text) {
      super(text);
    }

    public void testpopulateUserBasedAuthorization() throws Exception {



        UserRealm adminRealm = adminRegistry.getUserRealm();

        String user;
        String newRoleName = "registryTeam";

        /*add registry-team role*/
        try{
            adminRealm.getUserStoreAdmin().addRole(newRoleName);
        }catch (UserStoreException e) {
            e.printStackTrace();
        }

        FileWriter fstream = new FileWriter("out.txt");
             BufferedWriter out = new BufferedWriter(fstream);

        /*create users and assing to a role*/
        for (int i=1; i<=maxUserCount;i++){
            user= "user" +i;


            try {
                adminRealm.getUserStoreAdmin().addUser(user, "psw");
                Map currentProperties = adminRealm.getUserStoreAdmin().getUserProperties(user);
                currentProperties.put("friendlyName", "friendlyName");
                adminRealm.getUserStoreAdmin().setUserProperties(user, currentProperties);
                adminRealm.getUserStoreAdmin().addUserToRole(user,newRoleName);

                if (i%10==0){
                    try{
                    out.write("Adding "+i+" Users"+ "\n");


        }catch (Exception e){//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
                }

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
            if (i%10==0){
                out.write("Adding "+i+" Roles"+ "\n");
            }
        }
        System.out.println("End of Adding"+" "+maxRoleCount+" "+"roles");
        out.close();                 
    }

    public void testassignpermissiontoresources() throws RegistryException{

        UserRealm adminRealm = adminRegistry.getUserRealm();
        String user;

        /*assing PUT and DELETE permission to resources for each user*/
        for (int i=1;i<=maxUserCount;i++){
            String resource_1= "/c"+i;
            String resource_2= resource_1 + "/b" +i;
            String resource_path= resource_2+"/r1.txt";
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
        for (int i=1;i<=maxResourceCount;i++){
            for (int j=1;j<=maxRoleCount;j++){
                if ((i==j)||(i%maxRoleCount==j)||(i%maxRoleCount==0)){
                    String role = "role" +j;
                    String resource_1= "/c"+i;
                    String resource_2= resource_1 + "/b" +i;
                    String resource_path= resource_2+"/r1.txt";

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
    public void testpopulateResourceVersioning() throws RegistryException{

        for (int i=1;i<=maxResourceCount;i++){
            String path_1= "/c"+i;
            String path_2= path_1 + "/b" +i;
            String path= path_2+"/r1.txt";
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
                adminRegistry.put(path,r1);

            }catch (RegistryException e) {
                e.printStackTrace();
            }
        }
        System.out.println("End of creating 2nd version of "+" "+maxResourceCount+" "+"resources");
    }

    public void testgetresourcetime() throws RegistryException{

        UserRegistry adminRegistry = registry.
                getUserRegistry(RegistryConstants.ADMIN_USER, RegistryConstants.ADMIN_PASSWORD);

        String path = "/c50/b50/r1.txt";
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

    public void testpopulateResources() throws RegistryException {

        for (int i=1;i<=maxResourceCount;i++){
            String path_1= "/c"+i;
            String path_2= path_1 + "/b" +i;
            String path= path_2+"/r1.txt";

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
                c1.setResourcePath(path);
                c1.setText("This is a test comment1");

                Comment c2 = new Comment();
                c2.setResourcePath(path);
                c2.setText("This is a test comment2");

                r1.setProperty("key1", "value1");
                r1.setProperty("key2", "value2");
                r1.setProperty("key3", "value3");
                r1.setProperty("key4", "value4");

                adminRegistry.put(path,r1);
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
        System.out.println("End of creating"+" "+maxResourceCount+" Resources");
    }

    public void testgetCollectiontime() throws RegistryException{

         UserRegistry adminRegistry = registry.
                getUserRegistry(RegistryConstants.ADMIN_USER, RegistryConstants.ADMIN_PASSWORD);

        String path = "/c50/b50";
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

    public void testgetresourceversiontime() throws RegistryException{

        UserRegistry adminRegistry = registry.
                getUserRegistry(RegistryConstants.ADMIN_USER, RegistryConstants.ADMIN_PASSWORD);

        String path = "/c50/b50/r1.txt";
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
                //System.out.println( runTime);
                //System.out.println ("This total time is:" + totaltime);
            }
            System.out.println("Average Resource version get time: " + ((totaltime/1000000)/numberOfTimes)+ " ms");
            } catch (RegistryException e) {
                    fail("Couldn't get content from path" + version_path);
            }
        }


    public void testputresourcetime() throws RegistryException{

         UserRegistry adminRegistry = registry.
                getUserRegistry(RegistryConstants.ADMIN_USER, RegistryConstants.ADMIN_PASSWORD);

        long totaltime = 0;
        for (int i=1;i<=maxFileToputResource;i++){
            String path_1= "/d"+i;
            String path_2= path_1 + "/b" +i;
            String path= path_2+"/r1.txt";

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

        public void testputcollectiontime() throws RegistryException{

            UserRegistry adminRegistry = registry.
                    getUserRegistry(RegistryConstants.ADMIN_USER, RegistryConstants.ADMIN_PASSWORD);

            long totaltime = 0;

            for (int i=1;i<=maxFileToputCollections;i++){
                String path_1= "/dd"+i;
                String path_2= path_1 + "/bb" +i;


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

    public void testrenameresourcetime() throws RegistryException{

         UserRegistry adminRegistry = registry.
                getUserRegistry(RegistryConstants.ADMIN_USER, RegistryConstants.ADMIN_PASSWORD);

        long totaltime = 0;
        for (int i=1;i<=maxFileToputResource;i++){
            String path_1= "/d"+i;
            String path_2= path_1 + "/b" +i;
            String path= path_2+"/r1.txt";
            String new_path_1= "/d"+i;
            String new_path_2= new_path_1 + "/b" +i;
            String new_path= new_path_2+"/r2.txt";

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

    public void testrenamecollectiontime() throws RegistryException{

         UserRegistry adminRegistry = registry.
                getUserRegistry(RegistryConstants.ADMIN_USER, RegistryConstants.ADMIN_PASSWORD);

        long totaltime = 0;
        for (int i=1;i<=maxFileToputCollections;i++){

            String path_1= "/dd"+i;
            String path_2= path_1 + "/bb" +i;
            String new_path_1= "/dd"+i;
            String new_path_2= new_path_1 + "/kk" +i;

            try {
                long startTime = System.nanoTime();
                adminRegistry.rename(path_2,new_path_2);
                long runTime = System.nanoTime() - startTime;
                totaltime += runTime;

            }catch (RegistryException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Average time for collection rename: " +((totaltime/1000000)/maxFileToputResource)+ " ms");
    }

     public void testcopyresourcetime() throws RegistryException{

         UserRegistry adminRegistry = registry.
                getUserRegistry(RegistryConstants.ADMIN_USER, RegistryConstants.ADMIN_PASSWORD);

        long totaltime = 0;
        for (int i=1;i<=maxFileToputResource;i++){
            String path_1= "/d"+i;
            String path_2= path_1 + "/b" +i;
            String path= path_2+"/r2.txt";
            String new_path_1= "/a"+i;
            String new_path_2= new_path_1 + "/b" +i;
            String new_path= new_path_2+"/r3.txt";

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

        public void testcopycollectiontime() throws RegistryException{

         UserRegistry adminRegistry = registry.
                getUserRegistry(RegistryConstants.ADMIN_USER, RegistryConstants.ADMIN_PASSWORD);

        long totaltime = 0;
        for (int i=1;i<=maxFileToputResource;i++){
            String path_1= "/dd"+i;
            String path_2= path_1 + "/kk" +i;
            String new_path_1= "/yy"+i;
            String new_path_2= new_path_1 + "/zz" +i;

            try {
                long startTime = System.nanoTime();
                adminRegistry.copy(path_2,new_path_2);
                long runTime = System.nanoTime() - startTime;
                totaltime += runTime;

            }catch (RegistryException e) {
                        e.printStackTrace();
            }
        }
        System.out.println("Average Time for collection copy: " +((totaltime/1000000)/maxFileToputResource)+ " ms");
    }

    public void testmoveresourcetime() throws RegistryException{

         UserRegistry adminRegistry = registry.
                getUserRegistry(RegistryConstants.ADMIN_USER, RegistryConstants.ADMIN_PASSWORD);

        long totaltime = 0;
        for (int i=1;i<=maxFileToputResource;i++){
            String path_1= "/d"+i;
            String path_2= path_1 + "/b" +i;
            String path= path_2+"/r2.txt";
            String new_path_1= "/xx"+i;
            String new_path_2= new_path_1 + "/r5.txt" +i;


            try {
                long startTime = System.nanoTime();
                adminRegistry.move(path,new_path_2);
                long runTime = System.nanoTime() - startTime;
                totaltime += runTime;

            }catch (RegistryException e) {
                        e.printStackTrace();
            }
        }
        System.out.println("Average Time for resource move: " + ((totaltime/1000000)/maxFileToputResource)+ " ms");
    }

     public void testmovecollectiontime() throws RegistryException{
         UserRegistry adminRegistry = registry.
                getUserRegistry(RegistryConstants.ADMIN_USER, RegistryConstants.ADMIN_PASSWORD);

        long totaltime = 0;
        for (int i=1;i<=maxFileToputResource;i++){
            String path_1= "/dd"+i;
            String path_2= path_1 + "/kk" +i;

            String new_path_1= "/gg"+i;
            String new_path_2= new_path_1 + "/nn" +i;


            try {
                long startTime = System.nanoTime();
                adminRegistry.move(path_2,new_path_2);
                long runTime = System.nanoTime() - startTime;
                totaltime += runTime;

            }catch (RegistryException e) {
                        e.printStackTrace();
            }
        }
        System.out.println("Average Time for collection move: " +((totaltime/1000000)/maxFileToputResource)+ " ms");
    }

    public void testdeleteresourcetime() throws RegistryException{

        UserRegistry adminRegistry = registry.
                getUserRegistry(RegistryConstants.ADMIN_USER, RegistryConstants.ADMIN_PASSWORD);

        long totaltime = 0;
        for (int i=1;i<=maxFileToputResource;i++){
            String path_1= "/a"+i;
            String path_2= path_1 + "/b" +i;
            String path= path_2+"/r3.txt";

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

    public void testdeletecollectiontime() throws RegistryException{

         UserRegistry adminRegistry = registry.
                getUserRegistry(RegistryConstants.ADMIN_USER, RegistryConstants.ADMIN_PASSWORD);


        long totaltime = 0;
        for (int i=1;i<=maxFileToputCollections;i++){
            String path_1= "/gg"+i;
            String path_2= path_1 + "/nn" +i;


            try {
                long startTime = System.nanoTime();
                adminRegistry.delete(path_2);
                long runTime = System.nanoTime() - startTime;
                totaltime += runTime;
            }catch (RegistryException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Average Collection delete time: " + ((totaltime/1000000)/maxFileToputResource)+" ms");
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


    public void testImportresourcetime() throws RegistryException{

        UserRegistry adminRegistry = registry.
                getUserRegistry(RegistryConstants.ADMIN_USER, RegistryConstants.ADMIN_PASSWORD);
        long totalTime = 0;
        String url = "https://wso2.org/repos/wso2/trunk/registry/modules/core/src/test/java/org/wso2/registry/app/UserTest.java";

        for (int i=1;i<=maxFileToputResource;i++){
            Resource r1 = adminRegistry.newResource();
            r1.setMediaType("text/plain");
            r1.setDescription("this is a file imported from url");
            String path_1= "/c"+i;
            String path_2= path_1 + "/b" +i;
            String path= path_2 +"/UserTest.java";
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

    public void testImportWsdlTime() throws RegistryException{

        UserRegistry adminRegistry = registry.
                getUserRegistry(RegistryConstants.ADMIN_USER, RegistryConstants.ADMIN_PASSWORD);
        long totalTime = 0;
        String url = "http://131.107.72.15/SoapWsdl_ComplexDataTypes_XmlFormatter_Service_Indigo/ComplexDataTypesDocLitB.svc?wsdl";

        for (int i=1;i<=maxFileToputResource;i++){
            Resource r1 = adminRegistry.newResource();
            r1.setDescription("WSDL imported from url");
            r1.setMediaType("application/wsdl+xml");
            String path_1= "/c"+i;
            String path_2= path_1 + "/b" +i;
            String path= path_2 + "/ComplexDataTypesDocLitB.wsdl";

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

        public void testWsdlValidationTime() throws RegistryException{

            UserRegistry adminRegistry = registry.
                    getUserRegistry(RegistryConstants.ADMIN_USER, RegistryConstants.ADMIN_PASSWORD);
            long totalTime = 0;
            String url = "http://www.webservicex.net/RealTimeMarketData.asmx?wsdl";
            for (int i=1;i<=maxFileToputResource;i++){

                Resource r1 = adminRegistry.newResource();
                String path_1= "/c"+i;
                String path_2= path_1 + "/b" +i;
                String path = path_2+"/RealTimeMarketData.wsdl";
                r1.setDescription("WSDL imported from url");
                r1.setMediaType("application/wsdl+xml");
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
            System.out.println("Average time to validate a WSDL: " + totalTime/maxFileToputResource+ " ms");
    }
}










