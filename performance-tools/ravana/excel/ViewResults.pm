package ViewResults;
$version='v0.1';

use v5.10.0;
use warnings;
use strict;
use Text::CSV;

use TSS;

sub new{

	my $this = shift; 
	my $self = {};
	bless $self, $this;
	return $self;

}

sub view{

	my $module_name = shift;
	my $product = $_[0];
	my @data;
	my @rr;
	my @rt;

	my $file = "$product-results.csv";
	my $csv = Text::CSV->new();
	open (CSV, "<", $file);
	my $i = 0;
	while (<CSV>) {
		if($i!=0){
			if ($csv->parse($_)) {
				my @columns = $csv->fields();
				#print "@columns\n";
				push (@rr,$columns[0]);		
				push (@rt,$columns[7]);
			} else {
				my $err = $csv->error_input;
				print "Failed to parse line: $err";
		    }
		}
		$i++;
	}
	close CSV;

	push (@data,[@rr]);
	push (@data,[@rt]);

	my $tss = TSS->new();
	my $workbook = $tss->add_workbook("$product-results.xls");
	my $type = 'column';
	my $name = 'request rate vs response time';
	my $x = 'request rate';
	my $y = 'response time';
	$tss->add_chart($workbook,$type,$name,$x,$y,\@data);

}
