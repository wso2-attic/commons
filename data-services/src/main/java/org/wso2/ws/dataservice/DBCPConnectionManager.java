package org.wso2.ws.dataservice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.wso2.ws.dataservice.beans.Config;

public class DBCPConnectionManager {
	private static final Log log = LogFactory.getLog(DBCPConnectionManager.class);
	private DataSource datasource = null;
	private GenericObjectPool pool = null;
	

	public DataSource getDatasource() {
		return datasource;
	}
	public void setDatasource(DataSource value) {
		datasource = value;
	}

	/**
	 * @param config - contains configuration parameters to create connection pool
	 */
	public DBCPConnectionManager(Config config) {
		try {
			connectToDB(config);
		} catch (Exception e) {
			log.error("Error occured connecting to database " +
					"using connection pooling manager", e);
		}
	}


	protected void finalize() {
		try {
			super.finalize();
		} catch (Throwable e) {
			log.error("Error occured when finalizing.", e);
		}
	}

	/**
	 *  @param config - contains connection parameters such as JDBC URL,username,password,etc
	 *  
	 */
	private void connectToDB(Config config) {
		String driverClass = config.getPropertyValue(DBConstants.DRIVER);
		String jdbcURL = config.getPropertyValue(DBConstants.PROTOCOL);
		String userName = config.getPropertyValue(DBConstants.USER);
		String password = config.getPropertyValue(DBConstants.PASSWORD);
		String minPool = config.getPropertyValue(DBConstants.MIN_POOL_SIZE);
		String maxPool = config.getPropertyValue(DBConstants.MAX_POOL_SIZE);

		// set numeric values for min & max pool sizes
		int minPoolSize = 1; // default values
		int maxPoolSize = 5; // default values
		try {
			if (minPool != null && minPool.trim().length() > 0) {
				minPoolSize = Integer.valueOf(minPool).intValue();
			}
			if (maxPool != null && maxPool.trim().length() > 0) {
				maxPoolSize = Integer.valueOf(maxPool).intValue();
			}
		} catch (NumberFormatException e) {
			log.error("Non-numeric value found for max/min pool size", e);
		}

		try {
			java.lang.Class.forName(driverClass).newInstance();
		} catch (ClassNotFoundException e) {
			log.error("Error locating class "+driverClass, e);
		} catch (InstantiationException e) {
			log.error("Error instantiating class "+driverClass, e);
		} catch (IllegalAccessException e) {
			log.error("Illegal access to class "+driverClass, e);
		}

		try {
			datasource = setupDataSource(jdbcURL,
					userName, password, minPoolSize, maxPoolSize);
		} catch (Exception e) {
			log.error("Error occured while creating datasource.", e);
		}
	}

	/**
	 * Create dataSource object using given parameters
	 */
	public DataSource setupDataSource(String connectionURL,
			String username, String password, int minIdle, int maxActive)
			throws Exception {
		GenericObjectPool connectionPool = new GenericObjectPool(null);
		connectionPool.setMinIdle(minIdle);
		connectionPool.setMaxActive(maxActive);
		connectionPool.setMaxWait(1000*60);

		pool = connectionPool;
		ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(
				connectionURL, username, password);
		PoolableConnectionFactory factory = new PoolableConnectionFactory(
				connectionFactory, connectionPool, null, null, false, true);
		
		pool.setFactory(factory);
		PoolingDataSource dataSource = new PoolingDataSource(connectionPool);
		dataSource.setAccessToUnderlyingConnectionAllowed(true);
		return dataSource;
	}

	public void printDriverStats(){
		ObjectPool connectionPool = pool;
		log.info("NumActive: " + connectionPool.getNumActive());
		log.info("NumIdle: " + connectionPool.getNumIdle());
	}

	/**
	 *  getLockedProcessCount - gets the number of currently locked processes 
	 *  @return Number of locked processes
	 */
	public int getLockedProcessCount() {
		int num_locked_connections = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = datasource.getConnection();
			pstmt = con.prepareStatement("SHOW PROCESSLIST");
			rs = pstmt.executeQuery();
			while (rs.next()) {
				if (rs.getString("State") != null
						&& rs.getString("State").equals("Locked")) {
					num_locked_connections++;
				}
			}
			log.info("Number of locked connections : "+num_locked_connections);
		} catch (Exception e) {
			log.error("Error occured while getting lockedProcessCount",e);
		} finally {
			try {
				rs.close();
				pstmt.close();
				con.close();
			} catch (java.sql.SQLException e) {
				log.error("Error closing connection.",e);
			}
		}
		return num_locked_connections;
	}
}