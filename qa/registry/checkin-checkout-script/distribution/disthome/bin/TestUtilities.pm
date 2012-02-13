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

