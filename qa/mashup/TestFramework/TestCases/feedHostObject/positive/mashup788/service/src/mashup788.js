function test() {
    var reader = new FeedReader();
    var feed = reader.get("http://auburnmarshes.spaces.live.com/feed.rss");
    return <test>{feed.getEntries()[0].link}</test>;
}