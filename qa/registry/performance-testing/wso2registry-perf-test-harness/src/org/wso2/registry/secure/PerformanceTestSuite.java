package org.wso2.registry.secure;
import junit.framework.Test;
import junit.framework.TestSuite;


public class PerformanceTestSuite extends TestSuite{

    public static Test suite(){
        TestSuite suite = new TestSuite();

        suite.addTest(new DataPopulate("testpopulateUserBasedAuthorization"));
        suite.addTest(new DataPopulate("testpopulateResources"));
        suite.addTest(new DataPopulate("testassignpermissiontoresources"));
        suite.addTest(new DataPopulate("testgetresourcetime"));
        suite.addTest(new DataPopulate("testgetCollectiontime"));
        suite.addTest(new DataPopulate("testgetresourceversiontime"));
        suite.addTest(new DataPopulate("testputresourcetime"));
        suite.addTest(new DataPopulate("testputcollectiontime"));
        suite.addTest(new DataPopulate("testrenameresourcetime"));
        suite.addTest(new DataPopulate("testrenamecollectiontime"));
        suite.addTest(new DataPopulate("testcopyresourcetime"));
        suite.addTest(new DataPopulate("testcopycollectiontime"));
        suite.addTest(new DataPopulate("testmoveresourcetime"));
        suite.addTest(new DataPopulate("testmovecollectiontime"));
        suite.addTest(new DataPopulate("testdeleteresourcetime"));
        suite.addTest(new DataPopulate("testdeletecollectiontime"));
        suite.addTest(new DataPopulate("testImportresourcetime"));
        suite.addTest(new DataPopulate("testImportWsdlTime"));
        suite.addTest(new DataPopulate("testWsdlValidationTime"));

        suite.addTest(new CommentPerfTest("testAddCommentTime"));
        suite.addTest(new CommentPerfTest("testAccessComment"));
        suite.addTest(new CommentPerfTest("testAddProperties"));
        suite.addTest(new CommentPerfTest("testReadProperties"));
        suite.addTest(new CommentPerfTest("testPropertySearch"));

        suite.addTest(new TagPerfTesting("testAddTagToResources"));
        suite.addTest(new TagPerfTesting("testAddTagToCollection"));
        suite.addTest(new TagPerfTesting("testReadTagFromResourceTime"));
        suite.addTest(new TagPerfTesting("testReadTagFromResourcepathTime"));

        suite.addTest(new DataPopulate("testdatabaseStatistics"));

        return suite;
    }
}


