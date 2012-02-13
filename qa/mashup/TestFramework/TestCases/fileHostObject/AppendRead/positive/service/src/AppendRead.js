/* Purpose	:	This is a sample service to verify the handling of "file" host object of WSO2 Mashup Server.
 * Author	: 	Yumani Ranaweera
 */

function fileMan()
{
	var file = new File("readme.txt");
	file.createFile();
    file.openForAppending();
    file.write("Hello world!");
    file.close();
	file.openForReading();
    var x = file.readLine();
	return(x);
    file.close();
}