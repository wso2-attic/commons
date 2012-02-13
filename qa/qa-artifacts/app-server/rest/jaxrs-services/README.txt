QARestApp.war
==============
This is a jax-rs service which can be used to test RESTful service invocation.

1. Create a DB
create database JAXRS_DB;
crate table USER_T(name varchar(100), age int, address varchar(2000));

2. Deploy QARestApp.war in app server

3. Do the service invocation using curl

POST:
curl --data "charitha" -H "Content-Type: text/plain"  -X POST http://localhost:8099/QARestApp/rest/qa/user

GET:
curl  -X GET http://localhost:8080/QARestApp/rest/qa/user/charitha

PUT:
curl --data "charitha" -H "Content-Type: text/plain"  -X PUT http://localhost:8080/QARestApp/rest/qa/user/update
//Check the age of the user. It will be updated to a fixed value

DELETE:
curl  -X DELETE http://localhost:8080/QARestApp/rest/qa/user/delete/charitha

