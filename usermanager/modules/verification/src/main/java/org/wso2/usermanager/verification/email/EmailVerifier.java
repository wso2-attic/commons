package org.wso2.usermanager.verification.email;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Map.Entry;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.usermanager.Realm;
import org.wso2.usermanager.UserManagerException;
import org.wso2.usermanager.UserStoreAdmin;
import org.wso2.usermanager.readwrite.DefaultRealm;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class EmailVerifier {

    private static Log log = LogFactory.getLog(DefaultRealm.class);

    private static Realm realm = null;
    private static EmailVerifierConfig config = null;

    public static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    public static final String PROTOCOL = "jdbc:derby:";
    public static final String CONF_STRING = "confirmation";

    private static final String ADD_USER_SQL = "insert into PENDING_USERS(username, email_address, password, confirmation) values(?, ?, ?, ?)";
    private static final String GET_USER_BY_CONF_SQL = "select * from PENDING_USERS where confirmation=?";
    private static final String GET_USER_BY_USER_SQL = "select * from PENDING_USERS where username=?";
    private static final String DELETE_USER_SQL = "delete from PENDING_USERS where username=?";
    private static final String GET_USER_PROPERTY_SQL = "select * from PENDING_USER_PROPERTIES where username=?";
    private static final String DELETE_USER_PROPERTY = "delete from PENDING_USER_PROPERTIES where username=?";
    private static final String ADD_USER_PROPERTY_SQL = "insert into PENDING_USER_PROPERTIES(username, property_name, property_value) values(?, ?, ?)";

    private static Connection dbConnection = null;

    /**
     * Configures the Email Verifier
     * 
     * @param realmInstance -
     *            The realm instance where the email verifier is going to call
     *            addUser
     * @param verifierConfig -
     *            Email Verifierification config
     */
    public static void init(Realm realmInstance,
            EmailVerifierConfig verifierConfig) throws UserManagerException {

        if ((realmInstance == null) && (verifierConfig == null)) {
            throw new RuntimeException("Invalid or null data provided");
        }

        realm = realmInstance;
        config = verifierConfig;
        createDatabase();
    }

    /**
     * Returns the user name for matching config string. This can be used to get
     * the user name at the time where user is confirmed by the email.
     * 
     * @param confString
     * @return
     * @throws UserManagerException
     */
    public String getUserName(String confString) throws UserManagerException {

        try {
            PreparedStatement getUserByConfStmt = dbConnection
                    .prepareStatement(GET_USER_BY_CONF_SQL);
            getUserByConfStmt.setString(1, confString);
            ResultSet rs = getUserByConfStmt.executeQuery();
            if (rs.next()) {
                String username = rs.getString("username");
                getUserByConfStmt.close();
                return username;
            }
        } catch (SQLException e) {
            throw new UserManagerException("error", e);
        }

        return null;
    }

    public boolean confirmUser(String confString) throws UserManagerException {
        if (realm == null) {
            throw new UserManagerException("initVerifier");
        }

        boolean isConfirmed = false;

        try {
            PreparedStatement getUserByConfStmt = dbConnection
                    .prepareStatement(GET_USER_BY_CONF_SQL);
            PreparedStatement getUserPropertyStmt = dbConnection
                    .prepareStatement(GET_USER_PROPERTY_SQL);

            PreparedStatement deleteUserStmt = dbConnection
                    .prepareStatement(DELETE_USER_SQL);
            PreparedStatement deleteUserPropertyStmt = dbConnection
                    .prepareStatement(DELETE_USER_PROPERTY);

            getUserByConfStmt.setString(1, confString);
            ResultSet rs = getUserByConfStmt.executeQuery();
            if (rs.next()) {
                String username = rs.getString("username");
                String password = rs.getString("password");
                UserStoreAdmin usAdmin = realm.getUserStoreAdmin();
                usAdmin.addUser(username, password);
                isConfirmed = true;

                getUserPropertyStmt.setString(1, username);
                ResultSet propRS = getUserPropertyStmt.executeQuery();
                Map props = new HashMap();
                while (propRS.next()) {
                    String key = propRS.getString("property_name");
                    String value = propRS.getString("property_value");
                    props.put(key, value);
                }

                usAdmin.setUserProperties(username, props);
                deleteUserPropertyStmt.setString(1, username);
                deleteUserPropertyStmt.executeUpdate();
                deleteUserStmt.setString(1, username);
                deleteUserStmt.executeUpdate();

                dbConnection.commit();

                getUserByConfStmt.close();
                getUserPropertyStmt.close();
                deleteUserStmt.close();
                deleteUserPropertyStmt.close();
            }
        } catch (SQLException e) {
            throw new UserManagerException("error", e);
        }

        return isConfirmed;
    }

    public void requestUserVerification(String username, String emailAddress,
            String password, Map properties) throws UserManagerException {
        if (config == null) {
            throw new UserManagerException("initVerifier");
        }

        if (username == null || emailAddress == null || password == null) {
            throw new UserManagerException("invalidData");
        }

        username = username.trim();
        emailAddress = emailAddress.trim();
        password = password.trim();

        if (username.length() == 0 || emailAddress.length() == 0
                || password.length() == 0) {
            throw new UserManagerException("invalidData");
        }

        if (realm.getUserStoreReader().isExistingUser(username)) {
            throw new UserManagerException("duplicateUser");
        }

        try {

            PreparedStatement getUser = dbConnection
                    .prepareStatement(GET_USER_BY_USER_SQL);
            getUser.setString(1, username);
            ResultSet rs = getUser.executeQuery();
            if (rs.next()) {
                throw new UserManagerException("duplicateUser");
            }

            PreparedStatement addUserStmt = dbConnection
                    .prepareStatement(ADD_USER_SQL);
            PreparedStatement addUserPropertyStmt = dbConnection
                    .prepareStatement(ADD_USER_PROPERTY_SQL);

            Random random = new Random();
            byte[] temp = new byte[16];
            random.nextBytes(temp);
            String confString = Base64.encode(temp);
            confString = confString.replaceAll("/", "a");
            confString = confString.replaceAll("\\+", "b");
            confString = confString.replaceAll("=", "c");
            confString = confString.trim();
            // TODO check whether user is there
            String epr = config.getRegistrationServiceEPR();
            sendEmail(confString, emailAddress, epr);
            addUserStmt.setString(1, username);
            addUserStmt.setString(2, emailAddress);
            addUserStmt.setString(3, password);
            addUserStmt.setString(4, confString);

            addUserStmt.executeUpdate();

            if (properties != null) {
                Iterator ite = properties.entrySet().iterator();

                while (ite.hasNext()) {
                    Entry entry = (Entry) ite.next();
                    String key = (String) entry.getKey();
                    String value = (String) entry.getValue();
                    addUserPropertyStmt.setString(1, username);
                    addUserPropertyStmt.setString(2, key);
                    addUserPropertyStmt.setString(3, value);
                    addUserPropertyStmt.executeUpdate();
                }

                dbConnection.commit();
                addUserStmt.close();
                addUserPropertyStmt.close();
            }
        } catch (SQLException e) {
            throw new UserManagerException("pendingAdd", e);
        }

    }

    protected void sendEmail(String confString, String emailAddr, String epr)
            throws UserManagerException {
        // TODO :: Use a java thread here

        Properties props = new Properties();
        props.put(EmailVerifierConfig.HOST, config.getHost());
        props.put(EmailVerifierConfig.PORT, Integer.toString(config.getPort()));
        Session session = Session.getDefaultInstance(props, null);

        try {
            // Construct the message
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(config.getFromAddress()));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(
                    emailAddr));
            
            if(config.getReplyTo()!= null){
                msg.setReplyTo(new Address[]{new InternetAddress(config.getReplyTo()) });
            }
            
            if (config.getSubject().length() == 0) {
                msg.setSubject(EmailVerifierConfig.DEFAULT_VALUE_SUBJECT);
            } else {
                msg.setSubject(config.getSubject());
            }

            String responseMessage = getResponseMessage(confString, epr);
            msg.setText(responseMessage);

            log.debug("Sending confirmation mail to " + emailAddr);
            log.debug("Verification url : " + responseMessage);
            // Send the message
            Transport.send(msg);
            log.debug("Sending confirmation mail to " + emailAddr + "DONE");
        } catch (AddressException e) {
            throw new UserManagerException("sendingMailProblems", e);
        } catch (MessagingException e) {
            throw new UserManagerException("sendingMailProblems", e);
        }
    }

    protected static void createDatabase() throws UserManagerException {
        try {
            Class clazz = Class.forName(DRIVER);
            Driver driver = (Driver) clazz.newInstance();

            Properties props = new Properties();

            String connectionURL = PROTOCOL
                    + "UnVerifiedUserDatabase;create=true";
            dbConnection = driver.connect(connectionURL, props);

            Statement stmt = dbConnection.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);

            DatabaseMetaData dbmd = dbConnection.getMetaData();
            ResultSet rs = dbmd.getTables(null, null, "PENDING_USERS", null);

            if (rs.next() == false) {
                log.debug("Creating a new table in the database.");
                stmt = dbConnection.createStatement(
                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_UPDATABLE);
                stmt
                        .executeUpdate("create table PENDING_USERS(username varchar(255) not null, email_address varchar(255) not null, password varchar(255) not null, confirmation varchar(255) not null unique, primary key (username))");
                stmt
                        .executeUpdate("create table PENDING_USER_PROPERTIES(username varchar(255) not null, property_name varchar(255) not null, property_value varchar(255) not null, primary key (username, property_name))");

                stmt
                        .executeUpdate("insert into PENDING_USERS(username, email_address, password, confirmation) values('admin', 'admin', 'dummyPass', 'adminConfirmation')");
                stmt
                        .executeUpdate("insert into PENDING_USERS(username, email_address, password, confirmation) values('root', 'root', 'dummyPass', 'rootConfirmation')");
                dbConnection.commit();
            } else {
                log.debug("Database and table already found.");

            }
            dbConnection.commit();
        } catch (Exception e) {
            throw new UserManagerException("verifierUserDatabaseInit", e);
        }
    }

    private String getResponseMessage(String confString, String epr) {
        String msg = null;
        if (config.getEmailBody().length() == 0) {
            msg = EmailVerifierConfig.DEFAULT_VALUE_MESSAGE + "\n" + epr + "?"
                    + CONF_STRING + "=" + confString+"\n";
        } else {
            msg = config.getEmailBody()+"\n"+ epr + "?" + CONF_STRING + "="
                    + confString+"\n";
        }
        
        if(config.getEmailFooter() != null){
            msg = msg + "\n"+ config.getEmailFooter();
        }
        return msg;
    }

}
