package GenerateGraphs;
$version='v0.1';

#draw graphs for the results 
#this call bench2graph script to do the plotting

use v5.10.0;
use warnings;
use strict;

use SortTSV;

sub new{

	my $this=shift;
	my $self = {};
	bless ($self,$this);
	return $self;

}

#----------------------------------------------------------
#plot($product1, $product2, $path, $tsv, $file)
#plot the graphs for httperf
sub plot{
	
	my ($name, $product1, $product2, $path, $tsv, $file) = @_;
	
	if($file){
		#plot the graphs if message xml files inluded in scenario folder
		if($product2){
			
			system("bench/bench2graph '$product1/$product2 - Response Rates' $path/$tsv $path/$file-response_rates.ps 'httperf' 1 5 14");
			system("bench/bench2graph '$product1/$product2 - Response Times' $path/$tsv $path/$file-response_times.ps 'httperf' 1 8 17");
			system("bench/bench2graph '$product1/$product2 - Net I/O Rate' $path/$tsv $path/$file-net_io.ps 'httperf' 1 9 18");
			system("bench/bench2graph '$product1/$product2 - Errors' $path/$tsv $path/$file-errors.ps 'httperf' 1 10 19");			
			
		} else {
			#single host
			system("bench/bench2graph '$product1 - Response Rates' $path/$tsv $path/$file-response_rates.ps 'httperf' 1 5");
			system("bench/bench2graph '$product1 - Response Times' $path/$tsv $path/$file-response_times.ps 'httperf' 1 8");
			system("bench/bench2graph '$product1 - Net I/O Rate' $path/$tsv $path/$file-net_io.ps 'httperf' 1 9");
			system("bench/bench2graph '$product1 - Errors' $path/$tsv $path/$file-errors.ps 'httperf' 1 10");		
			
		}

	} else {
		#plot the graphs if no message xml files are in scenario folder
		if($product2){

			system("bench/bench2graph '$product1/$product2 - Response Rates' $path/$tsv $path/response_rates.ps 'httperf' 1 5 14");
			system("bench/bench2graph '$product1/$product2 - Response Times' $path/$tsv $path/response_times.ps 'httperf' 1 8 17");
			system("bench/bench2graph '$product1/$product2 - Net I/O Rate' $path/$tsv $path/net_io.ps 'httperf' 1 9 18");
			system("bench/bench2graph '$product1/$product2 - Errors' $path/$tsv $path/errors.ps 'httperf' 1 10 19");			
			
		} else {
			#single host
			system("bench/bench2graph '$product1 - Response Rates' $path/$tsv $path/response_rates.ps 'httperf' 1 5");
			system("bench/bench2graph '$product1 - Response Times' $path/$tsv $path/response_times.ps 'httperf' 1 8");
			system("bench/bench2graph '$product1 - Net I/O Rate' $path/$tsv $path/net_io.ps 'httperf' 1 9");
			system("bench/bench2graph '$product1 - Errors' $path/$tsv $path/errors.ps 'httperf' 1 10");
	
		}
	}

}

#plot graphs for apache bench
sub plot_ab{
	
	my ($name, $product1, $product2, $path, $tsv, $file) = @_;
	
	if($file){
		#plot the graphs if message xml files inluded in scenario folder
		if($product2){
			
			system("bench/bench2graph '$product1/$product2 - Transactions per Second' $path/$tsv $path/$file-trans_ps.ps 'ab' 1 2 5");
			system("bench/bench2graph '$product1/$product2 - Average Time per Request' $path/$tsv $path/$file-time_pr.ps 'ab' 1 3 6");
			system("bench/bench2graph '$product1/$product2 - Errors' $path/$tsv $path/$file-errors.ps 'ab' 1 4 7");			
			
		} else 
        {
			#single host
			system("bench/bench2graph '$product1 - Transactions_per_Second' $path/$tsv $path/$file-trans_ps.ps 'ab' 1 2");
			system("bench/bench2graph '$product1 - Average_Time_per_Request' $path/$tsv $path/$file-time_pr.ps 'ab' 1 3");
			system("bench/bench2graph '$product1 - Errors' $path/$tsv $path/$file-errors.ps 'ab' 1 4");		
			
		}

	} else {
		#plot the graphs if no message xml files are in scenario folder
		if($product2){

			system("bench/bench2graph '$product1/$product2 - Transactions per Second' $path/$tsv $path/trans_ps.ps 'ab' 1 2 5");
			system("bench/bench2graph '$product1/$product2 - Average Time per Request' $path/$tsv $path/time_pr.ps 'ab' 1 3 6");
			system("bench/bench2graph '$product1/$product2 - Errors' $path/$tsv $path/errors.ps 'ab' 1 4 7");			
			
		} else {
			#single host
			system("bench/bench2graph '$product1 - Transactions per Second' $path/$tsv $path/trans_ps.ps 'ab' 1 2");
			system("bench/bench2graph '$product1 - Average Time per Request' $path/$tsv $path/time_pr.ps 'ab' 1 3");
			system("bench/bench2graph '$product1 - Errors' $path/$tsv $path/errors.ps 'ab' 1 4");
	
		}
	}

}

#----------------------------------------------------------
#plot($product1, $product2, $path, $tsv,$sort_tsv)
#plot histograms
sub plot_histogram{

	my($name, $product1, $product2, $path, $tsv,$sort_tsv,$change_header) = @_;

	if($sort_tsv eq 'true'){
		print "Sorting results\n";
		my $sort = SortTSV->new;
		#WARNING:if there is duplicate rows then they will be eliminate to one
		if($change_header eq 'true'){
            $sort->sort("$path/Demand_Rate_at_Maximum_Response_Rate_vs_Message_Size.tsv", $product1, $product2);
	    }else{
            $sort->sort("$path/Demand_Rate_at_Maximum_Response_Rate_vs_Message_Size.tsv");
        }
    }

	#plot histogram
	if (defined($product1) && defined($product2)){
        	
		system("bench/bench2graph 'Demand_Rate_at_Maximum_Response_Rate_vs_Message_Size' '$path/$tsv' '$path/Demand_Rate_at_Maximum_Response_Rate_vs_Message_Size.ps' 'httperf-histo' 1 2 3");

	}elsif(defined($product1)) {

		system("bench/bench2graph 'Demand_Rate_at_Maximum_Response_Rate_vs_Message_Size' '$path/$tsv' '$path/Demand_Rate_at_Maximum_Response_Rate_vs_Message_Size.ps' 'httperf-histo' 1 2");

	}

}

sub plot_jms_histogram{

	my($name, $product1, $product2, $path, $tsv,$sort_tsv,$change_header) = @_;

	if($sort_tsv eq 'true'){
		print "Sorting results\n";
		my $sort = SortTSV->new;
		#WARNING:if there is duplicate rows then they will be eliminate to one
		if($change_header eq 'true'){
            $sort->sort("$path/transactions_per_second_vs_message_size.tsv", $product1, $product2);
	    }else{
            $sort->sort("$path/transactions_per_second_vs_message_size.tsv");
        }
    }

	#plot histogram
	if (defined($product1) && defined($product2)){
        	
		system("bench/bench2graph 'transactions_per_second_vs_message_size' '$path/$tsv' '$path/transactions_per_second_vs_message_size.ps' 'jms-histo' 1 2 3");

	}elsif(defined($product1)) {

		system("bench/bench2graph 'transactions_per_second_vs_message_size' '$path/$tsv' '$path/transactions_per_second_vs_message_size.ps' 'jms-histo' 1 2");

	}

}

sub plot_ab_histogram{

	my($name, $product1, $product2, $path, $tsv,$sort_tsv,$change_header) = @_;

	if($sort_tsv eq 'true'){
		print "Sorting results\n";
		my $sort = SortTSV->new;
		#WARNING:if there is duplicate rows then they will be eliminate to one
		if($change_header eq 'true'){
            $sort->sort("$path/transactions_per_second_vs_message_size.tsv", $product1, $product2);
	    }else{
            $sort->sort("$path/transactions_per_second_vs_message_size.tsv");
        }
    }

	#plot histogram
	if (defined($product1) && defined($product2)){
        	
		system("bench/bench2graph 'transactions_per_second_vs_message_size' '$path/$tsv' '$path/transactions_per_second_vs_message_size.ps' 'ab-histo' 1 2 3");

	}elsif(defined($product1)) {

		system("bench/bench2graph 'transactions_per_second_vs_message_size' '$path/$tsv' '$path/transactions_per_second_vs_message_size.ps' 'ab-histo' 1 2");

	}

}

sub plot_cpu_stats{
	my ($name, $path) = @_;
    if (-e "$path/cpu.txt"){
	    system("java -jar kSar/kSar.jar -input $path/cpu.txt -outputPDF $path/cpu-stats.pdf");
    }
}
