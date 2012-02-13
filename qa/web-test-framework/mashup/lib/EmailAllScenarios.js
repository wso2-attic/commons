/* Purpose	:	This is a sample service to verify the handling of "operationName" annotation of WSO2 Mashup Server.
 * Author	: 	Yumani Ranaweera
 */

 
function sendEmail_HostUserPass(param){

var message = new Email("smtp.gmail.com", "mashupserver@gmail.com", "wso2wsas"); // Default port is used unless specified in the server.xml

var file = new File("temp.txt");
message.from = param;
message.to = "wso2mashupserver1@gmail.com"; // alternatively message.to can be a array of strings. Same goes for cc bcc - password for this account -> "password123"
message.cc = "wso2mashupserver2@gmail.com"; //(password123)
message.bcc = "wso2mashupserver1@hotmail.com"; //(password123)
message.subject = "Mashup server - sendEmail_HostUserPass";
message.addAttachement(file); // this method has a variable number of arguments. each argument can be a File hostObject or a string representing a file.
message.text = "This is a mail sent from sendEmail_HostUserPass() function. The 2.0.1 release is planned for end of Aug/09";
message.send(); 
}


function sendEmail_HostPortUserPass(param){

var message = new Email("smtp.gmail.com", "25", "mashupserver@gmail.com", "wso2wsas");


message.from = param;
message.to = "wso2mashupserver1@gmail.com"; // alternatively message.to can be a array of strings. Same goes for cc bcc
message.cc = "wso2mashupserver2@gmail.com"; //(password123)
message.bcc = "wso2mashupserver1@hotmail.com"; //(password123)
message.subject = "Mashup server - sendEmail_HostPortUserPass";
message.addAttachement("temp.txt"); // this method has a variable number of arguments. each argument can be a File hostObject or a string representing a file.
message.text = "This is a mail sent from sendEmail_HostPortUserPass() function. The 2.0.1 release is planned for end of Aug/09";
message.send(); 

}

//************************************************************************************************
//Sending a .jpg as an attachement
//************************************************************************************************

function sendEmailwithJPG(param){

var message = new Email("smtp.gmail.com", "qamashupserver@gmail.com", "password123"); 

message.from = param;
message.to = "wso2mashupserver1@gmail.com"; // alternatively message.to can be a array of strings. Same goes for cc bcc
message.cc = "wso2mashupserver2@gmail.com"; //(password123)
message.bcc = "wso2mashupserver1@hotmail.com"; //(password123);
message.subject = "Mashup server - sendEmailwithJPG";
message.addAttachement("text.jpg"); // this method has a variable number of arguments. each argument can be a File hostObject or a string representing a file.
message.text = "This is a mail sent from sendMail() function. The 2.0.1 release is planned for end of Aug/09";
message.send(); 
}


//************************************************************************************************
//Sending two attachements
//************************************************************************************************

function sendEmailwithManyFiles(param){

var message = new Email("smtp.gmail.com", "qamashupserver@gmail.com", "password123"); 

message.from = param;
message.to = "wso2mashupserver1@gmail.com"; // alternatively message.to can be a array of strings. Same goes for cc bcc
message.cc = "wso2mashupserver2@gmail.com"; //(password123)
message.bcc = "wso2mashupserver1@hotmail.com"; //(password123)
message.subject = "Mashup server - sendEmailwithManyFiles";
message.addAttachement("text.jpg","temp.txt"); // this method has a variable number of arguments. Each argument can be a File hostObject or a string representing a file.
message.text = "This is a mail sent from sendMail() function. The 2.0.1 release is planned for end of Aug/09";
message.send(); 
}


//*************************************************************************************************
//Send to Array of TO
//*************************************************************************************************
function sendEmail_ArrayOfTo(param){
// Create a new Email hostObject
var message = new Email("smtp.gmail.com", "25", "qamashupserver@gmail.com", "password123");
var file = new File("temp.txt");
message.from = param;

//Having an array of addresses as "To"
var to = new Array();
to[0] = "wso2mashupserver1@gmail.com";
to[1] = "wso2mashupserver2@gmail.com";
message.to = to;

message.cc = "wso2mashupserver1@hotmail.com"; //(password123)
message.bcc = "wso2mashupserver2@hotmail.com"; //(password123)
message.subject = "Mashup server - sendEmail_ArrayOfTo";
message.addAttachement(file); // this method has a variable number of arguments. each argument can be a File hostObject or a string representing a file.
message.text = "This is a mail sent from sendEmail_ArrayOfTo() function. The 2.0.1 release is planned for end of Aug/09";
message.send(); 
}

//*************************************************************************************************
//Send to Array of CC
//*************************************************************************************************
function sendEmail_ArrayOfCC(param){
// Create a new Email hostObject
var message = new Email("smtp.gmail.com", "25", "qamashupserver@gmail.com", "password123");
var file = new File("temp.txt");
message.from = param;

//Having an array of addresses as "To"
var cc = new Array();
cc[0] = "wso2mashupserver1@gmail.com";
cc[1] = "wso2mashupserver2@gmail.com";
message.cc = cc;

message.bcc = "wso2mashupserver1@hotmail.com"; //(password123)
message.subject = "Mashup server - sendEmail_ArrayOfCC";
message.addAttachement(file); // this method has a variable number of arguments. each argument can be a File hostObject or a string representing a file.
message.text = "This is a mail sent from sendEmail_ArrayOfTo() function. The 2.0.1 release is planned for end of Aug/09";
message.send(); 
}


//*************************************************************************************************
//Send to Array of BCC
//*************************************************************************************************
function sendEmail_ArrayOfBCC(param){
// Create a new Email hostObject
var message = new Email("smtp.gmail.com", "25", "qamashupserver@gmail.com", "password123");
var file = new File("temp.txt");
message.from = param;

//Having an array of addresses as "To"
var bcc = new Array();
bcc[0] = "wso2mashupserver1@gmail.com";
bcc[1] = "wso2mashupserver2@gmail.com";
message.bcc = bcc;

message.to = "wso2mashupserver1@hotmail.com";
message.cc = "wso2mashupserver2@hotmail.com";
message.subject = "Mashup server - sendEmail_ArrayOfBCC";
message.addAttachement(file); // this method has a variable number of arguments. each argument can be a File hostObject or a string representing a file.
message.text = "This is a mail sent from sendEmail_ArrayOfTo() function. The 2.0.1 release is planned for end of Aug/09";
message.send(); 
}
