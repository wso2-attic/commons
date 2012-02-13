#! /usr/bin/env perl

# This is free software; you can redistribute it and/or modify it
# under the terms of the GNU Lesser General Public License as
# published by the Free Software Foundation; either version 2.1 of
# the License, or (at your option) any later version.
# 
# This software is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
# Lesser General Public License for more details.
# 
# You should have received a copy of the GNU Lesser General Public
# License along with this software; if not, write to the Free
# Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
# 02110-1301 USA, or see the FSF site: http://www.fsf.org.

#main script in the Ravana
#run remote server(optional)
#run Ravana (do the performance testing)

use v5.10.0;
use warnings;
use strict;

use XML::Simple;
use Getopt::Std;
use File::Copy;
use lib "./bench";
use xml2input;
use RemoteAccess;
use SortTSV;
use GenerateGraphs;

my $mode = 'single';
my $remote = 'false';
my $tool = 'java-ab';

#create objects
my $xml = XML::Simple->new;
my $xml2input = xml2input->new;

#read XMl file
my $data = $xml->XMLin("conf/config.xml");

#configure
my $product1 = $data->{"product1"}->{"product_name"};
my $host1 = $data->{'product1'}->{'server-config'}->{'transport'}->{'http'}->{'host'};
my $port1 = $data->{'product1'}->{'server-config'}->{'transport'}->{'http'}->{'port'};
my $uri1 = $data->{'product1'}->{'server-config'}->{'transport'}->{'http'}->{'uri'};

my $product2 = $data->{"product2"}->{"product_name"};
my $host2 = $data->{'product2'}->{'server-config'}->{'transport'}->{'http'}->{'host'};
	
my $scenario = $data->{'scenario'}->{'name'};
my $sort = $data->{'extra'}->{'sort'};

#Currently supported tools are java bench(java-ab), apache bench, httperf and jms
$tool = $data->{'extra'}->{'tool'};

#mode can be one of 
#cluster - Use multiple machines in a clustered environment to generate load. 
#           This is currently supported only for httperf tool
#single- Use a single machine to generate load
$mode = $data->{'extra'}->{'mode'};

#Whether the server for which the performance/load is tested should be controlloed remotely
$remote = $data->{'extra'}->{'remote'};

#check minimum data availability in the config.xml to do the test
if($tool ne 'jms')
{
    if(!(defined($host1)&&defined($port1)&&defined($uri1)))
    {
        die "Basic server details not available for the test: Make sure you set the server details in config.xml";
    }
}
opendir(DIR,"scenario/$scenario");
my @file = readdir(DIR);

my $file;

#In the scenario input folder if there are xml messages, then indicator will be changed to true
my $indicator = 'false';

#results
mkdir("scenario/$scenario/results");

#start remote server
my $remote_server = RemoteAccess->new;
if($remote eq 'true'){
	$remote_server->start;
	sleep(20);
}

my $graph_gen = GenerateGraphs->new;

#do the test for all available xml messages in the given scenario folder
foreach $file(@file){
	if($file =~  /\.xml$/i){	
        if($mode eq 'cluster')
        {
            if($tool eq 'httperf')
            {
                $xml2input->convert($file,$scenario,$data);
                system("bench/bench_httperf_admin >scenario/$scenario/results/output.txt");
            }
            elsif($tool eq 'jms')
            {
                #TODO
            }
            elsif($tool eq 'java-ab')
            {
                #TODO
            }
            elsif($tool eq 'ab')
            {
                #TODO
            }
        }
        else
        {
            if($tool eq 'httperf')
            {
                $xml2input->convert($file,$scenario,$data);
                system("bench/bench_httperf >scenario/$scenario/results/output.txt");	
            }
            elsif($tool eq 'jms')
            {
                system("cp scenario/$scenario/$file scenario/$scenario/inputfile");
                system("bench/bench_jms >scenario/$scenario/results/output.txt");
            }
            elsif($tool eq 'java-ab')
            {
                system("cp scenario/$scenario/$file scenario/$scenario/inputfile");
                system("bench/bench_java-ab >scenario/$scenario/results/output.txt");
            }
            elsif($tool eq 'ab')
            {
                system("cp scenario/$scenario/$file scenario/$scenario/inputfile");
                system("bench/bench_ab >scenario/$scenario/results/output.txt");
            }
        }
	
        if($tool eq 'httperf'){
            $file =~ s/.xml//;
            $graph_gen->plot($product1, $product2, "scenario/$scenario/results", 'results.tsv', $file);
            move("scenario/$scenario/results/results.tsv","scenario/$scenario/results/$file-results.tsv");	
        }
        elsif($tool eq 'java-ab')
        {
            $file =~ s/.xml//;
            $graph_gen->plot_ab($product1, $product2, "scenario/$scenario/results", 'results.tsv', $file);
            move("scenario/$scenario/results/results.tsv","scenario/$scenario/results/$file-results.tsv");
        }
        elsif($tool eq 'ab')
        {
            $file =~ s/.xml//;
            $graph_gen->plot_ab($product1, $product2, "scenario/$scenario/results", 'results.tsv', $file);
            move("scenario/$scenario/results/results.tsv","scenario/$scenario/results/$file-results.tsv");
        }
        elsif($tool eq 'jms')
        {
            #TODO
        }
		$indicator = 'true';	
		
	}
}

#do the test using inputfile if xml files not available in scenario folder
if($indicator eq 'false')
{
	#see whether there is an input file
	if(! -e "scenario/$scenario/inputfile")
    {
		die "No input file found in $scenario folder\n";
	}

	if($mode eq 'cluster')
    {
        if($tool eq 'httperf')
        {
            system("bench/bench_httperf_admin >>scenario/$scenario/results/output.txt");
        }
        elsif($tool eq 'jms')
        {
            #TODO
        }
        elsif($tool eq 'java-ab')
        {
            #TODO
        }
        elsif($tool eq 'ab')
        {
            #TODO
        }
    }
    else
    {
        if($tool eq 'httperf')
        {
            system("bench/bench_httperf >>scenario/$scenario/results/output.txt");
        }
        elsif($tool eq 'jms')
        {
            system("bench/bench_jms >scenario/$scenario/results/output.txt");
        }
        elsif($tool eq 'java-ab')
        {
            system("bench/bench_java-ab >scenario/$scenario/results/output.txt");
        }
        elsif($tool eq 'ab')
        {
            system("bench/bench_ab >scenario/$scenario/results/output.txt");
        }
    }

    if($tool eq 'httperf')
    {
	    $graph_gen->plot($product1, $product2, "scenario/$scenario/results", 'results.tsv');
    }
    elsif($tool eq 'jms')
    {
        #TODO
    }
    elsif($tool eq 'java-ab')
    {
	    $graph_gen->plot_ab($product1, $product2, "scenario/$scenario/results", 'results.tsv');
    }
    elsif($tool eq 'ab')
    {
	    $graph_gen->plot_ab($product1, $product2, "scenario/$scenario/results", 'results.tsv');
    }
}
if(!$sort){
	$sort = 'true';
}

if($remote eq 'true'){
	$remote_server->stop;
}

if($tool eq 'httperf')
{
    $graph_gen->plot_histogram($product1, $product2, "scenario/$scenario/results", 'Demand_Rate_at_Maximum_Response_Rate_vs_Message_Size.tsv',$sort,'false');
    $graph_gen->plot_cpu_stats("scenario/$scenario/results");
}
elsif($tool eq 'java-ab')
{
    $graph_gen->plot_ab_histogram($product1, $product2, "scenario/$scenario/results", 'transactions_per_second_vs_message_size.tsv',$sort,'false');
}
elsif($tool eq 'ab')
{
    $graph_gen->plot_ab_histogram($product1, $product2, "scenario/$scenario/results", 'transactions_per_second_vs_message_size.tsv',$sort,'false');
}
elsif($tool eq 'jms')
{
    $graph_gen->plot_jms_histogram($product1, $product2, "scenario/$scenario/results", 'transactions_per_second_vs_message_size.tsv',$sort,'false');
}

close(DIR);	

1;
