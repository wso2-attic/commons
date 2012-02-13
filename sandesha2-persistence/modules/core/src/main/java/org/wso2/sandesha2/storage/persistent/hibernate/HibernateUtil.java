/*
* Copyright 2005,2006 WSO2, Inc. http://wso2.com
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*
*/

package org.wso2.sandesha2.storage.persistent.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Does the hibernate configuration work including initialization.
 */
public class HibernateUtil {

	
	
	
    public static SessionFactory SESSION_FACTORY;
    public static ThreadLocal SESSION = new ThreadLocal();
    
    //Static configuration.
//	static {
//	try {
//		// Create the SessionFactory from hibernate.cfg.xml
//		SESSION_FACTORY = new Configuration().configure()
//				.buildSessionFactory();
//	} catch (Throwable ex) {
//		// Make sure you log the exception, as it might be swallowed
//		System.err.println("Initial SessionFactory creation failed." + ex);
//		throw new ExceptionInInitializerError(ex);
//	}
//	}
    
    //For dynamic configuration.
    public static void configureHibernateUtil(String dbConnIdentifier,
                           String dbDriver,
                           String sqlDialect,
                           String username,
                           String password,
                           String dbCreateMode,
                           String[] hbmXMLFilenames) {

        if (!validateRequiredParams(new Object[]{dbConnIdentifier, dbDriver,
                                                 sqlDialect, hbmXMLFilenames})) {
            throw new IllegalArgumentException("One or more of the required parameters " +
                                               "were not provided");
        }
        try {
            Configuration cfg = new Configuration();
            for (int i = 0; i < hbmXMLFilenames.length; i++) {
                cfg.addResource(hbmXMLFilenames[i]);
            }
            
            commonInit(cfg, sqlDialect, dbDriver, dbConnIdentifier, username, password,dbCreateMode);
            
            SESSION_FACTORY = cfg.buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    private static void commonInit(Configuration cfg, String sqlDialect, String dbDriver,
                            String dbConnIdentifier, String username, String password, String dbCreateMode) {

        cfg.setProperty("hibernate.dialect", sqlDialect);
        if (dbConnIdentifier.indexOf("java:comp") == 0) { // Is it a datasource JNDI?
            cfg.setProperty("hibernate.connection.datasource", dbConnIdentifier);
        } else {
            cfg.setProperty("hibernate.connection.url", dbConnIdentifier);
            if (username != null && username.trim().length() != 0) {
                cfg.setProperty("hibernate.connection.username",
                                username);
            }
            if (password != null && password.trim().length() != 0) {
                cfg.setProperty("hibernate.connection.password",
                                password);
            }
            cfg.setProperty("hibernate.connection.driver_class", dbDriver);
            cfg.setProperty("hibernate.hbm2ddl.auto", dbCreateMode);
        }
    }

    private static boolean validateRequiredParams(Object[] params) {
        if (params == null) {
            return false;
        }
        for (int i = 0; i < params.length; i++) {
            Object param = params[i];
            if (param == null) {
                return false;
            }
            if (param instanceof String && ((String) param).trim().length() == 0) {
                return false;
            }
            if (param instanceof Object[]) {
                if (!validateRequiredParams((Object[]) param)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * To get the session object for the current thread.
     * @return
     */
    public synchronized static Session currentSession() {
    	
		Session s = (Session) SESSION.get();
		// Open a new Session, if this thread has none yet
		if (s == null || !s.isOpen()) {
			s = SESSION_FACTORY.openSession();
			// Store it in the ThreadLocal variable
			SESSION.set(s);
		}
		
		return s;
    }

    /**
     * Closing the session.
     */
    public synchronized static void closeSession() {
    	
		Session s = (Session) SESSION.get();
		if (s != null)
			s.close();
		
		SESSION.set(null);
    	
    }
	
	
	
	public interface Constants {
		String CREATE_SEQ_MSG_ID = "createSeqMsgID";
		String MESSAGE_CONTEXT_REF_KEY = "messageContextRefKey";
		String SEQUENCE_ID = "sequenceID";
		String SEQUENCE_PROPERTY_KEY = "sequencePropertyKey";
		String MESSAGE_ID = "messageID";
		String INTERNAL_SEQ_ID = "internalSequenceID";
		String NAME = "name";
		String MESSAGE_STORED_KEY = "storedKey";
	}
	
}
