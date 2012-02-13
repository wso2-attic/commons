DROP DATABASE IF EXISTS CUSTOMER_DB;

#create customer DB
CREATE DATABASE CUSTOMER_DB;

USE CUSTOMER_DB;

CREATE TABLE CUSTOMER_T(customerID int, customerName varchar(100), customerAge int, customerAddress varchar(200));