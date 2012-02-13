package org.wso2.bpel;

import org.apache.axis2.deployment.Deployer;
import org.apache.axis2.deployment.DeploymentException;
import org.apache.axis2.deployment.util.Utils;
import org.apache.axis2.deployment.repository.util.DeploymentFileData;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.transport.http.HTTPConstants;
import javax.servlet.ServletContext;
import org.apache.ode.store.ProcessStoreImpl;
import javax.servlet.ServletException;
import java.io.File;


public class BPELDeployer implements Deployer {

    private ConfigurationContext configCtx;
    private ProcessStoreImpl store;

    public void init(ConfigurationContext configCtx) {
        this.configCtx = configCtx;
        try {
            AxisConfiguration configuration = configCtx.getAxisConfiguration();
            Axis2BasedODEServer server = new Axis2BasedODEServer();
            ServletContext servletCtx = (ServletContext)configCtx.getProperty(HTTPConstants.MC_HTTP_SERVLETCONTEXT);
            server.init(servletCtx.getRealPath("/WEB-INF"), configuration);
            store = server.getProcessStore();
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }

    public void deploy(DeploymentFileData deploymentFileData) throws DeploymentException {
        File file = deploymentFileData.getFile();
        boolean isDeployed = Util.unzip(file.getPath());
        if (!isDeployed) {
            return;
        }
        ClassLoader threadClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            File parentFile = file.getParentFile();
            ClassLoader classLoader =
                    Utils.getClassLoader(configCtx.getAxisConfiguration().
                            getSystemClassLoader(), parentFile);
            Thread.currentThread().setContextClassLoader(classLoader);
            String fullPath = file.getAbsolutePath();
            int index = fullPath.lastIndexOf(".");
            fullPath = fullPath.substring(0, index);
            store.deploy(new File(fullPath));

        } catch (DeploymentException e) {
        } finally {
            Thread.currentThread().setContextClassLoader(threadClassLoader);
        }
    }

    public void setDirectory(String directory) {

    }

    public void setExtension(String extension) {

    }

    public void unDeploy(String fileName) throws DeploymentException {

    }
}
