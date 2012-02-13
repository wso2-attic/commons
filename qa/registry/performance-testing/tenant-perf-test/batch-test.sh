#!/bin/sh
sed 4s/[0-9][0-9]*/10/ conf/test.properties >conf/test-test
mv conf/test-test conf/test.properties
cat conf/test.properties
mvn test -o
sed 4s/[0-9][0-9]*/20/ conf/test.properties >conf/test-test
mv conf/test-test conf/test.properties
cat conf/test.properties
mvn test -o
sed 4s/[0-9][0-9]*/30/ conf/test.properties >conf/test-test
mv conf/test-test conf/test.properties
cat conf/test.properties
mvn test -o
sed 4s/[0-9][0-9]*/40/ conf/test.properties >conf/test-test
mv conf/test-test conf/test.properties
cat conf/test.properties
mvn test -o
sed 4s/[0-9][0-9]*/50/ conf/test.properties >conf/test-test
mv conf/test-test conf/test.properties
cat conf/test.properties
mvn test -o
