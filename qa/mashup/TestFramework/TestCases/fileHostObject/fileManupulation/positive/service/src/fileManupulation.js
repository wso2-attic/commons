/* Purpose	:	This is a sample service to verify the handling of "file" host object of WSO2 Mashup Server.
 * Author	: 	Yumani Ranaweera
 */

 //creating a file
function createFileTest(){
var file = new File("temp.txt");
var results = file.createFile();
	return results;
}

//open the file for writing
function openFileTest(){		
var text = "Hello World!";
var file = new File("temp.txt");
	file.openForWriting();	
	file.write(text);
}

//close the file
function closeFileTest(){
var file = new File("temp.txt");
	file.close(); 
	var filemove= file.move("backup/goodbye.txt"); 
	return filemove;
}
