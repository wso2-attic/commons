
Test Environment Preparation
----------------------------

1. Download selenium-core-0.8.3 from http://www.openqa.org/selenium-core/
2. Create a folder called "MashupTests" inside 'selenium-core-0.8.3' home directory.
3. Copy and paste all the files available here into this new folder
2. Copy the selenium-core installtion in the webroot of your webserver.


To run the test suite
---------------------
1. Start the Mashup server.
2. Access selenium-core-0.8.3\TestRunner.html from the applications https port.
	https://localhost:7443/selenium-core-0.8.3/core/TestRunner.html
3. In TestRunner, there's a text box prompting for test suite path, modify the default value as follows "../MashupTests/ShortTestSuite.html"
3. Press Go button to start the scripts execution.