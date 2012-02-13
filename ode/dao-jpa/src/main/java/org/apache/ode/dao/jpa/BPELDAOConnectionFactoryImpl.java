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
package org.apache.ode.dao.jpa;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ode.bpel.dao.BpelDAOConnection;
import org.apache.ode.bpel.dao.BpelDAOConnectionFactoryJDBC;
import org.apache.ode.utils.DbIsolation;
import org.apache.openjpa.ee.ManagedRuntime;
import org.apache.openjpa.util.GeneralException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.sql.DataSource;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author Matthieu Riou <mriou at apache dot org>
 */
public class BPELDAOConnectionFactoryImpl implements BpelDAOConnectionFactoryJDBC {
    static final Log __log = LogFactory.getLog(BPELDAOConnectionFactoryImpl.class);

    protected EntityManagerFactory _emf;
    private TransactionManager _tm;
    private DataSource _ds;
    private Object _dbdictionary;

    static ThreadLocal<BPELDAOConnectionImpl> _connections = new ThreadLocal<BPELDAOConnectionImpl>();

    public BPELDAOConnectionFactoryImpl() {
    }

    public BpelDAOConnection getConnection() {
        try {
            _tm.getTransaction().registerSynchronization(new Synchronization() {
                // OpenJPA allows cross-transaction entity managers, which we don't want
                public void afterCompletion(int i) {
                    if (_connections.get() != null)
                        _connections.get().getEntityManager().close();
                    _connections.set(null);
                }

                public void beforeCompletion() {
                }
            });
        } catch (RollbackException e) {
            throw new RuntimeException("Coulnd't register synchronizer!");
        } catch (SystemException e) {
            throw new RuntimeException("Coulnd't register synchronizer!");
        }
        if (_connections.get() != null) {
            return _connections.get();
        } else {
            HashMap propMap2 = new HashMap();
            propMap2.put("openjpa.TransactionMode", "managed");
            EntityManager em = _emf.createEntityManager(propMap2);
            BPELDAOConnectionImpl conn = createBPELDAOConnection(em);
            _connections.set(conn);
            return conn;
        }
    }

    protected BPELDAOConnectionImpl createBPELDAOConnection(EntityManager em) {
        return new BPELDAOConnectionImpl(em);
    }

    public void init(Properties properties) {
        HashMap<String, Object> propMap = new HashMap<String, Object>();

//        propMap.put("openjpa.Log", "DefaultLevel=TRACE");
        propMap.put("openjpa.Log", "log4j");
//        propMap.put("openjpa.jdbc.DBDictionary", "org.apache.openjpa.jdbc.sql.DerbyDictionary");

        propMap.put("openjpa.ManagedRuntime", new TxMgrProvider());
        propMap.put("openjpa.ConnectionFactory", _ds);
        propMap.put("openjpa.ConnectionFactoryMode", "managed");
        propMap.put("openjpa.FlushBeforeQueries", "false");

        if (determineDatabaseDictionary() != null)
            propMap.put("openjpa.jdbc.DBDictionary", determineDatabaseDictionary());

        if (properties != null)
            for (Map.Entry me : properties.entrySet())
                propMap.put((String) me.getKey(), me.getValue());

        _emf = Persistence.createEntityManagerFactory("ode-dao", propMap);
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
                __log.warn("Exception while closing connection", e);
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
                if (__log.isDebugEnabled())
                    __log.debug("Using database " + dbProductName + " major version " + dbMajorVer);

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
            __log.warn("Unable to determine database dialect", e);
        } finally {
            close(con);
        }
        if (__log.isDebugEnabled()){
            __log.info("Using database dialect: " + dbDictionary);
        }
        return dbDictionary;
    }


    public void setTransactionManager(TransactionManager tm) {
        _tm = tm;
    }

    public void setDataSource(DataSource datasource) {
        _ds = datasource;

    }

    public void setDBDictionary(String dbd) {
        _dbdictionary = dbd;
    }

    public void setTransactionManager(Object tm) {
        _tm = (TransactionManager) tm;

    }

    public void setUnmanagedDataSource(DataSource ds) {
    }

    public void shutdown() {
        _emf.close();
    }


    private class TxMgrProvider implements ManagedRuntime {
        public TxMgrProvider() {
        }

        public TransactionManager getTransactionManager() throws Exception {
            return _tm;
        }

        public void setRollbackOnly(Throwable cause) throws Exception {
            // there is no generic support for setting the rollback cause
            getTransactionManager().getTransaction().setRollbackOnly();
        }

        public Throwable getRollbackCause() throws Exception {
            // there is no generic support for setting the rollback cause
            return null;
        }

        public Object getTransactionKey() throws Exception, SystemException {
            return _tm.getTransaction();
        }

        public void doNonTransactionalWork(java.lang.Runnable runnable) throws NotSupportedException {
            TransactionManager tm = null;
            Transaction transaction = null;

            try {
                tm = getTransactionManager();
                transaction = tm.suspend();
            } catch (Exception e) {
                NotSupportedException nse =
                        new NotSupportedException(e.getMessage());
                nse.initCause(e);
                throw nse;
            }

            runnable.run();

            try {
                tm.resume(transaction);
            } catch (Exception e) {
                try {
                    transaction.setRollbackOnly();
                }
                catch (SystemException se2) {
                    throw new GeneralException(se2);
                }
                NotSupportedException nse =
                        new NotSupportedException(e.getMessage());
                nse.initCause(e);
                throw nse;
            }
        }
    }
}
