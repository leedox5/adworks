--
-- Create Linked Server using ODBC DSN "AS400"
--
sp_addlinkedserver
	@server=N'MCAMSDEV',
	@srvproduct=N'DB2 UDB for iSeries',
	@provider=N'MSDASQL',
	@datasrc=N'MCAMS_DEV',
	@provstr='CMT=0;SYSTEM=10.133.21.181',
	@catalog='RMSFLE'
go

-- Define the credentials that will be used to
-- access objects hosted by the linked server

sp_addlinkedsrvlogin
	@rmtsrvname=N'MCAMSDEV',
	@useself='false',
	@rmtuser=N'MCAMSDEV',
	@rmtpassword='a#engnu62'
go

EXEC sp_serveroption 'MCAMSDEV', 'rpc out', true
go
-- for dr

sp_addlinkedserver
	@server=N'MCAMSDR',
	@srvproduct=N'DB2 UDB for iSeries',
	@provider=N'MSDASQL',
	@datasrc=N'MCAMS-DR',
	@provstr='CMT=0;SYSTEM=10.133.12.65',
	@catalog='RMSFLE'
go

-- Define the credentials that will be used to
-- access objects hosted by the linked server

sp_addlinkedsrvlogin
	@rmtsrvname=N'MCAMSDR',
	@useself='false',
	@rmtuser=N'WDEV101',
	@rmtpassword='a#engnu62'
go

EXEC sp_serveroption 'MCAMSDR', 'rpc out', true
go



--OLE DB 救凳

sp_addlinkedserver @server=N'MCAMS_DEV', 
                   @srvproduct=N'DB2400 UDB for iSeries',
                   -- IBMDA400/IBMDASQL are OLE DB providers 
                   -- for DB2 UDB for iSeries. IBMDASQL is
                   -- available with iSeries Access V5R3
                   @provider=N'IBMDASQL',
                   -- System Name
                   @datasrc=N'10.133.21.181',
                   @catalog='KPTD191'

go

sp_addlinkedsrvlogin @rmtsrvname=N'MCAMS_DEV',
                     @useself='false',
                     @rmtuser=N'MCAMSDEV',
                     @rmtpassword='a#engnu63'
go
--
-- RPC option is required for doing EXEC AT
--
EXEC sp_serveroption 'MCAMS_DEV', 'rpc out', true

--


-- 规过1
SELECT TOP 10 *
FROM   MCAMSDEV.KPTD191.RMSFLE.PFRACOFI
WHERE  COFI_CORP_CODE = 'K001'
AND    COFI_EONO      = 'E01'
AND    COFI_CUMT_NO   = '1127132'
FETCH  FIRST 20 ROWS ONLY

-- 规过2
EXEC ('
SELECT *
FROM   RMSFLE.PFRACOFI
WHERE  COFI_CORP_CODE = ''K001''
AND    COFI_EONO      = ''E01''
AND    COFI_CUMT_NO   = ''1127132''
FETCH  FIRST 20 ROWS ONLY
'
)
AT MCAMSDEV

declare @p1 varchar(100)
declare @p2 varchar(100)
declare @p3 varchar(100)
declare @p4 varchar(100)
declare @p5 varchar(100)
declare @p6 varchar(100)

EXEC('CALL RMSFLE.SPRA00IQ10(?,?,?,?,?,?,?,?,?,?,?,?)','ENGNU62','K001','AAAAA','E01','1127132','CICI'
     ,@p1 output,@p2 output,@p3 output,@p4 output,@p5 output,@p6 output)AT MCAMSDEV

print @p1
print @p2
print @p3
print @p4
print @p5

EXEC
(
'call RMSFLE.SPRAQ1SA01(?,?,?,?,?,?,?,?,?,?
                       ,?,?,?,?,?,?,?,?,?,?
                       ,?,?,?,?,?,?,?,?,?,?
                       ,?,?,?,?,?,?,?,?,?,?
                       ,?,?,?,?,?,?,?,?,?,?
                       ,?,?,?,?,?,?,?,?)','ENGNU62','K001','AAAAA','E01','1127132',
                       


	
	
SELECT TOP 10 *
FROM MCAMSDEV.KPTD191.QSYS2.SYSROUTINES 
WHERE SPECIFIC_SCHEMA = 'RMSFLE'	

