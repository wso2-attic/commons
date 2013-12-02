Steps
1.Copy the activemq jars to <ESB_HOME>/repository/components/lib

2.Start activemq

3.Enable the transport receiver for ActiveMQ
<transportReceiver name="jms" class="org.apache.axis2.transport.jms.JMSListener">

4.Enable the transport sender for active mq
<transportSender name="jms" class="org.apache.axis2.transport.jms.JMSSender"/>

5.Deploy the proxy service
<?xml version="1.0" encoding="UTF-8"?>
<proxy xmlns="http://ws.apache.org/ns/synapse"
       name="StockQuoteProxy"
       transports="jms"
       statistics="disable"
       trace="disable"
       startOnLoad="true">
   <target>
      <inSequence>
         <log level="full"/>
         <dbreport>
            <connection>
               <pool>
                  <password>dbuser_password</password>
                  <user>dbuser_name</user>
                  <url>jdbc:mysql://localhost:3306/esb480</url>
                  <driver>com.mysql.jdbc.Driver</driver>
               </pool>
            </connection>
            <statement>
               <sql>call addmessage(?)</sql>
               <parameter xmlns:m="http://services.samples"
                          xmlns:m0="http:// schemas.xmlsoap.org/soap/envelope/"
                          expression="//m0:Body/m:placeOrder/m:order/ m:symbol"
                          type="VARCHAR"/>
            </statement>
         </dbreport>
         <property name="OUT_ONLY" value="true" scope="default" type="STRING"/>
      </inSequence>
      <endpoint>
         <address uri="http://localhost:9000/services/SimpleStockQuoteService"/>
      </endpoint>
   </target>
   <publishWSDL uri="file:repository/samples/resources/proxy/sample_proxy_1.wsdl"/>
   <parameter name="transport.jms.ContentType">
      <rules>
         <jmsProperty>contentType</jmsProperty>
         <default>application/xml</default>
      </rules>
   </parameter>
   <description/>
</proxy>

6.Build the SimpleStockQuoteService with ant command at location <ESB_HOME>/samples/axis2Server/src/SimpleStockQuoteService

7.Start the Axis2 Server at location <ESB_HOME>/samples/axis2Server

8.Create a mysql db and source the following script

CREATE TABLE `testdb`.`messages` (
`message_id` int(10) NOT NULL AUTO_INCREMENT,
`message` text,
PRIMARY KEY (`message_id`)
) ENGINE=MyISAM AUTO_INCREMENT=44 DEFAULT CHARSET=latin1
CREATE DEFINER=`root`@`localhost` PROCEDURE `testdb`.`addmessage`(message_txt
VARCHAR(10))
INSERT INTO messages (message) values (message_txt)


9.From the Axis2 client at location <ESB_HOME>/samples/axis2Client
ant jmsclient -Djms_type=pox -Djms_dest=dynamicQueues/StockQuoteProxy -Djms_payload=dddddd

Refer:http://sanjeewamalalgoda.blogspot.com/2012/08/configure-wso2-esb-with-jms-accept.html
