#!/bin/bash

cd /home/as5test/artifacts/axis2_schema
i=1
while test $i != 11
do
sed 's/##name/calculatorImportSchema_'$i'/g' service.xml > ./service/META-INF/services.xml
sed 's/###sname/calculatorImportSchema_'$i'/g' calculator_import_schema.wsdl > ./service/META-INF/calculator_import_schema_${i}.wsdl
cd ./service
jar -cvf calculatorImportSchema_${i}.aar *
mv calculatorImportSchema_${i}.aar ../bulk
rm ./META-INF/calculator_import_schema_${i}.wsdl
i=`expr $i + 1`
cd ../
done
 
