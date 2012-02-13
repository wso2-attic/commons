#!/bin/sh
# ----------------------------------------------------------------------------
#
#  Stoping ESB
#
# ----------------------------------------------------------------------------
echo "Stoping ESB..."
cat ./../../results/test-cases/test-case-$1/esb.pid | xargs -i ./kill-process-tree.sh {}
echo "ESB Killed!"

