#!/bin/bash

i=1

echo "<order id='332'>
    <header>
        <customer number=\"123\">Joe</customer>
    </header>
    <order-items>" >> input658_50.xml

while test $i != 10001
do 

echo "<order-item id='$i'>
            <product quantity=\"4\">Pen</product>
            <price>8.80</price>
        </order-item>" >> input658_50.xml
#sed 's/_con_/'${conc[$i]}'/g' jmtest2.jmx > jmtest_${conc[$i]}.jmx
i=`expr $i + 1`
done
echo "</order-items>
  </order>" >> input658_50.xml

