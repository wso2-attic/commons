package org.wso2.registry.secure;

import org.wso2.registry.users.UserRealm;
import org.wso2.registry.users.UserStoreException;

import java.io.FileWriter;
import java.io.BufferedWriter;
import java.util.Map;

public class UserLoadTest extends PerfTestingSetup{

        public UserLoadTest(String text){
            super(text);
        }

        public void testUserLoad() throws Exception {

        UserRealm adminRealm = adminRegistry.getUserRealm();

        String user;
        String newRoleName = "registryTeam";

        /*add registry-team role*/
        try{
            adminRealm.getUserStoreAdmin().addRole(newRoleName);
        }catch (UserStoreException e) {
            e.printStackTrace();
        }

        FileWriter fstream = new FileWriter("UserLoadTest.txt");
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

                if (i%500==0){
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
            if (i%500==0){
                out.write("Adding "+i+" Roles"+ "\n");
            }
        }
        System.out.println("End of Adding"+" "+maxRoleCount+" "+"roles");
        out.close();
    }
}
