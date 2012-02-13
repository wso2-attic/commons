package org.wso2.ws.dataservice;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.TimerTask;

import javax.sql.DataSource;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.deployment.repository.util.DeploymentFileData;
import org.apache.axis2.description.AxisService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.ws.dataservice.beans.Config;

public class FaultyServiceRectifier extends TimerTask {
	private static final Log log = LogFactory.getLog(FaultyServiceRectifier.class);
	private ConfigurationContext configurationCtx;
	private Config config;
	private DeploymentFileData deploymentFileData;

	public FaultyServiceRectifier(AxisService service,DeploymentFileData deploymentData
			,ConfigurationContext configCtx, Config dataServiceConfigSection) {
		deploymentFileData = deploymentData;
		configurationCtx = configCtx;		
		config = dataServiceConfigSection;
	}

	public boolean cancel() {
		return super.cancel();
	}

	public void run() {
		DataSource dataSource;
		String deploymentFileName = "";
		try {
			deploymentFileName = deploymentFileData.getFile().getAbsolutePath();
			log.info("Trying to re-establish fautly database connection " +
					"for data services :"+deploymentFileName);
			DBCPConnectionManager dbcpConnectionManager = 
				DBUtils.initializeDBConnectionManager(deploymentFileName, config);
			dataSource = dbcpConnectionManager.getDatasource();
			if (dataSource != null) {
				Connection connection = dataSource.getConnection();
				log.info("Database connection successful. Removing "
						+ deploymentFileName + " from Fault Service list.");
				configurationCtx.getAxisConfiguration().getFaultyServices()
						.remove(deploymentFileName);
				//cancel the timer task				
				this.cancel();
				//send the dataservice configuration through re-deployement
				DBDeployer dbDeployer = new DBDeployer();
				dbDeployer.init(configurationCtx);
				dbDeployer.deploy(deploymentFileData);
			} else {
				log.error("Database connection re-establishment for "
						+ deploymentFileName + " failed.Retrying...");
			}
		} catch (AxisFault e) {
			log	.error("Error occurred while trying to re-establish database connection for "
					+deploymentFileName+".Retrying....",e);
		} catch (SQLException e) {
			log	.error("Error occurred while trying to re-establish database connection for "
					+deploymentFileName+".Retrying....",e);
		} 
	}

}
