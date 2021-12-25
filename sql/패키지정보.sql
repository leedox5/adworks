-- WELD_CODE
SELECT   WELK_CODE                    CODE                           
        ,WELK_GUBN_CODE               DESC                           
        ,CAST(RMSFLE.FNRMCMIQ03(WELK_CODE_KNAME, '', 'KO') AS VARCHAR(100) CCSID 1208) NAME 
SELECT  top 10 *
   FROM MCAMSDEV.KPTD191.RMSFLE.PFRAWELK                                              
   WHERE WELK_CORP_CODE = 'K001'                        
     AND WELK_STND_GUBN = 'H'                        
     AND WELK_GUBN_CODE = 'WELD02'                        
     AND WELK_APPL_GUBN = 'Y'  
     --AND WELK_CODE LIKE 'SPOT%'                                      
  ORDER BY WELK_SORT_NUMB             WITH UR                        


select *
from openquery
(MCAMSDEV,                   
'SELECT *
FROM   KPTD191.RMSFLE.PFRKPKCL
WHERE  PKCL_COME_CODE = ''WELD''
  AND  PKCL_TABL_ID   = ''PFRAWELK''
  AND  PKCL_DISP_ALLW = ''Y''            '
)


EXEC(
'
DELETE
FROM   KPTD191.RMSFLE.PFRKPKCL
WHERE  PKCL_COME_CODE = ''WELD''
  AND  PKCL_TABL_ID   = ''PFRAWELK''
  AND  PKCL_DISP_ALLW = ''Y'' 
'
)AT MCAMSDEV

--
6


EXEC( 'DROP PROCEDURE RMSFLE.SPRAQ1SA011' )AT MCAMSDEV


SELECT *
--DELETE
FROM   MCAMSDEV.KPTD191.RMSFLE.PFRKPKCL
WHERE  PKCL_COME_CODE = 'WELD'
  AND  PKCL_TABL_ID   = 'PFRAWELK'
  AND  PKCL_DISP_ALLW = 'Y' 


--



EXEC('insert into RMSFLE.PFRKPKCL   select ''WELD''              ,''PFRAWELK''          ,''WELK_INIT_DATE''    ,3400                ,''L1315300''          ,''CHAR''              ,''8''                 ,''C''                 ,''60''                ,''Y''                 ,''''                  ,''20091019''          ,''ENGNU62''           ,''20091013''          ,''ENGNU62''            from sysibm.sysdummy1 ')AT MCAMSDEV

--아래와 같이 해도 된다네
insert into MCAMSDEV.KPTD191.RMSFLE.PFRKPKCL   select 'WELD'              ,'PFRAWELK'          ,'WELK_INIT_DATE'    ,3400                ,'L1315300'          ,'CHAR'              ,'8'                 ,'C'                 ,'60'                ,'Y'                 ,''                  ,'20091019'          ,'ENGNU62'           ,'20091013'          ,'ENGNU62'   