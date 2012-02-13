USE wsasset;
CREATE PROCEDURE MyProc (EmpId int)  SELECT * FROM Employees WHERE EmployeeID=EmpId;
create PROCEDURE General(FromNo int,ToNo int) SELECT * FROM Assets WHERE AssetID BETWEEN FromNo AND ToNo; 
