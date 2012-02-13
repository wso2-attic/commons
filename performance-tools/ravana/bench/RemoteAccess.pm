package RemoteAccess;
$version='v0.1';

#copy a script in the remote server and execute it
#monitor servers CPU and Memory using top
#before using this need to store client key in the server machine. 
#use the following command to do that
#ssh-keygen -t rsa
#cat .ssh/id_rsa.pub | ssh user@remote.host 'cat >>.ssh/authorized_keys'

use v5.10.0;
use warnings;
use strict;

sub new{

	my $this=shift;
	my $self = {};
	bless ($self,$this);
	return $self;

}

#create objects
my $xml = XML::Simple->new;
my $xml2input = xml2input->new;

#read XMl file
my $data = $xml->XMLin("conf/config.xml");

#configure
my $product1 = $data->{"product1"}->{"product_name"};
my $user1 = $data->{"product1"}->{"username"};
my $product_path1 = $data->{"product1"}->{"product_path"};
my $host1 = $data->{"product1"}->{"server-config"}->{"host"};

my $product2 = $data->{"product2"}->{"product_name"};
my $product_path2 = $data->{"product2"}->{"product_path"};	
my $user2 = $data->{"product2"}->{"username"};
my $host2 = $data->{"product2"}->{"server-config"}->{"host"};

my $scenario = $data->{'scenario'}->{'name'};

my $command;

#-------------------------------------------------------------------------------------------
#start
sub start{
	#cpoy the server script
	system("scp scenario/$scenario/server-script.sh $user1"."@"."$host1:$product_path1/$product1");
	#start the server
	$command = "ssh $user1"."@"."$host1 'cd $product_path1/$product1;sh server-script.sh start'";
	system($command);
	
	if($product2){
		system("scp server-script.sh $user2"."@"."$host2:$product_path2/$product2");
		$command = "ssh $user2"."@"."$host2 'cd $product_path2/$product2;sh server-script.sh start'";
		system($command);	
	}
}

#---------------------------------------------------------------------------------------------
#stop
sub stop{
	#stop the server
	$command = "ssh $user1"."@"."$host1 'cd $product_path1/$product1;sh server-script.sh stop'";
	system($command);
	#remove server script
	$command = "ssh $user1"."@"."$host1 'cd $product_path1/$product1;rm server-script.sh'";
	system($command);
	#get the top.txt which includes the server CPU and Memory usage details
	system("scp $user1"."@"."$host1:$product_path1/$product1/cpu.txt scenario/$scenario/results");
	$command = "ssh $user1"."@"."$host1 'cd $product_path1/$product1;rm cpu.txt'";
	system($command);

	if($product2){

		$command = "ssh $user2"."@"."$host2 'cd $product_path2/$product2;sh server-script.sh stop'";
		system($command);
		$command = "ssh $user2"."@"."$host2 'cd $product_path2/$product2;rm server-script.sh'";
		system($command);
		system("scp $user2"."@"."$host2:$product_path2/$product2/cpu.txt scenario/$scenario/results");
		$command = "ssh $user2"."@"."$host2 'cd $product_path2/$product2;rm cpu.txt'";
		system($command);

	}	

}
1;
