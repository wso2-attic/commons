Scenario A
===========
Configuration File Name : synapse_localentry_prop_expressions.xml
Test Case 		: This configuration simulates retrieving local entry contents (Source URLs - files of local file  system + external URLs, Inline XMLs, Inline text)


Scenario B
==========
Added the following to the log4j.properties file

#Generating separate file for each proxy service
log4j.category.SERVICE_LOGGER.logMediatorProxy=INFO, LOGMEDIATORPROXY_APPENDER
log4j.additivity.SERVICE_LOGGER.logMediatorProxy=false
log4j.appender.LOGMEDIATORPROXY_APPENDER=org.apache.log4j.RollingFileAppender
log4j.appender.LOGMEDIATORPROXY_APPENDER.File=${carbon.home}/repository/logs/proxy/wso2-esb-service-logMediatorProxy.log
log4j.appender.LOGMEDIATORPROXY_APPENDER.MaxFileSize=1000KB
log4j.appender.LOGMEDIATORPROXY_APPENDER.MaxBackupIndex=10
log4j.appender.LOGMEDIATORPROXY_APPENDER.layout=org.apache.log4j.PatternLayout
log4j.appender.LOGMEDIATORPROXY_APPENDER.layout.ConversionPattern=%d{ISO8601} [%X{ip}-%X{host}] [%t] %5p %c{1} %m%n

Then restarted the server and use the synapse configuration in synapse_logFilegenerate.xml

Invoke the Proxy service and you should see logs related to the relevant proxy service under the location defined in log4j.appender.LOGMEDIATORPROXY_APPENDER.File
