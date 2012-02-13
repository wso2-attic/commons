drop database if exists customer_db;

create database customer_db;
use customer_db;
create table customer(id varchar(10), name varchar(100), address varchar(200));
insert into customer values('1', 'smith', 'colombia');
insert into customer values('2', 'mark', 'boston');


