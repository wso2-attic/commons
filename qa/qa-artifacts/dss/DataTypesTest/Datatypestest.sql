DROP DATABASE IF EXISTS DataTypesTest;
CREATE DATABASE DataTypesTest;
USE DataTypesTest;

CREATE TABLE Ints(
rowid INT NOT NULL AUTO_INCREMENT,
smallints SMALLINT,
tinyints TINYINT,
mediumints MEDIUMINT,
ints INT,
bigints BIGINT,
PRIMARY KEY (rowid)
);

CREATE TABLE Strings(
rowid INT NOT NULL AUTO_INCREMENT,
varchars VARCHAR(255),
texts TEXT,
tinytexts TINYTEXT,
mediumtexts MEDIUMTEXT,
longtexts LONGTEXT,
enums ENUM('S', 'M', 'L', 'XL', "XXL"),
PRIMARY KEY (rowid)
);

CREATE TABLE floats(
rowid INT NOT NULL AUTO_INCREMENT,
floats FLOAT,
doubles DOUBLE,
decimals DECIMAL(30,3),
decimals2 DECIMAL(30,27),
PRIMARY KEY (rowid)
);

CREATE TABLE DTimes(
rowid INT NOT NULL AUTO_INCREMENT,
dates DATE,
datetimes DATETIME,
times TIME,
timestamps TIMESTAMP,
years YEAR,
PRIMARY KEY (rowid)
);


insert into DTimes Values()
