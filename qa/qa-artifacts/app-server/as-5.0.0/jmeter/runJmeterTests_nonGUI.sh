#!/bin/bash

JMETER_HOME='/home/as5test/Jmeter2.7'  #change before test is run
Jmx_FILE='/home/as5test/tests/100Jaxrs.jmx'  #change before test is run - absolute path
ResultJTL_FILE='/home/as5test/tests/results_100Jaxrs.jtl'   #change before test is run: if you repeat the test the result will be appended to the same file. When this file is opened from- 
# -Jmeter summary Report component it will show the results summary, error percentage etc

cd ${JMETER_HOME}/bin
sh jmeter.sh -n -t ${Jmx_FILE} -l ${ResultJTL_FILE}

 
