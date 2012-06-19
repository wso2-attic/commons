Use the given configuration.
Send 10 messages from javabench/any tool. I used the command - java -jar benchmark.jar -p requests/StockQuoteRequestIBM.xml -n 10 -c 1 -k -H "urn:getQuote" -T "text/xml; charset=UTF-8" "http://evan-ThinkPad:8280/"

Since the aggregate mediator saying <messageCount min="-1" max="5" />, it would first aggregate 5 request messages. Then only it would aggregate the next 5 messages and create a new aggregated response.
