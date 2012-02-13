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
message.from = "yumani@gmail.com";
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
   
    