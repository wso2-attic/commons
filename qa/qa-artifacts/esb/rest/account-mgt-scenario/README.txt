1. Install an WSO2 AS instance and deploy the car file (AccountManager-1.0.0.car) - This will deploy a simple web service in AS.
2. Change the ESB ports by setting port offset to 1. (You should run AS on default ports) 
3. Copy the attached synapse-configs directory into ESB and replace the existing default configuration.
4. Start the ESB.

You can now make a few REST calls on the ESB and try it out. Some sample curl commands are available in the client.sh script. The REST API has following operations:

GET /am => List all the available accounts
POST /am => Create new account (account details should be in payload as a POX)
GET /am/account/{accountId} => Get a particular account
PUT /am/account/{accountId} => Update a particular account (account details should be in payload as a POX)
DELETE /am/account/{accountId} => Delete a particular account
