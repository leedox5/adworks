Exec sp_CreateDataLoadScriptDB2 'TW_SYS','PFRKPKCL'

TRUNCATE TABLE PFRKPKCL

select
	case 
		when [PKCL_COLM_ID] is null  then 'null'  
		else '|'+  left('''' + replace(convert(varchar(max),[PKCL_COLM_ID]),'''','''''') + ''''+'XXXXXXXXXXXXXXXXXXXXX',30)        +'|'
	end
from PFRKPKCL

INSERT INTO PFRKPKCL
SELECT [PKCL_COME_CODE]
      ,[PKCL_TABL_ID]
      ,[PKCL_COLM_ID]
      ,[PKCL_SORT_NUMB]
      ,[PKCL_COLM_LNCD]
      ,[PKCL_DATA_TYPE]
      ,[PKCL_DATA_LENG]
      ,[PKCL_COLM_ALGN]
      ,[PKCL_WITH_SCRN]
      ,[PKCL_DISP_ALLW]
      ,[PKCL_NAME_LNCD]
      ,[PKCL_UPDT_DATE]
      ,[PKCL_UPDT_CMAN]
      ,[PKCL_INIT_DATE]
      ,[PKCL_INIT_CMAN]
 FROM [MCAMSDEV].[KPTD191].[RMSFLE].[PFRKPKCL]
WHERE  PKCL_COME_CODE = 'WELD'
  AND  PKCL_TABL_ID   = 'PFRAWELK'
  AND  PKCL_DISP_ALLW = 'Y'
ORDER BY PKCL_SORT_NUMB




Exec sp_CreateDataLoadScriptDB2 'TW_SYS','PFRKPKCL'


