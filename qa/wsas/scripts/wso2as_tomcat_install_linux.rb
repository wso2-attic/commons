require 'rexml/document'
require 'fileutils'
include REXML

#Set wso2as_repo (Any directory in the local file system), CARBON_BIN_HOME(Home directory of the CARBON binary distribution) and CATALINA_HOME (Home directory of the tomcat binary)

$wso2as_repo = "/home/charitha/products/wsas/wso2as-repo"
$CARBON_BIN_HOME="/home/charitha/products/wsas/wso2appserver-4.0.0-SNAPSHOT"
$CATALINA_HOME="/home/charitha/software/apache-tomcat-6.0.14"

###########################################################################################################################

def createCarbonDirs()

print "************* Creating wso2as Repository ********************** \n"


if File.directory?("#$wso2as_repo")
   FileUtils.rm_rf "#$wso2as_repo"

   Dir.mkdir("#$wso2as_repo")
   Dir.mkdir("#$wso2as_repo/repository")
   Dir.mkdir("#$wso2as_repo/resources")
   
else
Dir.mkdir("#$wso2as_repo")
Dir.mkdir("#$wso2as_repo/repository")
Dir.mkdir("#$wso2as_repo/resources")
end

system ("cp -r  #$CARBON_BIN_HOME/repository/* #$wso2as_repo/repository")
system ("cp -r  #$CARBON_BIN_HOME/resources/* #$wso2as_repo/resources")

end

###########################################################################################################################

def createTomcatDirs()

print "*************** Deploying wso2as webapp on tomcat ************************** \n" 

if File.directory?("#$CATALINA_HOME/webapps/wso2as")
   FileUtils.rm_rf "#$CATALINA_HOME/webapps/wso2as"

   Dir.mkdir("#$CATALINA_HOME/webapps/wso2as")
   Dir.mkdir("#$CATALINA_HOME/webapps/wso2as/WEB-INF")
     
else
Dir.mkdir("#$CATALINA_HOME/webapps/wso2as")
Dir.mkdir("#$CATALINA_HOME/webapps/wso2as/WEB-INF")
end

system ("cp -r  #$CARBON_BIN_HOME/lib/core/WEB-INF/* #$CATALINA_HOME/webapps/wso2as/WEB-INF")
system ("cp -r  #$CARBON_BIN_HOME/lib/log4j.properties #$CATALINA_HOME/webapps/wso2as/WEB-INF/classes")

end

##########################################################################################################################

def enableHTTPSIntomcat()

print "***************************** Configuring HTTPS in tomcat ******************** \n"

tomcat_xml_file= "#{$CATALINA_HOME}/conf/server.xml"
carbon_ks_path="#$wso2as_repo/resources/security/wso2carbon.jks"
file = File.new("#{tomcat_xml_file}", "r")
doc2 = Document.new(file)
#puts doc2

#Remove the Connector element if exists
connector_ele = doc2.root.elements['Service'].elements['Connector[@SSLEnabled="true"]']
doc2.root.elements['Service'].delete connector_ele


doc2.root.elements['Service'].add_element("Connector", {"maxSpareThreads" => "76", "port" => "8443", "protocol" => "HTTP/1.1", "SSLEnabled" => "true", "maxThreads" => "150", "scheme" => "https", "secure" => "true", "clientAuth" => "false", "sslProtocol" => "TLS", "keystoreFile" => "#{carbon_ks_path}", "keystorePass" => "wso2carbon"})


formatter = REXML::Formatters::Default.new
File.open("#{tomcat_xml_file}", 'w') do |result|
formatter.write(doc2, result)
 end
end

###########################################################################################################################

def updateCarbonxml()

print "*********************** Updating carbon.xml **************************** \n"

carbon_xml_file= "#$wso2as_repo/repository/conf/carbon.xml"
 File.open("#{carbon_xml_file}") do |config_file|
  # Open the document and edit the port (carbon.xml)
   config = Document.new(config_file)
   config.root.elements['WebContextRoot'].text = '/wso2as'
   #puts config.root.elements['ServerURL']
   config.root.elements['ServerURL'].text = 'https://localhost:8443/wso2as/services/'
   
    # Write the result to the same file.
   formatter = REXML::Formatters::Default.new
   File.open("#{carbon_xml_file}", 'w') do |result|
     formatter.write(config, result)
   end
end
end


############################################################################################################################

def updateRegistryxml()

print "************************* Updating registry.xml *************************** \n"

registry_xml_file= "#$wso2as_repo/repository/conf/registry.xml"
db_path = "jdbc:h2:#$wso2as_repo/repository/database/WSO2CARBON_DB"
 File.open("#{registry_xml_file}") do |config_file|
  # Open the document and edit the port (registry.xml)
   config = Document.new(config_file)
   #puts config.root.elements[4].elements['url']
   config.root.elements[4].elements['url'].text = "#{db_path}"
   
     #Write the result to the same file.
   formatter = REXML::Formatters::Default.new
   File.open("#{registry_xml_file}", 'w') do |result|
    formatter.write(config, result)
   end
end
end

#############################################################################################################################

def updateusermgtxml()

print "********************************* Updating user-mgt.xml ************************* \n"

usermgt_xml_file= "#$wso2as_repo/repository/conf/user-mgt.xml"
db_path = "jdbc:h2:#$wso2as_repo/repository/database/WSO2CARBON_DB"
 File.open("#{usermgt_xml_file}") do |config_file|
  # Open the document and edit the port (user_mgt.xml)
   config = Document.new(config_file)

   #puts config.root.elements[1].elements[1].elements['Property[@name="url"]'].text
   config.root.elements[1].elements[1].elements['Property[@name="url"]'].text = "#{db_path}"
   
     #Write the result to the same file.
   formatter = REXML::Formatters::Default.new
   File.open("#{usermgt_xml_file}", 'w') do |result|
    formatter.write(config, result)
   end
end
end

#############################################################################################################################

def updateCarbonaxis2xml()

print "******************Updating axis2.xml ************************* \n"

axis2_xml_file= "#$wso2as_repo/repository/conf/axis2.xml"
 File.open("#{axis2_xml_file}") do |config_file|
  # Open the document and edit the port (axis2.xml)
   config = Document.new(config_file)
   #puts config.root.elements['transportReceiver[@name="http"]'].elements['parameter']
   config.root.elements['transportReceiver[@name="http"]'].elements['parameter'].text = '8080'
   config.root.elements['transportReceiver[@name="https"]'].elements['parameter'].text = '8443'
   #config.root.elements['parameter[@name="contextRoot"]'].text='/wso2as'
   
     #Write the result to the same file.
   formatter = REXML::Formatters::Default.new
   File.open("#{axis2_xml_file}", 'w') do |result|
   formatter.write(config, result)
   end
end
end

###############################################################################################################################

def startTomcat()
#CATALINA_HOME1 = "#$CATALINA_HOME"
#puts "In parent, term = #{ENV['JAVA_HOME']}" 
#system("set CATALINA_HOME=D:\\software\\apache-tomcat-6.0.14")
#system("set CARBON_HOME=D:\\wso2as\\wso2as-repo")
#system("D:\\software\\apache-tomcat-6.0.14\\bin\\catalina.bat start") 

end


################################################################################################################################

createCarbonDirs()
createTomcatDirs()
updateCarbonxml()
updateRegistryxml()
updateusermgtxml()
updateCarbonaxis2xml()
enableHTTPSIntomcat()
#startTomcat()
