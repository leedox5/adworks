SELECT *
FROM RMSFLE.PFRAPLCK

--

SELECT *
FROM RMSFLE.PFRAPLCI
ORDER BY PLCI_UPDT_DATE DESC
FETCH  FIRST 10 ROWS ONLY

--
--�����ڵ� PLCK_CORP_CODE
--ǥ�ر��� PLCK_STND_GUBN
--�з��ڵ� PLCK_GUBN_CODE
--�����ڵ� PLCK_CODE
--��ǰ���� PLCK_ITEM_GUBN
--��ǰ�ڵ� PLCK_ITEM_CODE

-- �ش��ǰ �ڵ� ��������
SELECT PLCK_ITEM_CODE                                       CODE
      ,RMSFLE.FNRMCMIQ03(PLCK_ITMC_KNAME,'','KO') NAME
      ,PLCK_GUBN_CODE                             PLCI_PROI_BOON
      ,PLCK_CODE                                  PLCI_PROI_GUBN
      ,PLCK_ITEM_GUBN                             PLCI_PROI_ITGB
      ,PLCK_ITEM_CODE                             PLCI_PROI_ITCD
  FROM RMSFLE.PFRAPLCK
 WHERE PLCK_CORP_CODE = 'K001'
   AND PLCK_STND_GUBN = 'H'
   AND PLCK_GUBN_CODE IN('PLCT02','PLCT22','PLCT32')
   AND PLCK_ITEM_CODE LIKE
          (SELECT CASE
                    -- '����1��'�� ���
                    WHEN 'A' = 'A'       THEN 'A'
                    -- '����2��'�� ���
                    WHEN 'A' = 'E'       THEN 'E'
                    -- PL FUEL FILLER DR�� ���
                    WHEN 'A' = 'D'       THEN 'D'
                    -- '����'�� ���
                    WHEN 'A' = 'B'       THEN 'B0201'
                    -- '����'�� ���
                    WHEN 'A' = 'C'       THEN 'C0201'
                    -- ����
                    ELSE 'ERROR'
                  END
            FROM SYSIBM.SYSDUMMY1
          ) || '%'
   AND PLCK_APPL_GUBN = 'Y'
 ORDER BY PLCK_SORT_NUMB
  WITH UR
--


SELECT PLCK_ITEM_GUBN          CODE
       ,RMSFLE.FNRMCMIQ03(PLCK_ITMG_KNAME,'','KO')  NAME
       ,PLCK_GUBN_CODE          DESC
  FROM RMSFLE.PFRAPLCK
 WHERE PLCK_CORP_CODE = 'K001'
   AND PLCK_STND_GUBN = 'H'
   AND PLCK_GUBN_CODE = 'PLCT11'
   AND PLCK_CODE LIKE
         CASE WHEN 'PLCT11' = 'PLCT51' THEN
           CASE WHEN 'A08' = 'E01' THEN 'E01%'
                WHEN 'A08' = 'E02' THEN 'E02%'
                WHEN 'A08' = 'E03' THEN 'E03%'
                WHEN 'A08' = 'E06' THEN 'E06%'
                ELSE 'ERROR'
           END
         ELSE
           CASE WHEN 'A08' = 'A01' THEN 'A01%'
                WHEN 'A08' = 'A02' THEN 'A02%'
                WHEN 'A08' = 'A03' THEN 'A03%'
                WHEN 'A08' = 'A06' THEN 'A06%'
                WHEN 'A08' = 'A08' THEN 'A08%'
                ELSE 'ERROR'
           END
         END
     AND PLCK_APPL_GUBN = 'Y'
 ORDER BY PLCK_SORT_NUMB
  WITH UR
--


SELECT PLCK_ITEM_GUBN          CODE
       ,RMSFLE.FNRMCMIQ03(PLCK_ITMG_KNAME,'','KO')  NAME
       ,PLCK_GUBN_CODE          DESC
  FROM RMSFLE.PFRAPLCK
 WHERE PLCK_CORP_CODE = 'K001'
   AND PLCK_STND_GUBN = 'H'
   AND PLCK_GUBN_CODE = 'PLCT51'
   AND PLCK_CODE LIKE
         CASE WHEN 'PLCT51' = 'PLCT51' THEN
           CASE WHEN 'E08' = 'E01' THEN 'E01%'
                WHEN 'E08' = 'E02' THEN 'E02%'
                WHEN 'E08' = 'E03' THEN 'E03%'
                WHEN 'E08' = 'E06' THEN 'E06%'
                ELSE 'ERROR'
           END
         ELSE
           CASE WHEN 'E08' = 'A01' THEN 'A01%'
                WHEN 'E08' = 'A02' THEN 'A02%'
                WHEN 'E08' = 'A03' THEN 'A03%'
                WHEN 'E08' = 'A06' THEN 'A06%'
                WHEN 'E08' = 'A07' THEN 'A07%'
                WHEN 'E08' = 'A08' THEN 'A08%'
                ELSE 'ERROR'
           END
         END
     AND PLCK_APPL_GUBN = 'Y'
 ORDER BY PLCK_SORT_NUMB
  WITH UR


