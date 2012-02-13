package org.wso2.carbon.web.test.common;

import junit.framework.TestCase;
import org.apache.tools.ant.BuildLogger;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.apache.axis2.util.XMLPrettyPrinter;


import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: jayani
 * Date: Nov 26, 2009
 * Time: 4:17:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class SamplesCommon extends TestCase {


    public boolean runAnt(String file,String target) throws Exception{
        BuildLogger logger = new DefaultLogger();

//          FileOutputStream out= new FileOutputStream("myfile.txt");
//           PrintStream ps = new PrintStream(out);
        boolean result=false;
        logger.setMessageOutputLevel(Project.MSG_INFO);
        logger.setOutputPrintStream(System.out);
        //         logger.setErrorPrintStream(ps);
        logger.setErrorPrintStream(System.out);
        logger.setEmacsMode(true);


        ProjectHelper ph = ProjectHelper.getProjectHelper();
        Project p = new Project();
        p.addBuildListener(logger);
        p.init();


        p.addReference("ant.projectHelper", ph);
        ph.parse(p, new File(file));

        try{
            if(target.equalsIgnoreCase("ant")){
                p.executeTarget("build-all");

            }else{
                p.executeTarget(target);
            }
            result=true;
        }catch(Exception e){
            System.out.println(e);

        }
        return result;

    }

    public boolean executeSystemCommands(String me) throws Exception{
        boolean result=true;
        try {
            String output,error;

            //execute the system command
            Process p = Runtime.getRuntime().exec(me);

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(p.getErrorStream()));

            // read the output from the command
            while ((output = stdInput.readLine()) != null) {
                System.out.println(output);
            }

            // read errors from the  command
            while ((error = stdError.readLine()) != null) {
                result=false;
                System.out.println(error);
            }

            System.exit(0);
        }
        catch (IOException e) {
            e.printStackTrace();
            result=false;
        }

          
        return result;

    }

   

}