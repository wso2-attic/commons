

create or replace type numo is table of integer;


create or replace TYPE num_arr AS VARRAY(2) OF INTEGER;


create or replace procedure see_employee3(param1 out numo, param2 IN num_arr)
   as
   begin
   param1 := numo();
   param1.extend(2); 
   for i in param2.first..param2.last
   loop
   param1(i) := param2(i);
   end loop;
  end;
  /
