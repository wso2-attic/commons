export AXIS2_HOME=/chamara/project/esb/wso2esb-3.0.1/repository/components/plugins
for f in $AXIS2_HOME/*.jar
do
 AXIS2_CLASSPATH=$AXIS2_CLASSPATH:$f
done
export AXIS2_CLASSPATH

#echo the classpath $AXIS2_CLASSPATH
javac -d . -classpath .:$AXIS2_CLASSPATH src/org/wso2/carbon/server/admin/test/ServerAdminCommand.java
