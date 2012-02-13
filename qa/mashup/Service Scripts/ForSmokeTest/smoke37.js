                        //******************************************************************************************
// Happy Scenarios
//******************************************************************************************
gabber.inputTypes="none";
gabber.outputType="string";
function gabber(){
var im = new IM("jabber");
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


    
    
    