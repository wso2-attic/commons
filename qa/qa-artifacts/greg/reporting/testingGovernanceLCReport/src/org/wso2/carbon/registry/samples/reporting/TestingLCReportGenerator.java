package org.wso2.carbon.registry.samples.reporting;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.governance.api.generic.GenericArtifactManager;
import org.wso2.carbon.governance.api.generic.dataobjects.GenericArtifact;
import org.wso2.carbon.governance.api.util.GovernanceUtils;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.core.session.CurrentSession;
import org.wso2.carbon.registry.reporting.AbstractReportGenerator;
import org.wso2.carbon.registry.reporting.annotation.Property;
import org.wso2.carbon.reporting.api.ReportingException;
import org.wso2.carbon.reporting.util.JasperPrintProvider;
import org.wso2.carbon.reporting.util.ReportParamMap;
import org.wso2.carbon.reporting.util.ReportStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class TestingLCReportGenerator extends AbstractReportGenerator{
    private Log log = LogFactory.getLog(TestingLCReportGenerator.class);
    public String responsibleQA;
    public String responsibleQAA;
    @Property(mandatory=false)
    public void setResponsibleQA(String responsibleQA) {
        this.responsibleQA = responsibleQA;
    }

    @Property(mandatory=true)
    public void setResponsibleQAA(String responsibleQAA) {
        this.responsibleQAA = responsibleQAA;
    }

    public ByteArrayOutputStream execute(String template, String type)
            throws IOException {
        Registry governanceRegistry;
        try {
            Registry registry = getRegistry();
            governanceRegistry = GovernanceUtils.getGovernanceUserRegistry(registry,CurrentSession.getUser());

            GenericArtifactManager manager = new GenericArtifactManager(governanceRegistry, "testGovernance");
            GenericArtifact[] genericArtifacts = manager.getAllGenericArtifacts();

            List<ProjectReportBean> beanList = new LinkedList<ProjectReportBean>();
            for(GenericArtifact artifact : genericArtifacts){
                ProjectReportBean bean = new ProjectReportBean();
                String[] attributeKeys = artifact.getAttributeKeys();
                for(String key : attributeKeys){
                    String value = artifact.getAttribute(key);
                    if (key.equals("details_govCycleName")) {
                        bean.setDetails_govCycleName(value);
                    } else if (key.equals("details_product")) {
                        bean.setDetails_product(value);
                    } else if (key.equals("details_version")) {
                        bean.setDetails_version(value);
                    }  else if (key.equals("details_comments")) {
                        bean.setDetails_comments(value);
                    }
                }
                beanList.add(bean);
            }

            String templateContent = new String((byte []) registry.get(template).getContent());

            JRDataSource dataSource = new JRBeanCollectionDataSource(beanList);
            JasperPrint print = new JasperPrintProvider().createJasperPrint(dataSource, templateContent,
                    new ReportParamMap[0]);
            return new ReportStream().getReportStream(print,type);

        } catch (RegistryException e) {
            log.error("Error while getting the Governance Registry", e);
        } catch (JRException e) {
            log.error("Error occured while creating the jasper print ", e);
        } catch (ReportingException e) {
            log.error("Error while generating the report", e);
        }

        return new ByteArrayOutputStream(0);
    }

    @SuppressWarnings("unused")
    public static class ProjectReportBean {
        private String details_version;
        private String details_govCycleName;
        private String details_product;
        private String details_comments;
        private String details_qa;
        private String details_qaa;

        public String getDetails_qaa() {
            return details_qaa;
        }

        public void setDetails_qaa(String details_qaa) {
            this.details_qaa = details_qaa;
//            this.details_qaa = ;
        }

        public String getDetails_comments() {
            return details_comments;
        }

        public void setDetails_comments(String details_comments) {
            this.details_comments = details_comments;
        }

        public String getDetails_qa() {
            return details_qa;
        }

        public void setDetails_qa(String details_qa) {
            this.details_qa = details_qa;
        }

        public String getDetails_govCycleName() {
            return details_govCycleName;
        }

        public void setDetails_govCycleName(String details_govCycleName) {
            this.details_govCycleName = details_govCycleName;
        }

        public String getDetails_product() {
            return details_product;
        }

        public void setDetails_product(String details_product) {
            this.details_product = details_product;
        }

        public String getDetails_version() {
            return details_version;
        }

        public void setDetails_version(String details_version) {
            this.details_version = details_version;
        }
    }
}
