#!/usr/local/bin/ruby 
require 'fileutils' 
require "ftools"
require "cgi"


puts "\n1. Enter the carbon_home path:"
Carbon_Home=gets.chomp

puts "2. Enter the version           :"
Version=gets.chomp
puts "==============================================================="
#checks whether the source path is valid
if !File.exists?(Carbon_Home)
	puts "\nCarbon home not found!!!"
	exit
end

#checks whether release-notes.html exists 
if File.exists?(File.join(Carbon_Home , 'release-notes.html'))
	puts "\n* release-notes.html exists"
	is_version_correct=false

	File.open(File.join(Carbon_Home , 'release-notes.html')).each_line{ |s|
		if s.include?(Version)
			is_version_correct=true
			break
		end
	}
	if is_version_correct
		puts "* release version is correct in release-notes.html!!!"
	else
		puts "* [ERROR] : release version is incorrect in release-notes.html !!!"
	end
else
	puts "\n* [ERROR] : release-notes.html not found!"
end

#checks whether README.txt exists
if File.exists?(File.join(Carbon_Home , 'README.txt'))
	puts "\n* README.txt exists"
	is_version_correct=false
	File.open(File.join(Carbon_Home , 'README.txt')).each_line{ |s|
  		if s.include?(Version)
			is_version_correct=true
			break
		end
	}

	if is_version_correct
		puts "* release version is correct in README.txt!!!"
	else
		puts "* [ERROR] : release version is incorrect in README.txt!!!"
	end

else
	puts "\n* [ERROR] : README.txt not found!!!"
end

#checks whether INSTALL.txt exists
if File.exists?(File.join(Carbon_Home , 'INSTALL.txt'))
	puts "\n* INSTALL.txt exists"
else
	puts "\n* [ERROR] : INSTALL.txt not found!!!"
end

#checks whether LICENSE.txt exists
if File.exists?(File.join(Carbon_Home , 'LICENSE.txt'))
	puts "\n* LICENSE.txt exists"
else
	puts "\n* [ERROR] : LICENSE.txt not found!!!"
end
