This script securityservicesGen.sh available at the correct location can be used to create multiple axis2 services that are secured.

All you have to do is, change the number in "while test $i != 101" of the gen.sh depending on the number of services that you need to create and run the script file. 

E.g.:- If you need to create 100 services out of RoomManagementService.aar, then set the number to 101, i.e.:- while test $i != 101 and it will generate RoomManagementService_1.aar to RoomManagementService_100.aar services under the 'appLoad' folder.
