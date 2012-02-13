#!/usr/local/bin/ruby 
require 'fileutils' 
require "ftools"
require 'rexml/document'
include REXML
include FileUtils



#-------------------------------------------------------------------------------------------------------------
#method to edit axis2.xml
#--------------------------------------------------------------------------------------------------------------
def edit_axis2XML(carbon_home,http_port,https_port)  

	File.open(File.join(carbon_home , 'conf','axis2.xml')) do |config_file|
		# Open the document and edit the port (axis2.xml)
		config = Document.new(config_file)
		
		config.root.elements[25].elements[1].text=http_port
		config.root.elements[26].elements[1].text=https_port
	
		config.root.elements['clustering'].attributes['enable']='true'
		config.root.elements['clustering'].elements[4].text="wso2.org.esb"

		ele1=Element.new("parameter")
		ele1.text = "127.0.0.1"
		ele1.add_attribute("name", "mcastBindAddress")

		ele2=Element.new("parameter")
		ele2.text = "127.0.0.1"
		ele2.add_attribute("name", "localMemberHost")

		ele3=Element.new("parameter")
		ele3.add_attribute("name", "domain")

		config.root.elements.each('//parameter') {|element| 
			
			if(element.attributes == ele1.attributes)
				element.parent.delete(element)
			end

			if(element.attributes == ele2.attributes)
				element.parent.delete(element)
			end

			
		}
		

		# Write the result to a new file.
		formatter = REXML::Formatters::Default.new
		File.open(File.join(carbon_home , 'conf','result_axis2.xml'), 'w') do |result|
		formatter.write(config, result)
		end
	end 
	File.delete(File.join(carbon_home , 'conf','axis2.xml'))
	File.rename( File.join(carbon_home , 'conf','result_axis2.xml'),File.join(carbon_home , 'conf','axis2.xml') )

end 
#-------------------------------------------------------------------------------------------------------------


#method to edit transport.xml
#--------------------------------------------------------------------------------------------------------------
def edit_transportXML(carbon_home,https_port)  

	File.open(File.join(carbon_home , 'conf','transports.xml')) do |config_file|
		# Open the document and edit the port (transport.xml)
		doc= Document.new(config_file)
			
		if doc.root.elements['transport'].attributes['name'].eql? "https"
			doc.root.elements['transport'].elements["parameter"].text=https_port
		else
			puts "Cannot find https transport element in transport.xml"
			exit
		end		

		# Write the result to a new file.
		formatter = REXML::Formatters::Default.new
		File.open(File.join(carbon_home , 'conf','result_transports.xml'), 'w') do |result|
		formatter.write(doc, result)
		end
	end 
	File.delete(File.join(carbon_home , 'conf','transports.xml'))
	File.rename( File.join(carbon_home , 'conf','result_transports.xml'),File.join(carbon_home , 'conf','transports.xml') )
end 
#-------------------------------------------------------------------------------------------------------------

#method to edit registry.xml
#--------------------------------------------------------------------------------------------------------------
def edit_registry(carbon_home,is_readOnly)  

	File.open(File.join(carbon_home , 'conf','registry.xml')) do |config_file|
	# Open the document and edit the port (carbon.xml)
	config = Document.new(config_file)
	config.root.elements['readOnly'].text =is_readOnly
	config.root.elements['registryRoot'].text= '/esb-root'
	
	# Write the result to a new file.
	formatter = REXML::Formatters::Default.new
	File.open(File.join(carbon_home , 'conf','result_registry.xml'), 'w') do |result|
	formatter.write(config, result)
	end
end 
File.delete(File.join(carbon_home , 'conf','registry.xml'))
File.rename( File.join(carbon_home , 'conf','result_registry.xml'),File.join(carbon_home , 'conf','registry.xml') )

end 

#-------------------------------------------------------------------------------------------------------------

#method to edit build.xml
#--------------------------------------------------------------------------------------------------------------
def edit_build(carbon_home,location,usr,pwd,url,dialects,driver)  

	File.open(File.join(Dir.pwd,'build.xml')) do |config_file|
		# Open the document and edit the port (carbon.xml)
		config = Document.new(config_file)
	
		config.root.elements[14].attributes['value']=carbon_home

		if(dialects == 'mysql')
			config.root.elements[21].attributes['value']=location
			config.root.elements[22].attributes['value']=usr
			config.root.elements[23].attributes['value']=pwd
			config.root.elements[24].attributes['value']=url
			config.root.elements[25].attributes['value']=dialects
			config.root.elements[26].attributes['value']=driver
		end

		if(dialects == 'oracle')
			config.root.elements[15].attributes['value']=location
			config.root.elements[16].attributes['value']=usr
			config.root.elements[17].attributes['value']=pwd
			config.root.elements[18].attributes['value']=url
			config.root.elements[19].attributes['value']=dialects
			config.root.elements[20].attributes['value']=driver
		end

	
	
		# Write the result to a new file.
		formatter = REXML::Formatters::Default.new
		File.open(File.join('result_build.xml'), 'w') do |result|
		formatter.write(config, result)
		end
	
	end 

	File.delete(File.join('build.xml'))
	File.rename( File.join('result_build.xml'),File.join('build.xml') )

	#call buld.xml to setup the connection with database
	if(dialects == 'mysql')
		if RUBY_PLATFORM.downcase.include?("mswin")
			`ant startServerWindows -Dscript=normal -Ddatabase.name=mysql`
		elsif RUBY_PLATFORM.downcase.include?("linux")
			`ant startServerUnix -Dscript=normal -Ddatabase.name=mysql`
		end
	end

	if(dialects == 'oracle')
		if RUBY_PLATFORM.downcase.include?("mswin")
			`ant startServerWindows -Dscript=normal -Ddatabase.name=oracle`
		elsif RUBY_PLATFORM.downcase.include?("linux")
			`ant startServerUnix -Dscript=normal -Ddatabase.name=oracle`
		end
	end

end 

#-------------------------------------------------------------------------------------------------------------
#get inputs
puts "Source directory is the location where you extracted CARBON binary distribution. i.e:CARBON_HOME"
puts "\nEnter the source ESB path       :"
Current_Path=gets.chomp

puts "Source directory is the location where you want your cluster the servers"
puts "\nEnter the destination path      :"
New_Path=gets.chomp

puts "ESB server 1 nio http transport port   :"
S1_nio_http_port=gets.chomp

puts "ESB server  nio https transport port   :"
S1_nio_https_port=gets.chomp

puts "ESB server 1 management console port   :"
server1_port=gets.chomp

puts "ESB server 2 nio http transport port    :"
S2_nio_http_port=gets.chomp

puts "ESB server 2 nio https transport port   :"
S2_nio_https_port=gets.chomp

puts "ESB server 2 management console port    :"
server2_port=gets.chomp


puts "Enter the source Greg path      :"
Greg_Carbon_Home=gets.chomp


puts "driver location  :"
driver_location=gets.chomp
puts "userName         :"
userName=gets.chomp
puts "password         :"
password=gets.chomp
puts "connection url   :"
connection_url=gets.chomp
puts "dialect           :"
dialect=gets.chomp
puts "driver name      :"
driver_name=gets.chomp

puts "Source path       :" + Current_Path
puts "Destination path  :" + New_Path
#-------------------------------------------------------------------------------------------------------------

Path=New_Path.chomp 

#checks whether the source path is valid
if !File.exists?(Current_Path.chomp)
	puts "Source path not found!"
	exit
end

#checks whether the destination path is valid
if !File.exists?(New_Path.chomp)
	FileUtils.mkdir_p New_Path.chomp
end

Server1_Carbon_Home = File.join(Path , 'one' )
Server2_Carbon_Home = File.join(Path , 'two' )


puts "\nserver1_carbon_home   :"+Server1_Carbon_Home 
puts "server2_carbon_home   :"+Server2_Carbon_Home 
#deletes previous copies of servers if exists
if File.exists?(Server1_Carbon_Home)
  	FileUtils.rm_r Server1_Carbon_Home
end

if File.exists?(Server2_Carbon_Home)
 	FileUtils.rm_r Server2_Carbon_Home
end


# Recursively copy the first directory into the second
cp_r Current_Path.chomp,Server1_Carbon_Home.chomp
cp_r Current_Path.chomp,Server2_Carbon_Home.chomp 


#edit axis2.xmls
edit_axis2XML(Server1_Carbon_Home,S1_nio_http_port,S1_nio_https_port)
edit_axis2XML(Server2_Carbon_Home,S2_nio_http_port,S2_nio_https_port)

#edit transport.xmls
edit_transportXML(Server1_Carbon_Home,server1_port)
edit_transportXML(Server2_Carbon_Home,server2_port)


#edit edit_registry.xml
edit_registry(Server1_Carbon_Home,'false')
edit_registry(Server2_Carbon_Home,'true')

edit_build(Server1_Carbon_Home,driver_location,userName,password,connection_url,dialect,driver_name)
edit_build(Server2_Carbon_Home,driver_location,userName,password,connection_url,dialect,driver_name)
edit_build(Greg_Carbon_Home,driver_location,userName,password,connection_url,dialect,driver_name) 

#start three servers
read, write = IO.pipe
Process.fork do
	Dir.chdir(File.join(Greg_Carbon_Home,'bin'))
	puts 'starting Greg server'
	if RUBY_PLATFORM.downcase.include?("mswin")
		`wso2server.bat -Dsetup`
	elsif RUBY_PLATFORM.downcase.include?("linux")
		`sh wso2server.sh -Dsetup`
	end
end

sleep 60

Process.fork do
	Dir.chdir(File.join(Server1_Carbon_Home,'bin'))
	puts 'starting esb server 1'
	if RUBY_PLATFORM.downcase.include?("mswin")
		`wso2server.bat`
	elsif RUBY_PLATFORM.downcase.include?("linux")
		`sh wso2server.sh`
	end
end

sleep 60

Process.fork do
	puts 'starting esb server 2'
	Dir.chdir(File.join(Server2_Carbon_Home,'bin'))
	if RUBY_PLATFORM.downcase.include?("mswin")
		`wso2server.bat`
	elsif RUBY_PLATFORM.downcase.include?("linux")
		`sh wso2server.sh`
	end

end

puts "\nGreg server URL :https://localhost:9443/carbon/"
puts "ESB server 1 URL :https://localhost:" + server1_port + "/carbon/"
puts "ESB server 2 URL :https://localhost:" + server2_port + "/carbon/"

Process.wait
Process.wait
Process.wait


