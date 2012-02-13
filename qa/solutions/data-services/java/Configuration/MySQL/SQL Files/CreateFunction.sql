create table CustomFunction(Number int(50));

insert INTO CustomFunction values (1);
insert INTO CustomFunction values (2);
insert INTO CustomFunction values (3);
insert INTO CustomFunction values (4);
insert INTO CustomFunction values (5);
insert INTO CustomFunction values (6);
insert INTO CustomFunction values (7);
insert INTO CustomFunction values (8);
insert INTO CustomFunction values (9);
insert INTO CustomFunction values (10);
insert INTO CustomFunction values (11);
insert INTO CustomFunction values (12);
insert INTO CustomFunction values (13);
insert INTO CustomFunction values (14);
insert INTO CustomFunction values (15);
insert INTO CustomFunction values (16);
insert INTO CustomFunction values (17);
insert INTO CustomFunction values (18);
insert INTO CustomFunction values (19);
insert INTO CustomFunction values (20);

# Create custom funtion for return factorial value.

CREATE FUNCTION factorial (n DECIMAL(3,0))
RETURNS DECIMAL(20,0)
DETERMINISTIC
BEGIN
DECLARE factorial DECIMAL(20,0) DEFAULT 1;
DECLARE counter DECIMAL(3,0);
SET counter = n;
factorial_loop: REPEAT
SET factorial = factorial * counter;
SET counter = counter - 1;
UNTIL counter = 1
END REPEAT;
RETURN factorial;
END//