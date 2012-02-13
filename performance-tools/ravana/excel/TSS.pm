package TSS;
$version='v0.1';

#draw charts in a excel sheets

use v5.10.0;
use warnings;
use strict;
use Carp;

use Spreadsheet::WriteExcel;
use Data::Dumper;

my $self;
my $n=5;


sub new{

	my $this=shift;
	$self = {};
	bless ($self,$this);
	return $self;

}

sub add_workbook{
	
	my $workbook_name = $_[1];
	my $workbook = Spreadsheet::WriteExcel->new($workbook_name);
	return $workbook;

}

sub add_chart{
	my $class_name = shift;
	my ($workbook,$type,$name,$x,$y,$data) = @_;

	my $worksheet = $workbook->add_worksheet($name); 
	my $format = $workbook->add_format(
					size=>13,
				);
	my $format1 = $workbook->add_format(border => 2, align=>'center');
	
	#heading
	my $s=$n-4;
	$worksheet->write("C$s","Performance Test-$name",$format);
	
	my $l=$n-1;
	my $m=$n-2;

	#draw a table according to the data
	my $headings = [$x,$y];
	$worksheet->write("A$m",$headings,$format1);
	$worksheet->write("A$n",$data,$format1);

	#create the chart
	my $chart = $workbook->add_chart(
		type=>$type,
		name=>$name,		
		embedded=>1,
	);
	
	#set the chart properties
	$chart->set_x_axis(name=>$x);
	$chart->set_y_axis(name=>$y);

	my $alp = ['A','B','C','D','E','F','G','H','I','J'];
	my $i=1;
	while($data->[$i]){
		my $j = $alp->[$i];
		$chart->add_series(
			name => "$name",
			catagories => "=$name!\$A\$$n:\$A\$24",
			values => "=$name!\$$j\$$n:\$$j\$24",
		);
		$i++;
	}
	$i++;
	#insert the values to the chart
	$worksheet->insert_chart("E$i",$chart);
	
}	
1;
