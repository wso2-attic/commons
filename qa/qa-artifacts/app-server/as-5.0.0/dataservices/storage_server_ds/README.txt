Steps to invoke the dataservice
===============================
1. Deploy the Department_DataService.dbs after changing the database URL & username/password.
2. Invoke the dataservice once deployed and see whether you get the correct data.
3. You can set different previledges through the Storage server when creating the database and then depending on that, verify whether the user is blocked to do those actions. E.g.:- Give only 'SELECT' previledges and try out a 'INSERT' operation and see if it's blocked
