atomFeedTest.inputTypes = {};
atomFeedTest.outputType = "string";
function atomFeedTest(){
    //Creating a feed
    var atomFeed = new AtomFeed();

    //Creating an Entry
    var atomEntry = new AtomEntry();
    atomEntry.id= "id:Testing";
    atomEntry.author= "Tester";
    atomEntry.content = "This is a test string to be stored as content in the entry.";

    //Adding the entry to the feed
    atomFeed.addEntry(atomEntry);

    //Writing the feed to a file
    var result = atomFeed.writeTo("new.xml");
	return result
} 