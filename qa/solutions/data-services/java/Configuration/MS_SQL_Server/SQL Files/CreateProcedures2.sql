USE WSASSET
GO
CREATE PROCEDURE General @FromNo numeric(37),@ToNo numeric(37) AS SELECT * FROM wsasset.Assets WHERE wsasset.AssetID BETWEEN @FromNo AND @ToNo;

