Documentation
-------------

Documentation folder can be found under the 'docs' folder in the distribution. This contains most of the architecture documents and API documents.

Deploying
---------

Mercury is an Axis2 module. Therefore it can be deployed as in the same way as any other Axis2 module. To deploy the Mercury the following steps can be followed.
1.put the mercury-mar-SNAPSHOT.mar to modules folder
2.put the mercury-core-SNAPSHOT.jar to lib folder.
3.Engage Mercury to service.
1.At server side - <module ref="Mercury"/>
2.At client side – serviceClient.engageModule("Mercury");

Support
-------


Any problem can be reported at the mailing lists given in the following url.
http://wso2.org/projects/commons/mercury


Thank you for using Mercury!

