CREATE TABLE ODE_JOB (
  jobid varchar(64)  NOT NULL DEFAULT '',
  ts bigint  NOT NULL DEFAULT 0,
  nodeid varchar(64)  NULL,
  scheduled int  NOT NULL DEFAULT 0,
  transacted int  NOT NULL DEFAULT 0,
  details varbinary(4096)  NULL,
  PRIMARY KEY(jobid));

CREATE INDEX IDX_ODE_JOB_TS ON ODE_JOB(ts);
CREATE INDEX IDX_ODE_JOB_NODEID ON ODE_JOB(nodeid);



