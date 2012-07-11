How to test
===========
1. Add the following entries to the mime.mappings file (Make sure you seperate the colums from tabs)

text/properties                                 myproperties
text/config                                     mycfg
text/ruby                                       myrb
xml/drool                                       mydrl
xml/xquery                                      myxq
text/x-java                                     myjava
text/plain                                      mytxt
text/html                                       html
application/x-javascript                                        myjs
application/xml                                 myxml
application/xml                                 myxslt
application/x-xsd+xml                                   myxsd
xml/evan                                        myeva

2. Now add the following entries to the mime.types file (Seperate the colums from tabs)

text/properties                                 properties
text/config                                     cfg
text/ruby                                       rb
xml/drool                                       drl
xml/xquery                                      xq
xml/evan                                        eva

3. Now start the server (This should be the first server startup)

Scenario 1
==========

a) Upload resources with extensions which you have added up to mime.types file & verify whether the correct media type is auto populated

b) Upload the given car file (MyApp-1.0.0.car). Once deployed, ensure that the files upload contain the media types which you have set above through the mime.mappings file
