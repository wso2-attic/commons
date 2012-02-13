/* Purpose	:	This is a sample service to verify the handling of "file" host object of WSO2 Mashup Server.
 * Author	: 	Yumani Ranaweera
 */
 
function WriteRead(){
var file = new File ("hello.text");
if (!file.exists)
	file.createFile();
	
file.openForWriting();
file.write("hello this is a test");
file.close();

file.openForReading();
var text = file.read(20);
print (text);
file.close();
}