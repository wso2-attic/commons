is_tomcat_install_win32.rb
=======================

This script automates WSO2 IS deployment on Tomcat application server. In order to run the script, please follow the steps given below.

1. Install Ruby on your system (You may also need to install ruby_gnome2 along with ruby)
2. Copy is_tomcat_install_win32.rb to a directory in your win32 file system
3. Edit the following settings in is_tomcat_install_win32.rb 

$is_repo = "D:\\is\\is-repo"
$CARBON_BIN_HOME="D:\\Testing\\IS\\wso2is-2.0.0.M1\\wso2is-2.0.0.M1"
$CATALINA_HOME="D:\\software\\apache-tomcat-6.0.14"

Make sure to use '\\' as file seperator. Here is_repo can be any location in your file system. CARBON_BIN_HOME is the location of the unzipped IS binary distribution.

3. Open a command shell and enter the following command
"ruby is_tomcat_install_win32.rb"

This will create new directories, configure the necessary files in both tomcat and IS. 

4. Now, you can start tomcat. Before starting tomcat, make sure to set CARBON_HOME environment variable and point it to is_repo directory.

i.e:- set CARBON_HOME=D:\\is\\is-repo

5. Access IS from https://localhost:8443/is/carbon/





is_jboss_install_win32.rb
=======================

This script automates WSO2 IS deployment on JBoss 5.* application server. In order to run the script, please follow the steps given below.

1. Install Ruby on your system (You may also need to install ruby_gnome2 along with ruby)
2. Copy is_jboss_install_win32.rb to a directory in your win32 file system
3. Edit the following settings in is_jboss_install_win32.rb 

$is_repo = "D:\\is\\is-repo"
$CARBON_BIN_HOME="D:\\is\\wso2is-SNAPSHOT"
$JBOSS_HOME="D:\\software\\jboss-5.0"

Make sure to use '\\' as file seperator. Here is_repo can be any location in your file system. CARBON_BIN_HOME is the location of the unzipped IS binary distribution.

3. Open a command shell and enter the following command
"ruby is_jboss_install_win32.rb"

This will create new directories, configure the necessary files in both Jboss and IS. 

4. Now, you can start Jboss. Before starting Jboss, make sure to set CARBON_HOME environment variable and point it to is_repo directory.

i.e:- set CARBON_HOME=D:\\is\\is-repo

5. Access IS from https://localhost:8443/wso2is/carbon/
 
