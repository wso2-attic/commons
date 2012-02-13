package org.wso2.registry.secure;
import junit.framework.Test;
import junit.framework.TestSuite;


public class PerformanceTestSuite extends TestSuite{

    public static Test suite(){
        TestSuite suite = new TestSuite();

        suite.addTest(new DataPopulate("testpopulateUserBasedAuthorization"));
        suite.addTest(new DataPopulate("testpopulateResources"));
        suite.addTest(new DataPopulate("testAddDependency"));
        suite.addTest(new DataPopulate("testAssignPermissionToResources"));
        suite.addTest(new DataPopulate("testpopulateResourceVersioning"));
        suite.addTest(new DataPopulate("testputresourcetime"));
        suite.addTest(new DataPopulate("testputcollectiontime"));
        suite.addTest(new DataPopulate("testgetresourcetime"));
        suite.addTest(new DataPopulate("testgetCollectiontime"));
        suite.addTest(new DataPopulate("testrenameresourcetime"));
        suite.addTest(new DataPopulate("testrenamecollectiontime"));
        suite.addTest(new DataPopulate("testdeleteresourcetime"));
        suite.addTest(new DataPopulate("testdeletecollectiontime"));
        suite.addTest(new DataPopulate("testdatabaseStatistics"));          

        return suite;
    }
}



