/* Purpose	:	This is a sample service to verify the handling of "file" host object of WSO2 Mashup Server.
 * Author	: 	Yumani Ranaweera
 */

function FileMove(){
  var fileM = new File("helloM.txt");
  fileM.createFile();
  var moved = fileM.move("moved/helloM.txt");
  return moved;
 }

 function FileCreate(){
  var fileC = new File("tempdir/helloC.txt");
  var resultC= fileC.createFile();
  return resultC;
 }

 function FileDelete(){
  var fileD = new File("helloD.txt");
  fileD.createFile();

  var resultD= fileD.deleteFile();
  return resultD;
 }

 function FileWrite(){
  var fileW = new File("helloW.txt");
  if (fileW.exists){
  fileW.openForWriting();
  fileW.writeLine("this is a new line");
  }

  fileW.createFile();
  fileW.openForWriting();
  fileW.write("WSO2 Mashup Server");
  fileW.close();
 }