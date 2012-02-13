#!/usr/bin/ruby
require 'rexml/document'
require 'fileutils'
include REXML

#Set greg_repo (Any directory in the local file system), CARBON_BIN_HOME(Home directory of the CARBON binary distribution) and CATALINA_HOME (Home directory of the tomcat binary)

$greg_repo="/home/krishantha/Desktop/greg/greg-repo/test"
$CARBON_BIN_HOME="/home/krishantha/Desktop/greg/wso2greg-3.0.3"
$CATALINA_HOME="/home/krishantha/tomcat/apache-tomcat-6.0.20"
$JAVA_HOME="/usr/local/jdk1.6.0_06"
$G_REG_VERSION=""

############################################################################################################

def createCarbonDirs()

system("mkdir #$greg_repo")
system("mkdir #$greg_repo/conf")
system("mkdir #$greg_repo/repository")
system("mkdir #$greg_repo/database")
system("mkdir #$greg_repo/resources")
system ("cp -r  #$CARBON_BIN_HOME/conf/* #$greg_repo/conf/ ")
system ("cp -r  #$CARBON_BIN_HOME/repository/* #$greg_repo/repository ")
system ("cp -r  #$CARBON_BIN_HOME/database/* #$greg_repo/database " )
system ("cp -r  #$CARBON_BIN_HOME/resources/* #$greg_repo/resources ")

end

################################################################################################################

def createTomcatDirs()

system("mkdir #$CATALINA_HOME/webapps/greg")
system("mkdir #$CATALINA_HOME/webapps/greg/WEB-INF")
system ("cp -r  #$CARBON_BIN_HOME/webapps/ROOT/WEB-INF/* #$CATALINA_HOME/webapps/greg/WEB-INF ")
system ("cp #$CARBON_BIN_HOME/lib/log4j.properties #$CATALINA_HOME/webapps/greg/WEB-INF/classes")

end

###############################################################################################################

def enableHTTPSIntomcat()

tomcat_xml_file= "#{$CATALINA_HOME}/conf/server.xml"
carbon_ks_path="#$greg_repo/resources/security/wso2carbon.jks"
file = File.new("#{tomcat_xml_file}", "r")
doc2 = Document.new(file)
#puts doc2
doc2.root.elements['Service'].add_element("Connector", {"maxSpareThreads" => "76", "port" => "8443", "protocol" => "HTTP/1.1", "SSLEnabled" => "true", "maxThreads" => "150", "scheme" => "https", "secure" => "true", "clientAuth" => "false", "sslProtocol" => "TLS", "keystoreFile" => "#{carbon_ks_path}", "keystorePass" => "wso2carbon"})
formatter = REXML::Formatters::Default.new
File.open("#{tomcat_xml_file}", 'w') do |result|
formatter.write(doc2, result)
 end
end

#################################################################################################################

def updateCarbonxml()
carbon_xml_file= "#$greg_repo/conf/carbon.xml"
 File.open("#{carbon_xml_file}") do |config_file|
  # Open the document and edit the port (carbon.xml)
   config = Document.new(config_file)

  #get the greg Product Version
   $G_REG_VERSION=config.root.elements['Version'].text 
   puts "**************"
   puts $G_REG_VERSION
   puts "**************"
   	if($G_REG_VERSION=="2.0.2")
   		config.root.elements['WebContext'].text='/greg'
   	elsif($G_REG_VERSION=="2.1.1" || $G_REG_VERSION=="3.0.3")		
   		config.root.elements['WebContextRoot'].text = '/greg'
   		puts config.root.elements['ServerURL']
   	end
   config.root.elements['ServerURL'].text = 'https://localhost:8443/greg/services/'
   
    # Write the result to the same file.
   formatter = REXML::Formatters::Default.new
   File.open("#{carbon_xml_file}", 'w') do |result|
     formatter.write(config, result)
   end
end
end


####################################################################################################################

def updateRegistryxml()
registry_xml_file= "#$greg_repo/conf/registry.xml"
db_path = "jdbc:h2:#$greg_repo/database/WSO2CARBON_DB"
 File.open("#{registry_xml_file}") do |config_file|
  # Open the document and edit the port (registry.xml)
   config = Document.new(config_file)
   if $G_REG_VERSION=="2.1.0"
   		puts config.root.elements[2].elements['url']
   		config.root.elements[2].elements['url'].text = "#{db_path}"
   elsif $G_REG_VERSION=="2.1.1" || $G_REG_VERSION=="3.0.3"
   		puts config.root.elements[4].elements['url']
   		config.root.elements[4].elements['url'].text = "#{db_path}"
   end
   
     #Write the result to the same file.
   formatter = REXML::Formatters::Default.new
   File.open("#{registry_xml_file}", 'w') do |result|
    formatter.write(config, result)
   end
end
end

#############################################################################################################################

def updateusermgtxml()
usermgt_xml_file= "#$greg_repo/conf/user-mgt.xml"
db_path = "jdbc:h2:#$greg_repo/database/WSO2CARBON_DB"
 File.open("#{usermgt_xml_file}") do |config_file|
  # Open the document and edit the port (user_mgt.xml)
   config = Document.new(config_file)
   puts config.root.elements[1].elements['URL']
   config.root.elements[1].elements['URL'].text = "#{db_path}"
   
     #Write the result to the same file.
   formatter = REXML::Formatters::Default.new
   File.open("#{usermgt_xml_file}", 'w') do |result|
    formatter.write(config, result)
   end
end
end


##############################################################################################################################

def updateCarbonaxis2xml()
axis2_xml_file= "#$greg_repo/conf/axis2.xml"

 File.open("#{axis2_xml_file}") do |config_file|
  # Open the document and edit the port (axis2.xml)
   config = Document.new(config_file)
   config.root.elements['transportReceiver[@name="http"]'].elements['parameter'].text = '8080'
   config.root.elements['transportReceiver[@name="https"]'].elements['parameter'].text = '8443'
      
     #Write the result to the same file.
   formatter = REXML::Formatters::Default.new
   File.open("#{axis2_xml_file}", 'w') do |result|
   formatter.write(config, result)
   end
end
end

##############################################################################################################################

def updateCarbonTrasportxml()
transports_xml_file="#$greg_repo/conf/transports.xml"

 File.open("#{transports_xml_file}") do |config_file|
  # Open the document and edit the port (transports.xml)
   config = Document.new(config_file)
   config.root.elements['transport[@name="http"]]'].elements['parameter'].text = '8080'
   config.root.elements['transport[@name="https"]]'].elements['parameter'].text = '8443'
      
     #Write the result to the same file.
   formatter = REXML::Formatters::Default.new
   File.open("#{transports_xml_file}", 'w') do |result|
   formatter.write(config, result)
   end
end
end


###############################################################################################################################


def startTomcat()
  startUpHome=$CATALINA_HOME+"/bin"
  ENV["CARBON_HOME"]=$greg_repo
  puts ENV["CARBON_HOME"] # => $greg_repo
  Dir.chdir(File.join($CATALINA_HOME,'bin'))
  #system "ls"
  #system('chmod +x run.sh')
  system('chmod 777 *')
  system('sh catalina.sh run')  
end


################################################################################################################################

createCarbonDirs()
createTomcatDirs()
updateCarbonxml()
updateRegistryxml()
updateusermgtxml()
updateCarbonaxis2xml()
updateCarbonTrasportxml()
enableHTTPSIntomcat()
startTomcat()




