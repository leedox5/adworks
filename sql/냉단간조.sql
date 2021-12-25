-- 절삭공정입력
SELECT *
FROM   RMSFLE.PFRACOFZ

--

SELECT *
FROM   RMSFLE.PFRACOFI
WHERE  COFI_CORP_CODE = 'K001'
AND    COFI_EONO      = 'E01'
AND    COFI_CUMT_NO   = '1127132'
FETCH  FIRST 20 ROWS ONLY

SELECT * FROM RMSFLE.PFRACOFK

--
SELECT 
      RTRIM(COFK_CODE)                                      CODE
   ,  RTRIM(COFK_GUBN_CODE)                                 DESC
   ,  CAST(RMSFLE.FNRMCMIQ03(COFK_CODE_KNAME,'', 'KO') AS VARCHAR(100) CCSID 1208) NAME
   ,  RTRIM(COFK_SORT_NUMB)                                 SORT
FROM  RMSFLE.PFRACOFK
WHERE COFK_CORP_CODE = 'K001'
AND   COFK_STND_GUBN = 'H'
AND   COFK_APPL_GUBN = 'Y'
ORDER BY COFK_GUBN_CODE, COFK_SORT_NUMB           
WITH UR
--
SELECT COFI_MATL_NAME                                   --재질명칭
              ,COFI_MATL_CODE                           --재질코드
              ,COFI_MATL_STND                           --재료단가표준구분
              ,COFI_MATL_IMSI                           --재료단가임시구분
              ,COFI_MATL_PRIC                           --재질단가
              ,COFI_MATL_UNIT                           --재질단위
              ,COFI_MATL_APDT                           --적용일
              ,COFI_MATL_GUBN                           --재질구분
              ,VALUE(COFI_SCRP_PRIC,0)  COFI_SCRP_PRIC  --SCRAP단가
              ,VALUE(COFI_SCRP_RATE,99) COFI_SCRP_RATE  --SCRAP율
              ,COFI_IMPT_CODE                           --수입코드
              ,VALUE(COFI_IMPT_COST,0)  COFI_IMPT_COST  --수입단가
              ,VALUE(COFI_TRIF_RATE,0)  COFI_TRIF_RATE  --관세율
              ,VALUE(COFI_UNUS_PRIC,0)  COFI_UNUS_PRIC  --산폐단가
              ,VALUE(COFI_CSTM_PRIC,0)  COFI_CSTM_PRIC  --환급단가
              ,COFI_MATL_GBCD                           --재질구분(입력값)
              ,VALUE(COFI_NET0_WGHT,0)  COFI_NET0_WGHT  --NET(절삭후)중량
              ,VALUE(COFI_MATT_WGHT,0)  COFI_MATT_WGHT  --소재(절삭전)중량
              ,VALUE(COFI_RCVR_RATE,0)  COFI_RCVR_RATE  --회수율
              ,VALUE(COFI_INPT_WGHT,0)  COFI_INPT_WGHT  --투입중량
              ,VALUE(COFI_DIA0_LENG,0)  COFI_DIA0_LENG  --소재직경
              ,VALUE(COFI_CUT0_LENG,0)  COFI_CUT0_LENG  --소재절단길이
              ,COFI_MATT_KIND                           --소재구분
              ,COFI_PROC_GUBN                           --가공비부품구분
              ,COFI_FORG_CODE                           --단조구분
              ,VALUE(COFI_PROC_NUM0,0)  COFI_PROC_NUM0  --가공공정수
              --절삭재료
              ,COFI_CHIP_STND
              ,COFI_CHIP_CODE
              ,COFI_CHIP_APDT
              ,COFI_CHIP_IMSI
              ,VALUE(MATL_SCRP_PRIC,0)  CHIP_SCRP_PRIC
              ,VALUE(MATL_SCRP_RATE,99) CHIP_SCRP_RATE
              ,MATL_UNIT                CHIP_MATL_UNIT
              ,VALUE(COFI_CUTT_YN,'N')  COFI_CUTT_YN
              ,COFI_CUTT_REMK
              ,(
                SELECT COUNT(*)
                  FROM RMSFLE.PFRACOFZ
                 WHERE COFZ_CORP_CODE = COFI_CORP_CODE
                   AND COFZ_PART_NO   = COFI_PART_NO
                   AND COFZ_EONO      = COFI_EONO
                   AND COFZ_CUMT_NO   = COFI_CUMT_NO
               )                         COFZ_CUTT_CNT0

               --패키지값
              ,VALUE(A.COFK_LOSS_RATE,0)     COFK_LOSS_RATE   --LOSS율
              ,VALUE(A.COFK_CODE_KNAME,' ')  COFK_CODE_KNAME  --소재절단 가공비처리 명

          FROM            RMSFLE.PFRACOFI
          LEFT OUTER JOIN RMSFLE.PFRACOFK A
          ON  v_APPL_CORP_CODE = COFK_CORP_CODE
          AND v_CMMS_STND_GUBN = COFK_STND_GUBN
          AND 'COFO03'         = COFK_GUBN_CODE
          AND COFI_MATT_KIND   = COFK_CODE
          AND 'Y'              = COFK_APPL_GUBN

          --절삭가공재료추출
          LEFT OUTER JOIN  RMSFLE.PFRBMATL
          ON  v_APPL_CORP_CODE = MATL_CORP_CODE
          AND COFI_CHIP_STND   = MATL_STND_GUBN
          AND COFI_CHIP_CODE   = MATL_CODE
          AND COFI_CHIP_APDT   = MATL_APPL_FRDT
          AND COFI_CHIP_IMSI   = MATL_GUBN

          WHERE COFI_CORP_CODE = 'K001'
          AND   COFI_PART_NO   = 'AAAAA'
          AND   COFI_EONO      = 'E01'
          AND   COFI_CUMT_NO   = '1127132'

