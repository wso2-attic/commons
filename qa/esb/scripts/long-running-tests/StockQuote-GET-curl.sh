NUM_REQS=100; for J in `seq -w 1 $NUM_REQS`; do echo -n "RequestNum=$J " ; curl -s "http://10.100.3.101:7000/" | grep Ack ; sleep 1 ; done
