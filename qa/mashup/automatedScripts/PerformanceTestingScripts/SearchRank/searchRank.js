searchRank.inputTypes={"google" : "boolean", "yahoo" : "boolean", "live" : "boolean", "searchString" : "string", "searchFor" : "string", "maxNumberOfResults" : "10 | 20 | 30 | 40 | 50 | 60 | 70 | 80 | 90 | 100", "searchAnywhere" : "boolean"};
searchRank.outputType="object";
function searchRank(google, yahoo, live, searchString, searchFor, maxNumberOfResults, searchAnywhere){
var ranks = new searchRanks();
if (google){
var googleRank = 0;
var i;
for (i = 0; (i < maxNumberOfResults && googleRank == 0); i += 10) {
	var googleResults = googleSearch(searchString, 10, i);
	googleRank = getRank(googleResults, searchFor, searchAnywhere); 
}
ranks.googleRank = i - 10 + googleRank;
}	

if (yahoo){
var yahooRank = 0;
var i;
for (i = 1; (i < maxNumberOfResults && yahooRank == 0); i += 10) {
	var yahooResults = yahooSearch(searchString, 10, i);
	yahooRank = getRank(yahooResults, searchFor, searchAnywhere); 
}
ranks.yahooRank = i - 11 + yahooRank;
}

if (live){
var liveRank = 0;
var i;
for (i = 1; (i < maxNumberOfResults && liveRank == 0); i += 10) {
	var liveResults = liveSearch(searchString, 10, i);
	liveRank = getRank(liveResults, searchFor, searchAnywhere); 
}
ranks.liveRank = i - 11 + liveRank;
}
return ranks;
}

searchRanks.visible = false;
function searchRanks(){};

getRank.visible = false;
function getRank(results, searchFor, searchAnywhere) {
var count = 1;
for each (var result in results.result) {
    if (result.link.toString().trim().match(searchFor)) {
	return count;
    }
    if (searchAnywhere && ((result.description.toString().match(searchFor)) || (result.title.toString().match(searchFor))))  {
	return count;
    }
    count ++;
}
return 0;
}

googleSearch.visible = false;
googleSearch.inputTypes = {"searchString" : "string", "numberOfResults" : "10 | 20 | 30 | 40 | 50 | 60 | 70 | 80 | 90 | 100", "start" : "xs:int"};
googleSearch.outputType = "XML";
function googleSearch(searchString, numberOfResults, start) {
var path = 'http://www.google.lk/search?hl=en&q=' + searchString + '&btnG=Search&meta=&num=' + numberOfResults + '&start=' + start + '&sa=N';
    var config =    <config>
                        <var-def name='content'>
                            <html-to-xml>
                            		<http url={path}/>
                            </html-to-xml>
                        </var-def>
                        <var-def name='result'>
			    <![CDATA[ <results> ]]>
                            <loop item="individualResult" index="i">
                                    <list>
                                        <xpath expression="/html/body/div[@id='res']//div[@class='g']">
                                            <var name="content"/>
                                        </xpath>
                                    </list>
                                    <body>
                                        <xquery>
                                        <xq-param name="doc">
                                            <var name="individualResult"/>
                                        </xq-param>
                                        <xq-expression><![CDATA[
                                            declare variable $doc as node()* external;
                                            let $link := data($doc//a[@class='l']/@href)
                                                return
                                                    <result>
                                                        <title>{for $x in $doc//a[@class='l']/node()
                                                                    return normalize-space(data($x))}</title>
                                                        <link>{$link}</link>
                                                        <description>{for $x in $doc//tbody/tr//div[@class='std']/node()
                                                                    where (fn:name($x) != "span" and fn:name($x) != "nobr")
                                                                    return normalize-space(data($x))}</description>
                                                    </result>
                                        ]]></xq-expression>
                                        </xquery>
                                    </body>
                                </loop>
				<![CDATA[ </results> ]]>
                          </var-def>
                    </config>;

    var scraper = new Scraper(config);
    return new XML(scraper.result);
}

liveSearch.visible = false;
liveSearch.inputTypes = {"searchString" : "string", "numberOfResults" : "xs:int", "start" : "xs:int"};
liveSearch.outputType = "XML";
function liveSearch(searchString, numberOfResults, start) {
var path = 'http://search.live.com/results.aspx?q=' + searchString + '&first=' + start;
 var config =    <config>
                        <var-def name='result'>
			    <![CDATA[ <results> ]]>
                            <loop item="individualResult" index="i">
                                    <list>
                                        <xpath expression="//div[@id='results_container']/div[@id='results']//ul[@class='sb_results']/li">
                                            	<html-to-xml>
                            				<http url={path}/>
                            			</html-to-xml>
                                        </xpath>
                                    </list>
                                    <body>
                                        <xquery>
                                        <xq-param name="doc">
                                            <var name="individualResult"/>
                                        </xq-param>
                                        <xq-expression><![CDATA[
                                            declare variable $doc as node()* external;
                                            let $link := data($doc/li/h3/a/@href)
                                                return
                                                    <result>
                                                        <title>{for $x in $doc/li/h3/a/node()
                                                                    return normalize-space(data($x))}</title>
                                                        <link>{$link}</link>
                                                        <description>{for $x in $doc/li/p/node()
                                                                    where (fn:name($x) != "span" and fn:name($x) != "nobr")
                                                                    return normalize-space(data($x))}</description>
                                                    </result>
                                        ]]></xq-expression>
                                        </xquery>
                                    </body>
                                </loop>
				<![CDATA[ </results> ]]>
                          </var-def>
                    </config>;

    var scraper = new Scraper(config);
    return new XML(scraper.result);
}

yahooSearch.visible = false;
yahooSearch.inputTypes = {"searchString" : "string", "numberOfResults" : "10 | 20 | 30 | 40 | 50 | 60 | 70 | 80 | 90 | 100", "start" : "xs:int"};
yahooSearch.outputType = "XML";
function yahooSearch(searchString, numberOfResults, start) {
var yahooURL = 'http://search.yahoo.com';
var submitURL = '${submitURL}?p=' + searchString + '&ei=UTF-8&iscqry=&fr=sfp&n=' + numberOfResults + '&b=' + start;
 var config = <config>
                    <var-def name='submitURL'>
			<xpath expression="//form[@id='sf']/@action">
                            <html-to-xml>
                            	<http url={yahooURL}/>
                            </html-to-xml>
			</xpath>
                    </var-def>
		    <var-def name='content'>
                            <html-to-xml>
				<http url={submitURL}>
                                </http>
                            </html-to-xml>
                    </var-def>
		    <var-def name='result'>
			    <![CDATA[ <results> ]]>
                            <loop item="individualResult" index="i">
                                    <list>
                                        <xpath expression="//div[@id='results']//div[@id='web']/ol/li">
                                            <var name="content"/>
                                        </xpath>
                                    </list>
                                    <body>
                                        <xquery>
                                        <xq-param name="doc">
                                            <var name="individualResult"/>
                                        </xq-param>
                                        <xq-expression><![CDATA[
                                            declare variable $doc as node()* external;
					    let $link := data($doc//div[@class='res'])
                                                return
                                                    <result>
                                                        <title>{for $x in $doc//div[@class='res']/div//a/node() 
								return normalize-space(data($x))}</title>   
							<link>{for $x in $doc//div[@class='res']/span[@class='url']/node()
                                                                return normalize-space(data($x))}</link>
                                                        <description>{for $x in $doc//div[@class='res']/div[@class='abstr']/node()
                                                                return normalize-space(data($x))}</description>                                                     
                                                    </result>
                                        ]]></xq-expression>
                                        </xquery>
                                    </body>
                                </loop>
				<![CDATA[ </results> ]]>
                          </var-def>
                </config>;
    var scraper = new Scraper(config);
    return new XML(scraper.result);
}

String.prototype.trim = function () {
    return this.replace(/^\s*/, "").replace(/\s*$/, "");
}
