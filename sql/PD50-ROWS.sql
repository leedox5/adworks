    SELECT EMST_CORP_CODE
                      ,SIME_MNGE_NO
                      ,SIMM_SIMU_CODE
                      ,SIMM_DEAD_DATE
                      ,EMST_VEND_CODE
                      ,EMST_COST_GUBN
                      ,EMST_PART_NO
                      ,EMST_EONO
                      ,EMST_CUMT_NO
                      ,EMST_EXTR_SEQ0
                      ,EMST_CARS_CODE
                      ,CASE SIME_CHNG_YN01 WHEN 'Y' THEN 'O' ELSE '' END SIME_CHNG_YN01
                      ,CASE SIME_CHNG_YN02 WHEN 'Y' THEN 'O' ELSE '' END SIME_CHNG_YN02
                      ,CASE SIME_CHNG_YN03 WHEN 'Y' THEN 'O' ELSE '' END SIME_CHNG_YN03
                      ,CASE SIME_CHNG_YN04 WHEN 'Y' THEN 'O' ELSE '' END SIME_CHNG_YN04
                      ,EMST_MPMS_PRIC
                      ,APPR_FINI_DATE
                      ,SIME_MPMS_CMAN
                      ,SIME_LKCH_CMAN
                      ,SIME_LKCH_TEAM
                      ,SIME_LKCH_DATE
                      ,SIME_CAUS_LKCD
                      ,CASE
                         WHEN VALUE(SIME_CAUS_LKCD,'') IN ('E','O','G','D') THEN RMSFLE.FNRMCMIQ02('L1858201','KO') -- 영구
                         WHEN VALUE(SIME_CAUS_LKCD,'') > ''         THEN RMSFLE.FNRMCMIQ02('L1860501','KO') -- 일시
                         ELSE                                       ''
                       END SIME_CAUS_LKNM
                      ,LNEC_ETC0_RESN
                      ,SIME_MINC_QTY0                                                          MINC_YEAR_QTY0
                      ,RMSFLE.FNRBCMIQ01(EMST_CORP_CODE,EMST_VEND_CODE)                        EMST_VEND_NAME
                      ,RMSFLE.FNRCCMIQ01(EMST_CORP_CODE,EMST_PART_NO  )                        EMST_PART_NAME

                      ,RMSFLE.FNRBCMIQ08(VALUE(SIME_MPMS_CMAN,''),'KO')             SIME_MPMS_NAME
                      ,RMSFLE.FNRBCMIQ12(EMST_CORP_CODE,ITEM_DEPT_CD,'KO')          SIME_MPMS_TMNM
                      ,RMSFLE.FNRBCMIQ08(VALUE(SIME_LKCH_CMAN,''),'KO')             SIME_LKCH_NAME
                      ,RMSFLE.FNRBCMIQ12(EMST_CORP_CODE,SIME_LKCH_TEAM,'KO')        SIME_TEAM_NAME

                      -- (페이지번호) 전체 공통
                      ,(ROW_NUMBER()
                          OVER(ORDER BY EMST_VEND_CODE
                                       ,EMST_PART_NO
                                       ,EMST_EONO
                                       ,EMST_EXTR_SEQ0
                       ) -1) / 50 + 1 AS PAGE
                  FROM
                      (
                       SELECT SIME.*
                             ,SIMM_SIMU_CODE
                             ,SIMM_DEAD_DATE
                             ,EMST_CORP_CODE
                             ,EMST_COST_GUBN
                             ,EMST_PART_NO
                             ,EMST_EONO
                             ,EMST_CUMT_NO
                             ,EMST_VEND_CODE
                             ,EMST_CARS_CODE
                             ,EMST_MPMS_PRIC
                             ,EMST_EXTR_SEQ0
                             ,APPR_FINI_DATE
                             ,LNEC_ETC0_RESN
                             ,ITEM_DEPT_CD
                         FROM RMSFLE.PFRDSIME SIME
                               INNER JOIN
                              RMSFLE.PFRDSIMM
                                  ON SIMM_CORP_CODE = SIME_CORP_CODE
                                 AND SIMM_MNGE_NO   = SIME_MNGE_NO
                               INNER JOIN
                              RMSFLE.PFRCEMST
                                  ON EMST_CORP_CODE = SIME_CORP_CODE
                                 AND EMST_COST_GUBN = SIME_COST_GUBN
                                 AND EMST_PART_NO   = SIME_PART_NO
                                 AND EMST_EONO      = SIME_EONO
                                 AND EMST_CUMT_NO   = SIME_CUMT_NO
                                LEFT OUTER JOIN
                              RMSFLE.PFRCAPPR
                                  ON APPR_CORP_CODE = EMST_APPR_CORP
                                 AND APPR_NO        = EMST_APPR_NO
                                LEFT OUTER JOIN
                              RMSFLE.PFRDLNEC
                                  ON LNEC_CORP_CODE = SIME_CORP_CODE
                                 AND LNEC_MNGE_NO   = SIME_MNGE_NO
                                 AND LNEC_LKCH_CMAN = SIME_LKCH_CMAN
                                 AND LNEC_VEND_CODE = SIME_VEND_CODE
                                 AND LNEC_PART_NO   = SIME_PART_NO
                                LEFT OUTER JOIN
                              MPMFLE.ITEM
                                  ON ITEM_PART_NO = SIME_PART_NO
                                 AND ITEM_VEND_CD = SIME_VEND_CODE

                        WHERE SIME_CORP_CODE = 'K001'
                          AND SIME_MNGE_NO   = 'Q201302001'
                          AND SIMM_SIMU_CODE = '90'
                          AND SIME_ADJT_SEQ0 = 1
                          AND SIME_VEND_CODE LIKE '%'
                          AND EMST_CARS_CODE LIKE '%'
                          AND SIME_PART_NO   LIKE '%'
                      )SIME


