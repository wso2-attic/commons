package AddTSV;
$version='v0.1';

#combine two TSV files with the same name in different scenario folders

use v5.10.0;
use warnings;
use strict;

my ($scenario1, $scenario2);
sub new{

	my $this=shift;
	($scenario1, $scenario2) = @_;
	my $self = {};
	bless ($self,$this);
	return $self;

}

#--------------------------------------------------
#add($file)
#add two tsv files
sub add{

	my ($name, $file) = @_;
	
	my ($row_1,$row_2);
	open(TSV1,"scenario/$scenario1/results/$file") or die "Cannot open $file";
	open(TSV2,"scenario/$scenario2/results/$file") or die "Cannot open $file";

	open(TSV_NEW,">compare_scenarios/$scenario1\_$scenario2/$file") or die "Cannot open the file";

	my $i = 0;
	my %data;
	#read all the rows in tsv file and save in a hash
	while ($row_1 = <TSV1>){
		$row_2 = <TSV2>;
		if(!defined($row_2)){
			die "Number of rows in files is not equal";
		}
		my @values = ([ parse_tsv($row_1)], [parse_tsv($row_2)]);
		my $i = 0;
		while(defined($values[0]->[$i])){
			chomp($values[0]->[$i]);
			print TSV_NEW "$values[0]->[$i]\t";
			$i++;
		}
		
		$i = 0;
		while(defined($values[1]->[$i])){
			if($i!=0){
				chomp($values[1]->[$i]);
				print TSV_NEW "$values[1]->[$i]\t";
			}
			$i++;
		}	
		print TSV_NEW "\n";	
	}
	if(<TSV2>){
		die "Number of rows in files is not equal";
	}
	
	close(TSV_NEW);
	close(TSV1);
	close(TSV2);

}

#-------------------------------------------------------------------
#parse_tsv($row)
#seperate values of a row in tsv file
sub parse_tsv{
	my $row = shift;
	return split('\t',$row);
}
1;
