This sample demonstrates the RESTful web service invocation with JAXRS annotated services.
Note that, this sample is built for the usage of WSO2 QA Team. This should be improved a lot to deliver this as a
part of WSO2 AppServer samples :)

Pre-Requisites:
We will use Apache Wink as the JAXRS implementation since WSO2 AS does not support JAXRS out of the box.
Download Apache Wink binary from http://incubator.apache.org/wink/downloads.html and extract into your local file system.

We also use MySQL as the database.

Please follow the steps given below to run the sample.

1. Create the sample database
Execute the dbscripts/CUSTOMER.sql to create the sample CUSTOMER_DB and the table

2. Open build/build.xml and specify the path of Apache Wink home directory as shown below.
 <property name="WINK_HOME" value="/home/charitha/products/apache-wink/apache-wink-1.1.3-incubating"/>

3. Go to build directory and type ant. This will build the sample and CustomerService.war file will be created at the root directory

4. We are going to deploy this webapp in WSO2 Application Server. Before that, make sure to copy MySql JDBC driver to CARBON_HOME/lib and CARBON_HOME/respository/components/lib directories in order to establish the communication between webapp and database

5. Restart WSO2 AppServer

6. Deploy the CustomerService.war file through webapp management console


Test the RESTful service
==========================

You can test the service by running clients/curl-client.sh. Lets have a look at each of the HTTP method invocations separately.

HTTP POST:
curl --data "customerid=1&customername=charitha&customerage=33&customeraddress=piliyandala" -X POST -H "Content-Type: application/x-www-form-urlencoded" http://localhost:9763/CustomerService/rest/qa/customer

HTTP GET:
curl -X GET http://localhost:9763/CustomerService/rest/qa/customer/1

HTTP PUT:
curl --data "customername=charitha&customeraddress=colombo" -X PUT -H "Content-Type: application/x-www-form-urlencoded" http://localhost:9763/CustomerService/rest/qa/customer

HTTP DELETE:
curl -X DELETE http://localhost:9763/CustomerService/rest/qa/customer/1

