package org.wso2.registry.secure;

import org.wso2.registry.*;
import org.wso2.registry.config.RegistryContext;
import org.wso2.registry.jdbc.utils.RegistryDataSource;
import org.wso2.registry.jdbc.realm.RegistryRealm;
import org.wso2.registry.jdbc.EmbeddedRegistry;
import org.wso2.registry.session.UserRegistry;
import org.wso2.registry.exceptions.RegistryException;

import javax.sql.DataSource;
import java.io.InputStream;
import java.io.FileInputStream;

import junit.framework.TestCase;


public class TagPerfTesting extends PerfTestingSetup {


    public TagPerfTesting (String text) {
        super(text);
    }

    public void testAddTagToResources() throws RegistryException {

        int totaltime = 0;
        for (int i=1;i<=maxFileToputResource;i++){
            String path_1= "/c"+i;
            String path_2= path_1 + "/b" +i;
            String path= path_2+"/r1.txt";
            
            long startTime = System.nanoTime();
            adminRegistry.applyTag(path,"wso2_soa_registry");
            long stopTime = System.nanoTime();
            long runTime = stopTime - startTime;
            totaltime += runTime;

        }
        System.out.println("Average time to apply tag to resource file"+" "+((totaltime/1000000)/maxFileToputResource)+" "+"ms");
    }



    public void testAddTagToCollection() throws RegistryException {

            int totaltime = 0;

            for (int i=1;i<=maxFileToputResource;i++){
                String path_1= "/c"+i;
                String path= path_1 + "/b" +i;

                try {
                    Resource r1 = adminRegistry.newCollection();
                    r1.setDescription("This is the collection description");

                    adminRegistry.put(path, r1);
                    long startTime = System.nanoTime();
                    adminRegistry.applyTag(path,"wso2_soa_registy_col");
                    long stopTime = System.nanoTime();
                    long runTime = stopTime - startTime;
                    totaltime += runTime;

                }catch (RegistryException e) {
                    e.printStackTrace();
                }
            }
        System.out.println("Average time to apply tag to Collection"+" "+((totaltime/1000000)/maxFileToputResource)+" "+"ms");
    }


    public void testReadTagFromResourceTime() throws RegistryException {
        long loopcount = 10000;
        String path= "/c1/b1/r1.txt";
        long totalTime = 0;

        for (int i=1;i<=loopcount;i++){
            long startTime = System.nanoTime();
            adminRegistry.getTags(path);
            long runTime = System.nanoTime() - startTime;
            totalTime += runTime;
        }
        System.out.println("Average time to read tag from resource file"+" "+((totalTime/1000000)/loopcount)+" "+"ms");
    }

     public void testReadTagFromResourcepathTime() throws RegistryException {
        long loopcount = 1000;
        String path= "/c1/b1/r1.txt";
        long totalTime = 0;
        TaggedResourcePath[] paths;

        for (int i=1;i<=loopcount;i++){
            long startTime = System.nanoTime();
            paths = adminRegistry.getResourcePathsWithTag("wso2_soa_registry");
            long runTime = System.nanoTime() - startTime;
            totalTime += runTime;
        }
        System.out.println("Average time to read tag from resource path"+" "+((totalTime/1000000)/loopcount)+" "+"ms");
    }


    public void testApplyTagsPerSecound() throws RegistryException {
        int totalTime = 0;

        int count = 0;

            long forStartTime = System.nanoTime();
            long forEndTime = forStartTime + 2000000000;
            System.out.println("End"+forEndTime);

            while(forEndTime >= System.nanoTime()){

                for (int i=1;i<=maxFileToputResource;i++){
                    String path_1= "/c"+i;
                    String path_2= path_1 + "/b" +i;
                    String path= path_2+"/r1.txt";
                    long startTime = System.nanoTime();
                    adminRegistry.applyTag(path,"wso2_soa_registry3");
                    long runTime = System.nanoTime() - startTime;
                    totalTime += runTime;
                    count ++;                    
                }
            }
        System.out.println("Average tags apply per secound (resource): "+" "+count/2+"/sec");
        System.out.println("Average time to tagging a resource: "+" "+((totalTime/1000000)/count)+" "+"ms");
        System.out.println("Count :" + count);
    }


    public void testApplyTagCollectionsNew() throws RegistryException {

        int totalTime = 0;
        long runTime = 0;
        long stopTime = 0;
        long startTime = 0;
        int count = 0;

            Long forStartTime = System.currentTimeMillis();
            Long forEndTime = forStartTime + 100000;
            System.out.println("End"+forEndTime);

            while(forEndTime >= System.currentTimeMillis() ){

                for (int i=1;i<=maxFileToputResource;i++){
                    String path_1= "/cc"+i;
                    String path= path_1 + "/bb" +i;

                    startTime = System.currentTimeMillis();
                    adminRegistry.applyTag(path,"wso2_soa_registry8");
                    stopTime = System.currentTimeMillis();
                    runTime = stopTime - startTime;
                    totalTime += runTime;
                    count ++;
                }
            }
        System.out.println("Average tags apply per secound (collection): "+" "+count/100+"/sec");
        System.out.println("Average time to tagging a collection: "+" "+totalTime/count+" "+"ms");
        System.out.println("Count :" + count);
        }

    public void testSearchTagTime() throws RegistryException {

        int totalTime = 0;
        long runTime = 0;
        long stopTime = 0;
        long startTime = 0;
        int count = 0;

        Long forStartTime = System.currentTimeMillis();
        Long forEndTime = forStartTime + 600000;
        System.out.println("End"+forEndTime);
        TaggedResourcePath[] paths;

            while(forEndTime >= System.currentTimeMillis() ){

                for (int i=1;i<=maxFileToputResource;i++){
                    startTime = System.currentTimeMillis();
                    paths = adminRegistry.getResourcePathsWithTag("wso2_soa_registry");
                    stopTime = System.currentTimeMillis();
                    runTime = stopTime - startTime;
                    totalTime += runTime;
                    System.out.println(totalTime);
                    count ++;
                }
            }

        System.out.println("Average tags search per secound : "+" "+count/600+"/sec");
        System.out.println("Average time to search a tag: "+" "+totalTime/count+" "+"ms");
        System.out.println("Count :" + count);
        }
    }







