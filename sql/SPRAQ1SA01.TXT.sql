/** �ڵ�����(������) - �Է����� **/

/**
 * [�����̷�] 2009.08.05 : @COMT_SQL0_CODE �ڸ����� �۰� ��� Host Variable..���� ������ ������. [P.S.R]
 *            2012.09.14 : @COMC_INDS_CODE �߰�
 */

CREATE PROCEDURE RMSFLE.SPRAQ1SA011
(
    IN  @COMH_SABN          VARCHAR(8),      -- 1���
    IN  @COMH_CORP_CODE     CHAR(4),         -- 2�����ڵ�
    IN  @SPMS_PART_NO       VARCHAR(21),     -- 3PART NO
    IN  @SPMS_EONO          VARCHAR(10),     -- 4EO NO
    IN  @SPMS_CUMT_NO       INTEGER,         -- 5����ȣ
    IN  @COMC_WELD_SEQ0     INTEGER,         -- 6����SEQ
    IN  @COMC_KIND_GUBN     VARCHAR(8),      -- 7���������з��ڵ�
    IN  @COMC_KIND_CODE     VARCHAR(8),      -- 8��������
    IN  @COMC_GUBN_GUBN     VARCHAR(8),      -- 9�������кз��ڵ�
    IN  @COMC_GUBN_CODE     VARCHAR(8),      -- 10��������
    IN  @COMC_SIZE_GUBN     VARCHAR(8),      -- 11��ǰSIZE�з��ڵ�
    IN  @COMC_SIZE_CODE     VARCHAR(8),      -- 12��ǰSIZE
    IN  @COMC_ICNT_GUBN     VARCHAR(8),      -- 13��ǰ�� �з��ڵ�
    IN  @COMC_ICNT_CODE     VARCHAR(8),      -- 14��ǰ��
    IN  @COMC_JIG0_YN       VARCHAR(1),      -- 15����JIG
    IN  @COMC_WELD_PONT     DECIMAL(10,2),   -- 16��������
    IN  @COMC_HWSP_CNT0     DECIMAL(10,2),   -- 17H/W����
    IN  @COMC_BASE_GUBN     VARCHAR(8),      -- 18�������� �з��ڵ�
    IN  @COMC_BASE_CODE     VARCHAR(8),      -- 19��������
    IN  @COMC_THIK_GUBN     VARCHAR(8),      -- 20������ǵβ��з��ڵ�
    IN  @COMC_THIK_CODE     VARCHAR(8),      -- 21������ǵβ�
    IN  @COMC_WELD_TERM     DECIMAL(10,2),   -- 22��������
    IN  @COMC_WELD_LENG     DECIMAL(10,2),   -- 23��������
    IN  @COMC_POND_CNT0     DECIMAL(10,2),   -- 24���ѷ�������
    IN  @COMC_INDS_CODE     VARCHAR(2),      -- 25�������

    IN  @COMC_SHET_GUBN     VARCHAR(8),      -- 26�����ż�����    
    IN  @COMC_SHET_CODE     VARCHAR(8),      -- 27�����ż�    
    IN  @COMC_BLNK_LENG     DECIMAL(10,2),   -- 28BLANK����   
    IN  @COMC_BLNK_CAVT     INTEGER,         -- 29BLANK CVT   
    IN  @COMC_PROD_CNT0     INTEGER,         -- 30�Ⱓ���갹��
    IN  @COMC_PART_LENG     DECIMAL(10,2),   -- 31��ǰ����    
    IN  @COMC_TWBL_WIDT     DECIMAL(10,2),   -- 32�庯        
    IN  @COMC_TWBL_LENG     DECIMAL(10,2),   -- 33�ܺ�        

    IN  @COMC_MATL_STND1    CHAR(1),         -- 34 ���ܰ�ǥ�ر���        
    IN  @COMC_MATL_CODE1    VARCHAR(12),     -- 35 �����ڵ�                
    IN  @COMC_MATL_APDT1    CHAR(8),         -- 36 ������                  
    IN  @COMC_MATL_IMSI1    CHAR(4),         -- 37 ���ܰ��ӽñ���        
    IN  @COMC_NET0_WEGT1    DECIMAL(10,2),   -- 38 NET�߷�                 

    IN  @COMC_MATL_STND2    CHAR(1),         -- 39 ���ܰ�ǥ�ر���        
    IN  @COMC_MATL_CODE2    VARCHAR(12),     -- 40 �����ڵ�                
    IN  @COMC_MATL_APDT2    CHAR(8),         -- 41 ������                  
    IN  @COMC_MATL_IMSI2    CHAR(4),         -- 42 ���ܰ��ӽñ���        
    IN  @COMC_NET0_WEGT2    DECIMAL(10,2),   -- 43 NET�߷�                 

    IN  @COMC_MATL_STND3    CHAR(1),         -- 44 ���ܰ�ǥ�ر���        
    IN  @COMC_MATL_CODE3    VARCHAR(12),     -- 45 �����ڵ�                
    IN  @COMC_MATL_APDT3    CHAR(8),         -- 46 ������                  
    IN  @COMC_MATL_IMSI3    CHAR(4),         -- 47 ���ܰ��ӽñ���        
    IN  @COMC_NET0_WEGT3    DECIMAL(10,2),   -- 48 NET�߷�                 

    IN  @COMC_MATL_STND4    CHAR(1),         -- 49 ���ܰ�ǥ�ر���        
    IN  @COMC_MATL_CODE4    VARCHAR(12),     -- 50 �����ڵ�                
    IN  @COMC_MATL_APDT4    CHAR(8),         -- 51 ������                  
    IN  @COMC_MATL_IMSI4    CHAR(4),         -- 52 ���ܰ��ӽñ���        
    IN  @COMC_NET0_WEGT4    DECIMAL(10,2),   -- 53 NET�߷�                 

    OUT @COMT_MESG_CODE     VARCHAR(10),     -- 54 
    OUT @COMT_MESG          VARCHAR(3000),   -- 55
    OUT @COMT_SQL0_CODE     VARCHAR(100),    -- 56
    OUT @COMT_PRGM_ID       VARCHAR(10),     -- 57
    OUT @COMT_LINE_NO       VARCHAR(10)      -- 58
)

LANGUAGE SQL
SPECIFIC RMSFLE.SPRAQ1SA011
MODIFIES SQL DATA
SET OPTION COMMIT = *CHG

BEGIN
    --------------------------------------------------------
    -- ���� ����
    --------------------------------------------------------
    DECLARE TODAY               CHAR(8)          DEFAULT '';
    DECLARE TITLE               VARCHAR(20)      DEFAULT '';
    DECLARE EXCEPTION_CODE      VARCHAR(100)     DEFAULT '';
    DECLARE EXCEPTION_TEXT      VARCHAR(2000)    DEFAULT '';

    --------------------------------------------------------
    -- EXCEPTION ����
    --------------------------------------------------------
    DECLARE EXIT HANDLER FOR SQLSTATE 'UE002' BEGIN
        SET @COMT_MESG_CODE = 'SA-002';
    END;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN
        GET DIAGNOSTICS EXCEPTION 1 EXCEPTION_CODE = DB2_MESSAGE_ID;
        GET DIAGNOSTICS EXCEPTION 1 EXCEPTION_TEXT = MESSAGE_TEXT;

        SET @COMT_MESG_CODE = 'SA-003';
        SET @COMT_MESG      = 'MD000103 : ' || EXCEPTION_TEXT; -- DB �����߻�
        SET @COMT_SQL0_CODE = EXCEPTION_CODE;
    END;

    --------------------------------------------------------
    -- ���� ����
    --------------------------------------------------------
    SET @COMT_MESG_CODE         = 'SA-002';
    SET @COMT_MESG              = 'M1900147'; -- ������ �����Ͽ����ϴ�.
    SET @COMT_SQL0_CODE         = '0';
    SET @COMT_PRGM_ID           = 'SPRAQ1SA01';
    SET @COMT_LINE_NO           = 'ST-10000';

    SELECT REPLACE(CHAR(CURDATE()),'-','') INTO TODAY FROM SYSIBM.SYSDUMMY1;

    --------------------------------------------------------
    -- ���� ó��
    --------------------------------------------------------

    SET @COMT_LINE_NO = 'ST-11000';
    IF @COMC_WELD_SEQ0 = 0 THEN
    	SELECT VALUE(MAX(WELI_WELD_SEQ0),0) + 1
    	  INTO @COMC_WELD_SEQ0
          FROM RMSFLE.PFRAWELI
         WHERE WELI_CORP_CODE = @COMH_CORP_CODE
           AND WELI_PART_NO   = @SPMS_PART_NO
           AND WELI_EONO      = @SPMS_EONO
           AND WELI_CUMT_NO   = @SPMS_CUMT_NO
        ;
    END IF;


    SET @COMT_LINE_NO = 'ST-20000';
    IF EXISTS(SELECT *
                FROM RMSFLE.PFRAWELI
               WHERE WELI_CORP_CODE = @COMH_CORP_CODE
                 AND WELI_PART_NO   = @SPMS_PART_NO
                 AND WELI_EONO      = @SPMS_EONO
                 AND WELI_CUMT_NO   = @SPMS_CUMT_NO
                 AND WELI_WELD_SEQ0 = @COMC_WELD_SEQ0
              )
    THEN
    	SET @COMT_MESG = 'M1100302 WELD_SEQ0:' || @COMC_WELD_SEQ0; --�� ��ϵ� �����Ͱ� �����մϴ�.
        SIGNAL SQLSTATE 'UE002';
    END IF;


    -- ������ �Է��� INSERT
    SET @COMT_LINE_NO = 'ST-30000';
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

         ,WELI_SHET_GUBN 
         ,WELI_SHET_CODE
         ,WELI_BLNK_LENG
         ,WELI_BLNK_CAVT
         ,WELI_PROD_CNT0
         ,WELI_PART_LENG
         ,WELI_TWBL_WIDT
         ,WELI_TWBL_LENG

         ,WELI_MATL_STND1
         ,WELI_MATL_CODE1
         ,WELI_MATL_APDT1
         ,WELI_MATL_IMSI1
         ,WELI_NET0_WEGT1

         ,WELI_MATL_STND2
         ,WELI_MATL_CODE2
         ,WELI_MATL_APDT2
         ,WELI_MATL_IMSI2
         ,WELI_NET0_WEGT2

         ,WELI_MATL_STND3
         ,WELI_MATL_CODE3
         ,WELI_MATL_APDT3
         ,WELI_MATL_IMSI3
         ,WELI_NET0_WEGT3
         
         ,WELI_MATL_STND4
         ,WELI_MATL_CODE4
         ,WELI_MATL_APDT4
         ,WELI_MATL_IMSI4
         ,WELI_NET0_WEGT4

         ,WELI_UPDT_CMAN
         ,WELI_UPDT_DATE
         ,WELI_INIT_CMAN
         ,WELI_INIT_DATE
      ) VALUES (
          @COMH_CORP_CODE
         ,@SPMS_PART_NO
         ,@SPMS_EONO
         ,@SPMS_CUMT_NO
         ,@COMC_WELD_SEQ0
         ,@COMC_KIND_GUBN
         ,@COMC_KIND_CODE
         ,@COMC_GUBN_GUBN
         ,@COMC_GUBN_CODE
         ,@COMC_SIZE_GUBN
         ,@COMC_SIZE_CODE
         ,@COMC_ICNT_GUBN
         ,@COMC_ICNT_CODE
         ,@COMC_JIG0_YN
         ,@COMC_WELD_PONT
         ,@COMC_HWSP_CNT0
         ,@COMC_BASE_GUBN
         ,@COMC_BASE_CODE
         ,@COMC_THIK_GUBN
         ,@COMC_THIK_CODE
         ,@COMC_WELD_TERM
         ,@COMC_WELD_LENG
         ,@COMC_POND_CNT0
	     ,@COMC_INDS_CODE

         ,@COMC_SHET_GUBN
         ,@COMC_SHET_CODE
         ,@COMC_BLNK_LENG
         ,@COMC_BLNK_CAVT
         ,@COMC_PROD_CNT0
         ,@COMC_PART_LENG
         ,@COMC_TWBL_WIDT
         ,@COMC_TWBL_LENG

         ,@COMC_MATL_STND1
         ,@COMC_MATL_CODE1
         ,@COMC_MATL_APDT1
         ,@COMC_MATL_IMSI1
         ,@COMC_NET0_WEGT1

         ,@COMC_MATL_STND2
         ,@COMC_MATL_CODE2
         ,@COMC_MATL_APDT2
         ,@COMC_MATL_IMSI2
         ,@COMC_NET0_WEGT2

         ,@COMC_MATL_STND3
         ,@COMC_MATL_CODE3
         ,@COMC_MATL_APDT3
         ,@COMC_MATL_IMSI3
         ,@COMC_NET0_WEGT3

         ,@COMC_MATL_STND4
         ,@COMC_MATL_CODE4
         ,@COMC_MATL_APDT4
         ,@COMC_MATL_IMSI4
         ,@COMC_NET0_WEGT4

         ,@COMH_SABN
         ,TODAY
         ,@COMH_SABN
         ,TODAY
      );


    SET @COMT_MESG_CODE = 'SA-001' ;
    SET @COMT_MESG      = 'M1900169'; --���������� ����Ǿ����ϴ�.

END
