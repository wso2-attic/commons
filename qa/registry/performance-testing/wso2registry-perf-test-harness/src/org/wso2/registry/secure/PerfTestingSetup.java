package org.wso2.registry.secure;

import org.wso2.registry.jdbc.utils.RegistryDataSource;
import org.wso2.registry.jdbc.realm.RegistryRealm;
import org.wso2.registry.jdbc.EmbeddedRegistry;
import org.wso2.registry.config.RegistryContext;
import org.wso2.registry.RegistryConstants;
import org.wso2.registry.session.UserRegistry;

import javax.sql.DataSource;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import junit.framework.TestCase;


public class PerfTestingSetup extends TestCase {


    UserRegistry adminRegistry = null;
    private static RegistryRealm realm = null;
    DataSource dataSource = null;
    RegistryRealm registryRealm = null;
    EmbeddedRegistry registry = null;

    int maxUserCount = 100;
    int maxRoleCount = 100;
    int maxFileToputCollections = 100;
    /* maxFileToputResource must be less than maxResourceCount*/
    long maxResourceCount = 100;
    long maxFileToputResource = 100;
    long loopIterator = 1000;
    long numberofLevels = 5;
    long maxChildPerNodes = maxResourceCount/numberofLevels;
    long maxChildPerNodesTesting =  maxFileToputResource/numberofLevels;

    public PerfTestingSetup (String text) {
      super(text);
    }

     public void setUp() {
           try {
               //dataSource = new RegistryDataSource("jdbc:derby://lochost:1527/derbyDB2",
               //        "org.apache.derby.jdbc.ClientDriver","reg","reg");

               dataSource = new RegistryDataSource("jdbc:mysql://localhost:3306/regdb5",
                       "com.mysql.jdbc.Driver","regadmin","regadmin");

               registryRealm = new RegistryRealm(dataSource);

               InputStream in = new FileInputStream("registry.xml");
               RegistryContext registryContext = new RegistryContext(in);
               registry = new EmbeddedRegistry(registryContext);
               adminRegistry = registry.getUserRegistry(RegistryConstants.ADMIN_USER, RegistryConstants.ADMIN_PASSWORD);
               //setUp2();


           }catch (Exception e) {
               fail("Failed to initialize the registry.");
               e.printStackTrace();
          }
     }

   
}


