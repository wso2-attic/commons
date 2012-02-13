
/* *******************************************************************************************
 * Name	: IMAllScenarios.js	
 * Description : Sample service to test WSO2 Mashup Server support for IM host Object
 * Author	: Yumani Ranaweera
 ********************************************************************************************/

// Happy Scenarios
gabberSingleMsg.inputTypes="none";
gabberSingleMsg.outputType="string";
function gabberSingleMsg(){
var im = new IM("gabber");
im.login("mashupserver@gmail.com","wso2wsas");
im.sendMessage("yumani@gmail.com","test hi");
im.disconnect();
return "successful";
} 

yahoo.inputTypes="none";
yahoo.outputType="string";
function yahoo(){
var im = new IM("yahoo");
im.login("mashupserver","wso2wsas");
im.sendMessage("manodya_r","test hi");
im.disconnect();
return "successful";
} 

msn.inputTypes="none";
msn.outputType="string";
function msn(){
var im = new IM("msn");
im.login("mashupserver@hotmail.com","wso2wsas");
im.sendMessage("yumaniranaweera@hotmail.com","test hi");
im.disconnect();
return "successful";
}

icq.inputTypes="none";
icq.outputType="string";
function icq(){
var im = new IM("icq");
im.login("427171591","wso2wsas");
im.sendMessage("469690601","test hi");
im.disconnect();
return "successful";
}

aim.inputTypes="none";
aim.outputType="string";
function aim(){
var im = new IM("icq");
im.login("mashupserver@aim.com","wso2wsas");
im.sendMessage("yumaniranaweera@aim.com","test hi");
im.disconnect();
return "successful";
}

//******************************************************************************************
// Additional Scenarios -With many consecutive messages
//******************************************************************************************
gabberManyMessages.inputTypes="none";
gabberManyMessages.outputType="string";
function gabberManyMessages(){
var im = new IM("gabber");
im.login("mashupserver@gmail.com","wso2wsas");
im.sendMessage("yumani@gmail.com","test hi");
im.sendMessage("yumani@gmail.com","how are u");
im.sendMessage("yumani@gmail.com","this is a test message");
im.sendMessage("yumani@gmail.com","this is a test message1");
im.disconnect();
return "successful";
} 

yahooManyMessages.inputTypes="none";
yahooManyMessages.outputType="string";
function yahooManyMessages(){
var im = new IM("yahoo");
im.login("mashupserver","wso2wsas");
im.sendMessage("manodya_r","test hi1");
im.sendMessage("manodya_r","test hi2");
im.sendMessage("manodya_r","test hi3");
im.sendMessage("manodya_r","test hi4");
im.sendMessage("manodya_r","test hi5");
im.disconnect();
return "successful";
}

//******************************************************************************************
// Additional Scenarios -With many consecutive messages & No disconnect
//******************************************************************************************
gabberManyMessagesWithNoDisconnect.inputTypes="none";
gabberManyMessagesWithNoDisconnect.outputType="string";
function gabberManyMessagesWithNoDisconnect(){
var im = new IM("gabber");
im.login("mashupserver@gmail.com","wso2wsas");
im.sendMessage("yumani@gmail.com","test hi");
im.sendMessage("yumani@gmail.com","how are u");
im.sendMessage("yumani@gmail.com","this is a test message");
im.sendMessage("yumani@gmail.com","this is a test message1");
return "successful";
} 

yahooManyMessagesWithNoDisconnect.inputTypes="none";
yahooManyMessagesWithNoDisconnect.outputType="string";
function yahooManyMessagesWithNoDisconnect(){
var im = new IM("yahoo");
im.login("mashupserver","wso2wsas");
im.sendMessage("manodya_r","test hi1");
im.sendMessage("manodya_r","test hi2");
im.sendMessage("manodya_r","test hi3");
im.sendMessage("manodya_r","test hi4");
im.sendMessage("manodya_r","test hi5");
return "successful";
}

//******************************************************************************************
// Additional Scenarios - With a 'system.wait'
//******************************************************************************************
yahooWithWait.inputTypes="none";
yahooWithWait.outputType="string";
function yahooWithWait(){
var im = new IM("yahoo");
im.login("mashupserver","wso2wsas");
im.sendMessage("manodya_r","Hi from yahooWithWait");
system.wait(60000);
im.disconnect();
return "successful";
} 

disconnectChat.inputTypes="none";
disconnectChat.outputType="string";
function disconnectChat(){
var im = new IM("yahoo");
im.login("mashupserver","wso2wsas");
im.sendMessage("manodya_r","Hi from yahooWithWait");
//system.wait(60000);
im.disconnect();
im.sendMessage("manodya_r","Hi from yahooWithWait");
return "successful";
} 

//******************************************************************************************
// Additional Scenarios - With 'emicons'
//******************************************************************************************
yahooWithEmicons.inputTypes="none";
yahooWithEmicons.outputType="string";
function yahooWithEmicons(){
var im = new IM("yahoo");
im.login("mashupserver","wso2wsas");
im.sendMessage("manodya_r","Hi from yahooWithEmicons :) :p");
im.disconnect();
return "successful";
} 

gabberWithEmicons.inputTypes="none";
gabberWithEmicons.outputType="string";
function gabberWithEmicons(){
var im = new IM("gabber");
im.login("mashupserver@gmail.com","wso2wsas");
im.sendMessage("yumani@gmail.com","Hi from yahooWithEmicons :) :p");
im.disconnect();
return "successful";
} 

//******************************************************************************************
// Additional Scenarios - Broadcasting
//******************************************************************************************
BroadcastingYahoo.inputTypes="none";
BroadcastingYahoo.outputType="string";
function BroadcastingYahoo(){
var im = new IM("yahoo");
im.login("mashupserver","wso2wsas");
im.sendMessage("manodya_r","Hi! This is to test IM broadcasting frm Mashup server :)  Pls BUZZ manodya_r if u recieve this");
im.sendMessage("keithgodwinchapman","Hi! This is to test IM broadcasting frm Mashup server :)  Pls BUZZ manodya_r if u recieve this");
im.sendMessage("channa_gun","Hi! This is to test IM broadcasting frm Mashup server :)  Pls BUZZ manodya_r if u recieve this");
//im.sendMessage("charitha_ka","Hi! This is to test IM broadcasting frm Mashup server :)  Pls BUZZ manodya_r if u recieve this");
im.disconnect();
return "successful";
} 

gabberWithEmicons.inputTypes="none";
gabberWithEmicons.outputType="string";
function gabberWithEmicons(){
var im = new IM("gabber");
im.login("mashupserver@gmail.com","wso2wsas");
im.sendMessage("yumani@gmail.com","Hi! This is to test IM broadcasting frm Mashup server :)  Pls BUZZ manodya_r if u recieve this");
im.sendMessage("keithgchapman@gmail.com","Hi! This is to test IM broadcasting frm Mashup server :)  Pls BUZZ manodya_r if u recieve this");
im.disconnect();
return "successful";
}