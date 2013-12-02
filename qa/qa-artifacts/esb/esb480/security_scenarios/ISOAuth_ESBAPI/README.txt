Steps

1)Deploy the playground2.0 on IS

2)Create an API with the SimpleStockQuoteAPI given with the example

3)Build the Simple StockQuote from location <ESB-HOME>/samples/axis2Server/src/SimpleStockQuoteService

4)Start the axis2Sever from path <ESB-HOME>/samples/axis2Server

5) Run the below curl and replace the client-id and client-secret and username and password as generated
curl -u <Client_id>:<Client_secret> -k -d "grant_type=<strong>password</strong>&amp;username=admin&amp;password=admin" -H "Content-Type:application/x-www-form-urlencoded" https://localhost:9443/oauth2endpoints/token

6)Replace the generated access token
curl -H "Authorization:Bearer <access-token>" -v  http://10.100.0.94:8282/stockquote/view/IBM
