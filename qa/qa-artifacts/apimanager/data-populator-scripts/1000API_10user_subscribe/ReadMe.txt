This Jmeter test runs with Jmeter 2.7. And this test runs with a fresh pack with a fresh database. And make sure you havnt published or subscribed any api before in this pack.  (Problem is the issue when subscribing the api with an application, we cannot presume the app id, and it is auto incremented)

This script will create 1000 APIs from 10 users and those 10 users will suscribe for 100 APIs each.

Steps :

1. Open the APIM_add_publish_subscribe.jmx with Jmeter.
2. Start the API Manager.
3. Use the users.csv file to bulk import users. Password should be given as "subscriber1". Assign the newly created users to admin role.
4. Go to Jmeter and change the server ip and port in "Server parameter variables"
5. Run the test.
