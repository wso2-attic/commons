package xml2input;
$version='v0.1';

#this module can use to create autobench competible messages using normal soap/http messages.

use v5.10.0;
use warnings;
use strict;
use Carp;

sub new{

	my $this=shift;
	my $self = {};
	bless ($self,$this);
	return $self;

}

#-------------------------------------------------------
#convert($file,$scenario,$data)
#add service, method(GET/POST) and set the message as the content
sub convert{
	#do the convertion
	my $name = shift;
	my $file = $_[0];
	my $scenario = $_[1];
	my $data = $_[2];
	my $uri = $data->{'product1'}->{'server-config'}->{'transport'}->{'http'}->{'uri'};
	my $method = $data->{'scenario'}->{'method'};
    my $max_piped_calls = $data->{'scenario'}->{'max_piped_calls'};
	open(FILE,"scenario/$scenario/$file");
	my $message = "$uri method=$method contents=\"";
	#change the message as inputfile need it
	while(<FILE>){
		$_ =~ s/\s+$//;	
		$_ =~ s/^\s+//;
		$_ =~ s/"/\\"/g;
		$message=$message.$_;	
	}
	close(FILE);
	$message=$message."\"";
	open (INPUTFILE, ">scenario/$scenario/inputfile");
    for(my $i=0;$i<$max_piped_calls;$i++){
	    print INPUTFILE "$message\n";
    }
	close(INPUTFILE);	
}
