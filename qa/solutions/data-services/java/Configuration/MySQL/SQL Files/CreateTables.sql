
use wsasset; # Change to WSASSET database. 

create table Employees( EmployeeID int(50),
                        FirstName varchar(50),  
                        LastName varchar(50),   
                        Title varchar(50),
                        Extention varchar(50),
                        WorkLocation varchar(50),
                        OfficeLocation varchar(50),
                        PRIMARY KEY(EmployeeID)); 

CREATE table AssetCategories(AccetCategoryID int(50),
                             AccetCategory varchar(100),
                             PRIMARY KEY(AccetCategoryID));


create table Assets(AssetID int(50),
                    AssetDescripion varchar(100),
                    EmployeeID int(50),
                    AssetCategoryID int(50),
                    StatusID int(50),
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
         
                    


create table AssetStatus(StatusID int(50),
                         StatusDescription varchar(100),
                         PRIMARY KEY(StatusID));

create table Depreciation(DepreciationID int(50),
                          AssetID int(50),
                          DepreciationDtate varchar(50),
                          DepreciationAmount Int(50),
                          PRIMARY KEY(AssetID),
                          Foreign Key (AssetID) references Assets(AssetID));
                          
create table Maintenance(MaintenanceID int(50),
                         AssetID int(50),
                         MaintenanceDate varchar(50),
                         MaintenanceDescription varchar(100),
                         MaintenancePerformedBy varchar(100),
                         MaintenanceCost varchar(50),
                         PRIMARY KEY(MaintenanceID),
                         Foreign Key (AssetID) references Assets(AssetID));
                         
                    
                    