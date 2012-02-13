#!/usr/local/bin/ruby 
require 'fileutils' 
require "ftools"
 

if ENV['JAVA_HOME'] == nil
	print "You must set the JAVA_HOME variable before running CARBON."
end

# ----- Execute The Requested Command-----------------------------------
if File.exists?('reports')
  puts "deleted reports"
	FileUtils.rm_r 'reports'
end

puts "created reports"
FileUtils.mkdir_p 'reports'

reportName=File.join('reports','surefire_report.txt')
report=File.new(reportName,'a+')
target=File.join('target','surefire-reports','org.wso2.carbon.web.test.is.AllTests.txt')

tempErrors=File.new(File.join('reports','tempErrors.txt'),'a+')
tempFailures=File.new(File.join('reports','tempFailures.txt'),'a+')
tempSkips=File.new(File.join('reports','tempSkips.txt'),'a+')

run=0
failures=0
errors=0
skipped=0

#run class by class and get the count of total runs errors etc
File.open('is_test_suites.txt').each_line{ |s|
  puts s
  puts ARGV[0]
  
  
	if s != "\n"
		if ARGV.length == 1
			system("mvn -e clean install -DfirefoxProfileTemplate="+ARGV[0]+" -Dtest.suite=" + s)
			#system("mvn -e clean install  -Dtest.suite=" + s)
	        end

		if ARGV[0] == '-o'
			system(" mvn -e clean install -o -DfirefoxProfileTemplate="+ARGV[1]+" -Dtest.suite=" + s)
			#system("mvn -e clean install -o -Dtest.suite=" + s)
		end

		report.puts "\n\n##################################################"
		report.puts  "\t\t\t" + s
  		report.puts "##################################################"

		File.open(target).each_line { |h|
			
     			if h.match("Tests run:") != nil
		  		@ruby= h
				@type = @ruby.split(' ')
				run=run + @type[2].to_i 
				failures=failures + @type[4].to_i
				errors=errors + @type[6].to_i
				skipped=skipped + @type[8].to_i	

				if @type[4].to_i > 0
					tempFailures << s
				end
				if @type[6].to_i > 0
					tempErrors << s
				end
				if @type[8].to_i > 0
					tempSkips << s
				end
			end
		}

		File.open(target).each_line do |g|
     			report.puts g 
	  	end
    
  end

}
tempErrors.close
tempFailures.close
tempSkips.close

#make the report
report << "\nTotal Tests Run : " 
report << run
report << "\nTotal Failures  : " 
report << failures
report << "\nTotal Errors    : " 
report << errors
report << "\nTotal Skipped   : " 
report << skipped

#list error/failure/skipped suites
if errors > 0 
	report.puts "\n\n-------------------Suites with errors--------------"
	ma = IO.readlines(File.join('reports','tempErrors.txt'))
	report.puts ma
end

if failures > 0 
	report.puts"\n\n-------------------Suites with failures------------"
	ma = IO.readlines(File.join('reports','tempFailures.txt'))
	report.puts ma	

end

if skipped > 0 
	report.puts "\n\n-------------------Suites with skips--------------"
	ma = IO.readlines(File.join('reports','tempSkips.txt'))
	report.puts ma	

end

report.close

File.copy( File.join('reports','surefire_report.txt'),File.join('target','surefire-reports'))
FileUtils.rm_r 'reports'


