
use XML::DOM;
use Mail::Sender;


use Getopt::Long;
use File::Compare;
use File::Copy;

use axis2c_test_module;
use rampart_test_module;
use sandesha_test_module;

use TestUtilities;

my $utils = new TestUtilities();

my $axis2_module = new axis2c_test_module();
my $rampart_module = new rampart_test_module();
my $sandesha_module = new sandesha_test_module();

my $logger = $utils->log_init ('wsfc_tests.logconf');

my $root = $utils->get_document_element('wsfc_tests.xml');

GetOptions(
			"co"    => \$checkout,
			"dist"  => \$dist,
      "bld"    => \$build,
			"at"    => \$axissamples,
			"rt"    => \$rampartsamples,
			"st"    => \$sandeshatests,
      "am"    => \$apachemodule,
			"mail"  => \$mail,
			"cm"  => \$clientmemory,
 		  "sm"  => \$servermemory,
			"all"    => \$all,
			"ms" => \$mysql,
			"guth" =>\$guththila,
			"lc" => \$libcurl
);
 
if(!$checkout && !$build && !$axissamples && !$rampartsamples && !$sandeshatests && !$all && !$apachemodule && !$mail && !$clientmemory && !$servermemory && !$dist)
{
	print "Please give the command line options as follows...\n";
	print "--all      To get a fresh checkout and run all the tests\n";
	print "--co       To get a fresh checkout\n";
	print "--dist     To make the distribution package\n";
	print "--bld      To Build the source\n";

	print "--ms       To build with mysql\n";
	print "--guth     To build with Guththila paser\n";
	print "--lc       To build with libcurl\n";
	print "--at       To run axis2c tests\n";
	print "--rt       To run Rampart tests\n";
        print "--st       To run Sandesha tests\n";
	print "--am       To run axis2c tests with apache server\n";
	print "--mail     To enable mail sender\n";
        print "--cm       To test client side memory\n";
        print "--sm       To test server side memory\n";
	die ;
}

print "WSO2 WSFC Test framework started...\n";
$logger->info("WSO2 WSFC test framework started...\n");

$sender = new Mail::Sender {smtp => 'relay.pair.com', from => 'WSO2 WSF/C Test Framework results <dushshantha@wso2.com>'};

if($checkout or $all)
{
  print "Deleting the source directory...\n";
  my @source =  $utils->get_element_value ($root , "directories", "source");
  $logger->info("Deleting the source directory...\n");
  #chdir $source[0] or die 'Cannot find the Source directory';

  if($^O eq 'MSWin32'){
    system "rd $source[0] /S/Q";
  }
  elsif($^O eq 'linux'){
  	system "rm -rf $source[0]";
  }

  print "Getting svn checkout \n";
  $logger->info ("Getting svn checkout \n");
  system "svn co https://svn.wso2.org/repos/wso2/trunk/wsf/c $source[0] > tmp.txt 2>error.txt";

  if( -s 'error.txt')
  {
    print "checkout failed...\n";
    $logger->info ("checkout failed...\n");
    if($mail)
    {
	  $sender->MailFile({to => 'dushshantha@wso2.com', subject => "[WSFC $^O Build] ERROR DOWNLOADING SOURCE FROM SVN",msg => "svn checkout failed... please see the attached log file",file => 'error.txt'});
    }
    die "unable to checkout the code";
  }
  else
  {
       print "checkout is done \n";
       $logger->info ("checkout is done\n");
   }
} #finished the checkout

#make the dist
if($dist or $all)
{
   if($^O eq 'linux'){
     &make_linux_dist();
   }
   
   elsif($^O eq 'MSWin32')
   {
       &compile_source_win32();
   }
}


#..........................................................................................................................................................
#   Test src distribution
#..........................................................................................................................................................   
#if($build or $all)
#{
#       my $database =  "--with-mysql=/usr/include/mysql ";
#       my $paser = "--enable-guththila=yes ";
#       my $transfer = "--enable-libcurl=yes ";
#
#       if($mysql)
#       {
#	push(@build_configuration,$database);
#       }
#       else
#       {
#        push(@build_configuration,"--with-sqlite=/usr/bin ");
#       }
#       if($guththila)
#       {
#	push(@build_configuration,$paser);
#       }
#       else
#       {
#	push(@build_configuration,"--enable-libxml=yes ");
#       }
#       if($libcurl)
#       {
#	push(@build_configuration,$transfer);
#       }
#       else
#       {
#	push(@build_configuration,"");
#       }
#       print "\nYour configuration is : " ." @build_configuration " . " \n";
#}

if($build or $all)
{
  if($^O eq 'MSWin32'){
    &compile_source_win32(); 
  }
  elsif($^O eq 'linux'){
    &compile_source_linux();
  }
}  

#print "Compiling Sample services \n";
#$logger->info ("Compiling Sample services \n");

if($axissamples or $all)
{
  $axis2_module->build_testcases();
  &run_simple_axis_server();
  $axis2_module->invoke_tests();
  &stop_simple_axis_server();
} #run axis samples done


if($apachemodule or $all)
{
  &test_apache_module();
}


if($rampartsamples or $all)
{
  
    printf("Testing Rampart...\n");
    $logger->info("Testing Rampart...\n");
    $rampart_module->build_testcases();
    &run_simple_axis_server();
    $rampart_module->invoke_tests();
    &stop_simple_axis_server();
  
}

if( $sandeshatests or $all)
{
  if($^O eq 'linux')
  { 
    printf("Testing Rampart...\n");
    $logger->info("Testing Rampart...\n");
    $sandesha_module->build_testcases();
    &run_simple_axis_server();
    $sandesha_module->invoke_tests();
    &stop_simple_axis_server();
  }
  elsif($^O eq 'MSWin32')
  {
    
  }
}



if($clientmemory or $all)
{
  &test_client_side_memory();
}

if($servermemory or $all)
{
  &test_server_side_memory();
}

my @working = &get_element_value ($root , "directories", "working");
chdir $working[0];
if($mail)
{
	$sender->MailFile({to => 'wsf-c-dev@wso2.org', subject => "[WSO2 WSF/C $^O Build] WSO2 WSFC Test framework SUCCESS",msg =>'Test framework finished. Please see the log file attached.',file =>'test.txt'});
}


sub compile_source_win32
{
  print "Copying the Configuration file...\n";
  $logger->info("Copying the Configuration file...\n");
  my @working = &get_element_value ($root , "directories", "working");
  my @source = &get_element_value ($root , "directories", "source");
  #chdir $working[0], or die "unable to change directory";
  #system "copy /Y configure.in $source[0]\\build\\win32";

  chdir $source [0];
  #chdir 'build\win32', or die "unable to change directory";

  print "compiling source...\n";
  $logger->info ("compiling source...\n");
  system 'build.bat > tmp.txt 2>error.txt';
  
  if( -s 'error.txt')
  {
    open(DAT, 'error.txt');
    @Error=<DAT>;
    close(DAT);
    foreach (@Error)
    {
      if (/Error/i)
      {
        print "build.bat failed...\n";
        $logger->info ("build.bat failed...\n");
        system 'copy /b tmp.txt+error.txt message.txt';
	      if($mail)
        {
         $sender->MailFile({to => 'wsf-c-dev@wso2.org', subject => "[Axis2C Windows Build] FAILED",msg =>'build failed on windows. Please see the attached log file.',file =>'message.txt'});
        }
        die "unable to build the source using nmake";
      }
    }
  }
  
  #system 'nmake tcpmon > tmp.log 2>error.log';
  
  #if( -s 'error.log')
  #{
  #  open(DAT, 'error.log');
  #  @Error=<DAT>;
  #  close(DAT);
  #  foreach (@Error)
  #  {
  #    if (/Error/i)
  #    {
  #      print "tcpmon nmake failed...\n";
  #      $logger->info ("tcpmon nmake failed...\n");
  #      system 'copy /b tmp.log+error.log message.log';
	#if($mail)
  #      {
  #      $sender->MailFile({to => 'wsf-c-dev@wso2.org', subject => '[Axis2C Windows Build] FAILED',msg =>'tcpmon nmake build failed on windows. Please see the attached log file.',file =>'message.log'});
  #      }
	#die "unable to build the source using nmake";
  #    }
  #  }
  #}
}

sub make_linux_dist
{
    print "Preparing Distribution...\n";
    $logger->info ("Preparing Distribution...\n");
    
    #my @deploy_dir =  $utils->get_element_value($root, "directories", "deploy");
    #my @apache_home =  &get_element_value ($root, "directories", "apache_home");
    #system "rm -rf $deploy_dir[0]";

    my @source_dir =  $utils->get_element_value($root, "directories", "source");
    chdir "$source_dir[0]", or die "Cannot change directory\n";
    #chdir "c", or die "Cannot change directory\n";
    system "make distclean";
    $logger->info ("make distclean");

    system `sh build.sh >tmp.log 2>error.log`;

    if( -s 'error.log')
    {
      open(DAT, 'error.log');
      @Error=<DAT>;
      close(DAT);
      foreach (@Error)
      {
        if (/Error/i)
        {
          print "build.sh failed...\n";
          $logger->info ("build.sh failed...\n");
          system 'cat error.log >> tmp.log';
          system 'cat tmp.log >> message.log';
         if($mail)
    	 {
	        $sender->MailFile({to => 'wsf-c-dev@wso2.org', subject => '[WSO2 WSF/C Linux Build] build.sh FAILED',msg =>'build failed on Linux. Please see the attached log file.',file =>'message.log'});
	     }
          die "build.sh failed...";
        }
      }
    }
    
    
    system "sh dist.sh >tmp.log 2>error.log";
    if( -s 'error.log')
    {
      open(DAT, 'error.log');
      @Error=<DAT>;
      close(DAT);
      foreach (@Error)
      {
        if (/Error/i)
        {
          print "dist.sh failed...\n";
          $logger->info ("dist.sh failed...\n");
          system 'cat error.log >> tmp.log';
          system 'cat tmp.log >> message.log';
          if($mail)
          {
	         $sender->MailFile({to => 'wsf-c-dev@wso2.org', subject => '[WSO2 WSF/C Linux Build] dist.sh FAILED',msg =>'build failed on Linux. Please see the attached log file.',file =>'message.log'});
          }
          die "dist.sh failed...";
        }
      }
    }
    print "WSO2 WSF/C distribution packaging is done...\n";
    $logger->info ("WSO2 WSF/C distribution packaging is done...\n");
}

sub compile_source_linux()
{
    print "compiling source...\n";

    $logger->info ("compiling source...\n");

    
    printf("deploy\n");
    my @deploy_dir =  $utils->get_element_value ($root, "directories", "deploy");

    printf("apache\n");
    my @apache_home = $utils->get_element_value ($root, "directories", "apache_home");
    printf("dist\n");
    system "rm -rf $deploy_dir[0]";



    my @dist_dir =  $utils->get_element_value ($root, "directories", "dist");

    chdir "$dist_dir[0]", or die "Cannot change directory\n";

    #chdir "c", or die "Cannot change directory\n";
    
    system "make distclean";
    $logger->info ("make distclean");

    printf("./configure --prefix=$deploy_dir[0] --with-openssl=/usr/bin/openssl --with-apache2=$apache_home[0]/include " . "@build_configuration"."> tmp.log 2>error.log");

    system "./configure --prefix=$deploy_dir[0] --with-openssl=/usr/bin/openssl --with-apache2=$apache_home[0]/include " . "@build_configuration"."> tmp.log 2>error.log";

    if( -s 'error.log')
    {
      open(DAT, 'error.log');
      @Error=<DAT>;
      close(DAT);
      foreach (@Error)
      {
        if (/Error/i)  
        {
          print "configure failed...\n";
          $logger->info ("configure failed...\n");
          system 'cat error.log >> tmp.log';
          system 'cat tmp.log >> message.log';
          if($mail)
          {

             $sender->MailFile({to => 'wsf-c-dev@wso2.org', subject => '[WSO2 WSF/C Linux Build] configure FAILED',msg =>'build failed on Linux. Please see the attached log file.',file =>'message.log'});

          }

          die "configure failed...";

        }

      }

    }


    system "make > tmp.log 2>error.log";
    if( -s 'error.log')

    {
      open(DAT, 'error.log');
      @Error=<DAT>;
      close(DAT);
      foreach (@Error)
      {
        if (/Error/i)
        {
          print "make failed...\n";  
          $logger->info ("make failed...\n");
          system 'cat error.log >> tmp.log';
          system 'cat tmp.log >> message.log';
          if($mail)
          {
             $sender->MailFile({to => 'wsf-c-dev@wso2.org', subject => '[WSO2 WSF/C Linux Build] make FAILED',msg =>'build failed on Linux. Please see the attached log file.',file =>'message.log'});

	  }

          die "make failed...";

        }

      }

    }

    system `make install > tmp.txt 2>error.txt`;

    if( -s 'error.txt')

    {
      open(DAT, 'error.txt');
      @Error=<DAT>;
      close(DAT);
      foreach (@Error)
      {
        if (/Error/i)
        {
          print "make install failed...\n";  
          $logger->info ("make install failed...\n");
          system 'cat error.txt >> tmp.txt';
          system 'cat tmp.txt >> message.txt';
          if($mail)
          {
             $sender->MailFile({to => 'wsf-c-dev@wso2.org', subject => '[WSO2 WSF/C Linux Build] make install FAILED',msg =>'build failed on Linux. Please see the attached log file.',file =>'message.txt'});

	  }

          die "make failed...";

        }

      }

    }

    
    system 'make samples >tmp.txt 2>error.txt ';

    if( -s 'error.txt')

    {

      open(DAT, 'error.txt');

      @Error=<DAT>;

      close(DAT);

      foreach (@Error)

      {
        if (/Error/i)
        {

          print "samples build failed...\n";

          $logger->info ("make samples failed...\n");

          system 'cat error.txt >> tmp.txt';

          system 'cat tmp.txt >> message.txt';

          if($mail)	

         {

          $sender->MailFile({to => 'wsf-c-dev@wso2.org', subject => '[Axis2C Linux Build] samples build FAILED',msg =>'build failed on Linux. Please see the attached log file.',file =>'message.txt'});

	 }

          die "samples build failed failed...";

        }

      }

    }

}



sub test_client_side_memory
{
   &run_simple_axis_server();
   my @value = &get_element_value ($root , "directories", "deploy");
   chdir $value[0], or die "unable to change directory $value[0]$! \n";
   chdir "bin" or die "unable to change director ";
   chdir "samples" or die "unable to change director " ;
   if($^O eq 'MSWin32'){
    system 'purify /SaveTextData=echo.txt echo.exe';
   }
   elsif($^O eq 'linux')
   {
    system 'valgrind ./echo 2>echo.txt';
    }
   if($mail)
    {
	$sender->MailFile({to => 'dushshantha@wso2.com,dushshantha.chandaradasa@gmail.com', subject => "[Axis2C $^O Build] Client side Memory report",msg =>'The Memory test report is done. please see the attachment.',file =>'echo.txt'});
     }
     &stop_simple_axis_server;
}

sub test_server_side_memory
{
   my @value = &get_element_value ($root , "directories", "deploy");
   chdir $value[0], or die "unable to change directory $value[0]$! \n";
   chdir "bin" or die "unable to change director ";
   if($^O eq 'MSWin32'){
		system 'start purify /SaveTextData=ServerSideMemory.txt axis2_http_server.exe';
   }
   elsif($^O eq 'linux')
   {
    system 'valgrind ./axis2_http_server 2>ServerSideMemory.txt'
   }
   sleep 5;
   $modul->invoke_clients();
   &stop_simple_axis_server;
   if($mail)
    {
	$sender->MailFile({to => 'dushshantha@wso2.com,dushshantha.chandaradasa@gmail.com', subject => "[Axis2C $^O Build] Server side Memory report",msg =>'The Memory test report is done. please see the attachment.',file =>'ServerSideMemory.txt'});
    }

}

sub test_apache_module
{
	if($^O eq 'MSWin32')
	{
		&test_apache_module_win();
	}
	elsif($^O eq 'linux')
	{
		&test_apache_module_linux();
	}
}	

sub test_apache_module_win
{
  print "Building Axis2 apache module...\n";
  $logger->info ("Building Axis2 apache module...\n");

  @source = &get_element_value ($root , "directories", "source");
  chdir $source [0], or warn "unable to change directory";
  chdir 'build\win32', or warn "unable to change directory";

  my $apache_Err;

  system 'nmake axis2_apache_module > tmp.log 2>error.log';

  if( -s 'error.log')
  {

    open(DAT, 'error.log');
    @Error=<DAT>;
    close(DAT);
    foreach (@Error)
    {
      if (/Error/i)
      {

        print "Apache module build failed...\n";
        $logger->info ("Apache module build failed...\n");
        system 'copy /b tmp.log+error.log message.log';
        if($mail)
    	{
		$sender->MailFile({to => 'wsf-c-dev@wso2.org', subject => '[Axis2C Apache Module Windows Build] FAILED',msg =>'Axis2C Apache Module build failed on windows. Please see the attached log file.',file =>'message.log'});
	}
        $apache_Err = 1;
        die "unable to build apache module using nmake";
      }
    }
  }
  if($apache_Err == 0)
  {
    print "Apache module build successful...\n";
    $logger->info ("Apache module build successful...\n");
    
    my @value = &get_element_value ($root , "directories", "deploy");
    my @apache_home = &get_element_value ($root , "directories", "apache_home");
    my @working = &get_element_value ($root , "directories", "working");
    chdir $value[0], or die "unable to change directory $value[0]$! \n";
    chdir "lib" or die "unable to change director ";
    
    if ($^O eq 'MSWin32')
    {
      system "copy /Y mod_axis2.dll $apache_home[0]\\modules";
      chdir $working[0], or die "unable to change directory $working[0]$! \n";
      system "copy /Y httpd.conf $apache_home[0]\\conf";
      system 'start httpd';
      &invoke_clients();
      system "start wmic process where name='httpd.exe' delete "   ;
      
    }
      
  }
} #  end test_apache_module

sub test_apache_module_linux
{

      print "Testing Axis2 apache module...\n";
      $logger->info ("Testing Axis2 apache module...\n");
      my @value = &get_element_value ($root , "directories", "deploy");
      my @apache_home = &get_element_value ($root , "directories", "apache_home");
      my @working = &get_element_value ($root , "directories", "working");
      chdir $value[0], or die "unable to change directory $value[0]$! \n";
      chdir 'lib', or die  'unable to change directory';     

      system "cp libmod_axis2.so $apache_home[0]/modules";
      chdir $working[0], or die "unable to change directory $working[0]$! \n";
      system "cp httpd.conf $apache_home[0]/conf";
      chdir $apache_home[0], or die "unable to change directory $apache_home[0]$! \n";
      chdir 'bin',or die 'unable to change directory ';
      system './httpd -X &';
      &invoke_clients();
      my @process = `ps ax`;
      foreach (@process)
      {
	     next unless /httpd/;
	     s#^\s*(\d+).*(\d+)#$1#g;
            kill (9, $_) if /\d+/;
      
      }
    
}

sub get_document_element
{
    my $file = "axis2c_tests.xml";
    $logger->info ("axis2c tests configuration file is $file ");

    my $parser = XML::DOM::Parser->new();
    my $root = $parser->parsefile($file);
}


sub get_element_value
{
    my @output;
    my @directories = $_[0]->getElementsByTagName($_[1]);
    foreach (@directories)
    {
	     my $directory = $_->getElementsByTagName ($_[2]);
	     push (@output, $directory->item(0)->getFirstChild->getNodeValue);
    }
    @output;
}

sub log_init
{
    my $axis2c_tests_logconf = 'axis2c_tests.logconf';
    Log::Log4perl::init_and_watch( $axis2c_tests_logconf, 5 );
    my $logger = Log::Log4perl::get_logger('main');
}

sub run_simple_axis_server
{
    $logger->info ("Enter into run simple axis server");
    my @output;
    my @value = &get_element_value ($root , "directories", "deploy");
    $logger->info ("element value of deploy directory $value[0]");

    chdir $value[0], or die "unable to change directory $value[0]$! \n";
    chdir "bin", or die "unable to change directory $value[0]$! \n";
    $logger->info ("change into $value[0]/bin directory ");

    my @name = &get_element_value ($root, "server", "name");
    my @port = &get_element_value ($root, "server", "port");
    $logger->info ("server name is $name[0] and starting port is $port[0]");

    if($^O eq 'MSWin32')
    {
      system " start $name[0] -p$port[0]";
    }
    elsif($^O eq 'linux')
    {
      system "./$name[0] -p$port[0] &";
    }
    $logger->info ("simple axis server execution finished ... ");
}


sub stop_simple_axis_server
{
  my $killed=0;
  print "killing simple axis server... ";
  if($^O eq 'MSWin32')
  {
    system "start wmic process where name='axis2_http_server.exe' delete "   ;
  }
  elsif ($^O eq 'linux')
  {
    my @process = `ps ax`;
    foreach (@process)
    {
	     next unless /axis2_http_server/;
	     s#^\s*(\d+).*(\d+)#$1#g;
	     $logger->info ("axis2_http_server pid $_");
	     kill (9, $_) if /\d+/;
    }
    @process = `ps ax`;
    foreach (@process)
    {
	     next unless /axis2_http_server/;
         $killed=1;
    }
  }
  if ($killed == 0)
  {
    print " killed.\n";
    $logger->info ("Simple axis server killed successfully. \n");
  }
  else
  {
    print " error!\n";
    $logger->info ("Error killing simple axis server. \n");
  }

}

sub massage_compare
{
  my $test_name = @_[0];
  my @working = &get_element_value ($root , "directories", "working");
  &capture_massage ($test_name);

  
  my $req_act_msg;
  my $req_exp_msg;
  my $res_act_msg;
  my $res_exp_msg;
  
  if($^O eq 'MSWin32')
  {
    $req_act_msg= "@_[1]\\$test_name.request.xml";
    $req_exp_msg= "@_[2]\\$test_name.request.xml";
    $res_act_msg= "@_[1]\\$test_name.response.xml";
    $res_exp_msg= "@_[2]\\$test_name.response.xml";
  }
  elsif($^O eq 'linux')
  {
    $req_act_msg= "@_[1]/$test_name.request.xml";
    $req_exp_msg= "@_[2]/$test_name.request.xml";
    $res_act_msg= "@_[1]/$test_name.response.xml";
    $res_exp_msg= "@_[2]/$test_name.response.xml";
  }
  
  my $result = msgcomp($req_act_msg, $req_exp_msg);
  if ($result == 0)
  {
     print "  Request Message  PASS\n";
     $logger->info ("  Request Message  PASS\n");
  }
  else
  {
     print "  Request Message  FAIL\n";
     $logger->info ("  Request Message  FAIL\n");
  }
  
  $result = msgcomp($res_act_msg, $res_exp_msg);
  if ($result == 0)
  {
     print "  Response Message  PASS\n";
     $logger->info ("  Response Message  PASS\n");
  }
  else
  {
     print "  Response Message  FAIL\n";
     $logger->info ("  Response Message  FAIL\n");
  }
}

sub msgcomp
{
  my $result = 0;
  my $diff = XML::SemanticDiff->new(keeplinenums => 1);
  
  foreach my $change ($diff->compare(@_[0], @_[1])) {
    if($change->{message} !~ (/MessageID/i or /RelatesTo/i))
    {
       $result++;
    }
  }
  return $result;
}


sub capture_massage
{
    my $test_name = @_[0];

    my @working = &get_element_value ($root , "directories", "working");
    my $expected_req = "$working[0]/msg/$test_name.reqest";
    my @deploy = &get_element_value ($root , "directories", "deploy");
    if($^O eq 'MSWin32'){
      my $actual_req_file = "@deploy[0]\\bin\\reqest";
      my $copy_req_file = "$working[0]\\act_msg\\$test_name.request.xml";
      &make_xml($actual_req_file,$copy_req_file);
      
      my $actual_res_file =  "@deploy[0]\\bin\\response";
      my $copy_res_file = "$working[0]\\act_msg\\$test_name.response.xml";
      &make_xml($actual_res_file,$copy_res_file);

    }
    elsif($^O eq 'linux')
    {
      my $actual_req = "@deploy[0]/bin/tools/reqest";
      my $copy_req_file = "$working[0]/act_msg/$test_name.reqest.xml";
      &make_xml($actual_req_file,$copy_req_file);
      
      my $actual_res =  "@deploy[0]/bin/tools/response";
      my $copy_res_file = "$working[0]/act_msg/$test_name.response.xml";
      &make_xml($actual_res_file,$copy_res_file);
    }


}

sub make_xml
{
  open(DAT, @_[0]);
  my @str = <DAT>;
  close(DAT);
  my $ind = 0;
  my $Index;

  foreach (@str)
  {
    if(/<?xml/i)
    {
      $Index = $ind;
    }
    else
    {
      $ind++;
    }
    }

  my $b = 0;
  while ($b <= $ind)
    {
    shift(@str);
    $b++;
    }
  my @aaa = split( /Envelope>/,$str[1]);
  open(INFO, ">@_[1]");
  print INFO  "$str[0]$aaa[0]Envelope>";
  close (INFO);
  
  system "del @_[0]";
}


sub start_monitor
{
  my @value = &get_element_value ($root , "directories", "deploy");
  chdir $value[0], or die "unable to change directory $value[0]$! \n";
  chdir "bin", or die "unable to change directory $value[0]$! \n";

  print "Start Monitor \n";

  my @port = &get_element_value ($root, "server", "port");
    
  if($^O eq 'MSWin32'){
    system "start tcpmon.exe -lp 9090 -tp $port[0] -th localhost --test ";
     print "Started Monitor \n";
  }
  elsif($^O eq 'linux'){
     chdir "bin/tools", or die "unable to change directory $value[0]$! \n";
     system "./tcpmon.exe -lp 9090 -tp $port[0] -th localhost --test &";
  }
}

sub stop_monitor
{
  if($^O eq 'MSWin32')
  {
    system "start wmic process where name='tcpmon.exe' delete "   ;
  }
  elsif ($^O eq 'linux')
  {
    my @process = `ps ax`;
    foreach (@process)
    {
	     next unless /tcpmon/;
	     s#^\s*(\d+).*(\d+)#$1#g;
       kill (9, $_) if /\d+/;
    }
  }
}

sub tail
{
   my $chunk = 400 * $numlines; #assume a <= 400 char line(generous)

  # Open the file in read mode
  open FILE, "<$filename" or die "Couldn't open $filename: $!";
  my $filesize = -s FILE;
  if($chunk >= $filesize){$chunk = $filesize}
    seek FILE,-$chunk,2; #get last chunk of bytes

  my @tail = <FILE>;
  if($numlines >= $#tail +1){$numlines = $#tail +1}
  splice @tail, 0, @tail - $numlines;
  
}
