CREATE OR REPLACE TYPE address_t AS OBJECT(
    num NUMBER,    
    street VARCHAR2(100),
    city VARCHAR2(100),
    state VARCHAR2(100),
    country VARCHAR2(100)
    );
/

CREATE TABLE UDTtest_tbl(customer_id NUMBER, customer_name VARCHAR2(100), customer_address address_t);

INSERT INTO UDTtest_tbl VALUES(1, 'john', address_t(25, 'flower road', 'Brooklyn', 'Western London', 'United Kingdom'));
INSERT INTO UDTtest_tbl VALUES(2, 'peter', address_t(25, 'flower road', 'El Camino Real', 'Palo Alto', 'California'));
