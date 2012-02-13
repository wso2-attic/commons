checkin/out automated script.
=============================

prerequisites
---------------

1. Install following perl modules (You can refer to README files of each module for installation details)

Log-Log4perl-1.31
XML-DOM-1.44
XML-Parser-2.40
XML-RegExp-0.03
XML-SemanticDiff-0.95

How to run the checkin-checkout automated script
---------------------------------------------------

1. Unzip wso2carbon-checkin-checkout-1.0.zip.

2. Copy each file in wso2carbon-checkin-checkout-1.0 into your CARBON_HOME/bin directory.

3. Then edit the following parameters in greg_test.xml file

<carbon_home>/home/krishantha/product/greg/RC2/wso2greg-3.5.1</carbon_home>
<test_home>/home/krishantha/product/greg/RC2/wso2greg-3.5.1/bin/resources</test_home> # put the resources directory under your CARBON_HOME/bin
<working_home>/home/krishantha/product/greg/RC2/wso2greg-3.5.1/bin</working_home>

4. Start G-REG server

5. Then run ant command inside CARBON_HOME/bin

6. Execute the perl script using command 
perl g-reg-checkinout.pl

6. You will see the final results in the console, detail results log is available in tmp-results.txt file under the same location.

7. If you are running the G-Reg server otherthan default ports and host, then edit the g-reg-checkinout.pl and set your port and host name as below
my $hostname="localhost";
my $httpsport=9443;
my $httpport=9763;

