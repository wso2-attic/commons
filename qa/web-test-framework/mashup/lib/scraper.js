function scraperConfig()
{
var config = 
<config> 
<var-def name='response'>
<html-to-xml>
<http method='get' url='http://ww2.wso2.org/~builder/'/>
</html-to-xml></var-def>
</config>;	

var scraper = new Scraper(config);
var result = scraper.response;
var resultXML = new XML(result.substring(result.indexOf('?>') + 2));
return resultXML;
}


