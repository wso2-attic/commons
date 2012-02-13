function test(){
var reader = new FeedReader();
var feed = reader.get("http://www.formula1.com/rss/news/headlines.rss");
var link = feed.getEntries()[0].link[0];
return <test>{feed.getEntries()[0].link[0]}</test>;
}