######################################
    WSO2 Server Management Client
######################################
1. Start WSO2 ESB
2. Change following properties in /conf/client.properties file
	carbon_home = ESB server directory location (ex : carbon_home=/chamara/project/esb/wso2esb-3.0.1)
	server_ip = IP address of esb server (ex : server_ip=localhost)
	https_port = HTTPS port of ESB server (ex: https_port=9443)
	admin_username = Administrator user name (ex: admin_username=admin)
	admin_password = Administrator password ( ex : admin_password=admin)
3. Run runclient.sh
