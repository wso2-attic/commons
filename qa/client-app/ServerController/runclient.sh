CARBON_HOME=`sed '/^\#/d' ./conf/client.properties | grep "carbon_home"  | tail -n 1 | sed 's/^.*=//'`
export TEST_HOME=$CARBON_HOME/repository/components/plugins
LOG4J_HOME=$CARBON_HOME/lib/log4j.properties

for f in $TEST_HOME/*.jar
do
 TEST_CLASSPATH=$TEST_CLASSPATH:$f
done
TEST_CLASSPATH="$TEST_CLASSPATH"
export TEST_CLASSPATH
cp -r $LOG4J_HOME .
java -classpath .:$TEST_CLASSPATH:ServerController.jar org.wso2.carbon.server.admin.test.ServerManager
rm -r ./log4j.properties
rm -r ./repository
