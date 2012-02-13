/* Purpose	:	This is a sample service to verify the handling of "feed" annotation of WSO2 Mashup Server.
 * Author	: 	Yumani Ranaweera
 */

 feedTest1.inputTypes = {};
 feedTest1.outputType = "string";

 function feedTest1(){
  var reader = new FeedReader();
  var feed = new Feed();
  feed = reader.get("http://www.formula1.com/rss/news/headlines.rss");
  var result = feed.writeTo("test.xml");

  var entries = feed.getEntries();
  return entries;
 }