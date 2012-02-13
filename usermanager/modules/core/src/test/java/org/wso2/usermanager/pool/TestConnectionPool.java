package org.wso2.usermanager.pool;

import java.sql.Connection;
import java.sql.Driver;
import java.util.Properties;

import junit.framework.TestCase;

import org.wso2.usermanager.Realm;
import org.wso2.usermanager.readwrite.DefaultRealm;
import org.wso2.usermanager.readwrite.DefaultRealmConfig;
import org.wso2.usermanager.readwrite.util.DefaultDatabaseUtil;

public class TestConnectionPool extends TestCase {
    
     
    DefaultRealmConfig config = null;

    String connectionURL = "jdbc:derby:target/pool/UserDatabase";

    protected void setUp() throws Exception {
        super.setUp();
        Class clazz = Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        Driver driver = (Driver) clazz.newInstance();
        Properties props = new Properties();
        Connection dbConnection = driver.connect(
                connectionURL + ";create=true", props);

        DefaultDatabaseUtil.createDatabase(dbConnection);
        
        dbConnection.close();
        dbConnection = null;
        
        Realm realm = new DefaultRealm();
        config = (DefaultRealmConfig) realm.getRealmConfiguration();
        config.setConnectionURL(connectionURL);
        realm.init(config);

    }
    
    
    public void testPool() throws Exception{
        UserSessionSimulator sim1 = new UserSessionSimulator("admin1", "admin1");
        UserSessionSimulator sim2 = new UserSessionSimulator("admin2", "admin2");
        UserSessionSimulator sim3 = new UserSessionSimulator("admin3", "admin3");
        UserSessionSimulator sim4 = new UserSessionSimulator("admin4", "admin4");
        UserSessionSimulator sim5 = new UserSessionSimulator("admin5", "admin5");
        Thread th1 = new Thread(sim1);
        Thread th2 = new Thread(sim2);
        Thread th3 = new Thread(sim3);
        Thread th4 = new Thread(sim4);
        Thread th5 = new Thread(sim5);
        th1.start();
        th2.start();
        th3.start();
        th4.start();
        th5.start();
        
        
        th1.join();
        th2.join();
        th3.join();
        th4.join();
        th5.join();
        
        //test reset method.....
        config.setConnectionURL(connectionURL);
        Realm realm = new DefaultRealm();
        realm.init(config);
        
        
        
    }

    
}
