#!/bin/sh
# ----------------------------------------------------------------------------
#
# To kill the whole process tree
#
# ----------------------------------------------------------------------------
for i in `ps -ef| awk '$3 == '$1' { print $2 }'`
do
echo killing $i
kill -9 $i
done
echo killing $1
kill -9 $1