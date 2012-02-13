
// Sending an email from an gmail account.
function sendEmail(){

var message = new Email();

message.from = "mashupserver@gmail.com";
message.to = "yumani@gmail.com"; // alternatively message.to can be a array of strings. Same goes for cc bcc
message.cc = "yumani@wso2.com";
message.bcc = "yumani77@yahoo.com";
message.subject = "Mashup server approaches 1.0";
message.addAttachement("temp.txt"); // this method has a variable number of arguments. each argument can be a File hostObject or a string representing a file.
message.text = "This is a mail sent from sendMail() function. The 1.0 release is planned for end of january";
message.send(); 
}



function sendEmail_HostUserPass(){

var message = new Email("smtp.gmail.com", "mashupserver@gmail.com", "wso2wsas"); // Default port is used unless specified in the server.xml

var file = new File("temp.txt");
message.from = "mashupserver@gmail.com";
message.to = "yumani@gmail.com"; // alternatively message.to can be a array of strings. Same goes for cc bcc
message.cc = "yumani@wso2.com";
message.bcc = "yumani77@yahoo.com";
message.subject = "Mashup server approaches 1.0";
message.addAttachement(file); // this method has a variable number of arguments. each argument can be a File hostObject or a string representing a file.
message.text = "This is a mail sent from sendEmail_HostUserPass() function. The 1.0 release is planned for end of january";
message.send(); 
}


function sendEmail_HostPortUserPass(){

var message = new Email("smtp.gmail.com", "25", "mashupserver@gmail.com", "wso2wsas");


message.from = "mashupserver@gmail.com";
message.to = "yumani@gmail.com"; // alternatively message.to can be a array of strings. Same goes for cc bcc
message.cc = "yumani@wso2.com";
message.bcc = "yumani77@yahoo.com";
message.subject = "Mashup server approaches 1.0";
message.addAttachement("temp.txt"); // this method has a variable number of arguments. each argument can be a File hostObject or a string representing a file.
message.text = "This is a mail sent from sendEmail_HostPortUserPass() function. The 1.0 release is planned for end of january";
message.send(); 

}

//************************************************************************************************
//Sending a .jpg as an attachement
//************************************************************************************************

function sendEmailwithJPG(){

var message = new Email();

message.from = "mashupserver@gmail.com";
message.to = "yumani@gmail.com"; // alternatively message.to can be a array of strings. Same goes for cc bcc
message.cc = "yumani@wso2.com";
message.bcc = "yumani77@yahoo.com";
message.subject = "Mashup server approaches 1.0";
message.addAttachement("text.jpg"); // this method has a variable number of arguments. each argument can be a File hostObject or a string representing a file.
message.text = "This is a mail sent from sendMail() function. The 1.0 release is planned for end of january";
message.send(); 
}


//************************************************************************************************
//Sending two attachements
//************************************************************************************************

function sendEmailwithManyFiles(){

var message = new Email();

message.from = "mashupserver@gmail.com";
message.to = "yumani@gmail.com"; // alternatively message.to can be a array of strings. Same goes for cc bcc
message.cc = "yumani@wso2.com";
message.bcc = "yumani77@yahoo.com";
message.subject = "Mashup server approaches 1.0";
message.addAttachement("text.jpg","temp.txt"); // this method has a variable number of arguments. each argument can be a File hostObject or a string representing a file.
message.text = "This is a mail sent from sendMail() function. The 1.0 release is planned for end of january";
message.send(); 
}


//*************************************************************************************************
//Send to Array of TO
//*************************************************************************************************
function sendEmail_ArrayOfTo(){
// Create a new Email hostObject
var message = new Email("smtp.gmail.com", "25", "mashupserver@gmail.com", "wso2wsas");
var file = new File("temp.txt");
message.from = "mashupserver@gmail.com";

//Having an array of addresses as "To"
var to = new Array();
to[0] = "yumani77@yahoo.com";
to[1] = "yumani@wso2.com";
to[2] = "yumaniranaweera@hotmail.com";
message.to = to;

//message.to = "yumaniranaweera@hotmail.com";
message.cc = "yumani@wso2.com";
message.bcc = "yumani@gmail.com";
message.subject = "Mashup server approaches 1.0";
message.addAttachement(file); // this method has a variable number of arguments. each argument can be a File hostObject or a string representing a file.
message.text = "This is a mail sent from sendEmail_ArrayOfTo() function. The 1.0 release is planned for end of January";
message.send(); 
}

//*************************************************************************************************
//Send to Array of CC
//*************************************************************************************************
function sendEmail_ArrayOfCC(){
// Create a new Email hostObject
var message = new Email("smtp.gmail.com", "25", "mashupserver@gmail.com", "wso2wsas");
var file = new File("temp.txt");
message.from = "mashupserver@gmail.com";

//Having an array of addresses as "To"
var cc = new Array();
cc[0] = "yumani77@yahoo.com";
cc[1] = "yumani@wso2.com";
cc[2] = "yumaniranaweera@hotmail.com";
message.cc = cc;

//message.to = "yumaniranaweera@hotmail.com";
message.to = "yumani@wso2.com";
message.bcc = "yumani@gmail.com";
message.subject = "Mashup server approaches 1.0";
message.addAttachement(file); // this method has a variable number of arguments. each argument can be a File hostObject or a string representing a file.
message.text = "This is a mail sent from sendEmail_ArrayOfTo() function. The 1.0 release is planned for end of January";
message.send(); 
}


//*************************************************************************************************
//Send to Array of BCC
//*************************************************************************************************
function sendEmail_ArrayOfBCC(){
// Create a new Email hostObject
var message = new Email("smtp.gmail.com", "25", "mashupserver@gmail.com", "wso2wsas");
var file = new File("temp.txt");
message.from = "mashupserver@gmail.com";

//Having an array of addresses as "To"
var bcc = new Array();
bcc[0] = "yumani77@yahoo.com";
bcc[1] = "yumani@wso2.com";
bcc[2] = "yumaniranaweera@hotmail.com";
message.bcc = bcc;

//message.to = "yumaniranaweera@hotmail.com";
message.to = "yumani@wso2.com";
message.cc = "yumani@gmail.com";
message.subject = "Mashup server approaches 1.0";
message.addAttachement(file); // this method has a variable number of arguments. each argument can be a File hostObject or a string representing a file.
message.text = "This is a mail sent from sendEmail_ArrayOfTo() function. The 1.0 release is planned for end of January";
message.send(); 
}
