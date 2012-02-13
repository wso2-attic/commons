/*
* Copyright 2005-2007 WSO2, Inc. http://www.wso2.org
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
*/
this.serviceName = "BlogAggregator";
this.documentation = "TODO: Add service level documentation here" ;

toString.documentation = "TODO: Add operation level documentation here" ;
toString.inputTypes = { /* TODO: Add input types of this operation */ };
toString.outputType = "String"; /* TODO: Add output type here */ 
function toString()
{
   //TODO: Add function code here
   return "Hi, my name is BlogAggregator";
}


function aggregateBlogs(){
   // Step 1 - Call our Data Service and get the list of blog RSS Feeds
   
   // The address of BlogMetaData Data Service
   var endPoint = "http://localhost:7762/services/admin/BlogMetaData";
   // The Service Operation to call
   var operation = "urn:getBlogs";  

   // Creating a new WSRequest Host Object, which can call Web Services
   var request = new WSRequest();
   var options = new Array();
   options.useSOAP = 1.2;
   options.useWSA = 1.0;
   options.action = operation;
   request.open(options, endPoint, false);
   request.send(null);
   
   // Getting the response received from the Data Service.
   // This response will contain a list of Blog RSS Feeds
   var result = request.responseXML;

   
   // Step 2 - Iterate through the Feed list and get recent posts
   var namespace = new Namespace("");

   var aggregatedPosts = <div></div>;

   for each (var record in result.namespace::Blog){
      var currentPostBlock = <div></div>;

      var blogAuthor = record.namespace::Author.toString();
      currentPostBlock.appendChild(<h2>{blogAuthor}</h2>);

      // fetch the RSS Feed
      var feedURL = record.namespace::Feed.toString();         
      
      try{
         var reader = new FeedReader();
         var feed = reader.get(feedURL);     
      
         // Get the latest post form the Feed and add it to the block
         var post = feed.getEntries()[0];      
         currentPostBlock.appendChild(<h3>{post.title}</h3>);
         currentPostBlock.appendChild(post.content);

         // Add this post block to the aggregated content
         aggregatedPosts.appendChild(currentPostBlock);
       }catch(e){
           system.log(e);
         }
   }
   
   return aggregatedPosts.toString();
}
