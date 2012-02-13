package org.wso2.usermanager.pool;

import org.wso2.usermanager.readwrite.DefaultRealm;
import org.wso2.usermanager.readwrite.DefaultRealmConfig;

public class UserSessionSimulator implements Runnable{
    
    private DefaultRealmConfig config = null;
    private DefaultRealm realm = null;
    private String username = null;
    private String password = null;
   
    public UserSessionSimulator(String username, String password) throws Exception{
        this.username = username;
        this.password = password;
        config = new DefaultRealmConfig();
        config.setConnectionURL("jdbc:derby:target/pool/UserDatabase");
        realm = new DefaultRealm();
        realm.init(config);
    }
        
    public void run() {
        //create a realm on User login
        try {
            realm.getAuthenticator().authenticate(username, 
                    password);
            Thread.sleep(30);
            realm.getAuthorizer().isUserAuthorized(username,
                    "book", "read");
            Thread.sleep(30);
            realm.getAuthorizer().isUserAuthorized(username,
                    "book", "read");
        } catch (Exception e) {
           e.printStackTrace();
        } 
    }

    
}
