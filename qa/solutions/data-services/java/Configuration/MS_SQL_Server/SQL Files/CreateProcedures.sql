USE WSASSET
GO
CREATE PROCEDURE MyProc @EmpId numeric(37) AS SELECT * FROM wsasset.Employees WHERE wsasset.EmployeeID=@EmpId;
