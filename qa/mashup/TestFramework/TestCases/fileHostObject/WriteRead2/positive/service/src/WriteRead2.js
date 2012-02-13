/* Purpose	:	This is a sample service to verify the handling of "file" host object of WSO2 Mashup Server.
 * Author	: 	Yumani Ranaweera
 */

this.documentation = <div>This is a service used to QA the <b>file host objects</b>.</div>;

//get the file object
var file = new File ("hello.text");

newFile.inputTypes = {};
newFile.outputType = "string";
function newFile(){
//cretae a new instance of the file object
	var result = file.createFile();
	if (!result){
		return (result + " = file was not created");}
	else{
		return (result + " = file was created"); }}

WritingReading.inputTypes = {"param" :"string"};
WritingReading.outputType = "string";
function WritingReading(param){
	file.openForWriting();
	file.write(param);
	file.close();
	file.openForReading();
	var readtext = file.read(10);
	file.close();
	return (readtext);
}