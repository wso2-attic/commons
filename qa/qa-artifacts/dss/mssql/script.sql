create table customer(
customerId integer,
customerName varchar(20),
phone integer)


insert into customer values(1,'saman',0112345234)
insert into customer values(2,'upul',0112345234)

create procedure getCustomers
@customerId integer 
as
select customerName, phone 
from customer
where customerId = @customerId



