Test case :
Create switch mediator specifying cases
Define onError sequence
Check whether error is returned properly through onError sequence, when an error occurs in switch mediator

- Other relevant artifacts can be found from (e.g wsdl, xslt files, services, etc...)
http://wso2.org/library/articles/2011/01/wso2-esb-by-example-service-chaining

- Send a request using java-bench
java -jar benchmark.jar -p /home/pavithra/software/java-ab/reqest.xml -n 1 -c 1 -k -H "SOAPAction: urn:credit" -T "text/xml; charset=UTF-8" http://localhost:8280/
