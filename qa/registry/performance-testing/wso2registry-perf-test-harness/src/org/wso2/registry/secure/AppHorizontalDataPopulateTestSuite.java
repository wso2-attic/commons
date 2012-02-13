package org.wso2.registry.secure;

import junit.framework.TestSuite;
import junit.framework.Test;

public class AppHorizontalDataPopulateTestSuite extends TestSuite {

    public static Test suite(){
        TestSuite suite = new TestSuite();

        suite.addTest(new AppHorizontalDataPopulate("testHorizontalDatapopulate"));
        suite.addTest(new AppHorizontalDataPopulate("testpopulateUserBasedAuthorization"));
        suite.addTest(new AppHorizontalDataPopulate("testassignpermissiontoresources"));
        suite.addTest(new AppHorizontalDataPopulate("testgetresourcetime"));
        suite.addTest(new AppHorizontalDataPopulate("testgetresourceversiontime"));
        suite.addTest(new AppHorizontalDataPopulate("testgetCollectiontime"));
        suite.addTest(new AppHorizontalDataPopulate("testputresourcetime"));
        suite.addTest(new AppHorizontalDataPopulate("testputcollectiontime"));
        suite.addTest(new AppHorizontalDataPopulate("testrenameresourcetime"));
        suite.addTest(new AppHorizontalDataPopulate("testrenamecollectiontime"));
        suite.addTest(new AppHorizontalDataPopulate("testcopyresourcetime"));
        suite.addTest(new AppHorizontalDataPopulate("testcopycollectiontime"));
        suite.addTest(new AppHorizontalDataPopulate("testmoveresourcetime"));
        suite.addTest(new AppHorizontalDataPopulate("testmovecollectiontime"));
        suite.addTest(new AppHorizontalDataPopulate("testdeleteresourcetime"));
        suite.addTest(new AppHorizontalDataPopulate("testdeletecollectiontime"));
        suite.addTest(new AppHorizontalDataPopulate("testImportresourcetime"));
        suite.addTest(new AppHorizontalDataPopulate("testImportWsdlTime"));
        suite.addTest(new AppHorizontalDataPopulate("testWsdlValidationTime"));
        suite.addTest(new AppHorizontalDataPopulate("testAddCommentTime"));
        suite.addTest(new AppHorizontalDataPopulate("testAccessComment"));
        suite.addTest(new AppHorizontalDataPopulate("testAddProperties"));
        suite.addTest(new AppHorizontalDataPopulate("testReadProperties"));
        suite.addTest(new AppHorizontalDataPopulate("testPropertySearch"));
        suite.addTest(new AppHorizontalDataPopulate("testAddTagToResources"));
        suite.addTest(new AppHorizontalDataPopulate("testAddTagToCollection"));
        suite.addTest(new AppHorizontalDataPopulate("testReadTagFromResourceTime"));
        suite.addTest(new AppHorizontalDataPopulate("testReadTagFromResourcepathTime"));
        suite.addTest(new AppHorizontalDataPopulate("testdatabaseStatistics"));

        return suite;
    }
}
