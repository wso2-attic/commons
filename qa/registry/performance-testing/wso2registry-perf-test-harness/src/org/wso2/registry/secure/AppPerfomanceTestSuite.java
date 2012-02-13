package org.wso2.registry.secure;

import junit.framework.TestSuite;
import junit.framework.Test;
import junit.framework.TestSuite;


public class AppPerfomanceTestSuite extends TestSuite {

    public static Test suite(){
        TestSuite suite = new TestSuite();

        suite.addTest(new AppDataPopulate("testpopulateUserBasedAuthorization"));
        suite.addTest(new AppDataPopulate("testpopulateResources"));
        suite.addTest(new AppDataPopulate("testassignpermissiontoresources"));
        suite.addTest(new AppDataPopulate("testgetresourcetime"));
        suite.addTest(new AppDataPopulate("testgetCollectiontime"));
        suite.addTest(new AppDataPopulate("testgetresourceversiontime"));
        suite.addTest(new AppDataPopulate("testputresourcetime"));
        suite.addTest(new AppDataPopulate("testputcollectiontime"));
        suite.addTest(new AppDataPopulate("testrenameresourcetime"));
        suite.addTest(new AppDataPopulate("testrenamecollectiontime"));
        suite.addTest(new AppDataPopulate("testcopyresourcetime"));
        suite.addTest(new AppDataPopulate("testcopycollectiontime"));
        suite.addTest(new AppDataPopulate("testmoveresourcetime"));
        suite.addTest(new AppDataPopulate("testmovecollectiontime"));
        suite.addTest(new AppDataPopulate("testdeleteresourcetime"));
        suite.addTest(new AppDataPopulate("testdeletecollectiontime"));
        suite.addTest(new AppDataPopulate("testImportresourcetime"));
        suite.addTest(new AppDataPopulate("testImportWsdlTime"));
        suite.addTest(new AppDataPopulate("testWsdlValidationTime"));

        suite.addTest(new AppDataPopulate("testAddCommentTime"));
        suite.addTest(new AppDataPopulate("testAccessComment"));
        suite.addTest(new AppDataPopulate("testAddProperties"));
        suite.addTest(new AppDataPopulate("testReadProperties"));
        suite.addTest(new AppDataPopulate("testPropertySearch"));

        suite.addTest(new AppDataPopulate("testAddTagToResources"));
        suite.addTest(new AppDataPopulate("testAddTagToCollection"));
        suite.addTest(new AppDataPopulate("testReadTagFromResourceTime"));
        suite.addTest(new AppDataPopulate("testReadTagFromResourcepathTime"));

        suite.addTest(new AppDataPopulate("testdatabaseStatistics"));

        return suite;
    }
}


