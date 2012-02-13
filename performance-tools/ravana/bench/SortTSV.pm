package SortTSV;
$version='v0.1';

#Sort a tsv file

use v5.10.0;
use warnings;
use strict;

use File::Copy;

sub new{

	my $this=shift;
	my $self = {};
	bless ($self,$this);
	return $self;

}

#---------------------------------------------------------------------
#sort($file)
sub sort{
	#sort tsv file
	my ($name,$file,$h1,$h2)=@_;
	my $row;
	open(TSV,"$file") or die "Cannot open the file\n";
	open(TSV_NEW,">/tmp/temp.tsv") or die "Cannot open the file\n";

	my $i = 0;
	my %data;
	#read all the rows in tsv file and save in a hash
	while ($row = <TSV>){
		if($i == 0){
            my $key = parse_tsv_key($row);
            if($h2){
                print TSV_NEW "$key\t$h1\t$h2\n";
            }elsif($h1){
                print TSV_NEW "$key\t$h1\n";
            }else{
			    print TSV_NEW $row;
            }
		} else{
			my $key = parse_tsv_key($row);
			$data{$key} = $row;
		}
		$i++;
	}
	my $key;
	#sort and save in a tempory file
	foreach $key (sort {$a <=> $b} keys %data) {
   		print TSV_NEW "$data{$key}";
	}
	close(TSV);
	close(TSV_NEW);
	move("/tmp/temp.tsv","$file");
	
}

#-------------------------------------------------------------------
#parse_tsv_key($row)
#parse the key value of sorting
sub parse_tsv_key{
	#parse a key value for a row (the value in the first column)
	my $row = shift;
	my @value = split('\t',$row);
	return $value[0];
}
