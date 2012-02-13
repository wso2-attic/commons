/*
package org.wso2.registry.secure;

public class PathGenerator implements PathGeneratorInterface{

    String generatedPath = null;

    public PathGenerator(){

    }


    public String pathGeneratorHorizontal(int ResourceCount, String resourceName){
        String resourcePath = resourceName + ResourceCount;
        System.out.println(resourcePath);
        String[] pathGenerated = null;
        String resourcePathNew = null;
        String path;
        path = pathGenerator(ResourceCount,resourcePath);

        //System.out.println("Inside main :" + path);
        pathGenerated = pathSplitter(path);


        for (int j=0;j<ResourceCount;j++)
        {
            if (j==0){
                resourcePath = pathGenerated[j];
            }
            else{
                resourcePath = resourcePath + pathGenerated[j];
            }
            resourcePathNew = resourcePath+"/r1.txt";
            System.out.println(resourcePathNew);


        }
        System.out.println(resourcePathNew);
        return resourcePathNew;
    }

    public String pathGeneratorVertical(int ResourceCount, String resourceName){
        String resourcePath = resourceName + ResourceCount;
        System.out.println(resourcePath);
        String[] pathGenerated = null;
        String resourcePathNew = null;
        String path;
        path = pathGenerator(ResourceCount,resourcePath);

        //System.out.println("Inside main :" + path);
        pathGenerated = pathSplitter(path);


        for (int j=0;j<ResourceCount;j++)
        {
            new BinaryTreeTest().run(j,pathGenerated[j]);

        }
        System.out.println(resourcePathNew);
        return resourcePathNew;
    }
    
    public String pathGeneratorCommon (int ResourceCount, String resourceName){
    String text =resourceName;
    return text;

    }

    public String pathGenerator( int counter, String path){

        path = path +("/c"+(counter-1));
        //String generatedPath = null;

        if(counter == 1){
            generatedPath = path;
            //System.out.println(generatedPath);
            return generatedPath;
        }
        else
        {
            pathGenerator(--counter, path);

        }
        //System.out.println(generatedPath);
        return generatedPath;
    }

    public String[] pathSplitter(String path) {

        String pathValue = null;
        String pathValue2 = null;
        String[] tokens = null;
        String splitPattern = "/";
        tokens = path.split(splitPattern);

        for (int i=1;i<tokens.length;i++)
        {
            pathValue = tokens[i];
            pathValue2 = "/"+pathValue;
            tokens[i-1] = pathValue2;
        }

        String yvalue = null;
        for (int j=0;j<tokens.length;j++)
        {
            if (j==0){
                yvalue = tokens[j];
            }
            else{
                yvalue = yvalue + tokens[j];
            }
        }
        return tokens;
    }


    public static void main(String[] args) {
		PathGenerator cubeObj1 = new PathGenerator();
		//cubeObj1.pathGeneratorVertical(100,"/c");

        cubeObj1.pathGeneratorHorizontal(100,"/c");

    }

}
  */