
use XML::DOM;
use Mail::Sender;


use Getopt::Long;
use File::Compare;
use File::Copy;
use axis2c_test_module;
 
use TestUtilities;

my $utils = new TestUtilities();

my $modul = new axis2c_test_module();

my $logger = $utils->log_init ('my_wsfc_tests.logconf');

my $root = $utils->get_document_element('my_wsfc_tests.xml');

GetOptions(
			"co"    => \$checkout,
			"dist"  => \$dist,
                        "bld"    => \$build,
			"test"    => \$phpsamples,
			"rs"    => \$rampartsamples,
			"am"    => \$apachemodule,
			"mail"  => \$mail,
			"cm"  => \$clientmemory,
        		"sm"  => \$servermemory,
			"all"    => \$all,
			"ms" => \$mysql,
			"guth" =>\$guththila, 
			"lc" => \$libcurl

);
 
if(!$checkout && !$build && !$axissamples && !$buildrampart && !$rampartsamples && !$all && !$apachemodule && !$mail && !$clientmemory && !$servermemory && !$dist && !$mysql && !$guththila && !$libcurl && !$phpsamples)
{
	print "Please give the command line options as follows...\n";
	print "--all      To get a fresh checkout and run all the tests\n";
	print "--co       To get a fresh checkout\n";
	print "--dist     To make the distribution package\n";
	print "--bld      To Build the source\n";
	print "--ms       To build with mysql\n";
	print "--guth     To build with Guththila paser\n";
	print "--lc       To build with libcurl\n";
	print "--test     To run PHP samples\n";
	print "--rs       To run Rampart samples\n";
	print "--am       To run axis2c tests with apache server\n";
	print "--mail     To enable mail sender\n";
        print "--cm       To test client side memory\n";
        print "--sm       To test server side memory\n";
	#$modul->run_test();	
	die ;
}

print "WSO2 WSF/PHP Test framework started...\n";
$logger->info("WSO2 WSF/PHP test framework started...\n");

$sender = new Mail::Sender {smtp => 'relay.pair.com', from => 'WSO2 WSF/PHP Test Framework results <krishantha@wso2.com>'};

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
  system "svn co https://wso2.org/repos/wso2/trunk/wsf/php $source[0] > tmp.log 2>error.log";

  if( -s 'error.log')
  {
    print "checkout failed...\n";
    $logger->info ("checkout failed...\n");
    if($mail)
    {
	  $sender->MailFile({to => 'krishantha@wso2.com', subject => "[WSF/PHP $^O Build] ERROR DOWNLOADING SOURCE FROM SVN",msg => "svn checkout failed... please see the attached log file",file => 'error.log'});
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
     &make_linux_dist2();
   }
   
   #for windows there's no dist process
  
   #elsif($^O eq 'MSWin32')
   #{
       #build windows source
   #}
}


#..........................................................................................................................................................
#   Test src distribution
#..........................................................................................................................................................   
if($build or $all)
{      
       my $database =  "--with-mysql=/usr/bin/mysql ";
       my $paser = "--enable-guththila=yes ";
       my $transfer = "--enable-libcurl=yes ";

       if($mysql)
       {
	push(@build_configuration,$database);
       }
       else
       {
        push(@build_configuration,"--with-sqlite=/usr/bin/sqlite ");
       }
       if($guththila) 
       {
	push(@build_configuration,$paser);
       }
       else
       {
	push(@build_configuration,"--enable-libxml2=yes ");
       }	
       if($libcurl)
       {
	push(@build_configuration,$transfer);
       }
       else
       {
	push(@build_configuration,"");
       }
       print "\nYour configuration is : " ." @build_configuration " . " \n";
}

if($build or $all)
{
  if($^O eq 'MSWin32'){
    &compile_source_win32();
  }
  elsif($^O eq 'linux'){
    &compile_source_linux();
    #&compile_samples();
  }
} #build samples done 

#print "Compiling Sample services \n";
#$logger->info ("Compiling Sample services \n");

if($phpsamples or $all)
{
  &start_apache_server();
  &invoke_test();
  &stop_apache_server();
} #run php samples done

if($buildrampart or $all)
{
	&build_rampart();
}

if($apachemodule or $all)
{
  &test_apache_module();
}


if($rampartsamples or $all)
{
  if($^O eq 'linux')
  {
    &invoke_rampart_tests_linux();
  }
  elsif($^O eq 'MSWin32')
  {
    &invoke_rampart_tests_win32();
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
	$sender->MailFile({to => 'dushshantha@wso2.com,samisa@wso2.com', subject => "[Axis2C $^O Build] Axis2C Test framework SUCCESS",msg =>'Test framework finished. Please see the log file attached.',file =>'test.log'});
}


sub compile_source_win32
{
  print "Copying the Configuration file...\n";
  $logger->info("Copying the Configuration file...\n");
  my @working = &get_element_value ($root , "directories", "working");
  my @source = &get_element_value ($root , "directories", "source");
  chdir $working[0], or die "unable to change directory";
  system "copy /Y configure.in $source[0]\\build\\win32";

  chdir $source [0];
  chdir 'build\win32', or die "unable to change directory";

  print "compiling source...\n";
  $logger->info ("compiling source...\n");
  system 'nmake all > tmp.log 2>error.log';
  
  if( -s 'error.log')
  {
    open(DAT, 'error.log');
    @Error=<DAT>;
    close(DAT);
    foreach (@Error)
    {
      if (/Error/i)
      {
        print "nmake failed...\n";
        $logger->info ("nmake failed...\n");
        system 'copy /b tmp.log+error.log message.log';
        #$sender->MailFile({to => 'tungsten-c-dev@lists.wso2.com', subject => '[WSO2 WSF/PHP Windows Build] FAILED',msg =>'nmake build failed on windows. Please see the attached log file.',file =>'message.log'});
        die "unable to build the source using nmake";
      }
    }
  }
}

sub make_linux_dist2
{
    my @source_dir =  $utils->get_element_value($root, "directories", "source");
    chdir "$source_dir[0]", or die "Cannot change directory\n"; 
    #chdir "c", or die "Cannot change directory\n";
    system "pwd \n";
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
	         $sender->MailFile({to => 'wsf-c-dev@wso2.org', subject => '[WSO2 WSF/PHP Linux Build] dist.sh FAILED',msg =>'build failed on Linux. Please see the attached log file.',file =>'message.log'});
          }
          die "dist.sh failed...";
        }
      }
    }
    print "dist running.....! \n";
    system "pwd \n";
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
	         $sender->MailFile({to => 'wsf-c-dev@wso2.org', subject => '[WSO2 WSF/PHP Linux Build] dist.sh FAILED',msg =>'build failed on Linux. Please see the attached log file.',file =>'message.log'});
          }
          die "dist.sh failed...";
        }
      }
    }
    print "WSO2 WSF/PHP distribution packaging is done...\n";
    $logger->info ("WSO2 WSF/PHP distribution packaging is done...\n");
    system "pwd \n";
}

sub make_linux_dist
{
    

    my @source_dir =  $utils->get_element_value($root, "directories", "source");
    chdir "$source_dir[0]", or die "Cannot change directory\n";
   
    system "make distclean";
    $logger->info ("make distclean");
    system "pwd \n";
    system `sh build.sh`;
    # >tmp.log 2>error.log:

    system "pwd \n";

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
	        $sender->MailFile({to => 'krishantha@wso2.org', subject => '[WSO2 WSF/PHP Linux Build] build.sh FAILED',msg =>'build failed on Linux. Please see the attached log file.',file =>'message.log'});
	 }
          die "build.sh failed...";
        }
      }
    }
    
    chdir "$source_dir[0]", or die "Cannot change directory\n";
    system "pwd \n\n";
    print "Running dist...";
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
	         $sender->MailFile({to => 'wsf-c-dev@wso2.org', subject => '[WSO2 WSF/PHP Linux Build] dist.sh FAILED',msg =>'build failed on Linux. Please see the attached log file.',file =>'message.log'});
          }
          die "dist.sh failed...";
        }
      }
    }
    print "WSO2 WSF/PHP distribution packaging is done...\n";
    $logger->info ("WSO2 WSF/PHP distribution packaging is done...\n");
}

sub compile_source_linux() 
{
       
    printf("dist\n");
    my @dist_dir =  $utils->get_element_value ($root, "directories", "dist");
    my @source_dir =  &get_element_value ($root, "directories", "source");
    my @working_dir =  &get_element_value ($root, "directories", "working");
    my @apache_home =  &get_element_value ($root, "directories", "apache_home");
    my @deploy_dir =  &get_element_value ($root, "directories", "deploy");
 
    chdir "$deploy_dir[0]", or die "Cannot change deploy directory\n";
    system "rm -rf wsf*";
    printf "Deploy directory deleted. \n";
    printf `pwd`;
    printf "\n";

    chdir "$source_dir[0]", or die "Cannot change to source directory\n";  
    system 'tar -xf wso2-wsf-php-src*.tar.gz';
    printf "File exctrated successfully \n";
    printf "\n";

    chdir "$dist_dir[0]", or die "Cannot change to dist directory\n";
    printf `pwd`;
    printf "\n";

    printf "compiling source...\n";
    $logger->info ("compiling source...\n");



    printf "./configure --with-wsf --enable-openssl --with-apache2=$apache_home[0]/include --with-axis2=`pwd`/wsf_c/axis2c/include --enable-tests=yes --enable-wsclient=no --enable-savan=no --prefix=`php-config --extension-dir`/wsf_c". "@build_configuration";


     system "./configure --prefix=`php-config --extension-dir`/wsf_c --with-wsf --enable-openssl --with-apache2=$apache_home[0]/include --with-axis2=`pwd`/wsf_c/axis2c/include --enable-tests=yes --enable-wsclient=no --enable-savan=no "."@build_configuration";

    print "\n";
    print "@build_configuration";
    print "\n";

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

	  $sender->MailFile({to => 'wsf-c-dev@wso2.org', subject => '[WSF/PHP Linux Build] configure FAILED',msg =>'build failed on Linux. Please see the attached log file.',file =>'message.log'});

           }

          die "configure failed...";

        }

      }

    }
   else{
	print "configure successfull...";
        print "\n";
    }

   system "pwd \n"; 
   system `make`;
   printf "running make..... \n";

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

	  $sender->MailFile({to => 'krishantha@wso2.org', subject => '[WSO2 WSF/PHP Linux Build] make FAILED',msg =>'make failed on Linux. Please see the attached log file.',file =>'message.log'});

	  }

          die "make failed...";

        }

      }

    }
else{
	print "make successfull...";
        print "\n";
    }

#> tmp.log 2>error.log
   system "pwd \n"; 
   system `make install`;
   print "running make install \n";

    if( -s 'error.log')

    {

      open(DAT, 'error.log');

      @Error=<DAT>;

      close(DAT);

      foreach (@Error)

      {

        if (/Error/i)

        {

          print "make install failed...\n";

          $logger->info ("make install failed...\n");

          system 'cat error.log >> tmp.log';

          system 'cat tmp.log >> message.log';

          if($mail)

          {

	  $sender->MailFile({to => 'krishantha@wso2.org', subject => '[WSO2 WSF/PHP Linux Build] make install FAILED',msg =>'build failed on Linux. Please see the attached log file.',file =>'message.log'});

	  }

          die "make install failed...";

        }

      }

    }
else{
	print "make install successfull...";
        print "\n";
    }
}

sub compile_samples
{
    $logger->info ("compiling axis2c samples ");
    my @source_dir = &get_element_value ($root, "directories", "source");
    chdir $source_dir[0] or
	die "unable to change direcotory";
    chdir "samples" or die "samples doesn't exsist \n";
    $logger->info ("changed direcotory to $source_dir[0]/samples");

    system `sh autogen.sh`;

    if( -s 'error.log')
    {
      open(DAT, 'error.log');
      @Error=<DAT>;
      close(DAT);
      foreach (@Error)
      {
        if (/Error/i)
        {
          print "autogen failed...\n";
          $logger->info ("autogen failed...\n");
          system 'cat error.log >> tmp.log';
          system 'cat tmp.log >> message.log';
          if($mail)
          {
		$sender->MailFile({to => 'wsf-c-dev@wso2.org', subject => '[Axis2C Linux Build] autogen FAILED',msg =>'build failed on Linux. Please see the attached log file.',file =>'message.log'});
	  }
          die "autogen failed...";
        }
      }
    }


    system `./configure --prefix=$ENV{'AXIS2C_HOME'} \
--with-axis2_util=$ENV{'AXIS2C_HOME'}/include \
--with-axiom=$ENV{'AXIS2C_HOME'}/include > tmp.log 2>error.log`;
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
	  	$sender->MailFile({to => 'wsf-c-dev@wso2.org', subject => '[Axis2C Linux Build] configure FAILED',msg =>'build failed on Linux. Please see the attached log file.',file =>'message.log'});
	 }
          die "configure failed...";
        }
      }
    }

    system `make >tmp.log 2>error.log`;
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
	  	$sender->MailFile({to => 'wsf-c-dev@wso2.org', subject => '[Axis2C Linux Build] make FAILED',msg =>'build failed on Linux. Please see the attached log file.',file =>'message.log'});
	   }
          die "make failed...";
        }
      }
    }

    system `make install > tmp.log 2>error.log`;
    if( -s 'error.log')
    {
      open(DAT, 'error.log');
      @Error=<DAT>;
      close(DAT);
      foreach (@Error)
      {
        if (/Error/i)
        {
          print "make install failed...\n";
          $logger->info ("make install failed...\n");
          system 'cat error.log >> tmp.log';
          system 'cat tmp.log >> message.log';
          if($mail)
          {
	  $sender->MailFile({to => 'wsf-c-dev@wso2.org', subject => '[Axis2C Linux Build] make install FAILED',msg =>'build failed on Linux. Please see the attached log file.',file =>'message.log'});
	  }
          die "make failed...";
        }
      }
    }
}

sub build_rampart
{
  if($^O eq 'MSWin32')
  {
    &build_rampart_win32();
  }
  elsif($^O eq 'linux')
  {
    &build_rampart_linux();
  }

}#end sub build_rampart

sub build_rampart_win32
{
  print "Building Rampart...\n";
  $logger->info ("Building Rampart...\n");
  my $rampart_Err = 1;
  
  my @source = &get_element_value ($root , "directories", "source");
  chdir $source [0],or die "unable to change directory";
  chdir 'build\win32', or die "unable to change directory";
  system 'nmake mod_rampart_all > tmp.log 2>error.log';
  if( -s 'error.log')
  {
    open(DAT, 'error.log');
    @Error=<DAT>;
    close(DAT);
    foreach (@Error)
    {
      if (/Error/i)
      {
        print "Rampart build failed...\n";
        $logger->info ("Rampart build failed...\n");
        system 'copy /b tmp.log+error.log message.log';
        
	if($mail)
        {

	$sender->MailFile({to => 'wsf-c-dev@wso2.org', subject => '[Axis2C Rampart Windows Build] FAILED',msg =>'Rampart build failed on windows. Please see the attached log file.',file =>'message.log'});
	}
        $rampart_Err = 0;
        #die "unable to build Rampart using nmake";
     }
    }
  }

  if($rampart_Err == 1)
  {
    print "Rampart build successful...\n";
    $logger->info ("Rampart build successful...\n");
  }
}#end of build_rampart_win32

sub build_rampart_linux
{
  print "Building Rampart...\n";
  $logger->info ("Building Rampart...\n");
  my $rampart_Err = 1;

  my @source = &get_element_value ($root , "directories", "source");
  chdir $source [0],or die "unable to change directory";
  chdir 'rampart', or die "unable to change directory";
  system `sh autogen.sh`;

    if( -s 'error.log')
    {
      open(DAT, 'error.log');
      @Error=<DAT>;
      close(DAT);
      foreach (@Error)
      {
        if (/Error/i)
        {
          $rampart_Err = 0;
          print "autogen failed...\n";
          $logger->info ("autogen failed...\n");
          system 'cat error.log >> tmp.log';
          system 'cat tmp.log >> message.log';
         if($mail)
         {
		 $sender->MailFile({to => 'wsf-c-dev@wso2.org', subject => '[Axis2C Rampart Linux Build] autogen FAILED',msg =>'build failed on Linux. Please see the attached log file.',file =>'message.log'});
	 }
          #die "autogen failed...";
        }
      }
    }


    system `./configure --prefix=$ENV{'AXIS2C_HOME'} > tmp.log 2>error.log`;
    if( -s 'error.log')
    {
      open(DAT, 'error.log');
      @Error=<DAT>;
      close(DAT);
      foreach (@Error)
      {
        if (/Error/i)
        {
          $rampart_Err = 0;
          print "configure failed...\n";
          $logger->info ("configure failed...\n");
          system 'cat error.log >> tmp.log';
          system 'cat tmp.log >> message.log';
          if($mail)
          {
		$sender->MailFile({to => 'wsf-c-dev@wso2.org', subject => '[Axis2C Rampart Linux Build] configure FAILED',msg =>'build failed on Linux. Please see the attached log file.',file =>'message.log'});
	  }
          #die "configure failed...";
        }
      }
    }

    system `make >tmp.log 2>error.log`;
    if( -s 'error.log')
    {
      open(DAT, 'error.log');
      @Error=<DAT>;
      close(DAT);
      foreach (@Error)
      {
        if (/Error/i)
        {
          $rampart_Err = 0;
          print "make failed...\n";
          $logger->info ("make failed...\n");
          system 'cat error.log >> tmp.log';
          system 'cat tmp.log >> message.log';
          if($mail)
    	  {
		$sender->MailFile({to => 'wsf-c-dev@wso2.org', subject => '[Axis2C Rampart Linux Build] make FAILED',msg =>'build failed on Linux. Please see the attached log file.',file =>'message.log'});
	  }
          #die "make failed...";
        }
      }
    }

    system `make install > tmp.log 2>error.log`;
    if( -s 'error.log')
    {
      open(DAT, 'error.log');
      @Error=<DAT>;
      close(DAT);
      foreach (@Error)
      {
        if (/Error/i)
        {
          $rampart_Err = 0;
          print "make install failed...\n";
          $logger->info ("make install failed...\n");
          system 'cat error.log >> tmp.log';
          system 'cat tmp.log >> message.log';
          if($mail)
   	  {
	  $sender->MailFile({to => 'wsf-c-dev@wso2.org', subject => '[Axis2C Rampart Linux Build] make install FAILED',msg =>'build failed on Linux. Please see the attached log file.',file =>'message.log'});
	  }
          #die "make failed...";
        }
      }
    }

  if($rampart_Err == 1)
  {
    print "Rampart build successful...\n";
    $logger->info ("Rampart build successful...\n");
  }

  chdir 'samples', or die "unable to change directory";
  system `sh autogen.sh`;

    if( -s 'error.log')
    {
      open(DAT, 'error.log');
      @Error=<DAT>;
      close(DAT);
      foreach (@Error)
      {
        if (/Error/i)
        {
          $rampart_Err = 0;
          print "autogen failed...\n";
          $logger->info ("autogen failed...\n");
          system 'cat error.log >> tmp.log';
          system 'cat tmp.log >> message.log';
         if($mail)
    	 { 
		$sender->MailFile({to => 'wsf-c-dev@wso2.org', subject => '[Axis2C Rampart samples Linux Build] autogen FAILED',msg =>'build failed on Linux. Please see the attached log file.',file =>'message.log'});
	 }
          #die "autogen failed...";
        }
      }
    }


    system `./configure --prefix=$ENV{'AXIS2C_HOME'} > tmp.log 2>error.log`;
    if( -s 'error.log')
    {
      open(DAT, 'error.log');
      @Error=<DAT>;
      close(DAT);
      foreach (@Error)
      {
        if (/Error/i)
        {
          $rampart_Err = 0;
          print "configure failed...\n";
          $logger->info ("configure failed...\n");
          system 'cat error.log >> tmp.log';
          system 'cat tmp.log >> message.log';
          if($mail)
    	  {
		$sender->MailFile({to => 'wsf-c-dev@wso2.org', subject => '[Axis2C Rampart samples Linux Build] configure FAILED',msg =>'build failed on Linux. Please see the attached log file.',file =>'message.log'});
	  }
          #die "configure failed...";
        }
      }
    }

    system `make >tmp.log 2>error.log`;
    if( -s 'error.log')
    {
      open(DAT, 'error.log');
      @Error=<DAT>;
      close(DAT);
      foreach (@Error)
      {
        if (/Error/i)
        {
          $rampart_Err = 0;
          print "make failed...\n";
          $logger->info ("make failed...\n");
          system 'cat error.log >> tmp.log';
          system 'cat tmp.log >> message.log';
          if($mail)
    	  {
	  	$sender->MailFile({to => 'wsf-c-dev@wso2.org', subject => '[Axis2C Rampart Samples Linux Build] make FAILED',msg =>'build failed on Linux. Please see the attached log file.',file =>'message.log'});
	  }
          #die "make failed...";
        }
      }
    }

    system `make install > tmp.log 2>error.log`;
    if( -s 'error.log')
    {
      open(DAT, 'error.log');
      @Error=<DAT>;
      close(DAT);
      foreach (@Error)
      {
        if (/Error/i)
        {
          $rampart_Err = 0;
          print "make install failed...\n";
          $logger->info ("make install failed...\n");
          system 'cat error.log >> tmp.log';
          system 'cat tmp.log >> message.log';
          if($mail)
    	  {
		$sender->MailFile({to => 'wsf-c-dev@wso2.org', subject => '[Axis2C Rampart Samples Linux Build] make install FAILED',msg =>'build failed on Linux. Please see the attached log file.',file =>'message.log'});
	  }
          #die "make failed...";
        }
      }
    }

  if($rampart_Err == 1)
  {
    print "Rampart samples build successful...\n";
    $logger->info ("Rampart samples build successful...\n");
  }
}#end of build_rampart_linux

sub invoke_rampart_tests_linux
{
    
    $logger->info ("Enter into invoke Rampart clients");
    my @output;
    my $all = 0;
    my $success = 0;
    my $fail = 0;

    my @source = &get_element_value ($root , "directories", "source");
    chdir $source[0], or die "unable to change directory\n ";

    chdir 'rampart/samples/client/sec_echo',or die "unable to change directory\n";
    system 'sh deploy_client_repo.sh';
    my @nodes = $root->getElementsByTagName("rampart_tests");

    my @test = $nodes[0]->getElementsByTagName("test");
    $logger->info ("got child nodes ");

    my @value = &get_element_value ($root , "directories", "deploy");
    my @client_repo =  &get_element_value ($root , "directories", "clientrepo");
    $logger->info ("deploy directory value is $value[0]\n\n");
    
    my @working = &get_element_value ($root , "directories", "working");

    #my @error;
    foreach (@test)
    {
       my $name = $_->getElementsByTagName("name");
       my $test_name = $name->item(0)->getFirstChild->getNodeValue;	     
      
       my $file_name = $_->getElementsByTagName("file");
       my $file = $file_name->item(0)->getFirstChild->getNodeValue;	


       $logger->info ("file name is $file");
	
       my $service = $_->getElementsByTagName("service");
       my $service_name = $service->item(0)->getFirstChild->getNodeValue;

       print "\nCopy Configuration files...\n";
       system "cp -f $working[0]/rampart_config/$test_name/client_axis2.xml $client_repo[0]/axis2.xml";
       system "cp $working[0]/rampart_config/$test_name/services.xml $value[0]/services/$service_name";
       
       print "cp -f $working[0]/rampart_config/$test_name/client_axis2.xml $client_repo[0]/axis2.xml";
       my @arg = $_->getElementsByTagName("arg");

       my @arg_values="";
       $all++;
       foreach (@arg)
       {
	  push (@arg_values, $_->getFirstChild->getNodeValue);
       }

       #my $exp_result = $_->getElementsByTagName("expected_result");
       #my $exp_result_val = $exp_result->item(0)->getFirstChild->getNodeValue;

       #if ($exp_result_val eq 'Fault')
       #{
       #    my $fault_msg = $_->getElementsByTagName("fault_msg");
       #    my $fault_msg_val = $fault_msg->item(0)->getFirstChild->getNodeValue;
       #s}
	&run_simple_axis_server();
        sleep 3;

	print "invoking $test_name \n";
	$logger->info ("$all. printed invoking $test_name to screen ");

       #&start_monitor();
       chdir $value[0], or die "unable to change directory $value[0]$! \n";
       chdir "bin/samples" or die "unable to change directory ";
       chdir "rampart/client/sec_echo" or die "unable to change directory ";
       $logger->info ("changed directory to $value[0]/rampart/samples/client");
       $logger->info ("Repeating results may occure for loop ahead \n");
      
       $logger->info ("running client: ./$file @arg_values > $working[0]/rampart_config/$test_name/$file.out \n");

       system "./$file @arg_values > $working[0]/rampart_config/$test_name/$file.out";
 
       #&stop_monitor(); 
       #&file_difference($file);
       &stop_simple_axis_server();
       sleep 1;

      chdir "$working[0]/rampart_config/$test_name", or die;
      if(compare("$file.out","$file.expected") == 0)
      {
	$success++;
	print "Test $test_name .... Passed\n";
        $logger->info ("Test $test_name .... Passed\n\n");
      }
      else
      {
	 print "Test $test_name .... Failed\n";
         $logger->info ("Test $test_name .... Failed\n\n");
      }
    }

    $fail = $all - $success;
    print "\n\n==================Summery==============\n\n";
    $logger->info ("==================Summery==============");

    print "Total Tests        : $all\n";
    $logger->info ( "Total Tests        : $all\n");

    print "Successful Invokes : $success\n";
    $logger->info ("Successful Invokes : $success\n");

    print "Failed Invokes     : $fail\n";
    $logger->info ("Failed Invokes     : $fail\n");

    print "\n========================================\n";
    $logger->info ("========================================\n");
    	
}

sub invoke_rampart_tests_win32
{

    $logger->info ("Enter into invoke Rampart clients");
    my @output;
    my $all = 0;
    my $success = 0;
    my $fail = 0;

    my @source = &get_element_value ($root , "directories", "source");
    chdir $source[0], or die "unable to change directory\n ";

    chdir 'rampart/samples/client/sec_echo',or die "unable to change directory\n";
    system 'deploy_client_repo.bat';
    my @nodes = $root->getElementsByTagName("rampart_tests");

    my @test = $nodes[0]->getElementsByTagName("test");
    $logger->info ("got child nodes ");

    my @value = &get_element_value ($root , "directories", "deploy");
    my @client_repo =  &get_element_value ($root , "directories", "clientrepo");
    $logger->info ("deploy directory value is $value[0]");
    
    my @working = &get_element_value ($root , "directories", "working");

    #my @error;
    foreach (@test)
    {
       my $name = $_->getElementsByTagName("name");
       my $test_name = $name->item(0)->getFirstChild->getNodeValue;

       my $file_name = $_->getElementsByTagName("file");
       my $file = $file_name->item(0)->getFirstChild->getNodeValue;


       $logger->info ("file name is $file");

       my $service = $_->getElementsByTagName("service");
       my $service_name = $service->item(0)->getFirstChild->getNodeValue;

       print "Copy Configuration files...\n";
       system "copy /Y $working[0]\\rampart_config\\$test_name\\client_axis2.xml $client_repo[0]\\axis2.xml";
       system "copy /Y $working[0]\\rampart_config\\$test_name\\services.xml $value[0]\\services\\$service_name";

       #print "copy /Y $working[0]\\rampart_config\\$test_name\\client_axis2.xml $client_repo[0]\\axis2.xml\n";
       my @arg = $_->getElementsByTagName("arg");

       my @arg_values="";
       $all++;
       foreach (@arg)
       {
	  push (@arg_values, $_->getFirstChild->getNodeValue);
       }

       #my $exp_result = $_->getElementsByTagName("expected_result");
       #my $exp_result_val = $exp_result->item(0)->getFirstChild->getNodeValue;

       #if ($exp_result_val eq 'Fault')
       #{
       #    my $fault_msg = $_->getElementsByTagName("fault_msg");
       #    my $fault_msg_val = $fault_msg->item(0)->getFirstChild->getNodeValue;
       #s}
	&run_simple_axis_server();
        sleep 3;

	print "invoking $test_name \n";
	$logger->info ("$all. printed invoking $test_name to screen ");

       #&start_monitor();
       chdir $value[0], or die "unable to change directory $value[0]$! \n";
       chdir "bin/samples" or die "unable to change directory ";
       chdir "rampart/client/sec_echo" or die "unable to change directory ";
       $logger->info ("changed directory to $value[0]\\rampart\\samples\\client");
      $logger->info ("Repeating results may occure for loop ahead \n\n");

       system "$file @arg_values > $working[0]\\rampart_config\\$test_name\\$file.out";
       print "$file @arg_values > $working[0]\\rampart_config\\$test_name\\$file.out\n";

       #&stop_monitor();
       #&file_difference($file);
       &stop_simple_axis_server();
       
       sleep 2;

      chdir "$working[0]/rampart_config/$test_name", or die;
      if(compare("$file.out","$file.expected") == 0)
      {
        $success++;
        print "Test $test_name .... Passed\n";
        $logger->info ("Test $test_name .... Passed\n\n");
      }
      else
      {
	       print "Test $test_name .... Failed\n";
         $logger->info ("Test $test_name .... Failed\n\n");
      }
    }

    $fail = $all - $success;
    print "\n\n==================Summery==============\n\n";
    $logger->info ("==================Summery==============");

    print "Total Tests        : $all\n";
    $logger->info ( "Total Tests        : $all\n");

    print "Successful Invokes : $success\n";
    $logger->info ("Successful Invokes : $success\n");

    print "Failed Invokes     : $fail\n";
    $logger->info ("Failed Invokes     : $fail\n");

    print "\n========================================\n";
    $logger->info ("========================================\n");

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

sub start_apache_server
{
    $logger->info ("Enter into run Apache server");
    my @output;
    my @value = &get_element_value ($root , "directories", "apache_home");
    $logger->info ("element value of apache home directory $value[0]");

    chdir $value[0], or die "unable to change directory $value[0]$! \n";
    chdir "bin", or die "unable to change directory $value[0]$! \n";
    $logger->info ("change into $value[0]/bin directory ");

    if($^O eq 'MSWin32')
    {
      system " start $name[0] -p$port[0]";
      print "apache server started ...\n"
    }
    elsif($^O eq 'linux')
    {
      system "sudo ./httpd -k start";
      print "apache server started ...\n"
    }
    $logger->info ("Apache server execution finished ... ");
}


sub stop_apache_server
{
    $logger->info ("Enter into run Apache server");
    my @output;
    my $killed=0;
    my @value = &get_element_value ($root , "directories", "apache_home");
    $logger->info ("element value of apache home directory $value[0]");

    chdir $value[0], or die "unable to change directory $value[0]$! \n";
    chdir "bin", or die "unable to change directory $value[0]$! \n";
    $logger->info ("change into $value[0]/bin directory ");

    if($^O eq 'MSWin32')
    {
      system " start $name[0] -p$port[0]";
      print "apache server started ...\n"
    }
    elsif($^O eq 'linux')
    {
      
      system "sudo ./httpd -k stop";
      sleep(15);
      #system "sudo ./httpd -k stop";
      system "pwd";
      printf "\n";

      @process = `ps ax`;
      
     my @apache_home = &get_element_value ($root , "directories", "apache_home");
     my @working = &get_element_value ($root , "directories", "working");
     chdir $apache_home[0], or die "unable to change directory $apache_home[0]$! \n";
     chdir 'bin', or die  'unable to change directory';
     print "changed directory ...\n";     
     system "pwd";

     @process = `ps ax`;
     foreach (@process)
     {	
	 next unless /httpd -k start/;
         $killed=1;
     }

    }
  
      if ($killed == 0)
      {
        print "Apache server stoped successfully.\n";
        $logger->info ("Apache server stoped successfully. \n");
      }
      else
      {
        print "Error stoping Apache server.\n";
        $logger->info ("Error stoping Apache server. \n");
      }

        $logger->info ("Apache server execution finished ... ");
}

sub stop1_apache_server
{
  my $killed=0;
  print "killing apache server... ";
  if($^O eq 'MSWin32')
  {
    system "start wmic process where name='axis2_http_server.exe' delete "   ;
  }
  elsif ($^O eq 'linux')
  {
    my @process = `ps ax`;
    foreach (@process)
    {
	     next unless //./httpd -k start/;
	     s#^\s*(\d+).*(\d+)#'./httpd -k start/'#g;
	     $logger->info ("apache_httpd_server pid $_");
	     kill (9, $_) if /\d+/;
    }
    @process = `ps ax`;
    foreach (@process)
    {
             print "$_";
	     next unless //./httpd -k start/;
             $killed=1;
    }
  }
  if ($killed == 0)
  {
    print " killed.\n";
    $logger->info ("Apache server killed successfully. \n");
  }
  else
  {
    print " error!\n";
    $logger->info ("Error killing Apache server. \n");
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

sub invoke_test
{
 if($^O eq 'MSWin32')
 {
    print "Locating test directory \n";

   if($mail)
   {
    $sender->MailFile({to => 'krishantha@wso2.com', subject => '[WSO2 WSF/PHP] sample execution result',msg =>'WSO2 WSF/PHP sample execution result. Please see the attached log file for test reults.',file =>'test_result.log'});
   }

 }
 elsif($^O eq 'linux')
 {
   printf "Locating test directory \n";
   @dist_dir = &get_element_value ($root , "directories", "dist");		
   print @dist_dir;

   chdir @dist_dir[0], or die "unable to change to test directory";
   chdir src, or die "unable to change to test directory";
   chdir tests, or die "unable to change to test directory";
   chdir samples, or die "unable to change to test directory";

   printf "Running sample PHP files \n";

   printf `pwd \n`;
   
   system `pear run-tests > test_result.log`;
   
   $file = './test_result.log';	# Name of the file
   printf $file;
   open(INFO, $file);		# Open the file
   my @lines = <INFO>;		# Read it into an array
   close(INFO);			# Close the file
   print @lines;		# Print the array

   
   if($mail)
   {
    $sender->MailFile({to => 'krishantha@wso2.com', subject => '[WSO2 WSF/PHP] sample execution result',msg =>'WSO2 WSF/PHP sample execution result. Please see the attached log file for test reults.',file =>'test_result.log'});
   }
 }
}
