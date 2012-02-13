package org.wso2.registry.secure;


import org.wso2.registry.*;
import org.wso2.registry.exceptions.RegistryException;
import java.io.*;

public class ResourceLoadTest extends PerfTestingSetup{

    public ResourceLoadTest(String text){
        super(text);
    }

    public void testResourcesLoad() throws Exception {



      FileWriter fstream = new FileWriter("ResourceLoadTest.txt");
      BufferedWriter out = new BufferedWriter(fstream);

       for (int i=1;i<=maxResourceCount;i++){

           String path_1= "/c"+i;
           String path_2= path_1 + "/b" +i;
           String path= path_2+"/r1.txt";

           try {
               Resource r1 = adminRegistry.newResource();
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

     	       adminRegistry.put(path,r1);
               adminRegistry.addComment(path,c1);
               adminRegistry.addComment(path,c2);
               adminRegistry.applyTag(path, "tag1");
               adminRegistry.applyTag(path, "tag2");
               adminRegistry.applyTag(path, "tag3");
               adminRegistry.rateResource(path, 4);

               if (i%500==0){
                  out.append("Adding "+i+" Resource"+ "\n");
                 }
           }catch (RegistryException e) {
               e.printStackTrace();
           }
       }
        out.close();

   }
}
