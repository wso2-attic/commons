wsas_tomcat_install_win32.rb
=======================

This script automates WSO2 WSAS deployment on Tomcat application server. In order to run the script, please follow the steps given below.

1. Install Ruby on your system (You may also need to install ruby_gnome2 along with ruby)
2. Copy wsas_tomcat_install_win32.rb to a directory in your win32 file system
3. Edit the following settings in wsas_tomcat_install_win32.rb 

$wsas_repo = "D:\\wsas\\wsas-repo"
$CARBON_BIN_HOME="D:\\wsas\\wsas-3.0\\wso2wsas-3.0.1"
$CATALINA_HOME="D:\\software\\apache-tomcat-6.0.14"

Make sure to use '\\' as file seperator. Here wsas_repo can be any location in your file system. CARBON_BIN_HOME is the location of the unzipped WSAS binary distribution.

3. Open a command shell and enter the following command
"ruby wsas_tomcat_install_win32.rb"

This will create new directories, configure the necessary files in both tomcat and WSAS. 

4. Now, you can start tomcat. Before starting tomcat, make sure to set CARBON_HOME environment variable and point it to wsas_repo directory.

i.e:- set CARBON_HOME=D:\\wsas\\wsas-repo

wsas_jboss_install_win32.rb
=======================

This script automates WSO2 WSAS deployment on JBoss 5.* application server. In order to run the script, please follow the steps given below.

1. Install Ruby on your system (You may also need to install ruby_gnome2 along with ruby)
2. Copy wsas_jboss_install_win32.rb to a directory in your win32 file system
3. Edit the following settings in wsas_jboss_install_win32.rb 

$wsas_repo = "D:\\wsas\\wsas-repo"
$CARBON_BIN_HOME="D:\\wsas\\wsas-3.0\\wso2wsas-3.0.1"
$JBOSS_HOME="D:\\software\\jboss-5.0.0"

Make sure to use '\\' as file seperator. Here wsas_repo can be any location in your file system. CARBON_BIN_HOME is the location of the unzipped WSAS binary distribution.

3. Open a command shell and enter the following command
"ruby wsas_jboss_install_win32.rb"

This will create new directories, configure the necessary files in both Jboss and WSAS. 

4. Now, you can start Jboss. Before starting Jboss, make sure to set CARBON_HOME environment variable and point it to wsas_repo directory.

i.e:- set CARBON_HOME=D:\\wsas\\wsas-repo
 
