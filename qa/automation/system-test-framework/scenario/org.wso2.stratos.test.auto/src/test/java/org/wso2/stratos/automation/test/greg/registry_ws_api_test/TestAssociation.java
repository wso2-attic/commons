package org.wso2.stratos.automation.test.greg.registry_ws_api_test;

import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.registry.app.RemoteRegistry;
import org.wso2.carbon.registry.core.Association;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.ws.client.registry.WSRegistryServiceClient;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.gregUtils.GregUserIDEvaluator;
import org.wso2.carbon.system.test.core.utils.gregUtils.RegistryProvider;

import java.util.List;


public class TestAssociation extends TestTemplate {
    private static final Log log = LogFactory.getLog(TestAssociation.class);
    private static WSRegistryServiceClient registry = null;

    @Override
    public void init() {
        testClassName = TestAssociation.class.getName();
        String tenantId = new GregUserIDEvaluator().getTenantID();
        registry = new RegistryProvider().getRegistry(tenantId);
        removeResource();
    }

    @Override
    public void runSuccessCase() {
        log.info("Running WS-API TestAssociation Test Cases............................ ");
        addAssociationToResource();
        addAssociationToCollection();
        addAssociationToRoot();
        removeResourceAssociation();
        getResourceAssociation();
        getCollectionAssociation();
        removeResourceAssociation();
        removeCollectionAssociation();
        log.info("Completed Running WS-API TestAssociation Test Cases.................... ");
    }

    @Override
    public void cleanup() {

    }

    private void removeResource() {
//           GregResourceRemover gregResourceRemover = new GregResourceRemover();
           deleteResources("/testk12");
           deleteResources("/assocol1");
           deleteResources("/testk123456");
           deleteResources("/testk1234");
           deleteResources("/getcol1");
           deleteResources("/assoColremove1");
       }

     public void deleteResources(String resourceName) {
        try {
            if (registry.resourceExists(resourceName)) {
                registry.delete(resourceName);
            }
        } catch (RegistryException e) {
            log.error("deleteResources RegistryException thrown:" + e.getMessage());
            Assert.fail("deleteResources RegistryException thrown:" + e.getMessage());
        }

    }
    
    private void addAssociationToResource() {
        Resource r2 = registry.newResource();
        String path = "/testk12/testa/testbsp/test.txt";

        try {
            r2.setContent(new String("this is the content").getBytes());

            r2.setDescription("this is test desc");
            r2.setMediaType("plain/text");
            r2.setProperty("test2", "value2");

            registry.put(path, r2);
            registry.addAssociation(path, "/vtr2121/test", "testasstype1");
            registry.addAssociation(path, "/vtr2122/test", "testasstype2");
            registry.addAssociation(path, "/vtr2123/test", "testasstype3");


            assertTrue("association Destination path not exist", associationPathExists(path, "/vtr2121/test"));
            assertTrue("association Destination path not exist", associationPathExists(path, "/vtr2122/test"));
            assertTrue("association Destination path not exist", associationPathExists(path, "/vtr2123/test"));
            assertTrue("association Type not exist", associationTypeExists(path, "testasstype1"));
            assertTrue("association Type not exist", associationTypeExists(path, "testasstype2"));
            assertTrue("association Type not exist", associationTypeExists(path, "testasstype3"));
            assertTrue("association Source path not exist", associationSourcepathExists(path, path));
            assertTrue("association Source path not exist", associationSourcepathExists(path, path));
            assertTrue("association Source path not exist", associationSourcepathExists(path, path));

            deleteResources("/testk12");
            log.info("addAssociationToResource - Passed");
        } catch (RegistryException e) {
            log.error("addAssociationToResource RegistryException thrown:" + e.getMessage());
            Assert.fail("addAssociationToResource RegistryException thrown:" + e.getMessage());
        } catch (Exception e) {
            log.error("addAssociationToResource Exception thrown:" + e.getMessage());
            Assert.fail("addAssociationToResource Exception thrown:" + e.getMessage());
        }


    }

    private void addAssociationToCollection() {
        Resource r2 = registry.newCollection();
        String path = "/assocol1/assocol2/assoclo3";
        r2.setDescription("this is test desc");
        r2.setMediaType("plain/text");
        r2.setProperty("test2", "value2");

        try {
            registry.put(path, r2);
            registry.addAssociation(path, "/vtr2121/test", "testasstype1");
            registry.addAssociation(path, "/vtr2122/test", "testasstype2");
            registry.addAssociation(path, "/vtr2123/test", "testasstype3");

            assertTrue("association Destination path not exist", associationPathExists(path, "/vtr2121/test"));
            assertTrue("association Destination path not exist", associationPathExists(path, "/vtr2122/test"));
            assertTrue("association Destination path not exist", associationPathExists(path, "/vtr2123/test"));
            assertTrue("association Type not exist", associationTypeExists(path, "testasstype1"));
            assertTrue("association Type not exist", associationTypeExists(path, "testasstype2"));
            assertTrue("association Type not exist", associationTypeExists(path, "testasstype3"));
            assertTrue("association Source path not exist", associationSourcepathExists(path, path));
            assertTrue("association Source path not exist", associationSourcepathExists(path, path));
            assertTrue("association Source path not exist", associationSourcepathExists(path, path));

            deleteResources("/assocol1");
            log.info("addAssociationToCollection _ Passed");
        } catch (RegistryException e) {
            log.error("addAssociationToCollection RegistryException thrown:" + e.getMessage());
            Assert.fail("addAssociationToCollection RegistryException thrown:" + e.getMessage());
        } catch (Exception e) {
            log.error("addAssociationToCollection RegistryException thrown:" + e.getMessage());
            Assert.fail("addAssociationToCollection RegistryException thrown:" + e.getMessage());
        }

    }

    private void addAssociationToRoot() {
        Resource r2 = registry.newCollection();
        String path = "/";

        try {
            registry.addAssociation(path, "/vtr2121/test", "testasstype1");
            registry.addAssociation(path, "/vtr2122/test", "testasstype2");
            registry.addAssociation(path, "/vtr2123/test", "testasstype3");

            assertTrue("association Destination path not exist", associationPathExists(path, "/vtr2121/test"));
            assertTrue("association Destination path not exist", associationPathExists(path, "/vtr2122/test"));
            assertTrue("association Destination path not exist", associationPathExists(path, "/vtr2123/test"));
            assertTrue("association Type not exist", associationTypeExists(path, "testasstype1"));
            assertTrue("association Type not exist", associationTypeExists(path, "testasstype2"));
            assertTrue("association Type not exist", associationTypeExists(path, "testasstype3"));
            assertTrue("association Source path not exist", associationSourcepathExists(path, path));
            assertTrue("association Source path not exist", associationSourcepathExists(path, path));
            assertTrue("association Source path not exist", associationSourcepathExists(path, path));

            registry.removeAssociation(path, "/vtr2121/test", "testasstype1");
            log.info("addAssociationToRoot - Passed");
        } catch (RegistryException e) {
            log.error("addAssociationToRoot RegistryException thrown:" + e.getMessage());
            Assert.fail("addAssociationToRoot RegistryException thrown:" + e.getMessage());
        } catch (Exception e) {
            log.error("addAssociationToRoot Exception thrown:" + e.getMessage());
            Assert.fail("addAssociationToRoot Exception thrown:" + e.getMessage());
        }

    }

    private void getResourceAssociation() {
        Resource r2 = registry.newResource();
        String path = "/testk1234/testa/testbsp/test.txt";
        try {

            r2.setContent(new String("this is the content").getBytes());
            r2.setDescription("this is test desc");
            r2.setMediaType("plain/text");
            r2.setProperty("test2", "value2");

            registry.addAssociation(path, "/vtr2121/test", "testasstype1");
            registry.addAssociation(path, "/vtr2122/test", "testasstype2");
            registry.addAssociation(path, "/vtr2123/test", "testasstype3");

            assertTrue("association Destination path not exist", getAssocitionbyDestinationByType(path, "testasstype1", "/vtr2121/test"));
            assertTrue("association Destination path not exist", getAssocitionbyDestinationByType(path, "testasstype2", "/vtr2122/test"));
            assertTrue("association Destination path not exist", getAssocitionbyDestinationByType(path, "testasstype3", "/vtr2123/test"));
            assertTrue("association Type not exist", getAssocitionbyType(path, "testasstype1"));
            assertTrue("association Type not exist", getAssocitionbyType(path, "testasstype2"));
            assertTrue("association Type not exist", getAssocitionbyType(path, "testasstype3"));
            assertTrue("association Source path not exist", getAssocitionbySourceByType(path, "testasstype1"));
            assertTrue("association Source path not exist", getAssocitionbySourceByType(path, "testasstype2"));
            assertTrue("association Source path not exist", getAssocitionbySourceByType(path, "testasstype3"));
            log.info("getResourceAssociation - Passed");
        } catch (RegistryException e) {
            log.error("getResourceAssociation Exception thrown:" + e.getMessage());
            Assert.fail("getResourceAssociation Exception thrown:" + e.getMessage());
        } catch (Exception e) {
            log.error("getResourceAssociation Exception thrown:" + e.getMessage());
            Assert.fail("getResourceAssociation Exception thrown:" + e.getMessage());
        }


    }

    private void getCollectionAssociation() {
        Resource r2 = registry.newCollection();
        String path = "/getcol1/getcol2/getcol3";
        r2.setDescription("this is test desc");
        r2.setProperty("test2", "value2");

        try {
            registry.put(path, r2);
            registry.addAssociation(path, "/vtr2121/test", "testasstype1");
            registry.addAssociation(path, "/vtr2122/test", "testasstype2");
            registry.addAssociation(path, "/vtr2123/test", "testasstype3");

            assertTrue("association Destination path not exist", getAssocitionbyDestinationByType(path, "testasstype1", "/vtr2121/test"));
            assertTrue("association Destination path not exist", getAssocitionbyDestinationByType(path, "testasstype2", "/vtr2122/test"));
            assertTrue("association Destination path not exist", getAssocitionbyDestinationByType(path, "testasstype3", "/vtr2123/test"));
            assertTrue("association Type not exist", getAssocitionbyType(path, "testasstype1"));
            assertTrue("association Type not exist", getAssocitionbyType(path, "testasstype2"));
            assertTrue("association Type not exist", getAssocitionbyType(path, "testasstype3"));
            assertTrue("association Source path not exist", getAssocitionbySourceByType(path, "testasstype1"));
            assertTrue("association Source path not exist", getAssocitionbySourceByType(path, "testasstype2"));
            assertTrue("association Source path not exist", getAssocitionbySourceByType(path, "testasstype3"));
            deleteResources("/getcol1");
            log.info("getCollectionAssociation -Passed");
        } catch (RegistryException e) {
            log.error("getCollectionAssociation RegistryException thrown:" + e.getMessage());
            Assert.fail("getCollectionAssociation RegistryException thrown:" + e.getMessage());
        } catch (Exception e) {
            log.error("getCollectionAssociation Exception thrown:" + e.getMessage());
            Assert.fail("getCollectionAssociation Exception thrown:" + e.getMessage());
        }
    }


    private void removeResourceAssociation() {
        Resource r2 = registry.newResource();
        String path = "/testk123456/testa/testbsp/test.txt";
        try {

            r2.setContent(new String("this is the content").getBytes());
            r2.setDescription("this is test desc");
            r2.setMediaType("plain/text");
            r2.setProperty("test2", "value2");

            registry.put(path, r2);
            registry.addAssociation(path, "/vtr2121/test", "testasstype1");
            registry.addAssociation(path, "/vtr2122/test", "testasstype2");
            registry.addAssociation(path, "/vtr2123/test", "testasstype3");


            assertTrue("association Destination path not exist", getAssocitionbyDestinationByType(path, "testasstype1", "/vtr2121/test"));
            assertTrue("association Destination path not exist", getAssocitionbyDestinationByType(path, "testasstype2", "/vtr2122/test"));
            assertTrue("association Destination path not exist", getAssocitionbyDestinationByType(path, "testasstype3", "/vtr2123/test"));
            assertTrue("association Type not exist", getAssocitionbyType(path, "testasstype1"));
            assertTrue("association Type not exist", getAssocitionbyType(path, "testasstype2"));
            assertTrue("association Type not exist", getAssocitionbyType(path, "testasstype3"));
            assertTrue("association Source path not exist", getAssocitionbySourceByType(path, "testasstype1"));
            assertTrue("association Source path not exist", getAssocitionbySourceByType(path, "testasstype2"));
            assertTrue("association Source path not exist", getAssocitionbySourceByType(path, "testasstype3"));

            registry.removeAssociation(path, "/vtr2121/test", "testasstype1");
            registry.removeAssociation(path, "/vtr2122/test", "testasstype2");
            registry.removeAssociation(path, "/vtr2123/test", "testasstype3");

            assertFalse("association Destination path exists", getAssocitionbyDestinationByType(path, "testasstype1", "/vtr2121/test"));
            assertFalse("association Destination path exists", getAssocitionbyDestinationByType(path, "testasstype2", "/vtr2122/test"));
            assertFalse("association Destination path exists", getAssocitionbyDestinationByType(path, "testasstype3", "/vtr2123/test"));
            assertFalse("association Type not exist", getAssocitionbyType(path, "testasstype1"));
            assertFalse("association Type not exist", getAssocitionbyType(path, "testasstype2"));
            assertFalse("association Type not exist", getAssocitionbyType(path, "testasstype3"));
            assertFalse("association Source path not exist", getAssocitionbySourceByType(path, "testasstype1"));
            assertFalse("association Source path not exist", getAssocitionbySourceByType(path, "testasstype2"));
            assertFalse("association Source path not exist", getAssocitionbySourceByType(path, "testasstype3"));
            deleteResources("/testk123456");
            log.info("removeResourceAssociation - Passed");
        } catch (RegistryException e) {
            log.error("removeResourceAssociation RegistryException thrown:" + e.getMessage());
            Assert.fail("removeResourceAssociation RegistryException thrown:" + e.getMessage());
        } catch (Exception e) {
            log.error("removeResourceAssociation Exception thrown:" + e.getMessage());
            Assert.fail("removeResourceAssociation Exception thrown:" + e.getMessage());
        }
    }

    private void removeCollectionAssociation() {
        Resource r2 = registry.newCollection();
        String path = "/assoColremove1/assoColremove2/assoColremove3";
        r2.setDescription("this is test desc");
        r2.setMediaType("plain/text");
        r2.setProperty("test2", "value2");

        try {
            registry.put(path, r2);
            registry.addAssociation(path, "/vtr2121/test", "testasstype1");
            registry.addAssociation(path, "/vtr2122/test", "testasstype2");
            registry.addAssociation(path, "/vtr2123/test", "testasstype3");

            assertTrue("association Destination path not exist", getAssocitionbyDestinationByType(path, "testasstype1", "/vtr2121/test"));
            assertTrue("association Destination path not exist", getAssocitionbyDestinationByType(path, "testasstype2", "/vtr2122/test"));
            assertTrue("association Destination path not exist", getAssocitionbyDestinationByType(path, "testasstype3", "/vtr2123/test"));
            assertTrue("association Type not exist", getAssocitionbyType(path, "testasstype1"));
            assertTrue("association Type not exist", getAssocitionbyType(path, "testasstype2"));
            assertTrue("association Type not exist", getAssocitionbyType(path, "testasstype3"));
            assertTrue("association Source path not exist", getAssocitionbySourceByType(path, "testasstype1"));
            assertTrue("association Source path not exist", getAssocitionbySourceByType(path, "testasstype2"));
            assertTrue("association Source path not exist", getAssocitionbySourceByType(path, "testasstype3"));

            registry.removeAssociation(path, "/vtr2121/test", "testasstype1");
            registry.removeAssociation(path, "/vtr2122/test", "testasstype2");
            registry.removeAssociation(path, "/vtr2123/test", "testasstype3");
            assertFalse("association Destination path exists", getAssocitionbyDestinationByType(path, "testasstype1", "/vtr2121/test"));
            assertFalse("association Destination path exists", getAssocitionbyDestinationByType(path, "testasstype2", "/vtr2122/test"));
            assertFalse("association Destination path exists", getAssocitionbyDestinationByType(path, "testasstype3", "/vtr2123/test"));
            assertFalse("association Type not exist", getAssocitionbyType(path, "testasstype1"));
            assertFalse("association Type not exist", getAssocitionbyType(path, "testasstype2"));
            assertFalse("association Type not exist", getAssocitionbyType(path, "testasstype3"));
            assertFalse("association Source path not exist", getAssocitionbySourceByType(path, "testasstype1"));
            assertFalse("association Source path not exist", getAssocitionbySourceByType(path, "testasstype2"));
            assertFalse("association Source path not exist", getAssocitionbySourceByType(path, "testasstype3"));
            deleteResources("/assoColremove1");
            log.info("removeCollectionAssociation - Passed");
        } catch (RegistryException e) {
            log.error("removeCollectionAssociation RegistryException thrown:" + e.getMessage());
            Assert.fail("removeCollectionAssociation RegistryExceptiont hrown:" + e.getMessage());
        } catch (Exception e) {
            log.error("removeCollectionAssociation Exception thrown:" + e.getMessage());
            Assert.fail("removeCollectionAssociation Exception thrown:" + e.getMessage());
        }
    }

//

    //All the following methods are used in the above methods. So no need to add them to the test suit.
    public static boolean resourceExists(RemoteRegistry registry, String fileName) throws Exception {
        boolean value = registry.resourceExists(fileName);
        return value;
    }

    public boolean associationPathExists(String path, String assoPath)
            throws Exception {
        Association association[] = registry.getAllAssociations(path);
        boolean value = false;

        for (int i = 0; i < association.length; i++) {
            //System.out.println(association[i].getDestinationPath());
            if (assoPath.equals(association[i].getDestinationPath()))
                value = true;
        }


        return value;
    }

    public boolean associationTypeExists(String path, String assoType) throws Exception {
        Association association[] = registry.getAllAssociations(path);
        boolean value = false;

        for (int i = 0; i < association.length; i++) {
            association[i].getAssociationType();
            if (assoType.equals(association[i].getAssociationType()))
                value = true;
        }


        return value;
    }

    public boolean associationSourcepathExists(String path, String sourcePath) throws Exception {
        Association association[] = registry.getAllAssociations(path);
        boolean value = false;

        for (int i = 0; i < association.length; i++) {
            association[i].getAssociationType();
            if (sourcePath.equals(association[i].getSourcePath()))
                value = true;
        }

        return value;
    }

    public boolean getAssocitionbyType(String path, String type) throws Exception {

        Association[] asso;
        asso = registry.getAssociations(path, type);

        boolean assoFound = false;
        if (asso == null) return assoFound;
        for (Association a2 : asso) {

            if (a2.getAssociationType().equals(type)) {
                assoFound = true;
                break;
            }
        }
        return assoFound;
    }

    public boolean getAssocitionbySourceByType(String path, String type) throws Exception {

        Association[] asso;
        asso = registry.getAssociations(path, type);

        boolean assoFound = false;
        if (asso == null) return assoFound;
        for (Association a2 : asso) {

            if (a2.getSourcePath().equals(path)) {
                assoFound = true;
                break;
            }
        }
        return assoFound;
    }

    public boolean getAssocitionbyDestinationByType(String path, String type, String destinationPath) throws Exception {

        Association[] asso;
        asso = registry.getAssociations(path, type);


        boolean assoFound = false;

        if (asso == null) return assoFound;
        for (Association a2 : asso) {

            if (a2.getDestinationPath().equals(destinationPath)) {
                assoFound = true;
                break;
            }
        }
        return assoFound;
    }

    public boolean associationNotExists(String path) throws Exception {
        Association association[] = registry.getAllAssociations(path);
        boolean value = true;
        if (association.length > 0)
            value = false;
        return value;
    }

    public boolean getProperty(String path, String key, String value) throws Exception {
        Resource r3 = registry.newResource();
        try {
            r3 = registry.get(path);
        }
        catch (Exception e) {
            fail((new StringBuilder()).append("Couldn't get file from the path :").append(path).toString());
        }
        List propertyValues = r3.getPropertyValues(key);
        Object valueName[] = propertyValues.toArray();
        boolean propertystatus = containsString(valueName, value);
        return propertystatus;
    }

    private boolean containsString(Object[] array, String value) {
        boolean found = false;
        for (Object anArray : array) {
            String s = anArray.toString();
            if (s.startsWith(value)) {
                found = true;
                break;
            }
        }
        return found;
    }

}
