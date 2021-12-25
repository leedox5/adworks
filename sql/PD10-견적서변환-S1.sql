SELECT ROW_NUMBER()
           OVER(ORDER BY COMD_VEND_NAME) COMD_ROW0_NUMB
          ,COMD_VEND_NAME
          ,COMD_ITEM_INCR
          ,COMD_ITEM_EQAL
          ,COMD_ITEM_DISC
          ,COMD_ITEM_TOTL
          ,COMD_EFCT_INCR
          ,COMD_EFCT_DISC
          ,COMD_EFCT_TOTL

          -- 대표 팀
          ,CASE COMD_TEAM_CNT0
             WHEN 0 THEN 'ERROR'
             WHEN 1 THEN EMST_TEAM_NAME
             ELSE  EMST_TEAM_NAME || '외 ' || (COMD_TEAM_CNT0-1) || '팀'
           END COMD_TITL_TEAM
FROM
(
    SELECT 
            SIME_CORP_CODE
        ,   SIME_MNGE_NO
        ,   SIME_VEND_CODE
        ,   RMSFLE.FNRBCMIQ01(SIME_CORP_CODE, SIME_VEND_CODE)                COMD_VEND_NAME
        ,   SUM(CASE WHEN SIME_ADJT_PRI1 > 0 THEN 1 ELSE 0 END)              COMD_ITEM_INCR
        ,   SUM(CASE WHEN SIME_ADJT_PRI1 = 0 THEN 1 ELSE 0 END)              COMD_ITEM_EQAL
        ,   SUM(CASE WHEN SIME_ADJT_PRI1 < 0 THEN 1 ELSE 0 END)              COMD_ITEM_DISC
        ,   COUNT(*)                                                         COMD_ITEM_TOTL
        ,   SUM(CASE WHEN SIME_EFCT_COST > 0 THEN SIME_EFCT_COST ELSE 0 END) COMD_EFCT_INCR
        ,   SUM(CASE WHEN SIME_EFCT_COST < 0 THEN SIME_EFCT_COST ELSE 0 END) COMD_EFCT_DISC
        ,   SUM(SIME_EFCT_COST)                                              COMD_EFCT_TOTL
        ,   COUNT(DISTINCT EMST_INIT_TEAM)                                   COMD_TEAM_CNT0
    FROM       RMSFLE.PFRDSIME
    INNER JOIN RMSFLE.PFRCEMST E1
    ON    EMST_CORP_CODE = SIME_CORP_CODE
    AND   EMST_COST_GUBN = SIME_COST_GUBN
    AND   EMST_PART_NO   = SIME_PART_NO
    AND   EMST_EONO      = SIME_EONO
    AND   EMST_CUMT_NO   = SIME_CUMT_NO
    WHERE SIME_CORP_CODE = 'K001'
    AND   SIME_MNGE_NO   = 'Q201303001'
    AND   EMST_INIT_TEAM IN ( '10001899' )
    AND   SIME_SIMU_YN   != 'N'
    AND   SIME_ADJT_SEQ0  > 0  --0은 정산대상 제외
    GROUP BY SIME_CORP_CODE, SIME_MNGE_NO, SIME_VEND_CODE
) A
LEFT OUTER JOIN
-- 업체별로 품목수가 가장 많은 업체 1개 얻기
(
           SELECT SIME_VEND_CODE
                 ,RMSFLE.FNRBCMIQ12(SIME_CORP_CODE,EMST_INIT_TEAM,'KO') EMST_TEAM_NAME
           FROM
           (
                  SELECT
                  SIME_CORP_CODE
                        ,SIME_VEND_CODE
                        ,EMST_INIT_TEAM
                        ,COUNT(EMST_PART_NO) EMST_ITEM_CNT0
                        ,ROW_NUMBER()
                         OVER(PARTITION BY SIME_CORP_CODE
                                          ,SIME_VEND_CODE
                                  ORDER BY COUNT(EMST_PART_NO) DESC
                         ) RANK
                  FROM       RMSFLE.PFRDSIME
                  INNER JOIN RMSFLE.PFRCEMST
                  ON  EMST_CORP_CODE = SIME_CORP_CODE
                  AND EMST_COST_GUBN = SIME_COST_GUBN
                  AND EMST_PART_NO   = SIME_PART_NO
                  AND EMST_EONO      = SIME_EONO
                  AND EMST_CUMT_NO   = SIME_CUMT_NO
                  WHERE SIME_CORP_CODE  = 'K001'
                  AND SIME_MNGE_NO    = 'Q201303001'
                  AND EMST_INIT_TEAM IN ('10001899')
                  AND SIME_SIMU_YN   != 'N'
                  GROUP BY SIME_CORP_CODE ,SIME_VEND_CODE ,EMST_INIT_TEAM
                  ORDER BY SIME_VEND_CODE ,EMST_ITEM_CNT0 DESC
            ) B1
            WHERE RANK = 1
            ORDER BY SIME_VEND_CODE
) B2
ON B2.SIME_VEND_CODE = A.SIME_VEND_CODE
ORDER BY COMD_ROW0_NUMB
WITH UR
