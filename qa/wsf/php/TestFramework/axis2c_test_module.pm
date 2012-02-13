package axis2c_test_module;

use XML::DOM;
use Log::Log4perl;
use XML::SemanticDiff;
use Mail::Sender;
use Getopt::Long;
use File::Compare;
use File::Copy;


use TestUtilities;

my $util = new TestUtilities();

my $logger = $util->log_init ('wsfc_tests.logconf');

my $my_root = $util->get_document_element('my_wsfc_tests.xml');

my $root = $util->get_document_element('my_wsfc_tests.xml');

sub invoke_clients
{
print "ivoking clients   ........\n";
    
    my @output;
    my $all = 0;
    my $success = 0;
    my $fail = 0;
    my @nodes = $root->getElementsByTagName("tests");

    $logger->info ("got children of \"tests\" by tag name $nodes[0]");
    my @test = $nodes[0]->getElementsByTagName("test");
    $logger->info ("got child nodes ");

    my @value = $util->get_element_value ($root , "directories", "deploy");
    my @working = $util->get_element_value ($root , "directories", "working");
    $logger->info ("deploy directory value is $value[0]");

    chdir $value[0], or die "unable to change directory $value[0]$! \n";
    chdir "bin" or die "unable to change director ";
    chdir "samples" or die "unable to change director ";
    $logger->info ("changed directory to $value[0]\bin\samples ");
    $logger->info ("Repeating results may occure for loop ahead \n\n");
   
    foreach (@test)
    {
       my $name = $_->getElementsByTagName("name");
           my $TestName = $name->item(0)->getFirstChild->getNodeValue;

             my $filename = $_->getElementsByTagName("file");
         my $file = $filename->item(0)->getFirstChild->getNodeValue;
             $logger->info ("file name is $file");

       my @arg = $_->getElementsByTagName("arg");

             my @arg_values="";
             foreach (@arg)
             {
                push (@arg_values, $_->getFirstChild->getNodeValue);
   	     }

	 print "invoking $TestName \n";
     $logger->info ("$all. printed invoking $TestName to screen ");

    chdir $value[0], or die "unable to change directory $value[0]$! \n";
    chdir "bin" or die "unable to change director ";
    chdir "samples" or die "unable to change director ";
    $logger->info ("changed directory to $value[0]\bin\samples ");
    $logger->info ("Repeating results may occure for loop ahead \n\n");

       if($^O eq 'MSWin32')
       {
            system "$file @arg_values > $working[0]\\output\\$TestName.out";
       }
       elsif($^O eq 'linux')
       {
               system "./$file @arg_values > $working[0]/output/$TestName.out" }
       sleep 3;


       if($^O eq 'MSWin32')
       {
	
	 }
       elsif($^O eq 'linux')
       { }
       $all++;
       chdir "$working[0]/output", or die;
       if(compare("$TestName.out","$TestName.expected") == 0)
       {
               $success++;
               print "Test $TestName .... Passed\n";
        $logger->info ("Test $TestName .... Passed\n\n");
       }
       else
       {
              print "Test $TestName .... Failed\n";
         $logger->info ("Test $TestName .... Failed\n\n");
       }

     }

}

sub new {
    my ($self) = {};
    bless ($self);
    $self->{'_created'} = 1;
    return $self;
}




1;
