package org.wso2.usermanager.custom.ldap;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.usermanager.UserManagerException;
import org.wso2.usermanager.UserStoreReader;
import org.wso2.usermanager.i18n.Messages;

/**
 * @see org.wso2.usermanager.UserStoreReader
 */
public class LDAPUserStoreReader implements UserStoreReader {

    private DirContext context = null;

    private LDAPRealmConfig config = null;

    private static Messages messages = Messages.getInstance();

    private static Log log = LogFactory.getLog(LDAPAuthenticator.class);

    public LDAPUserStoreReader(LDAPRealmConfig config, DirContext context) {
        this.config = config;
        this.context = context;
    }

    /**
     * Retrieves attribute names
     * @see org.wso2.usermanager.UserStoreReader#getUserProperties(String)
     */
    public Map getUserProperties(String userName) throws UserManagerException {
        log.debug(messages.getMessage("getProperties"));
        
        String[] tempIds = config.getAttributeIds().split(",");
        String[] attrIds = new String[tempIds.length];
        for(int i=0; i<tempIds.length;i++ ){
            attrIds[i] = tempIds[i].trim();
        }

        Map properties = new HashMap();

        try {

            Attributes results = null;
            // Use pattern or search for user entry
            if (config.getUserPattern() != null) {
                results = getGenericUserByPattern(userName, attrIds);
            }

            // else {
            // results = getGenericUserBySearch(userName, attrIds);
            // }

            if (results != null) {
                NamingEnumeration names = results.getAll();
                while (names.hasMoreElements()) {
                    Attribute attr = (Attribute) names.nextElement();
                    properties.put(attr.getID(), attr.get().toString());
                }
            }

        } catch (NamingException e) {
            log.debug(messages.getMessage("errorReadingFromUserStore"), e);
            throw new UserManagerException("errorReadingFromUserStore", e);
        }

        return properties;

    }

    /**
     * Gets All UserNames
     * @see org.wso2.usermanager.UserStoreReader#getAllUserNames()
     */
    public String[] getAllUserNames() throws UserManagerException {
        try {
            NamingEnumeration answer = context
                    .list(config.getUserContextName());
            List lst = new ArrayList();
            while (answer.hasMore()) {
                Object obj = answer.nextElement();
                lst.add(obj.toString());
            }

            String[] wholeNames = (String[]) lst
                    .toArray(new String[lst.size()]);
            String[] names = new String[lst.size()];
            for (int i = 0; i < wholeNames.length; i++) {
                String[] temp1 = wholeNames[i].split(":");
                if (temp1.length > 0) {
                    String[] temp2 = temp1[0].split("=");
                    if (temp2.length > 1) {
                        names[i] = temp2[1].trim();
                    } else {
                        throw new UserManagerException(
                                "unknownUserNameFormatInLDAP");
                    }
                } else {
                    throw new UserManagerException(
                            "unknownUserNameFormatInLDAP");
                }
            }
            return names;
        } catch (NamingException e) {
            throw new UserManagerException("errorReadingFromUserStore", e);
        }
    }

    
    public Map getUserProfileProperties(String username, String profileName)
            throws UserManagerException {
        throw new UserManagerException("actionNotSupportedByRealm");
    }

    public String[] getUsersInRole(String roleName) throws UserManagerException {
        throw new UserManagerException("actionNotSupportedByRealm");
    }

    public String[] getUserPropertyNames() throws UserManagerException {
        String[] attrIds = config.getAttributeIds().split(",");
        return attrIds;
    }

    public boolean isExistingUser(String userName) throws UserManagerException {
        throw new UserManagerException("actionNotSupportedByRealm");
    }
    
    public boolean isExistingRole(String roleName) throws UserManagerException{
        throw new UserManagerException("actionNotSupportedByRealm");
    }

    public String[] getUserRoles(String userName) throws UserManagerException {
        throw new UserManagerException("actionNotSupportedByRealm");
    }

    public String[] getAllRoleNames() throws UserManagerException {
        throw new UserManagerException("actionNotSupportedByRealm");
    }

    public Map getRoleProperties(String roleName) throws UserManagerException {
        throw new UserManagerException("actionNotSupportedByRealm");
    }

    public String[] getUserNamesWithPropertyValue(String propertyName,
            String propetyValue) throws UserManagerException {
        throw new UserManagerException("actionNotSupportedByRealm");
    }

    /**
     * Use the <code>UserPattern</code> configuration attribute to locate the
     * directory entry for the user with the specified username and return a
     * User object; otherwise return <code>null</code>.
     * 
     * @param context
     *            The directory context
     * @param username
     *            The username
     * @param attrIds
     *            String[]containing names of attributes to retrieve.
     * 
     * @exception NamingException
     *                if a directory server error occurs
     */
    protected Attributes getGenericUserByPattern(String userName,
            String[] attrIds) throws NamingException, UserManagerException {

        log.info("getGenericUserByPattern");

        if (userName == null)
            return (null);

        // Form the dn from the user pattern
        String dn = MessageFormat.format(config.getUserPattern(),
                new String[] { userName });

        // Get required attributes from user entry
        Attributes attrs = null;

        attrs = context.getAttributes(dn, attrIds);

        return attrs;

    }

}
