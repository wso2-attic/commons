package org.wso2.registry.jackrabbit.preformance;

import org.wso2.registry.jackrabbit.JackrabbitRegistry;
import org.wso2.registry.jackrabbit.JackrabbitRepository;
import org.wso2.registry.jackrabbit.RegistryException;
import org.wso2.registry.jackrabbit.nodetype.Comment;
import org.wso2.registry.jackrabbit.nodetype.Folder;
import org.wso2.registry.jackrabbit.nodetype.Resource;

public class PerformanceTest {
    
    private JackrabbitRepository repository;
    private JackrabbitRegistry registry;
    
    int maxResourceCount = 10000;
    int maxFileToputResource = 100;
    
    public PerformanceTest () throws RegistryException {
        repository = new JackrabbitRepository();
        registry = new JackrabbitRegistry(repository,"wso2","wso2");
    }
    
    public static void main(String[] args) throws RegistryException {
        
        PerformanceTest perfTest = new PerformanceTest();
        perfTest.testpopulateResources();
        perfTest.testpopulateResourceVersioning();
        perfTest.testputresourcetime();
        perfTest.testgetresourcetime();
        perfTest.testdeleteresourcetime();
        
    }
    
    
    
    public void testpopulateResources() throws RegistryException {

        for (int i=1;i<=maxResourceCount;i++){
            String path1= "/c"+i;
            String path2= path1 + "/b" +i;
            String path= path2+"/r1.txt";

            try {
                
                // Create path1
                Folder cx =  new Folder(path1);
                registry.createPath(cx);
                
                // Create path2
                cx = new Folder(path2);
                registry.createPath(cx);
                
                Resource r1 = registry.newResource();
                r1.setPath(path);
                r1.setDescription("This is a file to be renamed");
                String fileContent = "This is file the content";

                for (int filec=1;filec<=5;filec++){
                    fileContent += fileContent;
                }

                byte[] r1content = (fileContent).getBytes();
                r1.setContent(r1content);
                r1.setMediaType("txt");

                Comment c1 = new Comment();
                c1.setText("This is a test comment1");
                r1.addComment(c1);

                Comment c2 = new Comment();
                c2.setText("This is a test comment2");
                r1.addComment(c2);

                r1.setProperty("key1", "value1");
                r1.setProperty("key2", "value2");
                r1.setProperty("key3", "value3");
                r1.setProperty("key4", "value4");

                registry.put(r1);


            }catch (RegistryException e) {
                e.printStackTrace();
            }
        }
        System.out.println("End of creating"+" "+maxResourceCount+" Resources");
    }
    
    /* update existing resource to increase version numbers */
    public void testpopulateResourceVersioning() throws RegistryException{

        for (int i=1;i<=maxResourceCount;i++){
            String path_1= "/c"+i;
            String path_2= path_1 + "/b" +i;
            String path= path_2+"/r1.txt";
            try {
                Resource r1 = registry.get(path);
                r1.setDescription("This is a file to be renamed");
                String fileContent = "This is file the content updated";

                r1.setProperty("key1", "value1");
                r1.setProperty("key2", "value2");
                r1.setProperty("key3", "value3");
                r1.setProperty("key4", "value4");

                for (int filec=0;filec<=5;filec++){
                    fileContent += fileContent;
                }
                byte[] r1content = (fileContent).getBytes();
                r1.setContent(r1content);
                r1.setMediaType("txt");
                registry.update(r1);

            }catch (RegistryException e) {
                e.printStackTrace();
            }
        }
        System.out.println("End of creating 2nd version of "+" "+maxResourceCount+" "+"resources");
    }

    
    public void testputresourcetime() throws RegistryException{

        long totaltime = 0;
        for (int i=1;i<=maxFileToputResource;i++){
            String path1= "/d"+i;
            String path2= path1 + "/b" +i;
            String path= path2+"/r1.txt";

            try {         
                // Create path1
                Folder cx =  new Folder(path1);
                registry.createPath(cx);
                
                // Create path2
                cx = new Folder(path2);
                registry.createPath(cx);
                
                Resource r1 = registry.newResource();
                r1.setPath(path);
                r1.setDescription("This is new file description");
                String fileContent = "This is file the content";

                for (int filec=0;filec<=10;filec++){
                    fileContent += fileContent;
                }

                byte[] r1content = (fileContent).getBytes();
                r1.setContent(r1content);
                r1.setMediaType("txt");

/*                Comment c1 = new Comment();
                c1.setText("This is a test comment1");
                r1.addComment(c1);

                Comment c2 = new Comment();
                c2.setText("This is a test comment2");
                r1.addComment(c2);

                r1.setProperty("key1", "value1");
                r1.setProperty("key2", "value2");
                r1.setProperty("key3", "value3");
                r1.setProperty("key4", "value4");*/

                long startTime = System.currentTimeMillis();
                registry.put(r1);
                long stopTime = System.currentTimeMillis();
                long runTime = stopTime - startTime;
                totaltime += runTime;

            }catch (RegistryException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Average Resource put time: " + totaltime/maxFileToputResource+ " ms");
    }
    
    public void testdeleteresourcetime() throws RegistryException{

        long totaltime = 0;
        for (int i=1;i<=maxFileToputResource;i++){
            String path1= "/c"+i;
            String path2= path1 + "/b" +i;
            String path= path2+"/r1.txt";

            try {
                long startTime = System.currentTimeMillis();
                registry.delete(path);
                long stopTime = System.currentTimeMillis();
                long runTime = stopTime - startTime;
                totaltime += runTime;

            }catch (RegistryException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Average Resource delete time: " + totaltime/maxFileToputResource+ " ms");
    }
    
    public void testgetresourcetime() throws RegistryException {

        String path = "/c50/b50/r1.txt";
        long totaltime = 0;
        long numberOfTimes = 10000;

        for (int i=1;i<=numberOfTimes;i++){
              long startTime = System.currentTimeMillis();
              registry.get(path);
              long stopTime = System.currentTimeMillis();
              long runTime = stopTime - startTime;
              totaltime += runTime;
        }

        System.out.println("Average Resource get time: " + totaltime/numberOfTimes+ " ms");
    }

}
 


