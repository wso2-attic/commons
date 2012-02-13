SELECT * FROM Company.Employee;

SELECT Employee.FName, Employee.LName, Employee.Address
FROM Employee INNER JOIN Department ON Employee.Dep_id=Department.Dep_ID
WHERE Department.Dep_Name=?
ORDER BY Employee.LName;

SELECT Project.P_Name, Project.Client_Name, Project.Description, Employee.FName, Employee.LName
FROM Project LEFT JOIN Employee ON Project.Manager=Employee.ID
ORDER BY Project.P_ID;

SELECT Employee.FName, Employee.LName, Vehicle.model_name, Vehicle.Reg_NO
FROM Vehicle LEFT JOIN Employee ON Vehicle.AllocatedTo=Employee.ID
WHERE Vehicle.model_name LIKE ?;
