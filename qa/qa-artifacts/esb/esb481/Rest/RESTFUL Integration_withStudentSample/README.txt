1. Deploy "StudentService.dbs" file on App Server with correct database details
2. Use ESB configuration "restfulIntegrationConfig.xml" and point to dataservice endpoint 

Requests:
1. To add a new student
    curl --request POST -v --data @addPayload.xml -H "Content-type: application/xml"  http://localhost:8280/students/003
2. To get the added student details
    curl --request GET -v  http://localhost:8280/students/003
3. To update the student
    curl --request PUT -v --data @updatePayload.xml -H "Content-type: application/xml"  http://localhost:8280/students/003
4. To delete the student
    curl --request DELETE -v  http://localhost:8280/students/003
