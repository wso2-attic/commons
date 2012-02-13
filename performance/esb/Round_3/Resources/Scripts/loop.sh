#/bin/sh

count=$1
shift

file=$1
shift
n=$1
shift
c=$1
shift
action=$1
shift
url=$1
shift


for ((  i = 1 ;  i <= $count;  i++  ))
do
  echo "Running iteration $i"

  java -jar JavaBench/benchmark-0.1.jar -p $file -n $n -c $c -k -H "SOAPAction: $action" -T "text/xml; charset=UTF-8" $url
done


