1. comment_search_oneuser.jmx
   --------------------------

Scenario : 
Search for the comment, 'sample' entered by user3.

pre-requisites : 
1. Set the test environment as in TestEnvironment.txt (https://wso2.org/repos/wso2/trunk/commons/qa/mashup/Test Framework/PerformanceTest/TestEnv1/TestEnvironment.txt)
2. Start the Mashup Server
3. Login as user3 (user3/password), and add a comment to 'Version' service as 'sample'
4. Signout.



2. comment_search_ALLusers.jmx
   --------------------------

Scenario : 
Search for the comments entered by all users.

pre-requisite : 
1. Set the test environment as in TestEnvironment.txt (https://wso2.org/repos/wso2/trunk/commons/qa/mashup/Test Framework/PerformanceTest/TestEnv1/TestEnvironment.txt)



3. mashup_search_oneUser.jmx
   -------------------------

Scenario : 
Search for the mashups by user5.

pre-requisite : 
1. Set the test environment as in TestEnvironment.txt (https://wso2.org/repos/wso2/trunk/commons/qa/mashup/Test Framework/PerformanceTest/TestEnv1/TestEnvironment.txt)



4. mashup_search_allUsers.jmx
   --------------------------
Scenario : 
Search for the mashups by all users.

pre-requisite : 
1. Set the test environment as in TestEnvironment.txt (https://wso2.org/repos/wso2/trunk/commons/qa/mashup/Test Framework/PerformanceTest/TestEnv1/TestEnvironment.txt)




5. top_rated_mashup_byUser.jmx
   ---------------------------
Scenario : 
Search for the top rated mashups of user11.

pre-requisite : 
1. Set the test environment as in TestEnvironment.txt (https://wso2.org/repos/wso2/trunk/commons/qa/mashup/Test Framework/PerformanceTest/TestEnv1/TestEnvironment.txt)
2. Start the Mashup Server
3. Login as user11 (user11/password), and rate 'Version', yahoogeoCode services with 5 stars.
4. Signout.



6. top_rated_mashup_byAnyone.jmx
   -----------------------------
Scenario : 
Search for the top rated mashups of any user.

pre-requisite : 
1. Set the test environment as in TestEnvironment.txt (https://wso2.org/repos/wso2/trunk/commons/qa/mashup/Test Framework/PerformanceTest/TestEnv1/TestEnvironment.txt)
2. Start the Mashup Server
3. Login as user11 (user11/password), and rate 'Version', yahooGeoCode services with 5 stars.
4. Login as user9 (user9/password), and rate 'TomatoTube', yahooGeoCode services with 5 stars.
4. Signout.



7. signedIn_search_for_myMashups.jmx
   ---------------------------------
Scenario : 
Sign-in as user12 and Search for my mashups.

pre-requisite : 
1. Set the test environment as in TestEnvironment.txt (https://wso2.org/repos/wso2/trunk/commons/qa/mashup/Test Framework/PerformanceTest/TestEnv1/TestEnvironment.txt)


8. signedIn_search_for_mashups_byUser1
   -----------------------------------
Scenario : 
Sign-in as user15 and Search for mashups by user1.

pre-requisite : 
1. Set the test environment as in TestEnvironment.txt (https://wso2.org/repos/wso2/trunk/commons/qa/mashup/Test Framework/PerformanceTest/TestEnv1/TestEnvironment.txt)