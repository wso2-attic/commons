package a1deployer;

import org.apache.axis.ConfigurationException;
import org.apache.axis.MessageContext;
import org.apache.axis.configuration.FileProvider;
import org.apache.axis.deployment.wsdd.WSDDService;
import org.apache.axis.description.ServiceDesc;
import org.apache.axis.server.AxisServer;
import org.apache.axis.utils.DOM2Writer;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.dataretrieval.WSDLSupplier;
import org.apache.axis2.deployment.Deployer;
import org.apache.axis2.deployment.DeploymentErrorMsgs;
import org.apache.axis2.deployment.DeploymentException;
import org.apache.axis2.deployment.repository.util.DeploymentFileData;
import org.apache.axis2.deployment.util.PhasesInfo;
import org.apache.axis2.deployment.util.Utils;
import org.apache.axis2.description.*;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.i18n.Messages;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.wsdl.Definition;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.xml.namespace.QName;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

/*
* Copyright 2007 The Apache Software Foundation.
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
*/

/**
 * This is a Deployer which adapts Axis1 services to Axis2.  It works by embedding an instance of
 * Axis1's AxisEngine, and building proxy Axis2 services for each Axis1 service in a WSDD file. Each
 * of those Axis2 services uses the Axis1InOutMessageReceiver, which handles the runtime work of
 * pulling in a message from Axis2 (after any Modules have run) and pushing it into Axis1 via the
 * Local transport.
 * <p/>
 * Please contact Glen Daniels (gdaniels@wso2.com) for any questions.
 */
public class Axis1Deployer implements Deployer {

    public class Axis1ParameterObserver implements ParameterObserver {
        ServiceDesc serviceDesc;

        public Axis1ParameterObserver(ServiceDesc serviceDesc) {
            this.serviceDesc = serviceDesc;
        }

        public void parameterChanged(String name, Object value) {
            serviceDesc.setProperty(name, value);
        }
    }

    protected static final Log log = LogFactory.getLog(Axis1Deployer.class);

    public static class A1WSDLSupplier implements WSDLSupplier {
        AxisServer server;

        public A1WSDLSupplier(AxisServer server) {
            this.server = server;
        }

        public Definition getWSDL(AxisService service) throws AxisFault {
            Definition def = (Definition)service.getParameterValue("WSDLDefinition");
            if (def != null) return def;

            org.apache.axis.MessageContext mc = new MessageContext(server);
            try {
                mc.setTargetService(service.getName());
                String addr = service.getEPRs()[0];
                mc.setProperty(MessageContext.TRANS_URL, addr);
                server.generateWSDL(mc);
                Document wsdlDoc = (Document)mc.getProperty("WSDL");

                // Argh - I wish we didn't have to do this, but apparently
                // there's a WSDL4J bug reading DOM WSDLs, so serialize...
                String wsdlString = DOM2Writer.nodeToString(wsdlDoc, true);
                ByteArrayInputStream bis = new ByteArrayInputStream(wsdlString.getBytes());
                WSDLReader reader = WSDLFactory.newInstance().newWSDLReader();
                def = reader.readWSDL(null, new InputSource(bis));

                // And cache it.
                service.addParameter("WSDLDefinition", def);
                return def;
            } catch (Exception e) {
                log.error(e);
                throw AxisFault.makeFault(e);
            }
        }
    }

    ConfigurationContext configCtx;

    static final String REPLACEME = "http://ws.apache.org/axis";

    public Axis1Deployer() {
    }

    public void init(ConfigurationContext configCtx) {
        this.configCtx = configCtx;
    }

    public void deploy(DeploymentFileData deploymentFileData) throws DeploymentException {
        log.info("Deploying - " + deploymentFileData.getName());
        ClassLoader threadClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            File file = deploymentFileData.getFile();
            File parentFile = file.getParentFile();
            ClassLoader classLoader =
                    Utils.getClassLoader(configCtx.getAxisConfiguration().getSystemClassLoader(),
                                         parentFile);
            Thread.currentThread().setContextClassLoader(classLoader);
            FileProvider config = new FileProvider(deploymentFileData.getAbsolutePath());
            AxisServer server = new AxisServer(config);
            WSDLSupplier supplier = new A1WSDLSupplier(server);

            config.getDeployedServices(); // ensure everything's set up

            WSDDService[] services = config.getDeployment().getServices();
            AxisServiceGroup serviceGroup = new AxisServiceGroup();
            serviceGroup.addParameter("service.axis1.server", server);
            for (int i = 0; i < services.length; i++) {
                ServiceDesc service = services[i].getServiceDesc();
                log.info("Deploying Axis1 service -- " + service.getName());

                AxisService axisService;

                org.apache.axis.MessageContext mc = new MessageContext(server);

                String wsdlString;
                try {
                    mc.setTargetService(service.getName());
                    mc.setProperty(MessageContext.TRANS_URL, REPLACEME);
                    server.generateWSDL(mc);
                    Document wsdlDoc = (Document)mc.getProperty("WSDL");
                    wsdlString = DOM2Writer.nodeToString(wsdlDoc, true);
                } catch (Exception e) {
                    log.error(e);
                    continue;
                }

                try {
                    ByteArrayInputStream bis = new ByteArrayInputStream(wsdlString.getBytes());
                    WSDL11ToAxisServiceBuilder builder = new WSDL11ToAxisServiceBuilder(bis);
                    axisService = builder.populateService();

                    axisService.getEndpoints().clear();
                    axisService.setBindingName(null);
                    axisService.setEndpointName(null);
                } catch (Exception e) {
                    String serviceName = service.getName();
                    if (REPLACEME.equals(service.getDefaultNamespace())) {
                        service.setDefaultNamespace(null);
                    }
                    log.info("Couldn't process WSDL for Axis1 service '" + serviceName +
                             "', defaulting to passthrough mode.");

                    // Couldn't process WSDL (RPC/enc?), so set up passthrough
                    axisService = new AxisService(serviceName);
                    // Ensure dispatch works.
                    axisService.addParameter("supportSingleOperation", Boolean.TRUE);
                    // Add WSDL supplier
                    axisService.addParameter("WSDLSupplier", supplier);

                    AxisOperation op = new InOutAxisOperation(new QName("invokeAxis1Service"));
                    op.setDocumentation("This operation is a 'passthrough' for all operations in " +
                                        "an RPC/encoded Axis1 service.");
                    axisService.addOperation(op);
                }
                axisService.setName(service.getName());
                axisService.setClassLoader(classLoader);

                // Put all the parameters from the A1 service into the A2 service
                Hashtable params = services[i].getParametersTable();
                Enumeration paramKeys = params.keys();
                while (paramKeys.hasMoreElements()) {
                    String key = (String)paramKeys.nextElement();
                    axisService.addParameter(key, params.get(key));
                }

                Axis1InOutMessageReceiver receiver = new Axis1InOutMessageReceiver();
                AxisConfiguration axisConfig = configCtx.getAxisConfiguration();
                PhasesInfo phaseInfo = axisConfig.getPhasesInfo();
                for (Iterator ops = axisService.getOperations(); ops.hasNext();) {
                    AxisOperation op = (AxisOperation)ops.next();
                    op.setMessageReceiver(receiver);
                    phaseInfo.setOperationPhases(op);
                }
//                axisService.addMessageReceiver(WSDL2Constants.MEP_URI_IN_OUT,
//                                               new Axis1InOutMessageReceiver());

                // Add service type
                axisService.addParameter("serviceType", "axis1_service");
                axisService.setFileName(deploymentFileData.getFile().toURL());
                axisService.addParameterObserver(new Axis1ParameterObserver(service));
                serviceGroup.addService(axisService);
            }
            serviceGroup.setServiceGroupName(deploymentFileData.getName());
            configCtx.getAxisConfiguration().addServiceGroup(serviceGroup);
        } catch (ConfigurationException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            // If we get here, we couldn't start the AxisServer, so create a faulty service
            configCtx.getAxisConfiguration().getFaultyServices().put(
                    deploymentFileData.getFile().getAbsolutePath(),
                    sw.getBuffer().toString());
            throw new DeploymentException(e);
        } catch (AxisFault e) {
            throw new DeploymentException(e);
        } catch (MalformedURLException e) {
            throw new DeploymentException(e);
        } finally {
            if (threadClassLoader != null) {
                Thread.currentThread().setContextClassLoader(threadClassLoader);
            }
        }
    }

    public void setDirectory(String directory) {
    }

    public void setExtension(String extension) {
    }

    public void unDeploy(String fileName) throws DeploymentException {
        log.info("Undeploying - " + fileName);
        AxisConfiguration axisConfig = configCtx.getAxisConfiguration();
        fileName = Utils.getShortFileName(fileName);
        AxisServiceGroup asg = axisConfig.getServiceGroup(fileName);
        try {
            if (asg != null) {
                axisConfig.removeServiceGroup(fileName);
                configCtx.removeServiceGroupContext(asg);
                log.info(Messages.getMessage(DeploymentErrorMsgs.SERVICE_REMOVED,
                                             fileName));
            } else {
                axisConfig.removeFaultyService(fileName);
            }
        } catch (AxisFault axisFault) {
            //May be a faulty service
            axisConfig.removeFaultyService(fileName);
            throw new DeploymentException(axisFault);
        }
    }
    
    private void adjust(AxisService axisService) {
    	
    }
}
