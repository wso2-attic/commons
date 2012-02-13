package org.wso2.registry.secure;


public class VerticalPathGenerator implements PathGeneratorInterface{
    private String resourceName;
    private long generateResourceCount;
    private int counter;
    private String collectionName;

    public VerticalPathGenerator(long maxResourceCount, String resourceName, String collectionName) {
        this.resourceName = resourceName;
        this.generateResourceCount = maxResourceCount;
        this.collectionName = collectionName;
        counter = 1;
    }

    public String getPath() {

           String tempPath = "/";
           for (int i = 0; i < counter && (i+1<=generateResourceCount); i++) {
               tempPath = tempPath + collectionName + (i+1) + "/";
           }
           counter++;
           tempPath = tempPath + resourceName;
           return tempPath;
       }
   }

