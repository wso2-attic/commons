/* Purpose	:	This is a sample service to verify the handling of "operationName" annotation of WSO2 Mashup Server.
 * Author	: 	Yumani Ranaweera
 */
 
 feedTest1.inputTypes = {};
 feedTest1.outputType = "string";
 function feedTest1(){
 var reader = new FeedReader();
 var feed = new Feed();
 try {
  feed = reader.get("http://wso2.org/atom/feed");
 }
 catch (e) {
 throw ("invalid feed");
 }
 var result =feed.writeTo("test.xml");

 var entries = feed.getEntries();
 return entries;
 }