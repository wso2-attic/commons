greg-tomcat-win32.rb
=======================

This script automates WSO2 Goveranance Registry deployment on Tomcat application server. In order to run the script, please follow the steps given below.

1. Install Ruby on your system (You may also need to install ruby_gnome2 along with ruby)
2. Copy greg-tomcat-win32.rb to a directory in your win32 file system
3. Edit the following settings in greg-tomcat-win32.rb 

$registry_repo = "C:\\registry\\registry-repo"
$CARBON_BIN_HOME="C:\\registry\\snapshot\\wso2registry-2.0.1"
$CATALINA_HOME="C:\\software_backup\\apache-tomcat-6.0.18"

Make sure to use '\\' as file seperator. Here registry_repo can be any location in your file system. CARBON_BIN_HOME is the location of the unzipped WSO2 Governance Registry binary distribution.

3. Open a command shell and enter the following command
"ruby greg-tomcat-win32"

This will create new directories, configure the necessary files in both tomcat and WSO2 Goveranance Registry. 

4. Now, you can start tomcat. Before starting tomcat, make sure to set CARBON_HOME environment variable and point it to registry_repo directory.

i.e:- set CARBON_HOME=C:\\registry\\registry-repo
      export CARONB_HOME=/path/to/registry-repo

greg-jboss-win32.rb
=======================

This script automates WSO2 Governance Registry deployment on JBoss 5.* application server. In order to run the script, please follow the steps given below.

1. Install Ruby on your system (You may also need to install ruby_gnome2 along with ruby)
2. Copy greg-jboss-win32.rb to a directory in your win32 file system
3. Edit the following settings in greg-jboss-win32.rb

$registry_repo = "C:\\registry\\registry-repo"
$CARBON_BIN_HOME="C:\\registry\\snapshot\\wso2registry-2.0.1"
$JBOSS_HOME="C:\\software_backup\\jboss-5.0.0.GA"

Make sure to use '\\' as file seperator. Here registry_repo can be any location in your file system. CARBON_BIN_HOME is the location of the unzipped WSO2 Governance Registry binary distribution.

3. Open a command shell and enter the following command
"ruby greg-jboss-win32.rb"

This will create new directories, configure the necessary files in both Jboss and WSO2 Goveranance Registry . 

4. Now, you can start Jboss. Before starting Jboss, make sure to set CARBON_HOME environment variable and point it to registry_repo directory.

i.e:- set CARBON_HOME=C:\\registry\\registry-repo
      export CARONB_HOME=/path/to/registry-repo
