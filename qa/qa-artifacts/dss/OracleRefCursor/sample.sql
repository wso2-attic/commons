/* create and populate dept table */

DROP TABLE dept
/

CREATE TABLE dept (
    dept_id       NUMBER
  , name          VARCHAR2(100)
  , location      VARCHAR2(100)
)
/

INSERT INTO dept VALUES (100 , 'ACCOUNTING'          , 'BUTLER, PA');
INSERT INTO dept VALUES (101 , 'RESEARCH'            , 'DALLAS, TX');
INSERT INTO dept VALUES (102 , 'SALES'               , 'CHICAGO, IL');
INSERT INTO dept VALUES (103 , 'OPERATIONS'          , 'BOSTON, MA');
INSERT INTO dept VALUES (104 , 'IT'                  , 'PITTSBURGH, PA');
INSERT INTO dept VALUES (105 , 'ENGINEERING'         , 'WEXFORD, PA');
INSERT INTO dept VALUES (106 , 'QA'                  , 'WEXFORD, PA');
INSERT INTO dept VALUES (107 , 'PROCESSING'          , 'NEW YORK, NY');
INSERT INTO dept VALUES (108 , 'CUSTOMER SUPPORT'    , 'TRANSFER, PA');
INSERT INTO dept VALUES (109 , 'HQ'                  , 'WEXFORD, PA');
INSERT INTO dept VALUES (110 , 'PRODUCTION SUPPORT'  , 'MONTEREY, CA');
INSERT INTO dept VALUES (111 , 'DOCUMENTATION'       , 'WEXFORD, PA');
INSERT INTO dept VALUES (112 , 'HELP DESK'           , 'GREENVILLE, PA');
/
commit;
/
 
/* CREATE PACKAGE "ref_cursor_package" */

CREATE OR REPLACE PACKAGE ref_cursor_package
AS
    TYPE t_ref_cursor IS REF CURSOR;
    FUNCTION get_dept_ref_cursor(p_dept_id INTEGER) RETURN t_ref_cursor;
END;
/


CREATE OR REPLACE PACKAGE BODY ref_cursor_package
AS
    FUNCTION get_dept_ref_cursor (p_dept_id INTEGER)
        RETURN t_ref_cursor IS dept_ref_cursor t_ref_cursor;
    BEGIN
        OPEN dept_ref_cursor FOR
            SELECT dept_id, name, location FROM dept WHERE dept_id > p_dept_id ORDER BY dept_id;
        RETURN dept_ref_cursor;
    END get_dept_ref_cursor;
END ref_cursor_package;
/
