package org.wso2.registry.secure;

import junit.framework.TestSuite;
import junit.framework.Test;


public class VerticalDataPopulationTestSuite extends TestSuite{

    public static Test suite(){
        TestSuite suite = new TestSuite();

        suite.addTest(new VerticalDataPopulate("testVerticalDatapopulate"));
        suite.addTest(new VerticalDataPopulate("testpopulateUserBasedAuthorization"));
        suite.addTest(new VerticalDataPopulate("testassignpermissiontoresources"));
        suite.addTest(new VerticalDataPopulate("testgetresourcetime"));
        suite.addTest(new VerticalDataPopulate("testgetresourceversiontime"));
        suite.addTest(new VerticalDataPopulate("testgetCollectiontime"));
        suite.addTest(new VerticalDataPopulate("testputresourcetime"));
        suite.addTest(new VerticalDataPopulate("testputcollectiontime"));
        suite.addTest(new VerticalDataPopulate("testrenameresourcetime"));
        suite.addTest(new VerticalDataPopulate("testrenamecollectiontime"));
        suite.addTest(new VerticalDataPopulate("testcopyresourcetime"));
        suite.addTest(new VerticalDataPopulate("testcopycollectiontime"));
        suite.addTest(new VerticalDataPopulate("testmoveresourcetime"));
        suite.addTest(new VerticalDataPopulate("testmovecollectiontime"));
        suite.addTest(new VerticalDataPopulate("testdeleteresourcetime"));
        suite.addTest(new VerticalDataPopulate("testdeletecollectiontime"));
        suite.addTest(new VerticalDataPopulate("testImportresourcetime"));
        suite.addTest(new VerticalDataPopulate("testImportWsdlTime"));
        suite.addTest(new VerticalDataPopulate("testWsdlValidationTime"));
        suite.addTest(new VerticalDataPopulate("testAddCommentTime"));
        suite.addTest(new VerticalDataPopulate("testAccessComment"));
        suite.addTest(new VerticalDataPopulate("testAddProperties"));
        suite.addTest(new VerticalDataPopulate("testReadProperties"));
        suite.addTest(new VerticalDataPopulate("testPropertySearch"));
        suite.addTest(new VerticalDataPopulate("testAddTagToResources"));
        suite.addTest(new VerticalDataPopulate("testAddTagToCollection"));
        suite.addTest(new VerticalDataPopulate("testReadTagFromResourceTime"));
        suite.addTest(new VerticalDataPopulate("testReadTagFromResourcepathTime"));
        suite.addTest(new VerticalDataPopulate("testdatabaseStatistics"));

        return suite;
    }
}

