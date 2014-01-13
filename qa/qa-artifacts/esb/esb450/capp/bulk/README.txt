Creating multiple car files
===========================

* You can create multiple car files by executing the gen.sh file.
Steps
-----
1. Open the gen.sh file and specify the number of car files you need to generate by editing the following line.

while test $i != {number of car files to be generated}

E.g.:- If you need to generate 100 car files, edit the above line as shown below.

while test $i != 101

2. Execute the gen.sh file
3. 100 car files will be created under the bulk folder

NOTE: These car files contains an endpoint. According to your requirement, you need to change the gen.sh file
