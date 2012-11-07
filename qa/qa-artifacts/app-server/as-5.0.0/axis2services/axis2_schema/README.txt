This script gen.sh available at the correct location can be used to create multiple axis2 services with import schemas.

All you have to do is, change the number in "while test $i != 11" of the gen.sh depending on the number of services that you need to create and run the script file. E.g.:- If you need to create 100 services out of calculatorImportSchema_1.aar, then set the number to 101, i.e.:- while test $i != 101 and it will generate calculatorImportSchema_1.aar to calculatorImportSchema_100.aar services under the 'bulk' folder.
