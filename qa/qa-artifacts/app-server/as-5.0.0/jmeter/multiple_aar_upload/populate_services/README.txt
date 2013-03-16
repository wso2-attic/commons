How can I create multiple axis2services?
========================================
To create 100 axis2 services, run gen.sh

In the current gen.sh file, the tenant id is given as t1.
If you need to create a new set of aar services to be deployed on another tenant, all you have to do is run the script replace_tenantid.sh with two parameters, current teanantId and new tenantId as below.

$ bash replace_tenantid.sh t1 t2
