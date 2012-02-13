#!/bin/sh
curl -v http://localhost:8280/am
curl -v -X POST -H "Content-type: application/xml" -d @create.xml http://localhost:8280/am
curl -v http://localhost:8280/am
curl -v http://localhost:8280/am/account/A1
curl -v -X PUT -H "Content-type: application/xml" -d @update.xml http://localhost:8280/am/account/A1
curl -v http://localhost:8280/am/account/A1
curl -v -X DELETE http://localhost:8280/am/account/A1
curl -v http://localhost:8280/am

