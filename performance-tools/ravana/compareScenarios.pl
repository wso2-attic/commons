#! /usr/bin/env perl

#get results from two scenario folders and create a one result file which include a comparisson
#between those two results 
#need to input two scenario names which need to be compared

#@param scenario1:scenario1_name
#@param scenario2:scenario2_name


use v5.10.0;
use warnings;
use strict;

use lib "./bench";
use AddTSV;
use GenerateGraphs;
use Getopt::Long;

my ($scenario1, $scenario2);
my $help = "perl compareScenarios.pl\n\t[--scenario1 <first_scenario> --scenario2 <second_scenario>]\n\t[--help]\n";

GetOptions ('scenario1:s' => \$scenario1, 'scenario2:s' => \$scenario2, 'help:s' => '');

if(!(defined($scenario1) && defined($scenario2))){
	die "$help";
} 

my $add_tsv = AddTSV->new($scenario1,$scenario2);
my $graph_gen = GenerateGraphs->new;

#end the comparing if two scenarios are not given as parameters  
opendir(DIR1,"scenario/$scenario1/results") or die "Scenario $scenario1 does not exists\n";
opendir(DIR2,"scenario/$scenario2/results") or die "Scenario $scenario2 does not exists\n";
my @file1 = readdir(DIR1);
my @file2 = readdir(DIR2);

mkdir("compare_scenarios/$scenario1\_$scenario2");

my ($file1, $file2);

#do the comparisson for all the files
foreach $file1(@file1){
	if($file1 =~  /\.tsv$/i){
		foreach $file2(@file2){
			if($file1 eq $file2){
				#create a tsv for the $file which include results of both scenarios
				$add_tsv->add($file1);	
				if($file1 eq 'transactions_per_second_vs_message_size.tsv'){
					#genarate histogram
					$graph_gen->plot_ab_histogram($scenario1, $scenario2, "compare_scenarios/$scenario1\_$scenario2", "$file1", "true","false");
				} else {
					#genarate graphs
					$graph_gen->plot_ab($scenario1, $scenario2, "compare_scenarios/$scenario1\_$scenario2", "$file1");
				}
			}
		}	
	}
}


