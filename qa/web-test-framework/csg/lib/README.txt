WSO2 WSAS 3.0
-------------
Welcome to the WSO2 WSAS 3.0 release

WSO2 WSAS is an enterprise ready Web services engine powered by Apache Axis2 and
which offers a complete middleware solution. It is a lightweight, high
performing platform for Service Oriented Architecture, enabling business logic
and applications. Bringing together a number of Apache Web services projects,
WSO2 WSAS provides a secure, transactional and reliable runtime for deploying
and managing Web services.

This is based on the revolutionary WSO2 Carbon framework. All the major features
have been developed as pluggable Carbon components.

Key Features
------------

* Data services support - Expose you enterprise data as a services in a jiffy
* WSAS IDE - Eclipse IDE integration
* Clustering support for High Availability & High Scalability
* Full support for WS-Security, WS-Trust, WS-Policy and WS-Secure Conversation
  and XKMS
* EJB service provider support - Expose your EJBs as services
* Axis1 backward compatibility - Deploy Axis1 services on WSAS & Engage advanced
  WS-* protocols in front of legacy services
* JMX & Web interface based monitoring and management
* WS-* & REST support
* GUI, command line & IDE based tools for Web service development

New Features In This Release
----------------------------

1. Based on the OSGi based WSO2 Carbon architecture. This is a unification of
   all Java based products from WSO2. Now you can have features from the
   lighweight superfast WSO2 ESB & the super-cool WSO2 MashupServer, running on
   your WSAS instance.
2. Enhanced admin UI
3. Extensible server admin framework
4. WS-Eventing support
5. Policy editor
6. Separable frontend & backend - a single frontend server can be used to
   administer several backend servers

Installation & Running
----------------------
1. extract the downloaded zip file
2. Run the wso2server.sh or wso2server.bat file in the bin directory
3. Once the server starts, point your Web browser to
   https://localhost:9443/carbon/

For more details, see the Installation Guide

System Requirements
-------------------

1. Minimum memory - 256MB
2. Processor      - Pentium 800MHz or equivalent at minimum
3. The Management Console requires full Javascript enablement of the Web browser
   NOTE:
     On Windows Server 2003, it is not allowed to go below the medium security
     level in Internet Explorer 6.x.

For more details see
http://wso2.org/wiki/display/carbon/System+Requirements

Known Issues
------------

1. Data Services Definition wizard is incomplete.

All known issues have been recorded at https://wso2.org/jira/browse/CARBON
& https://wso2.org/jira/browse/WSAS

WSAS Binary Distribution Directory Structure
--------------------------------------------

	CARBON_HOME
		|- bin <folder>
		|- conf <folder>
                |- database <folder>
		|- lib <folder>
		|- logs <folder>
		|- repository <folder>
		|- resources <folder>
		|- samples <folder>
		|- tmp <folder>
		|- webapps <folder>
		|-- LICENSE.txt <file>
		|-- README.txt <file>
		|-- INSTALL.txt <file>
		|-- release-notes.html <file>

    	- bin
	  Contains various scripts .sh & .bat scripts

	- conf
	  Contains configuration files

        - database
          Contains the database

	- lib
	  Contains the basic set of libraries required to startup WSAS
	  in standalone mode

	- logs
	  Contains all log files created during execution

	- repository
	  The repository where services and modules deployed in WSO2 WSAS
	  are stored. In addition to this other custom deployers such as
	  dataservices, axis1services and pojoservices are also stored.

	- resources
	  Contains additional resources that may be required

	- samples
	  Contains some sample applications that demonstrate the functionality
	  and capabilities of WSO2 WSAS

	- tmp
	  Used for storing temporary files, and is pointed to by the
	  java.io.tmpdir System property

	- webapps
	  Contains the WSO2 WSAS webapp. Any other webapp also can be deployed
	  in this directory

	- LICENSE.txt
	  Apache License 2.0 under which WSO2 WSAS is distributed.

	- README.txt
	  This document.

	- INSTALL.txt
          This document will contain information on installing WSO2 WSAS

	- release-notes.html
	  Release information for WSO2 WSAS 3.0


Training
--------

WSO2 Inc. offers a variety of professional Training Programs, including
training on general Web services as well as WSO2 WSAS, Apache Axis2,
Data Services and a number of other products.

For additional support information please refer to
http://wso2.com/training/course-catalog/


Support
-------

WSO2 Inc. offers a variety of development and production support
programs, ranging from Web-based support up through normal business
hours, to premium 24x7 phone support.

For additional support information please refer to http://wso2.com/support/

For more information on WSO2 WSAS, visit the WSO2 Oxygen Tank (http://wso2.org)

Crypto Notice
-------------

This distribution includes cryptographic software.  The country in
which you currently reside may have restrictions on the import,
possession, use, and/or re-export to another country, of
encryption software.  Before using any encryption software, please
check your country's laws, regulations and policies concerning the
import, possession, or use, and re-export of encryption software, to
see if this is permitted.  See <http://www.wassenaar.org/> for more
information.

The U.S. Government Department of Commerce, Bureau of Industry and
Security (BIS), has classified this software as Export Commodity
Control Number (ECCN) 5D002.C.1, which includes information security
software using or performing cryptographic functions with asymmetric
algorithms.  The form and manner of this Apache Software Foundation
distribution makes it eligible for export under the License Exception
ENC Technology Software Unrestricted (TSU) exception (see the BIS
Export Administration Regulations, Section 740.13) for both object
code and source code.

The following provides more details on the included cryptographic
software:

Apacge Rampart   : http://ws.apache.org/rampart/
Apache WSS4J     : http://ws.apache.org/wss4j/
Apache Santuario : http://santuario.apache.org/
Bouncycastle     : http://www.bouncycastle.org/


For further details, see the WSO2 Carbon documentation at
http://wso2.org/wiki/display/carbon/1.0

---------------------------------------------------------------------------
(c) Copyright 2009 WSO2 Inc.
