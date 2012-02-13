package org.wso2.automation.tools.soapui.test;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import tools.soapui.core.SoapUICore;
import tools.soapui.core.SoapUIProperties;
import org.wso2.carbon.system.test.core.TestTemplate;

public class soapuiTest  extends TestTemplate {
    @Override
    public void init() {
              testClassName = soapuiTest.class.getName();
    }

    @Override
    public void runSuccessCase() {
                SoapUICore mojo = new SoapUICore();
         SoapUIProperties properties = new SoapUIProperties();
        properties.setProject("/home/dharshana/soapui/AS-Simple-test-soapui-project.xml", "/home/dharshana/soapui");
        try {
            mojo.execute();
        } catch (MojoExecutionException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (MojoFailureException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void cleanup() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
