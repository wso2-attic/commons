Preconditions
1.mysql must be installed
2.Data source should be configured

Note: The password for the email is available within the inputadapter

steps
=====
1.Go to path wso2cep-3.0.0/repository/deployment/server

2.copy the files into the relevant folder 
eventbuilders/
eventformatters/
executionplans/
inputeventadaptors/
outputeventadapto


3.Send an email with the followign details

To:wso2tester@gmail.com

Subject:SOAPAction:urn:ServiceManagement

EMail Body

{StockQuoteEvent:" +
                    "{StockSymbol:LNKD," +
                    "LastTradeAmount:240.36," +
                    "StockChange:0.05," +
                    "OpenAmount:245.05," +
                    "DayHigh:260.46," +
                    "DayLow:230.01," +
                    "StockVolume:20452658," +
                    "PrevCls:240.31," +
                    "ChangePercent:0.20," +
                    "FiftyTwoWeekRange:\"220.73 - 271.58\"," +
                    "EarnPerShare:2.326," +
                    "PE:10.88," +
                    "CompanyName:\"LinkedIn Corp\"," +
                    "QuoteError:false" +
                    "}" +
                    "}"
