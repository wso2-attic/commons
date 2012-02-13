package org.wso2.registry.secure;

import junit.framework.TestSuite;
import junit.framework.Test;

public class HorizontalDataPopulateTestSuite extends TestSuite {

    public static Test suite(){
        TestSuite suite = new TestSuite();

        suite.addTest(new HorizontalDataPopulate("testHorizontalDatapopulate"));
        suite.addTest(new HorizontalDataPopulate("testpopulateUserBasedAuthorization"));
        suite.addTest(new HorizontalDataPopulate("testassignpermissiontoresources"));
        suite.addTest(new HorizontalDataPopulate("testgetresourcetime"));
        suite.addTest(new HorizontalDataPopulate("testgetresourceversiontime"));
        suite.addTest(new HorizontalDataPopulate("testgetCollectiontime"));
        suite.addTest(new HorizontalDataPopulate("testputresourcetime"));
        suite.addTest(new HorizontalDataPopulate("testputcollectiontime"));
        suite.addTest(new HorizontalDataPopulate("testrenameresourcetime"));
        suite.addTest(new HorizontalDataPopulate("testrenamecollectiontime"));
        suite.addTest(new HorizontalDataPopulate("testcopyresourcetime"));
        suite.addTest(new HorizontalDataPopulate("testcopycollectiontime"));
        suite.addTest(new HorizontalDataPopulate("testmoveresourcetime"));
        suite.addTest(new HorizontalDataPopulate("testmovecollectiontime"));
        suite.addTest(new HorizontalDataPopulate("testdeleteresourcetime"));
        suite.addTest(new HorizontalDataPopulate("testdeletecollectiontime"));
        suite.addTest(new HorizontalDataPopulate("testImportresourcetime"));
        suite.addTest(new HorizontalDataPopulate("testImportWsdlTime"));
        suite.addTest(new HorizontalDataPopulate("testWsdlValidationTime"));
        suite.addTest(new HorizontalDataPopulate("testAddCommentTime"));
        suite.addTest(new HorizontalDataPopulate("testAccessComment"));
        suite.addTest(new HorizontalDataPopulate("testAddProperties"));
        suite.addTest(new HorizontalDataPopulate("testReadProperties"));
        suite.addTest(new HorizontalDataPopulate("testPropertySearch"));
        suite.addTest(new HorizontalDataPopulate("testAddTagToResources"));
        suite.addTest(new HorizontalDataPopulate("testAddTagToCollection"));
        suite.addTest(new HorizontalDataPopulate("testReadTagFromResourceTime"));
        suite.addTest(new HorizontalDataPopulate("testReadTagFromResourcepathTime"));
        suite.addTest(new HorizontalDataPopulate("testdatabaseStatistics"));

        return suite;
    }
}
