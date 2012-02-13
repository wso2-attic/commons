#!/usr/bin/perl -w
package TestUtilities;

use XML::DOM;
use Log::Log4perl;
use XML::SemanticDiff;

sub new {
    my ($self) = {};
    bless ($self);
    $self->{'_created'} = 1;
    return $self;
}

sub msgcomp
{
  my $result = 0;
  my $diff = XML::SemanticDiff->new(keeplinenums => 1);
  foreach my $change ($diff->compare(@_[1], @_[2])) {
          $result++;
  }
  return $result;
}


sub capture_massage
{
    my $test_name = @_[1];

    my @working = @_[2];
    my $expected_req = "$working[0]/msg/$test_name.reqest";
    my @deploy = @_[3];
    if($^O eq 'MSWin32'){
      my $actual_req_file = "@deploy[0]\\bin\\reqest";
      my $copy_req_file = "$working[0]\\msg\\$test_name.request.xml";
      system "move /Y $actual_req_file $copy_req_file";
      #&make_xml($actual_req_file,$copy_req_file);

      my $actual_res_file =  "@deploy[0]\\bin\\response";
      my $copy_res_file = "$working[0]\\msg\\$test_name.response.xml";
      system "move /Y $actual_res_file $copy_res_file";
      #&make_xml($actual_res_file,$copy_res_file);

    }
    elsif($^O eq 'linux')
    {
      my $actual_req = "@deploy[0]/bin/tools/reqest";
      my $copy_req_file = "$working[0]/msg/$test_name.reqest.xml";
      system "mv $actual_req_file $copy_req_file";
      #&make_xml($actual_req_file,$copy_req_file);

      my $actual_res =  "@deploy[0]/bin/tools/response";
      my $copy_res_file = "$working[0]/msg/$test_name.response.xml";
      system "mv $actual_res_file $copy_res_file";
      #&make_xml($actual_res_file,$copy_res_file);
    }
}


sub start_monitor
{
  my @value = $_[1];
  chdir $value[0], or die "unable to change directory $value[0]$! \n";
  chdir "bin", or die "unable to change directory $value[0]$! \n";

  print "Start Monitor \n";

  my $listen_port = $_[2];
  my $test_port = $_[3];

  if($^O eq 'MSWin32'){
    system "start tcpmon.exe -lp $listen_port -tp $test_port -th localhost --test ";
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

sub run_simple_axis_server
{
    my $logger = $_[4];
    $logger->info ("Enter into run simple axis server");
    my @output;
    my @deploy = $_[1];
    print " Despley $deploy[0]";
    $logger->info ("element value of deploy directory $deploy[0]");

    chdir $deploy[0], or die "unable to change directory $deploy[0]$! \n";
    chdir "bin", or die "unable to change directory $deploy[0]$! \n";
    $logger->info ("change into $deploy[0]/bin directory ");

    my @name = $_[2];
    my @port = $_[3];
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
  my $logger = $_[1];
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

sub log_init
{
    my $tests_logconf = $_[1];
    Log::Log4perl::init_and_watch( $tests_logconf, 5 );
    my $logger = Log::Log4perl::get_logger('main');
}

sub get_element_value
{
    my @output;
    
    my @directories = $_[1]->getElementsByTagName($_[2]);
    foreach (@directories)
    {
	     my $directory = $_->getElementsByTagName ($_[3]);
	     push (@output, $directory->item(0)->getFirstChild->getNodeValue);
    }
    return @output;
}

sub get_document_element
{
    my $file = $_[1];
    my $parser = XML::DOM::Parser->new();
    my $root = $parser->parsefile($file);
}

1;



