package org.wso2.usermanager.custom.ldap;

import java.text.MessageFormat;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.usermanager.Authenticator;
import org.wso2.usermanager.UserManagerException;
import org.wso2.usermanager.i18n.Messages;

/**
 * 
 * @see org.wso2.usermanager.Authenticator
 *
 */
public class LDAPAuthenticator implements Authenticator{
    
    private static Messages messages = Messages.getInstance();
    private static Log log = LogFactory.getLog(LDAPAuthenticator.class);
    private DirContext context = null;
    private LDAPRealmConfig config = null;
    
    public LDAPAuthenticator(LDAPRealmConfig config, DirContext context){
        this.config = config;
        this.context = context;
    }

    /**
     * @see org.wso2.usermanager.Authenticator#authenticate(String, Object)
     */
    public boolean authenticate(String userName, Object credentials) throws UserManagerException {
        boolean bValue = false;

        if (!(credentials instanceof String)) {
            throw new UserManagerException(
                    "Can handle onlyl string type credentials");
        } else {
            try {

                // String dn = userPatternFormatArray[curUserPattern]
                // .format(new String[] { (String) credentials });

                String dn = MessageFormat.format(config.getUserPattern(),
                        new String[] { (String) userName });
                bValue = this
                        .bindAsUser(this.context, dn, (String) credentials);

            } catch (NamingException e) {
                log.debug(messages.getMessage("exceptionOnAuthenticate"), e);
                throw new UserManagerException("exceptionOnAuthenticate", e);
            }
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
            String credentials) throws NamingException, AuthenticationException {

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
            context.getAttributes("", null);
            validated = true;
        } catch (AuthenticationException e) {
            String msg = messages
                    .getMessage("bindFailedBecauseAuthenticationException");
            log.debug(msg, e);

        } finally {
            
            
                       // restore default
            if (config.getConnectionName() != null) {
                context.addToEnvironment(Context.SECURITY_PRINCIPAL, config
                        .getConnectionName());
            } else {
                context.removeFromEnvironment(Context.SECURITY_PRINCIPAL);
            }

            if (config.getConnectionPass() != null) {
                context.addToEnvironment(Context.SECURITY_CREDENTIALS, config
                        .getConnectionPass());
            } else {
                context.removeFromEnvironment(Context.SECURITY_CREDENTIALS);
            }
           
        }
        return validated;
    }
    

}
