/Requests per second/ {REQ_PER = $4}
/\[ms\] \(mean\)/ {TIME_PER = $4}
/kb\/s total/ {KBS = $1}
END { printf ("%s, %s, %s, %s, %s, %s\n", SOAP_ENGINE, METHOD, ITEM_COUNT, REQ_PER, TIME_PER, KBS) }