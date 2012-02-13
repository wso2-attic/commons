/*
 * Copyright 2005-2007 WSO2, Inc. (http://wso2.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.usermanager.custom.ldap;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.usermanager.AccessControlAdmin;
import org.wso2.usermanager.Authenticator;
import org.wso2.usermanager.Authorizer;
import org.wso2.usermanager.Realm;
import org.wso2.usermanager.UserManagerException;
import org.wso2.usermanager.UserStoreAdmin;
import org.wso2.usermanager.UserStoreReader;

/**
 * @see org.wso2.usermanager.Realm 
 */
public class LDAPRealm implements Realm {

    private static Log log = LogFactory.getLog(LDAPRealm.class);

    protected DirContext context = null;

    protected int curUserPattern = 0;

    protected int connectionAttempt = 0;

    protected LDAPRealmConfig config = null;

    protected String driverName = "com.sun.jndi.ldap.LdapCtxFactory";

    public LDAPRealm() {

    }

    public Object getRealmConfiguration() throws UserManagerException {
        LDAPRealmConfig retConfig = null;
        if (config == null) {
            retConfig = new LDAPRealmConfig();
        } else {
            retConfig = new LDAPRealmConfig(config);
        }
        return retConfig;
    }

    public void init(Object configBean) throws UserManagerException {
        try {
            if (!(configBean instanceof LDAPRealmConfig)) {
                return;
            }
            this.config = (LDAPRealmConfig) configBean;

            this.open();
        } catch (NamingException e) {
            String msg = "errorOpeningLDAP";
            log.debug(msg, e);
            throw new UserManagerException("errorOpeningLDAP", e);
        }
    }

    public Authenticator getAuthenticator() throws UserManagerException {
        LDAPAuthenticator authenticator = new LDAPAuthenticator(config, context);
        return authenticator;
    }

    public UserStoreReader getUserStoreReader() throws UserManagerException {
        LDAPUserStoreReader reader = new LDAPUserStoreReader(config, context);
        return reader;
    }

    public UserStoreAdmin getUserStoreAdmin() throws UserManagerException {
        throw new UserManagerException("actionNotSupportedByRealm");
    }

    public AccessControlAdmin getAccessControlAdmin()
            throws UserManagerException {
        throw new UserManagerException("actionNotSupportedByRealm");
    }

    public Authorizer getAuthorizer() throws UserManagerException {
        throw new UserManagerException("actionNotSupportedByRealm");
    }

    /**
     * Open (if necessary) and return a connection to the configured directory
     * server for this Realm.
     * 
     * @exception NamingException
     *                if a directory server error occurs
     */
    protected DirContext open() throws NamingException {

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

        env.put(Context.INITIAL_CONTEXT_FACTORY, driverName);

        if (config.getConnectionName() != null)
            env.put(Context.SECURITY_PRINCIPAL, config.getConnectionName());

        if (config.getConnectionPass() != null)
            env.put(Context.SECURITY_CREDENTIALS, config.getConnectionPass());

        if (config.getConnectionUrl() != null && connectionAttempt == 0)
            env.put(Context.PROVIDER_URL, config.getConnectionUrl());

        return env;

    }

    // ********************************** starting private methods
    // *******************************************

    /**
     * Given a string containing LDAP patterns for user locations (separated by
     * parentheses in a pseudo-LDAP search string format -
     * "(location1)(location2)", returns an array of those paths. Real LDAP
     * search strings are supported as well (though only the "|" "OR" type).
     * 
     * @param userPatternString -
     *            a string LDAP search paths surrounded by parentheses
     */
    protected String[] parseUserPatternString(String userPatternString) {

        if (userPatternString != null) {
            ArrayList pathList = new ArrayList();
            int startParenLoc = userPatternString.indexOf('(');
            if (startParenLoc == -1) {
                // no parens here; return whole thing
                return new String[] { userPatternString };
            }
            int startingPoint = 0;
            while (startParenLoc > -1) {
                int endParenLoc = 0;
                // weed out escaped open parens and parens enclosing the
                // whole statement (in the case of valid LDAP search
                // strings: (|(something)(somethingelse))
                while ((userPatternString.charAt(startParenLoc + 1) == '|')
                        || (startParenLoc != 0 && userPatternString
                                .charAt(startParenLoc - 1) == '\\')) {
                    startParenLoc = userPatternString.indexOf("(",
                            startParenLoc + 1);
                }
                endParenLoc = userPatternString.indexOf(")", startParenLoc + 1);
                // weed out escaped end-parens
                while (userPatternString.charAt(endParenLoc - 1) == '\\') {
                    endParenLoc = userPatternString.indexOf(")",
                            endParenLoc + 1);
                }
                String nextPathPart = userPatternString.substring(
                        startParenLoc + 1, endParenLoc);
                pathList.add(nextPathPart);
                startingPoint = endParenLoc + 1;
                startParenLoc = userPatternString.indexOf('(', startingPoint);
            }
            return (String[]) pathList.toArray(new String[] {});
        }
        return null;

    }

}
