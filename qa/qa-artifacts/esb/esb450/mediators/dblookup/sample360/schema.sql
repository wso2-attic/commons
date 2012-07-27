create database esbdblookup;
use esbdblookup;

CREATE table company(name varchar(10), id varchar(10), price double);

INSERT into company values ('IBM','c1',0.0);
INSERT into company values ('SUN','c2',0.0);
INSERT into company values ('MSFT','c3',0.0);
INSERT into company values ('IBM','d1',0.0);

CREATE PROCEDURE getid(na VARCHAR(10)) SELECT * FROM company WHERE name= na;

call getid('IBM');

DELIMITER $$
CREATE FUNCTION  fn_getid(na VARCHAR(10)) RETURNS VARCHAR(3)
READS SQL DATA
RETURN (SELECT id FROM company WHERE name = na)
$$
DELIMITER ;

select fn_getid('IBM');



INSERT into company values ('IBM','d1',0.0);
