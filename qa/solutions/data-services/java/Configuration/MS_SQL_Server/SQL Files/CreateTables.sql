
use wsasset; /* Change to WSASSET database.*/ 

create table Employees( EmployeeID numeric(37),
                        FirstName varchar(50),  
                        LastName varchar(50),   
                        Title varchar(50),
                        Extention varchar(50),
                        WorkLocation varchar(50),
                        OfficeLocation varchar(50),
                        PRIMARY KEY(EmployeeID)); 

CREATE table AssetCategories(AccetCategoryID numeric(37),
                             AccetCategory varchar(100),
                             PRIMARY KEY(AccetCategoryID));


create table Assets(AssetID numeric(37),
                    AssetDescripion varchar(100),
                    EmployeeID numeric(37),
                    AssetCategoryID numeric(37),
                    StatusID numeric(37),
                    ModelNumber varchar(50),
                    SerialNumber varchar(50),
                    BarcodeNumber varchar(50),
                    DateAcquired varchar(50),
                    DateSold varchar(50),
                    PurchasePrice varchar(50),
                    DepreciationMethod varchar(100),
                    Currentvalue varchar(50),
                    PRIMARY KEY(AssetID),
                    Foreign Key (EmployeeID) references Employees(EmployeeID));
         
                    


create table AssetStatus(StatusID numeric(37),
                         StatusDescription varchar(100),
                         PRIMARY KEY(StatusID));

create table Depreciation(DepreciationID numeric(37),
                          AssetID numeric(37),
                          DepreciationDtate varchar(50),
                          DepreciationAmount numeric(37),
                          PRIMARY KEY(AssetID),
                          Foreign Key (AssetID) references Assets(AssetID));
                          
create table Maintenance(MaintenanceID numeric(37),
                         AssetID numeric(37),
                         MaintenanceDate varchar(50),
                         MaintenanceDescription varchar(100),
                         MaintenancePerformedBy varchar(100),
                         MaintenanceCost numeric(37),
                         PRIMARY KEY(MaintenanceID),
                         Foreign Key (AssetID) references Assets(AssetID));
                         
                    
   

CREATE table CustomFunction(Number numeric(37));

               