#!usr/bin/perl

use TestUtilities;
use XML::DOM;
use Log::Log4perl;
use XML::SemanticDiff;
use Getopt::Long;
use File::Compare;
use File::Copy;

#server parameter declaration
my $utils = new TestUtilities();
my $logger = $utils->log_init ('greg-tests.logconf');
my $root = $utils->get_document_element('greg_test.xml');

my $hostname="localhost";
my $httpsport=9443;
my $httpport=9763;
my $webcontextroot="";
my $url="https://$hostname:$httpsport$webcontextroot/registry";
my $resultfile="results.txt";
my $successcases=0;
my $failcases=0;
my $totaltestcases=0;
my $testhomevalue="";
my $working_directory="";
my $testdirctoryName="";
my $carbon_home="";
my $testName="";

print "The server URL is: https://$hostname:$httpsport$webcontextroot/registry \n";
$logger->info("The server URL is: https://$hostname:$httpsport$webcontextroot/registry \n");

print "checkin/out test framework is started \n\n";
$logger->info("checkin/out test framework is started \n\n");


test1();
test2();
test3();
test4();
test5();
test6();
test7();
test8();
test9();
test10();
test11();
test12();
test13();
test14();
test15();
test16();
test17();
test18();
test19();
test20();
test21();
test22();
test23();
test24();
test25();
test26();
test27();
test28();
test29();
test30();
test31();
test32();
test33();
test34();
test35();
test36();



finalrestresult();
cleanuptest();

sub readtestlog
{ 
  open (FILE, $resultfile);
  my @lines = <FILE>;
  print @lines;
  close(FILE);
  
  foreach (@lines)
  {
    if (/Operation invoked Successfully/i && !/Error in dumping the path/i && !/Error in restoring the path/i)
    {
       print "Operation successful\n";
       $logger->info ("Operation successful...\n");
       $successcases++;
      	
      
       $append = 0;
       if ($append)
	 {
	 open(MYOUTFILE, ">filename.out"); #open for write, overwrite
	 }
	else
	 {
	 open(MYOUTFILE, ">>filename.out"); #open for write, append
	 }
        print MYOUTFILE "test$totaltestcases -- $testName\n"; #write text, no newline
        close(MYOUTFILE);
    }

    elsif(/Error in dumping the path/i or /Error in restoring the path/i){

          $successcases++;
          $logger->error("Operation fails, There is no resource in the given path or the user don't have the READ permission to the path \n");
          $append = 0;
       	  if ($append)
	 	{
	  open(MYOUTFILE, ">filename.out"); #open for write, overwrite
	 	}
		else
	 	{
	 	open(MYOUTFILE, ">>filename.out"); #open for write, append
	 	}
        	print MYOUTFILE "test$totaltestcases -- $testName\n"; #write text, no newline
        	close(MYOUTFILE);
          }	


    elsif(/Error /i or /Exception /i)
    {  
 
       print "Operation fails with errors \n";
       $logger->error("Operation with errors \n");
       $failcases++;
       $appendfail=0;
	if ($appendfail)
	 {
	 open(MYOUTFILEFAIL, ">filename-fail.out"); #open for write, overwrite
	 }
	else
	 {
	 open(MYOUTFILEFAIL, ">>filename-fail.out"); #open for write, append
	 }
        print MYOUTFILEFAIL "test$totaltestcases -- $testName\n"; #write text, no newline
        close(MYOUTFILEFAIL);
     }
     elsif (/Exception /i){
       print "Operation fails due to unknown reason, re-execute the command for debugging \n";
       $logger->error("Operation with unknown errors \n");
       $failcases++;
       $appendfail=0;
	if ($appendfail)
	 {
	 open(MYOUTFILEFAIL, ">filename-fail.out"); #open for write, overwrite
	 }
	else
	 {
	 open(MYOUTFILEFAIL, ">>filename-fail.out"); #open for write, append
	 }
        print MYOUTFILEFAIL "test$totaltestcases -- $testName\n"; #write text, no newline
        close(MYOUTFILEFAIL);
     }     
   }
}

sub readtestxml
{    
    my @testroot = $root->getElementsByTagName("tests");
    my @test = $testroot[0]->getElementsByTagName("$_[0]");
    my @commandValues;
    my $testdumpfileName="";

    foreach (@test)
    {
       my $name_value = $_->getElementsByTagName("name");
       $testName = $name_value->item(0)->getFirstChild->getNodeValue;
       my $operation_value = $_->getElementsByTagName("operation");
       my $operationName = $operation_value->item(0)->getFirstChild->getNodeValue;
       my $username_value = $_->getElementsByTagName("username");
       my $userName = $username_value->item(0)->getFirstChild->getNodeValue;
       my $password_value = $_->getElementsByTagName("password");
       my $passwordName = $password_value->item(0)->getFirstChild->getNodeValue;
       my $testdirectory_value = $_->getElementsByTagName("directory");
          $testdirctoryName = $testdirectory_value->item(0)->getFirstChild->getNodeValue;
       my $testcollection_value = $_->getElementsByTagName("collectionname");
       my $testcollectionName = $testcollection_value->item(0)->getFirstChild->getNodeValue;
       my $testdumpfile_value = $_->getElementsByTagName("dumpfilename");
      
       if ($testdumpfile_value->item(0)->hasChildNodes()){
           $testdumpfileName = $testdumpfile_value->item(0)->getFirstChild->getNodeValue;
       }

       print "The test name is:                 $testName \n";
       print "The checkin/out operation name is: $operationName \n";
       print "The user name is:                 $userName \n";
       print "The password is:                  $passwordName \n";
       print "The checkout directory is:        $testdirctoryName \n";
       print "The registry collection name is:  $testcollectionName \n";
       print "The dump file name is:            $testdumpfileName \n";
       
       @commandValues = ("$testName","$operationName","$userName","$passwordName","$testdirctoryName","$testcollectionName","$testdumpfileName");
    }
    return @commandValues;
}

sub commandexecute
{
    my @commandconstruct={};
    @commandconstruct = readtestxml("$_[0]");
    
    unlink($resultfile);
   
    print "Running the @commandconstruct[0] \n";

    $urltocheckout = "$url" . "@commandconstruct[5]";
    print "The URL is $urltocheckout \n";
   
    my $dumpfilename = $commandconstruct[6];
    my $dumpfilelenght = length($dumpfilename);
   
    if($dumpfilelenght > 0 && @commandconstruct[1] ne "ci" )
     {      
       print "Running:  sh checkin-client.sh @commandconstruct[1] $urltocheckout -u @commandconstruct[2] -p @commandconstruct[3] -f @commandconstruct[6] -d @commandconstruct[4] \n\n";
       system("sh checkin-client.sh @commandconstruct[1] $urltocheckout -u @commandconstruct[2] -p @commandconstruct[3] -f @commandconstruct[6] -d @commandconstruct[4] >> $resultfile");
     }
    elsif(@commandconstruct[1] ne "ci")
     {
       print "Running: sh checkin-client.sh @commandconstruct[1] $urltocheckout -u @commandconstruct[2] -p @commandconstruct[3] -d @commandconstruct[4] \n\n";
       system("sh checkin-client.sh @commandconstruct[1] $urltocheckout -u @commandconstruct[2] -p @commandconstruct[3] -d @commandconstruct[4]  >> $resultfile");
     }
    elsif($dumpfilelenght <= 0 && @commandconstruct[1] eq "ci")
     {
      print "Running:  echo 'yes' | sh checkin-client.sh @commandconstruct[1] $urltocheckout -u @commandconstruct[2] -p @commandconstruct[3] -d @commandconstruct[4] \n\n";
      system("echo 'yes' | sh checkin-client.sh @commandconstruct[1] $urltocheckout -u @commandconstruct[2] -p @commandconstruct[3] -d @commandconstruct[4]  >> $resultfile");
     }
    elsif($dumpfilelenght > 0 && @commandconstruct[1] eq "ci")
     {
      print "Running: echo 'yes' | sh checkin-client.sh @commandconstruct[1] $urltocheckout -u @commandconstruct[2] -p @commandconstruct[3] -f @commandconstruct[6] -d @commandconstruct[4]";
      system("echo 'yes'| sh checkin-client.sh @commandconstruct[1] $urltocheckout -u @commandconstruct[2] -p @commandconstruct[3] -f @commandconstruct[6] -d @commandconstruct[4] >> $resultfile"); 
     }

    if ( $? == -1 )
    {
     print "command failed: $!\n";
     $logger->info("test$totaltestcases -- $testName\n");
     $logger->info("Checkout command failed.");
    }
    elsif($? >> 8 == 0 )
    {
     print "command exited sucessfully with value: ", $? >> 8;
     print "\n";
     $logger->info("test$totaltestcases -- $testName\n");
     $logger->error("Command execution successful.");
    }
    
    system ("cat $resultfile >> tmp-results.log");
}

sub finalrestresult()
{
   print "\n\n";
   print "Total test cases:  $totaltestcases \n";
   print "Successful test cases:  $successcases \n";   
   print "Failed test cases:  $failcases \n";
   $percentage = roundup($failcases / $totaltestcases * 100);
   print "Presenatage of failure cases: $percentage %\n";
   print "\n";
   print "Passed Test Cases \n";
   print "======================= \n";
   open(MYINPUTFILE, "<filename.out"); # open for input
   my(@lines) = <MYINPUTFILE>; # read file into list
  	foreach $line (@lines) # loop thru list
   	{
   	print "$line"; # print in sort order
   	}
 	close(MYINPUTFILE);  
 	print " \n";

  print "Failed Test Cases \n";
  print "======================= \n";
  open(MYINPUTFILEFAIL, "<filename-fail.out"); # open for input
  my(@linefail) = <MYINPUTFILEFAIL>; # read file into list
  	foreach $linefails (@linefail) # loop thru list
   	{
   	print "$linefails"; # print in sort order
   	}
 	close(MYINPUTFILEFAIL);  
 	print " \n"; 
  system ("cat filename-fail.out >> tmp-results.log");
  system ("cat filename.out >> tmp-results.log");
 }
 
  
sub cleanuptest
{
 unlink("filename.out");
 unlink("filename-fail.out");
 unlink("results.txt");
}

sub roundup 
{
    my $n = shift;
    return(($n == int($n)) ? $n : int($n + 1))
}


sub get_test_home
{
  my @value = $utils->get_element_value ($root , "directories", "test_home");
  print "get test home is @value[0]";
  my $value_test_home=@value[0];
  return $value_test_home;
}

sub get_working_home
{
  my @value_working_dir = $utils->get_element_value ($root , "directories", "working_home");
  $working_directory = @value_working_dir[0];
  return $working_directory;
}

sub get_carbon_home
{
  my @value_carbon_dir = $utils->get_element_value ($root , "directories", "carbon_home");
  $carbon_home = @value_carbon_dir[0];
  return $carbon_home;
}

sub fileexists
{  
   my $fileexist = 0;
   my $filename = "$_[0]";  
   my $target_path = "$_[1]";
   my $out_put_file = "filelist.txt";
    
   unlink($out_put_file);
   system ("grep -I -R  -L $filename $target_path |grep -v .meta >> $out_put_file");
   print "grep -I -R  -L $filename $target_path |grep -v .meta >> $out_put_file \n";
   use Cwd;
   my $dir = getcwd;

   use Cwd 'abs_path';
    
   open (FILEOUT, $out_put_file);
   my @fileout = <FILEOUT>;
   print @fileout;
   close(FILEOUT);
  
   foreach (@fileout)
    {
      my $result = rindex($filename,@fileout[i]);
      if (/$filename/i)
      {
        print "file found in the $target_path and the file is at @fileout[i] \n";
        $logger->info ("file found in the $target_path and the file is at @fileout[i] ...\n");
        $fileexist = 1;
        last;
      }
    }      
   return $fileexist;
}

sub test1()
{
    print "Running test1... \n";
    $totaltestcases++;
    commandexecute("test1");
    readtestlog();
}

sub test2()
{
    print "Running test2... \n";
    $totaltestcases++;
    commandexecute("test2");
    readtestlog();
}

sub test3()
{
    print "Running test3... \n";
    $totaltestcases++;
    commandexecute("test3");
    readtestlog();
}

sub test4()
{
    print "Running test4... \n";
    $totaltestcases++;
    commandexecute("test4");
    readtestlog();
}

sub test5()
{
   print "Running test5... \n";
   $totaltestcases++;
   commandexecute("test5");
   readtestlog();
}

sub test6()
{
   print "Running test6... \n";
   $totaltestcases++;
   commandexecute("test6");
   readtestlog();
}

sub test7()
{
   print "Running test7... \n";
   $totaltestcases++;
   commandexecute("test7");
   readtestlog();
} 

sub test8()
{
   print "Running test8... \n";
   $totaltestcases++;
   commandexecute("test8");
   readtestlog();
}

sub test9()
{
   print "Running test9... \n";
   $totaltestcases++;
   commandexecute("test9");
   readtestlog();
}

sub test10()
{ 
   my $filename = "README.txt";
   print "Running test10... \n";
   $totaltestcases++;
   my $test_home = get_test_home();

   use Cwd;
   my $dir = getcwd;  
   
   my $new_file_path = "$test_home" . "/" . "$filename";

   @xmlreader = readtestxml("test10");
   my $targetpath = "./" . "@xmlreader[4]";
   my $sourcepath = "$test_home" . "/" . "$filename";
   
   my $newvalue = rtrim($newnew);
  
   if (-e $new_file_path){

      system ("cp -r $sourcepath $targetpath");
   }
   commandexecute("test10");
   readtestlog();
}

sub test11()
{
   my $filename = "README.txt";
   print "Running test11... \n";
   $totaltestcases++;
   commandexecute("test11");

   use Cwd;
   my $dir = getcwd;  

   my $new_file_path = "$dir" . "/" . "$testdirctoryName" . "/" . "$filename";

   if (-e $new_file_path){

       readtestlog();
   } 
   else{
       $failcases++;
   }
}

sub test12()
{
   my $filename = "README.txt";
   print "Running test12... \n";
   $totaltestcases++;
   readtestxml("test12");
   my $absolute_file_path = "./" . "$testdirctoryName" . "/" . "$filename"; 
   
   use Cwd;
   my $dir = getcwd;  

   my $new_file_path = "$dir" . "/" . "$testdirctoryName" . "/" . "$filename";
   
   if (-e $new_file_path){
       my $file_text = "This is new line to $filename file\n";
       file_append("$absolute_file_path", "$file_text");
       my $file_match_status = check_file_content("$absolute_file_path" , "$file_text");     
       if ($file_match_status != 0){       
          print "inside if test12..";
          commandexecute("test12");
          readtestlog();
       }
   } 
   else{
       $failcases++;
   }
}

sub test13()
{
   my $filename = "README.txt";
   print "Running test13... \n";
   $totaltestcases++;
   readtestxml("test13");
   my $absolute_file_path = "./" . "$testdirctoryName" . "/" . "$filename";
   use Cwd;
   my $dir = getcwd;  

   my $new_file_path = "$dir" . "/" . "$testdirctoryName" . "/" . "$filename";
   commandexecute("test13");

   if (-e $new_file_path){
       my $file_text = "This is new line to $filename file \n";
       my $file_match_status = check_file_content("$new_file_path" , "$file_text");
       if ($file_match_status != 0){
          readtestlog();
       }
   }
   else{
       $failcases++;
   }
}

sub test14()
{
   my $filename = "file:with:colon.txt";
   print "Running test14... \n";
   $totaltestcases++;
   my $test_home = get_test_home();

   use Cwd;
   my $dir = getcwd;  
   
   my $new_file_path = "$test_home" . "/" . "$filename";

   @xmlreader = readtestxml("test14");
   my $targetpath = "./" . "@xmlreader[4]";
   my $sourcepath = "$test_home" . "/" . "$filename";
   
   my $newvalue = rtrim($newnew);
  
   if (-e $new_file_path){

      system ("cp -r $sourcepath $targetpath");
   }
   commandexecute("test14");
   readtestlog();
}

sub test15()
{
   my $filesystemfilename = "file%3Awith%3Acolon.txt";
   print "Running test15... \n";
   $totaltestcases++;
   commandexecute("test15");

   use Cwd;
   my $dir = getcwd;  

   my $new_file_path2 = "$dir" . "/" . "$testdirctoryName" . "/" . "$filesystemfilename";
   if (-e $new_file_path2){

       readtestlog();
   } 
   else{
       $failcases++;
   }
}

sub test16()
{
   my $filename = "file1\\ space\\ test.txt";
   print "Running test16... \n";
   $totaltestcases++;
   my $test_home = get_test_home();

   use Cwd;
   my $dir = getcwd;  
   
   my $new_file_path = "$test_home" . "/" . "$filename";

   @xmlreader = readtestxml("test16");
   my $targetpath = "./" . "@xmlreader[4]";
   my $sourcepath = "$test_home" . "/" . "$filename";

   my $newvalue = rtrim($newnew);
   
   $new_file_path2 = $sourcepath;
   my $test_path = "$test_home" . "/" . "file1\ space\ test.txt";
  
   if (-e $test_path){
      
      print "The command in: cp -r $sourcepath $targetpath \n";
      system ("cp -r $sourcepath $targetpath");

   }
   commandexecute("test16");
   readtestlog();
}

sub test17()
{
   my $filesystemfilename = "file1+space+test.txt";
   print "Running test17... \n";
   $totaltestcases++;
   commandexecute("test17");

   use Cwd;
   my $dir = getcwd;  

   my $new_file_path2 = "$dir" . "/" . "$testdirctoryName" . "/" . "$filesystemfilename";

   print "The new file path is : $new_file_path2 \n";

   if (-e $new_file_path2){
       readtestlog();
   } 
   else{
       $failcases++;
   }
}

sub test18()
{
   $filename = "file:with:colon.txt";
   print "Running test18... \n";
   $totaltestcases++;
   
   use Cwd;
   my $dir = getcwd;  
   
   @xmlreader = readtestxml("test18");
   my $targetpath = "./" . "@xmlreader[4]";
  
   commandexecute("test18");
   
   readtestlog();
}

sub test19()
{
   $filename = "README.txt";
   print "Running test19... \n";
   $totaltestcases++;
   
   use Cwd;
   my $dir = getcwd;  
   
   @xmlreader = readtestxml("test19");
   my $targetpath = "./" . "@xmlreader[4]";
   
   my $new_file_path2 = "$dir" . "/" . "$testdirctoryName" . "/" . "$filename";
   
   system("rm $new_file_path2");
  
   commandexecute("test19");
   
   readtestlog();
}

sub test20()
{
   $filename = "README.txt";
   print "Running test20... \n";
   $totaltestcases++;
   
   use Cwd;
   my $dir = getcwd;  
   
   @xmlreader = readtestxml("test20");
   my $targetpath = "./" . "@xmlreader[4]";
   
   my $new_file_path = "$dir" . "/" . "$testdirctoryName" . "/" . "$filename";
   
   commandexecute("test20");
   
   if (not -e $new_file_path){      
      print "$filename doesn't exist at $targetpath \n";
      readtestlog();
 
   }   
}

sub test21()
{
   print "Running test21... \n";
   $totaltestcases++;
   
   use Cwd;
   my $dir = getcwd;  
   
   @xmlreader = readtestxml("test21");
   my $targetpath = "./" . "@xmlreader[4]";
   
   my $carbon_home = get_carbon_home();
   my $resource_path = $carbon_home . "/resources";
   system ("mkdir $targetpath");
      
   if (-e $resource_path){
      
      print "The command in: cp -r $resource_path $targetpath \n";
      system ("cp -r $resource_path $targetpath");

   }
   commandexecute("test21");
   readtestlog();   
}

sub test22()
{
   print "Running test22... \n";
   $totaltestcases++;
   
   use Cwd;
   my $dir = getcwd;  
   
   @xmlreader = readtestxml("test22");
   my $targetpath = "./" . "@xmlreader[4]";
   my $carbon_home = get_carbon_home();
   my $resource_path = $carbon_home . "/resources";
   my $resource_checkout_path = $targetpath . "/resources";

   commandexecute("test22");
   if (diff_directory($resource_path,$resource_checkout_path)){
      readtestlog();
   }  
 }

sub test23()
{
   print "Running test23... \n";
   $totaltestcases++;
   
   use Cwd;
   my $dir = getcwd;  
   
   @xmlreader = readtestxml("test23");
   my $targetpath = "./" . "@xmlreader[4]";
   my $carbon_home = get_carbon_home();
   my $resource_path = $carbon_home . "/dbscripts";
   
   if (-e $resource_path){
      
      print "The command in: cp -r $resource_path $targetpath \n";
      system ("cp -r $resource_path $targetpath");

   }
   commandexecute("test23");
   readtestlog();
  
 }

sub test24()
{
   print "Running test24... \n";
   $totaltestcases++;
   
   use Cwd;
   my $dir = getcwd;  
   
   @xmlreader = readtestxml("test24");
   my $targetpath = "./" . "@xmlreader[4]";
   my $carbon_home = get_carbon_home();
   my $resource_path = $carbon_home . "/dbscripts";
   my $resource_checkout_path = $targetpath . "/resources/";
    
   commandexecute("test24"); 
 
   if ((diff_directory($resource_path,$targetpath . "/dbscripts/")) && (diff_directory($carbon_home . "/resources",$targetpath . "/resources/")))
   {
    readtestlog();
   }   
 }

sub test25()
{
   print "Running test25... \n";
   $totaltestcases++;
   
   use Cwd;
   my $dir = getcwd;  
   
   @xmlreader = readtestxml("test25");
   my $targetpath = "./" . "@xmlreader[4]";
   my $resource_checkout_path = $targetpath . "/resources";
   system ("rm -rf $resource_checkout_path");

   if (not -e $resource_checkout_path){      
      commandexecute("test25");
   }   
   readtestlog();
 }

sub test26()
{
   print "Running test26... \n";
   $totaltestcases++;
   
   use Cwd;
   my $dir = getcwd;  
   
   @xmlreader = readtestxml("test26");
   my $targetpath = "./" . "@xmlreader[4]";
   my $resource_checkout_path = $targetpath . "/resources/";
    
   commandexecute("test26"); 
 
   if (not -e $resource_checkout_path){
      readtestlog();     
   }  
 }

sub test27()
{
   print "Running test27... \n";
   $totaltestcases++;
   
   use Cwd;
   my $dir = getcwd;  
   
   @xmlreader = readtestxml("test27");
   my $testhome = get_test_home();
   my $testhome_governance = $testhome . "/governance";
   my $testhome_governance_artifacts = $testhome_governance . "/*";
   my $targetpath = "$dir" . "/" . "@xmlreader[4]" . "/";
   system("mkdir $targetpath");
   if (-e $testhome_governance){
 
      print "The command in: cp -r $testhome_governance_artifacts $targetpath \n";
      system("cp -r $testhome_governance_artifacts $targetpath");      
   }   
   commandexecute("test27"); 
   readtestlog();     
}

sub test28()
{
   print "Running test28... \n";
   $totaltestcases++;
   
   use Cwd;
   my $dir = getcwd;  
   
   @xmlreader = readtestxml("test28");
   my $testhome = get_test_home();
   my $testhome_governance = $testhome . "/governance";
   my $targetpath = "$dir" . "/" . "@xmlreader[4]";
   
   commandexecute("test28"); 

   if (diff_directory($testhome_governance,$targetpath)) 
   {
    readtestlog();
   }    
}


sub test29()
{
   print "Running test29... \n";
   $totaltestcases++;
   
   use Cwd;
   my $dir = getcwd;  
   
   @xmlreader = readtestxml("test29");
   my $testhome = get_test_home();
   my $testhome_symlink = $testhome . "/symlink";
   my $testhome_symlink_artifacts = $testhome_symlink . "/*";
   my $targetpath = "$dir" . "/" . "@xmlreader[4]" . "/";
   system("mkdir $targetpath");
   if (-e $testhome_symlink){
 
      print "The command in: cp -r $testhome_symlink_artifacts $targetpath \n";
      system("cp -r $testhome_symlink_artifacts $targetpath");      
   }   
   commandexecute("test29"); 
   readtestlog();         
}

sub test30()
{
   print "Running test30... \n";
   $totaltestcases++;
   
   use Cwd;
   my $dir = getcwd;  
   
   @xmlreader = readtestxml("test30");
   my $testhome = get_test_home();
   my $testhome_symlink = $testhome . "/symlink";
   my $targetpath = "$dir" . "/" . "@xmlreader[4]";
   
   commandexecute("test30"); 
   readtestlog();

}

sub test31()
{
   print "Running test31... \n";
   $totaltestcases++;
   
   use Cwd;
   my $dir = getcwd;  
   
   @xmlreader = readtestxml("test31");
   my $testhome = get_test_home();
   my $targetpath = "$dir" . "/" . "@xmlreader[4]";
   
   commandexecute("test31"); 

   readtestlog();   
}

sub test32()
{
   print "Running test32... \n";
   $totaltestcases++;
   
   use Cwd;
   my $dir = getcwd;  
   
   @xmlreader = readtestxml("test32");
   my $testhome = get_test_home();
   my $targetpath = "$dir" . "/" . "@xmlreader[4]";
   
   commandexecute("test32"); 

   readtestlog();   
}

sub test33()
{
   print "Running test33... \n";
   $totaltestcases++;
   
   use Cwd;
   my $dir = getcwd;  
   
   @xmlreader = readtestxml("test33");
   
   commandexecute("test33");
   readtestlog();   
}

sub test34()
{
   print "Running test34... \n";
   $totaltestcases++;
   
   use Cwd;
   my $dir = getcwd;  
   
   @xmlreader = readtestxml("test34");
   
   commandexecute("test34");
   readtestlog();   
}

sub test35()
{
   print "Running test35... \n";
   $totaltestcases++;
    
   use Cwd;
   my $dir = getcwd;  
   
   @xmlreader = readtestxml("test35");
   
   commandexecute("test35");
   readtestlog();
   changefile();
}


sub test36()
{
   print "Running test36... \n";
   $totaltestcases++;
   #readtestxml("test12");
   #commandexecute("test12"); #checkout updated README.txt file to regitry root.
   #unlink($resultfile);#delete the result file, otherwise end result will be conficted.
   
   readtestxml("test36");
   my $filename = "README.txt";
   my $filename_mime = "README.txt.mine";
   my $absolute_file_path = "./" . "$testdirctoryName" . "/" . "$filename"; 
   
   use Cwd;
   my $dir = getcwd;  

   my $new_file_path = "$dir" . "/" . "$testdirctoryName" . "/" . "$filename";
   my $mime_file_path = "$dir" . "/" . "$testdirctoryName" . "/" . "$filename_mime";
   unlink("$new_file_path");
   system("touch $new_file_path");
   
   if (-e $new_file_path){
       my $file_text = "Conficting line to $filename file";
       file_append("$absolute_file_path", "$file_text");
       commandexecute("test36");
       readtestlog();
       }

   if (-e $mime_file_path ){
        readtestlog();
       }
   }

sub changefile(){
   readtestxml("test12");
   my $filename = "README.txt";
   my $absolute_file_path = "./" . "$testdirctoryName" . "/" . "$filename"; 
   my $path = "./" . "$testdirctoryName" . "/*";
   use Cwd;
   my $dir = getcwd;  

   my $new_file_path = "$dir" . "/" . "$testdirctoryName" . "/" . "$filename";
   
   system("rm -rf $path");
   unlink("$new_file_path");
   system("touch $new_file_path");
   
   if (-e $new_file_path){
       my $file_text = "New new line $filename file";
       file_append("$absolute_file_path", "$file_text");
       commandexecute("test12");#checkout updated README.txt file to regitry root.
       }
    
   unlink($resultfile);#delete the result file, otherwise end result will be conficted.
}

sub diff_directory
{
   open(PS_F, "diff -r --exclude .meta $_[0] $_[1]|");
   my @commandlines;
   @commandlines = <PS_F>;


   if (@commandlines ) { # @commandlines is not empty...
  	foreach (@commandlines)
   	{
	print "fail ***111* \n";
    	if (/Only in/i)
    	{
      	print "diff of dicrectories fail /n";
    	}
    	elsif(/differ/i)
    	{
      	print "diff of dicrectories fail /n";
    	}
   	}         
   } else { # @commandlines is empty  	
    return true;
  }
}

sub file_append
{
  open (FILEOPEN, "+>>$_[0]") || die "Can't Open File: $_[0]\n";
  print FILEOPEN "$_[1]\n";
  my @lines = <FILEOPEN>;
  print @lines;
  close(FILEOPEN);
}

sub check_file_content
{
  my $file_content_match_status = 0;
  open (FILECONTENTOPEN, "+<$_[0]") || die "Can't Open File: $_[0]\n";
  my $file_content = rtrim("$_[1]"); 
  
  my @content = <FILECONTENTOPEN>;

    
  foreach $line (@content) {
   
    print $line;
    if (rtrim($line) eq "This is new line to README.txt file"){
       $logger->info ("file content found in $_[0], content is $line \n");
       $file_content_match_status = 1;
       last;
    }
  }
  return $file_content_match_status;
  close(FILECONTENTOPEN)
}

sub rtrim($)
{
  my $string = shift;
  $string =~ s/\s+$//;
  return $string;
}
