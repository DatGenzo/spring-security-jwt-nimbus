USE master;
GO

IF DB_ID(N'jwt_nimbus') IS NULL
BEGIN
    CREATE DATABASE jwt_nimbus;
END;
GO

SELECT
    name,
    create_date
FROM sys.databases
WHERE name = N'jwt_nimbus';
GO
