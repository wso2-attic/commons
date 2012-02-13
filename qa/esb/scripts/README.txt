Tomcat
======
esb_tomcat_install_win32.rb
---------------------------

This script automates WSO2 ESB deployment on Tomcat application server. In order to run the script, please follow the steps given below.

1. Install Ruby on your system (You may also need to install ruby_gnome2 along with ruby)
2. Copy esb_tomcat_install_win32.rb to a directory in your win32 file system
3. Edit the following settings in esb_tomcat_install_win32.rb 

$esb_repo = "D:\\esb\\esb-repo"
$CARBON_BIN_HOME="D:\\esb\\wso2esb-SNAPSHOT"
$CATALINA_HOME="D:\\software\\apache-tomcat-6.0.14"

Make sure to use '\\' as file seperator. Here esb_repo can be any location in your file system. CARBON_BIN_HOME is the location of the unzipped ESB binary distribution.

3. Open a command shell and enter the following command
"ruby esb_tomcat_install_win32.rb"

This will create new directories, configure the necessary files in both tomcat and ESB. 

4. Now, you can start tomcat. Before starting tomcat, make sure to set CARBON_HOME environment variable and point it to esb_repo directory.

i.e:- set CARBON_HOME=D:\\esb\\esb-repo

JBoss
=====
esb_jboss_install_win32.rb
--------------------------

This script automates WSO2 ESB deployment on JBoss 5.* application server on Windows. In order to run the script, please follow the steps given below.

1. Install Ruby on your system (You may also need to install ruby_gnome2 along with ruby)
2. Copy esb_jboss_install_win32.rb to a directory in your win32 file system
3. Edit the following settings in esb_jboss_install_win32.rb 

$esb_repo = "D:\\esb\\esb-repo"
$CARBON_BIN_HOME="D:\\esb\\wso2esb-SNAPSHOT"
$JBOSS_HOME="D:\\software\\jboss-5.0"

Make sure to use '\\' as file seperator. Here esb_repo can be any location in your file system. CARBON_BIN_HOME is the location of the unzipped ESB binary distribution.

3. Open a command shell and enter the following command
"ruby esb_jboss_install_win32.rb"

This will create new directories, configure the necessary files in both Jboss and ESB. 

4. Now, you can start Jboss. Before starting Jboss, make sure to set CARBON_HOME environment variable and point it to esb_repo directory.

i.e:- set CARBON_HOME=D:\\esb\\esb-repo


esb_jboss_install_linux.rb
--------------------------

This script automates WSO2 ESB deployment on JBoss 5.* application server on linux. In order to run the script, please follow the steps given below.

1. Install Ruby on your system (You may also need to install ruby_gnome2 along with ruby)
2. Copy esb_jboss_install_win32.rb to a directory in your win32 file system
3. Edit the following settings in esb_jboss_install_win32.rb 

$esb_repo = "/var/temp/esb-repo"
$CARBON_BIN_HOME="/path/to/unzipped/esb"
$JBOSS_HOME="/path/to/jboss/installtion"

CARBON_BIN_HOME is the location of the unzipped ESB binary distribution.

3. Open a command shell and enter the following command
"ruby esb_jboss_install_.linux.rb"

This will create new directories, configure the necessary files in both Jboss and ESB. 

4. Now, you can start Jboss. Before starting Jboss, make sure to set CARBON_HOME environment variable and point it to esb_repo directory.

i.e:- export CARBON_HOME=/path/to/esb-repo
 
