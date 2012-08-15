#!/bin/bash

cd /home/as5test/artifacts/axis2_schema_many_xsds
#make sure to keep i between 1001: < i <2000
i=1001
while test $i != 1150
do
sed 's/##name/calculatorImportSchema_'$i'/g' service.xml > ./service/META-INF/services.xml
sed 's/calculator.xsd/calculator'$i'.xsd/g' calculator_import_schema.wsdl > ./temp.wsdl
sed 's/###sname/calculatorImportSchema_'$i'/g' temp.wsdl > ./service/META-INF/calculator_import_schema_${i}.wsdl
rm -rf temp.wsdl
cd ./service
jar -cvf calculatorImportSchema_${i}.aar *
mv calculatorImportSchema_${i}.aar ../bulk
rm ./META-INF/calculator_import_schema_${i}.wsdl
i=`expr $i + 1`
cd ../
done
 
