REM echo ab -H "SOAPAction: %1" -T "application/soap+xml; charset=UTF-8" -p data/%1-%2.xml -n %3 -c %4 http://10.100.1.44:8080/%6
ab -H "SOAPAction: %1" -T "application/soap+xml; charset=UTF-8" -p data/%1-%2.xml -n %3 -c %4 http://10.100.1.44:8080/%6 > results\%5-%1-%2.txt
graboutput results\%5-%1-%2.txt %1 %2 %5 
