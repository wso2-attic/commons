#!/usr/bin/ruby
require 'rexml/document'
require 'fileutils'
include REXML

#Set greg_repo (Any directory in the local file system), CARBON_BIN_HOME(Home directory of the CARBON binary distribution) and JBOSS_HOME (Home directory of the jboss binary)

$greg_repo="/home/krishantha/Desktop/greg/greg-jboss-repo"
$CARBON_BIN_HOME="/home/krishantha/Desktop/greg/wso2greg-3.0.3"
$JBOSS_HOME="/home/krishantha/jboss/jboss-5.0.0.GA"
#$G-REG_VERSION="3.0.3"

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

def createJbossDirs()

system("mkdir #$JBOSS_HOME/server/default/deploy/greg.war")
system("mkdir #$JBOSS_HOME/server/default/deploy/greg.war/WEB-INF")
system ("cp -r #$CARBON_BIN_HOME/webapps/ROOT/WEB-INF/* #$JBOSS_HOME/server/default/deploy/greg.war/WEB-INF ")
system ("cp #$CARBON_BIN_HOME/lib/log4j.properties #$JBOSS_HOME/server/default/deploy/greg.war/WEB-INF/classes")
system ("cp #$CARBON_BIN_HOME/lib/endorsed/* #$JBOSS_HOME/lib/endorsed")

end

###############################################################################################################

def enableHTTPSInjboss()

jboss_xml_file= "#{$JBOSS_HOME}/server/default/deploy/jbossweb.sar/server.xml"
carbon_ks_path="#$greg_repo/resources/security/wso2carbon.jks"
file = File.open("#{jboss_xml_file}", "r")
doc2 = Document.new(file)
#puts doc2
doc2.root.elements['Service'].add_element("Connector", {"maxSpareThreads" => "76", "port" => "8443", "protocol" => "HTTP/1.1", "address" => "${jboss.bind.address}", "SSLEnabled" => "true", "maxThreads" => "150", "scheme" => "https", "secure" => "true", "clientAuth" => "false", "sslProtocol" => "TLS", "keystoreFile" => "#{carbon_ks_path}", "keystorePass" => "wso2carbon"})
formatter = REXML::Formatters::Default.new
File.open("#{jboss_xml_file}", 'w') do |result|
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

#############################################################################################################################

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

def startJboss()
  startUpHome=$JBOSS_HOME+"/bin"
  ENV["CARBON_HOME"]=$greg_repo
  puts ENV["CARBON_HOME"] # => $greg_repo
  puts ENV["JBOSS_HOME"] # => $JBOSS_HOME
  Dir.chdir(File.join($JBOSS_HOME,'bin'))
  system "ls"
  #system('chmod +x run.sh')
  system('sh run.sh start')  
end


###############################################################################################################################
createCarbonDirs()
createJbossDirs()
updateCarbonxml()
updateRegistryxml()
updateusermgtxml()
updateCarbonaxis2xml()
updateCarbonTrasportxml()
enableHTTPSInjboss()
startJboss()

