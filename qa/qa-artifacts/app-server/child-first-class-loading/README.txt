================================================================================================================
How to test child first class loading with the given test_1.0.0.war
================================================================================================================

1. There are two jar files which has a simple class. The class has a method which returns an integer value. 
For test1.jar it returns "5" and for test2.jar, it returns "7". The webapp has an import to this class.
2. Place the test1.jar in webapp's WEB-INF/lib directory and test2.jar in as-home/repo/components/lib directory. 
3. Then start the server and deploy the webapp. 
4. Access localhost:9763/test_1.0.0/carbon/. This will print value "5". 
5. Undeploy the webapp and remove the test1.jar from WEB-IN/lib and the redeploy it.
6. Again access the :9763/test_1.0.0/carbon/. This will print value "7". 

According to the observation, first the classes under webapp lib directory gets loaded, if not found only, it's 
getting loaded from its parent space, which is OSGi environment. So we can conclude that child first 
class loading is working for webapps in AS.

================================================================================================================
