#! /usr/bin/env perl

#run the deamon client in server machies

use v5.10.0;
use warnings;
use strict;

use XML::Simple;
use xml2input;

#start esb
#system("sh","$product_path/$product/bin/wso2server.sh","--start");
#wait for the server starts
#sleep(20);

#create objects
my $xml = XML::Simple->new;
my $xml2input = xml2input->new;

#read XMl file
my $data = $xml->XMLin("conf/config.xml");	
my $scenario = $data->{'scenario'}->{'name'};

opendir(DIR,"scenario/$scenario");
my @file = readdir(DIR);

my $file;

#convert xml(messages) file to autobench compatible mode
#if more than one files in scenario folder then deamon will not be started.
my $i = 0;
foreach $file(@file){
	if($i>0){
		print STDERR "Warning : More than one messages in the scenario folder\n";
	}
	if($file =~  /\.xml$/i){		
		$xml2input->convert($file,$scenario,$data);	
	}
	$i++;		
}

system("autobench/autobenchd_httperf");

#my $viewresults =ViewResults->new;
#$viewresults->view($product);

#stop esb
#system("sh","$product_path/$product/bin/wso2server.sh","--stop");
#sleep(10);
	
close(DIR);
1;
