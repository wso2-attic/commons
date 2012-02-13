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
#   export CARBON_HOME=/home/dinusha/Softwares/wso2esb-2.1.2
#                   
#
#   export JAVA_HOME=/home/dinusha/Softwares/jdk1.6.0_14
#
#   JAVA_OPTS       (Optional) Java runtime options used when the commands
#                   is executed.
#
# NOTE: Borrowed generously from Apache Tomcat startup scripts.
# -----------------------------------------------------------------------------
before="$(date +%s)"
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
  echo " CARBON cannot execute $JAVACMD"
  exit 1
fi

# if JAVA_HOME is not set we're not happy
if [ -z "$JAVA_HOME" ]; then
  echo "You must set the JAVA_HOME variable before running CARBON."
  exit 1
fi



# ----- Execute The Requested Command-----------------------------------
rm -r reports
mkdir reports

#get the firefox profile from user aegument
ffprofile=""
foundff=0
foundAll=0
for j in $@; do
  if echo "$j" | grep -q "[/]"  # extrat the FF profile from the  command line
     then
     ffprofile=${j}
     foundff=1
  fi
  if echo "$j" | grep -q "all" # check whether that user wants to execute the whole test framework or specified set
     then
     foundAll=1
  fi
done
if [ $foundff -eq 1 ] 
	then
	echo "$ffprofile"
	echo "FireFox Profile Detected"		# dispaly  the FF profile detection 
else
	echo "WARNING: No FireFox Profile Detected"
fi


NUMBER_OF_LINES=`wc -l <esb_test_suites.txt`
x=0
#if the user command 'sh runAll.sh -o specifySet firefoxProfileName' 
#if the user command 'sh runAll.sh specifySet firefoxProfileName' 

   if [ $foundAll -eq 1 ]
   then
       sed '/^\+/d' esb_test_suites.txt > tmp.txt
       sed '/^$/d' tmp.txt > tmp1.txt  
       mv tmp1.txt tmp.txt
       sed '/^Configure/d' tmp.txt > tmp1.txt
       mv tmp1.txt tmp.txt
       sed '/^Services/d' tmp.txt > tmp1.txt
       mv tmp1.txt tmp.txt
       sed '/^Samples/d' tmp.txt > tmp1.txt
       mv tmp1.txt tmp.txt
       sed '/^Local Entries/d' tmp.txt > tmp1.txt
       mv tmp1.txt tmp.txt
       sed '/^End Points/d' tmp.txt > tmp1.txt
       mv tmp1.txt tmp.txt
       sed '/^Sequences/d' tmp.txt > tmp1.txt
       mv tmp1.txt tmp.txt
       sed '/^Monitor/d' tmp.txt > tmp1.txt
       mv tmp1.txt tmp.txt
       sed '/^Mediators/d' tmp.txt > tmp1.txt
       mv tmp1.txt tmp.txt
   
   else
       while read line
       do 
	 i=`expr $i + 1`
         for j in $@; do
	    if [ "${line}" = "$j" ] 
	    then
	    x=`expr $i + 1`
	    break
	    fi
	done
	done < esb_test_suites.txt
   fi


if [ $foundAll -eq 0 ] #if executing specified set
then

   for j in `seq $x $NUMBER_OF_LINES`
   do
     suite=`sed -n "${j} p" esb_test_suites.txt`
     if [ "${suite}" != "" ]
     then
        firstchar=`expr substr "$suite" 1 1`

        if [ "${firstchar}" != "+" ] 
        then
	    if [ "${firstchar}" = "#" ]  #check whether the test is marked for skipping 
	    then
	       echo "WARNING: Skipping ${suite}" 		# diplay the name of the test with the skip tag
	       continue
	    fi
            echo "*********$suite**********"
            	if test $# = 3;
		then
		    mvn -e clean install -o -Dtest.suite=$suite -DfirefoxProfileTemplate=$ffprofile
		fi
		if test $# = 2;
		then 
		    mvn -e clean install -Dtest.suite=$suite -DfirefoxProfileTemplate=$ffprofile
		fi

     	    echo "\n\n##################################################
		        $suite
###################################################">> reports/surefire_report.txt
	    cat target/surefire-reports/org.wso2.carbon.web.test.esb.AllTests.txt >> reports/surefire_report.txt
        fi

       if [ "${firstchar}" = "+" ] 
       then
          break
       fi
    elif [ "${suite}" = "" ]
    then
       j=`expr $j + 1`
    fi
   done
fi



if [ $foundAll -eq 1 ]
then
   var=`cat tmp.txt`

   for i in $var; do
	if echo "$i" | grep -q "#\<"  #check whether the test is marked for skipping 	
	then
	    echo "WARNING: Skipping $i" 		# diplay the name of the test with the skip tag
	    continue
	fi
        
	if test $# = 3;
	then
	    echo "************$i**********"
	    mvn -e clean install -o -Dtest.suite=$i -DfirefoxProfileTemplate=$ffprofile
	fi
	if test $# = 2;
	then 
            echo "************$i**********"
	    mvn -e clean install -Dtest.suite=$i -DfirefoxProfileTemplate=$ffprofile
	fi

	echo "\n\n##################################################
		        $i
###################################################">> reports/surefire_report.txt
	cat target/surefire-reports/org.wso2.carbon.web.test.esb.AllTests.txt >> reports/surefire_report.txt
   done
rm -r tmp.txt
fi


# make the final report
#-----------------------------------------------------------------------------
egrep "Tests run:" reports/surefire_report.txt > reports/tmp1.txt
cut -d " " -f3 reports/tmp1.txt | cut -d "," -f1 > reports/run.txt
cut -d " " -f5 reports/tmp1.txt | cut -d "," -f1 > reports/Failures.txt
cut -d " " -f7 reports/tmp1.txt | cut -d "," -f1 > reports/Errors.txt
cut -d " " -f9 reports/tmp1.txt | cut -d "," -f1 > reports/Skipped.txt
cut -d " " -f12 reports/tmp1.txt | cut -d "," -f1 > reports/Time.txt



total=0

count(){
total=0
while  read number
do
    total=`expr $total + $number`
       
 done<reports/$1.txt
}


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

after="$(date +%s)"
elapsed_seconds="$(expr $after - $before)"


echo "\nTotal Tests Run: ${run}">> reports/surefire_report.txt
echo "Total Failures: ${failures}">> reports/surefire_report.txt
echo "Total Errors: ${errors}">> reports/surefire_report.txt
echo "Total Skipped: ${skipped}">> reports/surefire_report.txt
echo "Total Elapsed Time : ${elapsed_seconds} sec" >> reports/surefire_report.txt

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

mv -t target/surefire-reports reports/surefire_report.txt
rm -r reports
		

