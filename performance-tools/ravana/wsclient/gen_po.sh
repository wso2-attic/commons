#!/bin/bash 
charspool=(
'A' 'B' 'C' 'D' 'E' 'F' 'G' 'H' 'I' 'J' 'K' 'L' 'M' 'N' 'O' 
'P' 'Q' 'R' 'S' 'T' 'U' 'V' 'W' 'X' 'Y' 'Z');

numpool=(
'0' '1' '2' '3' '4' '5' '6' '7' 
'8' '9' '0'); 


function namegen()
{
    len=${#charspool[*]} 
    if [ $# -lt 1 ]; then 
          num=6; 
    else 
          num=$1; 
    fi 
    for c in $(seq $num); do 
          name=$name${charspool[$((RANDOM % len))]} 
    done 
}

function numgen()
{
    len=${#numpool[*]} 

    if [ $# -lt 1 ]; then 
          num=6; 
    else 
          num=$1; 
    fi 
    for c in $(seq $num); do 
          retnum=$retnum${numpool[$((RANDOM % len))]}
    done 
}

function currencygen()
{
    len=${#numpool[*]} 

    if [ $# -lt 1 ]; then 
          num=6; 
    else 
          num=$1; 
    fi 
    for c in $(seq $num); do 
          price=$price${numpool[$((RANDOM % len))]}
    done 
          price=$price'.';
          num=2;
    for c in $(seq $num); do 
          price=$price${numpool[$((RANDOM % len))]} 
    done 
}

message="<soapenv:Envelope xmlns:soapenv=\"http://www.w3.org/2003/05/soap-envelope\"><soapenv:Header xmlns:wsa=\"http://www.w3.org/2005/08/addressing\"></soapenv:Header><soapenv:Body><m:buyStocks xmlns:m=\"http://po.services.core.carbon.wso2.org\"><m:order><m:symbol>IBM</m:symbol><m:buyerID>asankha</m:buyerID><m:price>140.34</m:price><m:volume>200000</m:volume></m:order>"

size=${#message}
while [ $size -le $1 ]
do
message=$message"<m:order><m:symbol>"
name=""
namegen 6;
message=$message$name"</m:symbol><m:buyerID>"

name=""
namegen 6;
message=$message$name
message=$message"</m:buyerID><m:price>"

price=""
currencygen 6;
message=$message$price"</m:price><m:volume>"

retnum="";
numgen 6;
message=$message$retnum"</m:volume></m:order>"

size=${#message}
done

message=$message"</m:buyStocks></soapenv:Body></soapenv:Envelope>"
echo $message






