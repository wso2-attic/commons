#!/bin/bash
export AXIS2C_HOME=/axis2c/deploy
gcc -g -I$AXIS2C_HOME/include/axis2-1.7.0/ -I./src -I. -L$AXIS2C_HOME/lib -L. -laxutil -laxis2_axiom -laxis2_engine *.c -opoclient
