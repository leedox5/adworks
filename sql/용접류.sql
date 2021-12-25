SELECT *
DELETE FROM RMSFLE.PFRAWELI
WHERE WELI_CUMT_NO = '1127132'


DELETE

  FROM MCAMSDEV.RMSFLE.PFRAWELI
 WHERE WELI_CORP_CODE = 'K001'
   AND WELI_PART_NO   = 'AAAAA'
   AND WELI_EONO      = 'E01'
   AND WELI_CUMT_NO   =  1127132
   AND WELI_WELD_SEQ0 =  3
--
SELECT TOP 10 WELI_CUMT_NO, WELI_INDS_CODE
    --ELI_WELD_SEQ0, WELI_ICNT_CODE, WELI_INDS_CODE
    ,   WELI_SHET_GUBN , WELI_SHET_CODE
    ,   WELI_MATL_STND1, WELI_MATL_CODE1,WELI_MATL_APDT1, WELI_MATL_IMSI1, WELI_NET0_WEGT1
SELECT TOP 10 WELI_INDS_CODE,*
FROM MCAMSDEV.KPTD191.RMSFLE.PFRAWELI
ORDER BY WELI_UPDT_DATE DESC


FETCH  FIRST 10 ROWS ONLY
--
SELECT * FROM RMSFLE.PFRAWELM
SELECT * FROM RMSFLE.PFRAWELP
--

declare @p1 varchar(100)
declare @p2 varchar(100)
declare @p3 varchar(100)
declare @p4 varchar(100)
declare @p5 varchar(100)
declare @p6 varchar(100)
EXEC ('call RMSFLE.SPRA00IQ10(?,?,?,?,?,?,?,?,?,?,?,?)','ENGNU62','K001','AAAAA','E01','1127132','WELI',@p1 output,@p2 output,@p3 output,@p4 output,@p5 output,@p6 output) AT MCAMSDEV

--

    INSERT INTO RMSFLE.PFRAWELI (
          WELI_CORP_CODE
         ,WELI_PART_NO
         ,WELI_EONO
         ,WELI_CUMT_NO
         ,WELI_WELD_SEQ0
         ,WELI_KIND_GUBN
         ,WELI_KIND_CODE
         ,WELI_GUBN_GUBN
         ,WELI_GUBN_CODE
         ,WELI_SIZE_GUBN
         ,WELI_SIZE_CODE
         ,WELI_ICNT_GUBN
         ,WELI_ICNT_CODE
         ,WELI_JIG0_YN
         ,WELI_WELD_PONT
         ,WELI_HWSP_CNT0
         ,WELI_BASE_GUBN
         ,WELI_BASE_CODE
         ,WELI_THIK_GUBN
         ,WELI_THIK_CODE
         ,WELI_WELD_TERM
         ,WELI_WELD_LENG
         ,WELI_POND_CNT0
         ,WELI_INDS_CODE
         ,WELI_UPDT_CMAN
         ,WELI_UPDT_DATE
         ,WELI_INIT_CMAN
         ,WELI_INIT_DATE
      ) VALUES (
          'K001'
         ,'AAAAA'
         ,'E01'
         ,'1127132'
         ,0
         ,'WELD01'
         ,'SPOT'
         ,'WELD02'
         ,'SPOTMANU'
         ,'WELD03'
         ,'SPOT01'
         ,'WELD06'
         ,'2'
         ,'Y'
         ,'11.00'
         ,'0'
         ,''
         ,''
         ,''
         ,''
         ,'0'
         ,'0'
         ,'0'
         ,'P2'
         ,'ENGNU62'
         ,'20120914'
         ,'ENGNU62'
         ,'20120914'
      )



         ,@COMC_BASE_GUBN
         ,@COMC_BASE_CODE
         ,@COMC_THIK_GUBN
         ,@COMC_THIK_CODE
         ,@COMC_WELD_TERM
         ,@COMC_WELD_LENG
         ,@COMC_POND_CNT0
         ,@COMH_SABN
         ,TODAY
         ,@COMH_SABN
         ,TODAY
      );


-- Àç·áºñ
SELECT *
FROM RMSFLE.PFRAWELM
ORDER BY WELM_UPDT_DATE DESC
FETCH  FIRST 10 ROWS ONLY

