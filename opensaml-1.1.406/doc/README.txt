VERSION 1.1b
August 26, 2005

Welcome
Contents
Requirements
Bugs
Support
Documentation


Welcome to Internet2's OpenSAML
-------------------------------

OpenSAML is an implementation of SAML 1.0 and 1.1
(http://www.oasis-open.org/committees/security/).
Java and C++ APIs are provided.

Please review the terms described in the LICENSE file before
using this code. It is now the Apache 2.0 license.

Finally, be aware that RSA Security Inc. has asserted a patent claim
against all implementations of SAML. Their terms for licensing
can be found at http://www.rsasecurity.com/solutions/standards/saml/

As a SAML toolkit, OpenSAML may be subject to this claim and developers
may obtain a royalty-free license from RSA directly. Internet2, and
OpenSAML's developers are not responsible for anyone's failure to
do so, and take no position on the validity of this claim.


Contents:
---------

OpenSAML can be obtained directly from anonymous CVS as described in
http://www.opensaml.org/cvs.html

Separate source distributions for Java and C++ also exist and can be found
at http://wayf.internet2.edu/shibboleth/

The CVS layout is as follows:

opensaml/
    java/
        ant                 (Ant shell script)
        build.xml           (Ant build file)
        src/
        	conf/			(Default properties file)
            org/opensaml/   (OpenSAML source)
            schemas/        (XML schemas used)
        lib/                (required jar files)
        endorsed/			(Xerces 2.7.1 jar files that override broken JDK classes)
        dist/               (OpenSAML binary)
        data/               (stuff for unit testing)
        doc/               	(Note JavaDocs must be built, they're not in cvs)
        tests/              (JUnit tests)

The Java distribution is a tarball of the tree and related build scripts.
It includes JavaDocs and the compiled jar file.

Requirements:
-------------

Building the Java code is supported on Java compilers compatible with
JDK 1.3 and above. This version of OpenSAML has been rewritten to use
only standard APIs exported by JAXP 1.3, which comes with JDK 1.5.

There is a known issue with JDK 1.4 because it includes obsolete XML code
inside the JDK itself. The workaround for the issue is to copy the JAXP
jarfiles from the endorsed folder into your JDK's "endorsed" class library
location, typically "$JAVA_HOME/jre/lib/endorsed".

$ cp <opensaml root>/java/endorsed/*.jar $JAVA_HOME/jre/lib/endorsed

Note that many servlet containers such as Tomcat override this location
and set a different one, and the jar files must also be copied there.

Futhermore, the JAXP 1.3 code that ships in JDK 1.5 has a serious memory
leak and should not be used. You can endorse the Apache libraries supplied
with OpenSAML to correct this problem.

Bugs:
-----

A bugzilla is now available at http://bugzilla.internet2.edu/


Support:
--------

A mailing list for users is available. Subscription instructions are
provided at http://www.opensaml.org/index.html#mailing


Documentation:
--------------

Javadocs can be built using ant. There are no explicit samples yet,
but there are test programs and higher level code in the Shibboleth
codebase that would help a novice see what some of the classes do.
An understanding of SAML also helps.
