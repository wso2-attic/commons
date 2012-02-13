require 'rexml/document'
include REXML

#Set wsas_repo (Any directory in the local file system), CARBON_BIN_HOME(Home directory of the CARBON binary distribution) and CATALINA_HOME (Home directory of the tomcat binary)

$wsas_repo = "/home/charitha/products/wsas/wsas-repo"
$CARBON_BIN_HOME="/home/charitha/products/wsas/wso2wsas-3.1.3"
$CATALINA_HOME="/home/charitha/software/apache-tomcat-6.0.14"

############################################################################################################

def createCarbonDirs()

system("mkdir #$wsas_repo")
system("mkdir #$wsas_repo/conf")
system("mkdir #$wsas_repo/repository")
system("mkdir #$wsas_repo/database")
system("mkdir #$wsas_repo/resources")
system ("cp -r  #$CARBON_BIN_HOME/conf/* #$wsas_repo/conf")
system ("cp -r  #$CARBON_BIN_HOME/repository/* #$wsas_repo/repository")
system ("cp -r  #$CARBON_BIN_HOME/database/* #$wsas_repo/database" )
system ("cp -r  #$CARBON_BIN_HOME/resources/* #$wsas_repo/resources")

end

################################################################################################################

def createTomcatDirs()

system("mkdir #$CATALINA_HOME/webapps/wsas")
system("mkdir #$CATALINA_HOME/webapps/wsas/WEB-INF")
system ("cp -r  #$CARBON_BIN_HOME/webapps/ROOT/WEB-INF/* #$CATALINA_HOME/webapps/wsas/WEB-INF")

end

###############################################################################################################

def enableHTTPSIntomcat()

tomcat_xml_file= "#{$CATALINA_HOME}/conf/server.xml"
carbon_ks_path="#$wsas_repo/resources/security/wso2carbon.jks"
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
carbon_xml_file= "#$wsas_repo/conf/carbon.xml"
 File.open("#{carbon_xml_file}") do |config_file|
  # Open the document and edit the port (carbon.xml)
   config = Document.new(config_file)
   config.root.elements['WebContextRoot'].text = '/wsas'
   puts config.root.elements['ServerURL']
   config.root.elements['ServerURL'].text = 'https://localhost:8443/wsas/services/'
   
    # Write the result to the same file.
   formatter = REXML::Formatters::Default.new
   File.open("#{carbon_xml_file}", 'w') do |result|
     formatter.write(config, result)
   end
end
end


####################################################################################################################

def updateRegistryxml()
registry_xml_file= "#$wsas_repo/conf/registry.xml"
db_path = "jdbc:h2:#$wsas_repo/database/WSO2CARBON_DB"
 File.open("#{registry_xml_file}") do |config_file|
  # Open the document and edit the port (registry.xml)
   config = Document.new(config_file)
   puts config.root.elements[4].elements['url']
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
usermgt_xml_file= "#$wsas_repo/conf/user-mgt.xml"
db_path = "jdbc:h2:#$wsas_repo/database/WSO2CARBON_DB"
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
axis2_xml_file= "#$wsas_repo/conf/axis2.xml"
 File.open("#{axis2_xml_file}") do |config_file|
  # Open the document and edit the port (axis2.xml)
   config = Document.new(config_file)
   puts config.root.elements['transportReceiver[@name="http"]'].elements['parameter']
   config.root.elements['transportReceiver[@name="http"]'].elements['parameter'].text = '8080'
   config.root.elements['transportReceiver[@name="https"]'].elements['parameter'].text = '8443'
   #config.root.elements['parameter[@name="contextRoot"]'].text='/wsas'
   
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
#system("set CARBON_HOME=D:\\wsas\\wsas-repo")
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
