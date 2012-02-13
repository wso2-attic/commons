package org.wso2.registry.secure;

import junit.framework.TestSuite;
import junit.framework.Test;


public class AppVerticalDataPopulationTestSuite extends TestSuite{

    public static Test suite(){
        TestSuite suite = new TestSuite();

        suite.addTest(new AppVerticalDataPopulate("testVerticalDatapopulate"));
        suite.addTest(new AppVerticalDataPopulate("testpopulateUserBasedAuthorization"));
        suite.addTest(new AppVerticalDataPopulate("testassignpermissiontoresources"));
        suite.addTest(new AppVerticalDataPopulate("testgetresourcetime"));
        suite.addTest(new AppVerticalDataPopulate("testgetresourceversiontime"));
        suite.addTest(new AppVerticalDataPopulate("testgetCollectiontime"));
        suite.addTest(new AppVerticalDataPopulate("testputresourcetime"));
        suite.addTest(new AppVerticalDataPopulate("testputcollectiontime"));
        suite.addTest(new AppVerticalDataPopulate("testrenameresourcetime"));
        suite.addTest(new AppVerticalDataPopulate("testrenamecollectiontime"));
        suite.addTest(new AppVerticalDataPopulate("testcopyresourcetime"));
        suite.addTest(new AppVerticalDataPopulate("testcopycollectiontime"));
        suite.addTest(new AppVerticalDataPopulate("testmoveresourcetime"));
        suite.addTest(new AppVerticalDataPopulate("testmovecollectiontime"));
        suite.addTest(new AppVerticalDataPopulate("testdeleteresourcetime"));
        suite.addTest(new AppVerticalDataPopulate("testdeletecollectiontime"));
        suite.addTest(new AppVerticalDataPopulate("testImportresourcetime"));
        suite.addTest(new AppVerticalDataPopulate("testImportWsdlTime"));
        suite.addTest(new AppVerticalDataPopulate("testWsdlValidationTime"));
        suite.addTest(new AppVerticalDataPopulate("testAddCommentTime"));
        suite.addTest(new AppVerticalDataPopulate("testAccessComment"));
        suite.addTest(new AppVerticalDataPopulate("testAddProperties"));
        suite.addTest(new AppVerticalDataPopulate("testReadProperties"));
        suite.addTest(new AppVerticalDataPopulate("testPropertySearch"));
        suite.addTest(new AppVerticalDataPopulate("testAddTagToResources"));
        suite.addTest(new AppVerticalDataPopulate("testAddTagToCollection"));
        suite.addTest(new AppVerticalDataPopulate("testReadTagFromResourceTime"));
        suite.addTest(new AppVerticalDataPopulate("testReadTagFromResourcepathTime"));
        suite.addTest(new AppVerticalDataPopulate("testdatabaseStatistics"));

        return suite;
    }
}

