#!/bin/bash
cp -f /tmp/libmod_svc_simulator_256b.so ../modules/
cp -f /tmp/libmod_svc_simulator_1k.so ../modules/
cp -f /tmp/libmod_svc_simulator_2k.so ../modules/
cp -f /tmp/libmod_svc_simulator_10k.so ../modules/
cp -f /tmp/libmod_svc_simulator_20k.so ../modules/
cp -f /tmp/libmod_svc_simulator_100k.so ../modules/
cp -f /tmp/libmod_svc_simulator_1mb.so ../modules/
cd ../modules
mv libmod_svc_simulator_256b.so mod_svc_simulator_256b.so
mv libmod_svc_simulator_1k.so mod_svc_simulator_1k.so
mv libmod_svc_simulator_2k.so mod_svc_simulator_2k.so
mv libmod_svc_simulator_10k.so mod_svc_simulator_10k.so
mv libmod_svc_simulator_20k.so mod_svc_simulator_20k.so
mv libmod_svc_simulator_100k.so mod_svc_simulator_100k.so
mv libmod_svc_simulator_1mb.so mod_svc_simulator_1mb.so
cd ../bin
./apachectl stop
sleep 1
./apachectl start
sleep 2
ps -A|grep httpd
