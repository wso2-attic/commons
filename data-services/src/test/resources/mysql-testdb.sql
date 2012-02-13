create database projects;
grant all on projects.* to 'datauser'@'localhost' identified by 'wso2';
grant all on projects.* to 'datauser'@'%' identified by 'wso2';

use  projects;
create table student(id int,name varchar(100),deptNo int,address varchar(100),email varchar(100));
create table department(id int,name varchar(100));

insert into department values(1,'Electrical');
insert into department values(2,'Computer Science');
insert into department values(3,'Electronics');

insert into student values(1,'chamil',2,'Colombo 08','chamil@abc.com');
insert into student values(2,'sumedha',2,'Moratuwa','sumedha@abc.com');
insert into student values(3,'amal',3,'Gampaha','amal@abc.com');
insert into student values(4,'deep',1,'Nugegoda','deep@abc.com');
