#HTTP POST
curl --data "customerid=1&customername=charitha&customerage=33&customeraddress=piliyandala" -X POST -H "Content-Type: application/x-www-form-urlencoded" http://localhost:9763/CustomerService/rest/qa/customer

#HTTP GET
curl -X GET http://localhost:9763/CustomerService/rest/qa/customer/1

#HTTP PUT
curl --data "customername=charitha&customeraddress=colombo" -X PUT -H "Content-Type: application/x-www-form-urlencoded" http://localhost:9763/CustomerService/rest/qa/customer

#HTTP DELETE
curl -X DELETE http://localhost:9763/CustomerService/rest/qa/customer/1