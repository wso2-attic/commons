

create or replace TYPE num_arr AS VARRAY(2) OF INTEGER;



create or replace procedure see_employee2(param1 OUT INTEGER,param2 IN num_arr)
   as
   begin
    param1 := 1;
   end;
  /
