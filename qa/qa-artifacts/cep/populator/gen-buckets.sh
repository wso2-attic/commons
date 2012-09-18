#!/bin/bash

for c in `seq 1 100`;
do
 cp xml-stockquote-anlyzer.xml xml-stockquote-anlyzer_$c.xml ;
 sed -i  "s/XMLStockQuoteAnalyzer/XMLStockQuoteAnalyzer_$c/g" xml-stockquote-anlyzer_$c.xml ;
 
done

