USE WSASSET
/*Create custom funtion for return factorial value.*/
GO
CREATE FUNCTION factorial (@n DECIMAL(3,0))
RETURNS DECIMAL(20,0)
/*DETERMINISTIC*/
BEGIN
DECLARE  @factorial DECIMAL(20,0) /*DEFAULT 1*/
DECLARE @counter DECIMAL(3,0);
SET @counter = @n;
WHILE(@counter > 1)
BEGIN
SET @factorial = @factorial * @counter;
	SET @counter = @counter - 1;
		IF(@counter = 1)
			BREAK
		ELSE
			CONTINUE
	END
RETURN @factorial;
END;





