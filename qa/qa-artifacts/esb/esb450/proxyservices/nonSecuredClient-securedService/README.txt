1) Upload the given policy (PolicySecurePartnerService02.xml) to governance/config registry

2) Add service.jks through key stores (password : apache)

3) Secure echo service with above policy. Use service.jks to secure it

4) Create a proxy service with following configuration

 <proxy name="newProxy" transports="https http" startOnLoad="true" trace="disable">
        <description/>
        <target>
            <inSequence>
                <send>
                    <endpoint>
                        <address uri="http://localhost:8281/services/echo">
                            <enableAddressing/>
                            <enableSec policy="gov:/PolicySecurePartnerService02.xml"/>
                        </address>
                    </endpoint>
                </send>
                <log level="full"/>
            </inSequence>
            <outSequence>
                <log level="full"/>
                <header xmlns:wsse="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd" name="wsse:Security" action="remove"/>
                <send/>
            </outSequence>
        </target>
        <publishWSDL uri="http://localhost:8281/services/echo?wsdl"/>
    </proxy>


5) Invoke the service with tryit or soapui. Enable debug logs and observe the messages
