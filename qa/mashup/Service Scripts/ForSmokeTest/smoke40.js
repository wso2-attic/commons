feedTest1.inputTypes = {};
 feedTest1.outputType = "string";
 function feedTest1(){
  var reader = new FeedReader();
  var feed = new Feed();
  feed = reader.get("http://www.formula1.com/rss/news/headlines.rss");
  var result =feed.writeTo("test.xml");

  var entries = feed.getEntries();
  return entries;
 }

    