------------------------------------------------------------------------------------------
-- NAME
--    CreateDB-TW_SYS.sql
-- DESCRIPTION
--    데이터베이스 만들기
-- NOTE
--    1. 트랜잭션 로그 파일은 로그 전용 드라이브에 배치한다.
--    2. 데이터베이스를 만들 때 향후 예상되는 데이터 크리르 고려하여 충분한 크기로 생성
--    3. FILENAME부분을 자신의 환경에 맞게 고친다.
-- MODIFIED
--    [2010-06-09] created by Myungho Lee
------------------------------------------------------------------------------------------
CREATE DATABASE [TW_SYS] ON PRIMARY 
( 
		NAME       = N'TW_SYS_Data'
	,	FILENAME   = N'D:\DATA\TW_SYS.MDF' 
	,	SIZE       = 512 MB 
	,	MAXSIZE    = UNLIMITED
	,	FILEGROWTH = 10  MB
)
LOG ON 
( 
		NAME       = N'TW_SYS_Log'
	,	FILENAME   = N'D:\DATA\TW_SYS.LDF' 
	,	SIZE       = 512 MB 
	,	MAXSIZE    = UNLIMITED
	,	FILEGROWTH = 10  MB
)
GO

