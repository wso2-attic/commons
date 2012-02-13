raceNewsRss.safe = true;
raceNewsRss.documentation =
    <div>Retrieves information from the formula1.com RSS feed.</div>;
raceNewsRss.outputType = "string";

function raceNewsRss() {
	// Feed URI and keywords for races and testing.
	var f1InfoFeed = "http://www.formula1.com/rss/news/headlines.rss";
	var f1Keywords = new Array("grand", "prix", "test");
	
	// Get the entries from the feed at F1.com.
    var reader = new FeedReader();
    var feed = new Feed();
    feed = reader.get(f1InfoFeed);
    var entries = feed.getEntries();

    var titleString = entries[0].title;
return titleString;
	
}
