DELIMITER $$
DROP PROCEDURE IF EXISTS `dataservice_sample1`.`updateSalary` $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `updateSalary`(newSalary DOUBLE,INOUT revisedOn DATE)
BEGIN
update Salary set salary = newSalary where lastRevisedDate < revisedOn;
set revisedOn = CURRENT_DATE();
END $$

DELIMITER ;
