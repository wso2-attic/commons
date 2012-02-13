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
import java.util.HashMap;


public class AppVerticalDataPopulate extends AppPerfTestingSetup {
    public AppVerticalDataPopulate(String text) {
      super(text);
    }

    public void testVerticalDatapopulate() throws RegistryException {

        VerticalPathGenerator obj = new VerticalPathGenerator(maxResourceCount,"r1.txt","c");
        for (int j=0;j<maxResourceCount;j++)
        {
            String resourcePathNew = obj.getPath();

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
        System.out.println("End of creating"+" "+maxResourceCount+" Resources Vertically");
    }

    public void testpopulateUserBasedAuthorization() throws RegistryException {

        UserRealm adminRealm = userRegistry.getUserRealm();
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

    public void testassignpermissiontoresources() throws RegistryException{

        UserRealm adminRealm = userRegistry.getUserRealm();
        String user;

        VerticalPathGenerator obj = new VerticalPathGenerator(maxResourceCount,"r1.txt","c");
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
        VerticalPathGenerator obj2 = new VerticalPathGenerator(maxResourceCount,"r1.txt","c");
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
    public void testpopulateResourceVersioning() throws RegistryException{
        VerticalPathGenerator obj3 = new VerticalPathGenerator(maxResourceCount,"r1.txt","c");
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

            String path = "/c1/c2/c3/c4/c5/c6/c7/c8/c9/c10/c11/c12/c13/c14/c15/c16/c17/c18/c19/c20/c21/c22" +
                    "/c23/c24/c25/c26/c27/c28/c29/c30/c31/c32/c33/c34/c35/c36/c37/c38/c39/c40/c41/c42/c43/c44" +
                    "/c45/c46/c47/c48/c49/c50/c51/c52/c53/c54/c55/c56/c57/c58/c59/c60/c61/c62/c63/c64/c65/c66/c67" +
                    "/c68/c69/c70/c71/c72/c73/c74/c75/c76/c77/c78/c79/c80/c81/c82/c83/c84/c85/c86/c87/c88/c89" +
                    "/c90/c91/c92/c93/c94/c95/c96/c97/c98/c99/c100/r1.txt";
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


            String path = "/c1/c2/c3/c4/c5/c6/c7/c8/c9/c10/c11/c12/c13/c14/c15/c16/c17/c18/c19/c20/c21/c22" +
                    "/c23/c24/c25/c26/c27/c28/c29/c30/c31/c32/c33/c34/c35/c36/c37/c38/c39/c40/c41/c42/c43/c44" +
                    "/c45/c46/c47/c48/c49/c50/c51/c52/c53/c54/c55/c56/c57/c58/c59/c60/c61/c62/c63/c64/c65/c66/c67" +
                    "/c68/c69/c70/c71/c72/c73/c74/c75/c76/c77/c78/c79/c80/c81/c82/c83/c84/c85/c86/c87/c88/c89" +
                    "/c90/c91/c92/c93/c94/c95/c96/c97/c98/c99/c100/r1.txt";

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

        String path = "/c1/c2/c3/c4/c5/c6/c7/c8/c9/c10/c11/c12/c13/c14/c15/c16/c17/c18/c19/c20/c21/c22" +
                    "/c23/c24/c25/c26/c27/c28/c29/c30/c31/c32/c33/c34/c35/c36/c37/c38/c39/c40/c41/c42/c43/c44" +
                    "/c45/c46/c47/c48/c49/c50/c51/c52/c53/c54/c55/c56/c57/c58/c59/c60/c61/c62/c63/c64/c65/c66/c67" +
                    "/c68/c69/c70/c71/c72/c73/c74/c75/c76/c77/c78/c79/c80/c81/c82/c83/c84/c85/c86/c87/c88/c89" +
                    "/c90/c91/c92/c93/c94/c95/c96/c97/c98/c99";
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

    public void testputresourcetime() throws RegistryException{

        long totaltime = 0;
        VerticalPathGenerator obj3 = new VerticalPathGenerator(maxFileToputResource,"r1.txt","b");
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
    public void testputcollectiontime() throws RegistryException{

        long totaltime = 0;
                VerticalPathGenerator obj3 = new VerticalPathGenerator(maxFileToputResource,"y","x");
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

    public void testrenameresourcetime() throws RegistryException{

        long totaltime = 0;
        VerticalPathGenerator obj3 = new VerticalPathGenerator(maxFileToputResource,"r1.txt","b");
        VerticalPathGenerator obj4 = new VerticalPathGenerator(maxFileToputResource,"r2.txt","b");
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

    public void testrenamecollectiontime() throws RegistryException{

        long totaltime = 0;
        VerticalPathGenerator obj3 = new VerticalPathGenerator(maxFileToputResource,"y","x");
        VerticalPathGenerator obj4 = new VerticalPathGenerator(maxFileToputResource,"z","x");
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

    public void testcopyresourcetime() throws RegistryException{

        VerticalPathGenerator obj3 = new VerticalPathGenerator(maxFileToputResource,"r2.txt","b");
        VerticalPathGenerator obj4 = new VerticalPathGenerator(maxFileToputResource,"r3.txt","aa");
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

    public void testcopycollectiontime() throws RegistryException{

        VerticalPathGenerator obj3 = new VerticalPathGenerator(maxFileToputResource,"z","x");
        VerticalPathGenerator obj4 = new VerticalPathGenerator(maxFileToputResource,"r","p");
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

    public void testmoveresourcetime() throws RegistryException{

        VerticalPathGenerator obj3 = new VerticalPathGenerator(maxFileToputResource,"r2.txt","b");
        VerticalPathGenerator obj4 = new VerticalPathGenerator(maxFileToputResource,"r3.txt","bb");
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

    public void testmovecollectiontime() throws RegistryException{

        VerticalPathGenerator obj3 = new VerticalPathGenerator(maxFileToputResource,"r","p");
        VerticalPathGenerator obj4 = new VerticalPathGenerator(maxFileToputResource,"o","q");

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

    public void testdeleteresourcetime() throws RegistryException{

        VerticalPathGenerator obj3 = new VerticalPathGenerator(maxFileToputResource,"r2.txt","b");

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
    public void testdeletecollectiontime() throws RegistryException{

        VerticalPathGenerator obj3 = new VerticalPathGenerator(maxFileToputResource,"z","x");

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

    public void testImportresourcetime() throws RegistryException{
            long totalTime = 0;
            String url = "https://wso2.org/repos/wso2/trunk/registry/modules/core/src/test/java/org/wso2/registry/app/UserTest.java";

            VerticalPathGenerator obj3 = new VerticalPathGenerator(maxFileToputResource,"UserTest.java","c");
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

    public void testImportWsdlTime() throws RegistryException{

        long totalTime = 0;
        String url = "http://131.107.72.15/SoapWsdl_ComplexDataTypes_XmlFormatter_Service_Indigo/ComplexDataTypesDocLitB.svc?wsdl";
        VerticalPathGenerator obj3 = new VerticalPathGenerator(maxFileToputResource,"ComplexDataTypesDocLitB.wsdl","c");
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

    public void testWsdlValidationTime() throws RegistryException{

        long totalTime = 0;
        String url = "http://www.webservicex.net/RealTimeMarketData.asmx?wsdl";
        VerticalPathGenerator obj3 = new VerticalPathGenerator(maxFileToputResource,"RealTimeMarketData.wsdl","c");
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

    public void testAddCommentTime() throws RegistryException {
        long totalTime = 0;
        VerticalPathGenerator obj3 = new VerticalPathGenerator(maxFileToputResource,"r1.txt","c");
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

    public void testAccessComment() throws RegistryException {
        long totalTime = 0;
        VerticalPathGenerator obj3 = new VerticalPathGenerator(maxFileToputResource,"r1.txt","c");
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

    public void testAddProperties() throws RegistryException {
        long totalTime = 0;
        VerticalPathGenerator obj3 = new VerticalPathGenerator(maxFileToputResource,"r1.txt","c");
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

    public void testReadProperties() throws RegistryException {
        long totalTime = 0;
        VerticalPathGenerator obj3 = new VerticalPathGenerator(maxFileToputResource,"r1.txt","c");
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

    public void testPropertySearch() throws RegistryException {

        long totalTime = 0;
        VerticalPathGenerator obj3 = new VerticalPathGenerator(maxFileToputResource,"r1.txt","c");
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

    public void testAddTagToResources() throws RegistryException {

        int totaltime = 0;
        VerticalPathGenerator obj3 = new VerticalPathGenerator(maxFileToputResource,"r1.txt","c");
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

    public void testAddTagToCollection() throws RegistryException {
        int totaltime = 0;
        VerticalPathGenerator obj3 = new VerticalPathGenerator(maxFileToputResource,"z","x");
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
        String path = "/c1/c2/c3/c4/c5/c6/c7/c8/c9/c10/c11/c12/c13/c14/c15/c16/c17/c18/c19/c20/c21/c22" +
                    "/c23/c24/c25/c26/c27/c28/c29/c30/c31/c32/c33/c34/c35/c36/c37/c38/c39/c40/c41/c42/c43/c44" +
                    "/c45/c46/c47/c48/c49/c50/c51/c52/c53/c54/c55/c56/c57/c58/c59/c60/c61/c62/c63/c64/c65/c66/c67" +
                    "/c68/c69/c70/c71/c72/c73/c74/c75/c76/c77/c78/c79/c80/c81/c82/c83/c84/c85/c86/c87/c88/c89" +
                    "/c90/c91/c92/c93/c94/c95/c96/c97/c98/c99/c100/r1.txt";
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