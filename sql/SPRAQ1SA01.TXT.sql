/** 자동공법(용접류) - 입력저장 **/

/**
 * [개정이력] 2009.08.05 : @COMT_SQL0_CODE 자리수를 작게 잡아 Host Variable..오류 나던것 수정함. [P.S.R]
 *            2012.09.14 : @COMC_INDS_CODE 추가
 */

CREATE PROCEDURE RMSFLE.SPRAQ1SA011
(
    IN  @COMH_SABN          VARCHAR(8),      -- 1사번
    IN  @COMH_CORP_CODE     CHAR(4),         -- 2법인코드
    IN  @SPMS_PART_NO       VARCHAR(21),     -- 3PART NO
    IN  @SPMS_EONO          VARCHAR(10),     -- 4EO NO
    IN  @SPMS_CUMT_NO       INTEGER,         -- 5계산번호
    IN  @COMC_WELD_SEQ0     INTEGER,         -- 6용접SEQ
    IN  @COMC_KIND_GUBN     VARCHAR(8),      -- 7용접종류분류코드
    IN  @COMC_KIND_CODE     VARCHAR(8),      -- 8용접종류
    IN  @COMC_GUBN_GUBN     VARCHAR(8),      -- 9용접구분분류코드
    IN  @COMC_GUBN_CODE     VARCHAR(8),      -- 10용접구분
    IN  @COMC_SIZE_GUBN     VARCHAR(8),      -- 11제품SIZE분류코드
    IN  @COMC_SIZE_CODE     VARCHAR(8),      -- 12제품SIZE
    IN  @COMC_ICNT_GUBN     VARCHAR(8),      -- 13부품수 분류코드
    IN  @COMC_ICNT_CODE     VARCHAR(8),      -- 14부품수
    IN  @COMC_JIG0_YN       VARCHAR(1),      -- 15용접JIG
    IN  @COMC_WELD_PONT     DECIMAL(10,2),   -- 16용접점수
    IN  @COMC_HWSP_CNT0     DECIMAL(10,2),   -- 17H/W사양수
    IN  @COMC_BASE_GUBN     VARCHAR(8),      -- 18모재재질 분류코드
    IN  @COMC_BASE_CODE     VARCHAR(8),      -- 19모재재질
    IN  @COMC_THIK_GUBN     VARCHAR(8),      -- 20소재박판두께분류코드
    IN  @COMC_THIK_CODE     VARCHAR(8),      -- 21소재박판두께
    IN  @COMC_WELD_TERM     DECIMAL(10,2),   -- 22용접구간
    IN  @COMC_WELD_LENG     DECIMAL(10,2),   -- 23용접길이
    IN  @COMC_POND_CNT0     DECIMAL(10,2),   -- 24전둘레용접수
    IN  @COMC_INDS_CODE     VARCHAR(2),      -- 25적용업종

    IN  @COMC_SHET_GUBN     VARCHAR(8),      -- 26용접매수구분    
    IN  @COMC_SHET_CODE     VARCHAR(8),      -- 27용접매수    
    IN  @COMC_BLNK_LENG     DECIMAL(10,2),   -- 28BLANK길이   
    IN  @COMC_BLNK_CAVT     INTEGER,         -- 29BLANK CVT   
    IN  @COMC_PROD_CNT0     INTEGER,         -- 30년간생산갯수
    IN  @COMC_PART_LENG     DECIMAL(10,2),   -- 31부품길이    
    IN  @COMC_TWBL_WIDT     DECIMAL(10,2),   -- 32장변        
    IN  @COMC_TWBL_LENG     DECIMAL(10,2),   -- 33단변        

    IN  @COMC_MATL_STND1    CHAR(1),         -- 34 재료단가표준구분        
    IN  @COMC_MATL_CODE1    VARCHAR(12),     -- 35 재질코드                
    IN  @COMC_MATL_APDT1    CHAR(8),         -- 36 적용일                  
    IN  @COMC_MATL_IMSI1    CHAR(4),         -- 37 재료단가임시구분        
    IN  @COMC_NET0_WEGT1    DECIMAL(10,2),   -- 38 NET중량                 

    IN  @COMC_MATL_STND2    CHAR(1),         -- 39 재료단가표준구분        
    IN  @COMC_MATL_CODE2    VARCHAR(12),     -- 40 재질코드                
    IN  @COMC_MATL_APDT2    CHAR(8),         -- 41 적용일                  
    IN  @COMC_MATL_IMSI2    CHAR(4),         -- 42 재료단가임시구분        
    IN  @COMC_NET0_WEGT2    DECIMAL(10,2),   -- 43 NET중량                 

    IN  @COMC_MATL_STND3    CHAR(1),         -- 44 재료단가표준구분        
    IN  @COMC_MATL_CODE3    VARCHAR(12),     -- 45 재질코드                
    IN  @COMC_MATL_APDT3    CHAR(8),         -- 46 적용일                  
    IN  @COMC_MATL_IMSI3    CHAR(4),         -- 47 재료단가임시구분        
    IN  @COMC_NET0_WEGT3    DECIMAL(10,2),   -- 48 NET중량                 

    IN  @COMC_MATL_STND4    CHAR(1),         -- 49 재료단가표준구분        
    IN  @COMC_MATL_CODE4    VARCHAR(12),     -- 50 재질코드                
    IN  @COMC_MATL_APDT4    CHAR(8),         -- 51 적용일                  
    IN  @COMC_MATL_IMSI4    CHAR(4),         -- 52 재료단가임시구분        
    IN  @COMC_NET0_WEGT4    DECIMAL(10,2),   -- 53 NET중량                 

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
    -- 변수 선언
    --------------------------------------------------------
    DECLARE TODAY               CHAR(8)          DEFAULT '';
    DECLARE TITLE               VARCHAR(20)      DEFAULT '';
    DECLARE EXCEPTION_CODE      VARCHAR(100)     DEFAULT '';
    DECLARE EXCEPTION_TEXT      VARCHAR(2000)    DEFAULT '';

    --------------------------------------------------------
    -- EXCEPTION 선언
    --------------------------------------------------------
    DECLARE EXIT HANDLER FOR SQLSTATE 'UE002' BEGIN
        SET @COMT_MESG_CODE = 'SA-002';
    END;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN
        GET DIAGNOSTICS EXCEPTION 1 EXCEPTION_CODE = DB2_MESSAGE_ID;
        GET DIAGNOSTICS EXCEPTION 1 EXCEPTION_TEXT = MESSAGE_TEXT;

        SET @COMT_MESG_CODE = 'SA-003';
        SET @COMT_MESG      = 'MD000103 : ' || EXCEPTION_TEXT; -- DB 오류발생
        SET @COMT_SQL0_CODE = EXCEPTION_CODE;
    END;

    --------------------------------------------------------
    -- 공통 실행
    --------------------------------------------------------
    SET @COMT_MESG_CODE         = 'SA-002';
    SET @COMT_MESG              = 'M1900147'; -- 저장을 실패하였습니다.
    SET @COMT_SQL0_CODE         = '0';
    SET @COMT_PRGM_ID           = 'SPRAQ1SA01';
    SET @COMT_LINE_NO           = 'ST-10000';

    SELECT REPLACE(CHAR(CURDATE()),'-','') INTO TODAY FROM SYSIBM.SYSDUMMY1;

    --------------------------------------------------------
    -- 로직 처리
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
    	SET @COMT_MESG = 'M1100302 WELD_SEQ0:' || @COMC_WELD_SEQ0; --기 등록된 데이터가 존재합니다.
        SIGNAL SQLSTATE 'UE002';
    END IF;


    -- 용접류 입력을 INSERT
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
    SET @COMT_MESG      = 'M1900169'; --정상적으로 저장되었습니다.

END
