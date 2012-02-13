#!/usr/local/bin/ruby 
require 'fileutils' 
require "ftools"
require 'rexml/document'
include REXML
include FileUtils

#method to edit carbon.xml
#--------------------------------------------------------------------------------------------------------------
def edit_carbonXML(carbon_home,url_port,url_contextRoot,contextRoot)  
	File.open(File.join(carbon_home , 'conf','carbon.xml')) do |config_file|
		# Open the document and edit the port (carbon.xml)
		config = Document.new(config_file)
		if !url_port.eql? ""
			config.root.elements['ServerURL'].text = 'https://localhost:' + url_port + url_contextRoot + '/services/'
		end		
			config.root.elements['WebContextRoot'].text = contextRoot

		# Write the result to a new file.
		formatter = REXML::Formatters::Default.new
		File.open(File.join(carbon_home , 'conf','result_carbon.xml'), 'w') do |result|
		formatter.write(config, result)
		end
	end 
	File.delete(File.join(carbon_home , 'conf','carbon.xml'))
	File.rename( File.join(carbon_home , 'conf','result_carbon.xml'),File.join(carbon_home , 'conf','carbon.xml') )


end 
#-------------------------------------------------------------------------------------------------------------

#method to edit transport.xml
#-------------------------------------------------------------------------------------------------------------
def edit_transportXML(carbon_home,http_port,https_port)  


	File.open(File.join(carbon_home , 'conf','transports.xml')) do |config_file|
		# Open the document and edit the port (transport.xml)
		doc= Document.new(config_file)
		
		if doc.root.elements[1].attributes['name'].eql? "http"
			doc.root.elements[1].elements["parameter"].text=http_port
		else
			puts "Cannot find http transport element" 
			exit
		end
		
		if doc.root.elements[2].attributes['name'].eql? "https"
			doc.root.elements[2].elements["parameter"].text=https_port
		else
			puts "Cannot find https transport element"
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

#get inputs
puts "Source directory is the location where you extracted CARBON binary distribution. i.e:CARBON_HOME"
puts "1. Enter the source directory path       :"
Current_Path=gets

puts "Destination directory is the location where you wish to place front-end and back-end servers"
puts "2. Enter the destination directory path  :"
New_Path=gets

puts "3. Front End server http port   :"
FE_http_port=gets.chomp
puts "4. Front End server https port  :"
FE_https_port=gets.chomp
puts "5. Back End server http port    :"
BE_http_port=gets.chomp
puts "6. Back End server https port   :"
BE_https_port=gets.chomp

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


FE_Carbon_Home = File.join(Path , 'FE' )# @type[i-1].chomp
BE_Carbon_Home = File.join(Path , 'BE' )# @type[i-1].chomp
puts "FE_Carbon_Home :" + FE_Carbon_Home
puts "BE_Carbon_Home :" + BE_Carbon_Home

#deletes previous copies of FE_SERVER and BE_SERVER if exists
if File.exists?(FE_Carbon_Home)
  	FileUtils.rm_r FE_Carbon_Home
end

if File.exists?(BE_Carbon_Home)
 	FileUtils.rm_r BE_Carbon_Home
end

# Recursively copy the first directory into the second
#system("cp -r " + Current_Path.chomp + " " + FE_Carbon_Home.chomp )
#system("cp -r " + Current_Path.chomp + " " + BE_Carbon_Home.chomp)
cp_r Current_Path.chomp,FE_Carbon_Home.chomp
cp_r Current_Path.chomp,BE_Carbon_Home.chomp 


#change webapps
#remove server in feserver
FE_Server=File.join(FE_Carbon_Home , 'webapps','ROOT','WEB-INF','plugins','server')
FileUtils.rm_r FE_Server

#remove console in beserver
BE_Console=File.join(BE_Carbon_Home , 'webapps','ROOT','WEB-INF','plugins','console')
FileUtils.rm_r BE_Console

#rename feserver ROOT to feserver
File.rename(File.join(FE_Carbon_Home , 'webapps','ROOT'), File.join(FE_Carbon_Home , 'webapps','FE-SERVER'))
#rename beserver ROOT to beserver
File.rename(File.join(BE_Carbon_Home , 'webapps','ROOT'), File.join(BE_Carbon_Home , 'webapps','BE-SERVER'))



#change carbon.xml
edit_carbonXML(FE_Carbon_Home,BE_https_port,'/BE-SERVER','/FE-SERVER')
edit_carbonXML(BE_Carbon_Home,"","",'/BE-SERVER')

#change transport.xml
edit_transportXML(FE_Carbon_Home,FE_http_port,FE_https_port)
edit_transportXML(BE_Carbon_Home,BE_http_port,BE_https_port)

#start two servers
Dir.chdir(File.join(FE_Carbon_Home,'bin'))
read, write = IO.pipe

Process.fork do
	Dir.chdir(File.join(FE_Carbon_Home,'bin'))
	puts 'starting Front End server'
	if RUBY_PLATFORM.downcase.include?("mswin")
		`wso2server.bat`
	elsif RUBY_PLATFORM.downcase.include?("linux")
		`sh wso2server.sh`
	end
end

Process.fork do
	puts 'starting Back End server'
	Dir.chdir(File.join(BE_Carbon_Home,'bin'))
	if RUBY_PLATFORM.downcase.include?("mswin")
		`wso2server.bat`
	elsif RUBY_PLATFORM.downcase.include?("linux")
		`sh wso2server.sh`
	end

end
 
puts "\nFront End management console URL :https://localhost:" + FE_https_port + "/FE-SERVER/carbon/"
#puts "FE server URL :https://localhost:" + BE_http_port + "/BE-SERVER/services/"
Process.wait
Process.wait




