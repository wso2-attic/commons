WSO2 WSAS Web Test Framework
=============================

WSO2 WSAS Web Test Framework can be used to verify functional stability of WSO2 WSAS.
The tests were written using Junit test framework coupled with Selenium RC.


************************************************************
Setting Up The Environment
************************************************************

* ../commons/src/test/resources/framework.properties contains all the properties to run the tests (eg: server ip, port, contextroot etc..)
eg:-
host.name=localhost
https.fe.port=9443

* Surefire plugin will create the test report at ./target/surefire-reports.

**************************************************************
How to run the framework
**************************************************************

If you want to run tests one-by-one;
-------------------------------------
mvn clean install -Dtest.suite=<test suite name> -DfirefoxProfileTemplate=<path_of_the_Firefox_profile>

"test suite name" can be found out from wsas_test_suites.txt which is available in the same directory in which this README is located.

Since selenium cannot handle the FF3 SSL certificate error, you should create a separate firefox profile for WSAS. See "How to create a custom FireFox profile" section below.

If you want to run all tests or set of test suites at once;
------------------------------------------------------------

sh runAll.sh <path_of_the_Firefox_profile>
	-for offline mode add '-o'

* You can run multiple tests, and when you want to skip a test add '#' in front of the test name in wsas_test_suites.txt file.


How to create a custom firefox profile
---------------------------------------

When you access carbon management UI first time, FF3 security exception gets popped up. Selenium does not capture these browser specific controls.
Even if you permanently store the certificate exception, it get lost when running selenium tests because each selenium test starts using its own fresh browser instance.

In order to avoid this issue,

1. Close all FF windows
2. Create a new FF3 profile
firefox -profilemanager
3. Access carbon management console using the new FF profile
4. You will get the security error. Accept the exception once and permanently store it
5. Close browser