/*
 * Copyright 2007 WSO2, Inc. http://www.wso2.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 
   Created 2007-10 Jonathan Marsh; jonathan@wso2.com
   
 */
 
this.documentation = <div>Scrapes the <a href="http://photography.nationalgeographic.com/photography/photo-of-the-day/">National Geographic picture-of-the-day</a> into a feed.</div>;

system.include("storexml.stub.js");
var cachePath = "nationalgeographic/cache/";

scrape_picture_page.documentation = <div>Retrieve picture links and other metadata from a pic-of-the-day page url.  No url returns today's page. Local caching of this metadata ensures rapid access after the first attempt.  Exposed primarily for test purposes.</div>;
scrape_picture_page.operationName = "test_scrape_picture_page";
scrape_picture_page.inputTypes = {"url" : "xs:string?"};
scrape_picture_page.outputType = "xml";
function scrape_picture_page(url) {
	if (url == null)
		url = "http://photography.nationalgeographic.com/photography/photo-of-the-day";
	var config =
        <config>
            <var-def name='response'>
	            <xslt>
	            	<xml>
		                <html-to-xml>
		                    <http method='get' url={url} />
		                </html-to-xml>
		            </xml>
		            <stylesheet>
                        <![CDATA[
                            <xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
                                <xsl:output method="xml" omit-xml-declaration="yes"/>
                                <xsl:template match="/">
                                	<photo>
                                	    <xsl:for-each select="//*[@id='content-center-well']">
                                			<date><xsl:value-of select="div[@class='date']"/></date>
                                			<previous>http://photography.nationalgeographic.com<xsl:value-of select="div[@class='slide-navigation'][1]/p/a/@href"/></previous><xsl:for-each select="div[@class='image-viewer clearfix']">
                                			    <xsl:for-each select="table/tbody/tr[1]/td/a">
                                    			    <page>http://photography.nationalgeographic.com/photography/photo-of-the-day/<xsl:value-of select="substring-before(substring-after(@href,'enlarge/'),'_pod_image.html')"/>.html</page>
                                        			<xsl:variable name="href" select="concat('http://photography.nationalgeographic.com', substring-before(img/@src, '-ga.jpg'))"/>
                                        			<location type='small'><xsl:value-of select="$href"/>-ga.jpg</location>
                                        			<location type='medium'><xsl:value-of select="$href"/>-sw.jpg</location>
                                        			<location type='large'><xsl:value-of select="$href"/>-lw.jpg</location>
                                        			<location type='wide'><xsl:value-of select="$href"/>-xl.jpg</location>
                                                </xsl:for-each>
                                    			
                                    			<xsl:for-each select="div[@class='summary']">
                                    				<title><xsl:value-of select="h3"/></title>
                                    				<credit><xsl:value-of select="p[@class='credit']"/></credit>
                                    				<description>
                                    					<xsl:copy-of select="div[@class='description']/node()"/>
                                    				</description>
                                    			</xsl:for-each>
                                    		</xsl:for-each>
                                		</xsl:for-each>
                                	</photo>
                                </xsl:template>
                            </xsl:stylesheet>
                        ]]>
	            	</stylesheet>
	            </xslt>
            </var-def>
        </config>;

    var scraper = new Scraper(config);

    var result = new XML(scraper.response);
    
    if (result.hasComplexContent()) {
	    var date = xsDate(new Date(result.date));
	    storexml.store(cachePath + date, result);
	}

    return result;
}

picture_for_date.documentation = <div>Retrieve a picture for a given date (yyyy-mm-dd format).  If the date isn't already cached, it will be, along with all dates between the requested date and today's date.</div>;
picture_for_date.operationName = "test_picture_for_date";
picture_for_date.inputTypes = {"date" : "xs:string"};
picture_for_date.outputType = "xml";
function picture_for_date(date) {
    try {
		return storexml.retrieve(cachePath + date);
	} catch (e) {
		print("failed to find cached photo for date " + date);
		var photo;
		var startDate = parseDate(date);
		var today = new Date();
		// work forwards in the cache until we find something (or hit today)
		while (startDate <= today) {
			try {
				photo = storexml.retrieve(cachePath + xsDate(startDate));
				break;
			} catch (e) {
				startDate.setUTCDate(startDate.getUTCDate() + 1);
			}
		}
		// start with the most current thing in the cache (if any) an work backwards to the
		//	requested date, filling in the cache as we go...
		var targetDate = parseDate(date);
		while (startDate > targetDate) {
			print(startDate);
			var previousPageUrl;
			if (photo == null) previousPageUrl = null;
			else previousPageUrl = photo.previous;
			
			print("fetching photo for " + startDate);
			photo = scrape_picture_page(previousPageUrl);
			if (!photo.hasComplexContent())
			    break;
			startDate.setUTCDate(startDate.getUTCDate() - 1);
		}
		
		return photo;
	}
}

picture_of_the_day.documentation = <div>Retrieve and RSS 2.0 feed of the requested number (defaults to 30) photos, of the requested size.  Accessible through the rest binding from the url <a href="nationalgeographic/picture_of_the_day?size=wide">http://[mashup server]/services/[username]/nationalgeographic/picture_of_the_day?size=[size]</a>.</div>;
picture_of_the_day.inputTypes = {"size" : "small | medium | large | wide", "numPhotos" : "number?"};
picture_of_the_day.outputType = "#raw";
function picture_of_the_day(size, numPhotos) {
	if (numPhotos == null) numPhotos = 30;

	var feed = 
	    <rss version="2.0">
            <channel>
                <title>National Geographic Picture-of-the-day (from WSO2 Mashup Server)</title>
                <link>http://mashups.wso2.org/services/nationalgeographic/picture_of_the_day?size={size}</link>
                <description>WSO2 Mashup Server mashup acquiring and caching links to the National Geographic
                Picture of the Day (http://photography.nationalgeographic.com/photography/picture-of-the-day),
                and exposing them as a feed.  Sizes of "small", "medium", "large", and "wide" are available.
                A max number of photos can be specified with the "numPhotos" parameter.</description>
            </channel>
        </rss>;

	var startDate = new Date();
	var photo, photoDate, url, urlsmall, entry;
	for (var i = 0; i < numPhotos; i++) {
		photo = picture_for_date(xsDate(startDate));
		if (photo != undefined) {
		if (photo.hasComplexContent()) {
    		url = photo.location.(@type == size).toString();
    		urlsmall = photo.location.(@type == 'small').toString();
    		photoDate = new Date(photo.date.toString());
    		if (i == 0) {
    		    feed.channel.appendChild(<pubDate>{rssDate(photoDate)}</pubDate>);
    		}
    		entry = <item xmlns:media="http://search.yahoo.com/mrss/">
                        {photo.title}
                        <description>
                            &lt;a href='{url}'>&lt;img src='{urlsmall}'/>&lt;/a>
                            {photo.description.*.toXMLString()}
                        </description>
                        <pubDate>{rssDate(photoDate)}</pubDate>
                        <link>{photo.page.toString()}</link>
                        <guid isPermaLink='false'>{photo.page.toString()}</guid>
                        <media:content url={url} type="image/jpeg" />
                        <media:thumbnail url={urlsmall} />
                        <atom:published xmlns:atom="http://www.w3.org/2005/Atom">{xsDate(photoDate)}T00:00:00Z</atom:published>
                    </item>;
    		feed.channel.appendChild(entry);
        }
}
   		startDate.setUTCDate(startDate.getUTCDate() - 1);
	}

	return feed;
}

// Ensure serialized dates conform to http://www.rssboard.org/rss-profile#data-types-datetime
rssDate.visible = false;
function rssDate(d)
{
    return (new Array('Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'))[d.getUTCDay()] + ", " +
      	(d.getUTCDate() < 10 ? "0": "" ) + d.getUTCDate() + " " +
      	(new Array('Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'))[d.getUTCMonth()] + " " +
      	d.getUTCFullYear() + " 00:00:00 GMT";
}

xsDate.visible = false;
function xsDate(d)
{
    return d.getUTCFullYear() + "-" +
      	(d.getUTCMonth() < 9 ? "0": "" ) + (d.getUTCMonth() + 1) + "-" +
      	(d.getUTCDate() < 10 ? "0": "" ) + d.getUTCDate();
}

parseDate.visible = false;
function parseDate(xsdate)
{
    return new Date(xsdate.replace(/-/g, '/'));
}
