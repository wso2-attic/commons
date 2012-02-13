#!/bin/bash

rm -f /tmp/libmod_svc_simulator_256b.so
rm -f /tmp/libmod_svc_simulator_1k.so
rm -f /tmp/libmod_svc_simulator_2k.so
rm -f /tmp/libmod_svc_simulator_10k.so
rm -f /tmp/libmod_svc_simulator_20k.so
rm -f /tmp/libmod_svc_simulator_100k.so
rm -f /tmp/libmod_svc_simulator_1mb.so
gcc -g -I/usr/local/apache2/include -shared mod_simulator_256b.c -o libmod_svc_simulator_256b.a \
-fPIC -DPIC -shared -lpthread -ldl -Wl,-soname -Wl,libmod_svc_simulator_256b.so  \
-o libmod_svc_simulator_256b.so -DLINUX=2 -D_REENTRANT -D_XOPEN_SOURCE=500 -D_BSD_SOURCE -D_SVID_SOURCE -D_GNU_SOURCE

gcc -g -I/usr/local/apache2/include -shared mod_simulator_1k.c -o libmod_svc_simulator_1k.a \
-fPIC -DPIC -shared -lpthread -ldl -Wl,-soname -Wl,libmod_svc_simulator_1k.so  \
-o libmod_svc_simulator_1k.so -DLINUX=2 -D_REENTRANT -D_XOPEN_SOURCE=500 -D_BSD_SOURCE -D_SVID_SOURCE -D_GNU_SOURCE

gcc -g -I/usr/local/apache2/include -shared mod_simulator_2k.c -o libmod_svc_simulator_2k.a \
-fPIC -DPIC -shared -lpthread -ldl -Wl,-soname -Wl,libmod_svc_simulator_2k.so  \
-o libmod_svc_simulator_2k.so -DLINUX=2 -D_REENTRANT -D_XOPEN_SOURCE=500 -D_BSD_SOURCE -D_SVID_SOURCE -D_GNU_SOURCE

gcc -g -I/usr/local/apache2/include -shared mod_simulator_10k.c -o libmod_svc_simulator_10k.a \
-fPIC -DPIC -shared -lpthread -ldl -Wl,-soname -Wl,libmod_svc_simulator_10k.so  \
-o libmod_svc_simulator_10k.so -DLINUX=2 -D_REENTRANT -D_XOPEN_SOURCE=500 -D_BSD_SOURCE -D_SVID_SOURCE -D_GNU_SOURCE

gcc -g -I/usr/local/apache2/include -shared mod_simulator_20k.c -o libmod_svc_simulator_20k.a \
-fPIC -DPIC -shared -lpthread -ldl -Wl,-soname -Wl,libmod_svc_simulator_20k.so  \
-o libmod_svc_simulator_20k.so -DLINUX=2 -D_REENTRANT -D_XOPEN_SOURCE=500 -D_BSD_SOURCE -D_SVID_SOURCE -D_GNU_SOURCE

gcc -g -I/usr/local/apache2/include -shared mod_simulator_100k.c -o libmod_svc_simulator_100k.a \
-fPIC -DPIC -shared -lpthread -ldl -Wl,-soname -Wl,libmod_svc_simulator_100k.so  \
-o libmod_svc_simulator_100k.so -DLINUX=2 -D_REENTRANT -D_XOPEN_SOURCE=500 -D_BSD_SOURCE -D_SVID_SOURCE -D_GNU_SOURCE

gcc -g -I/usr/local/apache2/include -shared mod_simulator_1mb.c -o libmod_svc_simulator_1mb.a \
-fPIC -DPIC -shared -lpthread -ldl -Wl,-soname -Wl,libmod_svc_simulator_1mb.so  \
-o libmod_svc_simulator_1mb.so -DLINUX=2 -D_REENTRANT -D_XOPEN_SOURCE=500 -D_BSD_SOURCE -D_SVID_SOURCE -D_GNU_SOURCE

cp -f libmod_svc_simulator_256b.so /tmp
cp -f libmod_svc_simulator_1k.so /tmp
cp -f libmod_svc_simulator_2k.so /tmp
cp -f libmod_svc_simulator_10k.so /tmp
cp -f libmod_svc_simulator_20k.so /tmp
cp -f libmod_svc_simulator_100k.so /tmp
cp -f libmod_svc_simulator_1mb.so /tmp
