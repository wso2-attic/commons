/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.ode.store.jpa;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ode.store.ConfStoreConnection;
import org.apache.ode.store.ConfStoreConnectionFactory;
import org.apache.ode.utils.DbIsolation;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * @author Matthieu Riou <mriou at apache dot org>
 */
public class DbConfStoreConnectionFactory implements ConfStoreConnectionFactory {
    private static Log log = LogFactory.getLog(DbConfStoreConnectionFactory.class);

    private DataSource _ds;
    private EntityManagerFactory _emf;

    public DbConfStoreConnectionFactory(DataSource ds, boolean createDatamodel) {
        _ds = ds;
        HashMap propMap = new HashMap();
        propMap.put("javax.persistence.nonJtaDataSource", ds);
        propMap.put("openjpa.Log", "log4j");
        if (determineDatabaseDictionary() != null) {
            propMap.put("openjpa.jdbc.DBDictionary", determineDatabaseDictionary());
        }

        if (createDatamodel) propMap.put("openjpa.jdbc.SynchronizeMappings", "buildSchema(ForeignKeys=false)");
        _emf = Persistence.createEntityManagerFactory("ode-store", propMap);
    }

    public ConfStoreConnection getConnection() {
        return new ConfStoreConnectionJpa(_emf.createEntityManager());
    }

    private Connection getDBConnection() throws SQLException {
        Connection c = _ds.getConnection();
        DbIsolation.setIsolationLevel(c);
        return c;
    }

    private void close(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (Exception e) {
                log.warn("Exception while closing connection", e);
            }
        }
    }

    private String determineDatabaseDictionary() {
        String dbDictionary = null;
        Connection con = null;
        try {
            con = getDBConnection();
            DatabaseMetaData metaData = con.getMetaData();
            if (metaData != null) {
                String dbProductName = metaData.getDatabaseProductName().toLowerCase();
                int dbMajorVer = metaData.getDatabaseMajorVersion();
                if (log.isDebugEnabled())
                    log.debug("Using database " + dbProductName + " major version " + dbMajorVer);

                if (dbProductName.indexOf("db2") >= 0) {
                    dbDictionary = "db2";
                } else if (dbProductName.indexOf("oracle") >= 0) {
                    dbDictionary = "oracle";
                } else if (dbProductName.indexOf("derby") >= 0) {
                    dbDictionary = "derby";
                } else if (dbProductName.indexOf("hsql") >= 0) {
                    dbDictionary = "hsql(SimulateLocking=true)";
                } else if (dbProductName.indexOf("microsoft sql") >= 0) {
                    dbDictionary = "sqlserver";
                } else if (dbProductName.indexOf("mysql") >= 0) {
                    dbDictionary = "mysql";
                } else if (dbProductName.indexOf("sybase") >= 0) {
                    dbDictionary = "sybase";
                } else if (dbProductName.indexOf("h2") >= 0) {
                    dbDictionary = "h2";
                }
            }
        } catch (SQLException e) {
            log.warn("Unable to determine database dialect", e);
        } finally {
            close(con);
        }
        if (log.isDebugEnabled()) {
            log.info("Using database dialect: " + dbDictionary);
        }
        return dbDictionary;
    }


}
