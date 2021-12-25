------------------------------------------------------------------------------------------
-- NAME
--    CreateDB-TW_SYS.sql
-- DESCRIPTION
--    �����ͺ��̽� �����
-- NOTE
--    1. Ʈ����� �α� ������ �α� ���� ����̺꿡 ��ġ�Ѵ�.
--    2. �����ͺ��̽��� ���� �� ���� ����Ǵ� ������ ũ���� ����Ͽ� ����� ũ��� ����
--    3. FILENAME�κ��� �ڽ��� ȯ�濡 �°� ��ģ��.
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

