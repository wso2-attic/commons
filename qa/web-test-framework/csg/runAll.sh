#!/bin/sh
# ----------------------------------------------------------------------------
#  Copyright 2005-2009 WSO2, Inc. http://www.wso2.org
#
#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.

# ----------------------------------------------------------------------------
# Main Script for the WSO2 Carbon Server
#
# Environment Variable Prequisites
#
#   JAVA_HOME       Must point at your Java Development Kit installation.
#
#   JAVA_OPTS       (Optional) Java runtime options used when the commands
#                   is executed.
#
# NOTE: Borrowed generously from Apache Tomcat startup scripts.
# -----------------------------------------------------------------------------
#check for JAVA_HOME

if [ -z "$JAVACMD" ] ; then
  if [ -n "$JAVA_HOME"  ] ; then
    if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
      # IBM's JDK on AIX uses strange locations for the executables
      JAVACMD="$JAVA_HOME/jre/sh/java"
    else
      JAVACMD="$JAVA_HOME/bin/java"
    fi
  else
    JAVACMD=java
  fi
fi

if [ ! -x "$JAVACMD" ] ; then
  echo "Error: JAVA_HOME is not defined correctly."
  echo " Test Framework cannot be executed $JAVACMD"
  exit 1
fi

# if JAVA_HOME is not set we're not happy
if [ -z "$JAVA_HOME" ]; then
  echo "You must set the JAVA_HOME variable before running Test Framework."
  exit 1
fi



# ----- Execute The Requested Command-----------------------------------
rm -r reports
mkdir reports
var=`cat csg_test_suites.txt`

total=0

count(){
total=0
	while  read number
 do
    total=`expr $total + $number`
       
 done<reports/$1.txt

}

for i in $var; do
        echo $i
	echo $1

	if test $# = 0;then
	      mvn -e clean install -Dtest.suite=$i
	fi

	if test $1 = '-o';then
	      mvn -e clean install -o -Dtest.suite=$i
	fi
	      
	

	echo "\n\n##################################################
		        $i
###################################################">> reports/surefire_report.txt
	cat target/surefire-reports/org.wso2.carbon.web.test.csg.AllTests.txt >> reports/surefire_report.txt
	
	
	egrep "Tests run:" reports/surefire_report.txt > reports/tmp.txt
	#list classes with Failures
	cut -d " " -f5 reports/tmp.txt | cut -d "," -f1 > reports/temp.txt
	count temp
	if [ ${total} -gt 0 ];then
		echo $i>>reports/tempFailures.txt
	fi
	rm reports/temp.txt

	#list classes with Errors
	cut -d " " -f7 reports/tmp.txt | cut -d "," -f1 > reports/temp.txt
	count temp
	if [ ${total} -gt 0 ];then
		echo $i>>reports/tempErrors.txt
	fi
	rm reports/temp.txt

	#list classes with Skips
	cut -d " " -f9 reports/tmp.txt | cut -d "," -f1 > reports/temp.txt
	count temp
	if [ ${total} -gt 0 ];then
		echo $i>>reports/tempSkips.txt
	fi
	rm reports/temp.txt

done

# make the final report
#-----------------------------------------------------------------------------
egrep "Tests run:" reports/surefire_report.txt > reports/tmp1.txt
cut -d " " -f3 reports/tmp1.txt | cut -d "," -f1 > reports/run.txt
cut -d " " -f5 reports/tmp1.txt | cut -d "," -f1 > reports/Failures.txt
cut -d " " -f7 reports/tmp1.txt | cut -d "," -f1 > reports/Errors.txt
cut -d " " -f9 reports/tmp1.txt | cut -d "," -f1 > reports/Skipped.txt


echo "\n\n##################################################
		       Final Report
##################################################">> reports/surefire_report.txt

count run
run=${total}

count Failures
failures=${total}

count Errors
errors=${total}

count Skipped
skipped=${total}


echo "\nTotal Tests Run: ${run}">> reports/surefire_report.txt
echo "Total Failures: ${failures}">> reports/surefire_report.txt
echo "Total Errors: ${errors}">> reports/surefire_report.txt
echo "Total Skipped: ${skipped}">> reports/surefire_report.txt

echo "\n---------------------------------------------------">> reports/surefire_report.txt
		      
if [ $errors -gt 0 ];then
	echo "\t\t\tTEST ERROR!">> reports/surefire_report.txt
else if [ $failures -gt 0 ];then
	echo "\t\t\tTEST FAILURE!">> reports/surefire_report.txt
     else if [ $skipped -gt 0 ];then
	   echo "\t\t\tTESTS SKIPPED!">> reports/surefire_report.txt
          else
	        echo "\t\t\tTEST SUCESSFULL!">> reports/surefire_report.txt
	  fi
     fi
fi
echo "---------------------------------------------------">> reports/surefire_report.txt

#list error/failure/skipped suites
if [ $errors -gt 0 ];then
	echo "\n-------------------Suites with errors--------------">> reports/surefire_report.txt
	cat reports/tempErrors.txt >> reports/surefire_report.txt
fi

if [ $failures -gt 0 ];then
	echo "\n-------------------Suites with failures------------">> reports/surefire_report.txt
	cat reports/tempFailures.txt >> reports/surefire_report.txt
fi

if [ $skipped -gt 0 ];then
	echo "\n-------------------Suites with skips--------------">> reports/surefire_report.txt
	cat reports/tempSkips.txt >> reports/surefire_report.txt
fi


mv -t target/surefire-reports reports/surefire_report.txt
rm -r reports
