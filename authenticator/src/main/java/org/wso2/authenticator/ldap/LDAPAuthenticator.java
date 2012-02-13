package org.wso2.authenticator.ldap;

import java.text.MessageFormat;
import java.util.Hashtable;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.authenticator.Authenticator;
import org.wso2.authenticator.AuthenticatorException;
import org.wso2.authenticator.help.UserHelpInfo;

/**
 * 
 * @see org.wso2.usermanager.Authenticator
 *
 */
public class LDAPAuthenticator implements Authenticator {

    private static Log log = LogFactory.getLog(LDAPAuthenticator.class);
    private DirContext context = null;

    private String connectionName = null;

    private String connectionPass = null;

    private String userPattern = null;

    private String driverName = null;

    private String connectionUrl = null;
    
    public LDAPAuthenticator() {

    }

    /**
     * @see org.wso2.usermanager.Authenticator#authenticate(String, Object)
     */
    public boolean authenticate(String userName, Object credentials)
            throws AuthenticatorException {
        boolean bValue = false;
        try {
            open();
            if (!(credentials instanceof String)) {
                throw new AuthenticatorException(
                        "Can handle onlyl string type credentials");
            } else {

                // String dn = userPatternFormatArray[curUserPattern]
                // .format(new String[] { (String) credentials });

                String dn = MessageFormat.format(userPattern,
                        new String[] { (String) userName });
                bValue = this
                        .bindAsUser(this.context, dn, (String) credentials);

            }
        } catch (NamingException e) {
            log.debug("Unable to authenticate", e);
            throw new AuthenticatorException("exceptionOnAuthenticate", e);
        }
        return bValue;
    }

    /**
     * Check credentials by binding to the directory as the user
     * 
     * @param context
     *            The directory context
     * @param user
     *            The User to be authenticated
     * @param credentials
     *            Authentication credentials
     * 
     * @exception NamingException
     *                if a directory server error occurs
     */
    protected boolean bindAsUser(DirContext context, String dn,
            String credentials) throws NamingException, AuthenticatorException {
        Attributes attr;

        if (credentials == null || dn == null)
            return (false);

        // Validate the credentials specified by the user
        if (log.isTraceEnabled()) {
            log.trace("validatingCredentials");
        }

        // Set up security environment to bind as the user
        context.addToEnvironment(Context.SECURITY_PRINCIPAL, dn);
        context.addToEnvironment(Context.SECURITY_CREDENTIALS, credentials);

        // Elicit an LDAP bind operation
        boolean validated = false;

        try {
            if (log.isTraceEnabled()) {
                log.trace("binding as " + dn);
            }
            attr = context.getAttributes("", null);
            validated = true;
        } catch (AuthenticationException e) {
            log.debug(e.getMessage(), e);
            throw new AuthenticatorException(e);
        } finally {

            // restore default
            if (connectionName != null) {
                context.addToEnvironment(Context.SECURITY_PRINCIPAL,
                        connectionName);
            } else {
                context.removeFromEnvironment(Context.SECURITY_PRINCIPAL);
            }

            if (connectionPass != null) {
                context.addToEnvironment(Context.SECURITY_CREDENTIALS,
                        connectionPass);
            } else {
                context.removeFromEnvironment(Context.SECURITY_CREDENTIALS);
            }

        }
        return validated;
    }

    public DirContext open() throws NamingException {

        if (context != null)
            return (context);
        try {
            context = new InitialDirContext(getDirectoryContextEnvironment());
        } catch (NamingException e) {
            //try once again
            context = new InitialDirContext(getDirectoryContextEnvironment());
        } finally {

        }
        return (context);

    }

    protected Hashtable getDirectoryContextEnvironment() {

        Hashtable env = new Hashtable();

        if(driverName == null || driverName.trim().length()==0){
            driverName = "com.sun.jndi.ldap.LdapCtxFactory";
        }
        
        env.put(Context.INITIAL_CONTEXT_FACTORY, driverName);

        if (connectionName != null)
            env.put(Context.SECURITY_PRINCIPAL, connectionName);

        if (connectionPass != null)
            env.put(Context.SECURITY_CREDENTIALS, connectionPass);

        if (connectionUrl != null)
            env.put(Context.PROVIDER_URL, connectionUrl);

        return env;

    }
    
    @UserHelpInfo(isRequired = true, getHelpText = "e.g. cn=root,dc=wso2,dc=com",
            getInputType = "text", getLabel= "Connection Name")
    public void setConnectionName(String connectionName) {
        this.connectionName = connectionName;
    }
    @UserHelpInfo(isRequired = true, getHelpText = "Password of the connection user name",
            getInputType = "password", getLabel= "Connection Password")
    public void setConnectionPass(String connectionPass) {
        this.connectionPass = connectionPass;
    }
    @UserHelpInfo(isRequired = true, getHelpText = "e.g. uid={0},dc=wso2,dc=com",
            getInputType = "text", getLabel= "User Pattern")
    public void setUserPattern(String userPattern) {
        this.userPattern = userPattern;
    }
    
    @UserHelpInfo(isRequired = false, getHelpText ="com.sun.jndi.ldap.LdapCtxFactory",
            getInputType = "text", getLabel= "Driver name")
    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }
    
    @UserHelpInfo(isRequired = true, getHelpText = "e.g. ldap://localhost:389",
            getInputType = "text", getLabel= "Connection URL")
    public void setConnectionUrl(String connectionUrl) {
        this.connectionUrl = connectionUrl;
    }
    
    

}
