package org.wso2.registry.secure;

import org.wso2.registry.session.UserRegistry;
import org.wso2.registry.exceptions.RegistryException;
import org.wso2.registry.Resource;
import org.wso2.registry.Comment;
import junit.framework.TestCase;

public class ConcurrentUserTest extends TestCase {

    private UserRegistry registry = null;
    long maxConResourceCount = 10;
    private long totalResouceGetTime = 0;
    private int concurrentUsers = 0;
    private long totalResourcePutTime = 0;
    private int counter = 0;
    private int counterPut = 0;

    public ConcurrentUserTest(String text,UserRegistry registry,int concurrentUsers) {
          super(text);
          this.registry = registry;
          this.concurrentUsers = concurrentUsers;
    }

    public ConcurrentUserTest() {
          this.totalResouceGetTime = 0;
          this.totalResourcePutTime = 0;
          this.counter = 0;
          this.counterPut = 0;
         // this.concurrentUsers = concurrentUsers;
    }

    public void testGetConcurrentResources() throws RegistryException {

        String path = "/c1/b1/r1.txt";

        //long totaltime = 0;

        long startTime = System.nanoTime();
        registry.get(path);
        long runTime = System.nanoTime() - startTime;
        totalResouceGetTime += runTime;

        counter ++;
        System.out.println(counter);
        System.out.println(totalResouceGetTime);
        if (concurrentUsers == counter){
           calculateGetTime();
        }
    }

    public void testPutConcurrentResource() throws RegistryException, ArithmeticException{

        long totaltime =0;
        for (int i=1;i<=maxConResourceCount;i++){
            String path_1= "/d"+i;
            String path_2= path_1 + "/b" +i;
            String path= path_2+"/r1.txt";

            try {
                Resource r1 = registry.newResource();
                r1.setDescription("This is new file description");
                String fileContent = "This is file the content";

                for (int filec=1;filec<=12;filec++){
                    fileContent += fileContent;
                }

                byte[] r1content = (fileContent).getBytes();
                r1.setContent(r1content);
                r1.setMediaType("txt");

                Comment c1 = new Comment();
                c1.setResourcePath(path);
                c1.setText("This is a test comment1");

                Comment c2 = new Comment();
                c2.setResourcePath(path);
                c2.setText("This is a test comment2");

                r1.setProperty("key1", "value1");
                r1.setProperty("key2", "value2");
                r1.setProperty("key3", "value3");
                r1.setProperty("key4", "value4");

                long startTime = System.nanoTime();
                registry.put(path,r1);
                long runTime = System.nanoTime() - startTime;
                totalResourcePutTime += runTime;

                registry.addComment(path,c1);
                registry.addComment(path,c2);
                registry.applyTag(path, "tag1");
                registry.applyTag(path, "tag2");
                registry.applyTag(path, "tag3");
                registry.rateResource(path, 4);

                counterPut++;
                System.out.println(counterPut);
                System.out.println(totalResourcePutTime);
                if ((concurrentUsers*maxConResourceCount) == counterPut){
                   calculatePutTime();
                }

            }catch (RegistryException e) {
                e.printStackTrace();
            }

             System.out.println("put the resource:" + i);
        }
    }

    public void testPopulateResources() throws RegistryException {

        for (int i=1;i<=maxConResourceCount;i++){
            String path_1= "/c"+i;
            String path_2= path_1 + "/b" +i;
            String path= path_2+"/r1.txt";

            try {
                Resource r1 = registry.newResource();
                r1.setDescription("This is a file to test performance");
                String fileContent = "this is the file content";

                for (int filec=1;filec<=12;filec++){
                    fileContent += fileContent;
                }

                byte[] r1content = (fileContent).getBytes();
                r1.setContent(r1content);
                r1.setMediaType("text/plain");

                Comment c1 = new Comment();
                c1.setResourcePath(path);
                c1.setText("This is a test comment1");

                Comment c2 = new Comment();
                c2.setResourcePath(path);
                c2.setText("This is a test comment2");

                r1.setProperty("key1", "value1");
                r1.setProperty("key2", "value2");
                r1.setProperty("key3", "value3");
                r1.setProperty("key4", "value4");

                registry.put(path,r1);
                registry.addComment(path,c1);
                registry.addComment(path,c2);
                registry.applyTag(path, "tag1");
                registry.applyTag(path, "tag2");
                registry.applyTag(path, "tag3");
                registry.rateResource(path, 4);

            }catch (RegistryException e) {
                e.printStackTrace();
            }
        }
        System.out.println("End of creating"+" "+maxConResourceCount+" Resources");
    }

    public void calculateGetTime()throws ArithmeticException{
        long time = 0;
            try{
                time = ((totalResouceGetTime/1000000)/counter);
            }catch (ArithmeticException e) {
                e.printStackTrace();
            }
        System.out.println("Average Resource get time with " + concurrentUsers + " concurrent Users: " + time + " ms");

    }

    public void calculatePutTime()throws ArithmeticException{
        long time = 0;
            try{
                time = ((totalResourcePutTime/1000000)/(maxConResourceCount*concurrentUsers));
            }catch (ArithmeticException e) {
                e.printStackTrace();
            }
        System.out.println("Average Resource PUT time with "+ concurrentUsers + " concurrent Users: " + time + " ms");

    }
}
