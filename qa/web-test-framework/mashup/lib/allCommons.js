/*
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
this.serviceName = "admin/allCommons";

// Two-way direct service invocation
echoString.inputTypes = "String";
echoString.outputType = "String"; 
function echoString(param1)
{
   return (param1);
}

// Oneway invocation
Ping.inputTypes = "String"
function Ping(param2)
{
	print ("*******Received the ping request**********")
}


//Service invocation with caching
function getTime()
{
 var d = new Date();
 var t = d.getTime();
 print (t); 
}


// Ordered delivery (for RM testing)
echoInt.inputTypes ="number";
function echoInt(param3)
{
	print (param3);
}


